package nadiendev.ultimatefoods.fisura.mate.block;

import com.mojang.serialization.MapCodec;
import nadiendev.ultimatefoods.fisura.mate.MateItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * La yerbera: guarda yerba. Con un paquete de yerba (o yerba suelta) se llena; con un
 * mate vacio en la mano, sirve una porcion y te lo devuelve con yerba. Es el puesto
 * de trabajo del yerbatero.
 */
public class YerberaBlock extends BolsaBlock {

    public static final MapCodec<YerberaBlock> CODEC = simpleCodec(YerberaBlock::new);

    /** Porciones que aporta un paquete de yerba Verdeflor. */
    public static final int PORCIONES_POR_PAQUETE = 16;
    /** Porciones que aporta un puñado de yerba mate suelta. */
    public static final int PORCIONES_POR_YERBA = 4;
    /** Tope de la yerbera: cuatro paquetes. */
    public static final int MAX_PORCIONES = 64;

    public YerberaBlock(Properties properties) {
        super(properties, MateShapes.YERBERA);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new YerberaBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof YerberaBlockEntity yerbera)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        boolean paquete = stack.is(MateItems.YERBA_VERDEFLOR.get());
        boolean suelta = stack.is(MateItems.YERBA_MATE.get());
        if (paquete || suelta) {
            int aporte = paquete ? PORCIONES_POR_PAQUETE : PORCIONES_POR_YERBA;
            if (yerbera.getPorciones() + aporte > MAX_PORCIONES) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("message.ultimatefoods.yerbera.llena"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                yerbera.setPorciones(yerbera.getPorciones() + aporte);
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 0.8F, 1.0F);
                avisarPorciones(player, yerbera);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (stack.is(MateItems.MATE.get())) {
            if (yerbera.getPorciones() <= 0) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("message.ultimatefoods.yerbera.vacia"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                yerbera.setPorciones(yerbera.getPorciones() - 1);
                ItemStack lleno = new ItemStack(MateItems.MATE_CON_YERBA.get());
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, lleno));
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.9F, 1.1F);
                ((ServerLevel) level).sendParticles(ParticleTypes.COMPOSTER,
                        pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, 4, 0.15, 0.05, 0.15, 0.0);
                avisarPorciones(player, yerbera);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof YerberaBlockEntity yerbera) {
            avisarPorciones(player, yerbera);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void avisarPorciones(Player player, YerberaBlockEntity yerbera) {
        player.displayClientMessage(Component.translatable("message.ultimatefoods.yerbera.porciones",
                yerbera.getPorciones(), MAX_PORCIONES), true);
    }
}
