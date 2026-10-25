// Genera el libro guia de Patchouli ("Recetas De La Buela") en espanol (es_ar, es_es, es_mx) e ingles.
// Patchouli es opcional: sin el mod, estos archivos no se leen.
//   node tools/guia/gen_guia.js
const fs = require('fs');
const path = require('path');

const RAIZ = process.env.UF_REPO || path.resolve(__dirname, '..', '..');
const ID = 'guia_buela';
const DATA = path.join(RAIZ, 'src/main/resources/data/ultimatefoods/patchouli_books', ID);
const ASSETS = path.join(RAIZ, 'src/main/resources/assets/ultimatefoods/patchouli_books', ID);
const M = 'ultimatefoods:';

// ---- helpers para escribir el contenido: cada texto es [es, en] ---------------------------------
const t = (es, en, title) => ({ type: 'patchouli:text', text: [es, en], ...(title ? { title } : {}) });
const spot = (item, es, en, title) => ({ type: 'patchouli:spotlight', item: M + item, text: [es, en], ...(title ? { title } : {}) });
const craft = (recipe, es, en) => ({ type: 'patchouli:crafting', recipe: M + (recipe.startsWith('/') ? recipe.slice(1) : 'fisura/' + recipe), ...(es ? { text: [es, en] } : {}) });
const BR = '$(br2)';

const CATEGORIAS = {
  inicio: { name: ['Para empezar', 'Getting started'], description: ['Como se come y como se coloca todo lo del mod.', 'How everything in the mod is eaten and placed.'], icon: M + 'yerba_verdeflor' },
  mate: { name: ['El mate', 'The mate'], description: ['De la yerba al mate cebado, con todos sus gustos.', 'From the yerba to a brewed mate, in every flavor.'], icon: M + 'mate_amargo' },
  comidas: { name: ['A la mesa', 'At the table'], description: ['Parrilla, cacerola, milanesas, dulces y bebidas.', 'Grill, pot, milanesas, sweets and drinks.'], icon: M + 'asado' },
  objetos_mate: { name: ['Objetos: el mate', 'Items: the mate'], description: ['Cada item y bloque del mate, uno por uno.', 'Every mate item and block, one by one.'], icon: M + 'mate' },
  objetos_mesa: { name: ['Objetos: la mesa', 'Items: the table'], description: ['Cada comida, bebida y bloque de la mesa fisura.', 'Every food, drink and block of the Criollo table.'], icon: M + 'milanesa_de_carne' },
  ultimate_foods: { name: ['Ultimate Foods', 'Ultimate Foods'], description: ['Las comidas originales del mod.', 'The mod\'s original foods.'], icon: M + 'monster' },
  secretos: { name: ['Secretos', 'Secrets'], description: ['Pistas de lo que esconde el mod.', 'Hints about what the mod hides.'], icon: M + 'la_copa' },
};

