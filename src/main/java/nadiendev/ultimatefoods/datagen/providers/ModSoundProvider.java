package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.registry.SonidosReproducibles;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

    public ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, UltimateFoodsCore.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        addSound(SonidosReproducibles.MONSTER_EAT, "quemacizo");
        addSound(SonidosReproducibles.HAPPY_MEAL_EAT, "cajitafeli");
        addSound(SonidosReproducibles.CAJITA_FELIZ_MCCACO, "cajitafelizmccaco");
        addSound(SonidosReproducibles.SUPER_ENERGY_DRINK, "outrosong");
        addSound(SonidosReproducibles.DORITOS_EAT, "saxofono");
        addSound(SonidosReproducibles.POOP_EAT, "peo");
        addSound(SonidosReproducibles.GALAXY_SOUND, "galaxysound");
        addSound(SonidosReproducibles.LIGHT_RED_AND_GREEN, "luzverdeluzrojamakdonald");
        addSound(SonidosReproducibles.RICKROLL, "rickroll");
        addSound(SonidosReproducibles.AVENGERS, "the_avengers_theme_song");
        addSound(SonidosReproducibles.ERA_PAL_NENE, "me_compre_una_cajita_feliz");
    }

    private void addSound(net.neoforged.neoforge.registries.DeferredHolder<SoundEvent, SoundEvent> soundEvent, String soundName) {
        add(soundEvent, SoundDefinition.definition()
                .with(sound(ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, soundName)))
                .subtitle("sound_event.ultimatefoods." + soundName)
        );
    }

    private void addSoundWithPitch(net.neoforged.neoforge.registries.DeferredHolder<SoundEvent, SoundEvent> soundEvent, String soundName, float pitch) {
        add(soundEvent, SoundDefinition.definition()
                .with(sound(ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, soundName))
                        .pitch(pitch))
                .subtitle("sound_event.ultimatefoods." + soundName)
        );
    }
}
