package nadiendev.ultimatefoods.argentina.mate.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.mate.MateBlocks;
import nadiendev.ultimatefoods.argentina.mate.MateItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/** Tags de bloques e items del arbol de yerba mate y la yerbera. */
public final class MateTags {

    private MateTags() {
    }

    public static class Bloques extends BlockTagsProvider {
        public Bloques(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper helper) {
            super(output, lookup, UltimateFoodsCore.MOD_ID, helper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            Block log = MateBlocks.YERBA_MATE_LOG.get();
            tag(BlockTags.LOGS).add(log);
            tag(BlockTags.LOGS_THAT_BURN).add(log);
            tag(BlockTags.MINEABLE_WITH_AXE).add(log);
            tag(BlockTags.LEAVES).add(MateBlocks.YERBA_MATE_LEAVES.get());
            tag(BlockTags.MINEABLE_WITH_HOE).add(MateBlocks.YERBA_MATE_LEAVES.get());
            tag(BlockTags.SAPLINGS).add(MateBlocks.YERBA_MATE_SAPLING.get());
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
                     CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper helper) {
            super(output, lookup, blockTags, UltimateFoodsCore.MOD_ID, helper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(ItemTags.LOGS).add(MateItems.YERBA_MATE_LOG.get());
            tag(ItemTags.LOGS_THAT_BURN).add(MateItems.YERBA_MATE_LOG.get());
            tag(ItemTags.LEAVES).add(MateItems.YERBA_MATE_LEAVES.get());
            tag(ItemTags.SAPLINGS).add(MateItems.YERBA_MATE_SAPLING.get());
        }
    }
}
