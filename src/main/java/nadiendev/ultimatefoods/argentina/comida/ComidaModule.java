package nadiendev.ultimatefoods.argentina.comida;

import nadiendev.ultimatefoods.argentina.cacerola.CacerolaRegistry;
import nadiendev.ultimatefoods.argentina.comida.colocada.ColocadaData;
import nadiendev.ultimatefoods.argentina.comida.colocada.ColocadaRegistry;
import nadiendev.ultimatefoods.argentina.comida.data.ComidaLootAndTags;
import nadiendev.ultimatefoods.argentina.comida.data.ComidaModels;
import nadiendev.ultimatefoods.argentina.comida.data.ComidaRecipes;
import nadiendev.ultimatefoods.argentina.comida.data.NamedProvider;
import nadiendev.ultimatefoods.argentina.parrilla.ParrillaRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

/**
 * Modulo de comidas y bebidas argentinas, mas la parrilla. Dueño: agente B
 * (paquetes argentina.comida y argentina.parrilla).
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

    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        var lookup = event.getLookupProvider();
        var existing = event.getExistingFileHelper();

        var generator = event.getGenerator();
        generator.addProvider(event.includeServer(),
                new NamedProvider("Comida: Recipes", new ComidaRecipes(output, lookup)));
        generator.addProvider(event.includeServer(),
                new NamedProvider("Comida: Loot Tables", new ComidaLootAndTags.Loot(output, lookup)));
        generator.addProvider(event.includeServer(),
                new NamedProvider("Comida: Block Tags", new ComidaLootAndTags.Tags(output, lookup, existing)));
        generator.addProvider(event.includeClient(),
                new NamedProvider("Comida: Item Models", new ComidaModels.Items(output, existing)));
        generator.addProvider(event.includeClient(),
                new NamedProvider("Comida: Block States", new ComidaModels.Blocks(output, existing)));

        // Bloque comida_colocada: blockstate y loot propios (providers con nombre unico).
        generator.addProvider(event.includeServer(),
                new NamedProvider("Comida colocada: Loot Tables", new ColocadaData.Loot(output, lookup)));
        generator.addProvider(event.includeClient(), new ColocadaData.States(output, existing));
    }
}
