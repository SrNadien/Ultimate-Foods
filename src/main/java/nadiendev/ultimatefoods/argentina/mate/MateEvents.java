package nadiendev.ultimatefoods.argentina.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.mate.item.MateCebadoItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/** Enganches de juego del mate: pasar el mate, calentar la pava, fin de "Mandale mecha" y bonus de bonanza. */
@EventBusSubscriber(modid = UltimateFoodsCore.MOD_ID)
public final class MateEvents {

    private MateEvents() {
    }

    /** Click derecho con un mate cebado sobre otro jugador o un aldeano: le pasas el mate. */
    @SubscribeEvent
    public static void alPasarElMate(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof MateCebadoItem)) {
            return;
        }
        if (!(event.getTarget() instanceof LivingEntity destino)
                || !(destino instanceof Player || destino instanceof Villager)) {
            return;
        }
        Player quien = event.getEntity();
        if (quien.getCooldowns().isOnCooldown(stack.getItem())) {
            return;
        }
        if (!quien.level().isClientSide) {
            MateRitual.pasarMate(quien, event.getHand(), destino);
        }
        event.setCancellationResult(InteractionResult.sidedSuccess(quien.level().isClientSide));
        event.setCanceled(true);
    }

    /** Pava con agua sobre un horno encendido: que no se abra el horno, que se caliente la pava. */
    @SubscribeEvent
    public static void alCalentarLaPava(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().is(MateItems.PAVA_CON_AGUA.get())
                && MateRitual.esFuenteDeCalor(event.getLevel().getBlockState(event.getPos()))) {
            event.setUseBlock(TriState.FALSE);
        }
    }

    /** Al terminar "Mandale mecha" te queda una lentitud breve (la fiaca despues del sprint). */
    @SubscribeEvent
    public static void alTerminarMandaleMecha(MobEffectEvent.Expired event) {
        MobEffectInstance instancia = event.getEffectInstance();
        if (instancia != null && instancia.getEffect().value() == MateEffects.MANDALE_MECHA.get()) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, true, true));
        }
    }

    /** Bonanza: la experiencia de lo que matas rinde un 50 % mas. */
    @SubscribeEvent
    public static void conBonanza(LivingExperienceDropEvent event) {
        Player jugador = event.getAttackingPlayer();
        if (jugador != null && jugador.hasEffect(MateEffects.BONANZA) && event.getDroppedExperience() > 0) {
            event.setDroppedExperience(event.getDroppedExperience() + Math.max(1, event.getDroppedExperience() / 2));
        }
    }
}
