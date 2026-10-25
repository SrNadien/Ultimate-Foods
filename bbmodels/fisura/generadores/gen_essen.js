// Cacerola Essen 28 "Aqua": olla baja y ancha aqua mate, interior negro, borde y asas gris claro, tapa aqua con perilla.
const { ModelBuilder, mix, shade, rnd, pick, writeModel, writeTexture } = require('./lib');

const AQUA = '#6cc8c0', AQUA_L = '#86d6ce', AQUA_D = '#4fa9a2', GRIS = '#d9d9d9', GRIS_D = '#a9adad', NEGRO = '#1b1b1b';

function build(id, abierta) {
  const m = new ModelBuilder({ size: 128, density: 2 });
  m.region('side', 28, 12, (x, y, w, h) => {
    let c = mix(AQUA_L, AQUA, Math.min(1, Math.abs(x - w * 0.35) / (w * 0.5)));
    if (y >= h - 2) c = mix(c, AQUA_D, 0.6);
    if (x >= w - 2) c = shade(c, 0.92);
    if (rnd(x, y, 61) > 0.95) c = shade(c, 0.97);
    return c;
  });
  m.region('bottom', 28, 28, (x, y) => rnd(x, y, 62) > 0.9 ? '#3e8c86' : '#4a9a94');
  m.region('rim', 28, 2, (x, y) => y === 0 ? '#f2f2f2' : GRIS);
  m.region('handle', 6, 6, (x, y, w, h) => y === 0 ? '#f0f0f0' : (y === h - 1 ? GRIS_D : GRIS));
  m.region('black', 28, 28, (x, y) => rnd(x, y, 63) > 0.9 ? '#262626' : NEGRO);
  m.region('lidTop', 30, 30, (x, y, w, h) => {
    return rnd(x, y, 66) > 0.92 ? '#7ccfc7' : '#84d4cc';
  });
  m.region('lidSide', 30, 2, (x, y) => y === 0 ? '#e8ecec' : '#b8bcbc');
  m.region('knob', 8, 4, (x, y, w, h) => y === 0 ? AQUA_L : (y === h - 1 ? AQUA_D : AQUA));
  m.region('knobTop', 6, 6, (x, y, w, h) => { const d = Math.hypot(x - 2.5, y - 2.5); return d < 1.8 ? (d < 0.9 ? '#ffffff' : '#d6dcdc') : AQUA_L; });
  if (abierta) {
    // guiso: caldo marron con zanahoria, papa, hongos, arvejas y pollo
    m.region('guiso', 24, 24, (x, y) => {
      const r = rnd(x, y, 64), g = rnd(x >> 1, y >> 1, 65);
      if (g > 0.86) return r > 0.4 ? '#f07a1e' : '#d8641a';           // zanahoria
      if (g > 0.76) return r > 0.5 ? '#f2dc8a' : '#e6c86a';           // papa
      if (g > 0.68) return r > 0.5 ? '#c8ae8a' : '#8a6a4a';           // hongos
      if (g > 0.62) return r > 0.3 ? '#7ab83a' : '#5a9a2a';           // arvejas
      if (g > 0.54) return r > 0.5 ? '#f0e2c8' : '#dcc8a8';           // pollo
      return r > 0.7 ? '#8a4a1e' : (r > 0.3 ? '#9c5624' : '#7a3e18'); // salsa
    });
    m.region('sideRim', 28, 4, (x, y) => y === 0 ? '#f2f2f2' : (y === 1 ? GRIS : (Math.abs(x - 10) < 3 ? AQUA_L : AQUA)));
    m.region('trozo', 4, 4, (x, y) => (x + y) % 3 ? '#f07a1e' : '#d8641a');
    m.region('pollo', 4, 4, (x, y) => (x + y) % 3 ? '#f0e2c8' : '#c8a878');
  }

  const cx = 8, cz = 8;
  const oct = (y1, y2, w, cut, faces, name) => {
    m.cube([cx - w / 2, y1, cz - w / 2 + cut], [cx + w / 2, y2, cz + w / 2 - cut], faces, null, name + '_x');
    m.cube([cx - w / 2 + cut, y1, cz - w / 2], [cx + w / 2 - cut, y2, cz + w / 2], faces, null, name + '_z');
  };
  // cuerpo
  oct(0, 1, 12.6, 1.7, { sides: { r: 'side', oy: 10 }, up: 'black', down: 'bottom' }, 'base');
  if (!abierta) oct(1, 6.4, 14, 1.8, { sides: { r: 'side', oy: 0 }, up: 'black', down: 'bottom' }, 'cuerpo');
  else oct(1, 5.2, 14, 1.8, { sides: { r: 'side', oy: 3 }, up: 'guiso', down: 'bottom' }, 'cuerpo');
  // asas planas a los costados (este/oeste)
  m.cube([-0.6, 5.7, cz - 2.2], [1.2, 6.5, cz + 2.2], { all: 'handle' }, null, 'asa_o');
  m.cube([14.8, 5.7, cz - 2.2], [16.6, 6.5, cz + 2.2], { all: 'handle' }, null, 'asa_e');
  if (!abierta) {
    // borde de la olla
    oct(6.4, 6.9, 14.2, 1.8, { sides: 'rim', up: 'rim', down: 'black' }, 'borde');
    // tapa
    oct(6.9, 7.4, 14.6, 1.9, { sides: 'lidSide', up: 'lidTop', down: 'black' }, 'tapa_ala');
    oct(7.4, 8.3, 12.4, 1.7, { sides: { r: 'side', oy: 2 }, up: 'lidTop', down: 'black' }, 'tapa_cupula');
    oct(8.3, 8.8, 9, 1.3, { sides: { r: 'side', oy: 2 }, up: 'lidTop', down: 'black' }, 'tapa_cima');
    m.cube([cx - 0.9, 8.8, cz - 0.9], [cx + 0.9, 9.5, cz + 0.9], { all: 'knob' }, null, 'perilla_cuello');
    oct(9.5, 10.7, 3.6, 0.7, { sides: 'knob', up: 'knobTop', down: 'knob' }, 'perilla');
  } else {
    // marco superior alrededor de la boca cuadrada [3.6..12.4]: lados aqua con borde blanco, paredes internas negras
    const a = 2.8, b = 13.2, y1 = 5.2, y2 = 6.9;
    const F = (from, to, inner, name) => m.cube(from, to, { sides: 'sideRim', [inner]: 'black', up: 'black', down: null }, null, name);
    F([1, y1, a], [a, y2, b], 'east', 'marco_o');
    F([b, y1, a], [15, y2, b], 'west', 'marco_e');
    F([a, y1, 1], [b, y2, a], 'south', 'marco_n');
    F([a, y1, b], [b, y2, 15], 'north', 'marco_s');
    for (const [x, z, s, t] of [[4.5, 5, 1.6, 'trozo'], [9.5, 4.2, 1.4, 'trozo'], [6.5, 9.5, 2, 'pollo'], [10.5, 9.8, 1.5, 'trozo'], [4.6, 8.4, 1.4, 'pollo']])
      m.cube([x, 5.2, z], [x + s, 5.2 + s * 0.6, z + s], { all: t }, { angle: 22.5, axis: 'y', origin: [x + s / 2, 5.2, z + s / 2] }, 'presa');
  }

  const display = {
    gui: { rotation: [30, 225, 0], translation: [0, 1.5, 0], scale: [0.7, 0.7, 0.7] },
    ground: { translation: [0, 3, 0], scale: [0.25, 0.25, 0.25] },
    fixed: { translation: [0, 2, 0], scale: [0.5, 0.5, 0.5] },
    thirdperson_righthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    thirdperson_lefthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    firstperson_righthand: { rotation: [0, 45, 0], translation: [0, 2, 0], scale: [0.4, 0.4, 0.4] },
    firstperson_lefthand: { rotation: [0, 225, 0], translation: [0, 2, 0], scale: [0.4, 0.4, 0.4] },
    head: { translation: [0, 14, 0] }
  };
  const tex = `ultimatefoods:block/fisura/${id}`;
  const json = m.toJSON({ texture: tex, display });
  writeTexture('block', id, m.paint());
  return writeModel('block', id, json);
}
console.log(build('cacerola_essen', false));
console.log(writeModel('item', 'cacerola_essen', { parent: 'ultimatefoods:block/fisura/cacerola_essen' }));
console.log(build('cacerola_essen_abierta', true));
