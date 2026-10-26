package nadiendev.ultimatefoods.fisura;

import nadiendev.ultimatefoods.fisura.comida.ComidaModule;
import nadiendev.ultimatefoods.fisura.homenaje.HomenajeModule;
import nadiendev.ultimatefoods.fisura.mate.MateModule;
import net.neoforged.bus.api.IEventBus;

/**
 * Contenido criollo del mod: la pestaña "Artinna Re Fisura" y sus modulos. Cada modulo se registra solo desde aca.
 */
public final class Fisura {

    private Fisura() {
    }

    public static void register(IEventBus modEventBus) {
        MateModule.register(modEventBus);
        ComidaModule.register(modEventBus);
        HomenajeModule.register(modEventBus);
        FisuraTab.register(modEventBus);
    }
}
