#!/usr/bin/env node
// Verifica los recursos del mod en 26.1.2 (lo que dejo convertir.js y lo que ya estaba).
//
// Uso (desde la raiz del repo 26.1.2):
//   node tools/portar/verificar.js [--java <carpeta src/main/java extra>]... [--estricto]
//
// Revisa, contra este repo y los jars de vanilla 26.1.2 / NeoForge / Farmer's Delight 26 / Patchouli
// de la cache de Gradle:
//  - que toda referencia de modelo y textura exista (y que los modelos de blockstate solo usen texturas
//    del atlas de bloques, que en 26.1.2 es obligatorio);
//  - que todo id de receta, loot, tag, advancement, worldgen, tradeo y libro apunte a algo que exista;
//  - que cada item registrado tenga su items/<id>.json (y su nombre en el lang) y al reves;
//  - que no haya ingredientes con el formato viejo ({"item":..}, {"tag":..}, neoforge:compound con "type");
//  - que no haya el mismo archivo en src/main/resources y en src/generated/resources (processResources falla).
// Los items/bloques registrados se sacan del codigo Java (DeferredItem/DeferredBlock/...); con --java
// se suman otras carpetas (por ejemplo los worktrees de los otros modulos antes de mergear).
// Sale con codigo 1 si hay errores. Los avisos no cortan (con --estricto si).
'use strict';
const fs = require('fs');
const path = require('path');
const os = require('os');
const { execFileSync } = require('child_process');
const { listar, leerJson, partirId, entradasZip, jarDeCache, propiedades } = require('./comun');

const RAIZ = path.resolve(__dirname, '..', '..');
const MOD = 'ultimatefoods';
const args = process.argv.slice(2);
const ESTRICTO = args.includes('--estricto');
const javaExtra = [];
for (let i = 0; i < args.length; i++) if (args[i] === '--java') javaExtra.push(path.resolve(args[++i]));

const errores = [];
const avisos = [];
const error = (m) => errores.push(m);
const aviso = (m) => avisos.push(m);

// ---------------------------------------------------------------- recursos del repo
const MAIN = path.join(RAIZ, 'src/main/resources');
const GEN = path.join(RAIZ, 'src/generated/resources');
const archivos = new Map(); // rel -> abs
for (const raiz of [MAIN, GEN]) {
    for (const rel of listar(raiz)) {
        if (archivos.has(rel)) error(`Duplicado en src/main/resources y src/generated/resources: ${rel} (processResources falla)`);
        else archivos.set(rel, path.join(raiz, rel));
    }
}
const json = new Map();
for (const [rel, abs] of archivos) {
    if (!rel.endsWith('.json')) continue;
    try { json.set(rel, leerJson(abs)); } catch (e) { error(`JSON invalido: ${rel}: ${e.message}`); }
}
const nuestros = (re) => [...json.keys()].filter((r) => re.test(r));

// ---------------------------------------------------------------- jars de referencia
const props = propiedades(path.join(RAIZ, 'gradle.properties'));
const mc = props.minecraft_version || '26.1.2';
function clienteVanilla() {
    const cands = [path.join(RAIZ, '.gradle/caches/minecraft/versions', mc, 'client.jar')];
    try {
        const comun = execFileSync('git', ['-C', RAIZ, 'rev-parse', '--git-common-dir'], { encoding: 'utf8' }).trim();
        cands.push(path.join(path.resolve(RAIZ, comun), '..', '.gradle/caches/minecraft/versions', mc, 'client.jar'));
    } catch (e) { /* sin git */ }
    cands.push(path.join(os.homedir(), '.gradle/caches/neoformruntime/artifacts', `minecraft_${mc}_client.jar`));
    return cands.find((c) => fs.existsSync(c)) || null;
}
const jars = {
    vanilla: clienteVanilla(),
    neoforge: jarDeCache('net.neoforged', 'neoforge', props.neo_version || '', '-universal.jar'),
    farmersdelight: props.farmersdelight_file ? jarDeCache('farmersdelight-port', props.farmersdelight_file, props.farmersdelight_tag) : null,
    patchouli: jarDeCache('vazkii.patchouli', 'patchouli-neoforge', props.patchouli_version || ''),
    patchouliXplat: jarDeCache('vazkii.patchouli', 'patchouli-xplat', props.patchouli_version || ''),
};
const externos = new Set(); // rutas de recursos (assets/... data/...) que estan en algun jar
for (const [nombre, jar] of Object.entries(jars)) {
    if (!jar) { aviso(`No encontre el jar de ${nombre} en la cache de Gradle: lo que dependa de el no se verifica`); continue; }
    for (const n of entradasZip(jar).nombres) if (n.startsWith('assets/') || n.startsWith('data/')) externos.add(n);
}
const hayJar = (n) => !!jars[n];
const existe = (rel) => archivos.has(rel) || externos.has(rel);

