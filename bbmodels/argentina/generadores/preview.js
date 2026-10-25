// Amplia un PNG generado por lib.js (filtro 0) para mirarlo: node preview.js in.png escala out.png
const fs = require('fs'), zlib = require('zlib');
const { encodePNG } = require('./lib');
const [, , inp, sc = '6', out] = process.argv;
const b = fs.readFileSync(inp);
let p = 8, w, h, idat = [];
while (p < b.length) { const len = b.readUInt32BE(p), type = b.toString('ascii', p + 4, p + 8), data = b.slice(p + 8, p + 8 + len); if (type === 'IHDR') { w = data.readUInt32BE(0); h = data.readUInt32BE(4); } if (type === 'IDAT') idat.push(data); p += 12 + len; }
const raw = zlib.inflateSync(Buffer.concat(idat));
const s = +sc, W = w * s, H = h * s, o = Buffer.alloc(W * H * 4);
for (let y = 0; y < H; y++) for (let x = 0; x < W; x++) {
  const sx = Math.floor(x / s), sy = Math.floor(y / s), si = sy * (w * 4 + 1) + 1 + sx * 4, di = (y * W + x) * 4;
  const a = raw[si + 3] / 255, chk = ((x >> 3) + (y >> 3)) % 2 ? 200 : 235;
  for (let k = 0; k < 3; k++) o[di + k] = Math.round(raw[si + k] * a + chk * (1 - a));
  o[di + 3] = 255;
}
fs.writeFileSync(out, encodePNG(W, H, o));
