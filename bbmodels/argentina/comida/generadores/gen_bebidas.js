// Grupo bebidas: botellas paradas de Fernet Branca (750), Quilmes Clasica (1 L), Stella Artois (330) y Andes Origen (1 L).
// Cada botella son tramos redondeados (disco de 2 escalones) con las etiquetas proyectadas alrededor.
const { ModelBuilder, Img, mix, shade, rnd, ramp, save, disc } = require('./lib');
const { F35, textWidth } = require('./font');

const DENS = 4;
// tiers: [{n, y1, y2, w}] ; paint(tier, dir, x, y, W, H) con dir n/e/s/w/up
function botella(id, tiers, paint) {
  const m = new ModelBuilder({ size: 128, density: DENS });
  for (const t of tiers) {
    const W = t.w * DENS, H = (t.y2 - t.y1) * DENS;
    for (const d of ['n', 'e', 's', 'w']) m.region(`${t.n}_${d}`, W, H, (x, y) => paint(t, d, x, y, W, H));
    m.region(`${t.n}_up`, W, W, (x, y) => paint(t, 'up', x, y, W, W));
  }
  const cx = 8, cz = 8;
  for (const t of tiers) {
    const h = t.w / 2, fx = [cx - h, t.y1, cx + h, t.y2], fz = [cz - h, t.y1, cz + h, t.y2];
    disc(m, cx, cz, t.y1, t.y2, t.w, t.w, {
      north: { r: t.n + '_n', frame: fx }, south: { r: t.n + '_s', frame: fx },
      east: { r: t.n + '_e', frame: fz }, west: { r: t.n + '_w', frame: fz },
      up: t.n + '_up', down: t.n + '_up'
    }, t.n, 2);
  }
  return save(id, m, { displayKind: 'alto' });
}
// vidrio: sombreado vertical con un brillo en las caras de adelante
function vidrio(pal, d, x, y, W, H) {
  let c = pal.mid;
  const k = x / W;
  if (k < 0.12 || k > 0.88) c = pal.dark;
  if ((d === 'n' || d === 'e') && k > 0.2 && k < 0.34) c = pal.hi;
  if ((d === 'n' || d === 'e') && k >= 0.34 && k < 0.42) c = mix(pal.mid, pal.hi, 0.4);
  if (d === 's' || d === 'w') c = shade(c, 0.85);
  if (rnd(x, y, 7) > 0.97) c = mix(c, '#ffffff', 0.25); // gotitas
  return c;
}
const tapa = (col, d, x, y, W, H) => { if (d === 'up') return (x + y) % 3 ? col : shade(col, 0.85); return x % 2 ? col : shade(col, 0.82); };
// filas de la etiqueta: y local (0 arriba) a altura del modelo
const alto = (t, y, H) => t.y2 - (y + 0.5) / DENS;
function texto(img, s, cx, y, col) { const w = textWidth(F35, s); img.text(F35, s, Math.round(cx - w / 2), y, col); }

// ================= QUILMES =================
function quilmes() {
  const AMBAR = { dark: '#4a2208', mid: '#7a3c10', hi: '#c07a30' };
  const CELESTE = '#7cc4ec', AZUL = '#1c3a8a';
  const T = [{ n: 'cuerpo', y1: 0, y2: 8.5, w: 5 }, { n: 'hombro', y1: 8.5, y2: 10, w: 4 }, { n: 'cuello_base', y1: 10, y2: 11.5, w: 3 }, { n: 'cuello', y1: 11.5, y2: 14.5, w: 2 }, { n: 'tapa', y1: 14.5, y2: 15.25, w: 2.5 }];
  // etiqueta de adelante: blanca, franjas celestes, estrellas doradas, "QUILMES" en azul
  const front = new Img(20, 34, '#f6f8fa');
  front.rect(0, 0, 20, 3, CELESTE).rect(0, 31, 20, 3, CELESTE).rect(0, 3, 20, 1, AZUL).rect(0, 30, 20, 1, AZUL);
  for (const sx of [7, 10, 13]) front.set(sx - 0.5, 6, '#d8a830');
  front.rect(2, 9, 16, 1, AZUL);
  texto(front, 'QUIL', 10, 11, AZUL); texto(front, 'MES', 10, 17, AZUL);
  front.rect(2, 23, 16, 1, AZUL);
  for (let x = 1; x < 19; x += 2) front.set(x, 26, CELESTE), front.set(x + 1, 27, CELESTE);
  const lado = new Img(20, 34, '#f6f8fa'); lado.rect(0, 0, 20, 3, CELESTE).rect(0, 31, 20, 3, CELESTE).rect(0, 3, 20, 1, AZUL).rect(0, 30, 20, 1, AZUL);
  for (let y = 8; y < 26; y += 3) lado.rect(3, y, 14, 1, '#b8c8dc');
  return botella('quilmes', T, (t, d, x, y, W, H) => {
    if (t.n === 'tapa') return tapa('#d8b040', d, x, y, W, H);
    if (d === 'up') return t.n === 'cuerpo' ? AMBAR.dark : AMBAR.mid;
    const h = alto(t, y, H);
    if (t.n === 'cuerpo' && h >= 1.25 && h < 7.25 && d !== 's') { const ly = Math.floor((7.25 - h) * DENS / 6 * 34 / 4); const img = d === 'n' ? front : lado; return img.get(Math.min(19, Math.floor(x * 20 / W)), Math.min(33, Math.max(0, Math.round((7.25 - h) / 6 * 34 - 0.5)))); }
    if ((t.n === 'cuello') && h < 14 && d !== 's') return h > 13.4 ? CELESTE : (h < 12 ? CELESTE : '#f6f8fa');
    return vidrio(AMBAR, d, x, y, W, H);
  });
}

