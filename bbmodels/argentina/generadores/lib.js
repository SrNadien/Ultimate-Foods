// Libreria minima: PNG RGBA, lienzo con pintores, empaquetado de regiones y modelos Java de vanilla.
const fs = require('fs'), path = require('path'), zlib = require('zlib');

const REPO = 'E:/modsmios/UltimateFoodsMod/.claude/worktrees/agent-a2b36bc8db13b9653/src/main/resources/assets/ultimatefoods';

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
  const ihdr = Buffer.alloc(13); ihdr.writeUInt32BE(w, 0); ihdr.writeUInt32BE(h, 4); ihdr[8] = 8; ihdr[9] = 6; ihdr[10] = 0; ihdr[11] = 0; ihdr[12] = 0;
  return Buffer.concat([Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]), chunk('IHDR', ihdr), chunk('IDAT', zlib.deflateSync(raw, { level: 9 })), chunk('IEND', Buffer.alloc(0))]);
}

// ---------- color / ruido ----------
function hex(c) { if (Array.isArray(c)) return c; c = c.replace('#', ''); const a = c.length === 8 ? parseInt(c.slice(6, 8), 16) : 255; return [parseInt(c.slice(0, 2), 16), parseInt(c.slice(2, 4), 16), parseInt(c.slice(4, 6), 16), a]; }
function mix(a, b, t) { a = hex(a); b = hex(b); return a.map((v, i) => Math.round(v + (b[i] - v) * t)); }
function shade(c, f) { c = hex(c); return [Math.max(0, Math.min(255, Math.round(c[0] * f))), Math.max(0, Math.min(255, Math.round(c[1] * f))), Math.max(0, Math.min(255, Math.round(c[2] * f))), c[3]]; }
function rnd(x, y, s = 0) { let h = (x * 374761393 + y * 668265263 + s * 2147483647) | 0; h = Math.imul(h ^ (h >>> 13), 1274126177); h ^= h >>> 16; return ((h >>> 0) % 100000) / 100000; }
function pick(list, x, y, s) { return list[Math.floor(rnd(x, y, s) * list.length)]; }

class Canvas {
  constructor(w, h) { this.w = w; this.h = h; this.d = Buffer.alloc(w * h * 4); }
  set(x, y, c) { x = Math.floor(x); y = Math.floor(y); if (x < 0 || y < 0 || x >= this.w || y >= this.h) return; c = hex(c); this.d.set(c, (y * this.w + x) * 4); }
  get(x, y) { const i = (y * this.w + x) * 4; return [...this.d.slice(i, i + 4)]; }
  save(file) { fs.mkdirSync(path.dirname(file), { recursive: true }); fs.writeFileSync(file, encodePNG(this.w, this.h, this.d)); }
}

