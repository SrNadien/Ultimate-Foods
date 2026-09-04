package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class ModLootModifierProvider extends GlobalLootModifierProvider {

    public ModLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, UltimateFoodsCore.MOD_ID);
    }

    @Override
    protected void start() {

        add("nadienite_chest_loot", new NadieniteChestLootModifier(new net.minecraft.world.level.storage.loot.predicates.LootItemCondition[0]));
    }
}
