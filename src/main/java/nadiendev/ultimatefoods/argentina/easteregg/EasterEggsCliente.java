package nadiendev.ultimatefoods.argentina.easteregg;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.registry.SonidosReproducibles;
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

    /** Cambia el murmullo del comerciante errante por el pregon; el resto de sus sonidos queda igual. */
    @SubscribeEvent
    public static void comerciante(PlaySoundEvent event) {
        SoundInstance original = event.getSound();
        if (original == null || !original.getLocation().equals(SoundEvents.WANDERING_TRADER_AMBIENT.getLocation())) {
            return;
        }
        event.setSound(new SimpleSoundInstance(SonidosReproducibles.VENDO_CARROS.get(), original.getSource(),
                1.0F, 1.0F, RandomSource.create(), original.getX(), original.getY(), original.getZ()));
    }
}
