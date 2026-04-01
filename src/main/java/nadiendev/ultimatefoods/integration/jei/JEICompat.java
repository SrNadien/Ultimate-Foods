package nadiendev.ultimatefoods.integration.jei;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ItemsAdds;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * JEICompat
 * By NadienDev
 */
@JeiPlugin
public class JEICompat implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "jei_plugin");
    }

    // Registrar la categoría visual de la Netherite Dagger
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new NetheriteDaggerCategory(guiHelper));
    }

    // Registrar las recetas de cada categoría + info descriptiva
    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        // ── Categoría visual: Netherite Dagger Ritual ──
        registration.addRecipes(
                NetheriteDaggerCategory.RECIPE_TYPE,
                List.of(NetheriteDaggerRecipe.INSTANCE)
        );

        // ── Info descriptiva ──

        // Monster
        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.MONSTER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.monster.info")
        );

        // Cajita Feliz
        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.CAJITA_FELIZ.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.cajita_feliz.info")
        );

        // Super Energy Drink
        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.SUPER_ENERGY_DRINK.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.super_energy_drink.info")
        );

        // Doritos
        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.DORITOS.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.doritos.info")
        );

        // Netherite Dagger — texto de apoyo además de la vista visual
        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.NETHERITE_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.netherite_dagger.info")
        );
    }
}