// Libreria de los generadores de comidas 3D (Argentina): PNG RGBA, pintores, atlas, modelos Java de vanilla,
// display calculado a partir de los limites del modelo y medidas para la hitbox.
// La raiz del repo se deduce de la ubicacion de este archivo (bbmodels/argentina/comida/generadores) o de UF_REPO.
const fs = require('fs'), path = require('path'), zlib = require('zlib');

const ROOT = path.resolve(process.env.UF_REPO || path.join(__dirname, '..', '..', '..', '..'));
const ASSETS = path.join(ROOT, 'src', 'main', 'resources', 'assets', 'ultimatefoods');
const BB_DIR = path.join(ROOT, 'bbmodels', 'argentina', 'comida');
const MEDIDAS = path.join(BB_DIR, 'medidas.json');

// ---------- PNG ----------
const CRC = (() => { const t = new Uint32Array(256); for (let n = 0; n < 256; n++) { let c = n; for (let k = 0; k < 8; k++) c = c & 1 ? 0xedb88320 ^ (c >>> 1) : c >>> 1; t[n] = c >>> 0; } return t; })();
function crc32(buf) { let c = 0xffffffff; for (const b of buf) c = CRC[(c ^ b) & 0xff] ^ (c >>> 8); return (c ^ 0xffffffff) >>> 0; }
function chunk(type, data) {
  const len = Buffer.alloc(4); len.writeUInt32BE(data.length);
  const td = Buffer.concat([Buffer.from(type, 'ascii'), data]);
  const crc = Buffer.alloc(4); crc.writeUInt32BE(crc32(td));
  return Buffer.concat([len, td, crc]);
}
function encodePNG(w, h, rgba) {
  const raw = Buffer.alloc((w * 4 + 1) * h);
  for (let y = 0; y < h; y++) { raw[y * (w * 4 + 1)] = 0; rgba.copy(raw, y * (w * 4 + 1) + 1, y * w * 4, (y + 1) * w * 4); }
  const ihdr = Buffer.alloc(13); ihdr.writeUInt32BE(w, 0); ihdr.writeUInt32BE(h, 4); ihdr[8] = 8; ihdr[9] = 6;
  return Buffer.concat([Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]), chunk('IHDR', ihdr), chunk('IDAT', zlib.deflateSync(raw, { level: 9 })), chunk('IEND', Buffer.alloc(0))]);
}
// Decodificador PNG de 8 bits (grises, RGB, paleta, gris+alfa, RGBA), sin entrelazado. Devuelve {w,h,d:RGBA}
function decodePNG(file) {
  const b = fs.readFileSync(file);
  let p = 8, w, h, bd, ct, plte, trns, idat = [];
  while (p < b.length) {
    const len = b.readUInt32BE(p), type = b.toString('ascii', p + 4, p + 8), data = b.slice(p + 8, p + 8 + len);
    if (type === 'IHDR') { w = data.readUInt32BE(0); h = data.readUInt32BE(4); bd = data[8]; ct = data[9]; if (data[12]) throw new Error('PNG entrelazado'); }
    else if (type === 'PLTE') plte = data; else if (type === 'tRNS') trns = data; else if (type === 'IDAT') idat.push(data);
    p += 12 + len;
  }
  const ch = { 0: 1, 2: 3, 3: 1, 4: 2, 6: 4 }[ct];
  const bpp = Math.max(1, (ch * bd) >> 3), stride = (w * ch * bd + 7) >> 3;
  const raw = zlib.inflateSync(Buffer.concat(idat)), out = Buffer.alloc(w * h * 4);
  let prev = Buffer.alloc(stride);
  for (let y = 0; y < h; y++) {
    const f = raw[y * (stride + 1)], line = Buffer.from(raw.slice(y * (stride + 1) + 1, (y + 1) * (stride + 1)));
    for (let i = 0; i < stride; i++) {
      const a = i >= bpp ? line[i - bpp] : 0, up = prev[i], c = i >= bpp ? prev[i - bpp] : 0;
      let v = line[i];
      if (f === 1) v += a; else if (f === 2) v += up; else if (f === 3) v += (a + up) >> 1;
      else if (f === 4) { const pp = a + up - c, pa = Math.abs(pp - a), pb = Math.abs(pp - up), pc = Math.abs(pp - c); v += pa <= pb && pa <= pc ? a : (pb <= pc ? up : c); }
      line[i] = v & 255;
    }
    for (let x = 0; x < w; x++) {
      let r, g, bl, al = 255;
      const sample = k => { if (bd === 8) return line[x * ch + k]; const bits = x * bd; return (line[bits >> 3] >> (8 - bd - (bits & 7))) & ((1 << bd) - 1); };
      if (ct === 6) { r = sample(0); g = sample(1); bl = sample(2); al = sample(3); }
      else if (ct === 2) { r = sample(0); g = sample(1); bl = sample(2); }
      else if (ct === 0) { r = g = bl = sample(0) * (bd < 8 ? 255 / ((1 << bd) - 1) : 1); }
      else if (ct === 4) { r = g = bl = sample(0); al = sample(1); }
      else { const i = sample(0); r = plte[i * 3]; g = plte[i * 3 + 1]; bl = plte[i * 3 + 2]; al = trns && i < trns.length ? trns[i] : 255; }
      out.set([r, g, bl, al], (y * w + x) * 4);
    }
    prev = line;
  }
  return { w, h, d: out };
}

