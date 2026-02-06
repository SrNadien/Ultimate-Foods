package nadiendev.ultimatefoods.integration.jei;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ItemsAdds;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
     
       // Agregar información descriptiva para los ítems en JEI
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
 
        
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

    }
}