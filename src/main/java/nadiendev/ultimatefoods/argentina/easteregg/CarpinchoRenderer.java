package nadiendev.ultimatefoods.argentina.easteregg;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;

import java.text.Normalizer;
import java.util.Locale;

/** Un cerdo renombrado "carpincho" con una etiqueta se ve como el roedor mas grande del mundo. */
public class CarpinchoRenderer extends PigRenderer {

    private static final ResourceLocation CARPINCHO =
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "textures/entity/pig/carpincho.png");

    public CarpinchoRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Pig pig) {
        return esCarpincho(pig) ? CARPINCHO : super.getTextureLocation(pig);
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
