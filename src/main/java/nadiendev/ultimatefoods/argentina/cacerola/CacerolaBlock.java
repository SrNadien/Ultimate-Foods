package nadiendev.ultimatefoods.argentina.cacerola;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * La Cacerola Essen. Se apoya sobre una fuente de calor (fogata prendida, horno prendido, parrilla
 * prendida, magma...), se le tiran los ingredientes del guiso, se espera, y se sirve con un bowl.
 * La logica esta en {@link CacerolaBlockEntity}.
 */
public class CacerolaBlock extends BaseEntityBlock {

    public static final MapCodec<CacerolaBlock> CODEC = simpleCodec(CacerolaBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    /** Huella de 12x12 y 9 de alto (con las manijas). El modelo 3D respeta esta caja. */
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 9, 14);

    public CacerolaBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
        return new CacerolaBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, CacerolaRegistry.CACEROLA_BE.get(), CacerolaBlockEntity::serverTick);
    }

    // ---- Interaccion -----------------------------------------------------------------------------

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof CacerolaBlockEntity pot)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        boolean server = !level.isClientSide;

        // Servir: con el recipiente de la receta en la mano.
        if (pot.hasPortions()) {
            if (ItemStack.isSameItem(stack, pot.getContainer())) {
                if (server) {
                    ItemStack served = pot.servePortion((ServerLevel) level);
                    if (!player.hasInfiniteMaterials()) {
                        stack.shrink(1);
                    }
                    if (!player.getInventory().add(served)) {
                        player.drop(served, false);
                    }
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.8F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!stack.isEmpty()) {
                if (server) {
                    message(player, Component.translatable("message.ultimatefoods.cacerola.necesita_plato",
                            pot.getContainer().getHoverName(), pot.getPortions()));
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Cargar un ingrediente.
        if (!stack.isEmpty() && CacerolaBlockEntity.isUsefulIngredient(level, stack)) {
            if (server) {
                if (pot.canAddIngredient(level, stack)) {
                    Component name = stack.getHoverName();
                    pot.addIngredient(stack);
                    if (!player.hasInfiniteMaterials()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 0.9F);
                    message(player, Component.translatable("message.ultimatefoods.cacerola.agregado",
                            name, pot.ingredientCount(), CacerolaBlockEntity.SLOTS));
                } else {
                    message(player, Component.translatable("message.ultimatefoods.cacerola.llena"));
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /** Mano vacia: cuenta como va la cacerola y, si todavia se esta cargando, devuelve el ultimo ingrediente. */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (!player.getMainHandItem().isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!(level.getBlockEntity(pos) instanceof CacerolaBlockEntity pot)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            // Mientras se carga devuelve el ultimo ingrediente; cocinando o lista, solo cuenta como esta.
            if (!pot.isCooking() && !pot.hasPortions() && pot.ingredientCount() > 0) {
                ItemStack taken = pot.removeLastIngredient();
                if (!taken.isEmpty()) {
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    if (!player.getInventory().add(taken)) {
                        player.drop(taken, false);
                    }
                }
            }
            message(player, pot.statusMessage(level));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void message(Player player, Component text) {
        player.displayClientMessage(text, true);
    }

    // ---- Efectos ---------------------------------------------------------------------------------

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof CacerolaBlockEntity pot)) {
            return;
        }
        boolean cooking = pot.isCooking();
        if (!cooking && !pot.hasPortions()) {
            return;
        }
        double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        double y = pos.getY() + 0.6;
        if (random.nextInt(cooking ? 3 : 8) == 0) {
            level.addParticle(ParticleTypes.CLOUD, x, y + 0.2, z, 0.0, 0.03, 0.0);
        }
        if (cooking && random.nextInt(5) == 0) {
            level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.02, 0.0);
        }
        if (cooking && random.nextInt(20) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5,
                    SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 0.4F, 0.8F + random.nextFloat() * 0.4F, false);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof CacerolaBlockEntity pot) {
            pot.dropContents(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
