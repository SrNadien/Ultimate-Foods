package nadiendev.ultimatefoods.blocks;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ItemsAdds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Minerals
 * By NadienDev
 */
public class NadieniteOreBlock {
    
    public static final DeferredRegister<Block> ORE_BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, UltimateFoodsCore.MOD_ID);
    
    public static final DeferredRegister<Item> ORE_BLOCK_ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, UltimateFoodsCore.MOD_ID);

    /**
     // NADIENITE ORE
     */
    public static final DeferredHolder<Block, Block> NADIENITE_ORE = ORE_BLOCKS.register(
            "nadienite_ore",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(3.0F, 3.0F)  // Dureza y resistencia (diamante = 3.0F)
                            .requiresCorrectToolForDrops()  // Requiere herramienta correcta
                            .sound(SoundType.STONE)
            )
    );

    /**
     * DEEPSLATE NADIENITE ORE 
     */
    public static final DeferredHolder<Block, Block> DEEPSLATE_NADIENITE_ORE = ORE_BLOCKS.register(
            "deepslate_nadienite_ore",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(4.5F, 3.0F)  
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.DEEPSLATE)
            )
    );

    // ========== BLOCK ITEMS ==========
    
    public static final DeferredHolder<Item, Item> NADIENITE_ORE_ITEM = ORE_BLOCK_ITEMS.register(
            "nadienite_ore",
            () -> new BlockItem(NADIENITE_ORE.get(), new Item.Properties().rarity(Rarity.RARE))
    );

    public static final DeferredHolder<Item, Item> DEEPSLATE_NADIENITE_ORE_ITEM = ORE_BLOCK_ITEMS.register(
            "deepslate_nadienite_ore",
            () -> new BlockItem(DEEPSLATE_NADIENITE_ORE.get(), new Item.Properties().rarity(Rarity.RARE))
    );


    public static void register(IEventBus eventBus) {
        ORE_BLOCKS.register(eventBus);
        ORE_BLOCK_ITEMS.register(eventBus);
    }
}