const ENTRADAS = [
  // ------------------------------------------------------------------ para empezar
  ['inicio', 'bienvenida', ['Bienvenido, che', 'Welcome, che'], 'yerba_verdeflor', 0, [
    t('Esta guia reune todo lo criollo de Ultimate Foods: el mate y su ritual, la parrilla, la cacerola, las milanesas, los dulces y las bebidas.' + BR + 'Todo vive en la pestana "Artinna Re Fisura" del creativo.',
      'This guide gathers everything Criollo in Ultimate Foods: the mate ritual, the grill, the pot, milanesas, sweets and drinks.' + BR + 'It all lives in the "Artinna Re Fisura" creative tab.'),
  ]],
  ['inicio', 'colocar_y_comer', ['Colocar y comer', 'Placing and eating'], 'asado', 0, [
    t('Las comidas se pueden dejar apoyadas como bloque: Shift + click derecho sobre un bloque firme con la comida en la mano.' + BR + 'Colocadas llevan su plato o tabla; en la mano y el inventario se ven sin el.',
      'Foods can be set down as a block: Shift + right click on a solid block while holding the food.' + BR + 'Placed, they show their plate or board; in hand and inventory they do not.'),
    t('Click derecho sobre la comida colocada: te la comes (con su nutricion y sus efectos).' + BR + 'Shift + click derecho con las manos vacias: la levantas.' + BR + 'El asado se come en cuatro bocados: los efectos llegan con el ultimo y, una vez empezado, ya no se puede levantar.',
      'Right click a placed food to eat it (nutrition and effects included).' + BR + 'Shift + right click with empty hands picks it up.' + BR + 'The asado is eaten in four bites: the effects arrive with the last one, and once started it can no longer be picked up.'),
  ]],
  ['inicio', 'efectos', ['Efectos de las comidas', 'Food effects'], 'mega_dibu', 1, [
    t('Panza llena: mas vida maxima, pero mas lento.' + BR + 'Fuego de parrilla: pegas mas fuerte y cuesta empujarte.' + BR + 'Dulzura: velocidad al caminar y al pegar.' + BR + 'Buena onda: suerte.',
      'Full Belly: more max health, but slower.' + BR + 'Grill Fire: you hit harder and are harder to push.' + BR + 'Sugar Rush: faster walking and attacks.' + BR + 'Good Vibes: luck.'),
    t('Curda: mas lento al caminar y al pegar (lo dejan las bebidas).' + BR + 'Empacho: lento y sin fuerza (te pasaste de comida).' + BR + 'Atajapenales: mas alcance y las caidas duelen menos.' + BR + 'Los efectos del mate, en el capitulo "El mate".',
      'Tipsy: slower walking and attacks (drinks leave it).' + BR + 'Overstuffed: slow and weak (you ate too much).' + BR + 'Penalty Saver: more reach and softer falls.' + BR + 'Mate effects are in the "The mate" chapter.'),
  ]],

  // ------------------------------------------------------------------ el mate
  ['mate', 'yerba', ['La yerba mate', 'The yerba mate'], 'yerba_mate_sapling', 0, [
    t('El arbol de yerba mate crece de un brote. Las hojas suelen soltar una hoja de yerba (mas con Fortuna) y a veces un brote nuevo.' + BR + 'Con tijeras o Toque de seda te llevas el bloque de hojas.',
      'The yerba mate tree grows from a sapling. Its leaves often drop a yerba leaf (more with Fortune) and sometimes a new sapling.' + BR + 'Use shears or Silk Touch to take the leaf block.'),
    t('Hoja de yerba: se seca en una fogata, un horno o un ahumador y queda "seca".' + BR + 'Tres hojas secas dan dos de yerba mate molida.',
      'Yerba leaf: dry it in a campfire, furnace or smoker.' + BR + 'Three dried leaves make two ground yerba mate.'),
    craft('yerba_mate_molida'),
  ]],
  ['mate', 'verdeflor_yerbera', ['Verdeflor y la yerbera', 'Verdeflor and the yerbera'], 'yerbera', 0, [
    t('Cuatro de yerba mate y un papel arman un paquete Verdeflor. Se puede apoyar en el suelo con Shift + click derecho.',
      'Four ground yerba mate and a paper make a Verdeflor package. Set it down with Shift + right click.'),
    craft('yerba_verdeflor'),
    t('La yerbera guarda yerba: un paquete le suma 16 porciones y un punado de yerba suelta, 4 (tope de 64).' + BR + 'Click derecho con un mate vacio en la mano: te lo devuelve con yerba.',
      'The yerbera stores yerba: a package adds 16 portions and loose yerba adds 4 (max 64).' + BR + 'Right click with an empty mate to get it back filled.'),
    craft('yerbera'),
  ]],
  ['mate', 'azucarera', ['La azucarera', 'The sugar pouch'], 'azucarera', 0, [
    t('Igual que la yerbera, pero guarda azucar (8 porciones por terron). Click derecho con un mate con yerba en la mano: lo vuelve dulce.',
      'Like the yerbera, but it stores sugar (8 portions per sugar). Right click with a yerba-filled mate to make it sweet.'),
    craft('azucarera'),
  ]],
  ['mate', 'mate_pava_bombilla', ['Mate, pava y bombilla', 'Mate, kettle and bombilla'], 'pava', 0, [
    t('El mate se hace con un cuenco y cuero. La bombilla, con tres pepitas de hierro. La pava, con hierro y un palo.',
      'The mate is made from a bowl and leather. The bombilla, from three iron nuggets. The kettle, from iron and a stick.'),
    craft('mate'),
    craft('bombilla'),
    craft('pava'),
  ]],
  ['mate', 'pava_caliente', ['Calentar el agua', 'Heating the water'], 'pava_caliente', 0, [
    t('1. Click derecho a una fuente de agua (o un caldero) con la pava vacia: se llena.' + BR + '2. Click derecho a una fogata, un horno o un fuego encendido con la pava con agua: queda caliente, justo para el mate.' + BR + 'Una pava caliente rinde 8 cebadas.',
      '1. Right click water (or a cauldron) with the empty kettle: it fills.' + BR + '2. Right click a lit campfire, furnace or fire with the full kettle: it gets hot, just right for mate.' + BR + 'A hot kettle serves 8 brews.'),
  ]],
  ['mate', 'cebar', ['Cebar el mate', 'Brewing the mate'], 'mate_con_yerba', 0, [
    t('Con un mate con yerba en una mano, la pava caliente en la otra y una bombilla en el inventario: click derecho para cebar.' + BR + 'Cada cebada da 4 sorbos. Con cada una la yerba se lava un poco: el efecto dura menos.',
      'With a yerba-filled mate in one hand, the hot kettle in the other and a bombilla in your inventory: right click to brew.' + BR + 'Each brew gives 4 sips. The yerba washes out a bit each time: the effect lasts less.'),
    t('Despues de 4 cebadas la yerba queda lavada del todo y el mate vuelve vacio (a veces, con yerba usada).' + BR + 'Un mate lavado todavia se puede cebar una vez mas.',
      'After 4 brews the yerba is fully washed and the mate comes back empty (sometimes with used yerba).' + BR + 'A washed mate can still be brewed once more.'),
  ]],
  ['mate', 'gustos', ['Los gustos', 'The flavors'], 'mate_dulce', 0, [
    t('Mate amargo: Tranquilidad. Las criaturas hostiles cercanas se ponen lentas.' + BR + 'Mate dulce: Mandale mecha. Mucha velocidad, empujas a lo que esta cerca y al terminar quedas un rato lento.',
      'Bitter mate: Tranquility. Nearby hostile mobs get slowed.' + BR + 'Sweet mate: Out of my way! Lots of speed, you push nearby mobs and you are slow for a moment afterwards.'),
    t('Mate de leche: Desarrollo. Las crias cercanas crecen al instante.' + BR + 'Mate con yuyos: Bonanza. Suerte extra, algo de experiencia cada tanto y 50 % mas de experiencia por lo que matas.',
      'Milk mate: Development. Nearby babies grow up instantly.' + BR + 'Yuyos mate: Prosperity. Extra luck, some experience now and then and 50 % more experience from kills.'),
    craft('mate_dulce'),
    craft('mate_de_leche'),
    craft('mate_con_yuyos'),
  ]],
  ['mate', 'mate_puesto', ['El mate sobre la mesa', 'The mate on the table'], 'mate', 0, [
    t('Mates, pavas, paquetes de yerba y bombillas se apoyan con Shift + click derecho y quedan inclinados hacia donde miras.' + BR + 'Click derecho con la mano vacia: lo levantas (con la bombilla, que sale aparte).',
      'Mates, kettles, yerba packages and bombillas are set down with Shift + right click and lean toward where you look.' + BR + 'Right click with an empty hand to pick it up (the bombilla comes out separately).'),
    t('Servilo ahi mismo:' + BR + 'Yerbera o yerba suelta sobre un mate vacio: le pone yerba.' + BR + 'Azucar, balde de leche o yuyos: le dan gusto, antes de cebar.' + BR + 'Bombilla: se clava aparte y ya no sale sola.' + BR + 'Pava caliente: lo ceba (hace falta la bombilla clavada).',
      'Serve it right there:' + BR + 'Yerbera or loose yerba on an empty mate: fills it.' + BR + 'Sugar, milk bucket or yuyos: flavor it, before brewing.' + BR + 'Bombilla: it is stuck in separately and does not come out by itself.' + BR + 'Hot kettle: brews it (the bombilla must be in).'),
    t('Deslavar: con un pincel (Brush) sobre un mate lavado sacas la yerba usada y queda vacio; la bombilla vuelve a tu inventario. El pincel pierde 1 de durabilidad.',
      'Unwashing: use a Brush on a washed mate to clear out the used yerba; it ends up empty and the bombilla returns to your inventory. The brush loses 1 durability.'),
  ]],
  ['mate', 'pasar_mate', ['Pasar el mate', 'Passing the mate'], 'mate_amargo', 0, [
    t('Click derecho con un mate cebado sobre otro jugador o un aldeano: le pasas el mate y le toca el efecto.',
      'Right click another player or a villager with a brewed mate to pass it over; they get the effect.'),
  ]],
  ['mate', 'yerbatero', ['El yerbatero', 'The yerbatero'], 'yerba_mate', 0, [
    t('Un aldeano con profesion propia. Te compra hojas, yerba y yuyos, y te vende el mate, la bombilla, la pava, paquetes Verdeflor, brotes del arbol, la yerbera y la azucarera.' + BR + 'Su puesto de trabajo es la yerbera.',
      'A villager with its own profession. It buys leaves, yerba and yuyos, and sells the mate, bombilla, kettle, Verdeflor packages, tree saplings, the yerbera and the sugar pouch.' + BR + 'Its job site is the yerbera.'),
  ]],

  // ------------------------------------------------------------------ a la mesa
  ['comidas', 'parrilla', ['La parrilla', 'The grill'], 'parrilla', 0, [
    t('Se le pone combustible, se enciende con yesquero o carga de fuego y cocina seis cosas a la vez, como una fogata.' + BR + 'Ojo: caminar encima prendida quema.',
      'Fuel it, light it with flint and steel or a fire charge, and it cooks six things at once like a campfire.' + BR + 'Careful: walking on it while lit burns you.'),
    craft('parrilla'),
  ]],
  ['comidas', 'asado', ['Asado y chorizo', 'Asado and chorizo'], 'asado', 0, [
    t('Asado crudo y chorizo se asan en la parrilla, la hoguera, el horno o el ahumador.' + BR + 'Asado: 10 de hambre, Fuego de parrilla (2 min) y Nutricion.' + BR + 'Chorizo asado: 6 de hambre y Fuego de parrilla.',
      'Raw asado and chorizo cook on the grill, a campfire, a furnace or a smoker.' + BR + 'Asado: 10 hunger, Grill Fire (2 min) and Nourishment.' + BR + 'Grilled chorizo: 6 hunger and Grill Fire.'),
    craft('asado_crudo'),
    craft('chorizo'),
  ]],
  ['comidas', 'choripan', ['Choripan y provoleta', 'Choripan and provoleta'], 'choripan', 0, [
    t('Choripan: chorizo asado, pan y tomate. Fuego de parrilla y Absorcion.' + BR + 'Provoleta: queso a la parrilla. Fuego de parrilla y Resistencia.',
      'Choripan: grilled chorizo, bread and tomato. Grill Fire and Absorption.' + BR + 'Provoleta: grilled cheese. Grill Fire and Resistance.'),
    craft('choripan'),
  ]],
  ['comidas', 'cacerola', ['La Cacerola Essen', 'The Essen pot'], 'cacerola_essen', 0, [
    t('Se apoya sobre una fuente de calor (fogata, horno o parrilla prendidos), se le tiran los ingredientes, se espera y se sirve con un cuenco (3 tablones en V).' + BR + 'Mano vacia: devuelve el ultimo ingrediente.',
      'Set it on a heat source (lit campfire, furnace or grill), toss in the ingredients, wait and serve with a bowl.' + BR + 'Empty hand: returns the last ingredient.'),
    craft('cacerola_essen'),
    t('Guiso: papa, zanahoria, tomate, cebolla, carne vacuna cruda y arroz, fideos o masa (6 ingredientes, 45 segundos al fuego). Panza llena, Comodidad y un toque de Regeneracion.' + BR + 'Locro: zapallo, trigo, papa, cebolla, chorizo y carne. Panza llena, Nutricion y Resistencia.',
      'Guiso stew: potato, carrot, tomato, onion, raw beef and rice, pasta or dough (6 ingredients, 45 seconds on the fire). Full Belly, Comfort and a touch of Regeneration.' + BR + 'Locro: pumpkin, wheat, potato, onion, chorizo and beef. Full Belly, Nourishment and Resistance.'),
  ]],
  ['comidas', 'milanesas', ['Milanesas', 'Milanesas'], 'milanesa_de_carne', 0, [
    t('Pan rallado + carne (o pollo) = milanesa cruda; se cocina como cualquier carne.' + BR + 'De carne: Fuerza. De pollo: Velocidad.' + BR + 'Sanguche de milanesa: milanesa, pan, tomate y verdura. Fuerza, Resistencia y Panza llena.',
      'Bread crumbs + beef (or chicken) = raw milanesa; cook it like any meat.' + BR + 'Beef: Strength. Chicken: Speed.' + BR + 'Milanesa sandwich: milanesa, bread, tomato and greens. Strength, Resistance and Full Belly.'),
    craft('milanesa_cruda'),
    craft('sanguche_de_milanesa'),
  ]],
  ['comidas', 'noquis_y_mas', ['Empanadas y mas', 'Empanadas and more'], 'noquis', 0, [
    t('Noquis: papa, masa y tomate en la olla de Farmer\'s Delight. Suerte y Panza llena.' + BR + 'Empanada: Buena onda (se hornea).' + BR + 'Fugazzeta: cebolla, queso y masa. Buena onda y Panza llena.',
      'Noquis: potato, dough and tomato in a Farmer\'s Delight pot. Luck and Full Belly.' + BR + 'Empanada: Good Vibes (baked).' + BR + 'Fugazzeta: onion, cheese and dough. Good Vibes and Full Belly.'),
    t('Empanada cruda: masa, carne vacuna cruda, cebolla y un huevo (en la mesa de crafteo, salen 3). Despues se cocinan como cualquier carne: en la hoguera, el horno, el ahumador o la parrilla.',
      'Raw empanada: dough, raw beef, onion and an egg (crafting table, makes 3). Then cook them like any meat: campfire, furnace, smoker or grill.'),
    craft('empanada_cruda'),
    craft('fugazzeta'),
  ]],
  ['comidas', 'dulces', ['Dulces y panaderia', 'Sweets and bakery'], 'alfajor', 0, [
    t('Dulce de leche: leche y azucar en la olla.' + BR + 'Flan con dulce de leche: leche, huevo, azucar y dulce de leche en la olla (salen 2).' + BR + 'Alfajor: Dulzura. Medialuna, panqueque y torta frita.' + BR + 'Pitusas y pepas: las galletitas de la merienda.',
      'Dulce de leche: milk and sugar in the pot.' + BR + 'Dulce de leche flan: milk, egg, sugar and dulce de leche in the pot (makes 2).' + BR + 'Alfajor: Sugar Rush. Medialuna, pancake and fried cake.' + BR + 'Pitusas and pepas: the afternoon cookies.'),
    craft('alfajor'),
    craft('pitusas'),
    craft('pepas'),
  ]],
  ['comidas', 'mega_dibu', ['La Mega Dibu', 'The Mega Dibu'], 'mega_dibu', 0, [
    t('Doble carne, doble cheddar, doble panceta, cebolla crispy y salsa barbacoa. Llena todo el hambre y da los mejores efectos del mod... y un buen Empacho de postre.',
      'Double patty, double cheddar, double bacon, crispy onion and barbecue sauce. It fills your hunger and gives the best effects in the mod... plus a hefty Overstuffed for dessert.'),
    craft('mega_dibu'),
  ]],
  ['comidas', 'bebidas', ['Bebidas', 'Drinks'], 'fernet_branca', 0, [
    t('Fernet Branca, Quilmes, Stella Artois y Andes. Dan efectos buenos (Resistencia, Buena onda, Salto) y se pagan con Curda y un poco de confusion.' + BR + 'Se craftean con una botella de vidrio, devuelven la botella y se pueden dejar apoyadas en una mesa.',
      'Fernet Branca, Quilmes, Stella Artois and Andes. They give good effects (Resistance, Good Vibes, Jump) and cost you Tipsy and a little nausea.' + BR + 'They are crafted with a glass bottle, return the bottle and can be set down on a table.'),
    craft('quilmes'),
    craft('stella_artois'),
    craft('andes'),
    craft('fernet_branca'),
  ]],

  // ------------------------------------------------------------------ secretos
  ['secretos', 'la_copa', ['La Copa', 'The Cup'], 'la_copa', 0, [
    t('Un trofeo dorado para decorar, en honor a Lionel Andres Messi. Seis lingotes de oro con forma de copa.',
      'A golden trophy for decoration, in honor of Lionel Andres Messi. Six gold ingots in a cup shape.'),
    craft('/la_copa'),
  ]],
  ['secretos', 'messi', ['Easter egg: Messi', 'Easter egg: Messi'], 'minecraft:name_tag', 0, [
    t('Ponele una etiqueta con el nombre "Messi" a un aldeano (tambien valen "Lionel Messi", "Lionel Andres Messi", "Leo Messi" y "La Pulga", con o sin mayusculas y tildes).' + BR + 'El aldeano se transforma en un homenaje: salen fuegos artificiales, suenan festejos y un mensaje de campeon del mundo.',
      'Rename a villager with a name tag to "Messi" ("Lionel Messi", "Lionel Andres Messi", "Leo Messi" and "La Pulga" also work, in any case and with or without accents).' + BR + 'The villager becomes a tribute: fireworks, cheering sounds and a world champion message.'),
    t('Desde ese momento el aldeano te vende La Copa por 32 esmeraldas (una sola vez por aldeano).' + BR + 'La Copa tambien se puede craftear con oro, pero asi tiene su historia.',
      'From then on that villager sells you The Cup for 32 emeralds (once per villager).' + BR + 'The Cup can also be crafted from gold, but this way it has its story.'),
  ]],
  ['secretos', 'carpincho', ['Easter egg: el carpincho', 'Easter egg: the capybara'], 'minecraft:pig_spawn_egg', 0, [
    t('Ponele una etiqueta con el nombre "carpincho" a un cerdo (con o sin mayusculas ni tildes).' + BR + 'El cerdo cambia de piel y pasa a ser un carpincho marron y gordito. Es solo la apariencia: sigue siendo un cerdo.' + BR + 'Si le sacas el nombre, vuelve a ser cerdo.',
      'Rename a pig to "carpincho" with a name tag (any case, with or without accents).' + BR + 'The pig changes skin into a chubby brown capybara. It is only looks: it is still a pig.' + BR + 'Remove the name and it is a pig again.'),
  ]],
  ['secretos', 'comerciante', ['Easter egg: el comerciante', 'Easter egg: the trader'], 'minecraft:wandering_trader_spawn_egg', 0, [
    t('El comerciante errante deja de murmurar y lanza su pregon: "Compramos carros y camionetas viejas para desarmar".' + BR + 'El pregon dura unos 13 segundos; mientras suena no empieza otro encima. Todos sus otros sonidos (comprar, rechazar, tomar la pocion) quedan como siempre.',
      'The wandering trader stops muttering and shouts its sales pitch: "We buy old cars and trucks for scrap".' + BR + 'It lasts about 13 seconds; no other one starts over it. All its other sounds (buying, refusing, drinking the potion) stay as usual.'),
  ]],
  ['secretos', 'el_sol', ['Easter egg: el sol', 'Easter egg: the sun'], 'minecraft:clock', 0, [
    t('Mira el cielo de dia: el sol de Minecraft es un Sol de Mayo, con rayos rectos y ondulados, y una cara muy conocida en el centro.' + BR + 'Es un cambio de textura, asi que no hace falta nada para verlo: sale solo.',
      'Look at the sky by day: Minecraft\'s sun is a Sun of May, with straight and wavy rays and a very familiar face in the middle.' + BR + 'It is a texture change, so nothing is needed to see it: it just shows up.'),
  ]],
];

