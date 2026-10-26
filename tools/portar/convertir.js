#!/usr/bin/env node
// Porta los recursos de Artinna Re Fisura del repo 1.21.1 (ORIGEN) a este repo 26.1.2 (DESTINO).
//
// Uso (desde la raiz del repo 26.1.2):
//   node tools/portar/convertir.js [--origen <repo 1.21.1>] [--sounds-generated]
//
// Que hace:
//  - Copia texturas, sonidos (.ogg), modelos, blockstates, el libro de Patchouli, loot, tags,
//    advancements y el worldgen del arbol de yerba a src/main/resources (estatico).
//  - Convierte lo que cambio de formato en 26.1.2 (todo verificado contra el jar de vanilla
//    26.1.2, el de NeoForge 26.1.2.100, el del port de Farmer's Delight 26 y el de Patchouli 26.1-94):
//      * recetas: ingredientes como "id" / "#tag" / lista de ids; custom ingredients con
//        "neoforge:ingredient_type"; el compound solo queda cuando mezcla tags (no hay otra forma).
//      * worldgen: el arbol ya no tiene dirt_provider/force_dirt sino below_trunk_provider.
//      * modelos: "render_type" ya no existe (la capa sale sola de la transparencia de la textura).
//      * el sol vive en textures/environment/celestial/sun.png (atlas "celestials").
//      * los modelos de bloque solo pueden usar texturas del atlas de bloques: los del mate puesto
//        que usaban texturas de item/ pasan a usar copias en textures/block/puesto/.
//  - Crea assets/ultimatefoods/items/<id>.json (definicion de item nueva) para cada item.
//  - Crea los tradeos del yerbatero como datos (en 26.1.2 ya no hay VillagerTradesEvent).
//  - Mergea las claves criollas del lang del origen en los lang del destino.
//  - Nunca toca src/generated, salvo sounds.json si se pasa --sounds-generated.
//
// Es reproducible: guarda en tools/portar/generados.json lo que escribio y en cada corrida lo
// borra y lo vuelve a generar. Lo que ya existia en el destino (lo portado a mano) no se pisa.
'use strict';
const fs = require('fs');
const path = require('path');
const { execFileSync } = require('child_process');
const { listar, leerJson, escribirJson, copiar, partirId } = require('./comun');

// ---------------------------------------------------------------- configuracion
const args = process.argv.slice(2);
const opt = (n, def) => { const i = args.indexOf(n); return i >= 0 ? args[i + 1] : def; };
const ORIGEN = path.resolve(opt('--origen', 'E:/modsmios/UltimateFoodsMod'));
const DESTINO = path.resolve(__dirname, '..', '..');
const SOUNDS_GENERATED = args.includes('--sounds-generated');
const MOD = 'ultimatefoods';
/** Ultimo commit del origen antes de Artinna Re Fisura: sirve para saber que claves de lang son criollas. */
const COMMIT_PREVIO = '9fc96fe5~1';
const LOCALES = ['en_us', 'es_es', 'es_ar', 'es_mx'];

const ORIG_MAIN = path.join(ORIGEN, 'src/main/resources');
const ORIG_GEN = path.join(ORIGEN, 'src/generated/resources');
const DEST_MAIN = path.join(DESTINO, 'src/main/resources');
const DEST_GEN = path.join(DESTINO, 'src/generated/resources');
const MANIFIESTO = path.join(__dirname, 'generados.json');

if (!fs.existsSync(ORIG_GEN)) throw new Error('No encuentro el origen en ' + ORIGEN);

const reporte = { copiados: 0, convertidos: {}, yaEstaban: [], distintos: [], avisos: [] };
const conto = (k) => { reporte.convertidos[k] = (reporte.convertidos[k] || 0) + 1; };
const generados = new Set();

