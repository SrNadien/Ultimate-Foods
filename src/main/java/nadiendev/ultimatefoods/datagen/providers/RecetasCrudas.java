package nadiendev.ultimatefoods.datagen.providers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Recetas de Farmer's Delight (tabla de cortar y olla). Se dan como JSON porque sus
 * tipos de receta no estan en el classpath de compilacion. La clave es el nombre del
 * archivo dentro de data/ultimatefoods/recipe/argentina/.
 */
public final class RecetasCrudas {

    public static final Map<String, String> RECETAS = new LinkedHashMap<>();

    static {
        RECETAS.put("cheddar", "{\"type\":\"farmersdelight:cutting\",\"ingredients\":[{\"item\":\"ultimatefoods:queso\"}],\"result\":[{\"item\":{\"count\":3,\"id\":\"ultimatefoods:cheddar\"}}],\"tool\":[{\"type\":\"farmersdelight:item_ability\",\"action\":\"knife_dig\"},{\"tag\":\"c:tools/knife\"}]}");
        RECETAS.put("dulce_de_leche", "{\"type\":\"farmersdelight:cooking\",\"cookingtime\":400,\"experience\":0.5,\"ingredients\":[{\"type\":\"neoforge:compound\",\"children\":[{\"tag\":\"c:drinks/milk\"},{\"item\":\"minecraft:milk_bucket\"}]},{\"item\":\"minecraft:sugar\"},{\"item\":\"minecraft:sugar\"}],\"recipe_book_tab\":\"misc\",\"result\":{\"count\":2,\"id\":\"ultimatefoods:dulce_de_leche\"}}");
        RECETAS.put("dulce_de_membrillo", "{\"type\":\"farmersdelight:cooking\",\"cookingtime\":400,\"experience\":0.5,\"ingredients\":[{\"item\":\"minecraft:apple\"},{\"item\":\"minecraft:apple\"},{\"item\":\"minecraft:sugar\"},{\"item\":\"minecraft:sugar\"}],\"recipe_book_tab\":\"misc\",\"result\":{\"count\":2,\"id\":\"ultimatefoods:dulce_de_membrillo\"}}");
        RECETAS.put("noquis", "{\"type\":\"farmersdelight:cooking\",\"container\":{\"count\":1,\"id\":\"minecraft:bowl\"},\"experience\":1,\"ingredients\":[{\"tag\":\"c:crops/potato\"},{\"tag\":\"c:crops/potato\"},{\"tag\":\"c:foods/dough\"},{\"tag\":\"c:crops/tomato\"}],\"recipe_book_tab\":\"meals\",\"result\":{\"count\":1,\"id\":\"ultimatefoods:noquis\"}}");
        RECETAS.put("pan_rallado", "{\"type\":\"farmersdelight:cutting\",\"ingredients\":[{\"item\":\"minecraft:bread\"}],\"result\":[{\"item\":{\"count\":3,\"id\":\"ultimatefoods:pan_rallado\"}}],\"tool\":[{\"type\":\"farmersdelight:item_ability\",\"action\":\"knife_dig\"},{\"tag\":\"c:tools/knife\"}]}");
        RECETAS.put("queso", "{\"type\":\"farmersdelight:cooking\",\"cookingtime\":300,\"experience\":0.3,\"ingredients\":[{\"type\":\"neoforge:compound\",\"children\":[{\"tag\":\"c:drinks/milk\"},{\"item\":\"minecraft:milk_bucket\"}]},{\"type\":\"neoforge:compound\",\"children\":[{\"tag\":\"c:drinks/milk\"},{\"item\":\"minecraft:milk_bucket\"}]},{\"item\":\"minecraft:bone_meal\"}],\"recipe_book_tab\":\"misc\",\"result\":{\"count\":2,\"id\":\"ultimatefoods:queso\"}}");
    }

    private RecetasCrudas() {
    }
}
