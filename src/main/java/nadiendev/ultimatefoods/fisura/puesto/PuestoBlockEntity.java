package nadiendev.ultimatefoods.fisura.puesto;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/** Guarda el item exacto que se apoyo (con sus cebadas, sorbos, sabor y agua) para devolverlo igual. */
public class PuestoBlockEntity extends BlockEntity implements Nameable {

    private static final String TAG_ITEM = "item";

    private ItemStack stack = ItemStack.EMPTY;

    public PuestoBlockEntity(BlockPos pos, BlockState state) {
        super(PuestoRegistry.PUESTO_BE.get(), pos, state);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // Cada mate puesto se llama como su item ("Mate amargo", "Pava caliente"...). El cliente tiene que
    // saber que item es para mostrarlo en los visores de bloques (Jade y compania).
    @Override
    public Component getName() {
        return stack.isEmpty() ? Component.translatable("block.ultimatefoods.mate_puesto") : stack.getHoverName();
    }

    @Override
    public Component getCustomName() {
        return stack.isEmpty() ? null : stack.getHoverName();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!stack.isEmpty()) {
            output.store(TAG_ITEM, ItemStack.CODEC, stack);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        stack = input.read(TAG_ITEM, ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }
}
