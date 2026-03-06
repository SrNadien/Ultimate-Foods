package nadiendev.ultimatefoods.avaritia;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Compatibilidad con Avaritia
 * By NadienDev
 */
public class AvaritiaCompat {

    public static void register(IEventBus modEventBus) {
        AvaritiaToolsAdds.register(modEventBus);
        ModDataComponents.register(modEventBus);
        
    }
}