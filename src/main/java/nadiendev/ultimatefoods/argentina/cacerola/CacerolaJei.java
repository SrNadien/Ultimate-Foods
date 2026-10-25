package nadiendev.ultimatefoods.argentina.cacerola;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

/**
 * JEI (opcional): las recetas de la Cacerola Essen. Se ven los 6 ingredientes, el cuenco para servir,
 * el plato que sale (con las porciones) y el tiempo de cocion. Hace falta fuego debajo de la cacerola.
 */
@JeiPlugin
public class CacerolaJei implements IModPlugin {

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacerola");

    private static RecipeType<RecipeHolder<CacerolaRecipe>> tipo() {
        return RecipeType.createFromVanilla(CacerolaRegistry.CACEROLA_TYPE.get());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new Categoria(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level != null) {
            registration.addRecipes(tipo(), level.getRecipeManager().getAllRecipesFor(CacerolaRegistry.CACEROLA_TYPE.get()));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CacerolaRegistry.CACEROLA_ESSEN_ITEM.get()), tipo());
    }

    private static final class Categoria implements IRecipeCategory<RecipeHolder<CacerolaRecipe>> {

        private static final int ANCHO = 124;
        private static final int ALTO = 56;

        private final IDrawable icono;
        private final IDrawableAnimated flecha;

        Categoria(IGuiHelper gui) {
            this.icono = gui.createDrawableItemStack(new ItemStack(CacerolaRegistry.CACEROLA_ESSEN_ITEM.get()));
            this.flecha = gui.createAnimatedRecipeArrow(200);
        }

        @Override
        public RecipeType<RecipeHolder<CacerolaRecipe>> getRecipeType() {
            return tipo();
        }

        @Override
        public Component getTitle() {
            return Component.translatable("block.ultimatefoods.cacerola_essen");
        }

        @Override
        public IDrawable getIcon() {
            return icono;
        }

        @Override
        public int getWidth() {
            return ANCHO;
        }

        @Override
        public int getHeight() {
            return ALTO;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CacerolaRecipe> holder, IFocusGroup focuses) {
            CacerolaRecipe receta = holder.value();
            List<Ingredient> ingredientes = receta.getIngredients();
            for (int i = 0; i < ingredientes.size(); i++) {
                builder.addSlot(RecipeIngredientRole.INPUT, (i % 3) * 18, (i / 3) * 18).addIngredients(ingredientes.get(i));
            }
            if (!receta.getContainer().isEmpty()) {
                builder.addSlot(RecipeIngredientRole.CATALYST, 100, 34).addItemStack(receta.getContainer());
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 8).addItemStack(receta.getResult());
        }

        @Override
        public void draw(RecipeHolder<CacerolaRecipe> holder, IRecipeSlotsView slots, GuiGraphics graphics,
                         double mouseX, double mouseY) {
            CacerolaRecipe receta = holder.value();
            flecha.draw(graphics, 66, 10);
            var fuente = Minecraft.getInstance().font;
            graphics.drawString(fuente, Component.translatable("jei.ultimatefoods.cacerola.tiempo",
                    receta.getCookingTime() / 20), 62, 32, 0xFF808080, false);
            graphics.drawString(fuente, Component.translatable("jei.ultimatefoods.cacerola.calor"), 0, 44, 0xFF808080, false);
        }
    }
}