// ---------- color / ruido ----------
function hex(c) { if (Array.isArray(c)) return c.length === 3 ? [...c, 255] : c; c = c.replace('#', ''); const a = c.length === 8 ? parseInt(c.slice(6, 8), 16) : 255; return [parseInt(c.slice(0, 2), 16), parseInt(c.slice(2, 4), 16), parseInt(c.slice(4, 6), 16), a]; }
function mix(a, b, t) { a = hex(a); b = hex(b); return a.map((v, i) => Math.round(v + (b[i] - v) * t)); }
function shade(c, f) { c = hex(c); return [0, 1, 2].map(i => Math.max(0, Math.min(255, Math.round(c[i] * f)))).concat([c[3]]); }
function rnd(x, y, s = 0) { let h = (x * 374761393 + y * 668265263 + s * 2147483647) | 0; h = Math.imul(h ^ (h >>> 13), 1274126177); h ^= h >>> 16; return ((h >>> 0) % 100000) / 100000; }
function pick(list, x, y, s) { return list[Math.floor(rnd(x, y, s) * list.length)]; }
// ruido suave por celdas (para manchas grandes)
function blob(x, y, cell, s) { const fx = x / cell, fy = y / cell, x0 = Math.floor(fx), y0 = Math.floor(fy), tx = fx - x0, ty = fy - y0; const l = (a, b, t) => a + (b - a) * t * t * (3 - 2 * t); return l(l(rnd(x0, y0, s), rnd(x0 + 1, y0, s), tx), l(rnd(x0, y0 + 1, s), rnd(x0 + 1, y0 + 1, s), tx), ty); }
// rampa: elige color de una lista segun un valor 0..1
function ramp(list, t) { t = Math.max(0, Math.min(0.9999, t)); return list[Math.floor(t * list.length)]; }

class Canvas {
  constructor(w, h) { this.w = w; this.h = h; this.d = Buffer.alloc(w * h * 4); }
  set(x, y, c) { x = Math.floor(x); y = Math.floor(y); if (x < 0 || y < 0 || x >= this.w || y >= this.h) return; c = hex(c); this.d.set(c, (y * this.w + x) * 4); }
  get(x, y) { const i = (y * this.w + x) * 4; return [...this.d.slice(i, i + 4)]; }
  save(file) { fs.mkdirSync(path.dirname(file), { recursive: true }); fs.writeFileSync(file, encodePNG(this.w, this.h, this.d)); }
}

