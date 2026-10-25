// Paquete de Yerba Mate Verdeflor 1 kg: ladrillo amarillo, "verdeflor" verde oscuro, gran margarita blanca con centro verde y aro dorado.
const { ModelBuilder, Img, mix, shade, rnd, pick, writeModel, writeTexture } = require('./lib');
const { F35, FLOW, textWidth } = require('./font');

const YEL = '#f5c400', YEL2 = '#ebb800', YELD = '#d9a400', GRN = '#1d4a2a', GRN2 = '#2f6d3c', WHITE = '#fbfbf6', GOLD = '#caa232';
const D = 5;
const W = 9, H = 14, P = 5; // ancho, alto, profundidad (px)

function yellowNoise(x, y) { const r = rnd(x, y, 31); return r > 0.92 ? YEL2 : (r < 0.04 ? '#ffd21a' : YEL); }

function front() {
  const w = W * D, h = H * D, img = new Img(w, h, yellowNoise);
  // pliegues del envase (bordes mas oscuros)
  for (let y = 0; y < h; y++) { img.set(0, y, YELD); img.set(w - 1, y, YELD); }
  for (let x = 0; x < w; x++) { img.set(x, 0, YELD); img.set(x, h - 1, YELD); }
  // YERBA MATE
  const t1 = textWidth(F35, 'YERBA'), t2 = textWidth(F35, 'MATE');
  const tx = Math.floor((w - (t1 + 2 + t2)) / 2);
  img.text(F35, 'YERBA', tx, 2, GRN); img.text(F35, 'MATE', tx + t1 + 2, 2, GRN);
  // linea "elaborada con palo"
  for (let x = 14; x < w - 14; x += 2) img.set(x, 8, GRN2);
  // marca "verdeflor"
  const s = 'verdeflor', tw = textWidth(FLOW, s, 1), bx = Math.floor((w - tw) / 2), by = 10;
  { const { textPixels } = require('./font'); for (const [px, py] of textPixels(FLOW, s, 1)) { img.set(bx + px, by + py * 2, GRN); img.set(bx + px, by + py * 2 + 1, GRN); } }
  // la "o" es una florcita: centro rojo/amarillo
  let ox = bx; for (const ch of 'verdefl') ox += FLOW[ch][0].length + 1;
  for (let yy = by + 4; yy <= by + 9; yy++) for (let xx = ox + 1; xx <= ox + 2; xx++) img.set(xx, yy, (yy === by + 6 || yy === by + 7) ? '#d83a1e' : '#f0b81c');
  // margarita
  const cx = w / 2 - 0.5, cy = 43, N = 12;
  const R = th => 14.2 + 3.6 * Math.sqrt(Math.abs(Math.cos(N / 2 * th)));
  for (let y = 0; y < h; y++) for (let x = 0; x < w; x++) {
    const dx = x - cx, dy = y - cy, r = Math.hypot(dx, dy), th = Math.atan2(dy, dx);
    const rr = R(th);
    // sombra verde (corrida abajo/izquierda)
    const sdx = x - (cx + 1.3), sdy = y - (cy + 1.3), sr = Math.hypot(sdx, sdy), sth = Math.atan2(sdy, sdx);
    if (r >= rr && sr < R(sth) + 0.6) { img.set(x, y, r < rr + 1 ? GRN : GRN2); continue; }
    if (r < rr) {
      let c = WHITE;
      if (r > rr - 1.2) c = '#dfe4de';
      // separacion entre petalos
      const ph = ((N * th) / (2 * Math.PI)) % 1; const pa = (ph + 1) % 1;
      if (r > 9 && Math.abs(pa - 0.5) < 0.03) c = '#d4dad4';
      img.set(x, y, c);
    }
  }
  // centro verde con aro dorado
  for (let y = 0; y < h; y++) for (let x = 0; x < w; x++) {
    const r = Math.hypot(x - cx, y - cy);
    if (r < 7.4) img.set(x, y, GRN); else if (r < 8.6) img.set(x, y, r < 8 ? GOLD : '#8a6a1a');
  }
  // "Hierbas Serranas" en blanco (garabato legible a distancia)
  for (const [yy, x0, x1] of [[cy - 3, cx - 4, cx + 4], [cy - 1, cx - 5, cx + 5]]) for (let x = Math.round(x0); x <= x1; x++) if (rnd(x, yy, 33) > 0.25) img.set(x, yy, WHITE);
  for (const [yy, x0, x1] of [[cy + 2, cx - 4, cx + 4], [cy + 4, cx - 3, cx + 3]]) for (let x = Math.round(x0); x <= x1; x++) if (rnd(x, yy, 34) > 0.45) img.set(x, yy, '#cfe0c8');
  // sello sin gluten (rojo) abajo a la izquierda
  const gx = 6, gy = 62;
  for (let y = -3; y <= 3; y++) for (let x = -3; x <= 3; x++) { const r = Math.hypot(x, y); if (r < 3.2) img.set(gx + x, gy + y, r < 2 ? WHITE : '#c8322a'); }
  img.set(gx, gy, '#c8a040');
  // texto chico abajo y "1Kg"
  for (let x = 12; x < 34; x++) if (rnd(x, 66, 35) > 0.35) img.set(x, 66, GRN2);
  for (let x = 12; x < 30; x++) if (rnd(x, 67, 36) > 0.5) img.set(x, 67, GRN2);
  img.text(F35, '1KG', w - 13, 63, GRN);
  return img;
}

