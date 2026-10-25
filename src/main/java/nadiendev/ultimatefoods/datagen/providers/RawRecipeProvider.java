package nadiendev.ultimatefoods.datagen.providers;

import com.google.gson.JsonParser;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Escribe las recetas de Farmer's Delight. No pasan por RecipeProvider porque sus tipos
 * (tabla de cortar y olla) no existen en el classpath de compilacion de este mod.
 */
public class RawRecipeProvider implements DataProvider {

    private final PackOutput output;

    public RawRecipeProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path base = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(UltimateFoodsCore.MOD_ID).resolve("recipe").resolve("argentina");
        List<CompletableFuture<?>> tareas = new ArrayList<>();
        RecetasCrudas.RECETAS.forEach((id, json) ->
                tareas.add(DataProvider.saveStable(cache, JsonParser.parseString(json), base.resolve(id + ".json"))));
        return CompletableFuture.allOf(tareas.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Argentina: recetas de Farmer's Delight";
    }
}