// ---------------------------------------------------------------- limpieza de la corrida anterior
if (fs.existsSync(MANIFIESTO)) {
    for (const rel of leerJson(MANIFIESTO).archivos || []) {
        const abs = path.join(DESTINO, rel);
        if (fs.existsSync(abs)) fs.unlinkSync(abs);
    }
    // borra carpetas que quedaron vacias
    const vaciar = (dir) => {
        if (!fs.existsSync(dir)) return;
        for (const f of fs.readdirSync(dir)) {
            const p = path.join(dir, f);
            if (fs.statSync(p).isDirectory()) vaciar(p);
        }
        if (fs.readdirSync(dir).length === 0) fs.rmdirSync(dir);
    };
    vaciar(path.join(DEST_MAIN, 'assets'));
    vaciar(path.join(DEST_MAIN, 'data'));
}

/** Lo que el destino ya tiene (portado a mano o por su datagen): no se pisa. */
const baseDestino = new Set([...listar(DEST_MAIN), ...listar(DEST_GEN)]);

function escribir(rel, obj) {
    escribirJson(path.join(DEST_MAIN, rel), obj);
    generados.add('src/main/resources/' + rel);
}
function copiarA(rel, abs) {
    copiar(abs, path.join(DEST_MAIN, rel));
    generados.add('src/main/resources/' + rel);
    reporte.copiados++;
}

// ---------------------------------------------------------------- archivos del origen
/** rel -> ruta absoluta (main primero, despues generated; en 1.21.1 no se pisan). */
const origen = new Map();
for (const [raiz, nombre] of [[ORIG_MAIN, 'main'], [ORIG_GEN, 'generated']]) {
    for (const rel of listar(raiz)) {
        if (!rel.startsWith('assets/') && !rel.startsWith('data/')) continue;
        if (origen.has(rel)) reporte.avisos.push(`En el origen ${rel} esta en main y en generated: se usa el de main`);
        else origen.set(rel, path.join(raiz, rel));
    }
}

// ---------------------------------------------------------------- recetas
/** Ingrediente de 1.21.1 -> formato 26.1.2. */
function ingrediente(x, donde) {
    if (typeof x === 'string') return x;
    if (Array.isArray(x)) {
        const hijos = x.map((h) => ingrediente(h, donde));
        if (hijos.length === 1) return hijos[0];
        // una lista de ids sueltos es un HolderSet valido; si hay tags o custom hace falta el compound
        if (hijos.every((h) => typeof h === 'string' && !h.startsWith('#'))) return hijos;
        const planos = [];
        for (const h of hijos) Array.isArray(h) ? planos.push(...h) : planos.push(h);
        return { 'neoforge:ingredient_type': 'neoforge:compound', children: planos };
    }
    if (x && typeof x === 'object') {
        const claves = Object.keys(x);
        if (claves.length === 1 && typeof x.item === 'string') return x.item;
        if (claves.length === 1 && typeof x.tag === 'string') return '#' + x.tag;
        if (x.type === 'neoforge:compound' || x['neoforge:ingredient_type'] === 'neoforge:compound') {
            return ingrediente(x.children, donde);
        }
        if (typeof x['neoforge:ingredient_type'] === 'string') return x;
        if (typeof x.type === 'string') {
            const { type, ...resto } = x;
            return { 'neoforge:ingredient_type': type, ...resto };
        }
    }
    throw new Error(`Ingrediente que no se convertir en ${donde}: ${JSON.stringify(x)}`);
}

/** Resultado: el count 1 se omite, como hace el datagen de 26.1.2. */
function resultado(r) {
    if (r && typeof r === 'object' && !Array.isArray(r) && typeof r.id === 'string') {
        const out = { ...r };
        if (out.count === 1) delete out.count;
        return out;
    }
    return r;
}

function receta(j, rel) {
    const r = { ...j };
    switch (r.type) {
        case 'minecraft:crafting_shaped':
            r.key = Object.fromEntries(Object.entries(r.key).map(([k, v]) => [k, ingrediente(v, rel)]));
            r.result = resultado(r.result);
            break;
        case 'minecraft:crafting_shapeless':
        case 'farmersdelight:cooking':
        case `${MOD}:cacerola`:
            r.ingredients = r.ingredients.map((i) => ingrediente(i, rel));
            r.result = resultado(r.result);
            break;
        case 'minecraft:smelting':
        case 'minecraft:smoking':
        case 'minecraft:blasting':
        case 'minecraft:campfire_cooking':
        case 'minecraft:stonecutting':
            r.ingredient = ingrediente(r.ingredient, rel);
            r.result = resultado(r.result);
            break;
        case 'farmersdelight:cutting':
            // mismo formato que las recetas del port de FD 26: tool es UN ingrediente (compound)
            r.ingredients = r.ingredients.map((i) => ingrediente(i, rel));
            r.tool = ingrediente(r.tool, rel);
            break;
        default:
            throw new Error(`Tipo de receta sin conversion: ${r.type} (${rel})`);
    }
    conto('recetas');
    return r;
}

