// Grupo parrilla: asado (tira, vacio y chorizo en tabla), choripan, chorizo_asado (en tablita), provoleta (provoletera de hierro).
const { ModelBuilder, mix, shade, rnd, blob, ramp, save, disc, discMask } = require('./lib');

// ---------- pintores compartidos ----------
const MADERA = ['#b9824a', '#c48d52', '#cf9a5e', '#c08a50'];
function maderaTop(seed) {
  return (x, y, w, h) => {
    const veta = Math.sin((y + blob(x, 0, 9, seed) * 3) * 1.3) > 0.82;
    let c = ramp(MADERA, blob(x * 0.3, y, 4, seed));
    if (veta) c = shade(c, 0.86);
    if (rnd(x, y, seed) > 0.96) c = shade(c, 0.8);              // cortes de cuchillo
    if (rnd(x >> 2, y, seed + 1) > 0.97) c = mix(c, '#6a2c18', 0.35); // juguito
    if (x === 0 || y === 0 || x === w - 1 || y === h - 1) c = shade(c, 0.9);
    return c;
  };
}
const maderaLado = (x, y, w, h) => y === 0 ? '#d6a46a' : (rnd(x, y, 3) > 0.8 ? '#9a6838' : '#a9733f');

// carne asada: costra marron con marcas de parrilla diagonales
function costra(seed, base = ['#5c2a14', '#6e3418', '#7c3e1c', '#8a4a22'], marcas = true) {
  return (x, y) => {
    let c = ramp(base, blob(x, y, 3, seed) * 0.8 + rnd(x, y, seed) * 0.3);
    if (marcas && (x + (y >> 1)) % 7 === 0) c = mix(c, '#22100a', 0.7);
    if (rnd(x, y, seed + 5) > 0.93) c = mix(c, '#e0a050', 0.5);   // grasita dorada
    return c;
  };
}
const CHIMI = ['#3f6a1c', '#5a8a24', '#2f5214', '#7a9a2a'];

// ================= ASADO =================
function asado() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  m.region('tabla_top', 28, 24, maderaTop(11));
  m.region('tabla_lado', 28, 2, maderaLado);
  // tira de asado: arriba costra, costado con cortes de hueso (blanco hueso) y carne
  m.region('tira_top', 24, 6, costra(21));
  m.region('tira_lado', 24, 3, (x, y) => {
    const k = x % 6;
    if (y === 0) return rnd(x, y, 22) > 0.5 ? '#3a1a0c' : '#4c2410';
    if (k === 1 || k === 2) return y === 1 ? '#efe2c4' : '#d9c6a0';    // hueso
    if (k === 0 || k === 3) return '#c79c6a';                            // borde del hueso
    return y === 1 ? '#b8664a' : '#9c4e34';                              // carne
  });
  m.region('tira_punta', 6, 3, (x, y) => y === 0 ? '#3a1a0c' : (x === 2 || x === 3 ? '#efe2c4' : '#a85a3e'));
  // vacio: pieza gruesa con costra oscura, corte rosado al frente
  m.region('vacio_top', 14, 12, costra(31, ['#4a2210', '#5a2a14', '#6a3218', '#3a1a0c']));
  m.region('vacio_lado', 14, 4, (x, y) => y === 0 ? '#3a1a0c' : (y === 3 ? '#5a2a14' : (rnd(x, y, 32) > 0.75 ? '#c87a5e' : '#b45e46')));
  m.region('vacio_atras', 14, 4, (x, y) => y === 0 ? '#3a1a0c' : (rnd(x, y, 33) > 0.6 ? '#5a2a14' : '#6a3218'));
  // chorizo
  m.region('chori', 12, 6, (x, y, w, h) => {
    let c = ramp(['#6a2414', '#7a2e1a', '#8a3820'], blob(x, y, 2, 41));
    if (y === 1 && rnd(x, 0, 42) > 0.3) c = '#b4583a';           // brillo
    if (rnd(x, y, 43) > 0.9) c = '#3a140a';                       // tostado
    return c;
  });
  m.region('chori_punta', 4, 4, (x, y) => (x === 0 || y === 0 || x === 3 || y === 3) ? '#5a2010' : '#7a2e1a');
  m.region('chimi', 8, 8, (x, y) => rnd(x, y, 44) > 0.45 ? ramp(CHIMI, rnd(x, y, 45)) : null);

  m.cube([1, 0, 2], [15, 1, 14], { sides: 'tabla_lado', up: { r: 'tabla_top', frame: [1, 2, 15, 14] }, down: { r: 'tabla_top', frame: [1, 2, 15, 14] } }, null, 'tabla');
  m.cube([2, 1, 3], [14, 2.5, 6], { up: 'tira_top', down: 'tira_top', north: 'tira_lado', south: 'tira_lado', east: 'tira_punta', west: 'tira_punta' }, null, 'tira');
  // vacio cortado en tres feteadas: las caras del corte (este/oeste) muestran la carne rosada
  m.region('vacio_corte', 12, 4, (x, y) => y === 0 ? '#3a1a0c' : (y === 3 ? '#6a3218' : (rnd(x, y, 34) > 0.7 ? '#d08a70' : (rnd(x, y, 35) > 0.5 ? '#c27458' : '#b86a50'))));
  [[2.5, 4.5], [4.75, 6.75], [7, 9.5]].forEach(([a, b], i) => m.cube([a, 1, 7 + i * 0.25], [b, 3 - i * 0.25, 13 - i * 0.25], { up: 'vacio_top', down: 'vacio_top', north: 'vacio_lado', south: 'vacio_atras', east: 'vacio_corte', west: 'vacio_corte' }, null, 'vacio_' + (i + 1)));
  m.cube([7.5, 2.5, 8.5], [9.25, 2.75, 11], { up: 'chimi', north: null, south: null, east: null, west: null, down: null }, null, 'chimichurri');
  m.cube([10.5, 1, 7.5], [13, 3.5, 12.5], { sides: 'chori', ends: 'chori', north: 'chori_punta', south: 'chori_punta' }, null, 'chorizo');
  m.cube([11, 1.5, 7], [12.5, 3, 7.5], { all: 'chori_punta' }, null, 'chorizo_nudo_n');
  m.cube([11, 1.5, 12.5], [12.5, 3, 13], { all: 'chori_punta' }, null, 'chorizo_nudo_s');
  return save('asado', m);
}

