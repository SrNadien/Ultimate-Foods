package nadiendev.ultimatefoods.fisura.easteregg;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Un cerdo renombrado "carpincho" con una etiqueta se ve como el roedor mas grande del mundo.
 * <p>
 * El cerdo ahora tiene variantes (templado, calido, frio) y el renderer vanilla saca el modelo y la
 * textura de la variante guardada en el render state. En vez de reemplazar el renderer del cerdo, se
 * engancha el render state ({@code RegisterRenderStateModifiersEvent}, ver {@link EasterEggsCliente}):
 * si el cerdo se llama carpincho, se le pone una variante solo visual con el modelo normal y la
 * textura del carpincho. Las crias tienen modelo propio, asi que conservan su textura de cria.
 */
public final class CarpinchoRenderer {

    private static final Identifier CARPINCHO_ID =
            Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "entity/pig/carpincho");
    private static final Identifier CARPINCHO =
            Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "textures/entity/pig/carpincho.png");

    /** El modelo normal del cerdo (el frio tiene otra forma) con la textura del carpincho. */
    private static final ModelAndTexture<PigVariant.ModelType> MODELO =
            new ModelAndTexture<>(PigVariant.ModelType.NORMAL, new ClientAsset.ResourceTexture(CARPINCHO_ID, CARPINCHO));

    private CarpinchoRenderer() {
    }

    /** Modificador del render state del cerdo: corre despues de que el renderer vanilla lo llena. */
    static void alExtraerEstado(Pig pig, PigRenderState estado) {
        PigVariant original = estado.variant;
        if (original == null || !esCarpincho(pig)) {
            return;
        }
        estado.variant = new PigVariant(MODELO, original.babyTexture(), original.spawnConditions());
    }

    static boolean esCarpincho(Pig pig) {
        if (!pig.hasCustomName()) {
            return false;
        }
        String nombre = Normalizer.normalize(pig.getCustomName().getString(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase(Locale.ROOT);
        return nombre.equals("carpincho");
    }
}
