package nadiendev.ultimatefoods.argentina.puesto;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * Un mate o una pava apoyados sobre un bloque. Un solo bloque para todos: {@link #PUESTO} dice cual
 * es y {@link #FACING} hacia donde mira. El item exacto se guarda en el block entity, asi que al
 * levantarlo o romperlo vuelve igual que se puso (con sus cebadas y su sabor).
 *
 * <p>Click derecho: lo levanta. Si falta el bloque de abajo, se rompe y suelta el item.</p>
 */
public class PuestoBlock extends BaseEntityBlock {

    public static final MapCodec<PuestoBlock> CODEC = simpleCodec(PuestoBlock::new);
    public static final EnumProperty<Puesto> PUESTO = EnumProperty.create("puesto", Puesto.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public PuestoBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(PUESTO, Puesto.MATE).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PUESTO, FACING);
    }

    public BlockState stateFor(Puesto puesto, BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(PUESTO, puesto)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PuestoBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PUESTO).shape(state.getValue(FACING));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // ---- Soporte ---------------------------------------------------------------------------------

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide && !state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    // ---- Items -----------------------------------------------------------------------------------

    /** Suelta el item guardado, no uno nuevo: asi no se pierde el estado del mate. */
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof PuestoBlockEntity be
                && !be.getStack().isEmpty()) {
            return List.of(be.getStack().copy());
        }
        return List.of(new ItemStack(state.getValue(PUESTO).item()));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof PuestoBlockEntity be && !be.getStack().isEmpty()) {
            return be.getStack().copy();
        }
        return new ItemStack(state.getValue(PUESTO).item());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (!level.isClientSide) {
            ItemStack stack = level.getBlockEntity(pos) instanceof PuestoBlockEntity be && !be.getStack().isEmpty()
                    ? be.getStack().copy()
                    : new ItemStack(state.getValue(PUESTO).item());
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.4F, 1.0F);
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
