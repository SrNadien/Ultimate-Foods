package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;

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

        basicItem(nadiendev.ultimatefoods.registry.ItemsAdds.RAW_HAMBURGUER_MEAT.get());
        basicItem(nadiendev.ultimatefoods.registry.ItemsAdds.COOKED_HAMBURGUER_MEAT.get());
        basicItem(nadiendev.ultimatefoods.registry.ItemsAdds.BURGER.get());

        withExistingParent(
                nadiendev.ultimatefoods.registry.ItemsAdds.NETHERITE_DAGGER.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/handheld")
        ).texture("layer0", ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/netherite_dagger"));

        basicItem(nadiendev.ultimatefoods.registry.ItemsAdds.NADIENITE_NUGGET.get());
    }
}
