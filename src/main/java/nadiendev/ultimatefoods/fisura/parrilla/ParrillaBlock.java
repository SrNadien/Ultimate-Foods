package nadiendev.ultimatefoods.fisura.parrilla;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nullable;

/**
 * La parrilla: se le pone combustible, se enciende con yesquero o carga de fuego y cocina todo lo
 * que cocinaria una fogata, seis cosas a la vez. Toda la logica de estado vive en
 * {@link ParrillaBlockEntity}; aca solo estan las interacciones, las particulas y los efectos.
 */
public class ParrillaBlock extends BaseEntityBlock {

    public static final MapCodec<ParrillaBlock> CODEC = simpleCodec(ParrillaBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final net.minecraft.world.level.block.state.properties.DirectionProperty FACING =
            HorizontalDirectionalBlock.FACING;

    public static final int LIT_LIGHT_LEVEL = 13;

    /** Alto de la superficie de cocina, en bloques (10.9/16, la reja del modelo). Lo usan la colision, las particulas y el renderer. */
    public static final double TOP_HEIGHT = 10.9 / 16.0;

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 11, 16);

    public ParrillaBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ParrillaBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ParrillaRegistry.PARRILLA_BE.get(), ParrillaBlockEntity::serverTick);
    }

    // ---- Interaccion -----------------------------------------------------------------------------

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof ParrillaBlockEntity grill)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        boolean lit = state.getValue(LIT);
        boolean server = !level.isClientSide;

        // Encender: yesquero o carga de fuego, y tiene que haber combustible y no estar lloviendo encima.
        if (!lit && (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE))) {
            if (server) {
                if (!grill.hasFuel()) {
                    message(player, "sin_combustible");
                } else if (level.isRainingAt(pos.above())) {
                    message(player, "lluvia");
                } else {
                    boolean flint = stack.is(Items.FLINT_AND_STEEL);
                    level.playSound(null, pos, flint ? SoundEvents.FLINTANDSTEEL_USE : SoundEvents.FIRECHARGE_USE,
                            SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL_IMMEDIATE);
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    if (flint) {
                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                    } else {
                        stack.consume(1, player);
                    }
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // Apagar con una pala, como la fogata.
        if (lit && stack.canPerformAction(ItemAbilities.SHOVEL_DOUSE)) {
            if (server) {
                extinguish(player, level, pos, state);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // Combustible: carbon, carbon vegetal, leña, tablones, palos.
        int fuelTicks = ParrillaBlockEntity.fuelValue(stack);
        if (fuelTicks > 0) {
            if (server) {
                if (grill.addFuel(fuelTicks)) {
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 0.8F);
                    if (!player.hasInfiniteMaterials()) {
                        stack.shrink(1);
                    }
                } else {
                    message(player, "combustible_lleno");
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // Comida cruda: a la primera ranura libre.
        if (ParrillaBlockEntity.canCook(level, stack)) {
            if (server) {
                if (grill.addItem(level, stack)) {
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 1.1F);
                    if (!player.hasInfiniteMaterials()) {
                        stack.shrink(1);
                    }
                } else {
                    message(player, "llena");
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (!player.getMainHandItem().isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!(level.getBlockEntity(pos) instanceof ParrillaBlockEntity grill) || !grill.hasItems()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            ItemStack taken = grill.takeItem();
            if (!taken.isEmpty()) {
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                if (!player.getInventory().add(taken)) {
                    player.drop(taken, false);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void message(Player player, String key) {
        player.displayClientMessage(Component.translatable("message.ultimatefoods.parrilla." + key), true);
    }

    /** Apaga la parrilla (queda el combustible que sobre). */
    static void extinguish(@Nullable Entity cause, Level level, BlockPos pos, BlockState state) {
        if (state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL_IMMEDIATE);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.8F, 1.0F);
            level.gameEvent(cause, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    // ---- Efectos ---------------------------------------------------------------------------------

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }
        double top = pos.getY() + TOP_HEIGHT;
        if (random.nextInt(10) == 0) {
            level.playLocalSound(pos.getX() + 0.5, top, pos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE,
                    SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
        }
        if (random.nextInt(4) == 0) {
            level.addParticle(ParticleTypes.LAVA, pos.getX() + 0.2 + random.nextDouble() * 0.6, top,
                    pos.getZ() + 0.2 + random.nextDouble() * 0.6, 0.0, 0.0, 0.0);
        }
        if (random.nextInt(3) == 0) {
            level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.15 + random.nextDouble() * 0.7, top + 0.05,
                    pos.getZ() + 0.15 + random.nextDouble() * 0.7, 0.0, 0.03, 0.0);
        }
        if (random.nextInt(5) == 0) {
            level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                    pos.getX() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1), top + 0.2,
                    pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
        }
        // Humito mas denso arriba de lo que se esta cocinando.
        if (level.getBlockEntity(pos) instanceof ParrillaBlockEntity grill) {
            Direction facing = state.getValue(FACING);
            for (int slot = 0; slot < ParrillaBlockEntity.SLOTS; slot++) {
                if (!grill.getItem(slot).isEmpty() && random.nextInt(6) == 0) {
                    Vec3 center = ParrillaBlockEntity.slotCenter(slot, facing);
                    level.addParticle(ParticleTypes.SMOKE, pos.getX() + center.x, top + 0.1, pos.getZ() + center.z,
                            0.0, 0.05, 0.0);
                }
            }
        }
    }

    /** Caminar encima de la parrilla prendida quema, como las rocas de magma. */
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity && !entity.isSteppingCarefully()
                && !entity.fireImmune()) {
            entity.hurt(level.damageSources().hotFloor(), 1.0F);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof ParrillaBlockEntity grill) {
            grill.dropContents(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
