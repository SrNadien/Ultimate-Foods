package nadiendev.ultimatefoods.fisura;

import nadiendev.ultimatefoods.fisura.comida.ComidaModule;
import nadiendev.ultimatefoods.fisura.homenaje.HomenajeModule;
import nadiendev.ultimatefoods.fisura.mate.MateModule;
import nadiendev.ultimatefoods.fisura.puesto.PuestoRegistry;
import net.neoforged.bus.api.IEventBus;

/**
 * Contenido criollo del mod: la pestaña "Artinna Re Fisura" y sus dos modulos.
 * Cada modulo tiene un dueño y se registra solo desde aca, asi que ninguno
 * necesita tocar archivos compartidos.
 */
public final class Fisura {

    private Fisura() {
    }

    public static void register(IEventBus modEventBus) {
        MateModule.register(modEventBus);
        ComidaModule.register(modEventBus);
        HomenajeModule.register(modEventBus);
        PuestoRegistry.register(modEventBus);
        FisuraTab.register(modEventBus);
    }
}
