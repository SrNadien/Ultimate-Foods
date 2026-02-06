package nadiendev.ultimatefoods.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


@EventBusSubscriber
public class PoopEffectHandler {

    private static final int XP_PER_DROP = 10;           // XP que da cada vez
    private static final int TICKS_PER_DROP = 1200;      // 1 minuto = 1200 ticks (20 ticks/segundo * 60 segundos)
    private static final int EFFECT_DURATION = 6000;     // 5 minutos = 6000 ticks

    // Evento que se ejecuta cada tick del jugador
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        // Solo ejecutar en el servidor
        if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        // Verificar si el jugador tiene el efecto "Qué Guarro" activo
        long currentTime = player.level().getGameTime();
        long endTime = serverPlayer.getPersistentData().getLong("queGuarroEndTime");
        
        if (endTime > 0 && currentTime < endTime) {
            // El efecto está activo
            int ticksElapsed = serverPlayer.getPersistentData().getInt("queGuarroTicks");
            ticksElapsed++;
            
            // Cada 1200 ticks (1 minuto), dar XP
            if (ticksElapsed >= TICKS_PER_DROP) {
                // Dar XP al jugador
                serverPlayer.giveExperiencePoints(XP_PER_DROP);
                
                // Reiniciar el contador
                ticksElapsed = 0;
            }
            
            // Guardar el progreso
            serverPlayer.getPersistentData().putInt("queGuarroTicks", ticksElapsed);
            
        } else if (endTime > 0 && currentTime >= endTime) {
            // El efecto ha terminado, limpiar datos
            serverPlayer.getPersistentData().remove("queGuarroEndTime");
            serverPlayer.getPersistentData().remove("queGuarroTicks");
        }
    }
}