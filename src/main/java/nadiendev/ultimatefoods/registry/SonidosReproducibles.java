package nadiendev.ultimatefoods.registry;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SonidosReproducibles {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MONSTER_EAT = registerSound("quemacizo");

    public static final DeferredHolder<SoundEvent, SoundEvent> SUPER_ENERGY_DRINK = registerSound("outrosong");

    public static final DeferredHolder<SoundEvent, SoundEvent> DORITOS_EAT = registerSound("saxofono");

    public static final DeferredHolder<SoundEvent, SoundEvent> POOP_EAT = registerSound("peo");

    public static final DeferredHolder<SoundEvent, SoundEvent> RICKROLL = registerSound("rickroll");

    public static final DeferredHolder<SoundEvent, SoundEvent> ERA_PAL_NENE = registerSound("me_compre_una_cajita_feliz");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
