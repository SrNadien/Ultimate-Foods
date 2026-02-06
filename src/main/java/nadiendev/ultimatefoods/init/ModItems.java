package nadiendev.ultimatefoods.init;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Items Registry
 * By NadienDev
 */
public class ModItems {
    
    public static final DeferredRegister.Items ITEMS = 
        DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);

    // ========== BLOCKITEMS - Items que representan bloques ==========
    
    // bloque de acero
    public static final DeferredItem<BlockItem> STEEL_BLOCK_ITEM = ITEMS.register("steel_block",
            () -> new BlockItem(ModBlocks.STEEL_BLOCK.get(), new Item.Properties()));

    // bloque de nadienite
    public static final DeferredItem<BlockItem> NADIENITE_BLOCK_ITEM = ITEMS.register("nadienite_block",
            () -> new BlockItem(ModBlocks.NADIENITE_BLOCK.get(), new Item.Properties()));

    // mineral de nadienite (piedra normal)
    public static final DeferredItem<BlockItem> NADIENITE_ORE_ITEM = ITEMS.register("nadienite_ore",
            () -> new BlockItem(ModBlocks.NADIENITE_ORE.get(), new Item.Properties()));

    //  mineral de nadienite (deepslate)
    public static final DeferredItem<BlockItem> DEEPSLATE_NADIENITE_ORE_ITEM = ITEMS.register("deepslate_nadienite_ore",
            () -> new BlockItem(ModBlocks.DEEPSLATE_NADIENITE_ORE.get(), new Item.Properties()));

    // sexy_xD
    public static final DeferredItem<BlockItem> SEXY_XD_ITEM = ITEMS.register("sexy_xd",
            () -> new BlockItem(ModBlocks.SEXY_XD.get(), new Item.Properties()));
}