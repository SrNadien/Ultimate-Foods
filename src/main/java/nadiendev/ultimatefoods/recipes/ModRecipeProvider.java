package nadiendev.ultimatefoods.recipes;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.items.armor.ArmorAdds;
import nadiendev.ultimatefoods.items.tools.ToolsAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.concurrent.CompletableFuture;

/**
 * ModRecipeProvider
 * By NadienDev
 * Generador de recetas para el mod Ultimate Foods
 */
public class ModRecipeProvider extends RecipeProvider {
    
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    // Wrapper para evitar generar advancements automáticos
    @Override
    protected void buildRecipes(RecipeOutput writer) {
       
        RecipeOutput recipeOutput = new RecipeOutput() {
            @Override
            public void accept(ResourceLocation id, net.minecraft.world.item.crafting.Recipe<?> recipe, 
                             net.minecraft.advancements.AdvancementHolder advancement) {
                // Solo guardamos la receta, ignoramos el advancement
                writer.accept(id, recipe, null);
            }
            
            @Override
            public net.minecraft.advancements.Advancement.Builder advancement() {
                return writer.advancement();
            }

            @Override
            public void accept(ResourceLocation id, net.minecraft.world.item.crafting.Recipe<?> recipe, 
                             net.minecraft.advancements.AdvancementHolder advancement, ICondition... conditions) {
                // Solo guardamos la receta, ignoramos el advancement
                writer.accept(id, recipe, null, conditions);
            }
        };
          

        // ==========================================
        // RECETAS ULTIMATE FOODS HORNO
        // ==========================================

        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
            RecipeCategory.MISC,
            ItemsAdds.TORTILLA.get(),
            1.0f,
            200
        )
        .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "tortilla_cruda_horno"));

        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(NadieniteOreBlock.NADIENITE_ORE.get()),
            RecipeCategory.MISC,
            ItemsAdds.NADIENITE_INGOT.get(),
            1.0f,
            200
        )
        .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_ingot_furnace"));
        
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get()),
            RecipeCategory.MISC,
            ItemsAdds.NADIENITE_INGOT.get(),
            1.0f,
            200
        )
        .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_ingot_furnace_alt"));


         // ==========================================
        // RECETAS ULTIMATE FOODS ALTO HORNO
        // ==========================================
         SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(NadieniteOreBlock.NADIENITE_ORE.get()),
            RecipeCategory.MISC,
            ItemsAdds.NADIENITE_INGOT.get(),
            1.0f,
            100
        )
        .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_ingot_blastfurnace"));

         SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get()),
            RecipeCategory.MISC,
            ItemsAdds.NADIENITE_INGOT.get(),
            1.0f,
            100
        )
        .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_ingot_blastfurnace_alt"));

    
         // ==========================================
        // RECETAS ULTIMATE FOODS HORNO AHUMADOR
        // ==========================================
         SimpleCookingRecipeBuilder.smoking(
            Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
            RecipeCategory.MISC,
            ItemsAdds.TORTILLA.get(),
            1.0f,
            100
        )
         .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "tortilla_cruda_ahumador"));

        // ==========================================
        // RECETAS ULTIMATE FOODS HOGUERA
        // ==========================================
        SimpleCookingRecipeBuilder.campfireCooking(
            Ingredient.of(ItemsAdds.BAKED_TORTILLA.get()),
            RecipeCategory.MISC,
            ItemsAdds.TORTILLA.get(),
            1.0f,
            600
        )
        .unlockedBy("has_baked_tortilla", has(ItemsAdds.BAKED_TORTILLA.get()))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "tortilla_cruda_hoguera"));


        // ==========================================
        // RECETAS ULTIMATE FOODS
        // ==========================================

        // DORITOS - Trigo + Patata al horno + Cubo
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.DORITOS.get(), 1)
                .pattern("aba")
                .pattern("bcb")
                .pattern("aba")
                .define('a', Items.WHEAT)
                .define('b', Items.BAKED_POTATO)
                .define('c', Items.BUCKET)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "doritos_recipe"));

        // CAJITA FELIZ - Manzana + Patata al horno + Cofre + Pollo cocinado
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.CAJITA_FELIZ.get(), 1)
                .pattern(" a ")
                .pattern("bcd")
                .define('a', Items.APPLE)
                .define('b', Items.BAKED_POTATO)
                .define('c', Items.CHEST)
                .define('d', Items.COOKED_CHICKEN)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cajita_feliz_recipe"));

        // POOP (CACA) - Carne podrida + Manzana dorada
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.POOP.get(), 1)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', Items.ROTTEN_FLESH)
                .define('b', Items.GOLDEN_APPLE)
                .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "poop_recipe"));

        // NADIENITE BLOCK 
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksAdds.NADIENITE_BLOCK.get(), 1)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ItemsAdds.NADIENITE_INGOT.get())
                .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_block_recipe"));
         
         // NADIENITE HELMET
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_HELMET.get(), 1)
             .pattern("NNN")
             .pattern("N N")
             .define('N', ItemsAdds.NADIENITE_INGOT.get())
             .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
             .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_helmet"));
             
        // NADIENITE CHESTPLATE
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_CHESTPLATE.get(), 1)
             .pattern("N N")
             .pattern("NNN")
             .pattern("NNN")
             .define('N', ItemsAdds.NADIENITE_INGOT.get())
             .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
             .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_chestplate"));
             
         // NADIENITE LEGGINGS
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_LEGGINGS.get(), 1)
             .pattern("NNN")
             .pattern("N N")
             .pattern("N N")
             .define('N', ItemsAdds.NADIENITE_INGOT.get())
             .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
             .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_leggings"));
              
       // NADIENITE BOOTS
       ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ArmorAdds.NADIENITE_BOOTS.get(), 1)
             .pattern("N N")
             .pattern("N N")
             .define('N', ItemsAdds.NADIENITE_INGOT.get())
             .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
             .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_boots"));

        // NADIENITE SWORD (Espada)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ToolsAdds.NADIENITE_SWORD.get(), 1)
        .pattern(" N ")
        .pattern(" N ")
        .pattern(" S ")
        .define('N', ItemsAdds.NADIENITE_INGOT.get())
        .define('S', Items.STICK)
        .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
        .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite_sword"));

          //CHANCLA
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ToolsAdds.CHANCLA.get(), 1)
              .pattern(" C ")
              .pattern(" N ")
              .pattern(" S ")
              .define('N', ToolsAdds.NADIENITE_SWORD.get())
              .define('S', Items.STICK)
              .define('C', Items.NETHER_STAR)
              .unlockedBy("has_nadienite_ingot", has(ItemsAdds.NADIENITE_INGOT.get()))
              .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "chancleta"));
        
        
        
        // SUPER ENERGY DRINK - 
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.SUPER_ENERGY_DRINK.get(), 1)
                .pattern("ab")
                .pattern("cd")
                .pattern("e ")
                .define('a', ItemsAdds.CAJITA_FELIZ.get())
                .define('b', ItemsAdds.SUPER_CHILE.get())
                .define('c', ItemsAdds.CHOCOLATADA.get())
                .define('d', Items.CAKE)
                .define('e', Items.DIAMOND)
                .unlockedBy("has_cajita_feliz", has(ItemsAdds.CAJITA_FELIZ.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "super_energy_drink_recipe"));
        //TACO
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.TACO.get(), 1)
                .pattern("aaa")
                .pattern("bcd")
                .pattern("aaa")
                .define('a', ItemsAdds.TORTILLA.get())
                .define('b', Items.GOLDEN_CARROT)
                .define('c', Items.COOKED_BEEF)
                .define('d', Items.BEETROOT)
                .unlockedBy("has_cooked_beef", has(Items.COOKED_BEEF))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "taco"));

    //tortilla cruda
    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.BAKED_TORTILLA.get(), 1)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', Items.WHEAT)
                .define('b', Items.BREAD)
                .unlockedBy("has_bread", has(Items.BREAD))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "tortilla_cruda"));

    //steel ingot
     ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.STEEL_INGOT.get(), 1)
                .pattern("aa ")
                .pattern("bb ")
                .pattern("   ")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.COAL)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "acero"));

      //steel ingot
     ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsAdds.STEEL_INGOT.get(), 9)
                .pattern("a  ")
                .pattern("   ")
                .pattern("   ")
                .define('a', BlocksAdds.STEEL_BLOCK.get())
                .unlockedBy("has_steel_block", has(BlocksAdds.STEEL_BLOCK.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "acero_desde_bloque"));

    //steel block
     ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksAdds.STEEL_BLOCK.get(), 1)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ItemsAdds.STEEL_INGOT.get())
                .unlockedBy("has_steel_ingot", has(ItemsAdds.STEEL_INGOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "bloque_de_acero"));
    
    
    
    
    }
}