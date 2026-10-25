// Grupo cuchara: guiso (bowl blanco con lentejas y carne), locro (cazuela de barro) y noquis (plato hondo con tuco y queso).
const { ModelBuilder, mix, shade, rnd, blob, ramp, save, disc, discMask, octRing } = require('./lib');

// regiones de un bowl: canto exterior, interior y borde
function regionesBowl(m, pre, { ext, int, borde, alto = 8 }) {
  m.region(pre + '_lado', 14, alto, (x, y, w, h) => mix(ext[0], ext[1], y / (h - 1)));
  m.region(pre + '_in', 14, alto, (x, y, w, h) => mix(int[0], int[1], y / (h - 1)));
  m.region(pre + '_borde', 14, 4, (x, y) => y === 0 ? mix(borde, '#ffffff', 0.35) : borde);
  m.region(pre + '_fondo', 16, 16, (x, y) => shade(ext[1], 0.9));
}
function bowl(m, pre, { cx = 8, cz = 8, R = 6.75, t = 1, pie = 6, cuerpo = 11.5, yPie = 0.75, yCuerpo = 2.5, yBorde = 6.5 }) {
  if (pie) disc(m, cx, cz, 0, yPie, pie, pie, { sides: pre + '_lado', up: null, down: pre + '_fondo' }, pre + '_pie', 2);
  disc(m, cx, cz, pie ? yPie : 0, yCuerpo, cuerpo, cuerpo, { sides: pre + '_lado', up: pre + '_in', down: pre + '_fondo' }, pre + '_cuerpo', 3);
  octRing(m, cx, cz, yCuerpo, yBorde, R, t, { out: pre + '_lado', in: pre + '_in', edge: pre + '_lado', top: pre + '_borde', bottom: pre + '_fondo' }, pre + '_pared');
  return 2 * (R - t);
}
const trozo = (m, x, z, y, s, h, reg, name, ang) => m.cube([x, y, z], [x + s, y + h, z + s], { all: reg }, ang ? { angle: ang, axis: 'y', origin: [x + s / 2, y, z + s / 2] } : null, name);

// ================= GUISO =================
function guiso() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesBowl(m, 'bowl', { ext: ['#fbfaf6', '#d8d4cc'], int: ['#f2f0ea', '#cfcac0'], borde: '#f6f4ee' });
  const inner = 2 * (6.75 - 1), mk = discMask(inner, inner, 'oct');
  // lentejas en caldo: puntitos marron verdoso sobre caldo, brillos
  m.region('guiso_top', 23, 23, (x, y, w, h) => {
    if (mk(x, y, w, h) < 0) return null;
    const r = rnd(x, y, 121), b = blob(x, y, 3, 122);
    let c = (x + y * 2) % 3 === 0 || r > 0.55 ? ramp(['#6a5226', '#7a6030', '#5a4420', '#86683a'], rnd(x, y, 123)) : ramp(['#8a6230', '#7a5428'], b);
    if (r > 0.97) c = '#c8a878';                 // brillo de grasa
    else if (r < 0.03) c = '#d8642a';            // zanahoria
    return c;
  });
  m.region('guiso_lado', 16, 6, (x, y) => ramp(['#6a5226', '#7a5428'], rnd(x, y, 124)));
  m.region('carne', 4, 4, (x, y) => (x + y) % 3 ? '#6a3a24' : '#8a5236');
  m.region('zanahoria', 4, 4, (x, y) => (x + y) % 2 ? '#e8742a' : '#d0601e');
  m.region('papa', 4, 4, (x, y) => (x * y) % 3 ? '#ecd890' : '#d8c070');
  m.region('chorizo', 4, 4, (x, y) => (x + y) % 3 ? '#9a3a26' : '#c87a5a');

  bowl(m, 'bowl', {});
  disc(m, 8, 8, 2.5, 5.5, inner, inner, { sides: 'guiso_lado', up: 'guiso_top', down: null }, 'guiso', 'oct');
  trozo(m, 4.5, 5, 5.5, 1.75, 0.75, 'carne', 'carne_1', 22.5);
  trozo(m, 9, 9.5, 5.5, 2, 0.75, 'carne', 'carne_2');
  trozo(m, 9.5, 4.5, 5.5, 1.25, 0.5, 'zanahoria', 'zanahoria_1', 45);
  trozo(m, 5, 10, 5.5, 1.25, 0.5, 'papa', 'papa_1', -22.5);
  trozo(m, 6.75, 7.25, 5.5, 1.5, 0.5, 'chorizo', 'chorizo_1');
  return save('guiso', m, { displayKind: 'bloque' });
}

