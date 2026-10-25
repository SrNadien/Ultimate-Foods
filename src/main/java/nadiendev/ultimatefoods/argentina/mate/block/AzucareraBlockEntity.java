package nadiendev.ultimatefoods.argentina.mate.block;

import nadiendev.ultimatefoods.argentina.mate.MateBlockEntities;
import nadiendev.ultimatefoods.argentina.mate.MateComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Guarda las porciones de azucar que le quedan a la azucarera. */
public class AzucareraBlockEntity extends BlockEntity {

    private static final String TAG_PORCIONES = "porciones";

    private int porciones;

    public AzucareraBlockEntity(BlockPos pos, BlockState state) {
        super(MateBlockEntities.AZUCARERA.get(), pos, state);
    }

    public int getPorciones() {
        return porciones;
    }

    public void setPorciones(int valor) {
        this.porciones = Math.max(0, Math.min(valor, AzucareraBlock.MAX_PORCIONES));
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(TAG_PORCIONES, porciones);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        porciones = Math.max(0, Math.min(tag.getInt(TAG_PORCIONES), AzucareraBlock.MAX_PORCIONES));
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        porciones = Math.max(0, Math.min(input.getOrDefault(MateComponents.PORCIONES.get(), 0), AzucareraBlock.MAX_PORCIONES));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(MateComponents.PORCIONES.get(), porciones);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove(TAG_PORCIONES);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