// ================= CHORIPAN =================
const PAN = { costra: ['#c98a40', '#d39a4c', '#dca85a', '#c47e36'], miga: ['#f3e4c2', '#ead7b0', '#f7ecd2'] };
function panCostra(seed) {
  return (x, y) => {
    let c = ramp(PAN.costra, blob(x, y, 3, seed) * 0.7 + rnd(x, y, seed) * 0.3);
    if ((x + 2 * y) % 11 === 0) c = mix(c, '#f0d090', 0.5); // cortes del pan frances
    return c;
  };
}
function choripan() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  m.region('pan_top', 24, 10, (x, y, w, h) => {
    let c = panCostra(51)(x, y);
    const k = (x + y * 0.6) % 8; if (k > 6.6) c = mix(c, '#f2d79a', 0.65); // greñas
    return c;
  });
  m.region('pan_lado', 24, 5, (x, y, w, h) => y === h - 1 ? '#efe0bc' : panCostra(52)(x, y));
  m.region('pan_base', 24, 4, (x, y, w, h) => y === 0 ? '#efe0bc' : panCostra(53)(x, y));
  m.region('pan_punta', 10, 5, (x, y) => panCostra(54)(x, y));
  m.region('pan_abajo', 24, 10, (x, y) => ramp(['#b8742e', '#c4823a'], rnd(x, y, 55)));
  m.region('chori_top', 26, 8, (x, y) => {
    if (rnd(x, y, 56) > 0.45) return ramp(CHIMI, rnd(x, y, 57));
    return ramp(['#7a2e1a', '#8a3820', '#5a2010'], rnd(x, y, 58));
  });
  m.region('chori_lado', 26, 3, (x, y) => {
    if (y === 0 && rnd(x, y, 59) > 0.35) return ramp(CHIMI, rnd(x, y, 60));
    if (y === 2 && rnd(x, 0, 61) > 0.75) return ramp(CHIMI, rnd(x, y, 62)); // gotea
    return y === 1 && rnd(x, y, 63) > 0.6 ? '#b4583a' : ramp(['#7a2e1a', '#8a3820', '#6a2414'], rnd(x, y, 64));
  });
  m.region('chori_punta', 8, 3, (x, y) => x === 0 || x === 7 ? '#5a2010' : (y === 1 ? '#a84a2e' : '#7a2e1a'));

  m.cube([2, 0, 5.5], [14, 2, 10.5], { up: 'pan_abajo', down: 'pan_abajo', sides: 'pan_base', east: 'pan_punta', west: 'pan_punta' }, null, 'pan_abajo');
  m.cube([1.25, 2, 6], [14.75, 3.5, 10], { up: 'chori_top', down: 'chori_top', sides: 'chori_lado', east: 'chori_punta', west: 'chori_punta' }, null, 'chorizo');
  m.cube([2, 3.5, 6], [14, 5, 11], { up: 'pan_top', down: 'pan_abajo', sides: 'pan_lado', east: 'pan_punta', west: 'pan_punta' }, null, 'pan_arriba');
  m.cube([2.75, 5, 6.75], [13.25, 5.75, 10.25], { up: 'pan_top', down: null, sides: { r: 'pan_lado', oy: 0 }, east: 'pan_punta', west: 'pan_punta' }, null, 'pan_lomo');
  return save('choripan', m);
}

