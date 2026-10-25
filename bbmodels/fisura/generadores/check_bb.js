// Verifica los .bbmodel: parsean, traen elements y texturas embebidas, y la cantidad de cubos coincide con el JSON.
const fs = require('fs');
const WT = 'E:/modsmios/UltimateFoodsMod/.claude/worktrees/agent-a2b36bc8db13b9653';
const A = `${WT}/src/main/resources/assets/ultimatefoods/models`;
const ids = { mate: 'item', mate_con_yerba: 'item', mate_amargo: 'item', mate_dulce: 'item', mate_de_leche: 'item', mate_con_yuyos: 'item', mate_lavado: 'item', pava: 'item', pava_con_agua: 'item', pava_caliente: 'item', yerba_verdeflor: 'item', yerbera: 'block', parrilla: 'block', cacerola_essen: 'block', cacerola_essen_abierta: 'block', azucarera: 'block' };
let bad = 0;
for (const [id, k] of Object.entries(ids)) {
  const f = `${WT}/bbmodels/fisura/${id}.bbmodel`;
  try {
    const bb = JSON.parse(fs.readFileSync(f, 'utf8'));
    const js = JSON.parse(fs.readFileSync(`${A}/${k}/fisura/${id}.json`, 'utf8'));
    const cubes = (bb.elements || []).filter(e => (e.type || 'cube') === 'cube').length;
    const tex = (bb.textures || []).filter(t => (t.source || '').startsWith('data:image/png;base64,') && t.source.length > 100);
    const ok = cubes === js.elements.length && tex.length >= 1;
    if (!ok) bad++;
    console.log(`${ok ? 'OK ' : 'MAL'} ${id}.bbmodel ${fs.statSync(f).size} B  cubos=${cubes}/${js.elements.length}  texturas_embebidas=${tex.length}  formato=${bb.meta && bb.meta.model_format}`);
  } catch (e) { bad++; console.log(`MAL ${id}: ${e.message}`); }
}
console.log(bad ? `${bad} con problemas` : 'TODOS LOS BBMODEL OK');
process.exit(bad ? 1 : 0);
