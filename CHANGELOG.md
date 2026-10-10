# Changelog

## [2.0.10] — 2026-10-10 — Minecraft 26.1.2

Ultimate Foods is now only the foods and drinks. The equipment and materials moved to **UltimateArmoury: Tools And Weapons**, and the utility items and music discs moved to **Ultimate Tweaks**.

Requires **Java 25** and NeoForge `26.1.2.100`.

### ⚠️ Breaking
- Everything that is not a food left this mod, together with its item and block ids. Worlds made with 2.0.9 or earlier lose those items.
- **Farmer's Delight** is now required. On 26.1.2 use the community port *Farmer's Delight 26 Neo Ver*. It is also a dependency of the raw burger meat recipe.
- AppleSkin stays required.

### ✨ Added
- The raw burger meat recipe uses any **Farmer's Delight knife** instead of the Netherite Dagger, which now belongs to UltimateArmoury.

### ⚔️ Changed
- The mod is reduced to the 14 foods, their 6 eating sounds, the Poop effect and the burger drops of the Sniffer, Armadillo, Villager and Warden.
- GeckoLib, Patchouli, Ex Deorum and AllTheModium are no longer dependencies.

### 🗑️ Removed
- Materials, ores, armor, tools, weapons, hammers, meshes, compressed blocks and the Nadienite fluid: now in UltimateArmoury.
- The 21 utility items, the 2 music discs and their sounds: now in Ultimate Tweaks.

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
