package nadiendev.ultimatefoods.sounds;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * SonidosReproducibles
 * By NadienDev
 * Clase para registrar todos los sonidos personalizados del mod
 * Facilita agregar nuevos sonidos y reproducirlos al consumir alimentos o realizar acciones específicas
 */
public class SonidosReproducibles {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, UltimateFoodsCore.MOD_ID);

    // Sonido para Monster
    public static final DeferredHolder<SoundEvent, SoundEvent> MONSTER_EAT = registerSound("quemacizo");

    // Sonido para Cajita Feliz
    public static final DeferredHolder<SoundEvent, SoundEvent> HAPPY_MEAL_EAT = registerSound("cajitafeli");

    // Sonido para Cajita Feliz McCaco
    public static final DeferredHolder<SoundEvent, SoundEvent> CAJITA_FELIZ_MCCACO = registerSound("cajitafelizmccaco");

    // Sonido para Super Energy Drink
    public static final DeferredHolder<SoundEvent, SoundEvent> SUPER_ENERGY_DRINK = registerSound("outrosong");

    // Sonido para Doritos
    public static final DeferredHolder<SoundEvent, SoundEvent> DORITOS_EAT = registerSound("saxofono");

    // Sonido para Poop
    public static final DeferredHolder<SoundEvent, SoundEvent> POOP_EAT = registerSound("peo");

    // Galaxy Sound
    public static final DeferredHolder<SoundEvent, SoundEvent> GALAXY_SOUND = registerSound("galaxysound");

    // Luz roja luz verde
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHT_RED_AND_GREEN = registerSound("luzverdeluzrojamakdonald");

    // Rickroll
    public static final DeferredHolder<SoundEvent, SoundEvent> RICKROLL = registerSound("rickroll");

    // AVENGERS
    public static final DeferredHolder<SoundEvent, SoundEvent> AVENGERS = registerSound("the_avengers_theme_song");

    // PENSE QUE ERA PAL NENE
    public static final DeferredHolder<SoundEvent, SoundEvent> ERA_PAL_NENE = registerSound("me_compre_una_cajita_feliz");

    // ── Jukebox Song Keys ─────────────────────────────────────────────────────

    public static final ResourceKey<JukeboxSong> DISC_MCCACAO_KEY = createSong("cajitafeli");
    public static final ResourceKey<JukeboxSong> DISC_AVENGERS_KEY = createSong("the_avengers_theme_song");

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name));
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}