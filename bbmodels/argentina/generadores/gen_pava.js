// Pava argentina de aluminio: cuerpo panzon, tapa con perilla negra, pico largo inclinado, asa de alambre con mango negro.
const { ModelBuilder, hex, mix, shade, rnd, pick, writeModel, writeTexture, rotPoint } = require('./lib');

const DISPLAY = {
  thirdperson_righthand: { rotation: [75, 90, 0], translation: [0, -1, -3.5], scale: [0.45, 0.45, 0.45] },
  thirdperson_lefthand: { rotation: [75, 90, 0], translation: [0, -1, -3.5], scale: [0.45, 0.45, 0.45] },
  firstperson_righthand: { rotation: [0, 15, 0], translation: [0, 4, 0], scale: [0.4, 0.4, 0.4] },
  firstperson_lefthand: { rotation: [0, 15, 0], translation: [0, 4, 0], scale: [0.4, 0.4, 0.4] },
  ground: { translation: [0, 2, 0], scale: [0.45, 0.45, 0.45] },
  gui: { rotation: [25, -25, 0], translation: [0, -0.5, 0], scale: [0.85, 0.85, 0.85] },
  head: { translation: [0, 14, 0] },
  fixed: { rotation: [0, 180, 0], translation: [0, -0.5, 0], scale: [0.8, 0.8, 0.8] }
};

