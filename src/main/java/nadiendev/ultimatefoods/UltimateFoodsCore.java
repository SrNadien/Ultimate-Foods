package nadiendev.ultimatefoods;

import com.mojang.logging.LogUtils;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import nadiendev.ultimatefoods.creative.CreativeTab;
import nadiendev.ultimatefoods.datagen.ModLootTableProvider;
import nadiendev.ultimatefoods.effects.EffectsAdds;
import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.items.armor.ArmorAdds;
import nadiendev.ultimatefoods.items.armor.NadieniteArmorMaterial;
import nadiendev.ultimatefoods.items.tools.ToolsAdds;
import nadiendev.ultimatefoods.recipes.ModRecipeProvider;
import nadiendev.ultimatefoods.sounds.SonidosReproducibles;
import nadiendev.ultimatefoods.tags.ModBlockTags;
import nadiendev.ultimatefoods.tags.ModItemTags;
import nadiendev.ultimatefoods.datagen.BlockModelProvider;
import nadiendev.ultimatefoods.datagen.ModSoundProvider;
import nadiendev.ultimatefoods.datagen.ModWorldGenProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

/**
 * UltimateFoodsCore
 * By NadienDev
 * Clase principal del mod Ultimate Foods
 * Se encarga de registrar todos los elementos del mod (bloques, items, efectos, sonidos, etc.) y de configurar la generación de datos (data generation)
 * Sigue un orden específico para evitar problemas de dependencias entre elementos (por ejemplo, registrar sonidos antes que items que los usan)
 */
@Mod(UltimateFoodsCore.MOD_ID)
public class UltimateFoodsCore {
    public static final String MOD_ID = "ultimatefoods";
    private static final Logger LOGGER = LogUtils.getLogger();

    public UltimateFoodsCore(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Inicializando Ultimate Foods Mod...");

        // ============================================
        // ORDEN CORRECTO DE REGISTRO
        // ============================================
        
        // 1. Primero los sonidos (usados por items)
        SonidosReproducibles.register(modEventBus);
        LOGGER.info("Sonidos registrados");
        
        // 2. Luego los efectos (usados por items)
        EffectsAdds.register(modEventBus);
        LOGGER.info("Efectos registrados");
        
        // 3. Materiales de armadura (antes que los items de armadura)
        NadieniteArmorMaterial.ARMOR_MATERIALS.register(modEventBus);
        LOGGER.info("Materiales de armadura registrados");
        
        // 4. Bloques normales
        BlocksAdds.register(modEventBus);
        LOGGER.info("Bloques registrados");
        
        // 5. Bloques de mineral de Nadienite
        NadieniteOreBlock.register(modEventBus);
        LOGGER.info("Bloques de mineral registrados");
        
        // 6. Items regulares (que dependen de sonidos y efectos)
        ItemsAdds.register(modEventBus);
        LOGGER.info("Items registrados");
        
        // 7. Items de armadura (dependen de materiales)
        ArmorAdds.register(modEventBus);
        LOGGER.info("Armadura registrada");
        
        // 8. Herramientas y armas (dependen de materiales)
        ToolsAdds.register(modEventBus);
        LOGGER.info("Herramientas registradas");
        
        // 9. IMPORTANTE: Tab creativo AL FINAL (después de todos los items)
        CreativeTab.register(modEventBus);
        LOGGER.info("Tab creativo registrado");

        // Setup común
        modEventBus.addListener(this::commonSetup);
        
        // Registrar el listener para generación de datos - RUNDATA
        modEventBus.addListener(this::gatherData);

        LOGGER.info("Ultimate Foods Mod inicializado correctamente!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Configuración común de Ultimate Foods");
    }

    // MÉTODO RUNDATA - Genera todos los archivos JSON
    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        LOGGER.info("=== INICIANDO GENERACIÓN DE DATOS ===");
        
        // ========== SERVER-SIDE PROVIDERS ==========
        
        // Provider de recetas
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
        LOGGER.info("Provider de recetas registrado");
        
        // Provider de WorldGen - GENERACIÓN DE ORES
        generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookupProvider));
        LOGGER.info("Provider de WorldGen registrado");
        
        // Provider de LOOT TABLES - NUEVOS DROPS DE BLOQUES
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider));
        LOGGER.info("Provider de Loot Tables registrado");
        
        // Provider de TAGS de bloques
        ModBlockTags.Provider blockTagsProvider = generator.addProvider(
            event.includeServer(),
            new ModBlockTags.Provider(output, lookupProvider, existingFileHelper)
        );
        LOGGER.info("Provider de tags de bloques registrado");
        
        // Provider de TAGS de items
        generator.addProvider(
            event.includeServer(),
            new ModItemTags.Provider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper)
        );
        LOGGER.info("Provider de tags de items registrado");
        
        // ========== CLIENT-SIDE PROVIDERS ==========
        
        // Provider de modelos de bloques
        generator.addProvider(event.includeClient(), new BlockModelProvider(output, existingFileHelper));
        LOGGER.info("Provider de modelos de bloques registrado");
        
        // Provider de sonidos
        generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));
        LOGGER.info("Provider de sonidos registrado");
        
        LOGGER.info("=== TODOS LOS PROVIDERS REGISTRADOS CORRECTAMENTE ===");
        LOGGER.info("Ejecuta el comando: gradlew runData");
    }
}