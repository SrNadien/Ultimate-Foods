package nadiendev.ultimatefoods.fisura.mate.block;

import com.mojang.serialization.MapCodec;
import nadiendev.ultimatefoods.fisura.mate.MateComponents;
import nadiendev.ultimatefoods.fisura.mate.MateSabor;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
 * La azucarera, hermana de la yerbera. Con azucar en la mano se llena; con un
 * {@code mate_con_yerba} en la mano, lo endulza (le fija el sabor dulce) y gasta una porcion.
 */
public class AzucareraBlock extends BolsaBlock {

    public static final MapCodec<AzucareraBlock> CODEC = simpleCodec(AzucareraBlock::new);

    /** Porciones que aporta cada azucar. */
    public static final int PORCIONES_POR_AZUCAR = 8;
    /** Tope de la azucarera (8 azucares). */
    public static final int MAX_PORCIONES = 64;

    public AzucareraBlock(Properties properties) {
        super(properties, MateShapes.AZUCARERA);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AzucareraBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof AzucareraBlockEntity azucarera)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.is(Items.SUGAR)) {
            if (azucarera.getPorciones() + PORCIONES_POR_AZUCAR > MAX_PORCIONES) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("message.ultimatefoods.azucarera.llena"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                azucarera.setPorciones(azucarera.getPorciones() + PORCIONES_POR_AZUCAR);
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.8F, 1.3F);
                avisarPorciones(player, azucarera);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (stack.is(MateItems.MATE_CON_YERBA.get())) {
            if (stack.getOrDefault(MateComponents.SABOR.get(), MateSabor.AMARGO) == MateSabor.DULCE) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("message.ultimatefoods.azucarera.ya_dulce"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (azucarera.getPorciones() <= 0) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("message.ultimatefoods.azucarera.vacia"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                azucarera.setPorciones(azucarera.getPorciones() - 1);
                stack.set(MateComponents.SABOR.get(), MateSabor.DULCE);
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.9F, 1.5F);
                ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 3, 0.15, 0.05, 0.15, 0.0);
                avisarPorciones(player, azucarera);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AzucareraBlockEntity azucarera) {
            avisarPorciones(player, azucarera);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void avisarPorciones(Player player, AzucareraBlockEntity azucarera) {
        player.displayClientMessage(Component.translatable("message.ultimatefoods.azucarera.porciones",
                azucarera.getPorciones(), MAX_PORCIONES), true);
    }
}
