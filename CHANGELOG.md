# Changelog

## [2.0.10] — 2026-09-19 — Minecraft 1.21.1

### ✨ Added
- A distinct 3D helmet per tier, rendered through GeckoLib: a **crown** for Nadienite, a **detective fedora** for Mushashite and a **pointed wizard hat** for JoanFoite. The three tiers used to share a single wizard hat.
- Inventory icons for the twelve armor pieces.

### ⚔️ Changed
- New item textures for the swords, axes, pickaxes, shovels, hoes, hammers and ingots of the three tiers.
- The three ingots now share one base with a per-tier hue, so they read as a family.
- Deepslate ores redrawn to match the vanilla deepslate ore silhouette.
- New armor layer textures, one theme per tier: Roman king in deep red and gold, spy in black and gunmetal, mage in violet and gold.
- Only the helmet is 3D. Chestplate, leggings and boots use Minecraft's flat armor layers, which are painted onto the player's body and therefore cannot drift away from it while walking.
- `WizardHatItem` and `WizardHatRenderer` replaced by `HatItem` and `HatRenderer`, which choose the model from the tier.


### 🗑️ Removed
- `green_ingot.png` 
- The Mushashite and Nadienite `wizard_hat` armor textures, replaced by the crown and fedora atlases.

## [2.0.9] — 2026-09-04 — Minecraft 1.21.1

### ✨ Added
- Three full armor and tool tiers: **Mushashite** → **JoanFoite** → **Nadienite**, each with Gorro, Remera, Gayumbos, Medias, sword, axe, pickaxe, shovel and hoe.
- Stepped set bonuses with per-piece tooltips: Luck and extra health on Mushashite; Water Breathing, Speed II and Piglin/Creeper immunity on JoanFoite; Night Vision, Resistance III with creative flight, Jump III, Speed III plus Enderman and Phantom perks on Nadienite.
- Upgrade recipes between tiers using Corazón De Los Caídos, Corazón De Los Fantasmas and Corazón De La Elite.
- Ores, deepslate ores, raw ores, ingots, nuggets and storage blocks for the three tiers, plus steel and netherite nuggets, an Ender Pearl block and a new Nether Star block texture.
- 3D wizard hat helmet rendered through GeckoLib.
- Patchouli guide book and JEI information pages for every item, tool and integration.
- Ex Deorum integration: 7 sieve meshes, 7 hammers and their compressed versions, sieve and compressed sieve drops, and crucible heat sources. Meshes and hammers follow the real tier order — steel above iron, the three custom tiers after netherite — and the whole chain is obtainable in skyblock.
- AllTheModium tiers layered on top of the Nadienite mesh and hammer. The unobtainium hammer is unbreakable.
- Compressed blocks 1x–9x for steel, mushashite, joanfoite, nadienite, nether star and ender pearl, with a block-count tooltip.
- `ultimatefoods-common.toml` config to disable each integration individually.

### ⚔️ Changed
- The old Nadienite armor was removed and replaced by the new three-tier progression with renamed pieces.
- The custom flight effect was dropped in favour of `neoforge:creative_flight`.
- Avaritia support is disabled.
- Data providers moved to `datagen/providers` and every registration class to `registry/`.

### 🐛 Fixes
- The Nadienite fluid is replaceable again, so blocks can be placed on top of it and it can be scooped up like water or lava.

## [2.0.8] — 2026-03-31 — Minecraft 1.21.1

### 🐛 Fixes
- Fixed a crash when opening loot chests with Apotheosis installed.
