package nadiendev.ultimatefoods.argentina.mate.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.mate.MateComponents;
import nadiendev.ultimatefoods.argentina.mate.MateItems;
import nadiendev.ultimatefoods.argentina.mate.MateSabor;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.concurrent.CompletableFuture;

/** Recetas del modulo del mate. */
public class MateRecipes extends RecipeProvider {

    public MateRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "argentina/" + path);
    }

    @Override
    protected void buildRecipes(RecipeOutput writer) {
        // Mismo criterio que el resto del mod: sin avances de receta generados.
        RecipeOutput out = new RecipeOutput() {
            @Override
            public void accept(ResourceLocation id, Recipe<?> recipe, AdvancementHolder advancement) {
                writer.accept(id, recipe, null);
            }

            @Override
            public Advancement.Builder advancement() {
                return writer.advancement();
            }

            @Override
            public void accept(ResourceLocation id, Recipe<?> recipe, AdvancementHolder advancement, ICondition... conditions) {
                writer.accept(id, recipe, null, conditions);
            }
        };

        // ---- de la hoja al paquete ----
        Ingredient hoja = Ingredient.of(MateItems.YERBA_MATE_LEAF.get());
        SimpleCookingRecipeBuilder.smelting(hoja, RecipeCategory.MISC, MateItems.YERBA_MATE_LEAF_DRIED.get(), 0.1F, 200)
                .unlockedBy("has_yerba_mate_leaf", has(MateItems.YERBA_MATE_LEAF.get()))
                .save(out, rl("yerba_mate_leaf_dried_horno"));
        SimpleCookingRecipeBuilder.smoking(hoja, RecipeCategory.MISC, MateItems.YERBA_MATE_LEAF_DRIED.get(), 0.1F, 100)
                .unlockedBy("has_yerba_mate_leaf", has(MateItems.YERBA_MATE_LEAF.get()))
                .save(out, rl("yerba_mate_leaf_dried_ahumador"));
        SimpleCookingRecipeBuilder.campfireCooking(hoja, RecipeCategory.MISC, MateItems.YERBA_MATE_LEAF_DRIED.get(), 0.1F, 600)
                .unlockedBy("has_yerba_mate_leaf", has(MateItems.YERBA_MATE_LEAF.get()))
                .save(out, rl("yerba_mate_leaf_dried_fogata"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MateItems.YERBA_MATE.get(), 2)
                .requires(MateItems.YERBA_MATE_LEAF_DRIED.get(), 3)
                .unlockedBy("has_yerba_mate_leaf_dried", has(MateItems.YERBA_MATE_LEAF_DRIED.get()))
                .save(out, rl("yerba_mate_molida"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MateItems.YERBA_VERDEFLOR.get())
                .requires(MateItems.YERBA_MATE.get(), 4)
                .requires(Items.PAPER)
                .unlockedBy("has_yerba_mate", has(MateItems.YERBA_MATE.get()))
                .save(out, rl("yerba_verdeflor"));

        // ---- utensilios ----
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, MateItems.MATE.get())
                .requires(Items.BOWL)
                .requires(Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(out, rl("mate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MateItems.BOMBILLA.get())
                .pattern("N")
                .pattern("N")
                .pattern("N")
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(out, rl("bombilla"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MateItems.PAVA.get())
                .pattern("  S")
                .pattern("III")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(out, rl("pava"));

        // Bolsitas de tela con base de cuero: la yerbera con lana verde, la azucarera con lana blanca.
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MateItems.YERBERA.get())
                .pattern(" S ")
                .pattern("WLW")
                .pattern("LLL")
                .define('S', Items.STRING)
                .define('W', Items.GREEN_WOOL)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(out, rl("yerbera"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MateItems.AZUCARERA.get())
                .pattern(" S ")
                .pattern("WLW")
                .pattern("LLL")
                .define('S', Items.STRING)
                .define('W', Items.WHITE_WOOL)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(out, rl("azucarera"));

        // ---- yuyos: unas hierbitas del campo ----
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MateItems.YUYOS.get(), 2)
                .requires(Items.SHORT_GRASS)
                .requires(Items.FERN)
                .requires(Ingredient.of(ItemTags.SMALL_FLOWERS))
                .unlockedBy("has_fern", has(Items.FERN))
                .save(out, rl("yuyos"));

        // ---- sabores del mate (aplican el componente "sabor" al resultado) ----
        sabor(out, "mate_dulce", MateSabor.DULCE, Items.SUGAR);
        sabor(out, "mate_con_yuyos", MateSabor.YUYOS, MateItems.YUYOS.get());
        sabor(out, "mate_de_leche", MateSabor.LECHE, Items.MILK_BUCKET);

        // ---- madera ----
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, MateItems.YERBA_MATE_PLANKS.get(), 4)
                .requires(MateItems.YERBA_MATE_LOG.get())
                .unlockedBy("has_yerba_mate_log", has(MateItems.YERBA_MATE_LOG.get()))
                .save(out, rl("yerba_mate_planks"));
    }

    private void sabor(RecipeOutput out, String nombre, MateSabor sabor, ItemLike agregado) {
        ItemStack resultado = new ItemStack(MateItems.MATE_CON_YERBA.get());
        resultado.set(MateComponents.SABOR.get(), sabor);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, resultado)
                .requires(MateItems.MATE_CON_YERBA.get())
                .requires(agregado)
                .unlockedBy("has_mate_con_yerba", has(MateItems.MATE_CON_YERBA.get()))
                .save(out, rl(nombre));
    }
}
