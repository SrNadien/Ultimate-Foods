package nadiendev.ultimatefoods.fisura.mate.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;

/** Mate amargo: la calma se contagia y las criaturas hostiles cercanas se ponen lentas. */
public class TranquilidadEffect extends MobEffect {

    private static final double RADIO = 10.0;

    public TranquilidadEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel level) {
            for (Mob mob : level.getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(RADIO),
                    m -> m instanceof Enemy && m != entity)) {
                mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2 + amplifier, true, false));
            }
        }
        return true;
    }
}
