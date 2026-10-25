// Valida los modelos 3D de las comidas criollas (y los extra la_copa y bombilla).
// Uso: node bbmodels/fisura/comida/generadores/validate.js
const fs = require('fs'), path = require('path');
const { ROOT, ASSETS, BB_DIR, bounds } = require('./lib');

const COMIDAS = ['asado', 'choripan', 'chorizo_asado', 'provoleta', 'milanesa_de_carne', 'milanesa_de_pollo', 'sanguche_de_milanesa', 'guiso', 'locro', 'noquis', 'alfajor', 'medialuna', 'panqueque_con_dulce_de_leche', 'flan_con_dulce_de_leche', 'tortafrita', 'empanada', 'pitusas', 'pepas', 'fugazzeta', 'mega_dibu', 'fernet_branca', 'quilmes', 'stella_artois', 'andes'];
const MODELOS = [
  ...COMIDAS.map(id => ({ id, kind: 'block', folder: 'fisura/comida', bb: BB_DIR })),
  { id: 'la_copa', kind: 'block', folder: 'fisura', bb: path.join(ROOT, 'bbmodels', 'fisura') },
  { id: 'bombilla', kind: 'item', folder: 'fisura', bb: path.join(ROOT, 'bbmodels', 'fisura') }
];
const SLOTS = ['gui', 'ground', 'fixed', 'thirdperson_righthand', 'thirdperson_lefthand', 'firstperson_righthand', 'firstperson_lefthand', 'head'];
const ANGLES = new Set([-45, -22.5, 0, 22.5, 45]);
const medidas = JSON.parse(fs.readFileSync(path.join(BB_DIR, 'medidas.json'), 'utf8'));
let errores = 0;
const err = (id, m) => { errores++; console.log(`  ERROR ${id}: ${m}`); };
const pngSize = f => { const b = fs.readFileSync(f); if (b.readUInt32BE(0) !== 0x89504e47) throw new Error('no es PNG'); return [b.readUInt32BE(16), b.readUInt32BE(20)]; };

for (const { id, kind, folder, bb } of MODELOS) {
  const mf = path.join(ASSETS, 'models', kind, ...folder.split('/'), id + '.json');
  let j;
  try { j = JSON.parse(fs.readFileSync(mf, 'utf8')); } catch (e) { err(id, 'no parsea ' + e.message); continue; }
  const els = j.elements || [];
  if (!els.length) err(id, 'sin elements');
  for (const [k, ref] of Object.entries(j.textures || {})) {
    const [ns, p] = ref.split(':');
    if (ns !== 'ultimatefoods' || !p.includes('/fisura/')) { err(id, 'textura fuera de fisura/ ' + ref); continue; }
    const tf = path.join(ASSETS, 'textures', p + '.png');
    if (!fs.existsSync(tf)) { err(id, 'falta textura ' + tf); continue; }
    const [w, h] = pngSize(tf);
    if (!j.texture_size || j.texture_size[0] !== w || j.texture_size[1] !== h) err(id, `texture_size ${j.texture_size} != png ${w}x${h}`);
  }
  if (!j.textures || !j.textures.particle) err(id, 'sin particle');
  for (const s of SLOTS) if (!j.display || !j.display[s]) err(id, 'falta display ' + s);
  let caras = 0;
  for (const e of els) {
    for (const v of [...e.from, ...e.to]) if (v < 0 || v > 16) err(id, `coordenada fuera del cubo ${v} en ${e.name}`);
    for (let i = 0; i < 3; i++) if (e.to[i] < e.from[i]) err(id, `from>to en ${e.name}`);
    if (e.rotation && (!ANGLES.has(e.rotation.angle) || !['x', 'y', 'z'].includes(e.rotation.axis))) err(id, `rotacion invalida ${JSON.stringify(e.rotation)}`);
    for (const [d, fc] of Object.entries(e.faces)) {
      caras++;
      if (!(fc.texture || '').startsWith('#') || !j.textures[fc.texture.slice(1)]) err(id, `cara ${d} con textura ${fc.texture}`);
      for (const u of fc.uv) if (u < 0 || u > 16) err(id, `uv fuera de rango ${u} en ${e.name}.${d}`);
      if (fc.rotation && ![0, 90, 180, 270].includes(fc.rotation)) err(id, 'rotacion de uv invalida');
    }
  }
  const b = bounds(els);
  for (let i = 0; i < 3; i++) if (b.min[i] < 0 || b.max[i] > 16) err(id, `con las rotaciones sale del cubo: ${JSON.stringify(b)}`);
  if (b.min[1] !== 0 && kind === 'block') err(id, 'no apoya en y=0');
  const md = medidas[id];
  if (!md || JSON.stringify(md.min) !== JSON.stringify(b.min) || JSON.stringify(md.max) !== JSON.stringify(b.max)) err(id, 'medidas.json desactualizado');
  // .bbmodel
  const bf = path.join(bb, id + '.bbmodel');
  let cubosBB = '?';
  try {
    const p = JSON.parse(fs.readFileSync(bf, 'utf8'));
    cubosBB = (p.elements || []).filter(e => (e.type || 'cube') === 'cube').length;
    if (cubosBB !== els.length) err(id, `.bbmodel con ${cubosBB} cubos y el JSON con ${els.length}`);
    if (!(p.textures || []).some(t => (t.source || '').startsWith('data:image/png;base64,') && t.source.length > 100)) err(id, '.bbmodel sin textura embebida');
    if (!p.meta || p.meta.model_format !== 'java_block') err(id, '.bbmodel con formato raro');
  } catch (e) { err(id, 'falta o no parsea ' + bf); }
  console.log(`ok? ${id.padEnd(30)} ${String(els.length).padStart(3)} cubos ${String(caras).padStart(4)} caras  bbmodel=${cubosBB}  textura ${j.texture_size}  limites ${JSON.stringify(b.min)}..${JSON.stringify(b.max)}`);
}
// item de la copa
const ic = path.join(ASSETS, 'models', 'item', 'fisura', 'la_copa.json');
try { const j = JSON.parse(fs.readFileSync(ic, 'utf8')); if (j.parent !== 'ultimatefoods:block/fisura/la_copa') err('la_copa', 'item con parent incorrecto'); for (const s of SLOTS) if (!j.display || !j.display[s]) err('la_copa', 'item sin display ' + s); } catch (e) { err('la_copa', 'falta el modelo de item'); }
console.log(errores ? `\n${errores} ERRORES` : `\nTODO VALIDO: ${MODELOS.length} modelos (24 comidas + la_copa + bombilla), JSON, texturas, display, rangos, rotaciones, .bbmodel y medidas.json`);
process.exit(errores ? 1 : 0);
