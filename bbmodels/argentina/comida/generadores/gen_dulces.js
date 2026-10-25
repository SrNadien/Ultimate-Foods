// Grupo dulces: alfajor (uno banado en chocolate y uno de maicena), panqueque_con_dulce_de_leche (dos arrollados),
// flan_con_dulce_de_leche (flan con caramelo, dulce de leche y crema), pitusas (galletitas rellenas) y pepas (de membrillo).
const { ModelBuilder, mix, shade, rnd, blob, ramp, save, disc, discMask, plato, regionesPlato } = require('./lib');

const DDL = ['#8a4a1c', '#9a5624', '#7a3e16', '#a8642c'];   // dulce de leche

// ================= ALFAJOR =================
function alfajor() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14 });
  const mc = discMask(7, 7), mm = discMask(6, 6);
  m.region('choco_top', 14, 14, (x, y, w, h) => { const e = mc(x, y, w, h); if (e < 0) return null; let c = ramp(['#3a1e10', '#4a2614', '#542c18'], blob(x, y, 3, 211)); if (e === 0) c = '#2a140a'; if (x > 3 && x < 7 && y > 2 && y < 5) c = '#6a4026'; return c; });
  m.region('choco_lado', 28, 6, (x, y) => { let c = y < 2 ? '#4a2614' : '#3a1e10'; if (y === 3 && x % 5 === 2) c = '#2a140a'; return c; });
  m.region('maicena_top', 12, 12, (x, y, w, h) => { const e = mm(x, y, w, h); if (e < 0) return null; let c = rnd(x, y, 212) > 0.85 ? '#f0dc98' : '#f6e8b0'; if (e === 0) c = '#e8cc80'; return c; });
  m.region('maicena_lado', 24, 2, (x, y) => y === 0 ? '#f6e8b0' : '#e8cc80');
  m.region('coco', 24, 2, (x, y) => rnd(x, y, 213) > 0.45 ? '#f8f6f0' : ramp(DDL, rnd(x, y, 214)));
  plato(m, { w: 14 });
  // alfajor banado en chocolate (adelante a la derecha)
  disc(m, 9.75, 6.25, 1, 3.25, 7, 7, { sides: 'choco_lado', up: 'choco_top', down: null }, 'alfajor_chocolate');
  // alfajor de maicena con coco (atras a la izquierda): tapa, dulce de leche con coco, tapa
  disc(m, 5.75, 10.25, 1, 1.75, 6, 6, { sides: 'maicena_lado', up: null, down: null }, 'maicena_abajo');
  disc(m, 5.75, 10.25, 1.75, 2.5, 6, 6, { sides: 'coco', up: null, down: null }, 'maicena_dulce');
  disc(m, 5.75, 10.25, 2.5, 3.25, 6, 6, { sides: 'maicena_lado', up: 'maicena_top', down: null }, 'maicena_arriba');
  return save('alfajor', m);
}

// ================= PANQUEQUE CON DULCE DE LECHE =================
function panqueque() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14 });
  m.region('crepe', 22, 8, (x, y, w, h) => { let c = ramp(['#f2dca0', '#ecd08c', '#f6e4b0'], rnd(x, y, 221)); if (blob(x, y, 2, 222) > 0.6) c = mix('#b8782a', '#e0a850', rnd(x, y, 223)); if (rnd(x, y, 224) > 0.93) c = '#fffaf0'; return c; });
  m.region('crepe_punta', 8, 8, (x, y, w, h) => { const d = Math.max(Math.abs(x - 3.5), Math.abs(y - 3.5)); return d > 2.6 ? '#ecd08c' : (d > 1.6 ? DDL[1] : (d > 0.6 ? '#e6c47a' : DDL[2])); });
  m.region('ddl', 4, 4, (x, y) => y === 0 ? '#b0702e' : DDL[(x + y) % 4]);
  plato(m, { w: 14 });
  for (const [z, n] of [[3.75, 'a'], [8.75, 'b']]) {
    const x1 = 2.5, x2 = 13.5;
    m.cube([x1, 1, z], [x2, 3, z + 3.5], { all: 'crepe', east: 'crepe_punta', west: 'crepe_punta' }, null, 'panqueque_' + n);
    m.cube([x1 + 0.25, 1.4, z - 0.4], [x2 - 0.25, 2.6, z + 3.9], { all: 'crepe', east: 'crepe_punta', west: 'crepe_punta' }, null, 'panqueque_' + n + '_lomo');
    m.cube([x1 + 1, 3, z + 1.25], [x2 - 1.5, 3.4, z + 2.25], { all: 'ddl' }, null, 'hilo_dulce_' + n);
    m.cube([x2, 1, z + 0.75], [x2 + 1, 1.75, z + 2.75], { all: 'ddl' }, null, 'dulce_' + n);
  }
  return save('panqueque_con_dulce_de_leche', m);
}

