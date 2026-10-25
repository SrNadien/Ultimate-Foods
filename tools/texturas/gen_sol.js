// Textura del sol (64x64): Sol de Mayo, con rayos rectos y ondulados, y una cara en el disco central.
// La cara sale de cara_sol_38x41.rgb (38x41, RGB crudo, sacada de la foto que mando el dueño del mod).
//   node tools/texturas/gen_sol.js
const fs = require('fs'), zlib = require('zlib'), path = require('path');
const SALIDA = path.resolve(__dirname, '..', '..', 'src/main/resources/assets/minecraft/textures/environment/sun.png');
const CARA = fs.readFileSync(path.join(__dirname, 'cara_sol_38x41.rgb')); const CW = 38, CH = 41;

function crc32(b) { const t = []; for (let n = 0; n < 256; n++) { let c = n; for (let k = 0; k < 8; k++) c = c & 1 ? 0xEDB88320 ^ (c >>> 1) : c >>> 1; t[n] = c >>> 0; } let c = 0xFFFFFFFF; for (const x of b) c = t[(c ^ x) & 255] ^ (c >>> 8); return (c ^ 0xFFFFFFFF) >>> 0; }
function chunk(ty, d) { const l = Buffer.alloc(4); l.writeUInt32BE(d.length); const td = Buffer.concat([Buffer.from(ty), d]); const c = Buffer.alloc(4); c.writeUInt32BE(crc32(td)); return Buffer.concat([l, td, c]); }
function png(w, h, px) {
  const raw = Buffer.alloc(h * (1 + w * 4));
  for (let y = 0; y < h; y++) { raw[y * (1 + w * 4)] = 0; for (let x = 0; x < w; x++) { const c = px(x, y) || [0, 0, 0, 0]; const o = y * (1 + w * 4) + 1 + x * 4; raw[o] = c[0]; raw[o + 1] = c[1]; raw[o + 2] = c[2]; raw[o + 3] = c[3] === undefined ? 255 : c[3]; } }
  const ih = Buffer.alloc(13); ih.writeUInt32BE(w, 0); ih.writeUInt32BE(h, 4); ih[8] = 8; ih[9] = 6;
  return Buffer.concat([Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]), chunk('IHDR', ih), chunk('IDAT', zlib.deflateSync(raw)), chunk('IEND', Buffer.alloc(0))]);
}
const mezcla = (a, b, t) => a.map((v, i) => Math.round(v + (b[i] - v) * t));

const N = 64, C = (N - 1) / 2, RCARA = 19, RBORDE = 20.7;
const OSC = [150, 85, 0], AMB = [255, 150, 10], ORO = [255, 200, 40];
const px = (x, y) => {
  const dx = x - C, dy = y - C, d = Math.hypot(dx, dy), ang = (Math.atan2(dy, dx) + Math.PI * 2) % (Math.PI * 2);
  const sector = Math.floor(ang / (Math.PI * 2 / 32)), recto = sector % 2 === 0;
  const frac = (ang % (Math.PI * 2 / 32)) / (Math.PI * 2 / 32);
  if (d <= RCARA) {
    // la cara: se muestrea la foto (columna/fila de la cara que cae sobre este pixel del disco)
    const fx = Math.min(CW - 1, Math.max(0, Math.round(dx + CW / 2 - 0.5)));
    const fy = Math.min(CH - 1, Math.max(0, Math.round(dy + CH / 2 - 0.5 - 1)));
    const o = (fy * CW + fx) * 3;
    return [CARA[o], CARA[o + 1], CARA[o + 2], 255];
  }
  if (d <= RBORDE) return [...OSC, 255];
  const largo = recto ? 31 : 29;
  if (d > largo) return null;
  const t = (d - RBORDE) / (largo - RBORDE);
  const centro = 0.5 + (recto ? 0 : 0.28 * Math.sin(t * Math.PI * 3) * (1 - t * 0.4));
  const ancho = 0.5 * (1 - t) * (recto ? 1.0 : 0.85) + 0.02;
  if (Math.abs(frac - centro) > ancho) return null;
  const borde = Math.abs(frac - centro) > ancho - 0.1;
  return [...(borde ? OSC : mezcla(AMB, ORO, 1 - t)), 255];
};
fs.mkdirSync(path.dirname(SALIDA), { recursive: true });
fs.writeFileSync(SALIDA, png(N, N, px));
console.log('sun.png 64x64 con cara');
