package nadiendev.ultimatefoods.fisura.cacerola;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * JEI (opcional): las recetas de la Cacerola Essen. Se ven los 6 ingredientes, el cuenco para servir,
 * el plato que sale (con las porciones) y el tiempo de cocion. Hace falta fuego debajo de la cacerola.
 * <p>
 * En 26.1 el cliente no tiene las recetas: las que se muestran son las que el servidor manda al
 * sincronizar los datapacks (ver {@link CacerolaRecetas}).
 */
@JeiPlugin
public class CacerolaJei implements IModPlugin {

    private static final Identifier UID = Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacerola");

    private static IRecipeHolderType<CacerolaRecipe> tipo;

    private static IRecipeHolderType<CacerolaRecipe> tipo() {
        if (tipo == null) {
            tipo = IRecipeHolderType.create(CacerolaRegistry.CACEROLA_TYPE.get());
        }
        return tipo;
    }

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new Categoria(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(tipo(), new ArrayList<>(CacerolaRecetas.delCliente()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(tipo(), new ItemStack(CacerolaRegistry.CACEROLA_ESSEN_ITEM.get()));
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
        public IRecipeHolderType<CacerolaRecipe> getRecipeType() {
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
                builder.addSlot(RecipeIngredientRole.INPUT, (i % 3) * 18, (i / 3) * 18).add(ingredientes.get(i));
            }
            ItemStack recipiente = receta.getContainer();
            if (!recipiente.isEmpty()) {
                // En JEI 29 el antiguo rol CATALYST se llama CRAFTING_STATION.
                builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 100, 34).add(recipiente);
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 8).add(receta.getResult());
        }

        @Override
        public void draw(RecipeHolder<CacerolaRecipe> holder, IRecipeSlotsView slots, GuiGraphicsExtractor graphics,
                         double mouseX, double mouseY) {
            CacerolaRecipe receta = holder.value();
            flecha.draw(graphics, 66, 10);
            var fuente = Minecraft.getInstance().font;
            graphics.text(fuente, Component.translatable("jei.ultimatefoods.cacerola.tiempo",
                    receta.getCookingTime() / 20), 62, 32, 0xFF808080, false);
            graphics.text(fuente, Component.translatable("jei.ultimatefoods.cacerola.calor"), 0, 44, 0xFF808080, false);
        }
    }
}