function side() {
  const w = P * D, h = H * D, img = new Img(w, h, yellowNoise);
  for (let y = 0; y < h; y++) { img.set(0, y, YELD); img.set(w - 1, y, YELD); }
  for (let x = 0; x < w; x++) { img.set(x, 0, YELD); img.set(x, h - 1, YELD); }
  // franja verde con texto (tabla nutricional / leyendas)
  img.rect(3, 8, w - 6, 3, GRN);
  for (let y = 14; y < 40; y += 2) for (let x = 3; x < w - 3; x++) if (rnd(x, y, 37) > 0.4) img.set(x, y, GRN2);
  img.rect(3, 44, w - 6, 9, WHITE); for (let x = 4; x < w - 4; x++) img.set(x, 44 + 1 + (x % 7 < 3 ? 2 : 4), '#202020');
  for (let x = 4; x < w - 4; x++) if (x % 2) img.set(x, 50, '#202020');
  return img;
}

function top() {
  const w = W * D, h = P * D, img = new Img(w, h, (x, y) => (y % 5 === 0 ? YELD : yellowNoise(x, y)));
  for (let x = 0; x < w; x++) { img.set(x, 0, YELD); img.set(x, h - 1, YELD); }
  return img;
}

function build() {
  const m = new ModelBuilder({ size: 128, density: D });
  const fr = front(), sd = side(), tp = top();
  m.region('front', fr.w, fr.h, fr.painter());
  m.region('side', sd.w, sd.h, sd.painter());
  m.region('top', tp.w, tp.h, tp.painter());
  m.region('fold', 45, 6, (x, y) => (y === 0 ? '#ffdc3a' : (x % 6 === 0 ? YELD : (y === 5 ? YELD : YEL2))));
  const x1 = 8 - W / 2, x2 = 8 + W / 2, z1 = 8 - P / 2, z2 = 8 + P / 2;
  m.cube([x1, 0, z1], [x2, H, z2], { north: 'front', south: 'front', east: 'side', west: 'side', up: 'top', down: 'top' }, null, 'paquete');
  // cierre doblado arriba
  m.cube([x1 + 0.2, H, 8 - 1], [x2 - 0.2, H + 0.8, 8 + 1], { all: 'fold' }, null, 'cierre');
  const display = {
    thirdperson_righthand: { rotation: [75, 45, 0], translation: [0, 1.5, 1.5], scale: [0.42, 0.42, 0.42] },
    thirdperson_lefthand: { rotation: [75, 45, 0], translation: [0, 1.5, 1.5], scale: [0.42, 0.42, 0.42] },
    firstperson_righthand: { rotation: [0, -20, 0], translation: [1, 3, 0], scale: [0.4, 0.4, 0.4] },
    firstperson_lefthand: { rotation: [0, -20, 0], translation: [1, 3, 0], scale: [0.4, 0.4, 0.4] },
    ground: { translation: [0, 2, 0], scale: [0.45, 0.45, 0.45] },
    gui: { rotation: [20, -30, 0], translation: [0, -0.5, 0], scale: [0.9, 0.9, 0.9] },
    head: { translation: [0, 14, 0] },
    fixed: { rotation: [0, 180, 0], scale: [0.9, 0.9, 0.9] }
  };
  const tex = 'ultimatefoods:item/fisura/yerba_verdeflor';
  const json = m.toJSON({ texture: tex, display });
  writeTexture('item', 'yerba_verdeflor', m.paint());
  return writeModel('item', 'yerba_verdeflor', json);
}
console.log(build());