// ================= LOCRO =================
function locro() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  // cazuela de barro
  m.region('barro_lado', 14, 8, (x, y, w, h) => { let c = mix('#9a5a34', '#6e3a20', y / (h - 1)); if (rnd(x, y, 131) > 0.85) c = shade(c, 0.9); return c; });
  m.region('barro_in', 14, 8, (x, y, w, h) => mix('#7a4226', '#4e2814', y / (h - 1)));
  m.region('barro_borde', 14, 4, (x, y) => y === 0 ? '#b8744a' : (rnd(x, y, 132) > 0.7 ? '#3a2a20' : '#9a5a34'));   // borde con tizne
  m.region('barro_fondo', 16, 16, () => '#5a3018');
  m.region('asa', 4, 4, (x, y) => y === 0 ? '#b0703f' : '#8a4c2a');
  const inner = 2 * (7 - 1), mk = discMask(inner, inner, 'oct');
  m.region('locro_top', 24, 24, (x, y, w, h) => {
    if (mk(x, y, w, h) < 0) return null;
    const r = rnd(x, y, 133), b = blob(x, y, 3, 134);
    let c = ramp(['#e09a2a', '#eaae3a', '#f0c050', '#d88a24'], b);
    if (r > 0.9) c = '#f8f0d0';                                 // maiz blanco
    else if (r > 0.86) c = '#f4d878';                           // maiz amarillo
    else if (blob(x, y, 2, 135) > 0.82) c = mix(c, '#c8401a', 0.6); // grasa colorada
    if (rnd(x >> 1, y, 136) > 0.93) c = r > 0.5 ? '#3a8a2a' : '#5aaa3a'; // verdeo
    return c;
  });
  m.region('locro_lado', 16, 6, (x, y) => ramp(['#d88a24', '#e09a2a'], rnd(x, y, 137)));
  m.region('carne', 4, 4, (x, y) => (x + y) % 3 ? '#8a5a3a' : '#a8785a');
  m.region('zapallo', 4, 4, (x, y) => (x + y) % 2 ? '#f0a030' : '#e08a20');

  disc(m, 8, 8, 0, 2, 12, 12, { sides: 'barro_lado', up: 'barro_in', down: 'barro_fondo' }, 'cazuela_cuerpo', 3);
  octRing(m, 8, 8, 2, 5.5, 7, 1, { out: 'barro_lado', in: 'barro_in', edge: 'barro_lado', top: 'barro_borde', bottom: 'barro_fondo' }, 'cazuela_pared');
  m.cube([0.25, 4, 7], [1.1, 5, 9], { all: 'asa' }, null, 'asa_o');
  m.cube([14.9, 4, 7], [15.75, 5, 9], { all: 'asa' }, null, 'asa_e');
  disc(m, 8, 8, 2, 4.75, inner, inner, { sides: 'locro_lado', up: 'locro_top', down: null }, 'locro', 'oct');
  trozo(m, 5, 5.5, 4.75, 2, 0.75, 'carne', 'carne_1', 22.5);
  trozo(m, 9.5, 8.5, 4.75, 1.5, 0.5, 'zapallo', 'zapallo_1');
  trozo(m, 6, 9.75, 4.75, 1.25, 0.5, 'zapallo', 'zapallo_2', 45);
  return save('locro', m, { displayKind: 'bloque' });
}

