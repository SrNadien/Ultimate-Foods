// Textura del tablon de yerba mate: tablones verdes (#dbfab8) con el mismo dibujo de los tablones de vanilla.
//   node tools/texturas/gen_tablon.js
const fs = require('fs'), zlib = require('zlib'), path = require('path');
const SALIDA = path.resolve(__dirname, '..', '..', 'src/main/resources/assets/ultimatefoods/textures/block/argentina/yerba_mate_planks.png');

function crc32(b) { const t = []; for (let n = 0; n < 256; n++) { let c = n; for (let k = 0; k < 8; k++) c = c & 1 ? 0xEDB88320 ^ (c >>> 1) : c >>> 1; t[n] = c >>> 0; } let c = 0xFFFFFFFF; for (const x of b) c = t[(c ^ x) & 255] ^ (c >>> 8); return (c ^ 0xFFFFFFFF) >>> 0; }
function chunk(ty, d) { const l = Buffer.alloc(4); l.writeUInt32BE(d.length); const td = Buffer.concat([Buffer.from(ty), d]); const c = Buffer.alloc(4); c.writeUInt32BE(crc32(td)); return Buffer.concat([l, td, c]); }
function png(w, h, px) {
  const raw = Buffer.alloc(h * (1 + w * 4));
  for (let y = 0; y < h; y++) { raw[y * (1 + w * 4)] = 0; for (let x = 0; x < w; x++) { const c = px(x, y); const o = y * (1 + w * 4) + 1 + x * 4; raw[o] = c[0]; raw[o + 1] = c[1]; raw[o + 2] = c[2]; raw[o + 3] = 255; } }
  const ih = Buffer.alloc(13); ih.writeUInt32BE(w, 0); ih.writeUInt32BE(h, 4); ih[8] = 8; ih[9] = 6;
  return Buffer.concat([Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]), chunk('IHDR', ih), chunk('IDAT', zlib.deflateSync(raw)), chunk('IEND', Buffer.alloc(0))]);
}
const hash = (x, y, s = 0) => { let h = (x * 374761393 + y * 668265263 + s * 2147483647) | 0; h = (h ^ (h >>> 13)) * 1274126177 | 0; return ((h ^ (h >>> 16)) >>> 0) / 4294967295; };
const BASE = [0xdb, 0xfa, 0xb8];
const tono = f => BASE.map(v => Math.max(0, Math.min(255, Math.round(v * f))));

// cuatro tablones de 4 px de alto; cada uno con su veta, y una juntura oscura abajo
const px = (x, y) => {
  const fila = Math.floor(y / 4), dy = y % 4;
  const corte = (fila * 5 + 3) % 16;                // donde se cortan los tablones, distinto en cada fila
  const x2 = (x + 16 - corte) % 16;
  if (dy === 3) return tono(0.78);                  // juntura horizontal
  if (x2 === 0) return tono(0.82);                  // juntura vertical entre dos tablones de la misma fila
  const veta = hash(Math.floor(x2 / 3), fila, 7) * 0.08 + hash(x, y, 3) * 0.05;
  const claro = dy === 0 ? 1.03 : 1.0;
  return tono(Math.min(1.04, claro - veta + 0.04));
};
fs.mkdirSync(path.dirname(SALIDA), { recursive: true });
fs.writeFileSync(SALIDA, png(16, 16, px));
console.log('textura escrita:', SALIDA);
