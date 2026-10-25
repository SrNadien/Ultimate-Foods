package nadiendev.ultimatefoods.fisura.comida.colocada;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Una comida criolla puesta sobre un bloque (Shift + click derecho con el item). Un solo bloque
 * para las 24: la propiedad {@link #COMIDA} dice cual es y {@link #FACING} hacia donde mira. No tiene
 * block entity: el modelo sale del blockstate.
 *
 * <ul>
 *   <li>Click derecho (sin agacharse): se la come, con la nutricion y los efectos del item.</li>
 *   <li>Shift + click derecho con las manos vacias: la recoge como item.</li>
 *   <li>Romperla suelta el item. Si falta el bloque de abajo, se rompe sola y tambien lo suelta.</li>
 * </ul>
 */
public class ComidaColocadaBlock extends BaseEntityBlock {

    public static final MapCodec<ComidaColocadaBlock> CODEC = simpleCodec(ComidaColocadaBlock::new);
    public static final EnumProperty<Comida> COMIDA = EnumProperty.create("comida", Comida.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    /** Bocados ya sacados (solo lo usan las comidas que se comen por partes, como el asado). */
    public static final IntegerProperty BOCADOS = IntegerProperty.create("bocados", 0, 3);

    public ComidaColocadaBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(COMIDA, Comida.ASADO).setValue(FACING, Direction.NORTH).setValue(BOCADOS, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // El block entity no guarda nada: solo le da a cada comida colocada su propio nombre en los visores
    // de bloques (Jade y compania), que leen el nombre de la entidad.
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ComidaColocadaBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COMIDA, FACING, BOCADOS);
    }

    /** Estado para colocar {@code comida} mirando hacia quien la pone. */
    public BlockState stateFor(Comida comida, BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(COMIDA, comida)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(COMIDA).shape(state.getValue(FACING));
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

    /** Sin base firme se rompe soltando el item (por eso no se hace en updateShape, que no suelta nada). */
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide && !state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(state.getValue(COMIDA).item());
    }

    // ---- Interaccion -----------------------------------------------------------------------------

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        Comida comida = state.getValue(COMIDA);
        ItemStack stack = new ItemStack(comida.item());

        // Si el jugador esta agachado aca solo llega con las dos manos vacias: la recoge.
        if (player.isSecondaryUseActive()) {
            if (state.getValue(BOCADOS) > 0) {
                // Ya empezada: no se levanta entera (seria duplicar lo que ya se comio).
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.4F, 1.0F);
                give(player, stack);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null && !player.canEat(food.canAlwaysEat())) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.ultimatefoods.comida_colocada.llena"), true);
            }
            return InteractionResult.CONSUME;
        }

        int bocados = comida.bocados();
        int comidos = state.getValue(BOCADOS);
        if (bocados > 1) {
            if (!level.isClientSide) {
                comerBocado(player, food, bocados, comidos + 1 >= bocados);
                if (comidos + 1 >= bocados) {
                    level.removeBlock(pos, false);
                    level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                } else {
                    level.setBlock(pos, state.setValue(BOCADOS, comidos + 1), 3);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            // Come exactamente igual que el item: nutricion, efectos, sonido y recipiente.
            ItemStack result = stack.finishUsingItem(level, player);
            // Si el item deja un recipiente (plato hondo, botella) viene como resultado distinto de la comida;
            // en creativo vuelve la propia comida y no hay nada que entregar.
            if (!result.isEmpty() && !result.is(comida.item())) {
                give(player, result);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * Un bocado de una comida que se come por partes: una parte de la nutricion y la saciedad en cada
     * uno, y los efectos (el empacho, la panza llena...) recien al terminarla.
     */
    private static void comerBocado(Player player, FoodProperties food, int bocados, boolean ultimo) {
        FoodProperties parte = new FoodProperties(Math.max(1, food.nutrition() / bocados), food.saturation() / bocados,
                true, food.eatSeconds(), java.util.Optional.empty(), java.util.List.of());
        player.getFoodData().eat(parte);
        player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS,
                0.8F, 0.9F + player.getRandom().nextFloat() * 0.2F);
        if (ultimo) {
            for (FoodProperties.PossibleEffect efecto : food.effects()) {
                if (player.getRandom().nextFloat() < efecto.probability()) {
                    player.addEffect(new MobEffectInstance(efecto.effect()));
                }
            }
        }
    }

    private static void give(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}
