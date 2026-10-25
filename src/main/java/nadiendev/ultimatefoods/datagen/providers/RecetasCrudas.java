package nadiendev.ultimatefoods.datagen.providers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Recetas de Farmer's Delight (tabla de cortar y olla). Se dan como JSON porque sus
 * tipos de receta no estan en el classpath de compilacion. La clave es el nombre del
 * archivo dentro de data/ultimatefoods/recipe/fisura/.
 */
public final class RecetasCrudas {

    public static final Map<String, String> RECETAS = new LinkedHashMap<>();

    static {
        RECETAS.put("cheddar", "{\"type\":\"farmersdelight:cutting\",\"ingredients\":[{\"item\":\"ultimatefoods:queso\"}],\"result\":[{\"item\":{\"count\":3,\"id\":\"ultimatefoods:cheddar\"}}],\"tool\":[{\"type\":\"farmersdelight:item_ability\",\"action\":\"knife_dig\"},{\"tag\":\"c:tools/knife\"}]}");
        RECETAS.put("dulce_de_leche", "{\"type\":\"farmersdelight:cooking\",\"cookingtime\":400,\"experience\":0.5,\"ingredients\":[{\"type\":\"neoforge:compound\",\"children\":[{\"tag\":\"c:drinks/milk\"},{\"item\":\"minecraft:milk_bucket\"}]},{\"item\":\"minecraft:sugar\"},{\"item\":\"minecraft:sugar\"}],\"recipe_book_tab\":\"misc\",\"result\":{\"count\":2,\"id\":\"ultimatefoods:dulce_de_leche\"}}");
        RECETAS.put("dulce_de_membrillo", "{\"type\":\"farmersdelight:cooking\",\"cookingtime\":400,\"experience\":0.5,\"ingredients\":[{\"item\":\"minecraft:apple\"},{\"item\":\"minecraft:apple\"},{\"item\":\"minecraft:sugar\"},{\"item\":\"minecraft:sugar\"}],\"recipe_book_tab\":\"misc\",\"result\":{\"count\":2,\"id\":\"ultimatefoods:dulce_de_membrillo\"}}");
        RECETAS.put("noquis", "{\"type\":\"farmersdelight:cooking\",\"container\":{\"count\":1,\"id\":\"minecraft:bowl\"},\"experience\":1,\"ingredients\":[{\"tag\":\"c:crops/potato\"},{\"tag\":\"c:crops/potato\"},{\"tag\":\"c:foods/dough\"},{\"tag\":\"c:crops/tomato\"}],\"recipe_book_tab\":\"meals\",\"result\":{\"count\":1,\"id\":\"ultimatefoods:noquis\"}}");
        RECETAS.put("pan_rallado", "{\"type\":\"farmersdelight:cutting\",\"ingredients\":[{\"item\":\"minecraft:bread\"}],\"result\":[{\"item\":{\"count\":3,\"id\":\"ultimatefoods:pan_rallado\"}}],\"tool\":[{\"type\":\"farmersdelight:item_ability\",\"action\":\"knife_dig\"},{\"tag\":\"c:tools/knife\"}]}");
        // Bebidas (mesa de crafteo, con una botella de vidrio) y flan (olla de Farmer's Delight).
        RECETAS.put("quilmes", "{\"type\":\"minecraft:crafting_shapeless\",\"category\":\"misc\",\"ingredients\":[{\"item\":\"minecraft:glass_bottle\"},{\"item\":\"minecraft:wheat\"},{\"item\":\"minecraft:wheat\"},{\"item\":\"minecraft:sugar\"}],\"result\":{\"count\":1,\"id\":\"ultimatefoods:quilmes\"}}");
        RECETAS.put("stella_artois", "{\"type\":\"minecraft:crafting_shapeless\",\"category\":\"misc\",\"ingredients\":[{\"item\":\"minecraft:glass_bottle\"},{\"item\":\"minecraft:wheat\"},{\"item\":\"minecraft:wheat\"},{\"item\":\"minecraft:fern\"}],\"result\":{\"count\":1,\"id\":\"ultimatefoods:stella_artois\"}}");
        RECETAS.put("andes", "{\"type\":\"minecraft:crafting_shapeless\",\"category\":\"misc\",\"ingredients\":[{\"item\":\"minecraft:glass_bottle\"},{\"item\":\"minecraft:wheat\"},{\"item\":\"minecraft:wheat\"},{\"item\":\"minecraft:snowball\"}],\"result\":{\"count\":1,\"id\":\"ultimatefoods:andes\"}}");
        RECETAS.put("fernet_branca", "{\"type\":\"minecraft:crafting_shapeless\",\"category\":\"misc\",\"ingredients\":[{\"item\":\"minecraft:glass_bottle\"},{\"item\":\"minecraft:nether_wart\"},{\"item\":\"minecraft:cocoa_beans\"},{\"item\":\"minecraft:sweet_berries\"}],\"result\":{\"count\":1,\"id\":\"ultimatefoods:fernet_branca\"}}");
        RECETAS.put("flan_con_dulce_de_leche", "{\"type\":\"farmersdelight:cooking\",\"cookingtime\":300,\"experience\":0.5,\"ingredients\":[{\"type\":\"neoforge:compound\",\"children\":[{\"tag\":\"c:drinks/milk\"},{\"item\":\"minecraft:milk_bucket\"}]},{\"tag\":\"c:eggs\"},{\"item\":\"minecraft:sugar\"},{\"item\":\"ultimatefoods:dulce_de_leche\"}],\"recipe_book_tab\":\"misc\",\"result\":{\"count\":2,\"id\":\"ultimatefoods:flan_con_dulce_de_leche\"}}");
        // Solo existe si esta Patchouli: un libro, una yerba y la guia.
        RECETAS.put("guia", "{\"neoforge:conditions\":[{\"type\":\"neoforge:mod_loaded\",\"modid\":\"patchouli\"}],\"type\":\"minecraft:crafting_shapeless\",\"category\":\"misc\",\"ingredients\":[{\"item\":\"minecraft:book\"},{\"item\":\"ultimatefoods:yerba_mate\"}],\"result\":{\"count\":1,\"id\":\"patchouli:guide_book\",\"components\":{\"patchouli:book\":\"ultimatefoods:guia_buela\"}}}");
        RECETAS.put("queso", "{\"type\":\"farmersdelight:cooking\",\"cookingtime\":300,\"experience\":0.3,\"ingredients\":[{\"type\":\"neoforge:compound\",\"children\":[{\"tag\":\"c:drinks/milk\"},{\"item\":\"minecraft:milk_bucket\"}]},{\"type\":\"neoforge:compound\",\"children\":[{\"tag\":\"c:drinks/milk\"},{\"item\":\"minecraft:milk_bucket\"}]},{\"item\":\"minecraft:bone_meal\"}],\"recipe_book_tab\":\"misc\",\"result\":{\"count\":2,\"id\":\"ultimatefoods:queso\"}}");
    }

    private RecetasCrudas() {
    }
}
