package nadiendev.ultimatefoods.fisura.mate.effect;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * Mate con yuyos: la bonanza. Suerte extra, un poco de experiencia cada tanto y
 * mas experiencia por lo que matas (ver {@code MateEvents}).
 */
public class BonanzaEffect extends MobEffect {

    public BonanzaEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.LUCK,
                Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "effect.bonanza"),
                2.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 60 == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            player.giveExperiencePoints(1 + amplifier);
        }
        return true;
    }
}
