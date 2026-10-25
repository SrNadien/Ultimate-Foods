package nadiendev.ultimatefoods.argentina.mate.effect;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * Mate dulce ("Out of my way!"): mucha velocidad, y lo que esta a ~2 bloques sale
 * empujado y recibe un daño chico. Al terminar deja una lentitud breve
 * (ver {@code MateEvents}).
 */
public class MandaleMechaEffect extends MobEffect {

    private static final double RADIO = 2.0;

    public MandaleMechaEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "effect.mandale_mecha"),
                0.6, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel level) {
            DamageSource source = entity instanceof Player player
                    ? level.damageSources().playerAttack(player)
                    : level.damageSources().mobAttack(entity);
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                    entity.getBoundingBox().inflate(RADIO), e -> puedeEmpujar(entity, e))) {
                target.hurt(source, 1.0F);
                target.knockback(0.6, entity.getX() - target.getX(), entity.getZ() - target.getZ());
                level.sendParticles(ParticleTypes.CRIT, target.getX(), target.getY(0.5), target.getZ(), 4, 0.2, 0.2, 0.2, 0.1);
            }
        }
        return true;
    }

    private static boolean puedeEmpujar(LivingEntity quien, LivingEntity otro) {
        if (otro == quien || !otro.isAlive() || !otro.attackable()) return false;
        if (otro.isAlliedTo(quien)) return false;
        if (otro instanceof TamableAnimal mascota && mascota.getOwner() == quien) return false;
        if (otro instanceof Player victima && quien instanceof Player agresor && !agresor.canHarmPlayer(victima)) return false;
        return quien.distanceToSqr(otro) <= RADIO * RADIO;
    }
}
