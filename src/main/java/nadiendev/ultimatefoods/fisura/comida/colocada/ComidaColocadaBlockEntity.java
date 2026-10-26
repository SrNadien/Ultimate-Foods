package nadiendev.ultimatefoods.fisura.comida.colocada;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Sin datos propios: el nombre sale de la comida del estado, asi que no hay que sincronizar nada. */
public class ComidaColocadaBlockEntity extends BlockEntity implements Nameable {

    public ComidaColocadaBlockEntity(BlockPos pos, BlockState state) {
        super(ColocadaRegistry.COMIDA_BE.get(), pos, state);
    }

    @Override
    public Component getName() {
        return Component.translatable(getBlockState().getValue(ComidaColocadaBlock.COMIDA).item().getDescriptionId());
    }

    @Override
    public Component getCustomName() {
        return getName();
    }
}
