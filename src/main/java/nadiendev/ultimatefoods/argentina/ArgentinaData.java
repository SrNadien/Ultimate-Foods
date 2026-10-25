package nadiendev.ultimatefoods.argentina;

import nadiendev.ultimatefoods.argentina.comida.ComidaModule;
import nadiendev.ultimatefoods.argentina.mate.MateModule;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/** Un solo punto de entrada al datagen; cada modulo agrega sus propios providers. */
public final class ArgentinaData {

    private ArgentinaData() {
    }

    public static void gather(GatherDataEvent event) {
        MateModule.gatherData(event);
        ComidaModule.gatherData(event);
    }
}
