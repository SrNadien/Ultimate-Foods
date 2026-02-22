package nadiendev.ultimatefoods.avaritia;

import com.mojang.serialization.Codec;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Data Components personalizados para items de Avaritia
 * By NadienDev
 */
public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, UltimateFoodsCore.MOD_ID);

    /**
     * Componente para almacenar el modo Silk Touch del Infinity-1 Pickaxe
     * true = Silk Touch, false = Fortune III
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> SILK_TOUCH_MODE =
            DATA_COMPONENTS.register("silk_touch_mode",
                    () -> DataComponentType.<Boolean>builder()
                            .persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL) // Fix: Codec.BOOL no es StreamCodec
                            .build()
            );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}