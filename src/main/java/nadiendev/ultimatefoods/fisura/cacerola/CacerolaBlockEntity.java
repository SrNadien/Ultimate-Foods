package nadiendev.ultimatefoods.fisura.cacerola;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Estado de la Cacerola Essen.
 * <p>
 * Ciclo (todo se puede seguir leyendo este archivo):
 * <ol>
 *   <li><b>Cargar</b>: hasta {@link #SLOTS} ingredientes, uno por ranura, solo los que forman parte de
 *       alguna receta. Mientras haya porciones servidas no se puede cargar.</li>
 *   <li><b>Cocinar</b>: si lo que hay adentro coincide exactamente con una receta y el bloque de abajo
 *       da calor, el avance sube 1 por tick hasta el tiempo de la receta. Sin calor, o si cambia el
 *       contenido, el avance baja de a 2 por tick.</li>
 *   <li><b>Listo</b>: los ingredientes se consumen y quedan {@code portions} porciones del plato
 *       (la cantidad del resultado de la receta).</li>
 *   <li><b>Servir</b>: con el recipiente de la receta (el bowl) en la mano se saca una porcion por vez;
 *       al sacar la ultima, la cacerola queda vacia.</li>
 * </ol>
 */
public class CacerolaBlockEntity extends BlockEntity {

    public static final int SLOTS = 6;

    private static RecipeManager.@Nullable CachedCheck<CacerolaInput, CacerolaRecipe> quickCheck;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private int progress;
    private int total;
    private ItemStack meal = ItemStack.EMPTY;
    private ItemStack container = ItemStack.EMPTY;
    private int portions;
    private float experience;

    public CacerolaBlockEntity(BlockPos pos, BlockState state) {
        super(CacerolaRegistry.CACEROLA_BE.get(), pos, state);
    }

    private static RecipeManager.CachedCheck<CacerolaInput, CacerolaRecipe> check() {
        if (quickCheck == null) {
            quickCheck = RecipeManager.createCheck(CacerolaRegistry.CACEROLA_TYPE.get());
        }
        return quickCheck;
    }

    /** La receta que coincide con {@code input}: en el servidor con la busqueda cacheada, en el cliente con las sincronizadas. */
    private static Optional<RecipeHolder<CacerolaRecipe>> findRecipe(Level level, CacerolaInput input) {
        if (level instanceof ServerLevel server) {
            return check().getRecipeFor(input, server);
        }
        for (RecipeHolder<CacerolaRecipe> holder : CacerolaRecetas.todas(level)) {
            if (holder.value().matches(input, level)) {
                return Optional.of(holder);
            }
        }
        return Optional.empty();
    }

    // ---- Consultas -------------------------------------------------------------------------------

    public boolean hasPortions() {
        return portions > 0;
    }

    public int getPortions() {
        return portions;
    }

    public ItemStack getMeal() {
        return meal;
    }

    public ItemStack getContainer() {
        return container;
    }

    /**
     * Que esta pasando adentro, en una linea: vacia, con ingredientes que no forman receta, lista para
     * cocinar sin fuego, cocinando (con el porcentaje) o con las porciones listas para servir.
     * Es lo que muestra el click con la mano vacia y el visor de bloques (Jade).
     */
    public Component statusMessage(Level level) {
        if (portions > 0) {
            return Component.translatable("message.ultimatefoods.cacerola.estado.listo",
                    portions, meal.getHoverName(), container.getHoverName());
        }
        int count = ingredientCount();
        if (count == 0) {
            return Component.translatable("message.ultimatefoods.cacerola.estado.vacia");
        }
        Optional<RecipeHolder<CacerolaRecipe>> found = findRecipe(level, input());
        if (found.isEmpty()) {
            return Component.translatable("message.ultimatefoods.cacerola.estado.incompleta", count, SLOTS);
        }
        CacerolaRecipe recipe = found.get().value();
        int pct = Math.min(100, progress * 100 / Math.max(1, recipe.getCookingTime()));
        Component dish = recipe.getResult().getHoverName();
        return Component.translatable(isHeated(level, worldPosition)
                ? "message.ultimatefoods.cacerola.estado.cocinando"
                : "message.ultimatefoods.cacerola.estado.sin_fuego", dish, pct);
    }

    public boolean isCooking() {
        return progress > 0 && portions == 0;
    }

    public int getProgress() {
        return progress;
    }

    public int getTotal() {
        return total;
    }

    public int ingredientCount() {
        int count = 0;
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    /** Para dar calor, el bloque de abajo tiene que estar en el tag de fuentes de calor (y prendido si tiene LIT). */
    public static boolean isHeated(Level level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(CacerolaRegistry.HEAT_SOURCES)
                && (!below.hasProperty(BlockStateProperties.LIT) || below.getValue(BlockStateProperties.LIT));
    }

    /**
     * True si el item forma parte de algun ingrediente de alguna receta de cacerola. Funciona en los
     * dos lados: en el cliente mira las recetas que mando el servidor ({@link CacerolaRecetas}).
     */
    public static boolean isUsefulIngredient(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (RecipeHolder<CacerolaRecipe> holder : CacerolaRecetas.todas(level)) {
            for (Ingredient ingredient : holder.value().getIngredients()) {
                if (ingredient.test(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ---- Acciones del jugador --------------------------------------------------------------------

    public boolean canAddIngredient(Level level, ItemStack stack) {
        return portions == 0 && ingredientCount() < SLOTS && isUsefulIngredient(level, stack);
    }

    public void addIngredient(ItemStack stack) {
        for (int slot = 0; slot < SLOTS; slot++) {
            if (items.get(slot).isEmpty()) {
                items.set(slot, stack.copyWithCount(1));
                progress = 0;
                syncToClients();
                return;
            }
        }
    }

    /** Devuelve el ultimo ingrediente puesto, o vacio si no hay (o si ya hay porciones servidas). */
    public ItemStack removeLastIngredient() {
        if (portions > 0) {
            return ItemStack.EMPTY;
        }
        for (int slot = SLOTS - 1; slot >= 0; slot--) {
            if (!items.get(slot).isEmpty()) {
                ItemStack taken = items.get(slot);
                items.set(slot, ItemStack.EMPTY);
                progress = 0;
                syncToClients();
                return taken;
            }
        }
        return ItemStack.EMPTY;
    }

    /** Saca una porcion; da la experiencia de la receta en la posicion de la cacerola. */
    public ItemStack servePortion(ServerLevel level) {
        if (portions <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack served = meal.copyWithCount(1);
        portions--;
        if (experience > 0) {
            ExperienceOrb.award(level, Vec3.atCenterOf(worldPosition.above()), Mth.floor(experience));
            if (level.getRandom().nextFloat() < Mth.frac(experience)) {
                ExperienceOrb.award(level, Vec3.atCenterOf(worldPosition.above()), 1);
            }
        }
        if (portions == 0) {
            meal = ItemStack.EMPTY;
            container = ItemStack.EMPTY;
            experience = 0;
        }
        syncToClients();
        return served;
    }

    public void dropContents(Level level, BlockPos pos) {
        for (int slot = 0; slot < SLOTS; slot++) {
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, items.get(slot));
            items.set(slot, ItemStack.EMPTY);
        }
    }

    /** Al romper la cacerola suelta los ingredientes que tenga (en 1.21.1 era Block#onRemove). */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level != null) {
            dropContents(level, pos);
        }
    }

    // ---- Tick ------------------------------------------------------------------------------------

    public static void serverTick(Level level, BlockPos pos, BlockState state, CacerolaBlockEntity pot) {
        // La tapa se abre mientras hay ingredientes o porciones adentro, y se cierra cuando queda vacia.
        boolean abierta = pot.ingredientCount() > 0 || pot.portions > 0;
        if (state.getValue(CacerolaBlock.ABIERTA) != abierta) {
            level.setBlock(pos, state.setValue(CacerolaBlock.ABIERTA, abierta), Block.UPDATE_ALL);
        }
        if (pot.portions > 0 || pot.ingredientCount() == 0) {
            if (pot.progress != 0) {
                pot.progress = 0;
                pot.syncToClients();
            }
            return;
        }
        CacerolaInput input = pot.input();
        Optional<RecipeHolder<CacerolaRecipe>> found = isHeated(level, pos)
                ? findRecipe(level, input) : Optional.empty();

        if (found.isEmpty()) {
            if (pot.progress > 0) {
                pot.progress = Math.max(0, pot.progress - 2);
                if (pot.progress == 0) {
                    pot.syncToClients();
                }
            }
            return;
        }

        CacerolaRecipe recipe = found.get().value();
        pot.total = recipe.getCookingTime();
        boolean started = pot.progress == 0;
        pot.progress++;
        if (started) {
            pot.syncToClients();
        }
        if (pot.progress >= pot.total) {
            pot.finish(level, pos, recipe, input);
        }
    }

    private CacerolaInput input() {
        List<ItemStack> present = new ArrayList<>();
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                present.add(stack);
            }
        }
        return new CacerolaInput(present);
    }

    private void finish(Level level, BlockPos pos, CacerolaRecipe recipe, CacerolaInput input) {
        ItemStack result = recipe.assemble(input);
        // Lo que dejan los ingredientes (baldes, frascos...) se tira al lado.
        for (int slot = 0; slot < SLOTS; slot++) {
            ItemStackTemplate remainder = items.get(slot).isEmpty() ? null : items.get(slot).getCraftingRemainder();
            if (remainder != null) {
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, remainder.create());
            }
            items.set(slot, ItemStack.EMPTY);
        }
        meal = result.copyWithCount(1);
        portions = Math.max(1, result.getCount());
        container = recipe.getContainer().copyWithCount(1);
        experience = recipe.getExperience();
        progress = 0;
        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.9F, 0.8F);
        syncToClients();
    }

    // ---- Persistencia y sincronizacion -----------------------------------------------------------

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items, true);
        output.putInt("Progress", progress);
        output.putInt("Total", total);
        output.putInt("Portions", portions);
        output.putFloat("Experience", experience);
        if (!meal.isEmpty()) {
            output.store("Meal", ItemStack.CODEC, meal);
        }
        if (!container.isEmpty()) {
            output.store("Container", ItemStack.CODEC, container);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        for (int slot = 0; slot < SLOTS; slot++) {
            items.set(slot, ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(input, items);
        progress = Math.max(0, input.getIntOr("Progress", 0));
        total = Math.max(0, input.getIntOr("Total", 0));
        portions = Math.max(0, input.getIntOr("Portions", 0));
        experience = input.getFloatOr("Experience", 0.0F);
        meal = input.read("Meal", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        container = input.read("Container", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        if (meal.isEmpty()) {
            portions = 0;
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void syncToClients() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }
}
