package nadiendev.ultimatefoods.argentina.comida.colocada;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
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

    private ColocadaRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