// ---------------------------------------------------------------- lo registrado en Java
const carpetasJava = [path.join(RAIZ, 'src/main/java'), ...javaExtra];
const registrados = { item: new Set(), block: new Set(), sound: new Set(), poi: new Set(), component: new Set() };
(function escanearJava() {
    const constBloques = new Map();
    const pendientes = [];
    for (const raiz of carpetasJava) {
        for (const rel of listar(raiz).filter((r) => r.endsWith('.java'))) {
            const src = fs.readFileSync(path.join(raiz, rel), 'utf8').replace(/\/\*[\s\S]*?\*\/|\/\/.*$/gm, '');
            for (const sent of src.split(';')) {
                const m = /static\s+final\s+(Deferred\w+)\s*<([^=]*?)>\s+(\w+)\s*=\s*([\s\S]*)$/.exec(sent);
                if (!m) continue;
                const [, clase, gen, nombre, ini] = m;
                const lit = /"([a-z0-9_./-]+)"/.exec(ini);
                let tipo = null;
                if (clase === 'DeferredItem' || /^\s*Item\b|^\s*Item\s*,/.test(gen)) tipo = 'item';
                else if (clase === 'DeferredBlock' || /^\s*Block\b/.test(gen)) tipo = 'block';
                else if (/^\s*SoundEvent\b/.test(gen)) tipo = 'sound';
                else if (/^\s*PoiType\b/.test(gen)) tipo = 'poi';
                else if (/^\s*DataComponentType\b/.test(gen)) tipo = 'component';
                if (!tipo) continue;
                if (lit) {
                    registrados[tipo].add(lit[1]);
                    if (tipo === 'block') constBloques.set(nombre, lit[1]);
                } else if (tipo === 'item') {
                    const b = /registerSimpleBlockItem\(\s*(?:\w+\.)?(\w+)/.exec(ini);
                    if (b) pendientes.push([b[1], rel]);
                    else aviso(`No pude sacar el id del item ${nombre} (${rel})`);
                }
            }
        }
    }
    for (const [c, rel] of pendientes) {
        if (constBloques.has(c)) registrados.item.add(constBloques.get(c));
        else aviso(`registerSimpleBlockItem(${c}) sin bloque conocido (${rel})`);
    }
})();
const codigoListo = registrados.item.size > 0;

// ---------------------------------------------------------------- consultas de existencia
function itemExiste(id) {
    const [ns, p] = partirId(id);
    if (ns === MOD) return registrados.item.has(p);
    if (ns === 'minecraft') return !hayJar('vanilla') || externos.has(`assets/minecraft/items/${p}.json`);
    if (ns === 'farmersdelight') return !hayJar('farmersdelight') || externos.has(`assets/farmersdelight/items/${p}.json`);
    if (ns === 'patchouli') return !hayJar('patchouli') || externos.has(`assets/patchouli/items/${p}.json`);
    return null; // de otro mod: no se puede saber
}
function bloqueExiste(id) {
    const [ns, p] = partirId(id);
    if (ns === MOD) return registrados.block.has(p);
    if (ns === 'minecraft') return !hayJar('vanilla') || externos.has(`assets/minecraft/blockstates/${p}.json`);
    if (ns === 'farmersdelight') return !hayJar('farmersdelight') || externos.has(`assets/farmersdelight/blockstates/${p}.json`);
    return null;
}
function tagExiste(tipo, id) {
    const [ns, p] = partirId(id.replace(/^#/, ''));
    return existe(`data/${ns}/tags/${tipo}/${p}.json`);
}
/** Revisa un item de nuestro mod o de vanilla/FD; los de nuestro mod solo si el codigo ya esta portado. */
function revisarItem(id, donde, opcionalDeMod) {
    const ok = itemExiste(id);
    if (ok === false) {
        const ns = partirId(id)[0];
        if (ns === MOD && !codigoListo) return;
        (opcionalDeMod === ns ? aviso : error)(`${donde}: el item ${id} no existe`);
    }
}
function revisarTagItem(id, donde, opcionalDeMod) {
    if (tagExiste('item', id)) return;
    const ns = partirId(id.replace(/^#/, ''))[0];
    const conocido = (ns === 'minecraft' && hayJar('vanilla')) || (ns === 'c' && hayJar('neoforge')) ||
        (ns === 'farmersdelight' && hayJar('farmersdelight')) || ns === MOD;
    if (!conocido) return;
    // los tags c: los pueden definir otros mods; si no esta en neoforge ni en FD es un aviso
    (ns === 'c' || opcionalDeMod === ns ? aviso : error)(`${donde}: el tag de item ${id} no existe en este repo ni en los jars`);
}

// ---------------------------------------------------------------- modelos y texturas
const builtin = (id) => /^(minecraft:)?builtin\//.test(id);
function modeloRel(id) { const [ns, p] = partirId(id); return `assets/${ns}/models/${p}.json`; }
function modeloJson(id) {
    const rel = modeloRel(id);
    if (json.has(rel)) return json.get(rel);
    const [ns] = partirId(id);
    const jar = ns === 'minecraft' ? jars.vanilla : ns === 'farmersdelight' ? jars.farmersdelight : null;
    if (jar && externos.has(rel)) {
        try { return JSON.parse(entradasZip(jar).leer(rel).toString('utf8')); } catch (e) { return null; }
    }
    return null;
}
const cacheModelo = new Map();
function texturasDe(id, prof = 0) {
    if (cacheModelo.has(id)) return cacheModelo.get(id);
    const m = modeloJson(id);
    let t = {};
    if (m && m.parent && !builtin(m.parent) && prof < 30) t = { ...texturasDe(m.parent, prof + 1) };
    if (m && m.textures) Object.assign(t, m.textures);
    cacheModelo.set(id, t);
    return t;
}
function resolverSlot(tex, v, prof = 0) {
    if (typeof v === 'object' && v) v = v.sprite;
    if (typeof v !== 'string') return null;
    if (!v.startsWith('#')) return v;
    if (prof > 20) return null;
    return resolverSlot(tex, tex[v.slice(1)], prof + 1);
}
function texturaExiste(id) {
    const [ns, p] = partirId(id);
    return existe(`assets/${ns}/textures/${p}.png`);
}
const CLAVES_MODELO = new Set(['parent', 'textures', 'elements', 'display', 'gui_light', 'ambientocclusion',
    'credit', 'texture_size', 'groups', 'loader', 'visibility', 'transform', 'neoforge_data']);
for (const rel of nuestros(/^assets\/[^/]+\/models\/.+\.json$/)) {
    const j = json.get(rel);
    const id = rel.replace(/^assets\/([^/]+)\/models\/(.+)\.json$/, '$1:$2');
    for (const k of Object.keys(j)) if (!CLAVES_MODELO.has(k)) aviso(`${rel}: clave "${k}" que 26.1.2 ignora`);
    if (j.loader) error(`${rel}: usa loader ${j.loader}, revisar que exista en NeoForge 26.1.2`);
    if (j.parent && !builtin(j.parent) && !existe(modeloRel(j.parent))) error(`${rel}: el parent ${j.parent} no existe`);
    const tex = texturasDe(id);
    for (const [k, v] of Object.entries(j.textures || {})) {
        const t = resolverSlot(tex, v);
        if (!t) { error(`${rel}: la textura "${k}" (${JSON.stringify(v)}) no se resuelve`); continue; }
        if (!texturaExiste(t)) error(`${rel}: la textura ${t} no existe`);
        else if (!/^[^:]*:?(block|item)\//.test(t.includes(':') ? t : 'minecraft:' + t)) {
            aviso(`${rel}: la textura ${t} no esta en el atlas de bloques ni en el de items`);
        }
    }
    const sinTextura = new Set();
    for (const [i, e] of (j.elements || []).entries()) {
        for (const c of [...(e.from || []), ...(e.to || [])]) if (c < -16 || c > 32) error(`${rel}: elemento ${i} fuera de [-16, 32]`);
        for (const f of Object.values(e.faces || {})) if (f.texture && !resolverSlot(tex, f.texture)) sinTextura.add(f.texture);
    }
    // si el modelo es un "template" (lo usan como parent) las texturas las pone el hijo
    if (sinTextura.size && ![...json.values()].some((o) => o && o.parent === id)) {
        const usado = [...json.entries()].some(([r, o]) => (/\/(items|blockstates)\//.test(r)) && JSON.stringify(o).includes(`"${id}"`));
        (usado ? error : aviso)(`${rel}: caras con ${[...sinTextura].join(', ')} sin definir${usado ? '' : ' (modelo sin usar)'}`);
    }
}

// blockstates: modelos existentes y solo texturas del atlas de bloques
for (const rel of nuestros(/^assets\/[^/]+\/blockstates\/.+\.json$/)) {
    const bs = json.get(rel);
    const vars = [];
    for (const v of Object.values(bs.variants || {})) vars.push(...[].concat(v));
    for (const p of bs.multipart || []) vars.push(...[].concat(p.apply));
    const bloque = rel.replace(/^assets\/([^/]+)\/blockstates\/(.+)\.json$/, '$1:$2');
    if (bloqueExiste(bloque) === false && codigoListo) error(`${rel}: el bloque ${bloque} no esta registrado`);
    const vistos = new Set();
    for (const v of vars) {
        if (vistos.has(v.model)) continue;
        vistos.add(v.model);
        if (!existe(modeloRel(v.model))) { error(`${rel}: el modelo ${v.model} no existe`); continue; }
        const tex = texturasDe(v.model);
        for (const [k, val] of Object.entries(tex)) {
            const t = resolverSlot(tex, val);
            if (t && !/^([^:]+:)?block\//.test(t)) {
                error(`${rel}: el modelo ${v.model} usa ${t} (slot ${k}) que no esta en el atlas de bloques: 26.1.2 rechaza el modelo`);
            }
        }
    }
}

// ---------------------------------------------------------------- definiciones de item
const NO_ITEMS = new Set(['guia_buela']); // modelo del libro de Patchouli (book.json "model")
const defsItem = new Set();
for (const rel of nuestros(new RegExp(`^assets/${MOD}/items/[^/]+\\.json$`))) {
    const id = path.basename(rel, '.json');
    defsItem.add(id);
    const recorrer = (o) => {
        if (Array.isArray(o)) return o.forEach(recorrer);
        if (!o || typeof o !== 'object') return;
        if (o.type === 'minecraft:model' || o.type === 'model') {
            if (!existe(modeloRel(o.model))) error(`${rel}: el modelo ${o.model} no existe`);
        }
        Object.values(o).forEach(recorrer);
    };
    recorrer(json.get(rel).model);
    if (codigoListo && !registrados.item.has(id) && !NO_ITEMS.has(id)) {
        aviso(`${rel}: no hay un item registrado ${MOD}:${id} (todavia no portado o sobra)`);
    }
}
if (codigoListo) {
    for (const id of registrados.item) if (!defsItem.has(id)) error(`El item ${MOD}:${id} no tiene assets/${MOD}/items/${id}.json`);
}

// ---------------------------------------------------------------- lang
const LANGS = ['en_us', 'es_es', 'es_ar', 'es_mx'];
const en = json.get(`assets/${MOD}/lang/en_us.json`) || {};
for (const l of LANGS) if (!json.has(`assets/${MOD}/lang/${l}.json`)) aviso(`Falta el lang ${l}`);
for (const id of registrados.item) {
    if (!(`item.${MOD}.${id}` in en) && !(`block.${MOD}.${id}` in en)) aviso(`Sin nombre en en_us: item ${MOD}:${id}`);
}

// ---------------------------------------------------------------- recetas
const TIPOS_RECETA = new Set(['minecraft:crafting_shaped', 'minecraft:crafting_shapeless', 'minecraft:smelting',
    'minecraft:smoking', 'minecraft:blasting', 'minecraft:campfire_cooking', 'minecraft:stonecutting',
    'minecraft:smithing_transform', 'minecraft:crafting_transmute', 'farmersdelight:cooking',
    'farmersdelight:cutting', `${MOD}:cacerola`]);
function recetaExiste(id) { const [ns, p] = partirId(id); return existe(`data/${ns}/recipe/${p}.json`); }
function revisarIngrediente(x, donde, modCond) {
    if (typeof x === 'string') {
        if (x.startsWith('#')) revisarTagItem(x, donde, modCond); else revisarItem(x, donde, modCond);
        return;
    }
    if (Array.isArray(x)) {
        for (const h of x) {
            if (typeof h !== 'string' || h.startsWith('#')) error(`${donde}: una lista de ingrediente solo puede tener ids de item: ${JSON.stringify(h)}`);
            revisarIngrediente(h, donde, modCond);
        }
        return;
    }
    if (x && typeof x === 'object') {
        if ('item' in x || 'tag' in x || 'type' in x) {
            error(`${donde}: ingrediente con formato 1.21.1: ${JSON.stringify(x)}`);
            return;
        }
        const t = x['neoforge:ingredient_type'];
        if (t === 'neoforge:compound') return (x.children || []).forEach((h) => revisarIngrediente(h, donde, modCond));
        if (t === 'farmersdelight:item_ability') return;
        if (t) aviso(`${donde}: ingrediente custom ${t} sin verificar`);
        return;
    }
    error(`${donde}: ingrediente invalido ${JSON.stringify(x)}`);
}
function revisarStack(s, donde, modCond) {
    if (!s) return;
    if (typeof s.id !== 'string') { error(`${donde}: stack sin id: ${JSON.stringify(s)}`); return; }
    revisarItem(s.id, donde, modCond);
    for (const comp of Object.keys(s.components || {})) {
        const [ns, p] = partirId(comp);
        if (ns === MOD && codigoListo && registrados.component.size && !registrados.component.has(p)) {
            error(`${donde}: el componente ${comp} no esta registrado`);
        }
    }
}
for (const rel of nuestros(/^data\/[^/]+\/recipe\/.+\.json$/)) {
    const r = json.get(rel);
    const modCond = ((r['neoforge:conditions'] || []).find((c) => c.type === 'neoforge:mod_loaded') || {}).modid;
    if (!TIPOS_RECETA.has(r.type)) { error(`${rel}: tipo de receta desconocido ${r.type}`); continue; }
    if (r.key) Object.values(r.key).forEach((v) => revisarIngrediente(v, rel, modCond));
    if (r.ingredients) r.ingredients.forEach((v) => revisarIngrediente(v, rel, modCond));
    if (r.ingredient) revisarIngrediente(r.ingredient, rel, modCond);
    if (r.tool) revisarIngrediente(r.tool, rel, modCond);
    if (Array.isArray(r.result)) r.result.forEach((x) => revisarStack(x.item, rel, modCond));
    else revisarStack(r.result, rel, modCond);
    if (r.container) revisarStack(r.container, rel, modCond);
    if (r.pattern && r.key) {
        for (const fila of r.pattern) for (const c of fila) if (c !== ' ' && !(c in r.key)) error(`${rel}: el patron usa "${c}" que no esta en key`);
    }
}

// ---------------------------------------------------------------- advancements
for (const rel of nuestros(/^data\/[^/]+\/advancement\/.+\.json$/)) {
    const a = json.get(rel);
    for (const r of (a.rewards && a.rewards.recipes) || []) if (!recetaExiste(r)) error(`${rel}: la receta ${r} no existe`);
    if (a.parent && !existe(`data/${partirId(a.parent)[0]}/advancement/${partirId(a.parent)[1]}.json`)) {
        error(`${rel}: el parent ${a.parent} no existe`);
    }
    for (const c of Object.values(a.criteria || {})) {
        const cond = c.conditions || {};
        if (cond.recipe && !recetaExiste(cond.recipe)) error(`${rel}: la receta ${cond.recipe} no existe`);
        for (const it of cond.items || []) {
            const v = it.items;
            for (const x of [].concat(v || [])) (x.startsWith('#') ? revisarTagItem : revisarItem)(x, rel);
        }
    }
    if (a.display && a.display.icon) revisarItem(a.display.icon.id, rel);
}

// ---------------------------------------------------------------- loot tables
for (const rel of nuestros(/^data\/[^/]+\/loot_table\/.+\.json$/)) {
    const recorrer = (o) => {
        if (Array.isArray(o)) return o.forEach(recorrer);
        if (!o || typeof o !== 'object') return;
        if (o.type === 'minecraft:item' && typeof o.name === 'string') revisarItem(o.name, rel);
        if (o.type === 'minecraft:tag' && typeof o.name === 'string') revisarTagItem('#' + o.name, rel);
        if (o.condition === 'minecraft:block_state_property' && bloqueExiste(o.block) === false && codigoListo) {
            error(`${rel}: el bloque ${o.block} no existe`);
        }
        Object.values(o).forEach(recorrer);
    };
    recorrer(json.get(rel));
    const m = new RegExp(`^data/${MOD}/loot_table/blocks/(.+)\\.json$`).exec(rel);
    if (m && codigoListo && !registrados.block.has(m[1])) aviso(`${rel}: no hay bloque registrado ${MOD}:${m[1]}`);
}

// ---------------------------------------------------------------- tags
for (const rel of nuestros(/^data\/[^/]+\/tags\/.+\.json$/)) {
    const m = /^data\/[^/]+\/tags\/(item|block|point_of_interest_type|villager_trade|worldgen\/biome)\//.exec(rel);
    if (!m) { aviso(`${rel}: tipo de tag sin verificar`); continue; }
    const tipo = m[1];
    for (const v of json.get(rel).values || []) {
        const id = typeof v === 'string' ? v : v.id;
        const requerido = typeof v === 'string' || v.required !== false;
        if (id.startsWith('#')) {
            if (!tagExiste(tipo, id) && requerido) error(`${rel}: el tag ${id} no existe`);
            continue;
        }
        let ok = null;
        if (tipo === 'item') ok = itemExiste(id);
        else if (tipo === 'block') ok = bloqueExiste(id);
        else if (tipo === 'point_of_interest_type') ok = partirId(id)[0] === MOD ? registrados.poi.has(partirId(id)[1]) : true;
        else if (tipo === 'villager_trade') ok = existe(`data/${partirId(id)[0]}/villager_trade/${partirId(id)[1]}.json`);
        if (ok === false && requerido && (codigoListo || tipo === 'villager_trade' || partirId(id)[0] !== MOD)) {
            error(`${rel}: ${id} no existe`);
        }
    }
}

// ---------------------------------------------------------------- tradeos
for (const rel of nuestros(/^data\/[^/]+\/trade_set\/.+\.json$/)) {
    const t = json.get(rel).trades;
    if (typeof t === 'string' && t.startsWith('#') && !tagExiste('villager_trade', t)) error(`${rel}: el tag ${t} no existe`);
}
for (const rel of nuestros(/^data\/[^/]+\/villager_trade\/.+\.json$/)) {
    const t = json.get(rel);
    for (const k of ['wants', 'additional_wants', 'gives']) if (t[k]) revisarItem(t[k].id, rel);
}

// ---------------------------------------------------------------- worldgen
for (const rel of nuestros(/^data\/[^/]+\/worldgen\/configured_feature\/.+\.json$/)) {
    const j = json.get(rel);
    if (j.type === 'minecraft:tree' && ('dirt_provider' in j.config || 'force_dirt' in j.config)) {
        error(`${rel}: dirt_provider/force_dirt ya no existen en 26.1.2 (below_trunk_provider)`);
    }
    const recorrer = (o) => {
        if (Array.isArray(o)) return o.forEach(recorrer);
        if (!o || typeof o !== 'object') return;
        if (typeof o.Name === 'string' && bloqueExiste(o.Name) === false && codigoListo) error(`${rel}: el bloque ${o.Name} no existe`);
        Object.values(o).forEach(recorrer);
    };
    recorrer(j);
}
for (const rel of nuestros(/^data\/[^/]+\/worldgen\/placed_feature\/.+\.json$/)) {
    const f = json.get(rel).feature;
    if (typeof f === 'string' && !existe(`data/${partirId(f)[0]}/worldgen/configured_feature/${partirId(f)[1]}.json`)) {
        error(`${rel}: la feature ${f} no existe`);
    }
}
for (const rel of nuestros(/^data\/[^/]+\/neoforge\/biome_modifier\/.+\.json$/)) {
    const b = json.get(rel);
    for (const f of [].concat(b.features || [])) {
        if (!f.startsWith('#') && !existe(`data/${partirId(f)[0]}/worldgen/placed_feature/${partirId(f)[1]}.json`)) {
            error(`${rel}: la placed feature ${f} no existe`);
        }
    }
    for (const bi of [].concat(b.biomes || [])) {
        if (typeof bi === 'string' && bi.startsWith('#') && !tagExiste('worldgen/biome', bi)) error(`${rel}: el tag de bioma ${bi} no existe`);
    }
}

// ---------------------------------------------------------------- Patchouli
for (const rel of nuestros(/^data\/[^/]+\/patchouli_books\/[^/]+\/book\.json$/)) {
    const [, ns, libro] = /^data\/([^/]+)\/patchouli_books\/([^/]+)\//.exec(rel);
    const b = json.get(rel);
    if (b.model) {
        const [mns, mp] = partirId(b.model);
        if (!existe(`assets/${mns}/items/${mp}.json`)) error(`${rel}: model ${b.model} necesita assets/${mns}/items/${mp}.json (Patchouli 26 usa getItemModel)`);
    }
    if (b.book_texture && !existe(`assets/${partirId(b.book_texture)[0]}/${partirId(b.book_texture)[1]}`)) error(`${rel}: book_texture ${b.book_texture} no existe`);
    for (const k of ['name', 'landing_text']) if (b.i18n && b[k] && !(b[k] in en)) aviso(`${rel}: ${k} "${b[k]}" no esta en en_us`);
    const base = `assets/${ns}/patchouli_books/${libro}/`;
    const porLocale = new Map();
    for (const r of [...json.keys()].filter((x) => x.startsWith(base))) {
        const [loc, ...resto] = r.slice(base.length).split('/');
        if (!porLocale.has(loc)) porLocale.set(loc, new Set());
        porLocale.get(loc).add(resto.join('/'));
    }
    for (const [loc, set] of porLocale) {
        const categorias = new Set([...set].filter((x) => x.startsWith('categories/')).map((x) => `${ns}:${x.slice(11, -5)}`));
        for (const r of set) {
            const j = json.get(base + loc + '/' + r);
            const donde = `${base}${loc}/${r}`;
            if (r.startsWith('entries/') && j.category && !categorias.has(j.category)) error(`${donde}: la categoria ${j.category} no existe`);
            const items = [];
            if (j.icon && !/\.png$/.test(j.icon)) items.push(j.icon);
            for (const p of j.pages || []) {
                for (const k of ['item']) if (typeof p[k] === 'string') items.push(...p[k].split(','));
                for (const k of ['recipe', 'recipe2']) if (typeof p[k] === 'string' && !recetaExiste(p[k])) error(`${donde}: la receta ${p[k]} no existe`);
            }
            for (const it of items) revisarItem(it.trim().replace(/[#[{].*$/, ''), donde);
        }
    }
    const conjuntos = [...porLocale.values()];
    for (const [loc, set] of porLocale) {
        for (const otro of conjuntos) for (const r of otro) if (!set.has(r)) aviso(`Patchouli ${loc} no tiene ${r}`);
    }
}

// ---------------------------------------------------------------- sonidos
const sounds = json.get(`assets/${MOD}/sounds.json`) || {};
for (const [ev, def] of Object.entries(sounds)) {
    for (const s of def.sounds || []) {
        const n = typeof s === 'string' ? s : s.name;
        if (typeof s === 'object' && s.type === 'event') continue;
        const [ns, p] = partirId(n);
        if (!existe(`assets/${ns}/sounds/${p}.ogg`)) error(`sounds.json: ${ev} -> ${n}.ogg no existe`);
    }
}
for (const id of registrados.sound) if (!(id in sounds)) error(`El sonido ${MOD}:${id} esta registrado pero no esta en sounds.json`);
for (const rel of nuestros(new RegExp(`^assets/${MOD}/sounds/`))) { /* .ogg no son json */ }
for (const rel of archivos.keys()) {
    const m = new RegExp(`^assets/${MOD}/sounds/(.+)\\.ogg$`).exec(rel);
    if (m && !Object.values(sounds).some((d) => (d.sounds || []).some((s) => (typeof s === 'string' ? s : s.name) === `${MOD}:${m[1]}`))) {
        aviso(`${rel} no lo usa ningun evento de sounds.json`);
    }
}

// ---------------------------------------------------------------- sol
if (archivos.has('assets/minecraft/textures/environment/sun.png')) error('El sol en environment/sun.png ya no se usa: va en environment/celestial/sun.png');
if (!archivos.has('assets/minecraft/textures/environment/celestial/sun.png')) aviso('No esta el sol criollo (assets/minecraft/textures/environment/celestial/sun.png)');

// ---------------------------------------------------------------- resumen
if (!codigoListo) aviso('No encontre items registrados en el Java: no se cruzan items/bloques del mod con el codigo');
console.log(`Items registrados encontrados en Java: ${registrados.item.size}, bloques: ${registrados.block.size}, sonidos: ${registrados.sound.size}`);
console.log(`Archivos revisados: ${archivos.size} (${json.size} JSON)`);
for (const a of [...new Set(avisos)]) console.log('AVISO: ' + a);
for (const e of [...new Set(errores)]) console.log('ERROR: ' + e);
console.log(`\n${errores.length} errores, ${new Set(avisos).size} avisos`);
process.exit(errores.length || (ESTRICTO && avisos.length) ? 1 : 0);
