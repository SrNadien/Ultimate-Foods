package nadiendev.ultimatefoods.argentina.parrilla;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registro de la parrilla: bloque, item y tipo de block entity. */
public final class ParrillaRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UltimateFoodsCore.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, UltimateFoodsCore.MOD_ID);

    public static final DeferredBlock<ParrillaBlock> PARRILLA = BLOCKS.registerBlock(
            "parrilla",
            ParrillaBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(2.5F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(ParrillaBlock.LIT) ? ParrillaBlock.LIT_LIGHT_LEVEL : 0));

    public static final DeferredItem<BlockItem> PARRILLA_ITEM = ITEMS.registerSimpleBlockItem(PARRILLA);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ParrillaBlockEntity>> PARRILLA_BE =
            BLOCK_ENTITIES.register("parrilla", () ->
                    BlockEntityType.Builder.of(ParrillaBlockEntity::new, PARRILLA.get()).build(null));

    private ParrillaRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
