// Regenera todos los modelos, texturas, .bbmodel y medidas.json de las comidas argentinas.
// Uso (desde cualquier carpeta): node bbmodels/argentina/comida/generadores/todos.js
const fs = require('fs'), path = require('path');
for (const f of fs.readdirSync(__dirname).filter(f => /^gen_.*\.js$/.test(f)).sort()) {
  console.log('== ' + f);
  require(path.join(__dirname, f));
}
