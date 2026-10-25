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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Un mate, la pava, un paquete de yerba o una bombilla apoyados sobre un bloque. Un solo bloque
 * para todos: {@link #PUESTO} dice cual es y {@link #FACING} hacia donde mira. El item exacto se
 * guarda en el block entity, asi que al levantarlo o romperlo vuelve igual que se puso (con sus
 * cebadas y su sabor).
 *
 * <p>El mate se sirve ya puesto: yerba (yerbera o yerba suelta), azucar, leche o yuyos para darle
 * gusto, la bombilla aparte (propiedad {@link #BOMBILLA}) y la pava caliente para cebarlo. Click
 * derecho con la mano vacia: lo levanta. Si falta el bloque de abajo, se rompe y suelta el item.</p>
 */
public class PuestoBlock extends BaseEntityBlock {

    public static final MapCodec<PuestoBlock> CODEC = simpleCodec(PuestoBlock::new);
    public static final EnumProperty<Puesto> PUESTO = EnumProperty.create("puesto", Puesto.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    /** El mate lleva la bombilla clavada. Solo tiene sentido en los mates con yerba. */
    public static final BooleanProperty BOMBILLA = BooleanProperty.create("bombilla");

    public PuestoBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(PUESTO, Puesto.MATE).setValue(FACING, Direction.NORTH).setValue(BOMBILLA, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PUESTO, FACING, BOMBILLA);
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

    private static ItemStack guardado(BlockState state, PuestoBlockEntity be) {
        return be != null && !be.getStack().isEmpty() ? be.getStack().copy() : new ItemStack(state.getValue(PUESTO).item());
    }

    /** Lo que devuelve el bloque: el item guardado y, si el mate tenia la bombilla puesta, la bombilla. */
    private static List<ItemStack> contenido(BlockState state, PuestoBlockEntity be) {
        List<ItemStack> items = new ArrayList<>();
        items.add(guardado(state, be));
        if (state.getValue(BOMBILLA)) {
            items.add(new ItemStack(MateItems.BOMBILLA.get()));
        }
        return items;
    }

    /** Suelta el item guardado, no uno nuevo: asi no se pierde el estado del mate. */
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return contenido(state, params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof PuestoBlockEntity be ? be : null);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return guardado(state, level.getBlockEntity(pos) instanceof PuestoBlockEntity be ? be : null);
    }

    /**
     * Servir el mate ya puesto. Con yerba suelta o la yerbera se le pone yerba al mate vacio; con
     * azucar, leche o yuyos se le da gusto (antes de cebarlo); la bombilla se clava aparte; y con la
     * pava caliente se ceba (hace falta la bombilla puesta). El mate queda donde esta, solo cambia de estado.
     */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof PuestoBlockEntity be)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        Puesto puesto = state.getValue(PUESTO);
        ItemStack mate = guardado(state, be);
        boolean sirve = puesto.esMateConYerba();
        boolean sinCebar = puesto == Puesto.MATE_CON_YERBA;

        // Yerba al mate vacio
        boolean yerbera = held.is(MateItems.YERBERA.get()) && held.getOrDefault(MateComponents.PORCIONES.get(), 0) > 0;
        if (puesto == Puesto.MATE && (yerbera || held.is(MateItems.YERBA_MATE.get()))) {
            if (!level.isClientSide) {
                if (yerbera) {
                    gastarPorcion(player, held);
                } else if (!player.hasInfiniteMaterials()) {
                    held.shrink(1);
                }
                cambiar(level, pos, state, be, mate.transmuteCopy(MateItems.MATE_CON_YERBA.get(), 1));
                sonido(level, pos, SoundEvents.SAND_PLACE, 1.1F);
                particulas(level, pos, ParticleTypes.COMPOSTER);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // La bombilla se clava aparte, a un mate que ya tiene yerba
        if (held.is(MateItems.BOMBILLA.get()) && sirve && !state.getValue(BOMBILLA)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(BOMBILLA, true), 3);
                if (!player.hasInfiniteMaterials()) {
                    held.shrink(1);
                }
                sonido(level, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, 1.2F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // Darle gusto antes de cebar: azucar, leche o yuyos
        MateSabor sabor = sinCebar ? saborDe(held) : null;
        if (sabor != null) {
            if (mate.getOrDefault(MateComponents.SABOR.get(), MateSabor.AMARGO) == sabor) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                ItemStack gustoso = mate.copy();
                gustoso.set(MateComponents.SABOR.get(), sabor);
                be.setStack(gustoso);
                gastarIngrediente(player, hand, held);
                sonido(level, pos, sabor == MateSabor.LECHE ? SoundEvents.BUCKET_EMPTY : SoundEvents.SAND_PLACE, 1.4F);
                particulas(level, pos, ParticleTypes.HAPPY_VILLAGER);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // Cebar con la pava caliente
        if (sirve && puesto != Puesto.MATE_AMARGO && puesto != Puesto.MATE_DULCE && puesto != Puesto.MATE_DE_LECHE
                && puesto != Puesto.MATE_CON_YUYOS && held.is(MateItems.PAVA_CALIENTE.get())) {
            if (!state.getValue(BOMBILLA)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("message.ultimatefoods.puesto.sin_bombilla"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                MateSabor gusto = mate.getOrDefault(MateComponents.SABOR.get(), MateSabor.AMARGO);
                int cebadas = mate.getOrDefault(MateComponents.CEBADAS.get(), 0) + 1;
                ItemStack cebado = mate.transmuteCopy(gusto.itemCebado(), 1);
                cebado.set(MateComponents.SABOR.get(), gusto);
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
                sonido(level, pos, SoundEvents.BREWING_STAND_BREW, 1.4F);
                sonido(level, pos, SoundEvents.BOTTLE_FILL, 0.9F);
                particulas(level, pos, ParticleTypes.CLOUD);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /** El gusto que da lo que tenes en la mano, o {@code null} si no es un ingrediente del mate. */
    private static MateSabor saborDe(ItemStack held) {
        if (held.is(Items.SUGAR) || (held.is(MateItems.AZUCARERA.get())
                && held.getOrDefault(MateComponents.PORCIONES.get(), 0) > 0)) {
            return MateSabor.DULCE;
        }
        if (held.is(Items.MILK_BUCKET)) {
            return MateSabor.LECHE;
        }
        if (held.is(MateItems.YUYOS.get())) {
            return MateSabor.YUYOS;
        }
        return null;
    }

    private static void gastarPorcion(Player player, ItemStack paquete) {
        if (!player.hasInfiniteMaterials()) {
            paquete.set(MateComponents.PORCIONES.get(), paquete.getOrDefault(MateComponents.PORCIONES.get(), 0) - 1);
        }
    }

    private static void gastarIngrediente(Player player, InteractionHand hand, ItemStack held) {
        if (player.hasInfiniteMaterials()) {
            return;
        }
        if (held.is(MateItems.AZUCARERA.get())) {
            gastarPorcion(player, held);
        } else if (held.is(Items.MILK_BUCKET)) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        } else {
            held.shrink(1);
        }
    }

    private static void sonido(Level level, BlockPos pos, SoundEvent sonido, float pitch) {
        level.playSound(null, pos, sonido, SoundSource.BLOCKS, 0.8F, pitch);
    }

    private static void particulas(Level level, BlockPos pos, net.minecraft.core.particles.ParticleOptions tipo) {
        ((ServerLevel) level).sendParticles(tipo,
                pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, 4, 0.1, 0.05, 0.1, 0.0);
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
            List<ItemStack> items = contenido(state, level.getBlockEntity(pos) instanceof PuestoBlockEntity be ? be : null);
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.4F, 1.0F);
            for (ItemStack stack : items) {
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