// Imagen local para regiones con dibujo libre (texto, logos, etiquetas)
class Img {
  constructor(w, h, bg) { this.w = w; this.h = h; this.px = new Array(w * h).fill(null); if (bg) this.fill(bg); }
  set(x, y, c) { x = Math.round(x); y = Math.round(y); if (x < 0 || y < 0 || x >= this.w || y >= this.h) return; this.px[y * this.w + x] = typeof c === 'function' ? c(x, y, this.px[y * this.w + x]) : c; }
  get(x, y) { return this.px[y * this.w + x]; }
  fill(fn) { for (let y = 0; y < this.h; y++) for (let x = 0; x < this.w; x++) this.set(x, y, typeof fn === 'function' ? fn(x, y) : fn); return this; }
  rect(x, y, w, h, c) { for (let j = 0; j < h; j++) for (let i = 0; i < w; i++) this.set(x + i, y + j, c); return this; }
  ellipse(cx, cy, rx, ry, c) { for (let y = Math.floor(cy - ry); y <= cy + ry; y++) for (let x = Math.floor(cx - rx); x <= cx + rx; x++) if (((x + 0.5 - cx) / rx) ** 2 + ((y + 0.5 - cy) / ry) ** 2 <= 1) this.set(x, y, c); return this; }
  text(font, s, x, y, c, gap = 1) { const { textPixels } = require('./font'); for (const [px, py] of textPixels(font, s, gap)) this.set(x + px, y + py, c); return this; }
  painter() { return (x, y) => this.px[y * this.w + x]; }
}

// ---------- modelo ----------
const DIRS = ['north', 'east', 'south', 'west', 'up', 'down'];
class ModelBuilder {
  constructor({ size = 64, density = 2, h } = {}) { this.size = size; this.sizeH = h || size; this.dens = density; this.regions = {}; this.order = []; this.elements = []; }
  // paint(lx, ly, w, h) -> color | null
  region(name, w, h, paint) { if (this.regions[name]) throw new Error('region repetida ' + name); this.regions[name] = { w, h, paint }; this.order.push(name); return this; }
  pack() {
    const names = [...this.order].sort((a, b) => this.regions[b].h - this.regions[a].h || this.regions[b].w - this.regions[a].w);
    let x = 0, y = 0, rowH = 0;
    for (const n of names) {
      const r = this.regions[n];
      if (x + r.w > this.size) { x = 0; y += rowH; rowH = 0; }
      if (y + r.h > this.sizeH) {
        // no entra: se agranda el atlas (cuadrado) y se vuelve a empaquetar
        if (this.size >= 256) throw new Error('atlas lleno en ' + n);
        this.size *= 2; this.sizeH = this.size; return this.pack();
      }
      r.x = x; r.y = y; x += r.w; rowH = Math.max(rowH, r.h);
    }
  }
  paint() {
    this.pack();
    const c = new Canvas(this.size, this.sizeH);
    for (const n of this.order) { const r = this.regions[n]; for (let ly = 0; ly < r.h; ly++) for (let lx = 0; lx < r.w; lx++) { const col = r.paint(lx, ly, r.w, r.h); if (col) c.set(r.x + lx, r.y + ly, col); } }
    return c;
  }
  // faces: {all|sides|ends|north..down: spec}; spec = 'region' | {r, ox, oy, rot, full, flip} | null (cara omitida)
  cube(from, to, faces, rotation, name) { this.elements.push({ from, to, faces, rotation, name }); return this; }
  faceUV(el, dir, spec) {
    if (spec == null) return null;
    if (typeof spec === 'string') spec = { r: spec };
    const r = this.regions[spec.r]; if (!r) throw new Error('region ' + spec.r);
    const [x1, y1, z1] = el.from, [x2, y2, z2] = el.to;
    const dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;
    let fw, fh;
    if (dir === 'north' || dir === 'south') { fw = dx; fh = dy; } else if (dir === 'east' || dir === 'west') { fw = dz; fh = dy; } else { fw = dx; fh = dz; }
    if (spec.rot === 90 || spec.rot === 270) [fw, fh] = [fh, fw];
    fw *= this.dens; fh *= this.dens;
    const ku0 = 16 / this.size, kv0 = 16 / this.sizeH;
    // proyeccion plana: la region cubre el rectangulo `frame` [x0,z0,x1,z1] (caras up/down) o [a0,y0,a1,y1] (laterales)
    if (spec.frame) {
      const [a0, b0, a1, b1] = spec.frame;
      let p0, p1, q0, q1;
      if (dir === 'up') { p0 = x1; p1 = x2; q0 = z1; q1 = z2; }
      else if (dir === 'down') { p0 = x1; p1 = x2; q0 = b0 + b1 - z2; q1 = b0 + b1 - z1; }
      else if (dir === 'north') { p0 = a0 + a1 - x2; p1 = a0 + a1 - x1; q0 = b0 + b1 - y2; q1 = b0 + b1 - y1; }
      else if (dir === 'south') { p0 = x1; p1 = x2; q0 = b0 + b1 - y2; q1 = b0 + b1 - y1; }
      else if (dir === 'east') { p0 = a0 + a1 - z2; p1 = a0 + a1 - z1; q0 = b0 + b1 - y2; q1 = b0 + b1 - y1; }
      else { p0 = z1; p1 = z2; q0 = b0 + b1 - y2; q1 = b0 + b1 - y1; }
      const U = v => (r.x + (v - a0) / (a1 - a0) * r.w) * ku0, V = v => (r.y + (v - b0) / (b1 - b0) * r.h) * kv0;
      const f = { uv: [U(p0), V(q0), U(p1), V(q1)].map(v => +Math.max(0, Math.min(16, v)).toFixed(4)), texture: '#0' };
      return f;
    }
    const ox = spec.ox || 0, oy = spec.oy || 0;
    const w = spec.full ? r.w : Math.max(0.5, Math.min(fw, r.w - ox)), h = spec.full ? r.h : Math.max(0.5, Math.min(fh, r.h - oy));
    const ku = 16 / this.size, kv = 16 / this.sizeH;
    const u1 = (r.x + (spec.full ? 0 : ox)) * ku, v1 = (r.y + (spec.full ? 0 : oy)) * kv;
    let uv = [u1, v1, u1 + w * ku, v1 + h * kv];
    if (spec.flip) uv = [uv[2], uv[1], uv[0], uv[3]];
    const f = { uv: uv.map(v => +v.toFixed(4)), texture: '#0' };
    if (spec.rot) f.rotation = spec.rot;
    return f;
  }
  buildElements() {
    this.pack();
    return this.elements.map(el => {
      const faces = {};
      for (const d of DIRS) {
        let spec;
        if (el.faces[d] !== undefined) spec = el.faces[d];
        else if (el.faces.sides !== undefined && ['north', 'east', 'south', 'west'].includes(d)) spec = el.faces.sides;
        else if (el.faces.ends !== undefined && (d === 'up' || d === 'down')) spec = el.faces.ends;
        else spec = el.faces.all;
        const f = this.faceUV(el, d, spec); if (f) faces[d] = f;
      }
      const o = { name: el.name || undefined, from: el.from.map(r4), to: el.to.map(r4) };
      if (el.rotation) o.rotation = { angle: el.rotation.angle, axis: el.rotation.axis, origin: el.rotation.origin.map(r4) };
      o.faces = faces;
      return o;
    });
  }
}
const r4 = v => +(+v).toFixed(4);

