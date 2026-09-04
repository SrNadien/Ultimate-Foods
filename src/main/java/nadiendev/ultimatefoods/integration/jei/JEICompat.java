package nadiendev.ultimatefoods.integration.jei;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ModTier;
import nadiendev.ultimatefoods.config.ModConfigs;
import nadiendev.ultimatefoods.registry.ArmorAdds;
import nadiendev.ultimatefoods.registry.HammerAdds;
import nadiendev.ultimatefoods.registry.MeshAdds;
import nadiendev.ultimatefoods.registry.BlocksAdds;
import nadiendev.ultimatefoods.registry.ItemsAdds;
import nadiendev.ultimatefoods.registry.ModOreBlocks;
import nadiendev.ultimatefoods.registry.ToolsAdds;
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
        registration.addRecipeCategories(new NetheriteDaggerCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        registration.addRecipes(
                NetheriteDaggerCategory.RECIPE_TYPE,
                List.of(NetheriteDaggerRecipe.INSTANCE)
        );

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

        registration.addIngredientInfo(
                new ItemStack(ItemsAdds.NETHERITE_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.ultimatefoods.netherite_dagger.info")
        );

        for (ModTier tier : ModTier.values()) {
            String id = tier.id();

            info(registration, ArmorAdds.piecesOf(tier), "jei.ultimatefoods.armor." + id + ".info");
            info(registration, ToolsAdds.toolsOf(tier), "jei.ultimatefoods.tools." + id + ".info");

            info(registration, List.of(
                    ItemsAdds.ingotOf(tier),
                    ItemsAdds.nuggetOf(tier),
                    ItemsAdds.rawOreOf(tier)
            ), "jei.ultimatefoods.material." + id + ".info");

            registration.addIngredientInfo(
                    List.of(
                            new ItemStack(ModOreBlocks.stoneOreOf(tier).get()),
                            new ItemStack(ModOreBlocks.deepslateOreOf(tier).get()),
                            new ItemStack(BlocksAdds.storageBlockOf(tier).get())
                    ),
                    VanillaTypes.ITEM_STACK,
                    Component.translatable("jei.ultimatefoods.material." + id + ".info")
            );

            info(registration, List.of(ItemsAdds.heartOf(tier)), "jei.ultimatefoods.heart." + id + ".info");
        }

        info(registration, List.of(ItemsAdds.STEEL_NUGGET), "jei.ultimatefoods.steel_nugget.info");
        info(registration, List.of(ItemsAdds.NETHERITE_NUGGET), "jei.ultimatefoods.netherite_nugget.info");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (!MeshAdds.sieveModPresent() || !ModConfigs.exDeorumEnabled()) {
            return;
        }

        IJeiHelpers helpers = registration.getJeiHelpers();

        ItemLike[] meshes = MeshAdds.all().stream().map(DeferredHolder::get).toArray(ItemLike[]::new);
        catalysts(registration, helpers, "sieve", meshes);
        catalysts(registration, helpers, "compressed_sieve", meshes);

        catalysts(registration, helpers, "hammer", hammers(false));
        catalysts(registration, helpers, "compressed_hammer", hammers(true));
    }

    private static ItemLike[] hammers(boolean compressed) {
        return HammerAdds.all().stream()
                .filter(holder -> holder.getId().getPath().startsWith("compressed_") == compressed)
                .map(DeferredHolder::get)
                .toArray(ItemLike[]::new);
    }

    private static void catalysts(IRecipeCatalystRegistration registration, IJeiHelpers helpers,
                                  String type, ItemLike... items) {
        if (items.length == 0) {
            return;
        }
        helpers.getRecipeType(ResourceLocation.fromNamespaceAndPath("exdeorum", type))
                .ifPresent(recipeType -> registration.addRecipeCatalysts(recipeType, items));
    }

    private static void info(IRecipeRegistration registration,
                             List<DeferredHolder<Item, Item>> items, String translationKey) {
        registration.addIngredientInfo(
                items.stream().map(holder -> new ItemStack(holder.get())).toList(),
                VanillaTypes.ITEM_STACK,
                Component.translatable(translationKey)
        );
    }
}
