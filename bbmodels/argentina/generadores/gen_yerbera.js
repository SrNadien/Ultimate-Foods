// Yerbera: lata enlozada crema con tapa verde, leyenda "YERBA" y ramita de yerba mate pintada.
const { ModelBuilder, Img, mix, shade, rnd, pick, writeModel, writeTexture } = require('./lib');
const { F57, textWidth } = require('./font');

const CREAM = '#f1e8d0', CREAM2 = '#e6dcc0', CREAMD = '#cfc3a2', GRN = '#1f5a32', GRN2 = '#2f7a44', GRNL = '#4f9a50', RED = '#b8322a', EDGE = '#20262a';
const D = 4;

function enamel(x, y) { const r = rnd(x, y, 41); return r > 0.93 ? CREAM2 : (r < 0.02 ? '#d9cfb3' : CREAM); }

function panel(wpx, hpx, conTexto) {
  const w = Math.round(wpx * D), h = Math.round(hpx * D);
  const img = new Img(w, h, enamel);
  // sombreado de cilindro: bordes laterales mas oscuros
  for (let y = 0; y < h; y++) { img.set(0, y, CREAMD); img.set(w - 1, y, CREAMD); img.set(1, y, CREAM2); img.set(w - 2, y, CREAM2); }
  // borde inferior oscuro (lata) y filete verde arriba
  img.rect(0, h - 2, w, 2, EDGE); img.rect(0, h - 4, w, 1, GRN);
  img.rect(0, 2, w, 2, GRN); img.rect(0, 5, w, 1, RED);
  if (conTexto) {
    const tw = textWidth(F57, 'YERBA'), tx = Math.floor((w - tw) / 2), ty = 11;
    img.text(F57, 'YERBA', tx + 1, ty + 1, CREAMD); // sombra
    img.text(F57, 'YERBA', tx, ty, GRN);
  }
  // ramita de yerba mate
  const cx = Math.floor(w / 2), y0 = conTexto ? 22 : 12, y1 = h - 7;
  for (let y = y0; y <= y1; y++) img.set(cx + Math.round(Math.sin((y - y0) / 5) * 1), y, '#6a4a2a');
  const hojas = [];
  for (let y = y0 + 2, i = 0; y < y1 - 2; y += 5, i++) hojas.push([cx + (i % 2 ? 1 : -1), y, i % 2 ? 1 : -1]);
  for (const [hx, hy, s] of hojas) {
    for (let k = 0; k < 6; k++) { const lx = hx + s * k, ly = hy - Math.round(k / 2); const ww = k === 0 || k === 5 ? 0 : 1; for (let j = -ww; j <= ww; j++) img.set(lx, ly + j, j === 0 && k > 0 && k < 5 ? GRN2 : GRNL); }
    img.set(hx + s * 2, hy - 1, GRN);
  }
  return img;
}

function build() {
  const m = new ModelBuilder({ size: 128, density: D });
  const W = 10.5, cut = 1.25, Hb = 12;
  const front = panel(W - 2 * cut, Hb, true), back = panel(W - 2 * cut, Hb, false), diag = panel(W, Hb, false);
  m.region('front', front.w, front.h, front.painter());
  m.region('back', back.w, back.h, back.painter());
  m.region('diag', diag.w, diag.h, diag.painter());
  m.region('bottom', 40, 40, () => '#2a3034');
  m.region('inside', 8, 8, () => CREAMD);
  m.region('lidSide', 44, 6, (x, y) => y === 0 ? '#3c8a54' : (y >= 4 ? EDGE : (x % 11 === 3 && y === 1 ? '#6ab878' : GRN)));
  m.region('lidTop', 44, 44, (x, y, w, h) => { const d = Math.hypot(x - w * 0.42, y - h * 0.42) / (w * 0.6); let c = mix('#3f9a5a', GRN, Math.min(1, d)); if (rnd(x, y, 42) > 0.95) c = shade(c, 0.92); return c; });
  m.region('knob', 10, 6, (x, y) => y === 0 ? '#8a5a32' : (x === 9 ? '#4a2e18' : (y > 3 ? '#5a3a1e' : '#6e4626')));

  const cx = 8, cz = 8;
  // cuerpo octogonal: el cubo "z" lleva el frente con YERBA (norte/sur)
  m.cube([cx - W / 2 + cut, 0, cz - W / 2], [cx + W / 2 - cut, Hb, cz + W / 2], { north: 'front', south: 'back', east: null, west: null, up: 'inside', down: 'bottom' }, null, 'cuerpo_z');
  m.cube([cx - W / 2, 0, cz - W / 2 + cut], [cx + W / 2, Hb, cz + W / 2 - cut], { north: { r: 'diag' }, south: { r: 'diag' }, east: 'back', west: 'back', up: 'inside', down: 'bottom' }, null, 'cuerpo_x');
  // tapa verde con borde oscuro
  const L = 11.3, lc = 1.5;
  m.cube([cx - L / 2 + lc, Hb, cz - L / 2], [cx + L / 2 - lc, Hb + 1.4, cz + L / 2], { sides: 'lidSide', up: 'lidTop', down: 'bottom' }, null, 'tapa_z');
  m.cube([cx - L / 2, Hb, cz - L / 2 + lc], [cx + L / 2, Hb + 1.4, cz + L / 2 - lc], { sides: 'lidSide', up: 'lidTop', down: 'bottom' }, null, 'tapa_x');
  m.cube([cx - 3.5, Hb + 1.4, cz - 3.5], [cx + 3.5, Hb + 2, cz + 3.5], { sides: 'lidSide', up: 'lidTop', down: 'bottom' }, null, 'tapa_cupula');
  m.cube([cx - 1.2, Hb + 2, cz - 1.2], [cx + 1.2, Hb + 3.2, cz + 1.2], { all: 'knob' }, null, 'perilla');

  const display = {
    gui: { rotation: [30, 225, 0], translation: [0, -1, 0], scale: [0.75, 0.75, 0.75] },
    ground: { translation: [0, 3, 0], scale: [0.25, 0.25, 0.25] },
    fixed: { scale: [0.5, 0.5, 0.5] },
    thirdperson_righthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    thirdperson_lefthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    firstperson_righthand: { rotation: [0, 45, 0], scale: [0.4, 0.4, 0.4] },
    firstperson_lefthand: { rotation: [0, 225, 0], scale: [0.4, 0.4, 0.4] },
    head: { translation: [0, 14, 0] }
  };
  const tex = 'ultimatefoods:block/argentina/yerbera';
  const json = m.toJSON({ texture: tex, display });
  writeTexture('block', 'yerbera', m.paint());
  const f1 = writeModel('block', 'yerbera', json);
  const f2 = writeModel('item', 'yerbera', { parent: 'ultimatefoods:block/argentina/yerbera' });
  return f1 + '\n' + f2;
}
console.log(build());
