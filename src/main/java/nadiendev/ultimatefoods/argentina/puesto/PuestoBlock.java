package nadiendev.ultimatefoods.argentina.puesto;

import com.mojang.serialization.MapCodec;
import nadiendev.ultimatefoods.argentina.mate.MateComponents;
import nadiendev.ultimatefoods.argentina.mate.MateItems;
import nadiendev.ultimatefoods.argentina.mate.MateRitual;
import nadiendev.ultimatefoods.argentina.mate.MateSabor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

    /**
     * Servir el mate ya puesto: con la yerbera (o yerba suelta) en la mano se le pone yerba al mate
     * vacio, y con la pava caliente se ceba el que tiene yerba (hace falta una bombilla en el
     * inventario). El mate queda donde esta, solo cambia de estado.
     */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof PuestoBlockEntity be)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        Puesto puesto = state.getValue(PUESTO);
        ItemStack mate = be.getStack().isEmpty() ? new ItemStack(puesto.item()) : be.getStack();

        // Yerba al mate vacio
        boolean yerbera = held.is(MateItems.YERBERA.get()) && held.getOrDefault(MateComponents.PORCIONES.get(), 0) > 0;
        if (puesto == Puesto.MATE && (yerbera || held.is(MateItems.YERBA_MATE.get()))) {
            if (!level.isClientSide) {
                if (yerbera) {
                    if (!player.hasInfiniteMaterials()) {
                        held.set(MateComponents.PORCIONES.get(), held.getOrDefault(MateComponents.PORCIONES.get(), 0) - 1);
                    }
                } else if (!player.hasInfiniteMaterials()) {
                    held.shrink(1);
                }
                cambiar(level, pos, state, be, mate.transmuteCopy(MateItems.MATE_CON_YERBA.get(), 1));
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.9F, 1.1F);
                ((ServerLevel) level).sendParticles(ParticleTypes.COMPOSTER,
                        pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 4, 0.1, 0.05, 0.1, 0.0);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // Cebar con la pava caliente
        if ((puesto == Puesto.MATE_CON_YERBA || puesto == Puesto.MATE_LAVADO) && held.is(MateItems.PAVA_CALIENTE.get())) {
            if (!MateRitual.tieneBombilla(player)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("message.ultimatefoods.mate.sin_bombilla"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                MateSabor sabor = mate.getOrDefault(MateComponents.SABOR.get(), MateSabor.AMARGO);
                int cebadas = mate.getOrDefault(MateComponents.CEBADAS.get(), 0) + 1;
                ItemStack cebado = mate.transmuteCopy(sabor.itemCebado(), 1);
                cebado.set(MateComponents.SABOR.get(), sabor);
                cebado.set(MateComponents.CEBADAS.get(), cebadas);
                cebado.set(MateComponents.SORBOS.get(), MateRitual.SORBOS_POR_CEBADA);
                cambiar(level, pos, state, be, cebado);

                int restantes = held.getOrDefault(MateComponents.CEBADAS_RESTANTES.get(), 1) - 1;
                if (!player.hasInfiniteMaterials()) {
                    if (restantes <= 0) {
                        ItemStack vacia = held.transmuteCopy(MateItems.PAVA.get(), 1);
                        vacia.remove(MateComponents.CEBADAS_RESTANTES.get());
                        player.setItemInHand(hand, vacia);
                    } else {
                        held.set(MateComponents.CEBADAS_RESTANTES.get(), restantes);
                    }
                }
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.6F, 1.4F);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.8F, 0.9F);
                ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD,
                        pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.01);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /** Cambia el mate puesto por otro estado (nuevo modelo y nuevo item guardado), sin moverlo. */
    private static void cambiar(Level level, BlockPos pos, BlockState state, PuestoBlockEntity be, ItemStack nuevo) {
        Puesto destino = Puesto.de(nuevo);
        if (destino != null) {
            level.setBlock(pos, state.setValue(PUESTO, destino), 3);
        }
        be.setStack(nuevo);
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
