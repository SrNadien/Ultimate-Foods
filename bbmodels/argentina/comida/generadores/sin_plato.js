// Modelos de ITEM de las comidas: igual que el modelo del bloque pero sin el plato, la tabla ni el
// papel de abajo (esos solo aparecen cuando la comida esta colocada). La comida baja hasta apoyar
// en y=0. Los cuencos (guiso, locro) y la provoletera se quedan: son el recipiente de la comida.
//   node bbmodels/argentina/comida/generadores/sin_plato.js
const fs = require('fs');
const path = require('path');

const RAIZ = process.env.UF_REPO || path.resolve(__dirname, '..', '..', '..', '..');
const BLOQUES = path.join(RAIZ, 'src/main/resources/assets/ultimatefoods/models/block/argentina/comida');
const ITEMS = path.join(RAIZ, 'src/main/resources/assets/ultimatefoods/models/item/argentina/comida');

const ES_BASE = name => /^(plato|tabla|papel)(_|$)/.test(name || '');

fs.mkdirSync(ITEMS, { recursive: true });
let quitados = 0;
for (const archivo of fs.readdirSync(BLOQUES).filter(f => f.endsWith('.json') && !/_b[0-9]\.json$/.test(f))) {
  const modelo = JSON.parse(fs.readFileSync(path.join(BLOQUES, archivo), 'utf8'));
  const comida = modelo.elements.filter(e => !ES_BASE(e.name));
  const bajar = comida.length < modelo.elements.length
    ? Math.min(...comida.map(e => e.from[1])) : 0;
  quitados += modelo.elements.length - comida.length;

  const item = { ...modelo, elements: comida.map(e => {
    if (!bajar) return e;
    const copia = JSON.parse(JSON.stringify(e));
    copia.from[1] -= bajar;
    copia.to[1] -= bajar;
    if (copia.rotation && copia.rotation.origin) copia.rotation.origin[1] -= bajar;
    return copia;
  }) };
  fs.writeFileSync(path.join(ITEMS, archivo), JSON.stringify(item, null, 2) + '\n');
}
console.log('modelos de item sin plato listos; elementos quitados:', quitados);
