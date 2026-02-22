package nadiendev.ultimatefoods.recipes;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.items.armor.ArmorAdds;
import nadiendev.ultimatefoods.items.tools.ToolsAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import nadiendev.ultimatefoods.avaritia.AvaritiaToolsAdds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.conditions.ICondition;
import committee.nova.mods.avaritia.init.data.provider.recipe.ModShapedRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput writer) {

        // Wrapper para evitar advancements automáticos
        RecipeOutput recipeOutput = new RecipeOutput() {
            @Override
            public void accept(ResourceLocation id, net.minecraft.world.item.crafting.Recipe<?> recipe,
                               net.minecraft.advancements.AdvancementHolder advancement) {
                writer.accept(id, recipe, null);
            }
            @Override
            public net.minecraft.advancements.Advancement.Builder advancement() {
                return writer.advancement();
            }
            @Override
            public void accept(ResourceLocation id, net.minecraft.world.item.crafting.Recipe<?> recipe,
                               net.minecraft.advancements.AdvancementHolder advancement, ICondition... conditions) {
                writer.accept(id, recipe, null, conditions);
            }
        };

        // ==========================================
        // HORNO
        // ==========================================
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
                RecipeCategory.MISC, ItemsAdds.TORTILLA.get(), 1.0f, 200)
                .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
                .save(recipeOutput, rl("tortilla_cruda_horno"));

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(NadieniteOreBlock.NADIENITE_ORE.get()),
                RecipeCategory.MISC, ItemsAdds.NADIENITE_INGOT.get(), 1.0f, 200)
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(recipeOutput, rl("nadienite_ingot_furnace"));

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get()),
                RecipeCategory.MISC, ItemsAdds.NADIENITE_INGOT.get(), 1.0f, 200)
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(recipeOutput, rl("nadienite_ingot_furnace_alt"));

        // ==========================================
        // ALTO HORNO
        // ==========================================
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(NadieniteOreBlock.NADIENITE_ORE.get()),
                RecipeCategory.MISC, ItemsAdds.NADIENITE_INGOT.get(), 1.0f, 100)
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(recipeOutput, rl("nadienite_ingot_blastfurnace"));

        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get()),
                RecipeCategory.MISC, ItemsAdds.NADIENITE_INGOT.get(), 1.0f, 100)
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(recipeOutput, rl("nadienite_ingot_blastfurnace_alt"));

        // ==========================================
        // AHUMADOR
        // ==========================================
        SimpleCookingRecipeBuilder.smoking(
                Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
                RecipeCategory.MISC, ItemsAdds.TORTILLA.get(), 1.0f, 100)
                .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
                .save(recipeOutput, rl("tortilla_cruda_ahumador"));

        // ==========================================
        // HOGUERA
        // ==========================================
        SimpleCookingRecipeBuilder.campfireCooking(
                Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
                RecipeCategory.MISC, ItemsAdds.TORTILLA.get(), 1.0f, 600)
                .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
                .save(recipeOutput, rl("tortilla_cruda_hoguera"));

        // ==========================================
        // CRAFTEO NORMAL
        // ==========================================
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.DORITOS.get(), 1)
                .pattern("aba").pattern("bcb").pattern("aba")
                .define('a', Items.WHEAT).define('b', Items.BAKED_POTATO).define('c', Items.BUCKET)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(recipeOutput, rl("doritos_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.CAJITA_FELIZ.get(), 1)
                .pattern(" a ").pattern("bcd")
                .define('a', Items.APPLE).define('b', Items.BAKED_POTATO)
                .define('c', Items.CHEST).define('d', Items.COOKED_CHICKEN)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput, rl("cajita_feliz_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.POOP.get(), 1)
                .pattern("aaa").pattern("aba").pattern("aaa")
                .define('a', Items.ROTTEN_FLESH).define('b', Items.GOLDEN_APPLE)
                .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
                .save(recipeOutput, rl("poop_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksAdds.NADIENITE_BLOCK.get(), 1)
                .pattern("aaa").pattern("aaa").pattern("aaa")
                .define('a', ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_block_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_HELMET.get(), 1)
                .pattern("NNN").pattern("N N")
                .define('N', ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_helmet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_CHESTPLATE.get(), 1)
                .pattern("N N").pattern("NNN").pattern("NNN")
                .define('N', ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_chestplate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_LEGGINGS.get(), 1)
                .pattern("NNN").pattern("N N").pattern("N N")
                .define('N', ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_leggings"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_BOOTS.get(), 1)
                .pattern("N N").pattern("N N")
                .define('N', ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_boots"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ToolsAdds.NADIENITE_SWORD.get(), 1)
                .pattern(" N ").pattern(" N ").pattern(" S ")
                .define('N', ItemsAdds.NADIENITE_INGOT.get()).define('S', Items.STICK)
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_sword"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ToolsAdds.CHANCLA.get(), 1)
                .pattern(" C ").pattern(" N ").pattern(" S ")
                .define('N', ToolsAdds.NADIENITE_SWORD.get())
                .define('S', Items.STICK)
                .define('C', Items.NETHER_STAR)
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("chancleta"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.SUPER_ENERGY_DRINK.get(), 1)
                .pattern("ab").pattern("cd").pattern("e ")
                .define('a', ItemsAdds.CAJITA_FELIZ.get()).define('b', ItemsAdds.SUPER_CHILE.get())
                .define('c', ItemsAdds.CHOCOLATADA.get()).define('d', Items.CAKE).define('e', Items.DIAMOND)
                .unlockedBy("has_cajita_feliz", has(ItemsAdds.CAJITA_FELIZ.get()))
                .save(recipeOutput, rl("super_energy_drink_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.TACO.get(), 1)
                .pattern("aaa").pattern("bcd").pattern("aaa")
                .define('a', ItemsAdds.TORTILLA.get()).define('b', Items.GOLDEN_CARROT)
                .define('c', Items.COOKED_BEEF).define('d', Items.BEETROOT)
                .unlockedBy("has_cooked_beef", has(Items.COOKED_BEEF))
                .save(recipeOutput, rl("taco"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.BAKED_TORTILLA.get(), 1)
                .pattern("aaa").pattern("aba").pattern("aaa")
                .define('a', Items.WHEAT).define('b', Items.BREAD)
                .unlockedBy("has_bread", has(Items.BREAD))
                .save(recipeOutput, rl("tortilla_cruda"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.STEEL_INGOT.get(), 1)
                .pattern("aa ").pattern("bb ").pattern("   ")
                .define('a', Items.IRON_INGOT).define('b', Items.COAL)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput, rl("acero"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.STEEL_INGOT.get(), 9)
                .pattern("a  ").pattern("   ").pattern("   ")
                .define('a', BlocksAdds.STEEL_BLOCK.get())
                .unlockedBy("has_steel_block", has(BlocksAdds.STEEL_BLOCK.get()))
                .save(recipeOutput, rl("acero_desde_bloque"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksAdds.STEEL_BLOCK.get(), 1)
                .pattern("aaa").pattern("aaa").pattern("aaa")
                .define('a', ItemsAdds.STEEL_INGOT.get())
                .unlockedBy("has_steel_ingot", has(ItemsAdds.STEEL_INGOT.get()))
                .save(recipeOutput, rl("bloque_de_acero"));

        // ==========================================
        // RECETAS AVARITIA — EXTREME CRAFTING TABLE
        // tier 4 = Extreme Crafting Table (9x9)
        // Usamos ModShapedRecipeBuilder.shaped(category, ItemLike, tier)
        // ==========================================
        if (ModList.get().isLoaded("avaritia")
                && BuiltInRegistries.ITEM.containsKey(ResourceLocation.fromNamespaceAndPath("ultimatefoods", "infinity_1_sword"))) {

            // ESPADA INFINITY-1
            ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AvaritiaToolsAdds.INFINITY_1_SWORD.get(), 4)
                    .pattern("       II")
                    .pattern("      III")
                    .pattern("     III ")
                    .pattern("    III  ")
                    .pattern(" C III   ")
                    .pattern("  CII    ")
                    .pattern("  NC     ")
                    .pattern(" N  C    ")
                    .pattern("X        ")
                    .define('C', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix_ingot")))
                    .define('I', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix")))
                    .define('N', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "neutron")))
                    .define('X', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix")))
                    .unlockedBy("has_item", has(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix_ingot"))))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "avaritiacompat/infinity_1_sword"));

            // ESPADA INFINITY BALANCED
            ModShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AvaritiaToolsAdds.INFINITY_SWORD_BALANCED.get(), 4)
                    .pattern("       II")
                    .pattern("      III")
                    .pattern("     III ")
                    .pattern("    III  ")
                    .pattern(" C III   ")
                    .pattern("  CII    ")
                    .pattern("  NC     ")
                    .pattern(" N  C    ")
                    .pattern("X        ")
                    .define('C', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix_ingot")))
                    .define('I', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "infinity_ingot")))
                    .define('N', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "neutron_ingot")))
                    .define('X', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "infinity_catalyst")))
                    .unlockedBy("has_item", has(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "infinity_ingot"))))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "avaritiacompat/infinity_sword_balanced"));

            // PICO INFINITY-1
            ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AvaritiaToolsAdds.INFINITY_1_PICKAXE.get(), 4)
                    .pattern("CICICICIC")
                    .pattern(" CICICIC ")
                    .pattern("    I    ")
                    .pattern("    N    ")
                    .pattern("    N    ")
                    .pattern("    N    ")
                    .pattern("    N    ")
                    .pattern("    N    ")
                    .pattern("    X    ")
                    .define('C', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix_ingot")))
                    .define('I', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix")))
                    .define('N', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "neutron")))
                    .define('X', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix")))
                    .unlockedBy("has_item", has(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix_ingot"))))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "avaritiacompat/infinity_1_pickaxe"));

            // AZADA INFINITY-1
            ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AvaritiaToolsAdds.INFINITY_1_HOE.get(), 4)
                    .pattern("    CICIC")
                    .pattern("    ICICI")
                    .pattern("    IC   ")
                    .pattern("     N   ")
                    .pattern("     N   ")
                    .pattern("    NN   ")
                    .pattern("   NN    ")
                    .pattern("  NN     ")
                    .pattern(" X       ")
                    .define('C', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix_ingot")))
                    .define('I', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix")))
                    .define('N', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "neutron")))
                    .define('X', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix")))
                    .unlockedBy("has_item", has(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("avaritia", "crystal_matrix_ingot"))))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "avaritiacompat/infinity_1_hoe"));
        }
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, path);
    }
}