// rotacion de un punto alrededor de un eje (como Minecraft)
function rotPoint(p, axis, angleDeg, origin) {
  const a = angleDeg * Math.PI / 180, c = Math.cos(a), s = Math.sin(a);
  const [x, y, z] = [p[0] - origin[0], p[1] - origin[1], p[2] - origin[2]];
  let r;
  if (axis === 'z') r = [x * c - y * s, x * s + y * c, z];
  else if (axis === 'x') r = [x, y * c - z * s, y * s + z * c];
  else r = [x * c + z * s, y, -x * s + z * c];
  return [r[0] + origin[0], r[1] + origin[1], r[2] + origin[2]];
}
function corners(e) {
  const out = [];
  for (const x of [e.from[0], e.to[0]]) for (const y of [e.from[1], e.to[1]]) for (const z of [e.from[2], e.to[2]]) {
    out.push(e.rotation ? rotPoint([x, y, z], e.rotation.axis, e.rotation.angle, e.rotation.origin) : [x, y, z]);
  }
  return out;
}
function bounds(elements) {
  const min = [Infinity, Infinity, Infinity], max = [-Infinity, -Infinity, -Infinity];
  for (const e of elements) for (const p of corners(e)) for (let i = 0; i < 3; i++) { min[i] = Math.min(min[i], p[i]); max[i] = Math.max(max[i], p[i]); }
  return { min: min.map(v => +v.toFixed(2)), max: max.map(v => +v.toFixed(2)) };
}

