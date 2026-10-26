package nadiendev.ultimatefoods.fisura.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.mate.effect.BonanzaEffect;
import nadiendev.ultimatefoods.fisura.mate.effect.DesarrolloEffect;
import nadiendev.ultimatefoods.fisura.mate.effect.MandaleMechaEffect;
import nadiendev.ultimatefoods.fisura.mate.effect.TranquilidadEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Los cuatro efectos que dejan los mates. */
public final class MateEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> TRANQUILIDAD = EFFECTS.register("tranquilidad",
            () -> new TranquilidadEffect(MobEffectCategory.BENEFICIAL, 0x7FB6E8));
    public static final DeferredHolder<MobEffect, MobEffect> MANDALE_MECHA = EFFECTS.register("mandale_mecha",
            () -> new MandaleMechaEffect(MobEffectCategory.BENEFICIAL, 0xF08A2A));
    public static final DeferredHolder<MobEffect, MobEffect> DESARROLLO = EFFECTS.register("desarrollo",
            () -> new DesarrolloEffect(MobEffectCategory.BENEFICIAL, 0x6CC267));
    public static final DeferredHolder<MobEffect, MobEffect> BONANZA = EFFECTS.register("bonanza",
            () -> new BonanzaEffect(MobEffectCategory.BENEFICIAL, 0xF2C230));

    private MateEffects() {
    }
}
