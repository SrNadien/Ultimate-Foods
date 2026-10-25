// OJO: el modelo final del libro se pulio a mano en Blockbench (marcador, lomo redondeado, cabezales). Correr este script lo pisa.
// El libro guia ("Recetas De La Buela"): tapa dura celeste y blanca, como la bandera, con el titulo y las paginas a la vista.
// Inspirado un 10 % en un libro de recetas criollas de tapa albiceleste; el texto y el dibujo son propios.
//   node bbmodels/fisura/generadores/gen_libro.js
const path = require('path');
const { ModelBuilder, Img, rnd, shade, writeModel, writeTexture } = require('./lib');
const { exportBB } = require('../comida/generadores/bbmodel');

// ---- fuente 3x5 de mayusculas (la fuente de font.js solo trae las letras de la yerba) ----
const F = {
  A: ['010', '101', '111', '101', '101'], B: ['110', '101', '110', '101', '110'], C: ['011', '100', '100', '100', '011'],
  D: ['110', '101', '101', '101', '110'], E: ['111', '100', '110', '100', '111'], F: ['111', '100', '110', '100', '100'],
  G: ['011', '100', '101', '101', '011'], H: ['101', '101', '111', '101', '101'], I: ['111', '010', '010', '010', '111'],
  L: ['100', '100', '100', '100', '111'], M: ['101', '111', '111', '101', '101'], N: ['110', '101', '101', '101', '101'],
  O: ['010', '101', '101', '101', '010'], P: ['110', '101', '110', '100', '100'], R: ['110', '101', '110', '101', '101'],
  S: ['011', '100', '010', '001', '110'], T: ['111', '010', '010', '010', '010'], U: ['101', '101', '101', '101', '111'],
  ' ': ['00', '00', '00', '00', '00']
};
const ancho = s => [...s].reduce((w, ch) => w + F[ch][0].length + 1, -1);
// escribe texto 3x5 con escala (sx, sy); "negrita" repite cada columna un pixel mas
function texto(img, s, cx, y, color, sx = 1, sy = 1, negrita = false) {
  const total = ancho(s) * sx + (negrita ? 1 : 0);
  let x = Math.round(cx - total / 2);
  for (const ch of s) {
    const g = F[ch];
    g.forEach((fila, j) => [...fila].forEach((b, i) => {
      if (b !== '1') return;
      for (let a = 0; a < sx + (negrita ? 1 : 0); a++) for (let k = 0; k < sy; k++) img.set(x + i * sx + a, y + j * sy + k, color);
    }));
    x += (g[0].length + 1) * sx;
  }
}

const CEL = '#17a8e0', CEL_OSC = '#0f8fc4', BLANCO = '#ececea', TINTA = '#161616', CREMA = '#e8dfc8';
const D = 6, W = 10, H = 14, T = 3;         // densidad y medidas del libro (en pixeles de 1/16)
const FW = W * D, FH = H * D;               // 60 x 84
const BANDA1 = 24, BANDA2 = 60;             // donde termina la banda de arriba y donde empieza la de abajo

// tela con trama: leve ruido y hilos cruzados
function tela(base, x, y, s = 1) {
  const hilo = ((x + s) % 3 === 0 ? 1.03 : 1) * ((y + s) % 3 === 0 ? 0.97 : 1);
  return shade(base, hilo * (0.97 + rnd(x, y, 40 + s) * 0.06));
}
function franjas(w, h, y1, y2) {
  return (x, y) => (y < y1 || y >= y2 ? tela(CEL, x, y) : tela(BLANCO, x, y, 2));
}

function tapa() {
  const img = new Img(FW, FH, franjas(FW, FH, BANDA1, BANDA2));
  // borde de la tapa, un poco mas oscuro
  for (let y = 0; y < FH; y++) { img.set(0, y, img.get(0, y) && shade(img.get(0, y), 0.9)); img.set(FW - 1, y, shade(img.get(FW - 1, y), 0.9)); }
  const cx = FW / 2;
  texto(img, 'ULTIMATE FOODS', cx, 10, TINTA);
  texto(img, 'RECETAS', cx, 31, TINTA);
  texto(img, 'DE LA BUELA', cx, 40, TINTA, 1, 2, true);
  texto(img, 'RE FISURA', cx, 71, TINTA);
  return img;
}
function contratapa() {
  const img = new Img(FW, FH, franjas(FW, FH, BANDA1, BANDA2));
  texto(img, 'ULTIMATE FOODS', FW / 2, 71, TINTA);
  return img;
}
function lomo() {
  const w = T * D, img = new Img(w, FH, franjas(w, FH, BANDA1, BANDA2));
  // el titulo va "parado": una letra debajo de otra, centradas
  const titulo = 'BUELA'; let y = 31;
  for (const ch of titulo) { texto(img, ch, w / 2, y, TINTA); y += 6; if (y > BANDA2 - 5) break; }
  return img;
}
function hojasLado() {
  const w = 2 * D, img = new Img(w, FH - 2 * 3, (x, y) => (y % 2 === 0 ? shade('#f4f1e8', 0.93 + rnd(x, y, 51) * 0.04) : shade('#fbf9f2', 0.97 + rnd(x, y, 52) * 0.03)));
  return img;
}
function hojasCanto() {
  const w = W * D - 3, img = new Img(w, 2 * D, (x, y) => (x % 2 === 0 ? '#e6e2d4' : '#faf8f0'));
  return img;
}

