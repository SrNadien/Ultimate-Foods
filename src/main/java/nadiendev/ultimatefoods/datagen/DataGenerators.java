package nadiendev.ultimatefoods.datagen;

import nadiendev.ultimatefoods.argentina.ArgentinaData;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.datagen.providers.ItemsModelProvider;
import nadiendev.ultimatefoods.datagen.providers.ModAdvancementProvider;
import nadiendev.ultimatefoods.datagen.providers.ModLootTableProvider;
import nadiendev.ultimatefoods.datagen.providers.ModRecipeProvider;
import nadiendev.ultimatefoods.datagen.providers.ModSoundProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataGenerators {

    private DataGenerators() {
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        existingFileHelper.trackGenerated(
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/nadienite_fluid"),
                PackType.CLIENT_RESOURCES, ".png", "textures"
        );
        existingFileHelper.trackGenerated(
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/nadienite_flow"),
                PackType.CLIENT_RESOURCES, ".png", "textures"
        );

        UltimateFoodsCore.LOGGER.info("=== INICIANDO GENERACION DE DATOS ===");

        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(),
                new AdvancementProvider(output, lookupProvider, existingFileHelper,
                        List.of(new ModAdvancementProvider())));

        generator.addProvider(event.includeClient(), new ItemsModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));
        ArgentinaData.gather(event);


        UltimateFoodsCore.LOGGER.info("=== TODOS LOS PROVIDERS REGISTRADOS CORRECTAMENTE ===");
    }
}