// ---------------------------------------------------------------- worldgen
/** below_trunk_provider igual al del roble de vanilla 26.1.2 (configured_feature/oak.json). */
function debajoDelTronco(estado) {
    return {
        type: 'minecraft:rule_based_state_provider',
        rules: [{
            if_true: {
                type: 'minecraft:not',
                predicate: { type: 'minecraft:matching_block_tag', tag: 'minecraft:cannot_replace_below_tree_trunk' },
            },
            then: { type: 'minecraft:simple_state_provider', state: estado },
        }],
    };
}

function featureConfigurada(j) {
    if (j.type !== 'minecraft:tree' || !j.config || !('dirt_provider' in j.config)) return j;
    const { dirt_provider, force_dirt, ...resto } = j.config;
    const estado = dirt_provider && dirt_provider.state ? dirt_provider.state : { Name: 'minecraft:dirt' };
    const below = force_dirt ? dirt_provider : debajoDelTronco(estado);
    // mismo orden de claves que vanilla
    const config = {};
    config.below_trunk_provider = below;
    for (const [k, v] of Object.entries(resto)) config[k] = v;
    conto('worldgen (dirt_provider -> below_trunk_provider)');
    return { ...j, config };
}

// ---------------------------------------------------------------- modelos
function modelo(j) {
    if ('render_type' in j) {
        const { render_type, ...resto } = j;
        conto('modelos sin render_type');
        return resto;
    }
    return j;
}

// ---------------------------------------------------------------- pasada principal
const modelosSalida = new Map(); // rel -> json
const blockstatesSalida = new Map(); // rel -> json
const langOrigen = new Map(); // locale -> json
let soundsOrigen = null;

