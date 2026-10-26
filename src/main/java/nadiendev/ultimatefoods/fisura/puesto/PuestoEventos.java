package nadiendev.ultimatefoods.fisura.puesto;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.mate.MateRitual;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Apoyar un mate o la pava: Shift + click derecho sobre un bloque con el item en la mano. Se hace
 * por evento para no tocar las clases de los items del mate, que ya tienen su propio click derecho
 * (cebar, pasar el mate, calentar la pava).
 */
@EventBusSubscriber(modid = UltimateFoodsCore.MOD_ID)
public final class PuestoEventos {

    private PuestoEventos() {
    }

    @SubscribeEvent
    public static void alApoyar(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Puesto puesto = Puesto.de(stack);
        if (puesto == null || !player.isSecondaryUseActive()) {
            return;
        }
        Level level = event.getLevel();
        // La pava con agua sobre un fuego se calienta; ahi no se apoya.
        if (MateRitual.esFuenteDeCalor(level.getBlockState(event.getPos()))) {
            return;
        }
        BlockPlaceContext context = new BlockPlaceContext(new UseOnContext(player, event.getHand(), event.getHitVec()));
        if (!context.canPlace()) {
            return;
        }
        BlockPos pos = context.getClickedPos();
        BlockState state = PuestoRegistry.MATE_PUESTO.get().stateFor(puesto, context);
        if (level.isOutsideBuildHeight(pos)
                || !state.canSurvive(level, pos)
                || !level.isUnobstructed(state, pos, CollisionContext.of(player))
                || !player.mayUseItemAt(pos, context.getClickedFace(), stack)) {
            return;
        }
        if (!level.isClientSide()) {
            if (!level.setBlock(pos, state, 11)) {
                return;
            }
            if (level.getBlockEntity(pos) instanceof PuestoBlockEntity be) {
                be.setStack(stack.copyWithCount(1));
            }
            level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, state));
            SoundType sound = state.getSoundType();
            level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            stack.consume(1, player);
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if (Puesto.de(event.getItemStack()) != null) {
            event.getToolTip().add(Component.translatable("tooltip.ultimatefoods.colocable").withStyle(ChatFormatting.GRAY));
        }
    }
}
