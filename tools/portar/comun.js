// Utilidades compartidas por convertir.js y verificar.js (sin dependencias: solo Node).
'use strict';
const fs = require('fs');
const path = require('path');
const zlib = require('zlib');
const os = require('os');

/** Todos los archivos de una carpeta (recursivo), con rutas relativas en formato a/b/c. */
function listar(raiz) {
    const out = [];
    if (!fs.existsSync(raiz)) return out;
    const pila = [''];
    while (pila.length) {
        const rel = pila.pop();
        const abs = path.join(raiz, rel);
        for (const nombre of fs.readdirSync(abs)) {
            const r = rel ? rel + '/' + nombre : nombre;
            if (fs.statSync(path.join(raiz, r)).isDirectory()) pila.push(r);
            else out.push(r);
        }
    }
    return out.sort();
}

function leerJson(abs) {
    let txt = fs.readFileSync(abs, 'utf8');
    if (txt.charCodeAt(0) === 0xfeff) txt = txt.slice(1);
    return JSON.parse(txt);
}

/** Escribe JSON con 2 espacios y salto de linea final (mismo estilo que el datagen). */
function escribirJson(abs, obj) {
    fs.mkdirSync(path.dirname(abs), { recursive: true });
    fs.writeFileSync(abs, JSON.stringify(obj, null, 2) + '\n');
}

function copiar(origen, destino) {
    fs.mkdirSync(path.dirname(destino), { recursive: true });
    fs.copyFileSync(origen, destino);
}

/** "ns:ruta" -> [ns, ruta]; sin namespace es minecraft. */
function partirId(id) {
    const i = id.indexOf(':');
    return i < 0 ? ['minecraft', id] : [id.slice(0, i), id.slice(i + 1)];
}

/** Lee los nombres de las entradas de un zip/jar (directorio central; sin zip64). */
function entradasZip(abs) {
    const buf = fs.readFileSync(abs);
    let eocd = -1;
    for (let i = buf.length - 22; i >= Math.max(0, buf.length - 65557); i--) {
        if (buf.readUInt32LE(i) === 0x06054b50) { eocd = i; break; }
    }
    if (eocd < 0) throw new Error('No es un zip: ' + abs);
    const total = buf.readUInt16LE(eocd + 10);
    let p = buf.readUInt32LE(eocd + 16);
    const out = new Map();
    for (let n = 0; n < total; n++) {
        if (buf.readUInt32LE(p) !== 0x02014b50) break;
        const metodo = buf.readUInt16LE(p + 10);
        const tamComp = buf.readUInt32LE(p + 20);
        const lnNombre = buf.readUInt16LE(p + 28);
        const lnExtra = buf.readUInt16LE(p + 30);
        const lnCom = buf.readUInt16LE(p + 32);
        const local = buf.readUInt32LE(p + 42);
        const nombre = buf.toString('utf8', p + 46, p + 46 + lnNombre);
        out.set(nombre, { metodo, tamComp, local });
        p += 46 + lnNombre + lnExtra + lnCom;
    }
    return {
        nombres: [...out.keys()],
        leer(nombre) {
            const e = out.get(nombre);
            if (!e) return null;
            const lnN = buf.readUInt16LE(e.local + 26);
            const lnE = buf.readUInt16LE(e.local + 28);
            const ini = e.local + 30 + lnN + lnE;
            const datos = buf.subarray(ini, ini + e.tamComp);
            return e.metodo === 0 ? Buffer.from(datos) : zlib.inflateRawSync(datos);
        },
    };
}

/** Busca un archivo dentro de la cache de Gradle del usuario (modules-2) por grupo/artefacto/version. */
function jarDeCache(grupo, artefacto, version, sufijo) {
    const base = path.join(os.homedir(), '.gradle', 'caches', 'modules-2', 'files-2.1', grupo, artefacto, version);
    if (!fs.existsSync(base)) return null;
    for (const hash of fs.readdirSync(base)) {
        const dir = path.join(base, hash);
        for (const f of fs.readdirSync(dir)) {
            if (f.endsWith('.jar') && (!sufijo || f.endsWith(sufijo))) return path.join(dir, f);
        }
    }
    return null;
}

/** Lee gradle.properties como objeto. */
function propiedades(abs) {
    const out = {};
    if (!fs.existsSync(abs)) return out;
    for (const linea of fs.readFileSync(abs, 'utf8').split(/\r?\n/)) {
        const m = /^\s*([\w.]+)\s*=\s*(.*?)\s*$/.exec(linea);
        if (m) out[m[1]] = m[2];
    }
    return out;
}

module.exports = { listar, leerJson, escribirJson, copiar, partirId, entradasZip, jarDeCache, propiedades };
