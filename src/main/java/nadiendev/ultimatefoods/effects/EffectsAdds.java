package nadiendev.ultimatefoods.effects;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.effects.FlyingEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/* Effect Registry Provider
* By NadienDev
*/
public class EffectsAdds {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, UltimateFoodsCore.MOD_ID);

    
    public static final DeferredHolder<MobEffect, MobEffect> QUE_GUARRO = MOB_EFFECTS.register(
            "que_guarro",
            () -> new QueGuarroEffect(MobEffectCategory.BENEFICIAL, 0x8B4513) // Color marrón - BENEFICIAL porque da experiencia
    );

    // Efecto de vuelo - adaptado de Apothic Attributes (ahora en archivo separado FlyingEffect.java)
    public static final DeferredHolder<MobEffect, MobEffect> FLYING = MOB_EFFECTS.register(
            "flying",
            () -> new FlyingEffect()
    );

 
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

  
    public static class QueGuarroEffect extends MobEffect {
        protected QueGuarroEffect(MobEffectCategory category, int color) {
            super(category, color);
        }

        @Override
        public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
            // ============================================
            // INTERVALO DE EJECUCIÓN
            // ============================================
            // 1 tick    = 1/20 de un segundo
            // 20 ticks  = 1 segundo
            // 60 ticks  = 3 segundos
            // 200 ticks = 10 segundos
            // 600 ticks = 30 segundos
            // 1200 ticks = 1 minuto
            // ============================================
            // Cambiar el número de abajo para ajustar
            // cada cuánto se dan los 10 XP.
            // Actualmente: cada 900 ticks = cada 45 segundos
            // ============================================
            return duration % 200 == 0;
        }

        @Override
        public boolean applyEffectTick(LivingEntity entity, int amplifier) {
            if (entity instanceof Player player) {
                player.giveExperienceLevels(30);
                return true;
            }
            return false;
        }
    }
}