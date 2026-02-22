package nadiendev.ultimatefoods.avaritia;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Compatibilidad con Avaritia
 * Solo registra items y handlers - NO providers de datagen
 * Las recetas de Avaritia deben ir en ModRecipeProvider
 */
public class AvaritiaCompat {

    public static void register(IEventBus modEventBus) {
        AvaritiaToolsAdds.register(modEventBus);
        ModDataComponents.register(modEventBus);
        
    }
}