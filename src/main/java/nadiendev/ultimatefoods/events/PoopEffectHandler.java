package nadiendev.ultimatefoods.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class PoopEffectHandler {

    private static final int XP_PER_DROP = 10;
    private static final int TICKS_PER_DROP = 1200;
    private static final int EFFECT_DURATION = 6000;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        long currentTime = player.level().getGameTime();
        long endTime = serverPlayer.getPersistentData().getLongOr("queGuarroEndTime", 0L);

        if (endTime > 0 && currentTime < endTime) {

            int ticksElapsed = serverPlayer.getPersistentData().getIntOr("queGuarroTicks", 0);
            ticksElapsed++;

            if (ticksElapsed >= TICKS_PER_DROP) {

                serverPlayer.giveExperiencePoints(XP_PER_DROP);

                ticksElapsed = 0;
            }

            serverPlayer.getPersistentData().putInt("queGuarroTicks", ticksElapsed);

        } else if (endTime > 0 && currentTime >= endTime) {

            serverPlayer.getPersistentData().remove("queGuarroEndTime");
            serverPlayer.getPersistentData().remove("queGuarroTicks");
        }
    }
}
