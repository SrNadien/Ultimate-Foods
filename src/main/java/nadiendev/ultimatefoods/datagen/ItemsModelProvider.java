package nadiendev.ultimatefoods.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.avaritia.AvaritiaToolsAdds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemsModelProvider extends ItemModelProvider {

    public ItemsModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, UltimateFoodsCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        // ========== AVARITIA COMPAT ==========
        // Solo si los items están registrados
        if (ModList.get().isLoaded("avaritia")
                && BuiltInRegistries.ITEM.containsKey(
                        ResourceLocation.fromNamespaceAndPath("ultimatefoods", "infinity_1_sword"))) {

            // infinity_1_sword
            withExistingParent(
                    AvaritiaToolsAdds.INFINITY_1_SWORD.getId().getPath(),
                    ResourceLocation.withDefaultNamespace("item/handheld")
            ).texture("layer0", ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/infinitym1sword"));

            // infinity_sword_balanced
            withExistingParent(
                    AvaritiaToolsAdds.INFINITY_SWORD_BALANCED.getId().getPath(),
                    ResourceLocation.withDefaultNamespace("item/handheld")
            ).texture("layer0", ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/balancedinfinitysword"));

            // infinity_1_pickaxe
            withExistingParent(
                    AvaritiaToolsAdds.INFINITY_1_PICKAXE.getId().getPath(),
                    ResourceLocation.withDefaultNamespace("item/handheld")
            ).texture("layer0", ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/infinitym1pick"));

            // infinity_1_hoe
            withExistingParent(
                    AvaritiaToolsAdds.INFINITY_1_HOE.getId().getPath(),
                    ResourceLocation.withDefaultNamespace("item/handheld")
            ).texture("layer0", ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/infinitym1hoe"));
        }
    }
}