package nadiendev.ultimatefoods.argentina.cacerola;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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
 * Farmer's Delight, y se puede escribir en datapacks:
 * <pre>
 * { "type": "ultimatefoods:cacerola",
 *   "ingredients": [ {"item": "minecraft:potato"}, ... ],
 *   "result": {"id": "ultimatefoods:guiso", "count": 4},
 *   "container": {"id": "minecraft:bowl"},      // opcional, por defecto un bowl
 *   "cookingtime": 600,                          // opcional, en ticks
 *   "experience": 1.0 }                          // opcional
 * </pre>
 */
public class CacerolaRecipe implements Recipe<CacerolaInput> {

    public static final int DEFAULT_COOKING_TIME = 600;
    public static final int MAX_INGREDIENTS = CacerolaBlockEntity.SLOTS;

    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final ItemStack container;
    private final int cookingTime;
    private final float experience;

    public CacerolaRecipe(List<Ingredient> ingredients, ItemStack result, ItemStack container,
                          int cookingTime, float experience) {
        this.ingredients = NonNullList.copyOf(ingredients);
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
    public ItemStack assemble(CacerolaInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    /** Se cocina solo en la cacerola: no aparece en el libro de recetas ni avisa al desbloquearse. */
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CacerolaRegistry.CACEROLA_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CacerolaRegistry.CACEROLA_TYPE.get();
    }

    public ItemStack getContainer() {
        return container;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public float getExperience() {
        return experience;
    }

    public ItemStack getResult() {
        return result;
    }

    public static class Serializer implements RecipeSerializer<CacerolaRecipe> {

        private static final MapCodec<CacerolaRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter(CacerolaRecipe::getIngredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CacerolaRecipe::getResult),
                ItemStack.STRICT_CODEC.optionalFieldOf("container", new ItemStack(Items.BOWL))
                        .forGetter(CacerolaRecipe::getContainer),
                com.mojang.serialization.Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME)
                        .forGetter(CacerolaRecipe::getCookingTime),
                com.mojang.serialization.Codec.FLOAT.optionalFieldOf("experience", 1.0F)
                        .forGetter(CacerolaRecipe::getExperience)
        ).apply(instance, CacerolaRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, CacerolaRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), CacerolaRecipe::getIngredients,
                ItemStack.STREAM_CODEC, CacerolaRecipe::getResult,
                ItemStack.STREAM_CODEC, CacerolaRecipe::getContainer,
                ByteBufCodecs.VAR_INT, CacerolaRecipe::getCookingTime,
                ByteBufCodecs.FLOAT, CacerolaRecipe::getExperience,
                CacerolaRecipe::new);

        @Override
        public MapCodec<CacerolaRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CacerolaRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