// ---------- display ----------
// Matriz de rotacion de Minecraft (ItemTransform: rotationXYZ en grados)
function rotXYZ(v, [ax, ay, az]) {
  const r = d => d * Math.PI / 180;
  let [x, y, z] = v;
  // Rz
  let c = Math.cos(r(az)), s = Math.sin(r(az)); [x, y] = [x * c - y * s, x * s + y * c];
  // Ry
  c = Math.cos(r(ay)); s = Math.sin(r(ay)); [x, z] = [x * c + z * s, -x * s + z * c];
  // Rx
  c = Math.cos(r(ax)); s = Math.sin(r(ax)); [y, z] = [y * c - z * s, y * s + z * c];
  return [x, y, z];
}
// Calcula un slot: rota la caja del modelo (centrada en 8,8,8), elige escala para que el ancho/alto en pantalla
// mida `fit` pixeles, y traslada para que el centro quede en `offset`.
function slot(b, rotation, fit, offset = [0, 0, 0], opts = {}) {
  const pts = [];
  for (const x of [b.min[0], b.max[0]]) for (const y of [b.min[1], b.max[1]]) for (const z of [b.min[2], b.max[2]]) pts.push(rotXYZ([x - 8, y - 8, z - 8], rotation));
  const lo = [0, 1, 2].map(i => Math.min(...pts.map(p => p[i]))), hi = [0, 1, 2].map(i => Math.max(...pts.map(p => p[i])));
  const ext = Math.max(hi[0] - lo[0], hi[1] - lo[1]);
  let s = fit / ext; if (opts.maxScale) s = Math.min(s, opts.maxScale);
  s = +s.toFixed(3);
  const c = [0, 1, 2].map(i => (lo[i] + hi[i]) / 2 * s);
  const t = [0, 1, 2].map(i => +Math.max(-80, Math.min(80, offset[i] - (opts.keepZ && i === 2 ? 0 : c[i]))).toFixed(2));
  const o = {};
  if (rotation.some(v => v)) o.rotation = rotation;
  o.translation = t; o.scale = [s, s, s];
  return o;
}
// Display por defecto segun la forma: 'plato' (bajo y ancho), 'alto' (botella), 'bloque'
function autoDisplay(b, kind = 'plato', over = {}) {
  const d = {};
  if (kind === 'alto') {
    d.gui = slot(b, [15, 225, 0], 15.2, [0, 0, 0]);
    d.ground = slot(b, [0, 0, 0], 7, [0, 4, 0]);
    d.fixed = slot(b, [0, 180, 0], 14, [0, 0, 0]);
    d.thirdperson_righthand = slot(b, [0, 0, 0], 7, [0, 3.5, 1]);
    d.thirdperson_lefthand = slot(b, [0, 0, 0], 7, [0, 3.5, 1]);
    d.firstperson_righthand = slot(b, [0, -20, 0], 8, [1, 3.5, 0]);
    d.firstperson_lefthand = slot(b, [0, 160, 0], 8, [1, 3.5, 0]);
    d.head = slot(b, [0, 0, 0], 10, [0, 13, 0]);
  } else {
    const big = kind === 'bloque';
    d.gui = slot(b, [30, 225, 0], big ? 14.5 : 15.5, [0, 0, 0], { maxScale: 1.6 });
    d.ground = slot(b, [0, 0, 0], 8, [0, 3, 0]);
    d.fixed = slot(b, [-90, 0, 0], 13, [0, 0, -0.5], { keepZ: true });
    d.thirdperson_righthand = slot(b, [75, 45, 0], 7, [0, 2.5, 0]);
    d.thirdperson_lefthand = slot(b, [75, 45, 0], 7, [0, 2.5, 0]);
    d.firstperson_righthand = slot(b, [0, 45, 0], 8, [0, 3, 0]);
    d.firstperson_lefthand = slot(b, [0, 225, 0], 8, [0, 3, 0]);
    d.head = slot(b, [0, 0, 0], 11, [0, 13, 0]);
  }
  for (const [k, v] of Object.entries(over)) d[k] = { ...d[k], ...v };
  return d;
}

