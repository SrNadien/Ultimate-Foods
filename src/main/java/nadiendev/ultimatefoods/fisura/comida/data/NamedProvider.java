package nadiendev.ultimatefoods.fisura.comida.data;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

/**
 * Le cambia el nombre a un provider cuyo {@code getName()} es final (recetas, loot). El generador
 * rechaza dos providers con el mismo nombre, y el mod ya tiene los suyos con el nombre de vanilla.
 */
public record NamedProvider(String name, DataProvider delegate) implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return delegate.run(output);
    }

    @Override
    public String getName() {
        return name;
    }
}
