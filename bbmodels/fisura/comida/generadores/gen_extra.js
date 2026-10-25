// Pedidos extra: la_copa (copa dorada generica en honor a Messi, con placa albiceleste y el 10) y bombilla (item 3D,
// bombilla cuchara de acero inoxidable).
const { ModelBuilder, Img, mix, shade, rnd, ramp, save, disc, discMask, stringify, ASSETS, autoDisplay, bounds } = require('./lib');
const { F35 } = require('./font');
const fs = require('fs'), path = require('path');

// ================= LA COPA =================
function laCopa() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  const ORO = ['#8a5e10', '#b8861a', '#d8a420', '#f2c43a', '#ffe680'];
  // oro: sombreado horizontal (brillo vertical a un tercio de la cara)
  const oro = (x, y, w, h) => { const k = x / Math.max(1, w - 1); const t = 1 - Math.abs(k - 0.32) * 1.6; let c = ramp(ORO, 0.25 + t * 0.7); if (rnd(x, y, 261) > 0.94) c = '#fff4c0'; return c; };
  m.region('oro_lado', 16, 8, oro);
  m.region('oro_top', 18, 18, (x, y) => (x + y) % 7 === 0 ? '#ffe680' : '#e8b42c');
  const mb = discMask(8, 8);
  m.region('copa_boca', 16, 16, (x, y, w, h) => { const e = mb(x, y, w, h); if (e < 0) return null; if (e === 0) return '#fff0a0'; if (e === 1) return '#d8a420'; return mix('#6a4408', '#8a5e10', rnd(x, y, 262)); });
  m.region('base_lado', 16, 6, (x, y) => y === 0 ? '#3a3a44' : (rnd(x, y, 263) > 0.9 ? '#2a2a32' : '#1c1c22'));
  m.region('base_top', 14, 14, () => '#26262e');
  // placa albiceleste con el 10
  const placa = new Img(12, 8, null);
  placa.fill((x, y) => (x === 0 || y === 0 || x === 11 || y === 7) ? '#d8a420' : ((Math.floor((x - 1) / 2)) % 2 ? '#ffffff' : '#74acdf'));
  placa.text(F35, '10', 3, 1.5, '#1c2a5a', 1);
  m.region('placa', 12, 8, placa.painter());
  m.region('placa_canto', 4, 4, () => '#d8a420');

  const cx = 8, cz = 8;
  disc(m, cx, cz, 0, 1, 10, 10, { sides: 'oro_lado', up: 'oro_top', down: 'base_top' }, 'base_aro');
  m.cube([4, 1, 4], [12, 4.5, 12], { sides: 'base_lado', up: 'base_top', down: 'base_top' }, null, 'base_pedestal');
  m.cube([5, 1.75, 3.6], [10, 4, 4], { north: { r: 'placa', full: true }, south: null, east: 'placa_canto', west: 'placa_canto', up: 'placa_canto', down: 'placa_canto' }, null, 'placa_10');
  disc(m, cx, cz, 4.5, 5.25, 7, 7, { sides: 'oro_lado', up: 'oro_top', down: 'oro_top' }, 'base_tapa');
  // tallo con nudo
  disc(m, cx, cz, 5.25, 6.5, 2.5, 2.5, { sides: 'oro_lado', up: 'oro_top', down: null }, 'tallo_abajo', 2);
  disc(m, cx, cz, 6.5, 7.75, 3.5, 3.5, { sides: 'oro_lado', up: 'oro_top', down: 'oro_top' }, 'nudo', 2);
  m.cube([7.25, 7.75, 7.25], [8.75, 9.25, 8.75], { sides: 'oro_lado', up: 'oro_top', down: null }, null, 'tallo');
  // caliz
  disc(m, cx, cz, 9.25, 10.25, 4, 4, { sides: 'oro_lado', up: 'oro_top', down: 'oro_top' }, 'caliz_fondo', 2);
  disc(m, cx, cz, 10.25, 11.5, 6.5, 6.5, { sides: 'oro_lado', up: 'oro_top', down: 'oro_top' }, 'caliz_bajo');
  disc(m, cx, cz, 11.5, 15, 8, 8, { sides: 'oro_lado', up: 'copa_boca', down: 'oro_top' }, 'caliz');
  // asas curvas a los costados (este y oeste)
  for (const s of [-1, 1]) {
    const x0 = cx + s * 4, n = s < 0 ? 'o' : 'e';
    const X = (a, b) => s < 0 ? [x0 - b, x0 - a] : [x0 + a, x0 + b];
    const [a1, b1] = X(0, 2.25); m.cube([a1, 13.5, 7.5], [b1, 14.25, 8.5], { all: 'oro_lado', up: 'oro_top' }, null, 'asa_' + n + '_arriba');
    const [a2, b2] = X(1.75, 2.5); m.cube([a2, 10.75, 7.5], [b2, 13.75, 8.5], { all: 'oro_lado' }, { angle: s * 22.5, axis: 'z', origin: [(a2 + b2) / 2, 12.25, 8] }, 'asa_' + n + '_curva');
    const [a3, b3] = X(0, 1.75); m.cube([a3, 10.5, 7.5], [b3, 11.25, 8.5], { all: 'oro_lado', up: 'oro_top' }, null, 'asa_' + n + '_abajo');
  }
  const r = save('la_copa', m, { folder: 'fisura', bbDir: 'bbmodels/fisura', displayKind: 'alto' });
  // item de inventario: hereda del bloque y repite su display
  const itemFile = path.join(ASSETS, 'models', 'item', 'fisura', 'la_copa.json');
  fs.mkdirSync(path.dirname(itemFile), { recursive: true });
  fs.writeFileSync(itemFile, stringify({ parent: 'ultimatefoods:block/fisura/la_copa', display: r.json.display }) + '\n');
  return r;
}

