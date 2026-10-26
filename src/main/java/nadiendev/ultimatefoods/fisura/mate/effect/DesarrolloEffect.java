package nadiendev.ultimatefoods.fisura.mate.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;

/** Mate de leche: las crias cercanas crecen al instante. */
public class DesarrolloEffect extends MobEffect {

    private static final double RADIO = 5.0;

    public DesarrolloEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        for (AgeableMob cria : level.getEntitiesOfClass(AgeableMob.class,
                entity.getBoundingBox().inflate(RADIO), m -> m != entity && m.getAge() < 0)) {
            cria.setAge(0);
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, cria.getX(), cria.getY(0.6), cria.getZ(), 8, 0.3, 0.3, 0.3, 0.0);
        }
        return true;
    }
}
