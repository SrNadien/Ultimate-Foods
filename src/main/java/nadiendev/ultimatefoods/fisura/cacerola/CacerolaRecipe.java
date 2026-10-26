package nadiendev.ultimatefoods.fisura.cacerola;

/*
 * Formato JSON de la receta propia de la Cacerola Essen en 26.1.2 (data/<ns>/recipe/<id>.json).
 * Es el mismo de 1.21.1 salvo los ingredientes, que ahora usan el formato de ingredientes de 26.1:
 *
 * {
 *   "type": "ultimatefoods:cacerola",
 *   "ingredients": [                          // obligatorio, de 1 a 6, sin orden
 *     "minecraft:wheat",                      //   un item: el id como texto (antes {"item": "minecraft:wheat"})
 *     "#c:crops/potato",                      //   un tag: "#" + id del tag (antes {"tag": "c:crops/potato"})
 *     ["minecraft:beef", "minecraft:mutton"], //   varios items: lista de ids (sin tags adentro)
 *     {                                       //   varios tags (antes {"type": "neoforge:compound", ...}):
 *       "neoforge:ingredient_type": "neoforge:compound",
 *       "children": ["#c:crops/rice", "#c:foods/pasta", "#c:foods/dough"]
 *     }
 *   ],
 *   "result": {"id": "ultimatefoods:guiso", "count": 4},   // obligatorio; count = porciones que rinde
 *                                                          // (tambien vale "ultimatefoods:guiso" para 1)
 *   "container": {"id": "minecraft:bowl", "count": 1},     // opcional, por defecto un bowl
 *                                                          // (tambien vale "minecraft:bowl")
 *   "cookingtime": 900,                                    // opcional, en ticks (por defecto 600)
 *   "experience": 1.0                                      // opcional (por defecto 1.0)
 * }
 *
 * "result" y "container" no cambian respecto de 1.21.1 ({"count": N, "id": "..."}); solo hay que
 * convertir cada entrada de "ingredients" con las reglas de arriba. Los "children" del compuesto se
 * convierten con las mismas reglas.
 */

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Receta de la Cacerola Essen: una lista de ingredientes sin orden, un plato (o cualquier
 * recipiente) para servirla, y el resultado, cuya cantidad es la de porciones que rinde la olla.
 * <p>
 * Es un tipo de receta propio ({@code ultimatefoods:cacerola}): no depende de las clases de
 * Farmer's Delight, y se puede escribir en datapacks (el formato esta al principio de este archivo).
 * <p>
 * En 26.1 el resultado y el recipiente se guardan como {@link ItemStackTemplate} (la receta ya no
 * guarda {@link ItemStack} sueltos), y las recetas viven solo en el servidor: al cliente (JEI) se
 * mandan aparte, ver {@link CacerolaRecetas}.
 */
public class CacerolaRecipe implements Recipe<CacerolaInput> {

    public static final int DEFAULT_COOKING_TIME = 600;
    public static final int MAX_INGREDIENTS = CacerolaBlockEntity.SLOTS;
    private static final float DEFAULT_EXPERIENCE = 1.0F;

    public static final MapCodec<CacerolaRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.listOf(1, MAX_INGREDIENTS).fieldOf("ingredients").forGetter(CacerolaRecipe::getIngredients),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CacerolaRecipe::resultTemplate),
            ItemStackTemplate.CODEC.optionalFieldOf("container", new ItemStackTemplate(Items.BOWL))
                    .forGetter(CacerolaRecipe::containerTemplate),
            Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME).forGetter(CacerolaRecipe::getCookingTime),
            Codec.FLOAT.optionalFieldOf("experience", DEFAULT_EXPERIENCE).forGetter(CacerolaRecipe::getExperience)
    ).apply(instance, CacerolaRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CacerolaRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), CacerolaRecipe::getIngredients,
            ItemStackTemplate.STREAM_CODEC, CacerolaRecipe::resultTemplate,
            ItemStackTemplate.STREAM_CODEC, CacerolaRecipe::containerTemplate,
            ByteBufCodecs.VAR_INT, CacerolaRecipe::getCookingTime,
            ByteBufCodecs.FLOAT, CacerolaRecipe::getExperience,
            CacerolaRecipe::new);

    private final List<Ingredient> ingredients;
    private final ItemStackTemplate result;
    private final ItemStackTemplate container;
    private final int cookingTime;
    private final float experience;

    public CacerolaRecipe(List<Ingredient> ingredients, ItemStackTemplate result, ItemStackTemplate container,
                          int cookingTime, float experience) {
        this.ingredients = List.copyOf(ingredients);
        this.result = result;
        this.container = container;
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    @Override
    public boolean matches(CacerolaInput input, Level level) {
        if (input.size() != ingredients.size()) {
            return false;
        }
        List<ItemStack> stacks = new ArrayList<>(input.items());
        return RecipeMatcher.findMatches(stacks, ingredients) != null;
    }

    @Override
    public ItemStack assemble(CacerolaInput input) {
        return result.create();
    }

    /** Se cocina solo en la cacerola: no aparece en el libro de recetas ni avisa al desbloquearse. */
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    /** No se arma en ninguna grilla, asi que el libro de recetas no tiene nada que acomodar. */
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    /** No tiene entradas en el libro de recetas (no devuelve ningun {@code display}); va con las de fogata. */
    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CAMPFIRE;
    }

    @Override
    public RecipeSerializer<CacerolaRecipe> getSerializer() {
        return CacerolaRegistry.CACEROLA_SERIALIZER.get();
    }

    @Override
    public RecipeType<CacerolaRecipe> getType() {
        return CacerolaRegistry.CACEROLA_TYPE.get();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /** El recipiente para servir (una unidad nueva cada vez). */
    public ItemStack getContainer() {
        return container.create();
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public float getExperience() {
        return experience;
    }

    /** El plato que sale (una copia nueva cada vez); la cantidad es la de porciones. */
    public ItemStack getResult() {
        return result.create();
    }

    private ItemStackTemplate resultTemplate() {
        return result;
    }

    private ItemStackTemplate containerTemplate() {
        return container;
    }
}
