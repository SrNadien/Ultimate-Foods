package nadiendev.ultimatefoods.argentina.cacerola;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/** Builder de datagen para {@link CacerolaRecipe}. */
public class CacerolaRecipeBuilder implements RecipeBuilder {

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final ItemLike result;
    private final int portions;
    private ItemStack container = new ItemStack(Items.BOWL);
    private int cookingTime = CacerolaRecipe.DEFAULT_COOKING_TIME;
    private float experience = 1.0F;

    private CacerolaRecipeBuilder(ItemLike result, int portions) {
        this.result = result;
        this.portions = portions;
    }

    /** {@code portions} es cuantas porciones rinde la olla (cuantos platos se pueden servir). */
    public static CacerolaRecipeBuilder cacerola(ItemLike result, int portions) {
        return new CacerolaRecipeBuilder(result, portions);
    }

    public CacerolaRecipeBuilder requires(ItemLike item) {
        return requires(Ingredient.of(item));
    }

    public CacerolaRecipeBuilder requires(TagKey<Item> tag) {
        return requires(Ingredient.of(tag));
    }

    public CacerolaRecipeBuilder requires(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public CacerolaRecipeBuilder container(ItemLike container) {
        this.container = new ItemStack(container);
        return this;
    }

    public CacerolaRecipeBuilder cookingTime(int ticks) {
        this.cookingTime = ticks;
        return this;
    }

    public CacerolaRecipeBuilder experience(float experience) {
        this.experience = experience;
        return this;
    }

    // Las recetas de la cacerola no pasan por el libro de recetas: no hay logros que desbloquear.
    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return result.asItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        if (ingredients.isEmpty() || ingredients.size() > CacerolaRecipe.MAX_INGREDIENTS) {
            throw new IllegalStateException("La receta " + id + " tiene " + ingredients.size()
                    + " ingredientes; la cacerola admite de 1 a " + CacerolaRecipe.MAX_INGREDIENTS);
        }
        CacerolaRecipe recipe = new CacerolaRecipe(
                ingredients, new ItemStack(result, portions), container, cookingTime, experience);
        output.accept(id, recipe, null);
    }
}
