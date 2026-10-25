// GUI del libro guia (book_texture de Patchouli, 512x256).
// El libro de adentro (marco celeste y blanco, paginas, costura) esta dibujado de cero. Los iconitos sueltos
// (flechas, marcadores, buscador) tienen que estar en el mismo lugar que en la plantilla de Patchouli, asi que
// se toman de ella y se pintan de celeste. Esos iconos son de Patchouli (Vazkii, CC BY-NC-SA 3.0): ver CREDITOS.md.
//   node tools/guia/gen_gui.js [ruta/a/book_green.png]
const fs = require('fs'), path = require('path');
const { decodePNG } = require('../../bbmodels/fisura/comida/generadores/lib.js');
const { encodePNG } = require('../../bbmodels/fisura/generadores/lib.js');

const SALIDA = path.resolve(__dirname, '..', '..', 'src/main/resources/assets/ultimatefoods/textures/gui/guia_libro.png');
let plantilla = process.argv[2];
if (!plantilla) {
  const base = path.join(process.env.USERPROFILE || process.env.HOME, '.gradle/caches/modules-2/files-2.1/vazkii.patchouli/Patchouli');
  const jar = fs.readdirSync(base).flatMap(v => fs.readdirSync(path.join(base, v)).flatMap(h => fs.readdirSync(path.join(base, v, h)).filter(f => f.endsWith('.jar') && !f.includes('sources')).map(f => path.join(base, v, h, f))))[0];
  plantilla = path.join(process.env.TEMP || '/tmp', 'guia_plantilla.png');
  require('child_process').execFileSync('unzip', ['-o', '-j', '-q', jar, 'assets/patchouli/textures/gui/book_green.png', '-d', path.dirname(plantilla)]);
  fs.renameSync(path.join(path.dirname(plantilla), 'book_green.png'), plantilla);
}
const im = decodePNG(plantilla);
const { w, h } = im;
const src = Buffer.from(im.d);
const out = Buffer.alloc(w * h * 4);

const hash = (x, y, s = 0) => { let v = (x * 374761393 + y * 668265263 + s * 2147483647) | 0; v = (v ^ (v >>> 13)) * 1274126177 | 0; return ((v ^ (v >>> 16)) >>> 0) / 4294967295; };
const mezcla = (a, b, t) => a.map((v, i) => Math.round(v + (b[i] - v) * t));
const CEL = [23, 168, 224], CEL_OSC = [11, 95, 134], BLANCO = [255, 255, 255], PAPEL = [251, 248, 238];

// ---- libro: se conserva la silueta (alfa) de la plantilla y se pinta todo de nuevo ----
const PAG = { x1: 8, y1: 7, x2: 263, y2: 170 };
function libro(x, y) {
  const dentro = x >= PAG.x1 && x <= PAG.x2 && y >= PAG.y1 && y <= PAG.y2;
  if (!dentro) {
    // tapa de tela celeste con trama; el borde de afuera mas oscuro
    const borde = Math.min(x, y, 271 - x, 179 - y) < 2;
    const trama = ((x % 3 === 0 ? 1.04 : 1) * (y % 3 === 0 ? 0.96 : 1)) * (0.97 + hash(x, y, 1) * 0.06);
    const base = borde ? CEL_OSC : CEL;
    // linea blanca fina alrededor de las paginas (como el filete de la bandera)
    if (x === PAG.x1 - 1 || x === PAG.x2 + 1 || y === PAG.y1 - 1 || y === PAG.y2 + 1) return [...BLANCO.map(v => v * 0.95), 255].map(Math.round);
    return [...base.map(v => Math.max(0, Math.min(255, Math.round(v * trama)))), 255];
  }
  // paginas: papel claro, mas sombra cerca de la costura central y de los bordes
  const dc = Math.abs(x - 135.5), de = Math.min(x - PAG.x1, PAG.x2 - x, y - PAG.y1, PAG.y2 - y);
  let sombra = 0;
  if (dc < 14) sombra += (14 - dc) / 14 * 0.1;
  if (de < 4) sombra += (4 - de) / 4 * 0.05;
  let c = mezcla(PAPEL, [214, 206, 188], sombra + hash(x, y, 2) * 0.03);
  // costura central: guiones celestes
  if ((x === 135 || x === 136) && y % 6 < 4) c = [40, 140, 190];
  // rombitos en las cuatro esquinas de las paginas
  const esq = (cx, cy) => Math.abs(x - cx) + Math.abs(y - cy) <= 3 && Math.abs(x - cx) + Math.abs(y - cy) >= 2;
  if (esq(PAG.x1 + 5, PAG.y1 + 5) || esq(PAG.x2 - 5, PAG.y1 + 5) || esq(PAG.x1 + 5, PAG.y2 - 5) || esq(PAG.x2 - 5, PAG.y2 - 5)) c = [150, 200, 226];
  return [...c, 255];
}
// ---- el resto de la plantilla (iconos, cinta del nombre, buscador): se pinta del verde al celeste ----
function recolor(r, g, b, a) {
  if (a === 0) return [0, 0, 0, 0];
  if (g > r + 8 && g >= b) {                 // verde oscuro de la plantilla -> celeste oscuro
    const v = (r + g + b) / 3 / 255;
    return [Math.round(14 + v * 80), Math.round(95 + v * 150), Math.round(134 + v * 110), a];
  }
  return [r, g, b, a];                        // crema, dorado, gris, rojo, naranja: se quedan
}
for (let y = 0; y < h; y++) for (let x = 0; x < w; x++) {
  const i = (y * w + x) * 4;
  const a = src[i + 3];
  let c;
  if (x < 272 && y < 180 && a > 0) c = libro(x, y);
  else c = recolor(src[i], src[i + 1], src[i + 2], a);
  out.set(c, i);
}
fs.mkdirSync(path.dirname(SALIDA), { recursive: true });
fs.writeFileSync(SALIDA, encodePNG(w, h, out));
console.log('GUI escrita:', SALIDA);
