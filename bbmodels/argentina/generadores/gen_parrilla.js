// Parrilla argentina: patas de hierro, rejilla de barras con canaleta adelante, bandeja con brasas y leña abajo.
const { ModelBuilder, mix, shade, rnd, pick, writeModel, writeTexture } = require('./lib');

function build() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  const iron = (x, y) => { const r = rnd(x, y, 51); return r > 0.975 ? '#4a3428' : (r > 0.8 ? '#363636' : (r < 0.1 ? '#232323' : '#2c2c2c')); };
  m.region('iron', 32, 22, iron);
  m.region('ironTop', 32, 4, (x, y) => y === 0 ? (rnd(x, y, 52) > 0.85 ? '#6a6a6a' : '#555555') : iron(x, y));
  m.region('bar', 2, 26, (x, y) => x === 0 ? (rnd(x, y, 53) > 0.8 ? '#6e5a4a' : '#5c5c5c') : '#2a2a2a');
  m.region('canaleta', 32, 3, (x, y) => y === 0 ? '#5a5a5a' : (y === 1 ? '#1a1a1a' : '#303030'));
  // brasas: ceniza blanca, carbon negro, puntos naranjas/rojos
  m.region('brasas', 28, 28, (x, y) => {
    const r = rnd(x, y, 54), g = rnd(x >> 1, y >> 1, 55);
    if (g > 0.82) return r > 0.5 ? '#ff8c1a' : (r > 0.2 ? '#e0461a' : '#ffd040');
    if (g > 0.55) return r > 0.6 ? '#1c1a18' : '#2a2622';
    return r > 0.7 ? '#e8e4dc' : (r > 0.35 ? '#c8c2b8' : '#a8a298');
  });
  m.region('carbon', 6, 6, (x, y) => { const r = rnd(x, y, 56); return r > 0.85 ? '#ff7a1a' : (r > 0.7 ? '#c83a12' : (r > 0.25 ? '#1e1a16' : '#3a3028')); });
  m.region('lenia', 12, 3, (x, y) => (y === 1 ? '#5a3a20' : (rnd(x, y, 57) > 0.6 ? '#3a2614' : '#6a4a2a')));
  m.region('leniaPunta', 3, 3, (x, y) => (x === 1 && y === 1) ? '#ff9a2a' : '#c8a070');

  const IR = { all: 'iron', up: 'ironTop' };
  // patas
  for (const [x, z] of [[0.5, 0.5], [14, 0.5], [0.5, 14], [14, 14]]) m.cube([x, 0, z], [x + 1.5, 10, z + 1.5], IR, null, 'pata');
  // marco y canaleta delantera (norte)
  m.cube([0, 9.4, 0], [16, 11, 2], { all: 'iron', up: 'canaleta', north: 'iron' }, null, 'canaleta');
  m.cube([0, 10, 14], [16, 11, 16], IR, null, 'marco_atras');
  m.cube([0, 10, 2], [2, 11, 14], IR, null, 'marco_izq');
  m.cube([14, 10, 2], [16, 11, 14], IR, null, 'marco_der');
  // barras de la rejilla (de adelante hacia atras)
  for (let i = 0; i < 9; i++) { const x = 2.6 + i * 1.3; m.cube([x, 10.2, 2], [x + 0.5, 10.9, 14], { all: 'bar', up: 'bar' }, null, 'barra'); }
  // travesaño de refuerzo debajo
  m.cube([2, 9.6, 7.6], [14, 10.2, 8.4], { all: 'iron' }, null, 'travesanio');
  // bandeja de brasas
  m.cube([1, 1.5, 1], [15, 2, 15], { all: 'iron' }, null, 'bandeja');
  m.cube([1, 2, 1], [15, 3, 1.5], { all: 'iron' }, null, 'bandeja_borde_n');
  m.cube([1, 2, 14.5], [15, 3, 15], { all: 'iron' }, null, 'bandeja_borde_s');
  m.cube([1, 2, 1.5], [1.5, 3, 14.5], { all: 'iron' }, null, 'bandeja_borde_o');
  m.cube([14.5, 2, 1.5], [15, 3, 14.5], { all: 'iron' }, null, 'bandeja_borde_e');
  const GLOW = { block_light: 15, sky_light: 15 };
  m.cube([1.5, 2, 1.5], [14.5, 2.6, 14.5], { all: 'brasas', up: { r: 'brasas', data: GLOW } }, null, 'brasas');
  // carbones sueltos
  for (const [x, z, s] of [[3, 4, 2], [6.5, 9, 1.8], [10, 3.5, 1.6], [11.5, 10.5, 2], [4.5, 11.5, 1.5], [8.5, 6, 1.4]]) m.cube([x, 2.6, z], [x + s, 2.6 + s * 0.6, z + s], { all: 'carbon', up: { r: 'carbon', data: GLOW } }, null, 'carbon');
  // leña al fondo
  m.cube([2.5, 2.6, 12], [12.5, 4, 13.4], { all: 'lenia', east: 'leniaPunta', west: 'leniaPunta' }, null, 'lenia');
  m.cube([5, 2.6, 10.2], [13.5, 3.8, 11.4], { all: 'lenia', east: 'leniaPunta', west: 'leniaPunta' }, { angle: 22.5, axis: 'y', origin: [9, 3, 10.8] }, 'lenia2');

  const display = {
    gui: { rotation: [30, 225, 0], scale: [0.625, 0.625, 0.625] },
    ground: { translation: [0, 3, 0], scale: [0.25, 0.25, 0.25] },
    fixed: { scale: [0.5, 0.5, 0.5] },
    thirdperson_righthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    thirdperson_lefthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    firstperson_righthand: { rotation: [0, 45, 0], scale: [0.4, 0.4, 0.4] },
    firstperson_lefthand: { rotation: [0, 225, 0], scale: [0.4, 0.4, 0.4] },
    head: { translation: [0, 14, 0] }
  };
  const tex = 'ultimatefoods:block/argentina/parrilla';
  const json = m.toJSON({ texture: tex, display });
  writeTexture('block', 'parrilla', m.paint());
  return writeModel('block', 'parrilla', json) + '\n' + writeModel('item', 'parrilla', { parent: 'ultimatefoods:block/argentina/parrilla' });
}
console.log(build());
