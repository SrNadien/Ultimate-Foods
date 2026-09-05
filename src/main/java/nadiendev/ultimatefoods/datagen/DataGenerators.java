package nadiendev.ultimatefoods.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.datagen.providers.ExDeorumCompatProvider;
import nadiendev.ultimatefoods.datagen.providers.ModAdvancementProvider;
import nadiendev.ultimatefoods.datagen.providers.ModBlockTags;
import nadiendev.ultimatefoods.datagen.providers.ModItemTags;
import nadiendev.ultimatefoods.datagen.providers.ModJukeboxSongProvider;
import nadiendev.ultimatefoods.datagen.providers.ModLootModifierProvider;
import nadiendev.ultimatefoods.datagen.providers.ModLootTableProvider;
import nadiendev.ultimatefoods.datagen.providers.ModModelProvider;
import nadiendev.ultimatefoods.datagen.providers.ModRecipeProvider;
import nadiendev.ultimatefoods.datagen.providers.ModSoundProvider;
import nadiendev.ultimatefoods.datagen.providers.ModWorldGenProvider;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

public class DataGenerators {

    private DataGenerators() {
    }

    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModSoundProvider::new);
    }

    public static void gatherServerData(GatherDataEvent.Server event) {
        UltimateFoodsCore.LOGGER.info("=== INICIANDO GENERACION DE DATOS ===");

        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModWorldGenProvider::new);
        event.createProvider(ModLootTableProvider::new);
        event.createProvider((output, lookupProvider) ->
                new AdvancementProvider(output, lookupProvider, List.of(new ModAdvancementProvider())));
        event.createProvider(ModJukeboxSongProvider::new);
        event.createProvider(ModLootModifierProvider::new);
        event.createProvider(ExDeorumCompatProvider::new);
        event.createProvider(ModBlockTags.Provider::new);
        event.createProvider(ModItemTags.Provider::new);

        UltimateFoodsCore.LOGGER.info("=== TODOS LOS PROVIDERS REGISTRADOS CORRECTAMENTE ===");
    }
}
