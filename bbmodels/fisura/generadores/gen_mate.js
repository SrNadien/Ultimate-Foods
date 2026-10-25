// Mate imperial de calabaza (foto del usuario): tripode con bolitas doradas, virola con grecas y aro floral, bombilla de alpaca.
const L = require('./lib');
const { ModelBuilder, hex, mix, shade, rnd, pick, writeModel, writeTexture, rotPoint } = L;

const DISPLAY = {
  thirdperson_righthand: { rotation: [75, 45, 0], translation: [0, 0.5, 1.5], scale: [0.45, 0.45, 0.45] },
  thirdperson_lefthand: { rotation: [75, 45, 0], translation: [0, 0.5, 1.5], scale: [0.45, 0.45, 0.45] },
  firstperson_righthand: { rotation: [8, -30, 0], translation: [0, 4.5, -1], scale: [0.42, 0.42, 0.42] },
  firstperson_lefthand: { rotation: [8, -30, 0], translation: [0, 4.5, -1], scale: [0.42, 0.42, 0.42] },
  ground: { translation: [0, 2, 0], scale: [0.5, 0.5, 0.5] },
  gui: { rotation: [25, -30, 0], translation: [-0.5, -1.25, 0], scale: [0.85, 0.85, 0.85] },
  head: { translation: [0, 14, 0] },
  fixed: { rotation: [0, 180, 0], translation: [0, -1, 0], scale: [0.85, 0.85, 0.85] }
};

// ---------- paletas de contenido ----------
const CONT = {
  seca: { yerba: ['#7c8c3a', '#6a7a30', '#8e9c48', '#5d6c28', '#a3ab55', '#c9c07a', '#4a5820'], liquido: null },
  amargo: { yerba: ['#4f5f22', '#43521c', '#5c6c2a', '#3a4718', '#6a7a30', '#8a8a48'], liquido: ['#6e8c34', '#7a9a3e', '#668430', '#a6c06a', '#c8da9c'] },
  dulce: { yerba: ['#56662a', '#4a5a22', '#64742e', '#41501c', '#76863a', '#9a9a58'], liquido: ['#88a24c', '#94ae56', '#7e9844', '#c0d28c', '#f4f6e4'] },
  leche: { yerba: ['#5a6a2c', '#4c5c24', '#687832', '#44521e', '#7c8a42'], liquido: ['#d8c8a0', '#cbb88e', '#e4d8b8', '#c2b080', '#b8b27e'] },
  yuyos: { yerba: ['#4f5f22', '#43521c', '#5c6c2a', '#3a4718', '#6a7a30', '#8a8a48'], liquido: ['#6e8c34', '#7a9a3e', '#668430', '#a6c06a', '#c8da9c'] },
  lavado: { yerba: ['#9a9a6c', '#8c8c60', '#a8a678', '#7e7e56', '#b4b086', '#c4c09a'], liquido: ['#a4aa78', '#9aa06e', '#b0b486', '#8e9466'] }
};

