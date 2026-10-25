// Grupo horno y fritos: empanada (dos al horno con repulgue, en plato), fugazzeta (rellena, en tabla redonda),
// medialuna (dos de manteca en plato) y tortafrita (en plato).
const { ModelBuilder, mix, shade, rnd, blob, ramp, save, disc, discMask, slab, rectsMask, plato, regionesPlato } = require('./lib');

const MASA = ['#e0a858', '#e8b866', '#eec478', '#d89a48'];

// ================= EMPANADA =================
// Segun la foto de referencia: dos empanadas de carne sobre una tabla de madera, una entera (dorada, con
// ampollas y repulgue) y otra abierta al medio mostrando la carne picada.
function empanada() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  // tabla de madera rectangular (el nombre empieza con "tabla": en el item no se dibuja, solo colocada)
  m.region('tabla_top', 28, 20, (x, y, w, h) => {
    let c = ramp(['#d8aa70', '#e0b47a', '#cf9f64', '#e6bc86'], 0.5 + Math.sin(y * 0.9 + blob(x * 0.3, y, 4, 141) * 4) * 0.3 + rnd(x, y, 142) * 0.15);
    if (x === 0 || y === 0 || x === w - 1 || y === h - 1) c = '#f0cc98';
    return c;
  });
  m.region('tabla_lado', 28, 2, (x, y) => y === 0 ? '#f0cc98' : '#c8965a');
  m.region('repulgue', 18, 10, (x, y) => ((x + y) % 3 === 0) ? '#c88a3a' : ((x + y) % 3 === 1 ? '#f2d49a' : '#e6bc78'));
  m.region('emp_lado', 14, 3, (x, y) => y === 0 ? '#ecc888' : (y === 2 ? '#c88a3a' : '#e2b46c'));
  m.region('emp_top', 16, 8, (x, y, w, h) => {
    const cx = Math.abs(x - w / 2 + 0.5) / (w / 2);
    let c = ramp(['#f2d8a0', '#ecc480', '#e0a85a', '#d4923e'], 1 - cx * 0.6 - y / h * 0.2 + blob(x, y, 2, 151) * 0.3 - 0.1);
    if (rnd(x, y, 152) > 0.92) c = '#f8e8c0';   // ampollas de la masa
    return c;
  });
  // carne picada de la empanada abierta: marron con trocitos de cebolla y de huevo
  m.region('emp_relleno', 14, 8, (x, y) => {
    const r = rnd(x, y, 153);
    if (r > 0.93) return '#d8c488';              // huevo
    if (r > 0.84) return '#c89858';              // cebolla
    return ramp(['#5a3018', '#6e3c1e', '#7c4824', '#8a5428'], blob(x, y, 2, 154) + r * 0.25);
  });
  // media luna: franjas medidas desde el borde recto (dz) hacia la curva
  const half = (x0, zf, dir) => {
    const R = (a, b, d0, d1) => { const z1 = zf + dir * d0, z2 = zf + dir * d1; return [x0 + a, Math.min(z1, z2), x0 + b, Math.max(z1, z2)]; };
    return {
      base: [R(0, 9, 0, 2), R(1.25, 7.75, 2, 3.25), R(2.75, 6.25, 3.25, 4.25)],
      domo: [R(1, 8, 0.25, 2.25), R(2.25, 6.75, 2.25, 3.25)]
    };
  };
  slab(m, [[1, 3, 15, 13]], 0, 1, { sides: 'tabla_lado', up: 'tabla_top', down: 'tabla_top' }, 'tabla');
  // entera
  const a = half(1.75, 7.75, -1);
  slab(m, a.base, 1, 2, { sides: 'emp_lado', up: 'repulgue', down: null }, 'empanada_a');
  slab(m, a.domo, 2, 3.25, { sides: 'emp_top', up: 'emp_top', down: null }, 'empanada_a_domo');
  // abierta: el cascaron de masa y, adentro, la carne a la vista
  const b = half(5.25, 8.25, 1);
  slab(m, b.base, 1, 2, { sides: 'emp_lado', up: 'repulgue', down: null }, 'empanada_b');
  slab(m, b.domo, 2, 2.75, { sides: 'emp_relleno', up: 'emp_relleno', down: null }, 'empanada_b_relleno');
  return save('empanada', m);
}

// ================= FUGAZZETA =================
function fugazzeta() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  const mt = discMask(15, 15), mp = discMask(13, 13);
  m.region('tabla_top', 30, 30, (x, y, w, h) => { const e = mt(x, y, w, h); if (e < 0) return null; let c = ramp(['#5a3018', '#6a3a1e', '#4e2a14'], blob(x * 0.4, y, 3, 161)); if (e === 0) c = '#7a4a2a'; return c; });
  m.region('tabla_lado', 32, 2, (x, y) => y === 0 ? '#7a4a2a' : '#4a2814');
  m.region('fuga_top', 26, 26, (x, y, w, h) => {
    const e = mp(x, y, w, h); if (e < 0) return null;
    if (e === 0) return rnd(x, y, 162) > 0.5 ? '#d8a050' : '#c88a3c';                 // borde dorado
    const r = rnd(x, y, 163), s = Math.sin((x * 0.8 + y * 1.3) + blob(x, y, 4, 164) * 6);
    let c = s > 0.3 ? '#f6f0dc' : (s > -0.4 ? '#ece0c0' : '#f0e6cc');                // tiras de cebolla
    if (r > 0.9) c = r > 0.96 ? '#2a1a10' : '#6a4424';                                 // cebolla quemada
    else if (blob(x, y, 3, 165) > 0.78) c = '#f4d878';                                 // queso que asoma
    return c;
  });
  m.region('fuga_lado', 32, 5, (x, y) => y === 0 ? '#f0e6cc' : (y === 2 ? (rnd(x, y, 166) > 0.3 ? '#f6d860' : '#fff0a0') : ramp(MASA, rnd(x, y, 167))));
  disc(m, 8, 8, 0, 0.75, 15, 15, { sides: 'tabla_lado', up: 'tabla_top', down: 'tabla_top' }, 'tabla');
  disc(m, 8, 8, 0.75, 3.25, 13, 13, { sides: 'fuga_lado', up: 'fuga_top', down: null }, 'fugazzeta');
  return save('fugazzeta', m);
}

