# Port de recursos de Artinna Re Fisura (1.21.1 -> 26.1.2)

Los recursos los genera `tools/portar/convertir.js` desde el repo 1.21.1 y los revisa `tools/portar/verificar.js`.
Todos los formatos se compararon contra los jars de la cache de Gradle: cliente de vanilla 26.1.2,
NeoForge 26.1.2.100 (universal), el port de Farmer's Delight 26 (`FarmersDelight-26.1.2-1.3.3-fix5`)
y Patchouli 26.1-94.

## Como se usa

```
node tools/portar/convertir.js            # copia/convierte todo (se puede correr las veces que haga falta)
node tools/portar/verificar.js            # revisa referencias; sale con 1 si hay errores
node tools/portar/verificar.js --java ../mate/src/main/java --java ../comida/src/main/java --java ../homenaje/src/main/java
                                          # igual, pero sumando el Java de los worktrees antes de mergear
```

- `convertir.js --origen <repo>` cambia el repo 1.21.1 (por defecto `E:/modsmios/UltimateFoodsMod`).
- `convertir.js --sounds-generated` agrega lo que falte a `src/generated/.../sounds.json` (ver abajo).
- Lo que escribe queda listado en `tools/portar/generados.json`; en la proxima corrida lo borra y lo
  regenera. Lo que ya existia en el destino (las comidas originales, portadas a mano o por su datagen)
  no se pisa nunca.
- Los items/bloques registrados los saca `verificar.js` del Java (`DeferredItem`, `DeferredBlock`,
  `DeferredHolder<Item|Block|SoundEvent|PoiType|DataComponentType, ...>`). Hasta que esten mergeados
  los modulos hay que pasarle `--java` con los otros worktrees (o la carpeta `fisura` del repo 1.21.1,
  que tiene los mismos ids).

## Que cambio de formato y como quedo

| Recurso | 1.21.1 | 26.1.2 | Que hace el script |
|---|---|---|---|
| Ingredientes de receta | `{"item": ..}`, `{"tag": ..}`, listas de objetos, `{"type": "neoforge:compound"}` | `"id"`, `"#tag"`, lista de ids; custom con `"neoforge:ingredient_type"` | Convierte todo. El compound queda solo cuando mezcla tags (un HolderSet no puede tener varios tags); Farmer's Delight 26 hace exactamente lo mismo. |
| Cuchillo de FD (cutting) | `"tool": [{"type": "farmersdelight:item_ability", ..}, {"tag": "c:tools/knife"}]` | un solo ingrediente | `{"neoforge:ingredient_type": "neoforge:compound", "children": [{"neoforge:ingredient_type": "farmersdelight:item_ability", "action": "knife_dig"}, "#c:tools/knife"]}` (igual que `cutting/cabbage.json` del port de FD) |
| `result` con `count: 1` | se escribia | se omite (como el datagen 26.1.2) | se saca el `count` 1 |
| Receta `ultimatefoods:cacerola` | ingredientes viejos | mismo codec que en `port-comida` (`Ingredient.CODEC` lista, `ItemStackTemplate` en `result` y `container`, `cookingtime`, `experience`) | convertida con la misma regla |
| Arbol (configured_feature) | `dirt_provider` + `force_dirt` | `below_trunk_provider` | igual que el roble de vanilla: tierra salvo sobre `#minecraft:cannot_replace_below_tree_trunk` |
| Sol | `textures/environment/sun.png` | `textures/environment/celestial/sun.png` (atlas `celestials`) | movido (64x64 sirve: es un sprite de atlas) |
| `render_type` en modelos | lo leia NeoForge | no existe: la capa (solid/cutout/translucent) sale de la transparencia de la textura | se borra de los modelos portados (hojas y brote) |
| Modelos usados por blockstates | podian usar cualquier textura | solo texturas del atlas de bloques (`SimpleModelWrapper`: "Rejecting block model ... sprites from outside of supported atlas") | el mate puesto usaba modelos/texturas de `item/`: se hicieron copias de los modelos en `models/block/puesto/...` y de las texturas en `textures/block/puesto/...`, y los `*_sin_bombilla` pasaron a esas texturas |
| Definicion de item | no existia | `assets/ultimatefoods/items/<id>.json` | una por cada `models/item/<id>.json` del origen (62 nuevas) |
| Libro de Patchouli | `"model": "ultimatefoods:guia_buela"` era un modelo | Patchouli 26.1-94 hace `ModelManager.getItemModel(model)` | se crea `items/guia_buela.json` (no es un item registrado; el verificador lo sabe) |
| Tradeos del yerbatero | `VillagerTradesEvent` en `MateTrades.java` | ya no existe el evento: `villager_trade/`, `tags/villager_trade/`, `trade_set/` | generados desde `MateTrades.java` del origen. Trade sets `ultimatefoods:yerbatero/level_1..5` (los que usa `MateTrades` en `port-mate`), `amount` 2 como antes, `reputation_discount` 0.05 |
| Nombre de la profesion | `entity.minecraft.villager.ultimatefoods.yerbatero` | vanilla arma `entity.<ns>.villager.<id>` | se dejan las dos claves en los cuatro lang |

