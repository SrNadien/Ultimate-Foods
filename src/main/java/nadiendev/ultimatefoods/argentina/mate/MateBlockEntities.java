package nadiendev.ultimatefoods.argentina.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.mate.block.AzucareraBlockEntity;
import nadiendev.ultimatefoods.argentina.mate.block.YerberaBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MateBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<YerberaBlockEntity>> YERBERA =
            BLOCK_ENTITIES.register("yerbera", () ->
                    BlockEntityType.Builder.of(YerberaBlockEntity::new, MateBlocks.YERBERA.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AzucareraBlockEntity>> AZUCARERA =
            BLOCK_ENTITIES.register("azucarera", () ->
                    BlockEntityType.Builder.of(AzucareraBlockEntity::new, MateBlocks.AZUCARERA.get()).build(null));

    private MateBlockEntities() {
    }
}