// ---- una ficha por cada item y bloque (tools/guia/objetos.js) ----
const generado = path.join(RAIZ, 'src/generated/resources');
const idioma = l => JSON.parse(fs.readFileSync(path.join(generado, 'assets/ultimatefoods/lang', l + '.json'), 'utf8'));
const LANG = [idioma('es_es'), idioma('en_us')];
const nombreDe = (id, i) => LANG[i]['item.ultimatefoods.' + id] || LANG[i]['block.ultimatefoods.' + id] || id;
function recetaCrafteo(rec) {
  const rel = rec.startsWith('/') ? rec.slice(1) : 'fisura/' + rec;
  const archivo = path.join(generado, 'data/ultimatefoods/recipe', rel + '.json');
  if (!fs.existsSync(archivo)) return null;
  return JSON.parse(fs.readFileSync(archivo, 'utf8')).type.startsWith('minecraft:crafting') ? rec : null;
}
for (const [cat, id, tipo, texto, rec, icono] of require('./objetos')) {
  const pages = [];
  const item = icono || id;
  pages.push({ type: 'patchouli:spotlight', item: M + item, text: texto });
  const r = recetaCrafteo(rec === undefined ? id : (rec || ''));
  if (rec !== null && r) pages.push({ type: 'patchouli:crafting', recipe: M + (r.startsWith('/') ? r.slice(1) : 'fisura/' + r) });
  ENTRADAS.push([cat, 'obj_' + id, [nombreDe(id, 0), nombreDe(id, 1)], M + item, 0, pages]);
}

