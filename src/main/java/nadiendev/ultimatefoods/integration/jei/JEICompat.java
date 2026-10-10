package nadiendev.ultimatefoods.integration.jei;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.config.ModConfigs;
import nadiendev.ultimatefoods.registry.ItemsAdds;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;

@JeiPlugin
public class JEICompat implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.MONSTER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.monster.info")
        );

        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.CAJITA_FELIZ.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.cajita_feliz.info")
        );

        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.SUPER_ENERGY_DRINK.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.super_energy_drink.info")
        );

        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.DORITOS.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.doritos.info")
        );

    }

}
