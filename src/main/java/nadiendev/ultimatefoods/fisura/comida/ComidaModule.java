package nadiendev.ultimatefoods.fisura.comida;

import nadiendev.ultimatefoods.fisura.cacerola.CacerolaRegistry;
import nadiendev.ultimatefoods.fisura.comida.colocada.ColocadaRegistry;
import nadiendev.ultimatefoods.fisura.parrilla.ParrillaRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

import java.util.List;

/**
 * Modulo de comidas y bebidas criollas, mas la parrilla y la Cacerola Essen (paquetes
 * fisura.comida, fisura.parrilla y fisura.cacerola).
 * <p>
 * En 26.1.2 no hay datagen para este modulo: los modelos, blockstates, recetas, loot y tags son
 * JSON estaticos en {@code src/main/resources}, con los mismos ids que en 1.21.1.
 */
public final class ComidaModule {

    private ComidaModule() {
    }

    public static void register(IEventBus modEventBus) {
        ComidaEffects.register(modEventBus);
        ComidaItems.register(modEventBus);
        ParrillaRegistry.register(modEventBus);
        CacerolaRegistry.register(modEventBus);
        ColocadaRegistry.register(modEventBus);
    }

    public static void displayItems(CreativeModeTab.Output output) {
        ComidaItems.displayItems(output);
        output.accept(ParrillaRegistry.PARRILLA_ITEM.get());
        output.accept(CacerolaRegistry.CACEROLA_ESSEN_ITEM.get());
    }

    /** Ids de los items con modelo plano (todos menos la parrilla, que tiene modelo 3D). */
    public static List<String> flatItemIds() {
        return ComidaItems.flatIds();
    }
}
