package nadiendev.ultimatefoods.registry;

import nadiendev.ultimatefoods.items.CustomFoodItem;
import nadiendev.ultimatefoods.items.PoopItem;
import nadiendev.ultimatefoods.items.SuperChileItem;
import nadiendev.ultimatefoods.items.ModTier;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import nadiendev.ultimatefoods.items.ModFood;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsAdds {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<Item, Item> MONSTER = registerFoodItem(
            "monster",
            () -> new CustomFoodItem(props(), 
                    createFoodProperties(6, 0.6f)
                            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INSTANT_HEALTH, 400, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.MONSTER_EAT
            )
    );

    public static final DeferredHolder<Item, Item> CAJITA_FELIZ = registerFoodItem(
            "cajita_feliz",
            () -> new CustomFoodItem(props(), 
                    createFoodProperties(8, 0.8f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 800, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.ERA_PAL_NENE
            )
    );

    public static final DeferredHolder<Item, Item> SUPER_ENERGY_DRINK = registerFoodItem(
            "super_energy_drink",
            () -> new CustomFoodItem(props(), 
                    createFoodProperties(4, 0.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.JUMP_BOOST, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.STRENGTH, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.HASTE, 300, 3), 1.0f)
                            .build(),
                    SonidosReproducibles.SUPER_ENERGY_DRINK
            )
    );

    public static final DeferredHolder<Item, Item> DORITOS = registerFoodItem(
            "doritos",
            () -> new CustomFoodItem(props(), 
                    createFoodProperties(3, 0.3f)
                            .fast()
                            .build(),
                    SonidosReproducibles.DORITOS_EAT
            )
    );

    public static final DeferredHolder<Item, Item> POOP = registerFoodItem(
        "poop",
        () -> new PoopItem(props(), 
                createFoodProperties(1, 0.1f)
                        .effect(() -> new MobEffectInstance(EffectsAdds.QUE_GUARRO, 6000, 0), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                6000
        )
    );

    public static final DeferredHolder<Item, Item> SUPERPOOP = registerFoodItem(
        "superpoop",
        () -> new PoopItem(props(), 
                createFoodProperties(40, 40.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.SPEED, 12000, 10), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 12000, 10), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                12000
        )
    );

    public static final DeferredHolder<Item, Item> CHOCOLATADA = reg(
        "chocolatada",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> BAKED_TORTILLA = reg(
        "baked_tortilla",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> TORTILLA = reg(
        "tortilla",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> TACO = registerCustomFood(
         "taco",
              20,
              8f,
              64,
              Rarity.COMMON,
              createFoodProperties(20, 8f)
            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 12000, 10), 1.0f)
            .build(),
            SonidosReproducibles.RICKROLL
    );

    public static final DeferredHolder<Item, Item> SUPER_CHILE = reg(
         "super_chile",
             () -> new SuperChileItem(props()
            .stacksTo(64)
            .rarity(Rarity.COMMON)
            .food(ModFood.builder()
                    .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 12000, 10), 1.0f)
                    .nutrition(10)
                    .saturationModifier(3f)
                    .build().properties()
              )
            )
         );

    public static final DeferredHolder<Item, Item> RAW_HAMBURGUER_MEAT = reg(
        "raw_hamburger_meat",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.1f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> COOKED_HAMBURGUER_MEAT = reg(
        "cooked_hamburger_meat",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(6)
                        .saturationModifier(0.8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> BURGER = reg(
        "burger",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.EPIC)
                .food(ModFood.builder()
                        .nutrition(6)
                        .saturationModifier(1.2f)
                        .effect(() -> new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 5), 1.0f)
                        .build().properties()
                )
        )
    );

    public static final DeferredHolder<Item, Item> MUSHASHITE_INGOT = registerMaterial("mushashite_ingot");
    public static final DeferredHolder<Item, Item> MUSHASHITE_NUGGET = registerMaterial("mushashite_nugget");
    public static final DeferredHolder<Item, Item> RAW_MUSHASHITE = registerMaterial("raw_mushashite");

    public static final DeferredHolder<Item, Item> JOANFOITE_INGOT = registerMaterial("joanfoite_ingot");
    public static final DeferredHolder<Item, Item> JOANFOITE_NUGGET = registerMaterial("joanfoite_nugget");
    public static final DeferredHolder<Item, Item> RAW_JOANFOITE = registerMaterial("raw_joanfoite");

    public static final DeferredHolder<Item, Item> NADIENITE_INGOT = registerMaterial("nadienite_ingot");
    public static final DeferredHolder<Item, Item> RAW_NADIENITE = registerMaterial("raw_nadienite");

    public static final DeferredHolder<Item, Item> STEEL_NUGGET = registerMaterial("steel_nugget");
    public static final DeferredHolder<Item, Item> NETHERITE_NUGGET = registerMaterial("netherite_nugget");

    public static final DeferredHolder<Item, Item> STEEL_INGOT = reg(
        "steel_ingot",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> GALACTIC_STAR = reg(
        "galactic_star",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PATRICIO = reg(
        "patricio",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> BOB_ESPONJA = reg(
        "bob_esponja",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> SEMILLA = reg(
        "semilla",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> RADIOACTIVE_FUEL = reg(
        "radioactive_fuel",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PORTALMINI = reg(
        "portalmini",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> RADIOACTIVE_ANTIMATTER = reg(
        "radioactive_antimatter",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PLANOS = reg(
        "planos",
       () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> REACTORMINI = reg(
        "reactormini",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> SUPER_SEED = reg(
        "super_seed",
       () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> GAMEBOY = reg(
         "gameboy",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> GALACTIC_GLOBE = reg(
         "galactic_globe",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> DRAGONSOUL = reg(
         "dragonsoul",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> DOLL = reg(
         "doll",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> CATALIZADOR = reg(
         "catalizador",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> CALAVERA = reg(
         "calavera",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> AGUJERONEGRO = reg(
         "agujeronegro",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> ABSOLUTE_REACTION_PLATING = reg(
         "absolute_reaction_plating",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> BRUJULAMALDITA = reg(
         "brujulamaldita",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> UNIVERSE_SEARCH = reg(
         "universe_search",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> TRANSMISOR = reg(
         "transmisor",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> NADIENITE_NUGGET = registerMaterial("nadienite_nugget");

    public static final DeferredHolder<Item, Item> CORAZON_DE_LOS_CAIDOS = registerCorazon("corazon_de_los_caidos");

    public static final DeferredHolder<Item, Item> CORAZON_DE_LOS_FANTASMAS = registerCorazon("corazon_de_los_fantasmas");

    public static final DeferredHolder<Item, Item> CORAZON_DE_LA_ELITE = registerCorazon("corazon_de_la_elite");

    public static final DeferredHolder<Item, Item> NETHERITE_DAGGER = reg(
        "netherite_dagger",
        () -> new Item(props()
                .stacksTo(1)
                .fireResistant()
                .rarity(Rarity.EPIC)
        )
    );

public static final DeferredHolder<Item, Item> DISC_MCCACAO = reg(
    "disc_mccacao",
    () -> new Item(props()
            .jukeboxPlayable(SonidosReproducibles.DISC_MCCACAO_KEY)
            .stacksTo(1)
            .rarity(Rarity.RARE))
);

public static final DeferredHolder<Item, Item> DISC_AVENGERS = reg(
    "disc_avengers",
    () -> new Item(props()
            .jukeboxPlayable(SonidosReproducibles.DISC_AVENGERS_KEY)
            .stacksTo(1)
            .rarity(Rarity.RARE))
);

    public static final DeferredHolder<Item, BucketItem> NADIENITE_FLUID_BUCKET = reg(
        "nadienite_fluid_bucket",
        () -> new BucketItem(
                FluidsRegistry.NADIENITE_FLUID_SOURCE.get(),
                props()
                        .craftRemainder(Items.BUCKET)
                        .stacksTo(1)
        )
    );

    private static ModFood.Builder createFoodProperties(int nutrition, float saturation) {
        return ModFood.builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);
    }

    private static final ThreadLocal<Item.Properties> CURRENT_PROPERTIES = new ThreadLocal<>();

    private static Item.Properties props() {
        Item.Properties properties = CURRENT_PROPERTIES.get();
        return properties != null ? properties : new Item.Properties();
    }

    private static <T extends Item> DeferredHolder<Item, T> reg(String name, Supplier<T> supplier) {
        return ITEMS.registerItem(name, properties -> {
            CURRENT_PROPERTIES.set(properties);
            try {
                return supplier.get();
            } finally {
                CURRENT_PROPERTIES.remove();
            }
        });
    }

    private static DeferredHolder<Item, Item> registerFoodItem(String name, Supplier<Item> item) {
        return reg(name, item);
    }

    public static DeferredHolder<Item, Item> ingotOf(ModTier tier) {
        return switch (tier) {
            case MUSHASHITE -> MUSHASHITE_INGOT;
            case JOANFOITE -> JOANFOITE_INGOT;
            case NADIENITE -> NADIENITE_INGOT;
        };
    }

    public static DeferredHolder<Item, Item> nuggetOf(ModTier tier) {
        return switch (tier) {
            case MUSHASHITE -> MUSHASHITE_NUGGET;
            case JOANFOITE -> JOANFOITE_NUGGET;
            case NADIENITE -> NADIENITE_NUGGET;
        };
    }

    public static DeferredHolder<Item, Item> rawOreOf(ModTier tier) {
        return switch (tier) {
            case MUSHASHITE -> RAW_MUSHASHITE;
            case JOANFOITE -> RAW_JOANFOITE;
            case NADIENITE -> RAW_NADIENITE;
        };
    }

    public static DeferredHolder<Item, Item> heartOf(ModTier tier) {
        return switch (tier) {
            case MUSHASHITE -> CORAZON_DE_LOS_CAIDOS;
            case JOANFOITE -> CORAZON_DE_LOS_FANTASMAS;
            case NADIENITE -> CORAZON_DE_LA_ELITE;
        };
    }

    private static DeferredHolder<Item, Item> registerMaterial(String name) {
        return reg(name, () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        ));
    }

    private static DeferredHolder<Item, Item> registerCorazon(String name) {
        return reg(name, () -> new Item(props()
                .stacksTo(16)
                .fireResistant()
                .rarity(Rarity.EPIC)
        ));
    }

    public static DeferredHolder<Item, Item> registerItem(String name) {
        return reg(name, () -> new Item(props()));
    }

    public static DeferredHolder<Item, Item> registerSimpleFood(String name, int nutrition, float saturation) {
        return reg(name, () -> new Item(props()
                .food(createFoodProperties(nutrition, saturation).build().properties())));
    }

    public static DeferredHolder<Item, Item> registerCustomFood(String name, int nutrition, float saturation, int stackSize, Rarity rarity, ModFood foodProperties, DeferredHolder<SoundEvent, SoundEvent> sound) {
        return reg(name, () -> new CustomFoodItem(props(), foodProperties, sound));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
