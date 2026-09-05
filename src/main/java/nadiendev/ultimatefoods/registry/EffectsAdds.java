package nadiendev.ultimatefoods.registry;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectsAdds {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> QUE_GUARRO = MOB_EFFECTS.register(
            "que_guarro",
            () -> new QueGuarroEffect(MobEffectCategory.BENEFICIAL, 0x8B4513)
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

            return duration % 200 == 0;
        }

        @Override
        public boolean applyEffectTick(net.minecraft.server.level.ServerLevel level, LivingEntity entity, int amplifier) {
            if (entity instanceof Player player) {
                player.giveExperienceLevels(30);
                return true;
            }
            return false;
        }
    }
}
