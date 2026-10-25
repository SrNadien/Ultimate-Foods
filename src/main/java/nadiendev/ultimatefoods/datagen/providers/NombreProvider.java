package nadiendev.ultimatefoods.datagen.providers;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

/** Le da un nombre propio a un provider: el generador no admite dos con el mismo nombre. */
public final class NombreProvider implements DataProvider {

    private final DataProvider proveedor;
    private final String nombre;

    public NombreProvider(String nombre, DataProvider proveedor) {
        this.nombre = nombre;
        this.proveedor = proveedor;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return proveedor.run(cache);
    }

    @Override
    public String getName() {
        return nombre;
    }
}
