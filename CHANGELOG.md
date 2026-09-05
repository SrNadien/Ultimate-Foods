# Changelog

## [2.0.9] — 2026-09-04 — Minecraft 26.1.2

Port to Minecraft 26.1.2. Same content as the 1.21.1 release, rebuilt against the new APIs.

### ✨ Added
- Ex Deorum, AllTheModium, JEI, Jade, GeckoLib and Patchouli support on 26.1.2.
- Equipment assets for the three armor tiers, replacing the old armor layer textures.

### ⚔️ Changed
- Requires **Java 25** and NeoForge `26.1.2.100`.
- The whole item, tool and armor layer was rebuilt: `ArmorItem`, `SwordItem` and `DiggerItem` no longer exist, so every tool and armor piece is now a plain `Item` configured through its properties.
- Food effects moved from `FoodProperties` to the `Consumable` component.
- The Nadienite fluid is rendered through the new fluid model registration instead of a client extension.
- The wizard hat model moved to the folders GeckoLib 5 expects.
- Data generation is split into client and server passes, and every model and recipe was regenerated in the 26.1 format.

### 🐛 Fixes
- Ex Deorum sieve recipes now use the new single-string ingredient format, so they load again.
- Every item ships a client item definition, so nothing renders as a missing model.

### 🗑️ Removed
- Avaritia support: the mod has no 26.1.2 release, so its code is disabled.

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
