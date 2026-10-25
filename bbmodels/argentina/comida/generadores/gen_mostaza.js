// Mega Dibu (Mostaza): pan rustico enharinado, doble medallon de carne, doble cheddar que chorrea, doble panceta
// crocante, cebolla crispy y salsa barbacoa. Sobre el papel de la bandeja.
const { ModelBuilder, mix, shade, rnd, blob, ramp, save, disc, discMask } = require('./lib');

function megaDibu() {
  const m = new ModelBuilder({ size: 128, density: 2 });
  const W = 11, mk = discMask(W, W), mk2 = discMask(12, 12);
  // papel
  m.region('papel', 28, 28, (x, y) => { let c = (x + y) % 8 < 1 ? '#e8d8b0' : '#f4ead0'; if (x < 2 || y < 2 || x > 25 || y > 25) c = '#d8382a'; return c; });
  m.region('papel_lado', 28, 1, () => '#c8302a');
  // pan
  const PAN = ['#c8843c', '#d4944a', '#dca45a', '#e4b46a'];
  m.region('pan_base_lado', 24, 3, (x, y) => y === 2 ? '#a86a2a' : ramp(PAN, 0.4 + rnd(x, y, 191) * 0.3));
  m.region('pan_corte', 22, 22, (x, y) => rnd(x, y, 192) > 0.8 ? '#f4e0b8' : '#ecd4a4');
  m.region('pan_lado', 24, 4, (x, y) => { let c = ramp(PAN, 0.6 - y * 0.12 + rnd(x, y, 193) * 0.25); if (rnd(x, y, 194) > 0.93) c = '#f2ead8'; return c; });
  m.region('pan_top', 22, 22, (x, y, w, h) => {
    const e = mk(x, y, w, h);
    let c = ramp(PAN, 0.75 + blob(x, y, 3, 195) * 0.3 - 0.15);
    if (blob(x, y, 2.5, 196) > 0.74) c = mix(c, '#f6f0e4', 0.55);                 // harina
    if (Math.abs(x - y * 0.4 - 6) < 0.8 && y > 3 && y < 18) c = '#b87436';          // grieta del pan
    if (e === 0) c = shade(c, 0.9);
    return c;
  });
  // carne
  m.region('carne_lado', 24, 3, (x, y) => { let c = ramp(['#4a2414', '#5c2e18', '#6a3820'], rnd(x, y, 197)); if (rnd(x, y, 198) > 0.9) c = '#2a140a'; return c; });
  m.region('carne_top', 24, 24, (x, y) => (x + (y >> 1)) % 6 === 0 ? '#2a140a' : ramp(['#4a2414', '#5c2e18', '#6a3820'], rnd(x, y, 199)));
  // cheddar
  const CHED = '#f6a81c', CHED_L = '#ffc84a';
  m.region('ched_lado', 24, 1, () => CHED);
  m.region('ched_top', 24, 24, (x, y) => rnd(x, y, 200) > 0.85 ? CHED_L : CHED);
  m.region('ched_gota', 4, 4, (x, y) => y === 0 ? CHED_L : CHED);
  // panceta
  m.region('bacon', 16, 4, (x, y) => { const k = (x + y * 2) % 5; return k < 2 ? '#8a2a1a' : (k < 3 ? '#e8b098' : (k < 4 ? '#a83a24' : '#5a1a10')); });
  // cebolla crispy con barbacoa
  m.region('crispy', 22, 22, (x, y, w, h) => { if (mk(x, y, w, h) < 0) return null; const r = rnd(x, y, 201); if (blob(x, y, 2, 202) > 0.6) return r > 0.5 ? '#4a1a0e' : '#5e2412'; return r > 0.6 ? '#c88a3c' : (r > 0.3 ? '#a86a28' : '#e0a850'); });
  m.region('crispy_lado', 24, 2, (x, y) => rnd(x, y, 203) > 0.5 ? '#a86a28' : '#4a1a0e');

  const cx = 8, cz = 8;
  m.cube([1.5, 0, 1.5], [14.5, 0.25, 14.5], { up: { r: 'papel', full: true }, down: { r: 'papel', full: true }, sides: 'papel_lado' }, null, 'papel');
  let y = 0.25;
  const capa = (h, w, faces, name, n = 3) => { disc(m, cx, cz, y, y + h, w, w, faces, name, n); y += h; };
  capa(1.5, W, { sides: 'pan_base_lado', up: 'pan_corte', down: 'pan_corte' }, 'pan_abajo');
  for (const k of [1, 2]) {
    capa(1.25, 11.5, { sides: 'carne_lado', up: 'carne_top', down: 'carne_top' }, 'medallon_' + k);
    capa(0.5, 12, { sides: 'ched_lado', up: 'ched_top', down: 'ched_top' }, 'cheddar_' + k);
    // gotas de cheddar que caen por los costados
    for (const [gx, gz, gw, gd] of [[cx - 1, cz - 6.1, 1.5, 0.5], [cx + 5.6, cz + 1, 0.5, 1.5], [cx + 2.5, cz - 5.4, 1, 0.5]])
      m.cube([gx, y - 1.5, gz], [gx + gw, y - 0.5, gz + gd], { all: 'ched_gota' }, null, 'gota_' + k);
    // tiras de panceta que sobresalen
    const yb = y;
    m.cube([cx - 7.25, yb, cz - 2.25], [cx + 3, yb + 0.5, cz - 0.5], { all: 'bacon' }, { angle: 22.5, axis: 'y', origin: [cx, yb, cz] }, 'panceta_' + k + 'a');
    m.cube([cx - 2.5, yb, cz + 0.75], [cx + 7.25, yb + 0.5, cz + 2.5], { all: 'bacon' }, { angle: -22.5, axis: 'y', origin: [cx, yb, cz] }, 'panceta_' + k + 'b');
    y += 0.5;
  }
  capa(0.75, 10.5, { sides: 'crispy_lado', up: 'crispy', down: null }, 'cebolla_bbq');
  capa(2, W, { sides: 'pan_lado', up: 'pan_top', down: 'pan_corte' }, 'pan_arriba');
  capa(1, 9, { sides: { r: 'pan_lado', oy: 0 }, up: 'pan_top', down: null }, 'pan_lomo');
  capa(0.5, 6, { sides: { r: 'pan_lado', oy: 0 }, up: 'pan_top', down: null }, 'pan_cima', 2);
  return save('mega_dibu', m, { displayKind: 'bloque' });
}
megaDibu();
