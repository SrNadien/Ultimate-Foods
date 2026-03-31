package nadiendev.ultimatefoods.datagen;

import nadiendev.ultimatefoods.datagen.loot.UltimateFoodsBlockLootTables;
import nadiendev.ultimatefoods.datagen.loot.UltimateFoodsMobLootTables;
import nadiendev.ultimatefoods.datagen.loot.UltimateFoodsChestLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(UltimateFoodsBlockLootTables::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(UltimateFoodsMobLootTables::new,  LootContextParamSets.ENTITY),
                new SubProviderEntry(UltimateFoodsChestLootTables::new, LootContextParamSets.CHEST)
        ), registries);
    }
}