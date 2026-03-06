package nadiendev.ultimatefoods.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.blocks.BlocksAdds;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementProvider.AdvancementGenerator {

    private static final ResourceLocation RL_INFINITY_1_SWORD  = ResourceLocation.fromNamespaceAndPath("ultimatefoods", "infinity_1_sword");
    private static final ResourceLocation RL_INFINITY_BALANCED = ResourceLocation.fromNamespaceAndPath("ultimatefoods", "infinity_sword_balanced");

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {

        // ROOT: install_mod
        AdvancementHolder installMod = Advancement.Builder.advancement()
                .display(
                        BlocksAdds.NADIENITE_BLOCK.get(),
                        Component.translatable("advancements.ultimatefoods.install_mod.title"),
                        Component.translatable("advancements.ultimatefoods.install_mod.description"),
                        ResourceLocation.parse("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "install_mod").toString());

        // casy_try_hard + tryhard_balanced (solo si los items de Avaritia están registrados)
        if (BuiltInRegistries.ITEM.containsKey(RL_INFINITY_1_SWORD)
                && BuiltInRegistries.ITEM.containsKey(RL_INFINITY_BALANCED)) {

            Item infinity1Sword   = BuiltInRegistries.ITEM.get(RL_INFINITY_1_SWORD);
            Item infinityBalanced = BuiltInRegistries.ITEM.get(RL_INFINITY_BALANCED);

            Advancement.Builder.advancement()
                    .parent(installMod)
                    .display(
                            infinity1Sword,
                            Component.translatable("advancements.ultimatefoods.casy_try_hard.title"),
                            Component.translatable("advancements.ultimatefoods.casy_try_hard.description"),
                            null, AdvancementType.CHALLENGE, true, true, false
                    )
                    .addCriterion("has_infinity_1_sword",
                            InventoryChangeTrigger.TriggerInstance.hasItems(infinity1Sword))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "casy_try_hard").toString());

            Advancement.Builder.advancement()
                    .parent(installMod)
                    .display(
                            infinityBalanced,
                            Component.translatable("advancements.ultimatefoods.tryhard_balanced.title"),
                            Component.translatable("advancements.ultimatefoods.tryhard_balanced.description"),
                            null, AdvancementType.CHALLENGE, true, true, false
                    )
                    .addCriterion("has_infinity_sword_balanced",
                            InventoryChangeTrigger.TriggerInstance.hasItems(infinityBalanced))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "tryhard_balanced").toString());
        }

        // erapalnene — cajita feliz
        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.CAJITA_FELIZ.get(),
                        Component.translatable("advancements.ultimatefoods.erapalnene.title"),
                        Component.translatable("advancements.ultimatefoods.erapalnene.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("has_cajita_feliz",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemsAdds.CAJITA_FELIZ.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "erapalnene").toString());

        // supercaota — superpoop (al comerlo)
        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.SUPERPOOP.get(),
                        Component.translatable("advancements.ultimatefoods.supercaota.title"),
                        Component.translatable("advancements.ultimatefoods.supercaota.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("eat_superpoop",
                        ConsumeItemTrigger.TriggerInstance.usedItem(ItemsAdds.SUPERPOOP.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "supercaota").toString());

        // cacotas — poop (al comerlo)
        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.POOP.get(),
                        Component.translatable("advancements.ultimatefoods.cacotas.title"),
                        Component.translatable("advancements.ultimatefoods.cacotas.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("eat_poop",
                        ConsumeItemTrigger.TriggerInstance.usedItem(ItemsAdds.POOP.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacotas").toString());

        // eresunviciado — galactic star
        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.GALACTIC_STAR.get(),
                        Component.translatable("advancements.ultimatefoods.eresunviciado.title"),
                        Component.translatable("advancements.ultimatefoods.eresunviciado.description"),
                        null, AdvancementType.CHALLENGE, true, true, false
                )
                .addCriterion("has_galactic_star",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemsAdds.GALACTIC_STAR.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "eresunviciado").toString());

        // estabapicantesono — comer super chile
        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.SUPER_CHILE.get(),
                        Component.translatable("advancements.ultimatefoods.estabapicantesono.title"),
                        Component.translatable("advancements.ultimatefoods.estabapicantesono.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("ate_super_chile",
                        ConsumeItemTrigger.TriggerInstance.usedItem(ItemsAdds.SUPER_CHILE.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "estabapicantesono").toString());
    }
}