// ================= BOMBILLA =================
function bombilla() {
  const m = new ModelBuilder({ size: 32, density: 4 });
  const ACERO = ['#5a6470', '#8a96a4', '#b8c4d0', '#dfe8f0', '#a8c0e0'];
  const acero = (x, y, w, h) => { const k = x / Math.max(1, w - 1); let c = ramp(ACERO, 0.2 + (1 - Math.abs(k - 0.35) * 1.8) * 0.75); if (k > 0.55 && k < 0.7) c = '#a8c0e0'; return c; };
  m.region('acero', 4, 8, acero);
  m.region('acero_top', 4, 4, () => '#dfe8f0');
  m.region('anillo', 4, 2, (x, y) => y === 0 ? '#f4f8fc' : '#7a8694');
  // filtro: cuchara con agujeritos y borde brillante
  m.region('filtro', 10, 16, (x, y, w, h) => {
    if (x === 0 || x === w - 1 || y === h - 1) return '#e8f0f8';
    if (y < 3) return acero(x, y, w, h);
    if ((x + (y % 2)) % 2 === 0 && y > 4 && y < h - 2 && x > 1 && x < w - 2) return '#3a3e46';
    return mix('#b8c4d0', '#8a96a4', rnd(x, y, 271));
  });
  m.region('filtro_canto', 4, 4, () => '#c8d4e0');
  const T = (x1, y1, z1, x2, y2, z2, faces, n) => m.cube([x1, y1, z1], [x2, y2, z2], faces, null, n);
  // filtro (abajo): gota plana con el canto levantado
  T(7, 0, 7.6, 9, 0.5, 8.4, { all: 'filtro_canto' }, 'filtro_punta');
  T(6.75, 0.5, 7.6, 9.25, 2.5, 8.4, { north: { r: 'filtro', oy: 6 }, south: { r: 'filtro', oy: 6 }, east: 'filtro_canto', west: 'filtro_canto', up: 'filtro_canto', down: 'filtro_canto' }, 'filtro_ancho');
  T(7.1, 2.5, 7.65, 8.9, 3.25, 8.35, { north: { r: 'filtro', oy: 2 }, south: { r: 'filtro', oy: 2 }, east: 'filtro_canto', west: 'filtro_canto', up: 'filtro_canto', down: null }, 'filtro_medio');
  T(7.45, 3.25, 7.7, 8.55, 4, 8.3, { all: 'acero', up: null }, 'filtro_cuello');
  // cano
  T(7.65, 4, 7.65, 8.35, 10.25, 8.35, { sides: 'acero', up: null, down: null }, 'cano');
  // virola de anillos
  for (const [i, y] of [10.25, 10.65, 11.05].entries()) T(7.5, y, 7.5, 8.5, y + 0.3, 8.5, { sides: 'anillo', up: 'acero_top', down: 'acero_top' }, 'anillo_' + (i + 1));
  T(7.6, 10.55, 7.6, 8.4, 11.5, 8.4, { sides: 'acero', up: null, down: null }, 'virola');
  // boquilla plana que se ensancha hacia arriba
  T(7.5, 11.5, 7.75, 8.5, 13.5, 8.25, { sides: 'acero', up: null, down: 'acero_top' }, 'boquilla_baja');
  T(7.35, 13.5, 7.75, 8.65, 15.5, 8.25, { sides: 'acero', up: null, down: 'acero_top' }, 'boquilla_alta');
  T(7.25, 15.5, 7.75, 8.75, 16, 8.25, { sides: 'acero', up: 'acero_top', down: 'acero_top' }, 'boquilla_punta');
  const b = bounds(m.buildElements());
  const display = {
    gui: { rotation: [0, 0, -45], translation: [0, 0, 0], scale: [1.25, 1.25, 1.25] },
    ground: { rotation: [0, 0, -45], translation: [0, 2, 0], scale: [0.5, 0.5, 0.5] },
    fixed: { rotation: [0, 0, -45], translation: [0, 0, 0], scale: [1.1, 1.1, 1.1] },
    thirdperson_righthand: { rotation: [0, -90, -35], translation: [0, 4, 0.5], scale: [0.75, 0.75, 0.75] },
    thirdperson_lefthand: { rotation: [0, 90, 35], translation: [0, 4, 0.5], scale: [0.75, 0.75, 0.75] },
    firstperson_righthand: { rotation: [0, -90, -25], translation: [1.13, 3.2, 1.13], scale: [0.6, 0.6, 0.6] },
    firstperson_lefthand: { rotation: [0, 90, 25], translation: [1.13, 3.2, 1.13], scale: [0.6, 0.6, 0.6] },
    head: { rotation: [0, 0, -45], translation: [0, 10, 0], scale: [0.8, 0.8, 0.8] }
  };
  return save('bombilla', m, { kind: 'item', folder: 'fisura', texName: 'bombilla3d', display, bbDir: 'bbmodels/fisura' });
}

laCopa(); bombilla();
