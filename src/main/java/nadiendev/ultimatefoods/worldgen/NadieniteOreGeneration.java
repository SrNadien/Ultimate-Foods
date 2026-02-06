package nadiendev.ultimatefoods.worldgen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

/**
 * NadieniteOreGeneration
 * By NadienDev
 * Clase para definir la generación de mineral de nadienite en el mundo
 * Configura dónde y cómo se genera el mineral, incluyendo el tamaño de las venas, la altura de generación y los bloques que reemplaza
 */
public class NadieniteOreGeneration {

    // ========== CONFIGURED FEATURES ==========
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> NADIENITE_ORE_KEY = 
            createKey("nadienite_ore");

    // ========== PLACED FEATURES ==========
    
    public static final ResourceKey<PlacedFeature> NADIENITE_ORE_PLACED_KEY = 
            createPlacedKey("nadienite_ore");

    // ========== BOOTSTRAP METHODS ==========

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // Lista de targets: qué bloques reemplazar
        List<OreConfiguration.TargetBlockState> nadieniteOres = List.of(
                OreConfiguration.target(stoneReplaceables, NadieniteOreBlock.NADIENITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get().defaultBlockState())
        );

        // Registrar configured feature
        context.register(
                NADIENITE_ORE_KEY,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(nadieniteOres, 9)) // 9 = tamaño máximo de vena
        );
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Generación similar al diamante:
        // - Y: -64 a 16
        // - Más común en Y: -58 y -59
        // - 7 intentos por chunk
        context.register(
                NADIENITE_ORE_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(NADIENITE_ORE_KEY),
                        List.of(
                                // Rango de altura: -64 a 16 (como diamante)
                                HeightRangePlacement.triangle(
                                        VerticalAnchor.absolute(-64),
                                        VerticalAnchor.absolute(16)
                                ),
                                // 7 intentos por chunk
                                CountPlacement.of(7),
                                // Generación en bloque cuadrado
                                InSquarePlacement.spread(),
                                // Bias hacia abajo (más común en capas bajas)
                                BiomeFilter.biome()
                        )
                )
        );
    }

    // ========== HELPER METHODS ==========

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name)
        );
    }

    private static ResourceKey<PlacedFeature> createPlacedKey(String name) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name)
        );
    }
}