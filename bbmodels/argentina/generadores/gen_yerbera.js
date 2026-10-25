// Yerbera y azucarera de tela (foto del usuario): lona verde con trama, base de cuero marron claro,
// forma de trapecio (base angosta, arriba ancha) que se cierra en una cumbrera con cierre negro, boca en diagonal.
const { ModelBuilder, mix, shade, rnd, writeModel, writeTexture } = require('./lib');

const VARIANTES = {
  yerbera: { tela: ['#5b6b2e', '#647634', '#526228', '#6c7e3a'], parche: null },
  azucarera: { tela: ['#7f9a62', '#89a46c', '#748e58', '#94ae76'], parche: true }
};

function build(id) {
  const V = VARIANTES[id];
  const m = new ModelBuilder({ size: 64, density: 2 });
  const [T0, T1, T2, T3] = V.tela;
  // lona: trama de tejido (cuadricula fina) con algo de ruido
  m.region('tela', 28, 26, (x, y) => {
    let c = ((x + y) % 2) ? T0 : T1;
    if (x % 4 === 0 && y % 2 === 0) c = T2;
    if (rnd(x, y, 71) > 0.94) c = T3;
    if (rnd(x, y, 72) > 0.97) c = shade(T0, 0.85);
    return c;
  });
  m.region('telaSombra', 28, 6, (x, y) => shade(((x + y) % 2) ? T0 : T1, 0.82));
  m.region('cuero', 28, 6, (x, y, w, h) => {
    if (y === 0) return (x % 3 === 1) ? '#4a2e1a' : '#9a5c2c'; // costura
    let c = rnd(x, y, 73) > 0.85 ? '#c07a42' : '#b5703a';
    if (y === h - 1) c = '#8e5426';
    return c;
  });
  m.region('cueroBase', 16, 16, (x, y) => rnd(x, y, 74) > 0.8 ? '#8e5426' : '#9a5e2e');
  m.region('cierre', 30, 2, (x, y) => y === 0 ? ((x % 2) ? '#3a3a3a' : '#1a1a1a') : '#101010');
  m.region('cierreLado', 4, 2, () => '#151515');
  m.region('tirador', 4, 4, (x, y) => (x === 0 || y === 0) ? '#f0f0f0' : (x === 3 || y === 3 ? '#8a8a8a' : '#c8c8c8'));
  if (V.parche) m.region('parche', 8, 6, (x, y, w, h) => (x === 0 || y === 0 || x === w - 1 || y === h - 1) ? ((x + y) % 2 ? '#8a6a44' : '#e8dcc0') : (y === 2 && x > 1 && x < w - 2 ? '#c8b48a' : '#efe4c8'));

  const S = { sides: 'tela', up: 'tela', down: 'cueroBase' };
  // base de cuero (aprox. 20 % inferior)
  m.cube([5, 0, 5], [11, 2.6, 11], { sides: 'cuero', up: 'cueroBase', down: 'cueroBase' }, null, 'base_cuero');
  // cuerpo de lona: se ensancha en x y se afina en z al subir (trapecio que termina en cumbrera)
  const capas = [
    [2.6, 4.2, 4.4, 11.6, 5.0, 11.0],
    [4.2, 5.8, 3.8, 12.2, 5.2, 10.8],
    [5.8, 7.4, 3.2, 12.8, 5.5, 10.5],
    [7.4, 9.0, 2.8, 13.2, 5.9, 10.1],
    [9.0, 10.4, 2.6, 13.4, 6.4, 9.6]
  ];
  capas.forEach(([y1, y2, x1, x2, z1, z2], i) => m.cube([x1, y1, z1], [x2, y2, z2], { ...S, sides: { r: 'tela', oy: i * 3 } }, null, 'lona_' + i));
  // boca en diagonal: bloque inclinado 22.5 grados (lado este mas alto), se cierra arriba en la cumbrera
  const rot = { angle: 22.5, axis: 'z', origin: [8, 10.4, 8] };
  m.cube([2.5, 9.2, 6.6], [13.6, 11.6, 9.4], { ...S, sides: { r: 'tela', oy: 14 } }, rot, 'boca_inclinada');
  m.cube([2.8, 11.6, 7.2], [13.3, 12.6, 8.8], { ...S, sides: { r: 'tela', oy: 18 } }, rot, 'boca_pliegue');
  // cierre negro sobre la cumbrera + tirador plateado en el extremo alto
  m.cube([2.6, 12.6, 7.6], [13.5, 13.1, 8.4], { all: 'cierre', east: 'cierreLado', west: 'cierreLado' }, rot, 'cierre');
  m.cube([12.9, 12.4, 7.4], [14.1, 13.6, 7.7], { all: 'tirador' }, rot, 'tirador');
  if (V.parche) m.cube([6.2, 4.6, 4.9], [9.8, 7.0, 5.0], { north: 'parche', south: null, east: null, west: null, up: null, down: null }, null, 'parche_azucar');

  const display = {
    gui: { rotation: [25, 155, 0], translation: [0, -0.5, 0], scale: [0.8, 0.8, 0.8] },
    ground: { translation: [0, 3, 0], scale: [0.25, 0.25, 0.25] },
    fixed: { scale: [0.5, 0.5, 0.5] },
    thirdperson_righthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    thirdperson_lefthand: { rotation: [75, 45, 0], translation: [0, 2.5, 0], scale: [0.375, 0.375, 0.375] },
    firstperson_righthand: { rotation: [0, 45, 0], scale: [0.4, 0.4, 0.4] },
    firstperson_lefthand: { rotation: [0, 225, 0], scale: [0.4, 0.4, 0.4] },
    head: { translation: [0, 14, 0] }
  };
  const tex = `ultimatefoods:block/argentina/${id}`;
  const json = m.toJSON({ texture: tex, display });
  writeTexture('block', id, m.paint());
  return writeModel('block', id, json) + '\n' + writeModel('item', id, { parent: `ultimatefoods:block/argentina/${id}` });
}
console.log(build('yerbera'));
console.log(build('azucarera'));