for (const [rel, abs] of origen) {
    if (/^assets\/[^/]+\/lang\/[^/]+\.json$/.test(rel)) {
        langOrigen.set(path.basename(rel, '.json'), leerJson(abs));
        continue;
    }
    if (rel === `assets/${MOD}/sounds.json`) { soundsOrigen = leerJson(abs); continue; }

    let destRel = rel;
    if (rel === 'assets/minecraft/textures/environment/sun.png') {
        destRel = 'assets/minecraft/textures/environment/celestial/sun.png';
        conto('sol movido a environment/celestial');
    }

    if (baseDestino.has(destRel)) {
        const otro = fs.existsSync(path.join(DEST_MAIN, destRel)) ? path.join(DEST_MAIN, destRel) : path.join(DEST_GEN, destRel);
        let igual = Buffer.compare(fs.readFileSync(abs), fs.readFileSync(otro)) === 0;
        if (!igual && rel.endsWith('.json')) {
            // mismo contenido con otro formato (CRLF, sangria, orden de claves) cuenta como igual
            const norm = (x) => JSON.stringify(x, (k, v) => (v && typeof v === 'object' && !Array.isArray(v)
                ? Object.fromEntries(Object.keys(v).sort().map((c) => [c, v[c]])) : v));
            igual = norm(leerJson(abs)) === norm(leerJson(otro));
        }
        (igual ? reporte.yaEstaban : reporte.distintos).push(destRel);
        continue;
    }

    if (!rel.endsWith('.json')) { copiarA(destRel, abs); continue; }

    const j = leerJson(abs);
    if (/^assets\/[^/]+\/models\//.test(rel)) modelosSalida.set(destRel, modelo(j));
    else if (/^assets\/[^/]+\/blockstates\//.test(rel)) blockstatesSalida.set(destRel, j);
    else if (/^data\/[^/]+\/recipe\//.test(rel)) escribir(destRel, receta(j, rel));
    else if (/^data\/[^/]+\/worldgen\/configured_feature\//.test(rel)) escribir(destRel, featureConfigurada(j));
    else { copiarA(destRel, abs); }
}

// ---------------------------------------------------------------- modelos de bloque con texturas de item
// En 26.1.2 hay un atlas de bloques y otro de items, y SimpleModelWrapper rechaza ("Rejecting block
// model ... sprites from outside of supported atlas") cualquier modelo de blockstate que use una textura
// que no este en el de bloques. El mate puesto usaba los modelos/texturas de item: se hacen copias.
function buscarModelo(id) {
    const [ns, p] = partirId(id);
    const rel = `assets/${ns}/models/${p}.json`;
    if (modelosSalida.has(rel)) return { rel, json: modelosSalida.get(rel), nuestro: true };
    for (const raiz of [DEST_MAIN, DEST_GEN]) {
        const abs = path.join(raiz, rel);
        if (fs.existsSync(abs)) return { rel, json: leerJson(abs), nuestro: false };
    }
    return null;
}
function texturasResueltas(id, prof = 0) {
    const m = buscarModelo(id);
    if (!m || prof > 20) return {};
    const base = m.json.parent ? texturasResueltas(m.json.parent, prof + 1) : {};
    return Object.assign(base, m.json.textures || {});
}
const esTexturaItem = (t) => typeof t === 'string' && new RegExp(`^${MOD}:item/`).test(t);
const texturaBloque = (t) => t.replace(`${MOD}:item/`, `${MOD}:block/puesto/`);

const remapeados = new Map(); // id viejo -> id nuevo
function modeloParaBloque(id) {
    if (remapeados.has(id)) return remapeados.get(id);
    const tex = texturasResueltas(id);
    if (!Object.values(tex).some(esTexturaItem)) { remapeados.set(id, id); return id; }
    const m = buscarModelo(id);
    const nuevasTex = {};
    for (const [k, v] of Object.entries(tex)) {
        if (!esTexturaItem(v)) { nuevasTex[k] = v; continue; }
        nuevasTex[k] = texturaBloque(v);
        const p = partirId(v)[1];
        const desde = origen.get(`assets/${MOD}/textures/${p}.png`) || path.join(DEST_MAIN, `assets/${MOD}/textures/${p}.png`);
        const relTex = `assets/${MOD}/textures/block/puesto/${p.slice('item/'.length)}.png`;
        if (!generados.has('src/main/resources/' + relTex)) {
            copiarA(relTex, desde);
            const meta = desde + '.mcmeta';
            if (fs.existsSync(meta)) copiarA(relTex + '.mcmeta', meta);
        }
    }
    const [ns, p] = partirId(id);
    let nuevoId = id;
    let rel = m.rel;
    if (p.startsWith('item/') || !m.nuestro) {
        nuevoId = `${ns}:block/puesto/${p.replace(/^item\//, '')}`;
        rel = `assets/${ns}/models/${partirId(nuevoId)[1]}.json`;
    }
    modelosSalida.set(rel, { ...m.json, textures: nuevasTex });
    conto('modelos de bloque pasados a texturas de bloque');
    remapeados.set(id, nuevoId);
    return nuevoId;
}
for (const [rel, bs] of blockstatesSalida) {
    const variantes = [];
    for (const v of Object.values(bs.variants || {})) variantes.push(...[].concat(v));
    for (const parte of bs.multipart || []) variantes.push(...[].concat(parte.apply));
    for (const v of variantes) v.model = modeloParaBloque(v.model);
    escribir(rel, bs);
}
for (const [rel, j] of modelosSalida) escribir(rel, j);

// ---------------------------------------------------------------- definiciones de item (items/<id>.json)
const idsItems = new Set();
for (const rel of origen.keys()) {
    const m = new RegExp(`^assets/${MOD}/models/item/([^/]+)\\.json$`).exec(rel);
    if (m) idsItems.add(m[1]);
}
for (const id of [...idsItems].sort()) {
    const rel = `assets/${MOD}/items/${id}.json`;
    if (baseDestino.has(rel)) continue;
    // guia_buela no es un item registrado: es el "model" del book.json de Patchouli, que en
    // 26.1-94 se busca con ModelManager.getItemModel(id), o sea en items/<id>.json.
    escribir(rel, { model: { type: 'minecraft:model', model: `${MOD}:item/${id}` } });
    conto('definiciones de item creadas');
}

// ---------------------------------------------------------------- tradeos del yerbatero
// 1.21.1: MateTrades.java con VillagerTradesEvent. 26.1.2: villager_trade + tags/villager_trade +
// trade_set (como vanilla). VillagerProfession recibe un Int2ObjectMap nivel -> ResourceKey<TradeSet>
// con las claves ultimatefoods:yerbatero/level_1 .. level_5 (las tiene que usar el modulo del mate).
(function tradeos() {
    const java = path.join(ORIGEN, 'src/main/java/nadiendev/ultimatefoods/fisura/aldeano/MateTrades.java');
    if (!fs.existsSync(java)) { reporte.avisos.push('No encontre MateTrades.java en el origen: sin tradeos'); return; }
    const fuente = fs.readFileSync(java, 'utf8');
    const mult = parseFloat((/MULT\s*=\s*([\d.]+)F?/.exec(fuente) || [0, '0.05'])[1]);
    const idDe = (expr) => {
        const m = /(?:\w+Items|Items)\.(\w+)(?:\.get\(\))?/.exec(expr);
        if (!m) throw new Error('No entiendo el item del tradeo: ' + expr);
        return (expr.startsWith('Items.') ? 'minecraft:' : `${MOD}:`) + m[1].toLowerCase();
    };
    const niveles = {};
    let nivel = 0;
    for (const linea of fuente.split(/\r?\n/)) {
        const n = /trades\.get\((\d)\)/.exec(linea);
        if (n) { nivel = +n[1]; niveles[nivel] = niveles[nivel] || []; continue; }
        const c = /\.add\((venta|compra)\((.*)\)\);/.exec(linea);
        if (!c || !nivel) continue;
        const a = c[2].split(',').map((s) => s.trim());
        let wants, gives, usos, xp;
        if (c[1] === 'venta') { // venta(esmeraldas, item, cantidad, usos, xp)
            wants = { id: 'minecraft:emerald', count: +a[0] };
            gives = { id: idDe(a[1]), count: +a[2] };
            [usos, xp] = [+a[3], +a[4]];
        } else { // compra(item, cantidad, esmeraldas, usos, xp)
            wants = { id: idDe(a[0]), count: +a[1] };
            gives = { id: 'minecraft:emerald', count: +a[2] };
            [usos, xp] = [+a[3], +a[4]];
        }
        if (wants.count === 1) delete wants.count;
        if (gives.count === 1) delete gives.count;
        niveles[nivel].push({ gives, max_uses: usos, reputation_discount: mult, wants, xp });
    }
    for (const [nv, lista] of Object.entries(niveles)) {
        const ids = [];
        for (const t of lista) {
            let nombre = `${partirId(t.wants.id)[1]}_${partirId(t.gives.id)[1]}`;
            while (ids.includes(`${MOD}:yerbatero/${nv}/${nombre}`)) nombre += '_2';
            ids.push(`${MOD}:yerbatero/${nv}/${nombre}`);
            escribir(`data/${MOD}/villager_trade/yerbatero/${nv}/${nombre}.json`, t);
        }
        escribir(`data/${MOD}/tags/villager_trade/yerbatero/level_${nv}.json`, { values: ids });
        escribir(`data/${MOD}/trade_set/yerbatero/level_${nv}.json`, {
            amount: 2.0, random_sequence: `${MOD}:trade_set/yerbatero/level_${nv}`, trades: `#${MOD}:yerbatero/level_${nv}`,
        });
        conto('niveles de tradeo del yerbatero');
    }
})();

// ---------------------------------------------------------------- sounds.json
(function sonidos() {
    if (!soundsOrigen) return;
    const genAbs = path.join(DEST_GEN, `assets/${MOD}/sounds.json`);
    const mainRel = `assets/${MOD}/sounds.json`;
    if (!fs.existsSync(genAbs)) { escribir(mainRel, soundsOrigen); return; }
    const actual = leerJson(genAbs);
    const faltan = Object.keys(soundsOrigen).filter((k) => !(k in actual));
    if (!faltan.length) return;
    if (SOUNDS_GENERATED) {
        for (const k of faltan) actual[k] = soundsOrigen[k];
        escribirJson(genAbs, actual);
        reporte.avisos.push(`sounds.json (src/generated): agregados ${faltan.join(', ')}`);
    } else {
        // Un segundo sounds.json en src/main chocaria con el de src/generated en processResources
        // (entrada duplicada). Lo correcto es sumarlo en ModSoundProvider y correr runData.
        reporte.avisos.push(`sounds.json: faltan ${faltan.join(', ')} en src/generated/.../sounds.json ` +
            '(no se toca src/generated; ver VERIFICAR.md o correr con --sounds-generated)');
    }
})();

// ---------------------------------------------------------------- lang
(function lang() {
    let previo;
    try {
        previo = JSON.parse(execFileSync('git', ['-C', ORIGEN, 'show',
            `${COMMIT_PREVIO}:src/main/resources/assets/${MOD}/lang/en_us.json`], { encoding: 'utf8' }));
    } catch (e) {
        throw new Error('No pude leer el lang previo a Artinna Re Fisura del origen: ' + e.message);
    }
    const enOrigen = langOrigen.get('en_us') || {};
    const criollas = Object.keys(enOrigen).filter((k) => !(k in previo));
    const destEnAbs = path.join(DEST_MAIN, `assets/${MOD}/lang/en_us.json`);
    const delDestino = fs.existsSync(destEnAbs) ? Object.keys(leerJson(destEnAbs)) : [];
    for (const loc of LOCALES) {
        const src = langOrigen.get(loc);
        if (!src) { reporte.avisos.push(`El origen no tiene lang ${loc}`); continue; }
        const abs = path.join(DEST_MAIN, `assets/${MOD}/lang/${loc}.json`);
        const dest = fs.existsSync(abs) ? leerJson(abs) : {};
        let nuevas = 0;
        // las criollas mandan las del origen (fuente de verdad)
        for (const k of criollas) if (k in src) { if (!(k in dest)) nuevas++; dest[k] = src[k]; }
        // las de las comidas originales que ya estan en el destino: solo se completan si faltan
        for (const k of delDestino) if (!(k in dest) && k in src) { dest[k] = src[k]; nuevas++; }
        // 1.21.1 nombraba las profesiones modeadas "entity.minecraft.villager.<ns>.<id>"; en 26.1.2
        // VillagerProfession arma "entity.<ns>.villager.<id>" (como vanilla): se dejan las dos
        for (const k of Object.keys(dest)) {
            const m = /^entity\.minecraft\.villager\.([a-z0-9_]+)\.([a-z0-9_]+)$/.exec(k);
            if (m && criollas.includes(k)) {
                const nueva = `entity.${m[1]}.villager.${m[2]}`;
                if (!(nueva in dest)) nuevas++;
                dest[nueva] = dest[k];
            }
        }
        escribirJson(abs, dest);
        reporte.avisos.push(`lang ${loc}: ${nuevas} claves nuevas (${Object.keys(dest).length} en total)`);
    }
})();

// ---------------------------------------------------------------- manifiesto y resumen
escribirJson(MANIFIESTO, {
    comentario: 'Archivos escritos por tools/portar/convertir.js (se borran y regeneran en cada corrida).',
    origen: ORIGEN.replace(/\\/g, '/'),
    archivos: [...generados].sort(),
});

console.log(`Origen : ${ORIGEN}`);
console.log(`Destino: ${DESTINO}`);
console.log(`Archivos escritos: ${generados.size} (copiados tal cual: ${reporte.copiados})`);
for (const [k, v] of Object.entries(reporte.convertidos)) console.log(`  ${k}: ${v}`);
console.log(`Ya estaban en el destino e iguales: ${reporte.yaEstaban.length}`);
if (reporte.distintos.length) {
    console.log(`Ya estaban en el destino con otro contenido (se deja el del destino): ${reporte.distintos.length}`);
    for (const r of reporte.distintos) console.log('  - ' + r);
}
for (const a of reporte.avisos) console.log('AVISO: ' + a);
