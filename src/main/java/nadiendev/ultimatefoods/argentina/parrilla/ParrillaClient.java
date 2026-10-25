package nadiendev.ultimatefoods.argentina.parrilla;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/** Registro solo-cliente de la parrilla: el renderer de la comida sobre la reja. */
@EventBusSubscriber(modid = UltimateFoodsCore.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ParrillaClient {

    private ParrillaClient() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ParrillaRegistry.PARRILLA_BE.get(), ParrillaRenderer::new);
    }
}
