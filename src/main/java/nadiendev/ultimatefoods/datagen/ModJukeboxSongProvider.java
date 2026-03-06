package nadiendev.ultimatefoods.datagen;

import com.google.gson.JsonObject;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.sounds.SonidosReproducibles;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ModJukeboxSongProvider implements DataProvider {

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public ModJukeboxSongProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.lookupProvider = lookupProvider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return lookupProvider.thenCompose(provider -> {
            PackOutput.PathProvider pathProvider = output.createPathProvider(
                PackOutput.Target.DATA_PACK, "jukebox_song"
            );

            // disc_mccacao — cajita feliz: 2:43 = 163 segundos
            CompletableFuture<?> mccacao = saveJukeboxSong(cache, pathProvider,
                SonidosReproducibles.DISC_MCCACAO_KEY.location().getPath(),
                "ultimatefoods:cajitafeli",
                "jukebox_song.ultimatefoods.disc_mccacao",
                163, 1
            );

            // disc_avengers — avengers: 2:01 = 121 segundos
            CompletableFuture<?> avengers = saveJukeboxSong(cache, pathProvider,
                SonidosReproducibles.DISC_AVENGERS_KEY.location().getPath(),
                "ultimatefoods:the_avengers_theme_song",
                "jukebox_song.ultimatefoods.disc_avengers",
                121, 2
            );

            return CompletableFuture.allOf(mccacao, avengers);
        });
    }

    private CompletableFuture<?> saveJukeboxSong(CachedOutput cache, PackOutput.PathProvider pathProvider,
                                                   String name, String soundEvent, String descKey,
                                                   int lengthSeconds, int comparatorOutput) {
        Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name));

        JsonObject desc = new JsonObject();
        desc.addProperty("translate", descKey);

        JsonObject json = new JsonObject();
        json.addProperty("sound_event", soundEvent);
        json.add("description", desc);
        json.addProperty("length_in_seconds", lengthSeconds);
        json.addProperty("comparator_output", comparatorOutput);

        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "UltimateFoods JukeboxSongs";
    }
}