function regiones(m) {
  const t = tapa(), c = contratapa(), l = lomo(), hl = hojasLado(), hc = hojasCanto();
  m.region('tapa', t.w, t.h, t.painter());
  m.region('contratapa', c.w, c.h, c.painter());
  m.region('lomo', l.w, l.h, l.painter());
  m.region('hojas_lado', hl.w, hl.h, hl.painter());
  m.region('hojas_canto', hc.w, hc.h, hc.painter());
  m.region('borde', 6, 6, (x, y) => tela(CEL_OSC, x, y, 3));
  m.region('crema', 6, 6, (x, y) => shade(CREMA, 0.96 + rnd(x, y, 60) * 0.06));
}

// Repinta SOLO la tapa, la contratapa y el lomo sobre la textura que ya esta (pulida a mano en Blockbench):
//   node bbmodels/fisura/generadores/gen_libro.js --solo-tapa
function repintarTapa() {
  const fs = require('fs');
  const { decodePNG } = require('../comida/generadores/lib');
  const { encodePNG } = require('./lib');
  const archivo = path.resolve(__dirname, '..', '..', '..', 'src/main/resources/assets/ultimatefoods/textures/item/fisura/guia_libro.png');
  const m = new ModelBuilder({ size: 256, density: D });
  regiones(m);
  m.pack();
  const im = decodePNG(archivo);
  const d = Buffer.from(im.d);
  for (const nombre of ['tapa', 'contratapa', 'lomo']) {
    const r = m.regions[nombre];
    for (let ly = 0; ly < r.h; ly++) for (let lx = 0; lx < r.w; lx++) {
      const c = r.paint(lx, ly, r.w, r.h);
      if (!c) continue;
      const rgba = Array.isArray(c) ? (c.length === 3 ? [...c, 255] : c) : null;
      if (!rgba) continue;
      d.set(rgba, ((r.y + ly) * im.w + (r.x + lx)) * 4);
    }
  }
  fs.writeFileSync(archivo, encodePNG(im.w, im.h, d));
  return archivo;
}

function build() {
  const m = new ModelBuilder({ size: 256, density: D });
  regiones(m);

  const x1 = 3, x2 = 3 + W, z1 = 8 - T / 2, z2 = 8 + T / 2, g = 0.4;
  // lomo (a la izquierda), tapa de adelante, tapa de atras y el taco de hojas en el medio
  m.cube([x1, 0, z1], [x1 + 0.5, H, z2], { west: 'lomo', east: 'crema', north: 'borde', south: 'borde', up: 'borde', down: 'borde' }, null, 'lomo');
  // el item se ve de frente por su cara SUR (como cualquier item plano): ahi va la tapa de adelante
  m.cube([x1 + 0.5, 0, z2 - g], [x2, H, z2], { south: 'tapa', north: 'crema', east: 'borde', up: 'borde', down: 'borde' }, null, 'tapa');
  m.cube([x1 + 0.5, 0, z1], [x2, H, z1 + g], { north: 'contratapa', south: 'crema', east: 'borde', up: 'borde', down: 'borde' }, null, 'contratapa');
  m.cube([x1 + 0.5, g, z1 + g], [x2 - g, H - g, z2 - g], { east: 'hojas_lado', up: 'hojas_canto', down: 'hojas_canto', north: 'crema', south: 'crema', west: 'crema' }, null, 'hojas');

  const display = {
    thirdperson_righthand: { rotation: [75, 45, 0], translation: [0, 2, 1.5], scale: [0.45, 0.45, 0.45] },
    thirdperson_lefthand: { rotation: [75, 45, 0], translation: [0, 2, 1.5], scale: [0.45, 0.45, 0.45] },
    firstperson_righthand: { rotation: [0, -25, 0], translation: [1, 3, 0], scale: [0.45, 0.45, 0.45] },
    firstperson_lefthand: { rotation: [0, -25, 0], translation: [1, 3, 0], scale: [0.45, 0.45, 0.45] },
    ground: { translation: [0, 2, 0], scale: [0.5, 0.5, 0.5] },
    gui: { rotation: [18, -32, 0], translation: [0, -0.5, 0], scale: [0.95, 0.95, 0.95] },
    head: { translation: [0, 14, 0] },
    fixed: { rotation: [0, 0, 0], scale: [0.95, 0.95, 0.95] }
  };
  const tex = 'ultimatefoods:item/fisura/guia_libro';
  const json = m.toJSON({ texture: tex, display });
  writeTexture('item', 'guia_libro', m.paint());
  const f = writeModel('item', 'guia_libro', json);
  // Patchouli busca el modelo del libro como ultimatefoods:item/guia_buela: hereda del 3D de arriba
  const fs = require('fs');
  const raiz = path.resolve(__dirname, '..', '..', '..', 'src/main/resources/assets/ultimatefoods/models/item');
  fs.writeFileSync(path.join(raiz, 'guia_buela.json'), JSON.stringify({ parent: 'ultimatefoods:item/fisura/guia_libro' }, null, 2) + '\n');
  console.log(exportBB('guia_libro', 'item', 'fisura', path.resolve(__dirname, '..')));
  return f;
}
console.log(process.argv.includes('--solo-tapa') ? repintarTapa() : build());
