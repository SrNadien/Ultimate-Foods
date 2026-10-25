// Mates con yerba SIN la bombilla clavada, para el mate puesto: la bombilla se pone aparte.
// Los modelos de item del mate (con bombilla) salen del datagen; aca se les quitan los cubos
// girados (los de la bombilla, que va en diagonal) y se guardan como modelos de bloque.
// Correr despues de runData:   node bbmodels/fisura/sin_bombilla.js
const fs = require('fs');
const path = require('path');

const RAIZ = process.env.UF_REPO || path.resolve(__dirname, '..', '..');
const ORIGEN = path.join(RAIZ, 'src/generated/resources/assets/ultimatefoods/models/item/fisura');
const DESTINO = path.join(RAIZ, 'src/main/resources/assets/ultimatefoods/models/block/fisura/mate');
const MATES = ['mate_con_yerba', 'mate_lavado', 'mate_amargo', 'mate_dulce', 'mate_de_leche', 'mate_con_yuyos'];

fs.mkdirSync(DESTINO, { recursive: true });
for (const id of MATES) {
  const modelo = JSON.parse(fs.readFileSync(path.join(ORIGEN, id + '.json'), 'utf8'));
  const sin = { ...modelo, elements: modelo.elements.filter(e => !(e.rotation && e.rotation.axis === 'z')) };
  fs.writeFileSync(path.join(DESTINO, id + '_sin_bombilla.json'), JSON.stringify(sin, null, 2) + '\n');
  console.log(`${id}: ${sin.elements.length} de ${modelo.elements.length} cubos`);
}