// ================= NOQUIS =================
function noquis() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesBowl(m, 'hondo', { ext: ['#fbfaf6', '#d4d0c8'], int: ['#f4f2ec', '#dedad2'], borde: '#f8f6f0', alto: 4 });
  const inner = 2 * (7 - 0.75), mk = discMask(inner, inner, 'oct');
  m.region('fondo_salsa', 25, 25, (x, y, w, h) => {
    const e = mk(x, y, w, h); if (e < 0) return null;
    if (e < 2) return '#f2f0ea';                                 // loza del plato hondo
    return rnd(x, y, 141) > 0.85 ? '#f4ecd8' : (blob(x, y, 3, 142) > 0.6 ? '#c84a2a' : '#f0e2b0');
  });
  m.region('noqui', 4, 3, (x, y) => y === 0 ? '#f6e8b8' : (x % 2 ? '#e8d498' : '#dcc488'));   // estrias del tenedor
  m.region('noqui_salsa', 4, 4, (x, y) => { const r = rnd(x, y, 143); return r > 0.75 ? '#f4ecd0' : (r > 0.35 ? '#c43c22' : '#a8301c'); }); // tuco con queso rallado
  m.region('salsa_top', 12, 12, (x, y) => { const r = rnd(x, y, 144); return r > 0.8 ? '#f6eed6' : (r > 0.3 ? ramp(['#c8442a', '#b23620', '#d85a34'], blob(x, y, 2, 145)) : '#9a2c18'); });

  disc(m, 8, 8, 0, 0.5, 8, 8, { sides: 'hondo_lado', up: null, down: 'hondo_fondo' }, 'hondo_pie', 2);
  disc(m, 8, 8, 0.5, 1.5, 12, 12, { sides: 'hondo_lado', up: 'hondo_in', down: 'hondo_fondo' }, 'hondo_cuerpo', 3);
  octRing(m, 8, 8, 1.5, 3, 7, 0.75, { out: 'hondo_lado', in: 'hondo_in', edge: 'hondo_lado', top: 'hondo_borde', bottom: 'hondo_fondo' }, 'hondo_pared');
  disc(m, 8, 8, 1.5, 2.25, inner, inner, { sides: null, up: 'fondo_salsa', down: null }, 'fondo', 'oct');
  // montoncito de noquis: anillo de afuera simple, centro con tuco
  const N = [[3.5, 5], [3.25, 8], [4.25, 10.75], [6.75, 11.75], [9.5, 11.5], [11.5, 9.5], [11.75, 6.75], [10.5, 4.25], [7.75, 3.5], [5.5, 3.75]];
  N.forEach(([x, z], i) => m.cube([x - 0.75, 2.25, z - 0.6], [x + 0.75, 3.25, z + 0.6], { all: 'noqui', up: 'noqui' }, i % 2 ? { angle: i % 4 === 1 ? 22.5 : -22.5, axis: 'y', origin: [x, 2.25, z] } : null, 'noqui_' + (i + 1)));
  const C = [[6, 6.5], [8.25, 6], [9.75, 8], [7.75, 9.25], [5.75, 9]];
  C.forEach(([x, z], i) => m.cube([x - 0.75, 2.25, z - 0.6], [x + 0.75, 3.5, z + 0.6], { all: 'noqui', up: 'noqui_salsa' }, i % 2 ? { angle: 45, axis: 'y', origin: [x, 2.25, z] } : null, 'noqui_c' + (i + 1)));
  m.cube([5.5, 3.5, 5.5], [10.5, 4.25, 10.5], { up: 'salsa_top', down: null, sides: 'noqui_salsa' }, null, 'tuco');
  m.cube([6.75, 4.25, 6.75], [9.25, 4.75, 9.25], { up: 'salsa_top', down: null, sides: 'noqui_salsa' }, null, 'tuco_cima');
  return save('noquis', m);
}

guiso(); locro(); noquis();
