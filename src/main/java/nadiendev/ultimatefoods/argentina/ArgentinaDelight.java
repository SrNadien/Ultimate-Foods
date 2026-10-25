package nadiendev.ultimatefoods.argentina;

import nadiendev.ultimatefoods.argentina.comida.ComidaModule;
import nadiendev.ultimatefoods.argentina.homenaje.HomenajeModule;
import nadiendev.ultimatefoods.argentina.mate.MateModule;
import nadiendev.ultimatefoods.argentina.puesto.PuestoRegistry;
import net.neoforged.bus.api.IEventBus;

/**
 * Contenido argentino del mod: la pestaña "Argentina Delight" y sus dos modulos.
 * Cada modulo tiene un dueño y se registra solo desde aca, asi que ninguno
 * necesita tocar archivos compartidos.
 */
public final class ArgentinaDelight {

    private ArgentinaDelight() {
    }

    public static void register(IEventBus modEventBus) {
        MateModule.register(modEventBus);
        ComidaModule.register(modEventBus);
        HomenajeModule.register(modEventBus);
        PuestoRegistry.register(modEventBus);
        ArgentinaTab.register(modEventBus);
    }
}