// ================= ANDES ORIGEN =================
function andes() {
  const AMBAR = { dark: '#5a2c0a', mid: '#8e4c14', hi: '#d09040' };
  const ORO = '#c8aa58', ORO_D = '#a88a3c', NEGRO = '#1a1a1a';
  const front = new Img(20, 34, ORO);
  front.rect(0, 0, 20, 1, ORO_D).rect(0, 33, 20, 1, ORO_D);
  texto(front, 'ANDES', 10, 3, NEGRO);
  // la "A" grande: triangulo con punta blanca
  for (let r = 0; r < 14; r++) { const y = 10 + r, hw = Math.round(r * 0.55); front.set(10 - hw, y, NEGRO); front.set(9 + hw, y, NEGRO); if (r < 5) for (let x = 10 - hw + 1; x < 9 + hw; x++) front.set(x, y, '#ffffff'); }
  front.rect(7, 19, 6, 1, NEGRO);
  front.rect(3, 27, 14, 1, NEGRO);
  front.rect(5, 30, 10, 1, '#3a3020');
  const lado = new Img(20, 34, ORO); lado.rect(0, 0, 20, 1, ORO_D).rect(0, 33, 20, 1, ORO_D); for (let y = 8; y < 28; y += 4) lado.rect(4, y, 12, 1, ORO_D);
  const T = [{ n: 'cuerpo', y1: 0, y2: 8.5, w: 5 }, { n: 'hombro', y1: 8.5, y2: 10, w: 4 }, { n: 'cuello_base', y1: 10, y2: 11.5, w: 3 }, { n: 'cuello', y1: 11.5, y2: 14.5, w: 2 }, { n: 'tapa', y1: 14.5, y2: 15.25, w: 2.5 }];
  return botella('andes', T, (t, d, x, y, W, H) => {
    if (t.n === 'tapa') return tapa('#c89838', d, x, y, W, H);
    if (d === 'up') return AMBAR.dark;
    const h = alto(t, y, H);
    if (t.n === 'cuerpo' && h >= 1.25 && h < 7.25 && d !== 's') { const img = d === 'n' ? front : lado; return img.get(Math.min(19, Math.floor(x * 20 / W)), Math.min(33, Math.max(0, Math.round((7.25 - h) / 6 * 34 - 0.5)))); }
    if (t.n === 'cuello' && h < 14) return h < 12.25 ? ORO : '#f4f0e8';   // etiqueta del cuello blanca con banda dorada
    if (t.n === 'cuello_base' && h > 10.75) return '#f4f0e8';
    return vidrio(AMBAR, d, x, y, W, H);
  });
}

