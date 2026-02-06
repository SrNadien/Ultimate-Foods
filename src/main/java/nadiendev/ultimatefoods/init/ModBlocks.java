package nadiendev.ultimatefoods.init;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Blocks Registry
 * By NadienDev
 */
public class ModBlocks {
    
    public static final DeferredRegister.Blocks BLOCKS = 
        DeferredRegister.createBlocks(UltimateFoodsCore.MOD_ID);

    // ========== BLOQUES METÁLICOS ==========
    

    public static final DeferredBlock<Block> STEEL_BLOCK = BLOCKS.register("steel_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    // NADIENITE_BLOCK: Bloque de nadienite decorativo/almacenamiento
    // Drop: Al picarlo cae el bloque de nadienite (nadienite_block)
    public static final DeferredBlock<Block> NADIENITE_BLOCK = BLOCKS.register("nadienite_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    // ========== MINERALES ==========
    
    // NADIENITE_ORE
    public static final DeferredBlock<Block> NADIENITE_ORE = BLOCKS.register("nadienite_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    // DEEPSLATE_NADIENITE_ORE
    public static final DeferredBlock<Block> DEEPSLATE_NADIENITE_ORE = BLOCKS.register("deepslate_nadienite_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)));

    // ========== BLOQUES PERSONALIZADOS ==========
    
    // SEXY_XD
    public static final DeferredBlock<Block> SEXY_XD = BLOCKS.register("sexy_xd",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.STONE)));
}