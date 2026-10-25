// Convierte un modelo Java de vanilla (JSON + su PNG) en un proyecto de Blockbench (.bbmodel, formato 5.0,
// "Objeto/Bloque de Java") con la textura embebida. Es el mismo contenido que guarda Blockbench con
// Codecs.project.compile(): se abre directo con Archivo > Abrir.
// Uso: node bbmodel.js <id> [block|item] [carpeta=fisura/comida] [salida=bbmodels/fisura/comida]
const fs = require('fs'), path = require('path'), crypto = require('crypto');
const ROOT = path.resolve(process.env.UF_REPO || path.join(__dirname, '..', '..', '..', '..'));
const ASSETS = path.join(ROOT, 'src', 'main', 'resources', 'assets', 'ultimatefoods');

const uuid = seed => { const h = crypto.createHash('md5').update(seed).digest('hex'); return `${h.slice(0, 8)}-${h.slice(8, 12)}-${h.slice(12, 16)}-${h.slice(16, 20)}-${h.slice(20, 32)}`; };
function pngSize(f) { const b = fs.readFileSync(f); return [b.readUInt32BE(16), b.readUInt32BE(20)]; }

function toBB(id, model, texFiles) {
  const [tw, th] = model.texture_size || [16, 16];
  const texKeys = Object.keys(model.textures).filter(k => k !== 'particle');
  const textures = texKeys.map((k, i) => {
    const ref = model.textures[k], rel = ref.split(':')[1];
    const file = texFiles[k];
    const [w, h] = pngSize(file);
    return {
      name: path.basename(rel) + '.png', path: '', folder: path.dirname(rel), namespace: 'ultimatefoods', id: k, group: '', scope: 0,
      width: w, height: h, uv_width: tw, uv_height: th, particle: model.textures.particle === ref, use_as_default: false, layers_enabled: false,
      sync_to_project: '', file_format: 'png', render_mode: 'default', render_sides: 'auto', wrap_mode: 'limited', pbr_channel: 'color',
      fps: 7, force_translucent: false, frame_time: 1, frame_order_type: 'loop', frame_order: '', frame_interpolate: false,
      visible: true, internal: true, saved: true, uuid: uuid(id + ':tex:' + k),
      relative_path: '', source: 'data:image/png;base64,' + fs.readFileSync(file).toString('base64')
    };
  });
  const elements = model.elements.map((e, i) => {
    // Blockbench guarda las 6 caras de cada cubo; las que no se exportan van con texture null
    const faces = {};
    for (const d of ['north', 'east', 'south', 'west', 'up', 'down']) if (!e.faces[d]) faces[d] = { uv: [0, 0, 0, 0], texture: null };
    for (const [d, f] of Object.entries(e.faces)) {
      const o = { uv: [f.uv[0] * tw / 16, f.uv[1] * th / 16, f.uv[2] * tw / 16, f.uv[3] * th / 16].map(v => +v.toFixed(4)), texture: texKeys.indexOf(f.texture.slice(1)) };
      if (f.rotation) o.rotation = f.rotation;
      if (f.cullface) o.cullface = f.cullface;
      if (f.tintindex !== undefined) o.tintindex = f.tintindex;
      faces[d] = o;
    }
    const rot = [0, 0, 0];
    if (e.rotation) rot['xyz'.indexOf(e.rotation.axis)] = e.rotation.angle;
    const el = {
      name: e.name || 'cube', box_uv: false, render_order: 'default', rescale: !!(e.rotation && e.rotation.rescale), locked: false,
      shade_direction_override: '', light_emission: 0, export: true, scope: 0, allow_mirror_modeling: true,
      from: e.from, to: e.to, autouv: 0, color: i % 8
    };
    if (e.shade === false) el.shade = false;
    if (e.rotation) el.rotation = rot;
    el.origin = e.rotation ? e.rotation.origin : [8, 8, 8];
    el.faces = faces; el.type = 'cube'; el.uuid = uuid(id + ':el:' + i);
    return el;
  });
  return {
    meta: { format_version: '5.0', model_format: 'java_block', box_uv: false },
    name: id, parent: model.parent || '', java_block_version: '26.3', credit: model.credit || '', ambientocclusion: model.ambientocclusion !== false,
    front_gui_light: model.gui_light === 'front', visible_box: [1, 1, 0], variable_placeholders: '', multi_file_ruleset: '', variable_placeholder_buttons: [],
    unhandled_root_fields: {}, resolution: { width: tw, height: th }, elements, groups: [], outliner: elements.map(e => e.uuid), textures,
    display: model.display || {}
  };
}

function exportBB(id, kind = 'block', folder = 'fisura/comida', outDir = path.join(ROOT, 'bbmodels', 'fisura', 'comida')) {
  const mf = path.join(ASSETS, 'models', kind, ...folder.split('/'), id + '.json');
  const model = JSON.parse(fs.readFileSync(mf, 'utf8'));
  const texFiles = {};
  for (const [k, ref] of Object.entries(model.textures)) texFiles[k] = path.join(ASSETS, 'textures', ...ref.split(':')[1].split('/')) + '.png';
  const bb = toBB(id, model, texFiles);
  fs.mkdirSync(outDir, { recursive: true });
  const out = path.join(outDir, id + '.bbmodel');
  fs.writeFileSync(out, JSON.stringify(bb));
  return out;
}

if (require.main === module) {
  const [, , id, kind, folder, out] = process.argv;
  console.log(exportBB(id, kind, folder, out ? path.resolve(ROOT, out) : undefined));
}
module.exports = { exportBB, toBB };