Lo que se comprobo que NO cambio (se copia tal cual): blockstates (`variants` con `model`/`x`/`y`), modelos con
`elements` (rotacion `axis`+`angle` sigue valida, `neoforge_data` en caras y elementos sigue existiendo, `credit`,
`texture_size` y `name` se ignoran), loot tables, tags (`tags/block`, `tags/item`, `point_of_interest_type`,
`{"id": "#..", "required": false}`), advancements de recetas, placed features, biome modifiers de NeoForge
(`neoforge:add_features`, `step: vegetal_decoration`), `neoforge:conditions` con `neoforge:mod_loaded`, el componente
`patchouli:book`, el formato de `book.json`, categorias y entradas de Patchouli, rutas de texturas de efectos y de la
profesion del aldeano.

## Pendiente / distinto

1. **`sounds.json` sin `vendo_carros`.** El `sounds.json` del destino esta en `src/generated` (lo escribe
   `ModSoundProvider`). Poner otro en `src/main/resources` rompe `processResources` (entrada duplicada) y el
   contrato dice no tocar `src/generated`. Arreglo correcto: agregar
   `addSound(SonidosReproducibles.VENDO_CARROS, "vendo_carros");` en `datagen/providers/ModSoundProvider.java`
   (cuando `VENDO_CARROS` este mergeado) y correr `runData`. Alternativa rapida: `node tools/portar/convertir.js --sounds-generated`
   (agrega la entrada al JSON generado; la proxima `runData` sin el cambio en el provider la borra). Hasta entonces el
   verificador avisa que `vendo_carros.ogg` no se usa, y dara error cuando `VENDO_CARROS` este registrado.
2. **Mate puesto:** las texturas del mate/pava/bombilla/verdeflor estan duplicadas en `textures/block/puesto/`. Si se
   cambia una textura de `textures/item/fisura/`, volver a correr `convertir.js`.
3. **Hojas y brote de yerba:** ya no se puede pedir `cutout_mipped`; el juego elige solo por la transparencia de la
   textura. Mirar en el juego que las hojas se vean bien.
4. **Comidas originales:** 38 archivos del origen ya existian en el destino con otro contenido (modelos con
   `"render_type": "cutout"` en vez de `"minecraft:cutout"`, advancements y recetas del datagen 26.1.2). Se dejo el del
   destino. Lo mismo con `models/custom/pancho.json` y `sandwichito.json` (sin uso, con `#0` sin definir: ya venian asi).
5. **`bbmodels/` y `tools/` del repo 1.21.1** (generadores de modelos y texturas) no se copiaron: no son recursos del
   juego. Si se regeneran modelos alla, volver a correr `convertir.js`.
6. **Claves de lang:** se mergearon las 121 claves que agrego Artinna Re Fisura (las que no estaban en el lang previo,
   commit `9fc96fe5~1`) en `en_us` y `es_es`, y se crearon `es_ar` y `es_mx` con esas claves mas las de las comidas
   originales que ya tenia el destino. Las claves criollas mandan las del origen; las demas solo se completan si faltan.
7. **Para probar en el juego (coordinador):** el sol, las hojas, el mate puesto con cada estado (incluida la bombilla
   y la pava), el libro (modelo en la mano, recetas en las paginas), los tradeos del yerbatero por nivel, que el arbol
   aparezca en bosques/junglas/pantanos y la receta de la guia (solo con Patchouli).