// ================= MEDIALUNA =================
function medialuna() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14 });
  // capas del hojaldre: franjas transversales doradas con almibar brillante
  const capas = (x, y, w, h) => { let c = ramp(['#b86a24', '#c87c2c', '#d89040', '#e4a654'], 0.5 + Math.sin(x * 1.4) * 0.35 + rnd(x, y, 171) * 0.2); if (y === 0 && rnd(x, 0, 172) > 0.4) c = '#f0c070'; return c; };
  m.region('ml_lado', 10, 6, capas);
  m.region('ml_top', 10, 6, (x, y, w, h) => { let c = capas(x, y + 1, w, h); if (rnd(x, y, 173) > 0.88) c = '#f8dc98'; return c; });
  m.region('ml_punta', 4, 4, (x, y) => (x + y) % 2 ? '#e8b060' : '#d49444');
  plato(m, { w: 14 });
  const ml = (cx, cz, flip, n) => {
    const s = flip ? -1 : 1;
    // arco: centro grueso, dos tramos a 22,5 grados y dos puntas a 45 grados que se curvan hacia el lado concavo
    m.cube([cx - 1.5, 1, cz - 1.5], [cx + 1.5, 3.75, cz + 1.5], { all: 'ml_lado', up: 'ml_top' }, null, n + '_centro');
    for (const side of [-1, 1]) {
      let ex = cx + side * 1.5, ez = cz;
      for (const [ang, len, dep, alt, nm] of [[22.5, 2.25, 2.5, 2.5, 'tramo'], [45, 2, 1.5, 1.5, 'punta']]) {
        const a = ang * Math.PI / 180, dx = side * Math.cos(a), dz = s * Math.sin(a);
        const mx = ex + dx * len / 2, mz = ez + dz * len / 2;
        m.cube([mx - len / 2, 1, mz - dep / 2], [mx + len / 2, 1 + alt, mz + dep / 2], { all: nm === 'punta' ? 'ml_punta' : 'ml_lado', up: 'ml_top', east: 'ml_punta', west: 'ml_punta' }, { angle: -side * s * ang, axis: 'y', origin: [mx, 1, mz] }, n + '_' + nm + (side < 0 ? '_o' : '_e'));
        ex += dx * (len - 0.4); ez += dz * (len - 0.4);
      }
    }
  };
  ml(8, 6, true, 'medialuna_a');
  ml(8, 10.25, false, 'medialuna_b');
  return save('medialuna', m);
}

// ================= TORTA FRITA =================
function tortafrita() {
  const m = new ModelBuilder({ size: 64, density: 2 });
  regionesPlato(m, { w: 14 });
  const A = [[3.5, 3.5, 12, 12], [2.75, 4.5, 3.5, 11], [12, 4, 12.75, 10.5], [4.5, 2.75, 11, 3.5], [4, 12, 10.5, 12.75]];
  const B = [[5, 5.5, 10.5, 10.5], [4.5, 6.5, 5, 9.5], [6, 5, 9.5, 5.5]];
  const pinta = (mask, seed) => (x, y, w, h) => {
    const e = mask(x, y, w, h); if (e < 0) return null;
    let c = ramp(['#e8b868', '#f0c87a', '#e0a858', '#f4d48c'], blob(x, y, 2.5, seed) * 0.8 + rnd(x, y, seed) * 0.25);
    if (blob(x, y, 1.5, seed + 1) > 0.8) c = '#f8e0a8';                // globitos
    if (e === 0) c = '#c8863a';
    if (rnd(x, y, seed + 2) > 0.95) c = '#ffffff';                     // azucar
    return c;
  };
  m.region('tf_a', 22, 22, pinta(rectsMask(A, [2.75, 2.75, 12.75, 12.75]), 181));
  m.region('tf_b', 12, 12, pinta(rectsMask(B, [4.5, 5, 10.5, 10.5]), 185));
  m.region('tf_lado', 26, 2, (x, y) => y === 0 ? '#e8b060' : '#b87a30');
  plato(m, { w: 14 });
  slab(m, A, 1, 1.75, { sides: 'tf_lado', up: 'tf_a', down: null }, 'tortafrita', [2.75, 2.75, 12.75, 12.75]);
  [[5, 5, 2, 1.5], [8.5, 6.5, 2.5, 2], [6, 9, 1.5, 1.5], [9.5, 10, 1.5, 1]].forEach(([x, z, w, d], i) => m.cube([x, 1.75, z], [x + w, 2.25, z + d], { sides: 'tf_lado', up: 'tf_b', down: null }, null, 'globo_' + (i + 1)));
  return save('tortafrita', m);
}

empanada(); fugazzeta(); medialuna(); tortafrita();
