package nadiendev.ultimatefoods.argentina.mate.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

/**
 * Le da un nombre propio a un provider. El generador no admite dos providers con el mismo
 * nombre (y varios, como el de recetas o el de loot, tienen el nombre fijo), asi que este
 * modulo envuelve los suyos para no chocar con los demas.
 */
public final class NamedProvider implements DataProvider {

    private final String name;
    private final DataProvider delegate;

    public NamedProvider(String name, DataProvider delegate) {
        this.name = name;
        this.delegate = delegate;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return delegate.run(output);
    }

    @Override
    public String getName() {
        return name;
    }
}
