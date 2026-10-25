// Renderizador por software de modelos Java de vanilla, para revisar los modelos sin abrir el juego.
// Hoja de 6 vistas: inventario (con el display gui, a 48 px reales ampliado), en la mano (tercera persona),
// colocado visto de frente-izquierda (lado norte), de atras, de frente y desde arriba.
// Uso: node render.js <salida.png> <modelo.json | id> [id2 ...]   (los ids se buscan en models/block/fisura/comida)
const fs = require('fs'), path = require('path');
const { ASSETS, Canvas, decodePNG, rotPoint } = require('./lib');

function rotXYZ(v, [ax, ay, az]) {
  const r = d => d * Math.PI / 180; let [x, y, z] = v, c, s;
  c = Math.cos(r(az)); s = Math.sin(r(az)); [x, y] = [x * c - y * s, x * s + y * c];
  c = Math.cos(r(ay)); s = Math.sin(r(ay)); [x, z] = [x * c + z * s, -x * s + z * c];
  c = Math.cos(r(ax)); s = Math.sin(r(ax)); [y, z] = [y * c - z * s, y * s + z * c];
  return [x, y, z];
}
function loadModel(ref) {
  let f = ref;
  if (!ref.endsWith('.json')) f = path.join(ASSETS, 'models', 'block', 'fisura', 'comida', ref + '.json');
  let j = JSON.parse(fs.readFileSync(f, 'utf8'));
  if (j.parent && !j.elements) { const [, p] = j.parent.split(':'); const pj = JSON.parse(fs.readFileSync(path.join(ASSETS, 'models', p + '.json'), 'utf8')); j = { ...pj, display: { ...pj.display, ...j.display } }; }
  const tex = {};
  for (const [k, v] of Object.entries(j.textures)) { const [, p] = v.split(':'); tex[k] = decodePNG(path.join(ASSETS, 'textures', p + '.png')); }
  return { j, tex };
}
const SHADE = { up: 1, down: 0.5, north: 0.8, south: 0.8, east: 0.6, west: 0.6 };
function faceCorners(e, d) {
  const [x1, y1, z1] = e.from, [x2, y2, z2] = e.to;
  const C = {
    north: [[x2, y2, z1], [x1, y2, z1], [x2, y1, z1]], south: [[x1, y2, z2], [x2, y2, z2], [x1, y1, z2]],
    east: [[x2, y2, z2], [x2, y2, z1], [x2, y1, z2]], west: [[x1, y2, z1], [x1, y2, z2], [x1, y1, z1]],
    up: [[x1, y2, z1], [x2, y2, z1], [x1, y2, z2]], down: [[x1, y1, z2], [x2, y1, z2], [x1, y1, z1]]
  }[d];
  return e.rotation ? C.map(p => rotPoint(p, e.rotation.axis, e.rotation.angle, e.rotation.origin)) : C;
}
// dibuja el modelo en el lienzo; tr = {rotation, translation, scale} (display), F = pixeles por unidad, (ox, oy) centro
function draw(cv, zb, model, tr, F, ox, oy) {
  const { j, tex } = model;
  const rot = tr.rotation || [0, 0, 0], tl = tr.translation || [0, 0, 0], sc = tr.scale || [1, 1, 1];
  const T = p => { const q = rotXYZ([(p[0] - 8) * sc[0], (p[1] - 8) * sc[1], (p[2] - 8) * sc[2]], rot); return [ox + (q[0] + tl[0]) * F, oy - (q[1] + tl[1]) * F, q[2] + tl[2]]; };
  for (const e of j.elements) for (const [d, f] of Object.entries(e.faces)) {
    const t = tex[f.texture.slice(1)]; if (!t) continue;
    const [A, B, C] = faceCorners(e, d).map(T);
    const ux = B[0] - A[0], uy = B[1] - A[1], vx = C[0] - A[0], vy = C[1] - A[1], det = ux * vy - uy * vx;
    if (Math.abs(det) < 1e-6) continue;
    const xs = [A[0], B[0], C[0], B[0] + vx], ys = [A[1], B[1], C[1], B[1] + vy];
    const [u1, v1, u2, v2] = f.uv, r = (f.rotation || 0) / 90;
    // la luz depende de hacia donde mira la cara ya transformada (aprox. como en el juego)
    const sh = SHADE[d];
    for (let py = Math.max(0, Math.floor(Math.min(...ys))); py <= Math.min(cv.h - 1, Math.ceil(Math.max(...ys))); py++)
      for (let px = Math.max(0, Math.floor(Math.min(...xs))); px <= Math.min(cv.w - 1, Math.ceil(Math.max(...xs))); px++) {
        const qx = px + 0.5 - A[0], qy = py + 0.5 - A[1];
        const s = (qx * vy - qy * vx) / det, tt = (ux * qy - uy * qx) / det;
        if (s < 0 || s > 1 || tt < 0 || tt > 1) continue;
        const z = A[2] + s * (B[2] - A[2]) + tt * (C[2] - A[2]);
        const k = py * cv.w + px; if (z <= zb[k]) continue;
        let a = s, b = tt; if (r === 1) [a, b] = [tt, 1 - s]; else if (r === 2) [a, b] = [1 - s, 1 - tt]; else if (r === 3) [a, b] = [1 - tt, s];
        const u = u1 + a * (u2 - u1), v = v1 + b * (v2 - v1);
        const tx = Math.min(t.w - 1, Math.max(0, Math.floor(u * t.w / 16 - (u2 < u1 ? 1e-6 : -1e-6)))), ty = Math.min(t.h - 1, Math.max(0, Math.floor(v * t.h / 16 - (v2 < v1 ? 1e-6 : -1e-6))));
        const i = (ty * t.w + tx) * 4; if (t.d[i + 3] < 128) continue;
        zb[k] = z; cv.set(px, py, [Math.round(t.d[i] * sh), Math.round(t.d[i + 1] * sh), Math.round(t.d[i + 2] * sh), 255]);
      }
  }
}
function panel(model, kind, size) {
  const cv = new Canvas(size, size), zb = new Float64Array(size * size).fill(-Infinity);
  const bg = kind === 'gui' ? [139, 139, 139, 255] : [228, 232, 236, 255];
  for (let i = 0; i < size * size; i++) cv.d.set(bg, i * 4);
  const disp = model.j.display || {};
  if (kind === 'gui') {
    // 16 px de slot a escala de GUI 3 (48 px) y despues ampliado x(size/48)
    const small = new Canvas(48, 48), zs = new Float64Array(48 * 48).fill(-Infinity);
    for (let i = 0; i < 48 * 48; i++) small.d.set(bg, i * 4);
    draw(small, zs, model, disp.gui || {}, 3, 24, 24);
    const k = size / 48; for (let y = 0; y < size; y++) for (let x = 0; x < size; x++) cv.set(x, y, small.get(Math.floor(x / k), Math.floor(y / k)));
    return cv;
  }
  if (kind === 'mano') { const d = disp.thirdperson_righthand || {}; draw(cv, zb, model, { ...d, rotation: (d.rotation || [0, 0, 0]) }, size / 16 * 1.4, size / 2, size / 2); return cv; }
  const R = { iso: [30, 225, 0], atras: [30, 45, 0], frente: [12, 180, 0], arriba: [90, 0, 0] }[kind];
  draw(cv, zb, model, { rotation: R, scale: [1, 1, 1] }, size / 25, size / 2, size / 2);
  return cv;
}
function sheet(out, refs) {
  const S = 220, kinds = ['gui', 'mano', 'iso', 'atras', 'frente', 'arriba'];
  const cv = new Canvas(S * kinds.length, S * refs.length);
  refs.forEach((ref, row) => {
    const model = loadModel(ref);
    kinds.forEach((k, col) => { const p = panel(model, k, S); for (let y = 0; y < S; y++) for (let x = 0; x < S; x++) cv.set(col * S + x, row * S + y, (x === 0 || y === 0) ? [90, 90, 90, 255] : p.get(x, y)); });
  });
  cv.save(out);
  return out;
}
if (require.main === module) { const [, , out, ...refs] = process.argv; console.log(sheet(out, refs)); }
module.exports = { sheet, loadModel, draw, panel };
