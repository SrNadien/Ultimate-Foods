package nadiendev.ultimatefoods.fisura.comida;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Consumer;

/**
 * Efectos propios de las comidas criollas. Todos viven bajo el prefijo {@code fisura/}
 * (ej. {@code ultimatefoods:fisura/panza_llena}) asi el icono sale de
 * {@code textures/mob_effect/fisura/<id>.png} sin pelear con los efectos de otros modulos.
 * <p>
 * Son efectos de atributos puros (no tienen logica por tick), asi que el comportamiento se
 * lee directo de la tabla de abajo y no hay estado que se pueda romper.
 */
public final class ComidaEffects {

    private static final AttributeModifier.Operation ADD = AttributeModifier.Operation.ADD_VALUE;
    private static final AttributeModifier.Operation MULT = AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, UltimateFoodsCore.MOD_ID);

    /** Los ids (sin prefijo) en orden de registro; los usan el datagen y las claves de idioma. */
    public static final List<String> IDS = List.of(
            "panza_llena", "fuego_de_parrilla", "dulzura", "curda", "buena_onda", "empacho", "atajapenales");

    /** Estomago lleno: +2 corazones de vida maxima por nivel, pero andas mas lento. */
    public static final DeferredHolder<MobEffect, MobEffect> PANZA_LLENA = register(
            "panza_llena", MobEffectCategory.BENEFICIAL, 0xE8B796, effect -> {
                effect.addAttributeModifier(Attributes.MAX_HEALTH, modifier("panza_llena.vida"), 4.0, ADD);
                effect.addAttributeModifier(Attributes.MOVEMENT_SPEED, modifier("panza_llena.paso"), -0.10, MULT);
            });

    /** Brasa de la parrilla: pegas mas fuerte y cuesta empujarte. */
    public static final DeferredHolder<MobEffect, MobEffect> FUEGO_DE_PARRILLA = register(
            "fuego_de_parrilla", MobEffectCategory.BENEFICIAL, 0xE64539, effect -> {
                effect.addAttributeModifier(Attributes.ATTACK_DAMAGE, modifier("fuego_de_parrilla.dano"), 2.0, ADD);
                effect.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, modifier("fuego_de_parrilla.empuje"), 0.25, ADD);
            });

    /** Subidon de azucar: mas velocidad al caminar y al pegar. */
    public static final DeferredHolder<MobEffect, MobEffect> DULZURA = register(
            "dulzura", MobEffectCategory.BENEFICIAL, 0xCF752B, effect -> {
                effect.addAttributeModifier(Attributes.MOVEMENT_SPEED, modifier("dulzura.paso"), 0.15, MULT);
                effect.addAttributeModifier(Attributes.ATTACK_SPEED, modifier("dulzura.golpe"), 0.15, MULT);
            });

    /** El costo del alcohol: mas lento para caminar y para pegar. */
    public static final DeferredHolder<MobEffect, MobEffect> CURDA = register(
            "curda", MobEffectCategory.HARMFUL, 0x8B6B3E, effect -> {
                effect.addAttributeModifier(Attributes.MOVEMENT_SPEED, modifier("curda.paso"), -0.15, MULT);
                effect.addAttributeModifier(Attributes.ATTACK_SPEED, modifier("curda.golpe"), -0.10, MULT);
            });

    /** Buen clima entre amigos: suerte. */
    public static final DeferredHolder<MobEffect, MobEffect> BUENA_ONDA = register(
            "buena_onda", MobEffectCategory.BENEFICIAL, 0xFEE761, effect ->
                    effect.addAttributeModifier(Attributes.LUCK, modifier("buena_onda.suerte"), 1.0, ADD));

    /** Te pasaste de comida: lento y sin fuerza. */
    public static final DeferredHolder<MobEffect, MobEffect> EMPACHO = register(
            "empacho", MobEffectCategory.HARMFUL, 0x63C74D, effect -> {
                effect.addAttributeModifier(Attributes.MOVEMENT_SPEED, modifier("empacho.paso"), -0.20, MULT);
                effect.addAttributeModifier(Attributes.ATTACK_DAMAGE, modifier("empacho.dano"), -2.0, ADD);
                effect.addAttributeModifier(Attributes.ATTACK_SPEED, modifier("empacho.golpe"), -0.20, MULT);
            });

    /** Manos de arquero: mas alcance para bloques y criaturas, y las caidas duelen menos. */
    public static final DeferredHolder<MobEffect, MobEffect> ATAJAPENALES = register(
            "atajapenales", MobEffectCategory.BENEFICIAL, 0xDFE0E8, effect -> {
                effect.addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, modifier("atajapenales.alcance_criaturas"), 1.5, ADD);
                effect.addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, modifier("atajapenales.alcance_bloques"), 1.5, ADD);
                effect.addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, modifier("atajapenales.caida"), 4.0, ADD);
            });

    private ComidaEffects() {
    }

    static void register(IEventBus modEventBus) {
        MOB_EFFECTS.register(modEventBus);
    }

    private static Identifier modifier(String name) {
        return Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "effect.fisura." + name);
    }

    private static DeferredHolder<MobEffect, MobEffect> register(
            String id, MobEffectCategory category, int color, Consumer<MobEffect> attributes) {
        return MOB_EFFECTS.register("fisura/" + id, () -> {
            MobEffect effect = new MobEffect(category, color) {
            };
            attributes.accept(effect);
            return effect;
        });
    }
}