// ---------- escritura ----------
function stringify(o) {
  return JSON.stringify(o, null, 2).replace(/\[\s+([-\d.,\s]+?)\s+\]/g, (m, inner) => '[' + inner.split(/,\s*/).join(', ') + ']');
}
function readMedidas() { try { return JSON.parse(fs.readFileSync(MEDIDAS, 'utf8')); } catch { return {}; } }
function writeMedidas(id, b) {
  const all = readMedidas(); all[id] = { min: b.min, max: b.max };
  const sorted = Object.fromEntries(Object.keys(all).sort().map(k => [k, all[k]]));
  fs.mkdirSync(BB_DIR, { recursive: true });
  fs.writeFileSync(MEDIDAS, stringify(sorted) + '\n');
}
// Guarda modelo + textura + medidas.  opts: {kind:'block'|'item', folder:'argentina/comida', texName, display, displayKind, displayOver}
function save(id, m, opts = {}) {
  const kind = opts.kind || 'block', folder = opts.folder || 'argentina/comida', texName = opts.texName || id;
  const elements = m.buildElements();
  const b = bounds(elements);
  const display = opts.display || autoDisplay(b, opts.displayKind || 'plato', opts.displayOver || {});
  const tex = `ultimatefoods:${kind}/${folder}/${texName}`;
  const json = { credit: 'Ultimate Foods - Argentina', texture_size: [m.size, m.sizeH], textures: { 0: tex, particle: tex }, elements, display };
  if (opts.parent) json.parent = opts.parent;
  const mf = path.join(ASSETS, 'models', kind, ...folder.split('/'), id + '.json');
  const tf = path.join(ASSETS, 'textures', kind, ...folder.split('/'), texName + '.png');
  fs.mkdirSync(path.dirname(mf), { recursive: true });
  fs.writeFileSync(mf, stringify(json) + '\n');
  m.paint().save(tf);
  writeMedidas(opts.medidaId || id, b);
  require('./bbmodel').exportBB(id, kind, folder, opts.bbDir ? path.join(ROOT, opts.bbDir) : BB_DIR);
  console.log(`${id}: ${elements.length} cubos, limites ${JSON.stringify(b)} -> ${path.relative(ROOT, mf)}`);
  return { file: mf, json, bounds: b };
}

