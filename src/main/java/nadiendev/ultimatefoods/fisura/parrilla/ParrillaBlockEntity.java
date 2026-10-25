package nadiendev.ultimatefoods.fisura.parrilla;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Estado de la parrilla: {@link #SLOTS} ranuras de comida, combustible acumulado y el avance de
 * cocción de cada ranura.
 * <p>
 * Reglas (todas se pueden seguir leyendo este archivo, no hace falta abrir el juego):
 * <ul>
 *   <li>Solo entra comida que tenga receta de {@code campfire_cooking} (es decir, todo lo que se hace
 *       en una fogata: carne cruda, pollo, el asado crudo, el chorizo, las milanesas...).</li>
 *   <li>El combustible se acumula en ticks (hasta {@link #MAX_FUEL_TICKS}); mientras esta encendida
 *       gasta 1 tick por tick de juego, sin importar si hay comida o no.</li>
 *   <li>Solo cocina encendida. Apagada, el avance de cada ranura baja de a 2 por tick, como en la fogata.</li>
 *   <li>Cuando una ranura termina se reemplaza por el resultado de la receta; si el resultado ya no
 *       se puede cocinar queda "listo" y es lo primero que se saca con la mano vacia.</li>
 * </ul>
 */
public class ParrillaBlockEntity extends BlockEntity {

    public static final int SLOTS = 6;
    public static final int MAX_FUEL_TICKS = 6400;

    /** Disposicion de las ranuras: 2 columnas x 3 filas, en coordenadas del bloque mirando al norte. */
    private static final double[] SLOT_X = {0.28, 0.72, 0.28, 0.72, 0.28, 0.72};
    private static final double[] SLOT_Z = {0.20, 0.20, 0.50, 0.50, 0.80, 0.80};

    private static final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> QUICK_CHECK =
            RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private final int[] cookTime = new int[SLOTS];
    private final int[] cookTotal = new int[SLOTS];
    private int fuel;

    public ParrillaBlockEntity(BlockPos pos, BlockState state) {
        super(ParrillaRegistry.PARRILLA_BE.get(), pos, state);
    }

    // ---- Combustible -----------------------------------------------------------------------------

    /** Ticks de combustible que aporta el item, o 0 si la parrilla no lo acepta. */
    public static int fuelValue(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        boolean accepted = stack.is(ItemTags.COALS)
                || stack.is(ItemTags.LOGS_THAT_BURN)
                || stack.is(ItemTags.PLANKS)
                || stack.is(Items.STICK);
        return accepted ? Math.max(0, stack.getBurnTime(RecipeType.SMELTING)) : 0;
    }

    public boolean hasFuel() {
        return fuel > 0;
    }

    public int getFuel() {
        return fuel;
    }

    /** Suma combustible. Devuelve false (sin cambiar nada) si no entra todo. */
    public boolean addFuel(int ticks) {
        if (ticks <= 0 || fuel + ticks > MAX_FUEL_TICKS) {
            return false;
        }
        fuel += ticks;
        syncToClients();
        return true;
    }

    // ---- Comida ----------------------------------------------------------------------------------

    private static Optional<RecipeHolder<CampfireCookingRecipe>> findRecipe(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        return QUICK_CHECK.getRecipeFor(new SingleRecipeInput(stack), level);
    }

    public static boolean canCook(Level level, ItemStack stack) {
        return findRecipe(level, stack).isPresent();
    }

    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    public boolean hasItems() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull() {
        for (ItemStack stack : items) {
            if (stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Pone una unidad de {@code stack} en la primera ranura libre (el llamador es quien la descuenta).
     * Devuelve false si no se puede cocinar o no queda lugar.
     */
    public boolean addItem(Level level, ItemStack stack) {
        Optional<RecipeHolder<CampfireCookingRecipe>> recipe = findRecipe(level, stack);
        if (recipe.isEmpty()) {
            return false;
        }
        for (int slot = 0; slot < SLOTS; slot++) {
            if (items.get(slot).isEmpty()) {
                items.set(slot, stack.copyWithCount(1));
                cookTime[slot] = 0;
                cookTotal[slot] = recipe.get().value().getCookingTime();
                syncToClients();
                return true;
            }
        }
        return false;
    }

    /**
     * Saca una ranura: primero las que ya estan listas (sin nada mas que cocinar), si no hay
     * ninguna, la ultima que se haya puesto. Devuelve vacio si la parrilla esta vacia.
     */
    public ItemStack takeItem() {
        int chosen = -1;
        for (int slot = 0; slot < SLOTS; slot++) {
            if (items.get(slot).isEmpty()) {
                continue;
            }
            if (cookTotal[slot] <= 0) {
                chosen = slot;
                break;
            }
            chosen = slot;
        }
        if (chosen < 0) {
            return ItemStack.EMPTY;
        }
        ItemStack taken = items.get(chosen);
        items.set(chosen, ItemStack.EMPTY);
        cookTime[chosen] = 0;
        cookTotal[chosen] = 0;
        syncToClients();
        return taken;
    }

    public void dropContents(Level level, BlockPos pos) {
        for (int slot = 0; slot < SLOTS; slot++) {
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, items.get(slot));
            items.set(slot, ItemStack.EMPTY);
        }
    }

    // ---- Tick ------------------------------------------------------------------------------------

    public static void serverTick(Level level, BlockPos pos, BlockState state, ParrillaBlockEntity grill) {
        if (!state.getValue(ParrillaBlock.LIT)) {
            grill.coolDown();
            return;
        }
        if (grill.fuel <= 0 || level.isRainingAt(pos.above())) {
            ParrillaBlock.extinguish(null, level, pos, state);
            return;
        }
        grill.fuel--;
        boolean changed = false;
        for (int slot = 0; slot < SLOTS; slot++) {
            if (grill.items.get(slot).isEmpty() || grill.cookTotal[slot] <= 0) {
                continue;
            }
            if (++grill.cookTime[slot] >= grill.cookTotal[slot]) {
                grill.finishSlot(level, slot);
                changed = true;
            }
        }
        if (changed) {
            level.playSound(null, pos, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.8F, 1.2F);
            grill.syncToClients();
        } else if (level.getGameTime() % 40 == 0) {
            grill.setChanged();
        }
    }

    private void coolDown() {
        for (int slot = 0; slot < SLOTS; slot++) {
            if (cookTime[slot] > 0) {
                cookTime[slot] = Mth.clamp(cookTime[slot] - 2, 0, cookTotal[slot]);
            }
        }
    }

    private void finishSlot(Level level, int slot) {
        ItemStack raw = items.get(slot);
        SingleRecipeInput input = new SingleRecipeInput(raw);
        ItemStack cooked = QUICK_CHECK.getRecipeFor(input, level)
                .map(holder -> holder.value().assemble(input, level.registryAccess()))
                .orElse(raw);
        items.set(slot, cooked);
        cookTime[slot] = 0;
        cookTotal[slot] = findRecipe(level, cooked).map(holder -> holder.value().getCookingTime()).orElse(0);
    }

    // ---- Posiciones de las ranuras (las usan el renderer y las particulas) -----------------------

    /** Centro de la ranura en coordenadas locales del bloque (0..1), ya girado segun hacia donde mira. */
    public static Vec3 slotCenter(int slot, Direction facing) {
        double dx = SLOT_X[slot] - 0.5;
        double dz = SLOT_Z[slot] - 0.5;
        double x;
        double z;
        switch (facing) {
            case EAST -> {
                x = -dz;
                z = dx;
            }
            case SOUTH -> {
                x = -dx;
                z = -dz;
            }
            case WEST -> {
                x = dz;
                z = -dx;
            }
            default -> {
                x = dx;
                z = dz;
            }
        }
        return new Vec3(0.5 + x, 0, 0.5 + z);
    }

    // ---- Persistencia y sincronizacion -----------------------------------------------------------

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, true, registries);
        tag.putIntArray("CookTimes", cookTime);
        tag.putIntArray("CookTotals", cookTotal);
        tag.putInt("Fuel", fuel);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.clear();
        for (int slot = 0; slot < SLOTS; slot++) {
            items.set(slot, ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(tag, items, registries);
        copyInto(tag.getIntArray("CookTimes"), cookTime);
        copyInto(tag.getIntArray("CookTotals"), cookTotal);
        fuel = Mth.clamp(tag.getInt("Fuel"), 0, MAX_FUEL_TICKS);
    }

    private static void copyInto(int[] source, int[] target) {
        for (int i = 0; i < target.length; i++) {
            target[i] = i < source.length ? Math.max(0, source[i]) : 0;
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void syncToClients() {
        setChanged();
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }
}
