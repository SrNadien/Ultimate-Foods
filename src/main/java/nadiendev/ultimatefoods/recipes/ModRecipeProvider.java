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
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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

       
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ItemsAdds.RAW_HAMBURGUER_MEAT.get()),
                RecipeCategory.FOOD, ItemsAdds.COOKED_HAMBURGUER_MEAT.get(), 0.35f, 200)
                .unlockedBy("has_raw_hamburguer_meat", has(ItemsAdds.RAW_HAMBURGUER_MEAT.get()))
                .save(recipeOutput, rl("cooked_hamburguer_meat_furnace"));

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

       
        SimpleCookingRecipeBuilder.smoking(
                Ingredient.of(ItemsAdds.RAW_HAMBURGUER_MEAT.get()),
                RecipeCategory.FOOD, ItemsAdds.COOKED_HAMBURGUER_MEAT.get(), 0.35f, 100)
                .unlockedBy("has_patty_raw", has(ItemsAdds.RAW_HAMBURGUER_MEAT.get()))
                .save(recipeOutput, rl("cooked_hamburguer_meat_smoker"));

        // ==========================================
        // HOGUERA
        // ==========================================
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

        // ==========================================
        // CRAFTEO NORMAL
        // ==========================================

        // CARNE DE HAMBURGUESA CRUDA
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.RAW_HAMBURGUER_MEAT.get(), 1)
                .pattern(" O ")
                .pattern("PKV")
                .pattern(" O ")
                .define('P', Items.PORKCHOP)
                .define('O', Items.MUTTON)
                .define('K', ItemsAdds.NETHERITE_DAGGER.get())
                .define('V', Items.BEEF)
                .unlockedBy("has_beef", has(Items.BEEF))
                .save(recipeOutput, rl("patty_raw_recipe"));

         //daga de netherite
          ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemsAdds.NETHERITE_DAGGER.get(), 1)
                .pattern("   ")
                .pattern(" K ")
                .pattern(" O ")
                .define('K', Items.NETHERITE_INGOT)
                .define('O', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(recipeOutput, rl("daga_netherita"));


        // HAMBURGUESA
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.BURGER.get(), 1)
                .pattern(" B ")
                .pattern("CMG")
                .pattern(" B ")
                .define('B', Items.BREAD)
                .define('C', Items.GOLDEN_CARROT)
                .define('M', ItemsAdds.COOKED_HAMBURGUER_MEAT.get())
                .define('G', Items.GLISTERING_MELON_SLICE)
                .unlockedBy("has_patty_cooked", has(ItemsAdds.COOKED_HAMBURGUER_MEAT.get()))
                .save(recipeOutput, rl("hamburguesa_recipe"));

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

        // MONSTER — bebida energética
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.MONSTER.get(), 1)
                .pattern(" a ").pattern("bcd").pattern(" a ")
                .define('a', Items.SUGAR)
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.GLASS_BOTTLE)
                .define('d', Items.MELON_SLICE)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput, rl("monster_recipe"));

        // SUPER ENERGY DRINK — bebida con múltiples efectos
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.SUPER_ENERGY_DRINK.get(), 1)
                .pattern("aba").pattern("cdc").pattern("aba")
                .define('a', Items.BLAZE_POWDER)
                .define('b', Items.GHAST_TEAR)
                .define('c', Items.MAGMA_CREAM)
                .define('d', ItemsAdds.MONSTER.get())
                .unlockedBy("has_monster", has(ItemsAdds.MONSTER.get()))
                .save(recipeOutput, rl("super_energy_drink_recipe"));

        // SUPER POOP — poop rodeada de trigo
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.SUPERPOOP.get(), 1)
                .pattern("aaa").pattern("aba").pattern("aaa")
                .define('a', Items.WHEAT)
                .define('b', ItemsAdds.POOP.get())
                .unlockedBy("has_poop", has(ItemsAdds.POOP.get()))
                .save(recipeOutput, rl("superpoop_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksAdds.NADIENITE_BLOCK.get(), 1)
                .pattern("aaa").pattern("aaa").pattern("aaa")
                .define('a', ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_block_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksAdds.NETHER_STAR_BLOCK.get(), 1)
                .pattern("aaa").pattern("aaa").pattern("aaa")
                .define('a', Items.NETHER_STAR)
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(recipeOutput, rl("nether_star_block"));

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
        
        // NADIENITE PICKAXE
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsAdds.NADIENITE_PICKAXE.get(), 1)
        .pattern("NNN")
        .pattern(" S ")
        .pattern(" S ")
        .define('N', ItemsAdds.NADIENITE_INGOT.get())
        .define('S', Items.STICK)
        .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_pickaxe"));

        // NADIENITE AXE
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsAdds.NADIENITE_AXE.get(), 1)
                .pattern("NN ")
                .pattern("NS ")
                .pattern(" S ")
                .define('N', ItemsAdds.NADIENITE_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_axe"));

        // NADIENITE SHOVEL
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsAdds.NADIENITE_SHOVEL.get(), 1)
                .pattern(" N ")
                .pattern(" S ")
                .pattern(" S ")
                .define('N', ItemsAdds.NADIENITE_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_shovel"));

        // NADIENITE HOE
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsAdds.NADIENITE_HOE.get(), 1)
                .pattern("NN ")
                .pattern(" S ")
                .pattern(" S ")
                .define('N', ItemsAdds.NADIENITE_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_hoe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ToolsAdds.CHANCLA.get(), 1)
                .pattern(" C ").pattern(" N ").pattern(" S ")
                .define('N', ToolsAdds.NADIENITE_SWORD.get())
                .define('S', Items.STICK)
                .define('C', Items.NETHER_STAR)
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("chancleta"));

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
        
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.NADIENITE_INGOT.get(), 9)
                .pattern("   ").pattern(" a ").pattern("   ")
                .define('a', BlocksAdds.NADIENITE_BLOCK.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("ligote_de_nadienite_desde_bloque"));


        // NADIENITE NUGGET 
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemsAdds.NADIENITE_NUGGET.get(), 9)
                .requires(ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, rl("nadienite_nugget_from_ingot"));

                 ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsAdds.NADIENITE_INGOT.get(), 1)
                .pattern("aaa").pattern("aaa").pattern("aaa")
                .define('a', ItemsAdds.NADIENITE_NUGGET.get())
                .unlockedBy("has_nadienite_nugget", has(ItemsAdds.NADIENITE_NUGGET.get()))
                .save(recipeOutput, rl("nadienite_ingot_from_nuggets"));
        // ==========================================
        // RECETAS AVARITIA — EXTREME CRAFTING TABLE
        // ==========================================
        if (ModList.get().isLoaded("avaritia")
                && BuiltInRegistries.ITEM.containsKey(ResourceLocation.fromNamespaceAndPath("ultimatefoods", "infinity_1_sword"))) {

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