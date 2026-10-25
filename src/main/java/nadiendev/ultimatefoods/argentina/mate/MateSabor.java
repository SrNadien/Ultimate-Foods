package nadiendev.ultimatefoods.argentina.mate;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

/** Los cuatro sabores del mate. Cada uno tiene su estado de item y su efecto. */
public enum MateSabor implements StringRepresentable {
    AMARGO("amargo", 20 * 20),
    DULCE("dulce", 30 * 20),
    LECHE("leche", 30 * 20),
    YUYOS("yuyos", 15 * 20);

    public static final Codec<MateSabor> CODEC = StringRepresentable.fromEnum(MateSabor::values);
    public static final StreamCodec<ByteBuf, MateSabor> STREAM_CODEC = ByteBufCodecs.idMapper(
            ByIdMap.continuous(MateSabor::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO),
            MateSabor::ordinal);

    private final String id;
    private final int duracionBase;

    MateSabor(String id, int duracionBase) {
        this.id = id;
        this.duracionBase = duracionBase;
    }

    @Override
    public String getSerializedName() {
        return id;
    }

    /** Duracion del efecto de un sorbo, en ticks, para la primera cebada. */
    public int duracionBase() {
        return duracionBase;
    }

    /** Clave de traduccion del nombre del sabor. */
    public String translationKey() {
        return "mate.ultimatefoods.sabor." + id;
    }

    /** Item del mate ya cebado con este sabor. */
    public Item itemCebado() {
        return switch (this) {
            case AMARGO -> MateItems.MATE_AMARGO.get();
            case DULCE -> MateItems.MATE_DULCE.get();
            case LECHE -> MateItems.MATE_DE_LECHE.get();
            case YUYOS -> MateItems.MATE_CON_YUYOS.get();
        };
    }

    public Holder<MobEffect> efecto() {
        return switch (this) {
            case AMARGO -> MateEffects.TRANQUILIDAD;
            case DULCE -> MateEffects.MANDALE_MECHA;
            case LECHE -> MateEffects.DESARROLLO;
            case YUYOS -> MateEffects.BONANZA;
        };
    }
}
