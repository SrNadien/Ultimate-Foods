package nadiendev.ultimatefoods.fisura.easteregg;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.registry.SonidosReproducibles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

/**
 * Easter eggs del lado cliente: el carpincho (cerdo renombrado) y el comerciante errante que
 * grita "vendo carros y camionetas". El sol con cara va como textura propia del mod.
 */
@EventBusSubscriber(modid = UltimateFoodsCore.MOD_ID, value = Dist.CLIENT)
public final class EasterEggsCliente {

    private EasterEggsCliente() {
    }

    @SubscribeEvent
    public static void registrarRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.PIG, CarpinchoRenderer::new);
    }

    /** Duracion del pregon (13,5 s) mas un margen: mientras suena no se arranca otro encima. */
    private static final long PREGON_MS = 14_500L;

    private static SoundInstance pregon;
    private static long pregonDesde;

    /**
     * Cambia el murmullo del comerciante errante por el pregon; el resto de sus sonidos queda igual.
     * El murmullo se repite cada pocos segundos (y de nuevo al ponerse invisible de noche): si el pregon
     * todavia esta sonando, esos murmullos se descartan para que no se pisen ni se reinicie.
     */
    @SubscribeEvent
    public static void comerciante(PlaySoundEvent event) {
        SoundInstance original = event.getSound();
        if (original == null || !original.getLocation().equals(SoundEvents.WANDERING_TRADER_AMBIENT.getLocation())) {
            return;
        }
        long ahora = System.currentTimeMillis();
        boolean sonando = pregon != null
                && (ahora - pregonDesde < PREGON_MS || Minecraft.getInstance().getSoundManager().isActive(pregon));
        if (sonando) {
            event.setSound(null);
            return;
        }
        pregon = new SimpleSoundInstance(SonidosReproducibles.VENDO_CARROS.get(), original.getSource(),
                1.0F, 1.0F, RandomSource.create(), original.getX(), original.getY(), original.getZ());
        pregonDesde = ahora;
        event.setSound(pregon);
    }
}
