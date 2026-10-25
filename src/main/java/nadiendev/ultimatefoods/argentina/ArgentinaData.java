package nadiendev.ultimatefoods.argentina;

import nadiendev.ultimatefoods.argentina.comida.ComidaModule;
import nadiendev.ultimatefoods.argentina.homenaje.HomenajeModule;
import nadiendev.ultimatefoods.argentina.mate.MateModule;
import nadiendev.ultimatefoods.datagen.providers.LangDatos;
import nadiendev.ultimatefoods.datagen.providers.ModLanguageProvider;
import nadiendev.ultimatefoods.datagen.providers.ModelosBloquesArgentina;
import nadiendev.ultimatefoods.datagen.providers.ModelosCustomComidas;
import nadiendev.ultimatefoods.datagen.providers.ModelosItemsArgentina;
import nadiendev.ultimatefoods.datagen.providers.ModelosItemsComidas;
import nadiendev.ultimatefoods.datagen.providers.NombreProvider;
import nadiendev.ultimatefoods.datagen.providers.RawRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Un solo punto de entrada al datagen. Cada modulo agrega sus propios providers y
 * aca se suman los que son de todos: los textos en cuatro idiomas, los modelos 3D y las
 * recetas de Farmer's Delight.
 */
public final class ArgentinaData {

    private ArgentinaData() {
    }

    public static void gather(GatherDataEvent event) {
        MateModule.gatherData(event);
        ComidaModule.gatherData(event);
        HomenajeModule.gatherData(event);

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // Modelos 3D. Llevan nombre propio porque "Item Models: ultimatefoods" ya lo usan otros providers.
        generator.addProvider(event.includeClient(),
                new NombreProvider("Argentina: modelos 3D de bloques", new ModelosBloquesArgentina(output, helper)));
        generator.addProvider(event.includeClient(),
                new NombreProvider("Argentina: modelos 3D de items", new ModelosItemsArgentina(output, helper)));

        generator.addProvider(event.includeClient(),
                new NombreProvider("Comidas originales: modelos de item", new ModelosItemsComidas(output, helper)));
        generator.addProvider(event.includeClient(),
                new NombreProvider("Comidas originales: modelos custom", new ModelosCustomComidas(output, helper)));

        generator.addProvider(event.includeServer(), new RawRecipeProvider(output));

        // El español sirve igual para España, Argentina y Mexico.
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "en_us", LangDatos.EN));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "es_es", LangDatos.ES));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "es_ar", LangDatos.ES));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "es_mx", LangDatos.ES));
    }
}