// ================= STELLA ARTOIS =================
function stella() {
  const VERDE = { dark: '#1e5a14', mid: '#2f8420', hi: '#86d060' };
  const ROJO = '#c8202c', ORO = '#d0a840', BLANCO = '#f8f6f2';
  const front = new Img(20, 26, BLANCO);
  front.set(10, 2, ROJO); front.set(9, 3, ROJO); front.set(10, 3, ROJO); front.set(11, 3, ROJO); front.set(10, 4, ROJO); // estrella
  front.rect(7, 6, 6, 2, ORO);                    // cuerno
  front.rect(2, 10, 16, 8, ROJO); front.rect(1, 11, 1, 6, ORO); front.rect(18, 11, 1, 6, ORO);
  texto(front, 'STEL', 10, 10.5, BLANCO);
  front.rect(4, 16, 12, 1, BLANCO);
  front.rect(5, 21, 10, 1, ORO);
  const lado = new Img(20, 26, BLANCO); lado.rect(0, 0, 20, 1, ORO).rect(0, 25, 20, 1, ORO);
  const T = [{ n: 'cuerpo', y1: 0, y2: 6.5, w: 4.5 }, { n: 'hombro', y1: 6.5, y2: 8, w: 3.5 }, { n: 'cuello', y1: 8, y2: 11.25, w: 2 }, { n: 'tapa', y1: 11.25, y2: 12, w: 2.5 }];
  return botella('stella_artois', T, (t, d, x, y, W, H) => {
    if (t.n === 'tapa') return tapa(ORO, d, x, y, W, H);
    if (d === 'up') return VERDE.dark;
    const h = alto(t, y, H);
    if (t.n === 'cuerpo' && h >= 1.25 && h < 5.75 && d !== 's') { const img = d === 'n' ? front : lado; return img.get(Math.min(19, Math.floor(x * 20 / W)), Math.min(25, Math.max(0, Math.round((5.75 - h) / 4.5 * 26 - 0.5)))); }
    if (t.n === 'cuello') { if (h > 10.25) return ORO; if (h > 8.5) return d === 'n' && Math.abs(x - W / 2 + 0.5) < 1 && h > 9 && h < 10 ? ROJO : BLANCO; }
    return vidrio(VERDE, d, x, y, W, H);
  });
}

// ================= FERNET BRANCA =================
function fernet() {
  const OSCURO = { dark: '#100804', mid: '#22120a', hi: '#5a3a22' };
  const DURAZNO = '#f2c690', DUR_D = '#d89a5a', MARRON = '#3a1a0a';
  const front = new Img(20, 28, DURAZNO);
  front.rect(0, 0, 20, 1, DUR_D).rect(0, 27, 20, 1, DUR_D);
  texto(front, 'FERN', 10, 3, MARRON);
  front.rect(2, 9, 16, 1, '#a82a1a');
  for (let y = 12; y < 26; y += 2) front.rect(3 + (y % 4 ? 1 : 0), y, 13, 1, mix(DURAZNO, MARRON, 0.45));
  const lado = new Img(20, 28, DURAZNO); lado.rect(0, 0, 20, 1, DUR_D).rect(0, 27, 20, 1, DUR_D); for (let y = 4; y < 26; y += 2) lado.rect(4, y, 12, 1, mix(DURAZNO, MARRON, 0.3));
  // emblema redondo arriba de la etiqueta: aguila oscura sobre el globo verde
  const emb = new Img(20, 12, null);
  emb.ellipse(10, 6, 5.5, 5.5, DURAZNO).ellipse(10, 6, 4.5, 4.5, '#5aa078').ellipse(10, 7.5, 3.5, 2.5, '#2c6a8a');
  emb.rect(6, 3, 8, 1, MARRON).rect(8, 2, 4, 3, MARRON);
  const T = [{ n: 'cuerpo', y1: 0, y2: 9, w: 5 }, { n: 'hombro', y1: 9, y2: 10.5, w: 4 }, { n: 'cuello', y1: 10.5, y2: 14, w: 2.5 }, { n: 'tapa', y1: 14, y2: 15.5, w: 2.75 }];
  return botella('fernet_branca', T, (t, d, x, y, W, H) => {
    if (t.n === 'tapa') return d === 'up' ? '#2a2a2a' : (x % 2 ? '#1a1a1a' : '#3a3a3a');
    if (d === 'up') return OSCURO.dark;
    const h = alto(t, y, H);
    if (t.n === 'cuerpo' && d !== 's') {
      if (h >= 1 && h < 5.5) { const img = d === 'n' ? front : lado; return img.get(Math.min(19, Math.floor(x * 20 / W)), Math.min(27, Math.max(0, Math.round((5.5 - h) / 4.5 * 28 - 0.5)))); }
      if (d === 'n' && h >= 5.75 && h < 8.75) { const c = emb.get(Math.min(19, Math.floor(x * 20 / W)), Math.min(11, Math.max(0, Math.round((8.75 - h) / 3 * 12 - 0.5)))); if (c) return c; }
    }
    if (t.n === 'cuello' && h > 11 && h < 13.25 && d !== 's') return h > 13 || h < 11.25 ? DUR_D : DURAZNO;
    return vidrio(OSCURO, d, x, y, W, H);
  });
}

fernet(); quilmes(); stella(); andes();
