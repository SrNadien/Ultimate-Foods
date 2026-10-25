// Valida los modelos 3D del agente C.
const fs = require('fs'), path = require('path');
const { REPO } = require('./lib');
const ITEMS = ['mate', 'mate_con_yerba', 'mate_amargo', 'mate_dulce', 'mate_de_leche', 'mate_con_yuyos', 'mate_lavado', 'pava', 'pava_con_agua', 'pava_caliente', 'yerba_verdeflor', 'yerbera', 'parrilla', 'cacerola_essen', 'azucarera'];
const BLOCKS = ['yerbera', 'parrilla', 'cacerola_essen', 'azucarera'];
const SLOTS = ['gui', 'ground', 'fixed', 'thirdperson_righthand', 'thirdperson_lefthand', 'firstperson_righthand', 'firstperson_lefthand', 'head'];
const ANGLES = new Set([-45, -22.5, 0, 22.5, 45]);
let errors = 0;
const err = (id, m) => { errors++; console.log(`  ERROR ${id}: ${m}`); };
function pngSize(f) { const b = fs.readFileSync(f); if (b.readUInt32BE(0) !== 0x89504e47) throw new Error('no es PNG'); return [b.readUInt32BE(16), b.readUInt32BE(20)]; }
function resolve(ref) { // "ultimatefoods:block/fisura/x" -> ruta
  const [ns, p] = ref.includes(':') ? ref.split(':') : ['minecraft', ref];
  if (ns !== 'ultimatefoods') return null; return { ns, p };
}
function loadModel(ref) { const r = resolve(ref); const f = `${REPO}/models/${r.p}.json`; return { f, j: JSON.parse(fs.readFileSync(f, 'utf8')) }; }
function checkFull(id, ref) {
  const { f, j } = loadModel(ref);
  const els = j.elements || [];
  if (!els.length) err(id, 'sin elements');
  for (const [k, t] of Object.entries(j.textures || {})) {
    const r = resolve(t); if (!r) { err(id, 'textura fuera del mod ' + t); continue; }
    if (!r.p.includes('/fisura/')) err(id, 'textura fuera de fisura/ ' + t);
    const tf = `${REPO}/textures/${r.p}.png`;
    if (!fs.existsSync(tf)) { err(id, 'falta textura ' + tf); continue; }
    const [w, h] = pngSize(tf);
    if (j.texture_size && (j.texture_size[0] !== w || j.texture_size[1] !== h)) err(id, `texture_size ${j.texture_size} != png ${w}x${h}`);
  }
  if (!j.textures || !j.textures.particle) err(id, 'sin particle');
  for (const s of SLOTS) if (!j.display || !j.display[s]) err(id, 'falta display ' + s);
  let faces = 0;
  for (const e of els) {
    for (const v of [...e.from, ...e.to]) if (v < -16 || v > 32) err(id, `coordenada fuera de rango ${v} en ${e.name}`);
    for (let i = 0; i < 3; i++) if (e.to[i] < e.from[i]) err(id, `from>to en ${e.name}`);
    if (e.rotation && !ANGLES.has(e.rotation.angle)) err(id, `angulo invalido ${e.rotation.angle}`);
    for (const [d, fc] of Object.entries(e.faces)) {
      faces++;
      if (!(fc.texture || '').startsWith('#') || !j.textures[fc.texture.slice(1)]) err(id, `cara ${d} con textura ${fc.texture}`);
      for (const u of fc.uv) if (u < 0 || u > 16) err(id, `uv fuera de rango ${u}`);
    }
  }
  return { f, els: els.length, faces, tex: Object.values(j.textures) };
}
for (const id of ITEMS) {
  const ref = `ultimatefoods:item/fisura/${id}`;
  let { f, j } = loadModel(ref);
  let info;
  if (j.parent) {
    if (!BLOCKS.includes(id)) err(id, 'item con parent inesperado');
    if (j.parent !== `ultimatefoods:block/fisura/${id}`) err(id, 'parent incorrecto ' + j.parent);
    info = checkFull(id, j.parent); info.f = f + ' -> ' + info.f;
  } else info = checkFull(id, ref);
  console.log(`OK? ${id}: ${info.els} cubos, ${info.faces} caras, texturas ${info.tex.join(', ')}`);
}
{ const i = checkFull('cacerola_essen_abierta', 'ultimatefoods:block/fisura/cacerola_essen_abierta'); console.log('OK? cacerola_essen_abierta (solo bloque, variante): ' + i.els + ' cubos'); }
console.log(errors ? `\n${errors} ERRORES` : '\nTODO VALIDO (15 ids + variante, JSON parsea, texturas existen, display completo)');
process.exit(errors ? 1 : 0);
