package nadiendev.ultimatefoods.fisura;

import nadiendev.ultimatefoods.fisura.comida.ComidaModule;
import nadiendev.ultimatefoods.fisura.homenaje.HomenajeModule;
import nadiendev.ultimatefoods.fisura.puesto.PuestoData;
import nadiendev.ultimatefoods.fisura.mate.MateModule;
import nadiendev.ultimatefoods.datagen.providers.LangDatos;
import nadiendev.ultimatefoods.datagen.providers.ModLanguageProvider;
import nadiendev.ultimatefoods.datagen.providers.ModelosBloquesFisura;
import nadiendev.ultimatefoods.datagen.providers.ModelosCustomComidas;
import nadiendev.ultimatefoods.datagen.providers.ModelosItemsFisura;
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
public final class FisuraData {

    private FisuraData() {
    }

    public static void gather(GatherDataEvent event) {
        MateModule.gatherData(event);
        ComidaModule.gatherData(event);
        HomenajeModule.gatherData(event);
        PuestoData.gatherData(event);

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // Modelos 3D. Llevan nombre propio porque "Item Models: ultimatefoods" ya lo usan otros providers.
        generator.addProvider(event.includeClient(),
                new NombreProvider("Fisura: modelos 3D de bloques", new ModelosBloquesFisura(output, helper)));
        generator.addProvider(event.includeClient(),
                new NombreProvider("Fisura: modelos 3D de items", new ModelosItemsFisura(output, helper)));

        generator.addProvider(event.includeClient(),
                new NombreProvider("Comidas originales: modelos de item", new ModelosItemsComidas(output, helper)));
        generator.addProvider(event.includeClient(),
                new NombreProvider("Comidas originales: modelos custom", new ModelosCustomComidas(output, helper)));

        generator.addProvider(event.includeServer(), new RawRecipeProvider(output));

        // El español sirve igual para España, el Rio de la Plata y Mexico.
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "en_us", LangDatos.EN));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "es_es", LangDatos.ES));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "es_ar", LangDatos.ES));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "es_mx", LangDatos.ES));
    }
}