// ---------- piezas comunes ----------
// Disco "octogonal" sin caras superpuestas: una franja central de fondo completo y dos laterales recortadas.
// faces.up / faces.down (string de region) se proyectan sobre todo el disco; el resto se recorta normal.
function oct(m, cx, cz, y1, y2, w, cut, faces, name, d = w) {
  const fr = [cx - w / 2, cz - d / 2, cx + w / 2, cz + d / 2];
  const F = { ...faces };
  for (const k of ['up', 'down', 'ends']) if (typeof F[k] === 'string') F[k] = { r: F[k], frame: fr };
  if (F.ends) { F.up = F.up || F.ends; F.down = F.down || F.ends; delete F.ends; }
  m.cube([cx - w / 2 + cut, y1, cz - d / 2], [cx + w / 2 - cut, y2, cz + d / 2], F, null, name);
  m.cube([cx - w / 2, y1, cz - d / 2 + cut], [cx - w / 2 + cut, y2, cz + d / 2 - cut], F, null, name + '_o');
  m.cube([cx + w / 2 - cut, y1, cz - d / 2 + cut], [cx + w / 2, y2, cz + d / 2 - cut], F, null, name + '_e');
}
// Escalones de un disco "redondo" de ancho w (x) y fondo d (z): lista de [medio ancho, medio fondo].
function discSteps(w, d, n = 3) {
  const q = v => Math.round(v * 2) / 2;
  const fr = { 2: [[0.72, 1], [1, 0.72]], 3: [[0.5, 1], [0.84, 0.84], [1, 0.5]], 4: [[0.4, 1], [0.72, 0.92], [0.92, 0.72], [1, 0.4]], oct: [[0.42, 1], [0.72, 0.72], [1, 0.42]] }[n];
  return fr.map(([a, b]) => [q(a * w / 2), q(b * d / 2)]);
}
// Disco escalonado sin caras superpuestas (una franja por escalon, simetrica). faces.up/down se proyectan.
function disc(m, cx, cz, y1, y2, w, d, faces, name, n = 3) {
  const st = discSteps(w, d, n);
  const fr = [cx - w / 2, cz - d / 2, cx + w / 2, cz + d / 2];
  const F = { ...faces };
  if (F.ends !== undefined) { if (F.up === undefined) F.up = F.ends; if (F.down === undefined) F.down = F.ends; delete F.ends; }
  for (const k of ['up', 'down']) if (typeof F[k] === 'string') F[k] = { r: F[k], frame: fr };
  let prev = 0;
  st.forEach(([a, b], i) => {
    if (i === 0) m.cube([cx - a, y1, cz - b], [cx + a, y2, cz + b], F, null, name);
    else {
      m.cube([cx - a, y1, cz - b], [cx - prev, y2, cz + b], F, null, name + '_o' + i);
      m.cube([cx + prev, y1, cz - b], [cx + a, y2, cz + b], F, null, name + '_e' + i);
    }
    prev = a;
  });
}
// mascara de un disco para pintar su cara superior: devuelve f(lx,ly,W,H) = distancia al borde en texeles (-1 fuera)
function discMask(w, d, n = 3) {
  const st = discSteps(w, d, n);
  return (lx, ly, W, H) => {
    const inside = (ix, iy) => { if (ix < 0 || iy < 0 || ix >= W || iy >= H) return false; const px = (ix + 0.5) / W * w - w / 2, pz = (iy + 0.5) / H * d - d / 2; return st.some(([a, b]) => Math.abs(px) <= a && Math.abs(pz) <= b); };
    if (!inside(lx, ly)) return -1;
    for (let k = 1; k < 6; k++) if (!inside(lx + k, ly) || !inside(lx - k, ly) || !inside(lx, ly + k) || !inside(lx, ly - k)) return k - 1;
    return 6;
  };
}
// Forma libre hecha de rectangulos [x1,z1,x2,z2] (sin superponerse): cubos con la cara de arriba proyectada sobre `frame`
function slab(m, rects, y1, y2, faces, name, frame) {
  frame = frame || [Math.min(...rects.map(r => r[0])), Math.min(...rects.map(r => r[1])), Math.max(...rects.map(r => r[2])), Math.max(...rects.map(r => r[3]))];
  const F = { ...faces };
  if (F.ends !== undefined) { if (F.up === undefined) F.up = F.ends; if (F.down === undefined) F.down = F.ends; delete F.ends; }
  for (const k of ['up', 'down']) if (typeof F[k] === 'string') F[k] = { r: F[k], frame };
  rects.forEach((r, i) => m.cube([r[0], y1, r[1]], [r[2], y2, r[3]], F, null, name + (i ? '_' + i : '')));
  return frame;
}
function rectsMask(rects, frame) {
  frame = frame || [Math.min(...rects.map(r => r[0])), Math.min(...rects.map(r => r[1])), Math.max(...rects.map(r => r[2])), Math.max(...rects.map(r => r[3]))];
  return (lx, ly, W, H) => {
    const inside = (ix, iy) => { if (ix < 0 || iy < 0 || ix >= W || iy >= H) return false; const px = frame[0] + (ix + 0.5) / W * (frame[2] - frame[0]), pz = frame[1] + (iy + 0.5) / H * (frame[3] - frame[1]); return rects.some(r => px >= r[0] && px <= r[2] && pz >= r[1] && pz <= r[3]); };
    if (!inside(lx, ly)) return -1;
    for (let k = 1; k < 6; k++) if (!inside(lx + k, ly) || !inside(lx - k, ly) || !inside(lx, ly + k) || !inside(lx, ly - k)) return k - 1;
    return 6;
  };
}
// regiones de loza para platos (aro claro, fondo, canto)
// plato redondo: regiones de loza (canto, fondo, cara de arriba con ala) y cubos. La comida se apoya en y = 1.
function regionesPlato(m, { pre = 'plato', w = 14, d = w, base = '#f4f2ec', borde = '#d6d0c4', brillo = '#ffffff', sombra = '#b8b2a6', filete = null, dens = 2 } = {}) {
  const mask = discMask(w, d, 3);
  m.region(pre + '_lado', 32, 2, (x, y) => y === 0 ? mix(base, brillo, 0.5) : sombra);
  m.region(pre + '_fondo', 16, 16, (x, y) => rnd(x, y, 7) > 0.93 ? borde : shade(base, 0.92));
  m.region(pre + '_aro', w * dens, d * dens, (x, y, W, H) => {
    const e = mask(x, y, W, H); if (e < 0) return null;
    if (e === 0) return mix(base, brillo, 0.7);
    if (e === 1) return mix(base, borde, 0.5);
    if (filete && e === 2) return mix(base, filete, 0.75);
    if (e === 3) return mix(base, borde, 0.6);
    return rnd(x, y, 8) > 0.96 ? mix(base, borde, 0.5) : base;
  });
}
function plato(m, { cx = 8, cz = 8, w = 14, d = w, pre = 'plato' } = {}) {
  disc(m, cx, cz, 0, 0.5, w - 4, d - 4, { sides: pre + '_lado', up: null, down: pre + '_fondo' }, pre + '_pie', 2);
  disc(m, cx, cz, 0.5, 1, w, d, { sides: pre + '_lado', up: pre + '_aro', down: pre + '_fondo' }, pre, 3);
  return 1;
}
// Aro octogonal (paredes de bowl, vaso, copa): 4 placas rectas + 4 placas giradas 45 grados.
// R = apotema exterior, t = espesor. faces: {out, in, top, bottom} (regiones) o un objeto de caras tipo cube.
function octRing(m, cx, cz, y1, y2, R, t, faces, name) {
  const L = 2 * R * Math.tan(Math.PI / 8) + t * 0.42;
  const F = typeof faces === 'string' ? { all: faces } : faces;
  const side = (outDir) => { const o = { ...F }; if (F.out !== undefined) { const inn = { north: 'south', south: 'north', east: 'west', west: 'east' }[outDir]; o[outDir] = F.out; o[inn] = F.in !== undefined ? F.in : F.out; const perp = ['north', 'south'].includes(outDir) ? ['east', 'west'] : ['north', 'south']; for (const p of perp) o[p] = F.edge !== undefined ? F.edge : F.out; delete o.out; delete o.in; delete o.edge; } if (F.top !== undefined) { o.up = F.top; delete o.top; } if (F.bottom !== undefined) { o.down = F.bottom; delete o.bottom; } return o; };
  m.cube([cx - L / 2, y1, cz - R], [cx + L / 2, y2, cz - R + t], side('north'), null, name + '_n');
  m.cube([cx - L / 2, y1, cz + R - t], [cx + L / 2, y2, cz + R], side('south'), null, name + '_s');
  m.cube([cx - R, y1, cz - L / 2], [cx - R + t, y2, cz + L / 2], side('west'), null, name + '_o');
  m.cube([cx + R - t, y1, cz - L / 2], [cx + R, y2, cz + L / 2], side('east'), null, name + '_e');
  const dd = (R - t / 2) / Math.SQRT2;
  for (const [sx, sz, ang, out, nm] of [[1, -1, -45, 'north', 'ne'], [-1, -1, 45, 'north', 'no'], [1, 1, 45, 'south', 'se'], [-1, 1, -45, 'south', 'so']]) {
    const px = cx + sx * dd, pz = cz + sz * dd;
    m.cube([px - L / 2, y1, pz - t / 2], [px + L / 2, y2, pz + t / 2], side(out), { angle: ang, axis: 'y', origin: [px, y1, pz] }, name + '_' + nm);
  }
}

module.exports = { ROOT, ASSETS, BB_DIR, Canvas, Img, ModelBuilder, hex, mix, shade, rnd, pick, blob, ramp, rotPoint, bounds, autoDisplay, slot, save, oct, disc, discSteps, discMask, slab, rectsMask, plato, regionesPlato, octRing, encodePNG, decodePNG, stringify };
