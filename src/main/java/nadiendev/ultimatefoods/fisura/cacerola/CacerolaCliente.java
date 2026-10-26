package nadiendev.ultimatefoods.fisura.cacerola;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

/**
 * Solo cliente: guarda las recetas de la cacerola que manda el servidor (ver {@link CacerolaRecetas}).
 * Corre con prioridad normal, antes que JEI, que arranca con prioridad minima en el mismo evento.
 */
@EventBusSubscriber(modid = UltimateFoodsCore.MOD_ID, value = Dist.CLIENT)
public final class CacerolaCliente {

    private CacerolaCliente() {
    }

    @SubscribeEvent
    public static void recetasRecibidas(RecipesReceivedEvent event) {
        CacerolaRecetas.recibidas(event.getRecipeMap());
    }

    @SubscribeEvent
    public static void salirDelMundo(ClientPlayerNetworkEvent.LoggingOut event) {
        CacerolaRecetas.olvidar();
    }
}
