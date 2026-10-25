// Modelos del asado colocado a medida que se lo van comiendo (asado_b1..b3). Cada uno es el modelo
// completo sin las piezas que ya se comieron. El cuarto bocado saca el bloque.
//   node bbmodels/fisura/comida/generadores/bocados.js
const fs = require('fs');
const path = require('path');

const RAIZ = process.env.UF_REPO || path.resolve(__dirname, '..', '..', '..', '..');
const DIR = path.join(RAIZ, 'src/main/resources/assets/ultimatefoods/models/block/fisura/comida');

// piezas (por nombre de elemento) que ya no estan despues de cada bocado, acumulativas
const SACADO = [
  [/^chorizo/],                       // 1: el chorizo
  [/^vacio_3$/, /^chimichurri$/],     // 2: la tercera feta y el chimichurri
  [/^vacio_2$/],                      // 3: la segunda feta; queda la tira y una feta
];

const base = JSON.parse(fs.readFileSync(path.join(DIR, 'asado.json'), 'utf8'));
let acumulado = [];
SACADO.forEach((patrones, i) => {
  acumulado = acumulado.concat(patrones);
  const modelo = { ...base, elements: base.elements.filter(e => !acumulado.some(p => p.test(e.name || ''))) };
  fs.writeFileSync(path.join(DIR, `asado_b${i + 1}.json`), JSON.stringify(modelo, null, 2) + '\n');
  console.log(`asado_b${i + 1}: ${modelo.elements.length} de ${base.elements.length} elementos`);
});
