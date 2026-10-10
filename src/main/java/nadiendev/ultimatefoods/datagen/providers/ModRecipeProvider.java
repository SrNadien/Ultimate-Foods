package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.registry.ItemsAdds;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        public String getName() {
            return "Recipes: " + UltimateFoodsCore.MOD_ID;
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }
    }

    @Override
    protected void buildRecipes() {
        RecipeOutput writer = this.output;
        RecipeOutput recipeOutput = writer;


        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
                RecipeCategory.MISC, net.minecraft.world.item.crafting.CookingBookCategory.MISC, ItemsAdds.TORTILLA.get(), 1.0f, 200)
                .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
                .save(recipeOutput, rl("tortilla_cruda_horno"));

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ItemsAdds.RAW_HAMBURGUER_MEAT.get()),
                RecipeCategory.FOOD, net.minecraft.world.item.crafting.CookingBookCategory.MISC, ItemsAdds.COOKED_HAMBURGUER_MEAT.get(), 0.35f, 200)
                .unlockedBy("has_raw_hamburguer_meat", has(ItemsAdds.RAW_HAMBURGUER_MEAT.get()))
                .save(recipeOutput, rl("cooked_hamburguer_meat_furnace"));

        SimpleCookingRecipeBuilder.smoking(
                Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
                RecipeCategory.MISC, ItemsAdds.TORTILLA.get(), 1.0f, 100)
                .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
                .save(recipeOutput, rl("tortilla_cruda_ahumador"));

        SimpleCookingRecipeBuilder.smoking(
                Ingredient.of(ItemsAdds.RAW_HAMBURGUER_MEAT.get()),
                RecipeCategory.FOOD, ItemsAdds.COOKED_HAMBURGUER_MEAT.get(), 0.35f, 100)
                .unlockedBy("has_patty_raw", has(ItemsAdds.RAW_HAMBURGUER_MEAT.get()))
                .save(recipeOutput, rl("cooked_hamburguer_meat_smoker"));

        SimpleCookingRecipeBuilder.campfireCooking(
                Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
                RecipeCategory.MISC, ItemsAdds.TORTILLA.get(), 1.0f, 600)
                .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
                .save(recipeOutput, rl("tortilla_cruda_hoguera"));

        SimpleCookingRecipeBuilder.campfireCooking(
                Ingredient.of(ItemsAdds.RAW_HAMBURGUER_MEAT.get()),
                RecipeCategory.FOOD, ItemsAdds.COOKED_HAMBURGUER_MEAT.get(), 0.35f, 600)
                .unlockedBy("has_patty_raw", has(ItemsAdds.RAW_HAMBURGUER_MEAT.get()))
                .save(recipeOutput, rl("cooked_hamburguer_meat_campfire"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.BURGER.get(), 1)
                .pattern(" B ")
                .pattern("CMG")
                .pattern(" B ")
                .define('B', Items.BREAD)
                .define('C', Items.GOLDEN_CARROT)
                .define('M', ItemsAdds.COOKED_HAMBURGUER_MEAT.get())
                .define('G', Items.GLISTERING_MELON_SLICE)
                .unlockedBy("has_patty_cooked", has(ItemsAdds.COOKED_HAMBURGUER_MEAT.get()))
                .save(recipeOutput, rl("hamburguesa_recipe"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.DORITOS.get(), 1)
                .pattern("aba").pattern("bcb").pattern("aba")
                .define('a', Items.WHEAT).define('b', Items.BAKED_POTATO).define('c', Items.BUCKET)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(recipeOutput, rl("doritos_recipe"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.CAJITA_FELIZ.get(), 1)
                .pattern(" a ").pattern("bcd")
                .define('a', Items.APPLE).define('b', Items.BAKED_POTATO)
                .define('c', Items.CHEST).define('d', Items.COOKED_CHICKEN)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput, rl("cajita_feliz_recipe"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.POOP.get(), 1)
                .pattern("aaa").pattern("aba").pattern("aaa")
                .define('a', Items.ROTTEN_FLESH).define('b', Items.GOLDEN_APPLE)
                .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
                .save(recipeOutput, rl("poop_recipe"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.MONSTER.get(), 1)
                .pattern(" a ").pattern("bcd").pattern(" a ")
                .define('a', Items.SUGAR)
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.GLASS_BOTTLE)
                .define('d', Items.MELON_SLICE)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput, rl("monster_recipe"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.SUPER_ENERGY_DRINK.get(), 1)
                .pattern("aba").pattern("cdc").pattern("aba")
                .define('a', Items.BLAZE_POWDER)
                .define('b', Items.GHAST_TEAR)
                .define('c', Items.MAGMA_CREAM)
                .define('d', ItemsAdds.MONSTER.get())
                .unlockedBy("has_monster", has(ItemsAdds.MONSTER.get()))
                .save(recipeOutput, rl("super_energy_drink_recipe"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.SUPERPOOP.get(), 1)
                .pattern("aaa").pattern("aba").pattern("aaa")
                .define('a', Items.WHEAT)
                .define('b', ItemsAdds.POOP.get())
                .unlockedBy("has_poop", has(ItemsAdds.POOP.get()))
                .save(recipeOutput, rl("superpoop_recipe"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.TACO.get(), 1)
                .pattern("aaa").pattern("bcd").pattern("aaa")
                .define('a', ItemsAdds.TORTILLA.get()).define('b', Items.GOLDEN_CARROT)
                .define('c', Items.COOKED_BEEF).define('d', Items.BEETROOT)
                .unlockedBy("has_cooked_beef", has(Items.COOKED_BEEF))
                .save(recipeOutput, rl("taco"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.BAKED_TORTILLA.get(), 1)
                .pattern("aaa").pattern("aba").pattern("aaa")
                .define('a', Items.WHEAT).define('b', Items.BREAD)
                .unlockedBy("has_bread", has(Items.BREAD))
                .save(recipeOutput, rl("tortilla_cruda"));

        // cuchillos de Farmer's Delight: solo se referencia el tag, sin depender de sus clases
        TagKey<Item> knives = ItemTags.create(Identifier.parse("farmersdelight:tools/knives"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, ItemsAdds.RAW_HAMBURGUER_MEAT.get(), 1)
                .pattern(" O ")
                .pattern("PKV")
                .pattern(" O ")
                .define('P', Items.PORKCHOP)
                .define('O', Items.MUTTON)
                .define('K', knives)
                .define('V', Items.BEEF)
                .unlockedBy("has_beef", has(Items.BEEF))
                .save(recipeOutput, rl("patty_raw_recipe"));
    }

    private static net.minecraft.resources.ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> rl(String path) {
        return net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE,
                Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, path));
    }
}
