package nadiendev.ultimatefoods;

import com.mojang.logging.LogUtils;
import nadiendev.ultimatefoods.avaritia.ModDataComponents;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import nadiendev.ultimatefoods.creative.CreativeTab;
import nadiendev.ultimatefoods.datagen.BlockModelProvider;
import nadiendev.ultimatefoods.datagen.ItemsModelProvider;
import nadiendev.ultimatefoods.datagen.ModAdvancementProvider;
import nadiendev.ultimatefoods.datagen.ModJukeboxSongProvider;
import nadiendev.ultimatefoods.datagen.ModLootTableProvider;
import nadiendev.ultimatefoods.datagen.ModSoundProvider;
import nadiendev.ultimatefoods.datagen.ModWorldGenProvider;
import nadiendev.ultimatefoods.effects.EffectsAdds;
import nadiendev.ultimatefoods.fluidos_xd.FluidsRegistry;
import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.items.armor.ArmorAdds;
import nadiendev.ultimatefoods.items.armor.NadieniteArmorMaterial;
import nadiendev.ultimatefoods.items.tools.ToolsAdds;
import nadiendev.ultimatefoods.recipes.ModRecipeProvider;
import nadiendev.ultimatefoods.sounds.SonidosReproducibles;
import nadiendev.ultimatefoods.tags.ModBlockTags;
import nadiendev.ultimatefoods.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(UltimateFoodsCore.MOD_ID)
public class UltimateFoodsCore {
    public static final String MOD_ID = "ultimatefoods";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UltimateFoodsCore(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Inicializando Ultimate Foods Mod...");

        SonidosReproducibles.register(modEventBus);
        EffectsAdds.register(modEventBus);
        NadieniteArmorMaterial.ARMOR_MATERIALS.register(modEventBus);
        BlocksAdds.register(modEventBus);
        NadieniteOreBlock.register(modEventBus);
        ItemsAdds.register(modEventBus);
        ArmorAdds.register(modEventBus);
        ToolsAdds.register(modEventBus);

        // Fluidos
        FluidsRegistry.FLUID_TYPES.register(modEventBus);
        FluidsRegistry.FLUIDS.register(modEventBus);
        FluidsRegistry.FLUID_BLOCKS.register(modEventBus);

        if (ModList.get().isLoaded("avaritia")) {
            ModDataComponents.register(modEventBus);
            nadiendev.ultimatefoods.avaritia.AvaritiaToolsAdds.register(modEventBus);
            LOGGER.info("Avaritia detectado — items y data components registrados");
        } else {
            LOGGER.info("Avaritia no detectado — items de Avaritia omitidos");
        }

        CreativeTab.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);

        LOGGER.info("Ultimate Foods Mod inicializado correctamente!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Configuración común de Ultimate Foods");
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        existingFileHelper.trackGenerated(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/nadienite_fluid"),
            PackType.CLIENT_RESOURCES, ".png", "textures"
        );
        existingFileHelper.trackGenerated(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/nadienite_flow"),
            PackType.CLIENT_RESOURCES, ".png", "textures"
        );

        LOGGER.info("=== INICIANDO GENERACIÓN DE DATOS ===");

        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(),
                new AdvancementProvider(output, lookupProvider, existingFileHelper,
                        List.of(new ModAdvancementProvider())));
        generator.addProvider(event.includeServer(), new ModJukeboxSongProvider(output, lookupProvider));

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

        LOGGER.info("=== TODOS LOS PROVIDERS REGISTRADOS CORRECTAMENTE ===");
    }
}