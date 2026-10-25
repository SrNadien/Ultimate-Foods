package nadiendev.ultimatefoods.fisura.homenaje;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.text.Normalizer;
import java.util.Set;

/**
 * Easter egg: ponerle a un aldeano una etiqueta con el nombre de Messi hace que lo
 * festeje, y desde ese momento el aldeano vende La Copa. Pasa una sola vez por aldeano.
 */
@EventBusSubscriber(modid = UltimateFoodsCore.MOD_ID)
public final class MessiEasterEgg {

    /** Nombres que lo activan, ya sin tildes ni mayusculas. */
    private static final Set<String> NOMBRES = Set.of(
            "messi", "lionel messi", "lionel andres messi", "leo messi", "la pulga");

    /** Marca guardada en el aldeano para que no se repita. */
    private static final String MARCA = UltimateFoodsCore.MOD_ID + ".messi";

    private MessiEasterEgg() {
    }

    @SubscribeEvent
    public static void alUsarEtiqueta(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        ItemStack etiqueta = event.getItemStack();
        if (!etiqueta.is(Items.NAME_TAG) || !(event.getTarget() instanceof Villager aldeano)) {
            return;
        }
        Component nombre = etiqueta.get(DataComponents.CUSTOM_NAME);
        if (nombre == null || !NOMBRES.contains(normalizar(nombre.getString()))) {
            return;
        }
        if (aldeano.isBaby() || aldeano.getPersistentData().getBoolean(MARCA)) {
            return;
        }
        aldeano.getPersistentData().putBoolean(MARCA, true);

        // el aldeano ya con su oferta: la copa, una sola vez, por 32 esmeraldas
        aldeano.getOffers().add(new MerchantOffer(
                new ItemCost(Items.EMERALD, 32), new ItemStack(HomenajeModule.LA_COPA_ITEM.get()), 1, 0, 0.05f));

        festejar(level, aldeano, event.getEntity());
    }

    private static void festejar(ServerLevel level, Villager aldeano, Player jugador) {
        double x = aldeano.getX(), y = aldeano.getY() + 1.0, z = aldeano.getZ();
        level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 60, 0.5, 0.8, 0.5, 0.12);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y + 0.5, z, 20, 0.6, 0.6, 0.6, 0.0);
        level.playSound(null, aldeano.blockPosition(), SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.NEUTRAL, 1.0F, 1.0F);
        level.playSound(null, aldeano.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0F, 1.2F);

        Component mensaje = Component.translatable("easter_egg.ultimatefoods.messi")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD);
        for (ServerPlayer cerca : level.getPlayers(p -> p.distanceToSqr(aldeano) < 24 * 24)) {
            cerca.displayClientMessage(mensaje, false);
        }
    }

    private static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.trim().toLowerCase();
    }
}
