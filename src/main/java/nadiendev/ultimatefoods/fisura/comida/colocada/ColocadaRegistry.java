package nadiendev.ultimatefoods.fisura.comida.colocada;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registro del bloque {@code comida_colocada}. No tiene item propio: cada comida usa el suyo. */
public final class ColocadaRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UltimateFoodsCore.MOD_ID);

    public static final DeferredBlock<ComidaColocadaBlock> COMIDA_COLOCADA = BLOCKS.registerBlock(
            "comida_colocada",
            ComidaColocadaBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(0.3F)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ComidaColocadaBlockEntity>> COMIDA_BE =
            BLOCK_ENTITIES.register("comida_colocada", () ->
                    BlockEntityType.Builder.of(ComidaColocadaBlockEntity::new, COMIDA_COLOCADA.get()).build(null));

    private ColocadaRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
