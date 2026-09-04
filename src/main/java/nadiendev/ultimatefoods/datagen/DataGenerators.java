package nadiendev.ultimatefoods.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.datagen.providers.BlockModelProvider;
import nadiendev.ultimatefoods.datagen.providers.ExDeorumCompatProvider;
import nadiendev.ultimatefoods.datagen.providers.ItemsModelProvider;
import nadiendev.ultimatefoods.datagen.providers.ModAdvancementProvider;
import nadiendev.ultimatefoods.datagen.providers.ModJukeboxSongProvider;
import nadiendev.ultimatefoods.datagen.providers.ModLootModifierProvider;
import nadiendev.ultimatefoods.datagen.providers.ModLootTableProvider;
import nadiendev.ultimatefoods.datagen.providers.ModRecipeProvider;
import nadiendev.ultimatefoods.datagen.providers.ModSoundProvider;
import nadiendev.ultimatefoods.datagen.providers.ModWorldGenProvider;
import nadiendev.ultimatefoods.datagen.providers.ModBlockTags;
import nadiendev.ultimatefoods.datagen.providers.ModItemTags;
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
        generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(),
                new AdvancementProvider(output, lookupProvider, existingFileHelper,
                        List.of(new ModAdvancementProvider())));
        generator.addProvider(event.includeServer(), new ModJukeboxSongProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModLootModifierProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ExDeorumCompatProvider(output));

        ModBlockTags.Provider blockTagsProvider = generator.addProvider(
                event.includeServer(),
                new ModBlockTags.Provider(output, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new ModItemTags.Provider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper)
        );

        generator.addProvider(event.includeClient(), new BlockModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ItemsModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));

        UltimateFoodsCore.LOGGER.info("=== TODOS LOS PROVIDERS REGISTRADOS CORRECTAMENTE ===");
    }
}
