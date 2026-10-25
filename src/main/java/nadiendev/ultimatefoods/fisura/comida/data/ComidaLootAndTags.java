package nadiendev.ultimatefoods.fisura.comida.data;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.cacerola.CacerolaRegistry;
import nadiendev.ultimatefoods.fisura.parrilla.ParrillaRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/** Loot y tags del modulo (por ahora solo la parrilla). */
public final class ComidaLootAndTags {

    private ComidaLootAndTags() {
    }

    public static class Loot extends LootTableProvider {

        public Loot(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, Set.of(), List.of(new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)), registries);
        }
    }

    private static class BlockLoot extends BlockLootSubProvider {

        BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            dropSelf(ParrillaRegistry.PARRILLA.get());
            dropSelf(CacerolaRegistry.CACEROLA_ESSEN.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(ParrillaRegistry.PARRILLA.get(), CacerolaRegistry.CACEROLA_ESSEN.get());
        }
    }

    public static class Tags extends BlockTagsProvider {

        public Tags(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                    @Nullable ExistingFileHelper existingFileHelper) {
            super(output, registries, UltimateFoodsCore.MOD_ID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Comida: Block Tags";
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ParrillaRegistry.PARRILLA.get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(ParrillaRegistry.PARRILLA.get());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CacerolaRegistry.CACEROLA_ESSEN.get());

            // Lo que calienta la Cacerola Essen (los que tienen LIT tienen que estar prendidos).
            tag(CacerolaRegistry.HEAT_SOURCES)
                    .add(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE, Blocks.FURNACE, Blocks.SMOKER, Blocks.BLAST_FURNACE,
                            Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.LAVA, Blocks.MAGMA_BLOCK)
                    .add(ParrillaRegistry.PARRILLA.get())
                    .addOptionalTag(ResourceLocation.fromNamespaceAndPath("farmersdelight", "heat_sources"));
        }
    }
}
