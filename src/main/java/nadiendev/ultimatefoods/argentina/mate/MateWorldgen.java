package nadiendev.ultimatefoods.argentina.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Optional;

/** Todo lo de mundo del arbol de yerba mate: feature, placed features y biome modifiers (por datagen). */
public final class MateWorldgen {

    public static final ResourceKey<ConfiguredFeature<?, ?>> YERBA_MATE_TREE = configured("yerba_mate_tree");

    public static final ResourceKey<PlacedFeature> YERBA_MATE_TREE_JUNGLE = placed("yerba_mate_tree_jungle");
    public static final ResourceKey<PlacedFeature> YERBA_MATE_TREE_SILVESTRE = placed("yerba_mate_tree_silvestre");

    public static final ResourceKey<BiomeModifier> ADD_EN_JUNGLA = modifier("yerba_mate_en_jungla");
    public static final ResourceKey<BiomeModifier> ADD_EN_PANTANO = modifier("yerba_mate_en_pantano");
    public static final ResourceKey<BiomeModifier> ADD_EN_BOSQUE = modifier("yerba_mate_en_bosque");

    /** Lo que usa el brote para crecer: apunta a la configured feature de arriba. */
    public static final TreeGrower TREE_GROWER = new TreeGrower("yerba_mate",
            Optional.empty(), Optional.of(YERBA_MATE_TREE), Optional.empty());

    private MateWorldgen() {
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, path);
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, id(name));
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, id(name));
    }

    private static ResourceKey<BiomeModifier> modifier(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id(name));
    }

    // ---------- bootstraps para el DatapackBuiltinEntriesProvider ----------

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Arbolito de tronco recto y copa redonda y bien tupida, como la yerba mate de monte.
        context.register(YERBA_MATE_TREE, new ConfiguredFeature<>(Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(MateBlocks.YERBA_MATE_LOG.get()),
                        new StraightTrunkPlacer(4, 2, 0),
                        BlockStateProvider.simple(MateBlocks.YERBA_MATE_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1))
                        .ignoreVines()
                        .build()));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(YERBA_MATE_TREE_JUNGLE, new PlacedFeature(features.getOrThrow(YERBA_MATE_TREE),
                arbol(RarityFilter.onAverageOnceEvery(8))));
        context.register(YERBA_MATE_TREE_SILVESTRE, new PlacedFeature(features.getOrThrow(YERBA_MATE_TREE),
                arbol(RarityFilter.onAverageOnceEvery(32))));
    }

    private static List<PlacementModifier> arbol(PlacementModifier rareza) {
        return List.of(
                rareza,
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome(),
                PlacementUtils.filteredByBlockSurvival(MateBlocks.YERBA_MATE_SAPLING.get()));
    }

    public static void bootstrapModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);

        HolderSet<PlacedFeature> jungla = HolderSet.direct(placed.getOrThrow(YERBA_MATE_TREE_JUNGLE));
        HolderSet<PlacedFeature> silvestre = HolderSet.direct(placed.getOrThrow(YERBA_MATE_TREE_SILVESTRE));

        // Calido y humedo: jungla un poco mas seguido; pantanos y bosques, muy de vez en cuando.
        context.register(ADD_EN_JUNGLA, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_JUNGLE), jungla, GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_EN_PANTANO, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_SWAMP), silvestre, GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_EN_BOSQUE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST), silvestre, GenerationStep.Decoration.VEGETAL_DECORATION));
    }
}
