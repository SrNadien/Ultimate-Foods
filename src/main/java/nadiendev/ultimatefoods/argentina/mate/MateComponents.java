package nadiendev.ultimatefoods.argentina.mate;

import com.mojang.serialization.Codec;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** DataComponents propios del mate, la pava y la yerbera. */
public final class MateComponents {

    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, UltimateFoodsCore.MOD_ID);

    private static final Codec<Integer> NO_NEGATIVO = Codec.intRange(0, 1024);

    /** Sabor con el que esta (o va a quedar) el mate. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MateSabor>> SABOR =
            COMPONENTS.registerComponentType("sabor", b -> b
                    .persistent(MateSabor.CODEC)
                    .networkSynchronized(MateSabor.STREAM_CODEC));

    /** Sorbos que le quedan al mate cebado. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SORBOS =
            COMPONENTS.registerComponentType("sorbos", b -> b
                    .persistent(NO_NEGATIVO)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    /** Cuantas veces se cebo ya la misma yerba del mate (a mas cebadas, mas lavada). */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CEBADAS =
            COMPONENTS.registerComponentType("cebadas", b -> b
                    .persistent(NO_NEGATIVO)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    /** Cebadas que le quedan a la pava caliente. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CEBADAS_RESTANTES =
            COMPONENTS.registerComponentType("cebadas_restantes", b -> b
                    .persistent(NO_NEGATIVO)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    /** Porciones de yerba guardadas en una yerbera (tambien viaja en el item al romperla). */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PORCIONES =
            COMPONENTS.registerComponentType("porciones", b -> b
                    .persistent(NO_NEGATIVO)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    private MateComponents() {
    }
}
