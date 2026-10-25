package nadiendev.ultimatefoods.argentina.comida.data;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.cacerola.CacerolaRecipeBuilder;
import nadiendev.ultimatefoods.argentina.cacerola.CacerolaRegistry;
import nadiendev.ultimatefoods.argentina.comida.ComidaItems;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import nadiendev.ultimatefoods.argentina.parrilla.ParrillaRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

/**
 * Recetas de mesa de crafteo y de cocion (horno, ahumador, fogata) de las comidas argentinas.
 * Las de olla de cocina y tabla de cortar de Farmer's Delight estan escritas a mano en
 * {@code data/ultimatefoods/recipe/argentina/}.
 */
public class ComidaRecipes extends RecipeProvider {

    private static final TagKey<Item> DOUGH = tag("c:foods/dough");
    private static final TagKey<Item> ONION = tag("c:crops/onion");
    private static final TagKey<Item> TOMATO = tag("c:crops/tomato");
    private static final TagKey<Item> LEAFY_GREEN = tag("c:foods/leafy_green");

    public ComidaRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        parrilla(output);
        cacerola(output);
        ingredientes(output);
        platos(output);
        megaDibu(output);
        cocciones(output);
    }

    private void parrilla(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ParrillaRegistry.PARRILLA_ITEM.get())
                .pattern("BBB")
                .pattern("I I")
                .pattern("III")
                .define('B', Items.IRON_BARS)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, rl("parrilla"));
    }

    /** La olla (hierro, con manijas) y los dos platos que se cocinan en ella. */
    private void cacerola(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, CacerolaRegistry.CACEROLA_ESSEN_ITEM.get())
                .pattern("NIN")
                .pattern("I I")
                .pattern("III")
                .define('N', Items.IRON_NUGGET)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, rl("cacerola_essen"));

        // Guiso (4 porciones): papa, zanahoria, tomate, cebolla, carne, y arroz o fideos.
        CacerolaRecipeBuilder.cacerola(ComidaItems.GUISO.get(), 4)
                .requires(tag("c:crops/potato"))
                .requires(tag("c:crops/carrot"))
                .requires(tag("c:crops/tomato"))
                .requires(ONION)
                .requires(tag("c:foods/raw_beef"))
                .requires(CompoundIngredient.of(Ingredient.of(tag("c:crops/rice")), Ingredient.of(tag("c:foods/pasta")),
                        Ingredient.of(tag("c:foods/dough"))))
                .cookingTime(900)
                .save(output, rl("guiso"));

        // Locro (4 porciones): zapallo, trigo pisado, papa, cebolla, chorizo y carne.
        CacerolaRecipeBuilder.cacerola(ComidaItems.LOCRO.get(), 4)
                .requires(farmersDelight("pumpkin_slice"))
                .requires(Items.WHEAT)
                .requires(tag("c:crops/potato"))
                .requires(ONION)
                .requires(ComidaItems.CHORIZO.get())
                .requires(tag("c:foods/raw_beef"))
                .cookingTime(1200)
                .save(output, rl("locro"));
    }

    private void ingredientes(RecipeOutput output) {
        // La tabla de cortar de Farmer's Delight ya parte la carne vacuna y el cerdo (carne picada,
        // panceta), asi que el asado y el chorizo van por mesa de crafteo.
        shapeless(output, ComidaItems.ASADO_CRUDO.get(), 2, Items.BEEF, Items.BONE);
        shapeless(output, ComidaItems.CHORIZO.get(), 2, Items.PORKCHOP, farmersDelight("minced_beef"), Items.STRING);
        shapeless(output, ComidaItems.MILANESA_CRUDA.get(), 2, Items.BEEF, Tags.Items.EGGS, ComidaItems.PAN_RALLADO.get());
        shapeless(output, ComidaItems.MILANESA_DE_POLLO_CRUDA.get(), 2, Items.CHICKEN, Tags.Items.EGGS, ComidaItems.PAN_RALLADO.get());
        shapeless(output, ComidaItems.CEBOLLA_CRISPY.get(), 2, ONION, ComidaItems.PAN_RALLADO.get());
        shapeless(output, ComidaItems.SALSA_BARBACOA.get(), 2, TOMATO, TOMATO, Items.SUGAR, Items.HONEY_BOTTLE);
        shapeless(output, ComidaItems.EMPANADA_CRUDA.get(), 3, DOUGH, Items.BEEF, ONION, Tags.Items.EGGS);
    }

    private void platos(RecipeOutput output) {
        shapeless(output, ComidaItems.ALFAJOR.get(), 2,
                Items.WHEAT, Items.WHEAT, Items.COCOA_BEANS, ComidaItems.DULCE_DE_LECHE.get());
        shapeless(output, ComidaItems.MEDIALUNA.get(), 3, DOUGH, Items.SUGAR, Tags.Items.EGGS);
        shapeless(output, ComidaItems.TORTAFRITA.get(), 2, DOUGH, Items.SUGAR);
        shapeless(output, ComidaItems.PITUSAS.get(), 4, DOUGH, Items.SUGAR, Tags.Items.EGGS, Items.MILK_BUCKET);
        shapeless(output, ComidaItems.PEPAS.get(), 4, DOUGH, Items.SUGAR, Tags.Items.EGGS, ComidaItems.DULCE_DE_MEMBRILLO.get());
        shapeless(output, ComidaItems.PANQUEQUE_CON_DULCE_DE_LECHE.get(), 2,
                Items.WHEAT, Tags.Items.EGGS, Items.MILK_BUCKET, ComidaItems.DULCE_DE_LECHE.get());
        shapeless(output, ComidaItems.CHORIPAN.get(), 1, ComidaItems.CHORIZO_ASADO.get(), Items.BREAD, TOMATO);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ComidaItems.FUGAZZETA.get())
                .pattern("OOO")
                .pattern("QDQ")
                .define('O', ONION)
                .define('Q', ComidaItems.QUESO.get())
                .define('D', DOUGH)
                .unlockedBy("has_queso", has(ComidaItems.QUESO.get()))
                .save(output, rl("fugazzeta"));

        // Cualquiera de las dos milanesas sirve para el sanguche.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ComidaItems.SANGUCHE_DE_MILANESA.get())
                .requires(Ingredient.of(ComidaItems.MILANESA_DE_CARNE.get(), ComidaItems.MILANESA_DE_POLLO.get()))
                .requires(Items.BREAD)
                .requires(TOMATO)
                .requires(LEAFY_GREEN)
                .unlockedBy("has_milanesa", has(ComidaItems.MILANESA_DE_CARNE.get()))
                .save(output, rl("sanguche_de_milanesa"));
    }

    /**
     * La Mega Dibu de Mostaza: doble carne, doble cheddar, doble bacon, cebolla crispy y barbacoa,
     * todo en un pan. Son 9 ingredientes, justo el maximo de la mesa de crafteo.
     */
    private void megaDibu(RecipeOutput output) {
        Item patty = farmersDelight("beef_patty");
        Item bacon = farmersDelight("cooked_bacon");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ComidaItems.MEGA_DIBU.get())
                .requires(Items.BREAD)
                .requires(patty, 2)
                .requires(ComidaItems.CHEDDAR.get(), 2)
                .requires(bacon, 2)
                .requires(ComidaItems.CEBOLLA_CRISPY.get())
                .requires(ComidaItems.SALSA_BARBACOA.get())
                .unlockedBy("has_cheddar", has(ComidaItems.CHEDDAR.get()))
                .save(output, rl("mega_dibu"));
    }

    private void cocciones(RecipeOutput output) {
        cook(output, ComidaItems.ASADO_CRUDO.get(), ComidaItems.ASADO.get(), 0.35F);
        cook(output, ComidaItems.CHORIZO.get(), ComidaItems.CHORIZO_ASADO.get(), 0.35F);
        cook(output, ComidaItems.MILANESA_CRUDA.get(), ComidaItems.MILANESA_DE_CARNE.get(), 0.35F);
        cook(output, ComidaItems.MILANESA_DE_POLLO_CRUDA.get(), ComidaItems.MILANESA_DE_POLLO.get(), 0.35F);
        cook(output, ComidaItems.EMPANADA_CRUDA.get(), ComidaItems.EMPANADA.get(), 0.35F);
        cook(output, ComidaItems.QUESO.get(), ComidaItems.PROVOLETA.get(), 0.2F);
    }

    // ---- Helpers ---------------------------------------------------------------------------------

    /** Horno, ahumador y fogata (la parrilla usa la receta de fogata). */
    private void cook(RecipeOutput output, ItemLike raw, ItemLike cooked, float experience) {
        String name = BuiltInRegistries.ITEM.getKey(cooked.asItem()).getPath();
        Ingredient input = Ingredient.of(raw);
        String criterion = "has_" + BuiltInRegistries.ITEM.getKey(raw.asItem()).getPath();
        SimpleCookingRecipeBuilder.smelting(input, RecipeCategory.FOOD, cooked, experience, 200)
                .unlockedBy(criterion, has(raw)).save(output, rl(name + "_horno"));
        SimpleCookingRecipeBuilder.smoking(input, RecipeCategory.FOOD, cooked, experience, 100)
                .unlockedBy(criterion, has(raw)).save(output, rl(name + "_ahumador"));
        SimpleCookingRecipeBuilder.campfireCooking(input, RecipeCategory.FOOD, cooked, experience, 600)
                .unlockedBy(criterion, has(raw)).save(output, rl(name + "_hoguera"));
    }

    /** Receta sin forma; cada argumento puede ser un {@link ItemLike} o un {@code TagKey<Item>}. */
    @SuppressWarnings("unchecked")
    private void shapeless(RecipeOutput output, ItemLike result, int count, Object... ingredients) {
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, result, count);
        Object first = ingredients[0];
        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemLike item) {
                builder.requires(item);
            } else {
                builder.requires((TagKey<Item>) ingredient);
            }
        }
        if (first instanceof ItemLike item) {
            builder.unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(), has(item));
        } else {
            builder.unlockedBy("has_ingredient", has((TagKey<Item>) first));
        }
        builder.save(output, rl(BuiltInRegistries.ITEM.getKey(result.asItem()).getPath()));
    }

    private static TagKey<Item> tag(String id) {
        return TagKey.create(Registries.ITEM, ResourceLocation.parse(id));
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "argentina/" + path);
    }

    /** Item de Farmer's Delight por id; si falta, el datagen falla en vez de generar una receta rota. */
    private static Item farmersDelight(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("farmersdelight", path);
        Item item = BuiltInRegistries.ITEM.get(id);
        if (item == Items.AIR) {
            throw new IllegalStateException("Farmer's Delight no esta cargado en el datagen: falta " + id);
        }
        return item;
    }
}
