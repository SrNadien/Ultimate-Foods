package nadiendev.ultimatefoods.fisura.cacerola;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.Collection;

/**
 * Donde estan las recetas de la cacerola. En 26.1 las recetas viven solo en el servidor; el cliente
 * no las tiene salvo que el servidor se las mande. Como el cliente necesita saberlas (para no ponerse
 * a comer un ingrediente en vez de cargarlo en la olla, y para JEI), el servidor las manda al
 * sincronizar los datapacks ({@link #enviarAlCliente}) y {@link CacerolaCliente} las guarda aca.
 */
public final class CacerolaRecetas {

    /** Las recetas que mando el servidor (solo del lado del cliente). */
    private static volatile RecipeMap delCliente = RecipeMap.EMPTY;

    private CacerolaRecetas() {
    }

    /** Todas las recetas de la cacerola: las del servidor si {@code level} es del servidor, si no las sincronizadas. */
    public static Collection<RecipeHolder<CacerolaRecipe>> todas(Level level) {
        if (level instanceof ServerLevel server) {
            return server.recipeAccess().recipeMap().byType(CacerolaRegistry.CACEROLA_TYPE.get());
        }
        return delCliente();
    }

    /** Las recetas que el servidor le mando al cliente (vacio si todavia no llegaron). */
    public static Collection<RecipeHolder<CacerolaRecipe>> delCliente() {
        return delCliente.byType(CacerolaRegistry.CACEROLA_TYPE.get());
    }

    /** Al sincronizar datapacks (entrar al mundo o /reload), el servidor manda tambien las recetas de cacerola. */
    static void enviarAlCliente(OnDatapackSyncEvent event) {
        event.sendRecipes(CacerolaRegistry.CACEROLA_TYPE.get());
    }

    static void recibidas(RecipeMap recetas) {
        delCliente = recetas;
    }

    static void olvidar() {
        delCliente = RecipeMap.EMPTY;
    }
}
