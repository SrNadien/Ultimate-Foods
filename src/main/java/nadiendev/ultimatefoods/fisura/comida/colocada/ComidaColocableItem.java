package nadiendev.ultimatefoods.fisura.comida.colocada;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item de una comida criolla que ademas se puede colocar como bloque. Sin agacharse se come
 * como cualquier comida (y si viene en un recipiente, plato hondo o botella, lo devuelve al
 * terminar, como el guiso de hongos). Con Shift + click derecho sobre un bloque la pone al lado.
 */
public class ComidaColocableItem extends Item {

    private final Comida comida;
    @Nullable
    private final ItemLike container;
    private final UseAnim animation;

    public ComidaColocableItem(Properties properties, Comida comida, @Nullable ItemLike container, UseAnim animation) {
        super(properties);
        this.comida = comida;
        this.container = container;
        this.animation = animation;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return animation;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (comida.esCerveza()) {
            tooltip.add(Component.translatable("tooltip.ultimatefoods.birra").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }
        tooltip.add(Component.translatable("tooltip.ultimatefoods.colocable").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        // Sin agacharse no se coloca: PASS deja que el item se coma como siempre.
        if (player == null || !player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        return place(new BlockPlaceContext(context), player);
    }

    private InteractionResult place(BlockPlaceContext context, Player player) {
        if (!context.canPlace()) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = ColocadaRegistry.COMIDA_COLOCADA.get().stateFor(comida, context);
        if (level.isOutsideBuildHeight(pos)
                || !state.canSurvive(level, pos)
                || !level.isUnobstructed(state, pos, CollisionContext.of(player))
                || !player.mayUseItemAt(pos, context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.PASS;
        }
        if (!level.setBlock(pos, state, 11)) {
            return InteractionResult.FAIL;
        }
        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, state));
        SoundType sound = state.getSoundType();
        level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        context.getItemInHand().consume(1, player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (container == null || (entity instanceof Player player && player.hasInfiniteMaterials())) {
            return result;
        }
        ItemStack leftover = new ItemStack(container);
        if (result.isEmpty()) {
            return leftover;
        }
        if (entity instanceof Player player && !player.getInventory().add(leftover)) {
            player.drop(leftover, false);
        }
        return result;
    }
}