// ================= FLAN CON DULCE DE LECHE =================
function flan() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14 });
  const mf = discMask(6, 6), mcar = discMask(9, 9);
  m.region('flan_lado', 24, 8, (x, y, w, h) => { if (y < 2 || (y < 4 && rnd(x, 0, 231) > 0.6)) return '#9a4a14'; return ramp(['#f2d47a', '#ecc868', '#f6dc8c'], rnd(x, y, 232)); });
  m.region('flan_top', 12, 12, (x, y, w, h) => { const e = mf(x, y, w, h); if (e < 0) return null; return e === 0 ? '#7a3408' : (rnd(x, y, 233) > 0.85 ? '#c06a20' : '#a8541a'); });
  m.region('caramelo', 18, 18, (x, y, w, h) => { const e = mcar(x, y, w, h); if (e < 0) return null; return e === 0 ? '#c8803a' : '#b0601c'; });
  m.region('ddl_lado', 16, 4, (x, y) => ramp(DDL, 0.3 + y * 0.15 + rnd(x, y, 234) * 0.2));
  m.region('ddl_top', 8, 8, (x, y) => (x + y) % 5 === 0 ? '#c08040' : DDL[1]);
  m.region('crema_lado', 16, 4, (x, y) => (x + y) % 4 === 0 ? '#e8e2d4' : '#fbf8f0');
  m.region('crema_top', 8, 8, (x, y) => (x * 3 + y) % 5 === 0 ? '#e8e2d4' : '#ffffff');
  plato(m, { w: 14 });
  disc(m, 8.5, 8.5, 1, 1.15, 9, 9, { sides: 'caramelo', up: 'caramelo', down: null }, 'caramelo_charco');
  disc(m, 8.5, 8.5, 1.15, 5, 6, 6, { sides: 'flan_lado', up: 'flan_top', down: null }, 'flan');
  disc(m, 8.5, 8.5, 5, 5.5, 4.5, 4.5, { sides: 'flan_lado', up: 'flan_top', down: null }, 'flan_cima', 2);
  // cucharada de dulce de leche y copo de crema
  disc(m, 4.25, 4.75, 1, 2.5, 3.5, 3.5, { sides: 'ddl_lado', up: 'ddl_top', down: null }, 'dulce_de_leche', 2);
  m.cube([3.75, 2.5, 4.25], [4.75, 3, 5.25], { all: 'ddl_top' }, null, 'dulce_de_leche_punta');
  disc(m, 4.25, 11.5, 1, 2.25, 3.5, 3.5, { sides: 'crema_lado', up: 'crema_top', down: null }, 'crema', 2);
  m.cube([3.5, 2.25, 10.75], [5, 3.25, 12.25], { all: 'crema_lado', up: 'crema_top' }, null, 'crema_medio');
  m.cube([4, 3.25, 11.25], [4.5, 3.75, 11.75], { all: 'crema_top' }, null, 'crema_punta');
  return save('flan_con_dulce_de_leche', m);
}

// ================= PITUSAS =================
function pitusas() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14 });
  const mg = discMask(5, 5);
  // galletita dorada con borde festoneado, letras en relieve y puntitos rosados (como el paquete)
  m.region('gall_top', 10, 10, (x, y, w, h) => { const e = mg(x, y, w, h); if (e < 0) return null; if (e === 0) return (x + y) % 2 ? '#c8862e' : '#dca04a'; if ((x === 4 || x === 5) && (y === 4 || y === 5)) return '#e88aa8'; if (e === 1) return '#d89640'; return rnd(x, y, 241) > 0.8 ? '#e8b460' : '#e2a854'; });
  m.region('gall_lado', 20, 2, (x, y) => x % 2 ? '#c8862e' : '#d89a44');
  m.region('relleno', 20, 1, () => '#fbf6ea');
  plato(m, { w: 14 });
  const pitusa = (cx, cz, y0, n) => {
    disc(m, cx, cz, y0, y0 + 0.5, 5, 5, { sides: 'gall_lado', up: null, down: 'gall_top' }, n + '_abajo', 2);
    disc(m, cx, cz, y0 + 0.5, y0 + 1, 5, 5, { sides: 'relleno', up: null, down: null }, n + '_relleno', 2);
    disc(m, cx, cz, y0 + 1, y0 + 1.5, 5, 5, { sides: 'gall_lado', up: 'gall_top', down: null }, n + '_arriba', 2);
  };
  // pila de tres y dos sueltas
  pitusa(6.5, 6.5, 1, 'pila_1'); pitusa(6.75, 6.25, 2.5, 'pila_2'); pitusa(6.5, 6.5, 4, 'pila_3');
  pitusa(10.75, 7, 1, 'suelta_1'); pitusa(8.25, 11, 1, 'suelta_2');
  return save('pitusas', m);
}

// ================= PEPAS =================
function pepas() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14 });
  const mp = discMask(4.5, 4.5), mm = discMask(2, 2);
  m.region('pepa_top', 10, 10, (x, y, w, h) => { const e = mp(x, y, w, h); if (e < 0) return null; if (e === 0) return '#c8863a'; return rnd(x, y, 251) > 0.75 ? '#f0c27a' : '#e6b066'; });
  m.region('pepa_lado', 18, 2, (x, y) => y === 0 ? '#e6b066' : '#c07a30');
  m.region('membrillo', 4, 4, (x, y) => (x === 1 && y === 1) ? '#c8402a' : ((x + y) % 3 ? '#8a1a14' : '#a82418'));
  plato(m, { w: 14 });
  const P = [[5, 5], [10.5, 4.75], [8, 8.5], [4.75, 11], [11, 11]];
  P.forEach(([x, z], i) => {
    disc(m, x, z, 1, 2, 4.5, 4.5, { sides: 'pepa_lado', up: 'pepa_top', down: null }, 'pepa_' + (i + 1), 2);
    m.cube([x - 1, 2, z - 1], [x + 1, 2.4, z + 1], { all: 'membrillo' }, null, 'membrillo_' + (i + 1));
  });
  return save('pepas', m);
}

alfajor(); panqueque(); flan(); pitusas(); pepas();