function build(id, estado) {
  const m = new ModelBuilder({ size: 64, density: 2 });
  // ---------- regiones / pintura ----------
  m.region('gourd', 16, 10, (x, y, w, h) => {
    let c = rnd(x, y, 1) > 0.75 ? '#21140d' : '#25170f';
    if (x === 4 && y > 0 && y < h - 2) c = '#3e2a1e'; // brillo
    if (x === 5 && y > 1 && y < h - 3) c = '#33221a';
    if (x === 4 && y === 2) c = '#5a3e2c';
    if (rnd(x, y, 3) > 0.97) c = '#33221a';
    if (y >= h - 1) c = shade(c, 0.8);
    return c;
  });
  m.region('gourdDark', 8, 8, (x, y) => pick(['#1d110b', '#22140d', '#1a0f09'], x, y, 4));
  m.region('interior', 16, 16, (x, y, w, h) => {
    const e = Math.min(x, y, w - 1 - x, h - 1 - y);
    let c = pick(['#1a0f0a', '#160c07', '#1e120c'], x, y, 5);
    if (e <= 2) c = '#3a271b'; else if (e === 3) c = '#2a1a12';
    return c;
  });
  // friso de grecas escalonadas (laton con relieve plateado)
  m.region('greca', 16, 4, (x, y) => {
    if (y === 0) return '#e4e4e0';
    if (y === 3) return '#7a7a74';
    const p = x % 6, fg = '#d2b45a', bg = (x + y) % 2 ? '#bcbcb4' : '#aaaaa2';
    if (y === 1) return (p === 0 || p === 1 || p === 2) ? fg : bg;
    return (p === 2 || p === 3 || p === 4) ? fg : (p === 5 ? '#6a5a30' : bg);
  });
  // aro superior con grabado floral (lados)
  m.region('ringSide', 18, 3, (x, y) => {
    if (y === 0) return '#f2f2f2';
    if (y === 2) return (x % 4 === 3) ? '#7c7c7c' : '#a8a8a8';
    return (x % 4 === 1) ? '#8a8a8a' : (x % 4 === 2 ? '#e0e0e0' : '#c4c4c4');
  });
  m.region('ringTopH', 18, 3, (x, y) => {
    if (y === 2) return '#9c9c9c';
    if (y === 0) return (x % 3 === 0) ? '#b0b0b0' : '#ececec';
    return (x % 3 === 1) ? '#8e8e8e' : '#d6d6d6';
  });
  m.region('ringTopV', 3, 12, (x, y) => {
    if (x === 2) return '#9c9c9c';
    if (x === 0) return (y % 3 === 0) ? '#b0b0b0' : '#ececec';
    return (y % 3 === 1) ? '#8e8e8e' : '#d6d6d6';
  });
  m.region('ringInner', 12, 3, (x, y) => y === 0 ? '#bdbdbd' : (y === 1 ? '#9a9a9a' : '#6e6e6e'));
  m.region('silver', 16, 2, (x, y) => y === 0 ? '#a4a4a4' : '#7c7c7c');
  m.region('gold', 3, 3, (x, y) => (x + y === 0) ? '#fff0a0' : (x + y <= 2 ? '#d8b040' : '#8a6418'));
  // bombilla
  m.region('tube', 2, 16, (x, y) => x === 0 ? (y % 5 === 2 ? '#ffffff' : '#e2e2e2') : '#a2a2a2');
  m.region('bombDeco', 3, 7, (x, y) => x === 2 ? ((y % 2) ? '#8a8a84' : '#a08a4a') : ((x + y) % 3 === 0 ? '#d8c27a' : (x === 0 ? '#f0f0ec' : '#c6c6c0')));
  m.region('knot', 3, 3, (x, y) => y === 1 ? '#f6e6a8' : (x === 0 ? '#e8d48a' : '#a88a3a'));

  const C = CONT[estado];
  if (C) {
    m.region('yerba', 12, 12, (x, y) => {
      let c = pick(C.yerba, x, y, 11);
      if (rnd(x, y, 12) > 0.9) c = C.yerba[C.yerba.length - 1];
      return c;
    });
    m.region('yerbaSide', 12, 3, (x, y) => shade(pick(C.yerba, x, y, 13), y === 0 ? 1 : 0.8));
    if (C.liquido) m.region('liquido', 12, 12, (x, y) => {
      let c = pick(C.liquido.slice(0, 3), x, y, 14);
      const r = rnd(x, y, 15);
      if (r > 0.86) c = C.liquido[3]; // burbujas
      if (r > 0.96 && C.liquido[4]) c = C.liquido[4];
      return c;
    });
    if (estado === 'yuyos') m.region('hoja', 4, 3, (x, y) => (y === 1 ? (x === 0 ? '#2f5a18' : '#4a8a28') : (x === 3 ? null : '#62a034')));
  }

  // ---------- geometria ----------
  const G = 'gourd';
  // patas tripode con bolitas doradas
  const legs = [[8, 11.4], [4.9, 5.8], [11.1, 5.8]];
  for (const [lx, lz] of legs) {
    m.cube([lx - 0.55, 0.9, lz - 0.55], [lx + 0.55, 2.6, lz + 0.55], { all: G, up: null }, null, 'pata');
    m.cube([lx - 0.9, 2.6, lz - 0.9], [lx + 0.9, 4.3, lz + 0.9], { all: G, up: null }, null, 'pata_alta');
    m.cube([lx - 0.6, 0, lz - 0.6], [lx + 0.6, 1.2, lz + 0.6], { all: 'gold' }, null, 'bolita');
  }
  // aro fino de la base (une las patas)
  m.cube([5.6, 1.0, 5.6], [10.4, 1.3, 10.4], { all: 'silver' }, null, 'aro_base');
  // calabaza: cuenco redondeado, mas ancho arriba
  m.cube([6.5, 2, 6.5], [9.5, 2.8, 9.5], { all: G }, null, 'calabaza_fondo');
  m.cube([5.5, 2.8, 6], [10.5, 3.8, 10], { all: G, down: 'gourdDark' }, null, 'calabaza_baja_x');
  m.cube([6, 2.8, 5.5], [10, 3.8, 10.5], { all: G, down: 'gourdDark' }, null, 'calabaza_baja_z');
  m.cube([4.5, 3.8, 5.5], [11.5, 5.3, 10.5], { all: G, down: 'gourdDark' }, null, 'calabaza_media_x');
  m.cube([5.5, 3.8, 4.5], [10.5, 5.3, 11.5], { all: G, down: 'gourdDark' }, null, 'calabaza_media_z');
  m.cube([4, 5.3, 5], [12, 8, 11], { all: G, down: 'gourdDark' }, null, 'calabaza_panza_x');
  m.cube([5, 5.3, 4], [11, 8, 12], { all: G, down: 'gourdDark' }, null, 'calabaza_panza_z');
  // virola: friso de grecas
  m.cube([4, 8, 4], [12, 10, 12], { sides: 'greca', up: 'interior', down: 'gourdDark' }, null, 'virola_grecas');
  // aro floral (4 tiras alrededor de la boca)
  const y0 = 10, y1 = 11.5;
  m.cube([3.5, y0, 3.5], [12.5, y1, 5], { sides: 'ringSide', north: 'ringSide', south: 'ringInner', up: 'ringTopH', down: 'silver' }, null, 'aro_n');
  m.cube([3.5, y0, 11], [12.5, y1, 12.5], { sides: 'ringSide', north: 'ringInner', up: 'ringTopH', down: 'silver' }, null, 'aro_s');
  m.cube([3.5, y0, 5], [5, y1, 11], { sides: 'ringSide', east: 'ringInner', north: null, south: null, up: 'ringTopV', down: 'silver' }, null, 'aro_o');
  m.cube([11, y0, 5], [12.5, y1, 11], { sides: 'ringSide', west: 'ringInner', north: null, south: null, up: 'ringTopV', down: 'silver' }, null, 'aro_e');

  // ---------- contenido ----------
  if (estado === 'seca') {
    // montañita de yerba corrida hacia el oeste, hueco del lado de la bombilla
    m.cube([5, 10, 5], [9, 11, 11], { all: 'yerba', sides: 'yerbaSide', down: null }, null, 'yerba');
    m.cube([5, 11, 5], [7.5, 11.8, 11], { all: 'yerba', sides: 'yerbaSide', down: null }, null, 'yerba_monte');
    m.cube([5, 11.8, 6], [6.5, 12.3, 10], { all: 'yerba', sides: 'yerbaSide', down: null }, null, 'yerba_cima');
    m.cube([9, 10, 5], [11, 10.3, 11], { all: 'yerba', sides: 'yerbaSide', down: null }, null, 'yerba_fondo');
  } else if (C) {
    m.cube([5, 10, 5], [11, 10.9, 11], { all: 'liquido', sides: 'yerbaSide', down: null }, null, 'cebado');
    m.cube([5, 10.9, 5], [8, 11.5, 11], { all: 'yerba', sides: 'yerbaSide', down: null }, null, 'yerba_mojada');
    m.cube([5, 11.5, 6], [6.2, 11.9, 10], { all: 'yerba', sides: 'yerbaSide', down: null }, null, 'yerba_cima');
    if (estado === 'yuyos') {
      const hojas = [[6, 11.9, 7, 22.5], [7.6, 11.5, 8.6, -22.5], [6.4, 11.9, 9.2, -45]];
      for (const [hx, hy, hz, a] of hojas) m.cube([hx, hy, hz], [hx + 2, hy + 0.25, hz + 1.5], { all: 'hoja' }, { angle: a, axis: 'y', origin: [hx + 1, hy, hz + 0.75] }, 'yuyo');
    }
  }

  // ---------- bombilla ----------
  if (estado !== null) {
    const o = [9.6, 10, 8.4], rot = { angle: -22.5, axis: 'z', origin: o };
    const t = 0.4; // medio grosor del tubo
    m.cube([o[0] - t, 9.6, o[2] - t], [o[0] + t, 12.2, o[2] + t], { all: 'tube' }, rot, 'bombilla_tubo');
    m.cube([o[0] - 0.6, 12.2, o[2] - 0.6], [o[0] + 0.6, 15.2, o[2] + 0.6], { all: 'bombDeco', ends: 'knot' }, rot, 'bombilla_virola');
    m.cube([o[0] - 0.75, 15.2, o[2] - 0.75], [o[0] + 0.75, 15.9, o[2] + 0.75], { all: 'knot' }, rot, 'bombilla_anillo');
    m.cube([o[0] - t, 15.9, o[2] - t], [o[0] + t, 17.6, o[2] + t], { all: 'tube' }, rot, 'bombilla_cuello');
    // pico aplastado y curvo: arranca en la punta del tubo, mas inclinado
    const tip = rotPoint([o[0], 17.6, o[2]], 'z', -22.5, o);
    m.cube([tip[0] - 0.35, tip[1] - 0.35, tip[2] - 0.65], [tip[0] + 0.35, tip[1] + 2.3, tip[2] + 0.65], { all: 'tube' }, { angle: -45, axis: 'z', origin: tip }, 'bombilla_pico');
  }
  const tex = `ultimatefoods:item/fisura/${id}`;
  const json = m.toJSON({ texture: tex, display: DISPLAY });
  writeTexture('item', id, m.paint());
  return writeModel('item', id, json);
}

const out = [];
out.push(build('mate', null));
out.push(build('mate_con_yerba', 'seca'));
out.push(build('mate_amargo', 'amargo'));
out.push(build('mate_dulce', 'dulce'));
out.push(build('mate_de_leche', 'leche'));
out.push(build('mate_con_yuyos', 'yuyos'));
out.push(build('mate_lavado', 'lavado'));
console.log(out.join('\n'));