// ---------- modelo ----------
// regiones: {name: {w,h,paint(lx,ly,w,h) -> color}} ; se empaquetan solas en el atlas.
class ModelBuilder {
  constructor({ size = 64, density = 2 } = {}) { this.size = size; this.dens = density; this.regions = {}; this.order = []; this.elements = []; }
  region(name, w, h, paint) { this.regions[name] = { w, h, paint }; this.order.push(name); return this; }
  pack() {
    this.packed = true;
    // estanteria simple, de mas alto a mas bajo
    const names = [...this.order].sort((a, b) => this.regions[b].h - this.regions[a].h);
    let x = 0, y = 0, rowH = 0;
    for (const n of names) {
      const r = this.regions[n];
      if (x + r.w > this.size) { x = 0; y += rowH; rowH = 0; }
      if (y + r.h > this.size) throw new Error('atlas lleno en ' + n);
      r.x = x; r.y = y; x += r.w; rowH = Math.max(rowH, r.h);
    }
  }
  paint() {
    if (!this.packed) this.pack();
    const c = new Canvas(this.size, this.size);
    for (const n of this.order) { const r = this.regions[n]; for (let ly = 0; ly < r.h; ly++) for (let lx = 0; lx < r.w; lx++) { const col = r.paint(lx, ly, r.w, r.h); if (col) c.set(r.x + lx, r.y + ly, col); } }
    return c;
  }
  // faces: {all: spec} o {north: spec, ...}; spec = 'region' | {r, ox, oy, rot, full} | null (cara omitida)
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
    const ox = spec.ox || 0, oy = spec.oy || 0;
    let w = spec.full ? r.w : Math.min(fw, r.w - ox), h = spec.full ? r.h : Math.min(fh, r.h - oy);
    const k = 16 / this.size;
    const u1 = (r.x + (spec.full ? 0 : ox)) * k, v1 = (r.y + (spec.full ? 0 : oy)) * k;
    const uv = [u1, v1, u1 + w * k, v1 + h * k].map(v => +v.toFixed(4));
    const f = { uv, texture: '#0' };
    if (spec.rot) f.rotation = spec.rot;
    if (spec.data) f.neoforge_data = spec.data;
    return f;
  }
  toJSON({ texture, display, particle }) {
    this.pack();
    const DIRS = ['north', 'east', 'south', 'west', 'up', 'down'];
    const elements = this.elements.map(el => {
      const faces = {};
      for (const d of DIRS) {
        const spec = el.faces[d] !== undefined ? el.faces[d] : el.faces.all;
        if (el.faces.sides !== undefined && el.faces[d] === undefined && ['north', 'east', 'south', 'west'].includes(d)) { const f = this.faceUV(el, d, el.faces.sides); if (f) faces[d] = f; continue; }
        if (el.faces.ends !== undefined && el.faces[d] === undefined && (d === 'up' || d === 'down')) { const f = this.faceUV(el, d, el.faces.ends); if (f) faces[d] = f; continue; }
        const f = this.faceUV(el, d, spec); if (f) faces[d] = f;
      }
      const o = { from: el.from.map(r4), to: el.to.map(r4) };
      if (el.name) o.name = el.name;
      if (el.rotation) o.rotation = { angle: el.rotation.angle, axis: el.rotation.axis, origin: el.rotation.origin.map(r4) };
      o.faces = faces;
      return o;
    });
    return { credit: 'Ultimate Foods - Argentina', texture_size: [this.size, this.size], textures: { 0: texture, particle: particle || texture }, elements, display };
  }
}
const r4 = v => +(+v).toFixed(4);

function writeModel(kind, id, json) {
  const f = `${REPO}/models/${kind}/argentina/${id}.json`;
  fs.mkdirSync(path.dirname(f), { recursive: true });
  fs.writeFileSync(f, stringify(json) + '\n');
  return f;
}
// JSON compacto estilo Blockbench: arrays numericos en una linea
function stringify(o) {
  return JSON.stringify(o, null, 2).replace(/\[\s+([-\d.,\s]+?)\s+\]/g, (m, inner) => '[' + inner.split(/,\s*/).join(', ') + ']');
}
function writeTexture(kind, id, canvas) { const f = `${REPO}/textures/${kind}/argentina/${id}.png`; canvas.save(f); return f; }

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

// Imagen local para regiones con dibujo libre (texto, logos)
class Img {
  constructor(w, h, bg) { this.w = w; this.h = h; this.px = new Array(w * h).fill(null); if (bg) this.fill(bg); }
  set(x, y, c) { x = Math.round(x); y = Math.round(y); if (x < 0 || y < 0 || x >= this.w || y >= this.h) return; this.px[y * this.w + x] = typeof c === 'function' ? c(x, y, this.px[y * this.w + x]) : c; }
  get(x, y) { return this.px[y * this.w + x]; }
  fill(fn) { for (let y = 0; y < this.h; y++) for (let x = 0; x < this.w; x++) this.set(x, y, typeof fn === 'function' ? fn(x, y) : fn); return this; }
  rect(x, y, w, h, c) { for (let j = 0; j < h; j++) for (let i = 0; i < w; i++) this.set(x + i, y + j, c); return this; }
  text(font, s, x, y, c, gap = 1) { const { textPixels } = require('./font'); for (const [px, py] of textPixels(font, s, gap)) this.set(x + px, y + py, c); return this; }
  painter() { return (x, y) => this.px[y * this.w + x]; }
}

module.exports = { REPO, Canvas, ModelBuilder, Img, hex, mix, shade, rnd, pick, writeModel, writeTexture, rotPoint, encodePNG };
