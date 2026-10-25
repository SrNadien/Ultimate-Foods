package nadiendev.ultimatefoods.fisura.comida;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.comida.colocada.Comida;
import nadiendev.ultimatefoods.fisura.comida.colocada.ComidaColocableItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Comidas, bebidas e ingredientes criollos. Todos son items "planos" con la textura
 * {@code ultimatefoods:item/fisura/<id>}.
 */
public final class ComidaItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);

    /** Items en el orden en que aparecen en la pestaña creativa. */
    private static final List<DeferredItem<? extends Item>> TAB_ORDER = new ArrayList<>();

    /** Ids de los items con modelo plano generado por datagen. */
    private static final List<String> FLAT_IDS = new ArrayList<>();

    private static final float RAW_FOOD_POISON_CHANCE = 0.3f;

    // ---- Ingredientes (no se comen) -------------------------------------------------------------

    public static final DeferredItem<Item> PAN_RALLADO = plain("pan_rallado");
    public static final DeferredItem<Item> SALSA_BARBACOA = plain("salsa_barbacoa");

    // ---- Ingredientes comestibles ----------------------------------------------------------------

    public static final DeferredItem<Item> QUESO = food("queso", 3, 0.4f, b -> b);
    public static final DeferredItem<Item> CHEDDAR = food("cheddar", 2, 0.3f, b -> b);
    public static final DeferredItem<Item> CEBOLLA_CRISPY = food("cebolla_crispy", 2, 0.3f, b -> b);
    public static final DeferredItem<Item> DULCE_DE_LECHE = food("dulce_de_leche", 3, 0.4f,
            b -> b.effect(effect(ComidaEffects.DULZURA, 15, 0), 1.0f));

    public static final DeferredItem<Item> ASADO_CRUDO = food("asado_crudo", 3, 0.3f, ComidaItems::rawMeat);
    public static final DeferredItem<Item> CHORIZO = food("chorizo", 3, 0.3f, ComidaItems::rawMeat);
    public static final DeferredItem<Item> MILANESA_CRUDA = food("milanesa_cruda", 2, 0.2f, ComidaItems::rawMeat);
    public static final DeferredItem<Item> MILANESA_DE_POLLO_CRUDA =
            food("milanesa_de_pollo_cruda", 2, 0.2f, ComidaItems::rawMeat);
    public static final DeferredItem<Item> EMPANADA_CRUDA = food("empanada_cruda", 2, 0.2f, ComidaItems::rawMeat);

    // ---- Parrilla y fuego ------------------------------------------------------------------------

    public static final DeferredItem<Item> CHORIZO_ASADO = food("chorizo_asado", 6, 0.7f,
            b -> b.effect(effect(ComidaEffects.FUEGO_DE_PARRILLA, 30, 0), 1.0f));

    public static final DeferredItem<Item> ASADO = food("asado", 10, 0.9f, b -> b
            .effect(effect(ComidaEffects.FUEGO_DE_PARRILLA, 120, 0), 1.0f)
            .effect(farmersDelight("nourishment", 30, 0), 1.0f));

    public static final DeferredItem<Item> CHORIPAN = food("choripan", 10, 0.8f, b -> b
            .effect(effect(ComidaEffects.FUEGO_DE_PARRILLA, 60, 0), 1.0f)
            .effect(effect(MobEffects.ABSORPTION, 120, 0), 1.0f));

    public static final DeferredItem<Item> PROVOLETA = food("provoleta", 7, 0.7f, b -> b
            .effect(effect(ComidaEffects.FUEGO_DE_PARRILLA, 40, 0), 1.0f)
            .effect(effect(MobEffects.DAMAGE_RESISTANCE, 60, 0), 1.0f));

    // ---- Milanesas ------------------------------------------------------------------------------

    public static final DeferredItem<Item> MILANESA_DE_CARNE = food("milanesa_de_carne", 9, 0.8f, b -> b
            .effect(effect(MobEffects.DAMAGE_BOOST, 60, 0), 1.0f));

    public static final DeferredItem<Item> MILANESA_DE_POLLO = food("milanesa_de_pollo", 8, 0.7f, b -> b
            .effect(effect(MobEffects.MOVEMENT_SPEED, 60, 0), 1.0f));

    public static final DeferredItem<Item> SANGUCHE_DE_MILANESA = food("sanguche_de_milanesa", 13, 0.9f, b -> b
            .effect(effect(MobEffects.DAMAGE_BOOST, 90, 0), 1.0f)
            .effect(effect(MobEffects.DAMAGE_RESISTANCE, 60, 0), 1.0f)
            .effect(effect(ComidaEffects.PANZA_LLENA, 120, 0), 1.0f));

    // ---- Platos de cuchara (devuelven el plato hondo) ---------------------------------------------

    public static final DeferredItem<Item> GUISO = bowl("guiso", 12, 0.9f, b -> b
            .effect(effect(ComidaEffects.PANZA_LLENA, 180, 0), 1.0f)
            .effect(farmersDelight("comfort", 120, 0), 1.0f)
            .effect(effect(MobEffects.REGENERATION, 5, 0), 1.0f));

    public static final DeferredItem<Item> LOCRO = bowl("locro", 13, 1.0f, b -> b
            .effect(effect(ComidaEffects.PANZA_LLENA, 240, 0), 1.0f)
            .effect(farmersDelight("nourishment", 120, 0), 1.0f)
            .effect(effect(MobEffects.DAMAGE_RESISTANCE, 120, 0), 1.0f));

    public static final DeferredItem<Item> NOQUIS = bowl("noquis", 9, 0.7f, b -> b
            .effect(effect(MobEffects.LUCK, 300, 0), 1.0f)
            .effect(effect(ComidaEffects.PANZA_LLENA, 60, 0), 1.0f));

    // ---- Dulces y panaderia ---------------------------------------------------------------------

    public static final DeferredItem<Item> ALFAJOR = food("alfajor", 4, 0.6f, b -> b.fast()
            .effect(effect(ComidaEffects.DULZURA, 60, 0), 1.0f));

    public static final DeferredItem<Item> MEDIALUNA = food("medialuna", 4, 0.5f, b -> b.fast()
            .effect(effect(ComidaEffects.DULZURA, 20, 0), 1.0f)
            .effect(effect(MobEffects.REGENERATION, 3, 0), 1.0f));

    public static final DeferredItem<Item> PANQUEQUE_CON_DULCE_DE_LECHE = food("panqueque_con_dulce_de_leche", 8, 0.7f, b -> b
            .effect(effect(ComidaEffects.DULZURA, 120, 0), 1.0f)
            .effect(effect(MobEffects.REGENERATION, 6, 0), 1.0f));

    public static final DeferredItem<Item> FLAN_CON_DULCE_DE_LECHE = food("flan_con_dulce_de_leche", 8, 0.6f, b -> b
            .effect(effect(ComidaEffects.DULZURA, 90, 0), 1.0f)
            .effect(effect(MobEffects.ABSORPTION, 90, 1), 1.0f));

    public static final DeferredItem<Item> TORTAFRITA = food("tortafrita", 5, 0.4f, b -> b
            .effect(farmersDelight("comfort", 90, 0), 1.0f));

    public static final DeferredItem<Item> EMPANADA = food("empanada", 7, 0.7f, b -> b
            .effect(effect(ComidaEffects.BUENA_ONDA, 90, 0), 1.0f));

    // Galletitas de la merienda: las Pitusas (rellenas de crema, Parnor) y las Pepas (con ojo de membrillo).
    public static final DeferredItem<Item> DULCE_DE_MEMBRILLO = food("dulce_de_membrillo", 3, 0.3f, b -> b);

    public static final DeferredItem<Item> PITUSAS = food("pitusas", 3, 0.3f, b -> b.fast()
            .effect(effect(ComidaEffects.DULZURA, 30, 0), 1.0f));

    public static final DeferredItem<Item> PEPAS = food("pepas", 4, 0.4f, b -> b.fast()
            .effect(effect(ComidaEffects.BUENA_ONDA, 45, 0), 1.0f)
            .effect(effect(ComidaEffects.DULZURA, 15, 0), 1.0f));

    public static final DeferredItem<Item> FUGAZZETA = food("fugazzeta", 11, 0.8f, b -> b
            .effect(effect(ComidaEffects.BUENA_ONDA, 120, 0), 1.0f)
            .effect(effect(ComidaEffects.PANZA_LLENA, 90, 0), 1.0f));

    // ---- La mas potente: Mega Dibu ---------------------------------------------------------------
    // Doble carne, doble cheddar, doble bacon, cebolla crispy y barbacoa. Llena todo el hambre y la
    // saturacion y da los mejores efectos del mod, pero el empacho dura bastante.

    public static final DeferredItem<Item> MEGA_DIBU = track("mega_dibu", ITEMS.registerItem("mega_dibu",
            props -> create("mega_dibu", props
                    .stacksTo(16)
                    .rarity(Rarity.EPIC)
                    .food(new FoodProperties.Builder()
                            .nutrition(20)
                            .saturationModifier(1.2f)
                            .effect(effect(MobEffects.ABSORPTION, 180, 2), 1.0f)
                            .effect(effect(MobEffects.DAMAGE_BOOST, 180, 1), 1.0f)
                            .effect(effect(MobEffects.DAMAGE_RESISTANCE, 180, 1), 1.0f)
                            .effect(effect(MobEffects.REGENERATION, 20, 1), 1.0f)
                            .effect(effect(ComidaEffects.ATAJAPENALES, 300, 0), 1.0f)
                            .effect(effect(ComidaEffects.PANZA_LLENA, 300, 1), 1.0f)
                            .effect(effect(ComidaEffects.EMPACHO, 240, 0), 1.0f)
                            .build()), null, UseAnim.EAT)));

    // ---- Bebidas (devuelven la botella de vidrio) -------------------------------------------------

    public static final DeferredItem<Item> FERNET_BRANCA = drink("fernet_branca", 2, 0.2f, Rarity.UNCOMMON, b -> b
            .effect(effect(MobEffects.DAMAGE_RESISTANCE, 120, 0), 1.0f)
            .effect(effect(MobEffects.SATURATION, 1, 1), 1.0f)
            .effect(effect(ComidaEffects.CURDA, 120, 0), 1.0f)
            .effect(effect(MobEffects.CONFUSION, 10, 0), 1.0f)
            .effect(effect(MobEffects.WEAKNESS, 20, 0), 1.0f));

    public static final DeferredItem<Item> QUILMES = drink("quilmes", 2, 0.1f, Rarity.COMMON, b -> b
            .effect(effect(ComidaEffects.BUENA_ONDA, 120, 0), 1.0f)
            .effect(effect(ComidaEffects.CURDA, 60, 0), 1.0f)
            .effect(effect(MobEffects.CONFUSION, 5, 0), 1.0f));

    public static final DeferredItem<Item> STELLA_ARTOIS = drink("stella_artois", 2, 0.1f, Rarity.COMMON, b -> b
            .effect(effect(ComidaEffects.BUENA_ONDA, 180, 0), 1.0f)
            .effect(effect(ComidaEffects.CURDA, 90, 0), 1.0f)
            .effect(effect(MobEffects.CONFUSION, 8, 0), 1.0f));

    public static final DeferredItem<Item> ANDES = drink("andes", 2, 0.1f, Rarity.COMMON, b -> b
            .effect(effect(MobEffects.JUMP, 120, 0), 1.0f)
            .effect(effect(ComidaEffects.BUENA_ONDA, 90, 0), 1.0f)
            .effect(effect(ComidaEffects.CURDA, 60, 0), 1.0f)
            .effect(effect(MobEffects.CONFUSION, 5, 0), 1.0f));

    private ComidaItems() {
    }

    static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    static void displayItems(CreativeModeTab.Output output) {
        for (DeferredItem<? extends Item> item : TAB_ORDER) {
            output.accept(item.get());
        }
    }

    /** Ids de los items con modelo plano generado por datagen. */
    static List<String> flatIds() {
        return List.copyOf(FLAT_IDS);
    }

    // ---- Helpers ---------------------------------------------------------------------------------

    private static DeferredItem<Item> plain(String id) {
        return track(id, ITEMS.registerSimpleItem(id));
    }

    private static DeferredItem<Item> food(String id, int nutrition, float saturation,
                                           java.util.function.UnaryOperator<FoodProperties.Builder> extras) {
        FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation);
        FoodProperties properties = extras.apply(builder).build();
        return track(id, ITEMS.registerItem(id, props -> create(id, props.food(properties), null, UseAnim.EAT)));
    }

    private static DeferredItem<Item> bowl(String id, int nutrition, float saturation,
                                           java.util.function.UnaryOperator<FoodProperties.Builder> extras) {
        FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation);
        FoodProperties properties = extras.apply(builder).build();
        return track(id, ITEMS.registerItem(id,
                props -> create(id, props.stacksTo(16).food(properties), Items.BOWL, UseAnim.EAT)));
    }

    private static DeferredItem<Item> drink(String id, int nutrition, float saturation, Rarity rarity,
                                            java.util.function.UnaryOperator<FoodProperties.Builder> extras) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(nutrition).saturationModifier(saturation).alwaysEdible();
        FoodProperties properties = extras.apply(builder).build();
        return track(id, ITEMS.registerItem(id, props -> create(
                id, props.stacksTo(16).rarity(rarity).food(properties), Items.GLASS_BOTTLE, UseAnim.DRINK)));
    }

    /**
     * Las comidas de {@link Comida} son colocables como bloque (y devuelven su recipiente al
     * consumirse); el resto de los ingredientes comestibles son items comunes.
     */
    private static Item create(String id, Item.Properties props, @Nullable ItemLike container, UseAnim animation) {
        Comida comida = Comida.byId(id);
        return comida == null ? new Item(props) : new ComidaColocableItem(props, comida, container, animation);
    }

    private static <T extends Item> DeferredItem<T> track(String id, DeferredItem<T> item) {
        TAB_ORDER.add(item);
        FLAT_IDS.add(id);
        return item;
    }

    private static FoodProperties.Builder rawMeat(FoodProperties.Builder builder) {
        return builder.effect(effect(MobEffects.HUNGER, 30, 0), RAW_FOOD_POISON_CHANCE);
    }

    private static Supplier<MobEffectInstance> effect(Holder<MobEffect> effect, int seconds, int amplifier) {
        return () -> new MobEffectInstance(effect, seconds * 20, amplifier);
    }

    /**
     * Efecto de Farmer's Delight buscado por id en tiempo de ejecucion (el mod no se importa). Si por
     * algun motivo no esta, cae a regeneracion para que el plato siga dando algo.
     */
    private static Supplier<MobEffectInstance> farmersDelight(String path, int seconds, int amplifier) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("farmersdelight", path);
        return () -> {
            Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.getHolder(id)
                    .<Holder<MobEffect>>map(reference -> reference)
                    .orElse(MobEffects.REGENERATION);
            return new MobEffectInstance(holder, seconds * 20, amplifier);
        };
    }
}