function build(id, estado) {
  const m = new ModelBuilder({ size: 64, density: 2 });
  const hot = estado === 'caliente';
  // aluminio: gradiente vertical + franja de brillo
  const aluCol = (x, y, w, h) => {
    let base = mix('#d2d4d6', '#8e9194', y / Math.max(1, h - 1));
    const band = Math.abs(x - Math.round(w * 0.3));
    if (band === 0) base = mix(base, '#ffffff', 0.7); else if (band === 1) base = mix(base, '#ffffff', 0.35);
    if (x === w - 1 || x === w - 2) base = shade(base, 0.85);
    if (rnd(x, y, 21) > 0.93) base = shade(base, 0.94);
    if (hot) { if (y >= 6) base = mix(base, '#7a4a30', y >= 8 ? 0.65 : 0.4); if (y === h - 1) base = mix(base, '#ff7a1a', 0.75); else if (y === h - 2) base = mix(base, '#ff9a3a', 0.35); }
    return base;
  };
  m.region('alu', 24, 10, aluCol);
  m.region('aluShoulder', 22, 4, (x, y, w, h) => { let c = mix('#e4e6e8', '#b4b7ba', y / (h - 1)); if (Math.abs(x - 7) <= 1) c = mix(c, '#ffffff', 0.5); return c; });
  m.region('aluTop', 24, 24, (x, y, w, h) => {
    const d = Math.hypot(x - w * 0.4, y - h * 0.4) / (w * 0.7);
    let c = mix('#eceef0', '#a6a9ac', Math.min(1, d));
    if (rnd(x, y, 22) > 0.94) c = shade(c, 0.95);
    return c;
  });
  m.region('aluDark', 20, 20, (x, y) => hot ? pick(['#3a2a20', '#4a3426', '#2e221a'], x, y, 23) : pick(['#6e7174', '#76797c', '#66696c'], x, y, 23));
  m.region('rim', 14, 2, (x, y) => y === 0 ? '#f4f6f8' : '#7c7f82');
  m.region('black', 18, 4, (x, y, w, h) => y === 0 ? '#3a3a3c' : (y === h - 1 ? '#0e0e10' : pick(['#1c1c1e', '#202022', '#18181a'], x, y, 24)));
  m.region('wire', 2, 14, (x, y) => x === 0 ? '#dcdcdc' : '#8a8a8a');
  m.region('spout', 4, 18, (x, y, w, h) => { let c = mix('#dadcde', '#9a9da0', x / (w - 1)); if (x === 1 && y % 6 === 2) c = '#ffffff'; if (hot && y < 4) c = mix(c, '#7a5236', 0.3); return c; });
  m.region('spoutHole', 4, 4, (x, y, w, h) => (x === 0 || y === 0 || x === w - 1 || y === h - 1) ? '#b8bbbe' : (estado === 'agua' ? '#3a74c8' : (hot ? '#4a5a6a' : '#2a2c2e')));
  if (estado === 'agua') m.region('water', 2, 2, (x, y) => (x + y === 0) ? '#cfe8ff' : '#3f86e0');
  if (hot) m.region('steam', 6, 6, (x, y) => { const r = rnd(x, y, 25); if (r < 0.18) return null; return r > 0.7 ? [255, 255, 255, 220] : [222, 228, 234, 190]; });

  const cx = 10.5, cz = 8;
  const oct = (y1, y2, w, cut, faces, name) => {
    m.cube([cx - w / 2, y1, cz - w / 2 + cut], [cx + w / 2, y2, cz + w / 2 - cut], faces, null, name + '_x');
    m.cube([cx - w / 2 + cut, y1, cz - w / 2], [cx + w / 2 - cut, y2, cz + w / 2], faces, null, name + '_z');
  };
  // cuerpo
  m.cube([cx - 4, 0, cz - 4], [cx + 4, 0.5, cz + 4], { all: 'aluDark' }, null, 'base');
  oct(0.5, 1.5, 10, 1, { sides: { r: 'alu', oy: 7 }, up: 'aluTop', down: 'aluDark' }, 'panza_baja');
  oct(1.5, 2.6, 11.2, 1.2, { sides: { r: 'alu', oy: 1 }, up: 'aluTop', down: 'aluDark' }, 'panza_inf');
  oct(2.6, 5.2, 12, 1.4, { sides: { r: 'alu', oy: 3 }, up: 'aluTop', down: 'aluDark' }, 'panza');
  oct(5.2, 6, 11.2, 1.2, { sides: 'alu', up: 'aluTop', down: 'aluDark' }, 'panza_sup');
  oct(6, 7.4, 10, 1, { sides: 'aluShoulder', up: 'aluTop', down: 'aluDark' }, 'hombro');
  oct(7.4, 8.2, 7, 1, { sides: 'rim', up: 'aluTop', down: 'aluDark' }, 'borde_tapa');
  oct(8.2, 9.1, 5, 0.75, { sides: 'aluShoulder', up: 'aluTop', down: 'aluDark' }, 'tapa');
  // perilla negra
  m.cube([cx - 0.75, 9.1, cz - 0.75], [cx + 0.75, 10.2, cz + 0.75], { all: 'black' }, null, 'perilla_cuello');
  m.cube([cx - 1.25, 10.2, cz - 1.25], [cx + 1.25, 11, cz + 1.25], { all: 'black' }, null, 'perilla');
  // asa: dos alambres y mango negro
  for (const s of [-1, 1]) m.cube([cx + s * 3.6 - 0.3, 6.6, cz - 0.3], [cx + s * 3.6 + 0.3, 13.2, cz + 0.3], { all: 'wire' }, null, 'asa_alambre');
  m.cube([cx - 4.6, 13, cz - 0.9], [cx + 4.6, 14.6, cz + 0.9], { all: 'black' }, null, 'mango');
  for (const s of [-1, 1]) m.cube([cx + s * 4.1 - 0.5, 12.3, cz - 0.8], [cx + s * 4.1 + 0.5, 13, cz + 0.8], { all: 'black' }, null, 'mango_punta');
  // pico largo inclinado hacia -x, nace abajo del cuerpo
  const o = [cx - 5.3, 2, cz];
  m.cube([o[0] - 1.1, o[1] - 0.6, cz - 1.1], [o[0] + 1.1, o[1] + 3, cz + 1.1], { all: 'spout', down: 'aluDark' }, { angle: 45, axis: 'z', origin: o }, 'pico_base');
  m.cube([o[0] - 0.7, o[1] + 3, cz - 0.7], [o[0] + 0.7, o[1] + 10, cz + 0.7], { all: 'spout', up: 'spoutHole' }, { angle: 45, axis: 'z', origin: o }, 'pico');
  const tip = rotPoint([o[0], o[1] + 10, cz], 'z', 45, o);
  m.cube([tip[0] - 0.9, tip[1] - 0.6, cz - 0.9], [tip[0] + 0.9, tip[1] + 0.4, cz + 0.9], { all: 'spout', up: 'spoutHole' }, { angle: 22.5, axis: 'z', origin: tip }, 'pico_boca');
  if (estado === 'agua') { m.cube([tip[0] - 1.9, tip[1] - 1.9, cz - 0.6], [tip[0] - 0.7, tip[1] - 0.7, cz + 0.6], { all: 'water' }, null, 'gota'); m.cube([tip[0] - 1.6, tip[1] - 3.6, cz - 0.4], [tip[0] - 0.8, tip[1] - 2.8, cz + 0.4], { all: 'water' }, null, 'gota2'); }
  if (hot) {
    m.cube([tip[0] - 1, tip[1] + 1, cz - 1], [tip[0] + 1, tip[1] + 2.5, cz + 1], { all: 'steam' }, null, 'vapor1');
    m.cube([tip[0] - 0.4, tip[1] + 3, cz - 1.2], [tip[0] + 2, tip[1] + 5, cz + 1.2], { all: 'steam' }, null, 'vapor2');
    m.cube([tip[0] - 1.6, tip[1] + 5.6, cz - 0.9], [tip[0] + 0.2, tip[1] + 7.2, cz + 0.9], { all: 'steam' }, null, 'vapor3');
  }
  const tex = `ultimatefoods:item/argentina/${id}`;
  writeTexture('item', id, m.paint());
  return writeModel('item', id, m.toJSON({ texture: tex, display: DISPLAY }));
}

console.log([build('pava', null), build('pava_con_agua', 'agua'), build('pava_caliente', 'caliente')].join('\n'));