// ---- escritura -------------------------------------------------------------------------------------
const LOCALES = { es_ar: 0, es_es: 0, es_mx: 0, en_us: 1 };
const elegir = (v, i) => (Array.isArray(v) ? v[i] : v);
const escribir = (ruta, obj) => { fs.mkdirSync(path.dirname(ruta), { recursive: true }); fs.writeFileSync(ruta, JSON.stringify(obj, null, 2) + '\n'); };

fs.rmSync(ASSETS, { recursive: true, force: true });
escribir(path.join(DATA, 'book.json'), {
  name: 'ultimatefoods.guia.nombre',
  landing_text: 'ultimatefoods.guia.portada',
  version: 1,
  i18n: true,
  use_resource_pack: true,
  creative_tab: 'ultimatefoods:fisura_tab',
  show_progress: false,
  // modelo 3D y GUI propios (bbmodels/fisura/generadores/gen_libro.js y tools/guia/gen_gui.js)
  book_texture: 'ultimatefoods:textures/gui/guia_libro.png',
  model: 'ultimatefoods:guia_buela',
  text_color: '1B2A35',
  header_color: '0B5F86',
  nameplate_color: 'FFFFFF',
  link_color: '0B5F86',
  link_hover_color: '8800EE',
});

for (const [locale, i] of Object.entries(LOCALES)) {
  const base = path.join(ASSETS, locale);
  for (const [id, c] of Object.entries(CATEGORIAS)) {
    escribir(path.join(base, 'categories', id + '.json'), {
      name: elegir(c.name, i), description: elegir(c.description, i), icon: c.icon, sortnum: Object.keys(CATEGORIAS).indexOf(id),
    });
  }
  ENTRADAS.forEach(([cat, id, name, icon, sort, pages], n) => {
    escribir(path.join(base, 'entries', cat, id + '.json'), {
      name: elegir(name, i),
      category: M + cat,
      icon: icon.includes(':') ? icon : M + icon,
      sortnum: n,
      pages: pages.map(p => ({ ...p, ...(p.text ? { text: elegir(p.text, i) } : {}), ...(p.title ? { title: elegir(p.title, i) } : {}) })),
    });
  });
}
console.log(`libro "${ID}": ${ENTRADAS.length} entradas, ${Object.keys(CATEGORIAS).length} categorias, ${Object.keys(LOCALES).length} idiomas`);
