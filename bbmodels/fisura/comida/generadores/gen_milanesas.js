// Grupo milanesas: milanesa_de_carne y milanesa_de_pollo (en plato con limon) y sanguche_de_milanesa (pan frances en plato).
const { ModelBuilder, mix, shade, rnd, blob, ramp, save, slab, rectsMask, plato, regionesPlato } = require('./lib');

const LIMON = { cascara: ['#f4d020', '#ecc410', '#fadc40'], pulpa: '#f8eca0', blanco: '#fff8dc' };
function regionesLimon(m) {
  m.region('limon_cascara', 8, 3, (x, y) => y === 0 ? '#fae878' : ramp(LIMON.cascara, rnd(x, y, 91)));
  m.region('limon_pulpa', 8, 4, (x, y, w, h) => (x === 0 || x === w - 1) ? LIMON.blanco : ((x + y) % 3 === 0 ? '#f0dc78' : LIMON.pulpa));
}
function limon(m, x, z, ang = 45) {
  // gajo de limon acostado: cascara afuera (norte/sur), pulpa en el corte de arriba y los extremos
  m.cube([x, 1, z], [x + 3.5, 2.5, z + 1.75], { up: 'limon_pulpa', down: 'limon_cascara', north: 'limon_cascara', south: 'limon_cascara', east: 'limon_pulpa', west: 'limon_pulpa' }, { angle: ang, axis: 'y', origin: [x + 1.75, 1, z + 0.875] }, 'limon');
}
// pintor de rebozado (pan rallado frito)
function rebozado(pal, seed, mask, extras) {
  return (x, y, w, h) => {
    const e = mask ? mask(x, y, w, h) : 3; if (e < 0) return null;
    let c = ramp(pal, blob(x, y, 2.5, seed) * 0.75 + rnd(x, y, seed) * 0.3);
    const r = rnd(x, y, seed + 1);
    if (r > 0.9) c = mix(c, '#f6c878', 0.5);       // migas claras
    else if (r < 0.07) c = shade(c, 0.78);          // migas tostadas
    if (e === 0) c = shade(c, 0.82);                // borde mas frito
    if (extras) c = extras(x, y, c) || c;
    return c;
  };
}
const CARNE = ['#b8642a', '#c87430', '#d4843a', '#c06a2c', '#dc9244'];
const POLLO = ['#d29444', '#dca452', '#e4b260', '#cc8a3e', '#e8bc6c'];

function milanesa(id, pal, rects, limonPos, perejil) {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14, filete: '#9aa2ac' });
  regionesLimon(m);
  const fr = [2, 4, 14, 12];
  const mask = rectsMask(rects, fr);
  m.region('mila_top', 24, 16, rebozado(pal, id.length * 7, mask, perejil ? (x, y, c) => rnd(x, y, 97) > 0.95 ? '#4a7a2a' : null : null));
  m.region('mila_lado', 30, 2, (x, y) => y === 0 ? ramp(pal, rnd(x, y, 95)) : shade(ramp(pal, rnd(x, y, 96)), 0.75));
  plato(m, { w: 14 });
  slab(m, rects, 1, 1.75, { sides: 'mila_lado', up: 'mila_top', down: null }, 'milanesa', fr);
  limon(m, ...limonPos);
  return save(id, m);
}