// ================= CHORIZO ASADO =================
function chorizo_asado() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  m.region('tabla_top', 24, 16, maderaTop(71));
  m.region('tabla_lado', 28, 2, maderaLado);
  m.region('chori', 22, 6, (x, y, w, h) => {
    let c = ramp(['#5c2012', '#6e2a18', '#7e321c', '#8c3c22'], blob(x, y, 3, 72) * 0.8 + rnd(x, y, 73) * 0.25);
    if (y === 1 && rnd(x, 0, 74) > 0.25) c = '#b25a3c';                  // brillo de grasa
    if (y === 0 && rnd(x, 0, 75) > 0.6) c = '#c8784e';
    if (rnd(x, y, 76) > 0.92) c = '#2e100a';                             // quemado
    if (x % 7 === 3 && y > 2) c = mix(c, '#2e100a', 0.5);                // pliegues
    return c;
  });
  m.region('chori_punta', 6, 6, (x, y) => (x === 0 || y === 0 || x === 5 || y === 5) ? '#4e1a0e' : (x + y) % 3 ? '#7a3020' : '#a4583e');
  m.region('hilo', 4, 4, (x, y) => (x + y) % 2 ? '#e8dcc0' : '#c8b898');

  m.cube([2, 0, 4.5], [14, 1, 11.5], { sides: 'tabla_lado', up: { r: 'tabla_top', frame: [2, 4.5, 14, 11.5] }, down: { r: 'tabla_top', frame: [2, 4.5, 14, 11.5] } }, null, 'tabla');
  m.cube([3.5, 1.4, 6.5], [12.5, 3.35, 9.5], { sides: 'chori', ends: 'chori', east: 'chori_punta', west: 'chori_punta' }, null, 'chorizo');
  m.cube([3.75, 1, 6.9], [12.25, 3.75, 9.1], { sides: 'chori', ends: 'chori', east: 'chori_punta', west: 'chori_punta' }, null, 'chorizo_lomo');
  m.cube([2.75, 1.4, 7], [3.5, 3.35, 9], { all: 'chori_punta' }, null, 'punta_o');
  m.cube([12.5, 1.4, 7], [13.25, 3.35, 9], { all: 'chori_punta' }, null, 'punta_e');
  m.cube([2.25, 2, 7.6], [2.75, 2.75, 8.4], { all: 'hilo' }, null, 'atadura_o');
  m.cube([13.25, 2, 7.6], [13.75, 2.75, 8.4], { all: 'hilo' }, null, 'atadura_e');
  return save('chorizo_asado', m);
}

// ================= PROVOLETA =================
function provoleta() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  const NEGRO = ['#1c1c1e', '#242426', '#2c2c2e'];
  const mp = discMask(11, 11), mq = discMask(9, 9);
  m.region('hierro_lado', 30, 4, (x, y) => y === 0 ? '#55555a' : ramp(NEGRO, rnd(x, y, 81)));
  m.region('hierro_top', 22, 22, (x, y, w, h) => { const e = mp(x, y, w, h); if (e < 0) return null; return e === 0 ? '#5a5a60' : (e === 1 ? '#38383c' : ramp(NEGRO, rnd(x, y, 82))); });
  m.region('mango', 8, 3, (x, y) => y === 0 ? '#4a4a4e' : ramp(NEGRO, rnd(x, y, 83)));
  // queso fundido: amarillo palido con manchas doradas, oregano y aji
  m.region('queso_top', 18, 18, (x, y, w, h) => {
    const e = mq(x, y, w, h); if (e < 0) return null;
    let c = ramp(['#f4e6a0', '#efd884', '#f8eebc', '#e8c86a'], blob(x, y, 3, 84));
    if (e === 0) c = mix(c, '#c07a2a', 0.6);                           // borde dorado
    else if (e === 1) c = mix(c, '#d8a040', 0.35);
    const r = rnd(x, y, 85);
    if (r > 0.86) c = r > 0.94 ? '#4a5a1c' : '#6a7a2a';                // oregano
    else if (r > 0.84) c = '#c8402a';                                  // aji molido
    return c;
  });
  m.region('queso_lado', 30, 2, (x, y) => y === 0 ? '#f0d47a' : '#c8903a');

  const cx = 6.5, cz = 8;
  disc(m, cx, cz, 0, 1.75, 11, 11, { sides: 'hierro_lado', up: 'hierro_top', down: 'hierro_top' }, 'provoletera');
  disc(m, cx, cz, 1.75, 2.5, 9, 9, { sides: 'queso_lado', up: 'queso_top', down: null }, 'queso');
  m.cube([12, 1, 7.25], [15.5, 1.75, 8.75], { all: 'mango' }, null, 'mango');
  return save('provoleta', m);
}

asado(); choripan(); chorizo_asado(); provoleta();
