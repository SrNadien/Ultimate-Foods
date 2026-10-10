package nadiendev.ultimatefoods.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.registry.ItemsAdds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEICompat implements IModPlugin {

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "jei_plugin");
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
