package nadiendev.ultimatefoods.items;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.sounds.SonidosReproducibles;
import nadiendev.ultimatefoods.effects.EffectsAdds;
import nadiendev.ultimatefoods.fluidos_xd.FluidsRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/*
* Items Registry
* By NadienDev
*/
public class ItemsAdds {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, UltimateFoodsCore.MOD_ID);

//      ===============================>[Food Item Add]<===============================

    // MONSTER 
    public static final DeferredHolder<Item, Item> MONSTER = registerFoodItem(
            "monster",
            () -> new CustomFoodItem(
                    createFoodProperties(6, 0.6f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 400, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.MONSTER_EAT
            )
    );

    // CAJITA FELIZ - Comida rápida con efectos de velocidad e invisibilidad
    public static final DeferredHolder<Item, Item> CAJITA_FELIZ = registerFoodItem(
            "cajita_feliz",
            () -> new CustomFoodItem(
                    createFoodProperties(8, 0.8f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 800, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.ERA_PAL_NENE
            )
    );

    // SUPER ENERGY DRINK 
    public static final DeferredHolder<Item, Item> SUPER_ENERGY_DRINK = registerFoodItem(
            "super_energy_drink",
            () -> new CustomFoodItem(
                    createFoodProperties(4, 0.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 300, 3), 1.0f)
                            .build(),
                    SonidosReproducibles.SUPER_ENERGY_DRINK
            )
    );

    // DORITOS
    public static final DeferredHolder<Item, Item> DORITOS = registerFoodItem(
            "doritos",
            () -> new CustomFoodItem(
                    createFoodProperties(3, 0.3f)
                            .fast() 
                            .build(),
                    SonidosReproducibles.DORITOS_EAT
            )
    );

    // POOP 
    public static final DeferredHolder<Item, Item> POOP = registerFoodItem(
        "poop",
        () -> new PoopItem(
                createFoodProperties(1, 0.1f)
                        .effect(() -> new MobEffectInstance(EffectsAdds.QUE_GUARRO, 6000, 0), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                6000
        )
    );
    
    // SUPERPOOP
    public static final DeferredHolder<Item, Item> SUPERPOOP = registerFoodItem(
        "superpoop",
        () -> new PoopItem(
                createFoodProperties(40, 40.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 10), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 12000, 10), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                12000 
        )
    );

    // CHOCOLATADA
    public static final DeferredHolder<Item, Item> CHOCOLATADA = ITEMS.register(
        "chocolatada",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    // tortilla cruda
    public static final DeferredHolder<Item, Item> BAKED_TORTILLA = ITEMS.register(
        "baked_tortilla",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    // tortilla cocinada
    public static final DeferredHolder<Item, Item> TORTILLA = ITEMS.register(
        "tortilla",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    // taco 
    public static final DeferredHolder<Item, Item> TACO = registerCustomFood(
         "taco",
              20,
              8f,
              64,
              Rarity.COMMON,
              createFoodProperties(20, 8f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 10), 1.0f)
            .build(),
            SonidosReproducibles.RICKROLL
    );

    // SUPER CHILE 
    public static final DeferredHolder<Item, Item> SUPER_CHILE = ITEMS.register(
         "super_chile",
             () -> new SuperChileItem(new Item.Properties()
            .stacksTo(64)
            .rarity(Rarity.COMMON)
            .food(new FoodProperties.Builder()
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 12000, 10), 1.0f)
                    .nutrition(10)
                    .saturationModifier(3f)
                    .build()
              )
            )
         );

//      ===============================>[Normal Item Add]<===============================

    // NADIENITE INGOT
    public static final DeferredHolder<Item, Item> NADIENITE_INGOT = ITEMS.register(
        "nadienite_ingot",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // steel ingot
    public static final DeferredHolder<Item, Item> STEEL_INGOT = ITEMS.register(
        "steel_ingot",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // galaxy star
    public static final DeferredHolder<Item, Item> GALACTIC_STAR = ITEMS.register(
        "galactic_star",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // PATRICIO ESTRELLA
    public static final DeferredHolder<Item, Item> PATRICIO = ITEMS.register(
        "patricio",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // BOB ESPONJA
    public static final DeferredHolder<Item, Item> BOB_ESPONJA = ITEMS.register(
        "bob_esponja",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // SEMILLA
    public static final DeferredHolder<Item, Item> SEMILLA = ITEMS.register(
        "semilla",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );
    
    // RADIACTIVE FUEL
    public static final DeferredHolder<Item, Item> RADIOACTIVE_FUEL = ITEMS.register(
        "radioactive_fuel",
       () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );
    
    // portalmini
    public static final DeferredHolder<Item, Item> PORTALMINI = ITEMS.register(
        "portalmini",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // radioactive antimatter
    public static final DeferredHolder<Item, Item> RADIOACTIVE_ANTIMATTER = ITEMS.register(
        "radioactive_antimatter",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // PLANOS
    public static final DeferredHolder<Item, Item> PLANOS = ITEMS.register(
        "planos",
       () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // REACTOR MINI
    public static final DeferredHolder<Item, Item> REACTORMINI = ITEMS.register(
        "reactormini",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // SUPER SEED
    public static final DeferredHolder<Item, Item> SUPER_SEED = ITEMS.register(
        "super_seed",
       () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // GAMEBOY
    public static final DeferredHolder<Item, Item> GAMEBOY = ITEMS.register(
         "gameboy",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // GALACTIC GLOBE
    public static final DeferredHolder<Item, Item> GALACTIC_GLOBE = ITEMS.register(
         "galactic_globe",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // DRAGON SOUL
    public static final DeferredHolder<Item, Item> DRAGONSOUL = ITEMS.register(
         "dragonsoul",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    ); 

    // doll
    public static final DeferredHolder<Item, Item> DOLL = ITEMS.register(
         "doll",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // CATALIZADOR
    public static final DeferredHolder<Item, Item> CATALIZADOR = ITEMS.register(
         "catalizador",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // CALAVERA
    public static final DeferredHolder<Item, Item> CALAVERA = ITEMS.register(
         "calavera",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // AGUJERO NEGRO
    public static final DeferredHolder<Item, Item> AGUJERONEGRO = ITEMS.register(
         "agujeronegro",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // ABSOLUTE REACTION PLATING
    public static final DeferredHolder<Item, Item> ABSOLUTE_REACTION_PLATING = ITEMS.register(
         "absolute_reaction_plating",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // brujula maldita
    public static final DeferredHolder<Item, Item> BRUJULAMALDITA = ITEMS.register(
         "brujulamaldita",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );
    
    // BUSCADOR DEL UNIVERSO
    public static final DeferredHolder<Item, Item> UNIVERSE_SEARCH = ITEMS.register(
         "universe_search",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    // TRANSMISOR
    public static final DeferredHolder<Item, Item> TRANSMISOR = ITEMS.register(
         "transmisor",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

//      ===============================>[Music Discs]<===============================

    // DISC MCCACAO
public static final DeferredHolder<Item, Item> DISC_MCCACAO = ITEMS.register(
    "disc_mccacao",
    () -> new Item(new Item.Properties()
            .jukeboxPlayable(SonidosReproducibles.DISC_MCCACAO_KEY)
            .stacksTo(1)
            .rarity(Rarity.RARE))
);

// DISC AVENGERS
public static final DeferredHolder<Item, Item> DISC_AVENGERS = ITEMS.register(
    "disc_avengers",
    () -> new Item(new Item.Properties()
            .jukeboxPlayable(SonidosReproducibles.DISC_AVENGERS_KEY)
            .stacksTo(1)
            .rarity(Rarity.RARE))
);

//      ===============================>[Fluid Buckets]<===============================

    // NADIENITE FLUID BUCKET
    public static final DeferredHolder<Item, BucketItem> NADIENITE_FLUID_BUCKET = ITEMS.register(
        "nadienite_fluid_bucket",
        () -> new BucketItem(
                FluidsRegistry.NADIENITE_FLUID_SOURCE.get(),
                new Item.Properties()
                        .craftRemainder(Items.BUCKET)
                        .stacksTo(1)
        )
    );

    // ========== MÉTODOS HELPER ==========

    private static FoodProperties.Builder createFoodProperties(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);
    }

    private static DeferredHolder<Item, Item> registerFoodItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

    public static DeferredHolder<Item, Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static DeferredHolder<Item, Item> registerSimpleFood(String name, int nutrition, float saturation) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()
                .food(createFoodProperties(nutrition, saturation).build())));
    }

    public static DeferredHolder<Item, Item> registerCustomFood(String name, int nutrition, float saturation, int stackSize, Rarity rarity, FoodProperties foodProperties, DeferredHolder<SoundEvent, SoundEvent> sound) {
        return ITEMS.register(name, () -> new CustomFoodItem(foodProperties, sound));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}