function sanguche() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14, filete: '#7aa88a', borde: '#cfe0d0' });
  const PAN = ['#dcb074', '#e6c088', '#eccb96', '#f2d8a8'];
  m.region('pan_top', 12, 12, (x, y, w, h) => {
    const cx = Math.abs(x - w / 2 + 0.5) / (w / 2), cy = Math.abs(y - h / 2 + 0.5) / (h / 2);
    let c = ramp(PAN, 1 - Math.max(cx, cy) * 0.6 + blob(x, y, 4, 101) * 0.3 - 0.15);
    if (y === 5 && x > 2 && x < 9) c = '#f8ecd0';           // corte del pan (greña)
    if (y === 6 && x > 2 && x < 9) c = '#d8b67a';
    return c;
  });
  m.region('pan_lado', 24, 5, (x, y, w, h) => y === h - 1 ? '#c09a5e' : ramp(PAN, 0.55 - y * 0.1 + blob(x, y, 3, 103) * 0.3));
  m.region('pan_base', 24, 3, (x, y) => y === 0 ? '#f6ead0' : ramp(PAN, rnd(x, y, 104)));
  m.region('pan_punta', 12, 5, (x, y) => ramp(PAN, 0.3 + blob(x, y, 3, 105) * 0.4));
  m.region('pan_abajo', 24, 12, (x, y) => ramp(['#d4b07a', '#dcbc86'], rnd(x, y, 106)));
  const mr = [[1.75, 4.75, 14.25, 11.25], [3, 4.25, 12, 4.75], [4, 11.25, 13, 11.75]];
  const fr = [1.75, 4.25, 14.25, 11.75];
  m.region('mila_top', 26, 16, rebozado(CARNE, 107, rectsMask(mr, fr)));
  m.region('mila_lado', 30, 2, (x, y) => y === 0 ? ramp(CARNE, rnd(x, y, 108)) : shade(ramp(CARNE, rnd(x, y, 109)), 0.75));
  m.region('lechuga', 26, 2, (x, y) => rnd(x, y, 110) > 0.4 ? (y === 0 ? '#9ad05a' : '#6aa83a') : null);
  m.region('lechuga_top', 26, 14, (x, y) => ramp(['#7ab840', '#9ad05a', '#5a9a30'], rnd(x, y, 111)));
  m.region('tomate', 8, 2, (x, y) => y === 0 ? '#e8503a' : '#c8302a');

  plato(m, { w: 14 });
  m.cube([2, 1, 5], [14, 2.5, 11], { up: 'pan_abajo', down: 'pan_abajo', sides: 'pan_base', east: 'pan_punta', west: 'pan_punta' }, null, 'pan_abajo');
  slab(m, mr, 2.5, 3, { sides: 'mila_lado', up: 'mila_top', down: 'mila_top' }, 'milanesa', fr);
  m.cube([2.5, 3, 4.5], [13.5, 3.25, 11.5], { up: 'lechuga_top', down: null, sides: 'lechuga' }, null, 'lechuga');
  m.cube([9, 3.25, 4.6], [12, 3.5, 11.4], { all: 'tomate' }, null, 'tomate');
  // dos bollos del pan frances, con el lomo redondeado
  for (const [a, b, n] of [[2, 8, 'pan_bollo_o'], [8, 14, 'pan_bollo_e']]) {
    m.cube([a, 3.5, 5], [b, 5.5, 11], { up: 'pan_top', down: 'pan_abajo', sides: 'pan_lado', east: 'pan_punta', west: 'pan_punta' }, null, n);
    m.cube([a + 0.5, 5.5, 5.75], [b - 0.5, 6.25, 10.25], { up: 'pan_top', down: null, sides: { r: 'pan_lado', oy: 0 }, east: 'pan_punta', west: 'pan_punta' }, null, n + '_lomo');
  }
  return save('sanguche_de_milanesa', m);
}

milanesa('milanesa_de_carne', CARNE,
  [[3, 4.5, 11, 11.5], [2.5, 5.5, 3, 10.5], [11, 5, 12, 10], [4, 4, 9, 4.5], [4.5, 11.5, 10, 12]], [11, 10.25, 45], false);
milanesa('milanesa_de_pollo', POLLO,
  [[3, 5, 13, 11], [2.25, 6, 3, 10], [13, 5.5, 13.75, 10.5], [4, 4.5, 12, 5], [3.5, 11, 12.5, 11.5]], [4, 2.25, -22.5], true);
sanguche();
