package nadiendev.ultimatefoods.client;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.armor.WizardHatItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WizardHatRenderer extends GeoArmorRenderer<WizardHatItem> {

    public WizardHatRenderer() {
        super(new WizardHatGeoModel());
    }

    private static class WizardHatGeoModel extends GeoModel<WizardHatItem> {

        @Override
        public ResourceLocation getModelResource(WizardHatItem item) {
            return path("geo/wizard_hat.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(WizardHatItem item) {
            return path("textures/armor/" + item.modTier().id() + "_wizard_hat.png");
        }

        @Override
        public ResourceLocation getAnimationResource(WizardHatItem item) {
            return path("animations/wizard_hat.animation.json");
        }

        private static ResourceLocation path(String path) {
            return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, path);
        }
    }
}
