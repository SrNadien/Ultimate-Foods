package nadiendev.ultimatefoods.fisura.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.mate.block.AzucareraBlock;
import nadiendev.ultimatefoods.fisura.mate.block.YerbaLeavesBlock;
import nadiendev.ultimatefoods.fisura.mate.block.YerbaLogBlock;
import nadiendev.ultimatefoods.fisura.mate.block.YerberaBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Bloques del modulo del mate. */
public final class MateBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UltimateFoodsCore.MOD_ID);

    public static final DeferredBlock<YerbaLogBlock> YERBA_MATE_LOG = BLOCKS.registerBlock("yerba_mate_log",
            YerbaLogBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava());

    public static final DeferredBlock<Block> YERBA_MATE_PLANKS = BLOCKS.registerBlock("yerba_mate_planks",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava());

    public static final DeferredBlock<YerbaLeavesBlock> YERBA_MATE_LEAVES = BLOCKS.registerBlock("yerba_mate_leaves",
            YerbaLeavesBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2F)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<SaplingBlock> YERBA_MATE_SAPLING = BLOCKS.registerBlock("yerba_mate_sapling",
            props -> new SaplingBlock(MateWorldgen.TREE_GROWER, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<YerberaBlock> YERBERA = BLOCKS.registerBlock("yerbera",
            YerberaBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.8F)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .ignitedByLava());

    public static final DeferredBlock<AzucareraBlock> AZUCARERA = BLOCKS.registerBlock("azucarera",
            AzucareraBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.8F)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .ignitedByLava());

    private MateBlocks() {
    }

    /** Bloques propios, para los providers de datagen (sirve como lista de "conocidos"). */
    public static Iterable<Block> todos() {
        return BLOCKS.getEntries().stream().<Block>map(h -> h.get()).toList();
    }
}
