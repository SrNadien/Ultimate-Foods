package nadiendev.ultimatefoods.fisura.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

/**
 * Lo de mundo del arbol de yerba mate: las claves de la feature, las placed features y los
 * biome modifiers, y el {@link TreeGrower} del brote. Los JSON (arbolito de tronco recto
 * 4+2 y copa redonda tupida; jungla 1 de cada 8 chunks, pantanos y bosques 1 de cada 32)
 * van como datos estaticos en {@code data/ultimatefoods/worldgen} y
 * {@code data/ultimatefoods/neoforge/biome_modifier}.
 */
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

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, path);
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
}
