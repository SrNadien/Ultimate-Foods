package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.registry.ItemsAdds;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import net.minecraft.data.advancements.AdvancementSubProvider;

import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
        net.minecraft.core.HolderGetter<Item> itemLookup = registries.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM);

        AdvancementHolder installMod = Advancement.Builder.advancement()
                .display(
                        ItemsAdds.BURGER.get(),
                        Component.translatable("advancements.ultimatefoods.install_mod.title"),
                        Component.translatable("advancements.ultimatefoods.install_mod.description"),
                        Identifier.parse("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "install_mod").toString());

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
                .save(saver, Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "erapalnene").toString());

        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.SUPERPOOP.get(),
                        Component.translatable("advancements.ultimatefoods.supercaota.title"),
                        Component.translatable("advancements.ultimatefoods.supercaota.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("eat_superpoop",
                        ConsumeItemTrigger.TriggerInstance.usedItem(itemLookup, ItemsAdds.SUPERPOOP.get()))
                .save(saver, Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "supercaota").toString());

        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.POOP.get(),
                        Component.translatable("advancements.ultimatefoods.cacotas.title"),
                        Component.translatable("advancements.ultimatefoods.cacotas.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("eat_poop",
                        ConsumeItemTrigger.TriggerInstance.usedItem(itemLookup, ItemsAdds.POOP.get()))
                .save(saver, Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacotas").toString());

        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.SUPER_CHILE.get(),
                        Component.translatable("advancements.ultimatefoods.estabapicantesono.title"),
                        Component.translatable("advancements.ultimatefoods.estabapicantesono.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("ate_super_chile",
                        ConsumeItemTrigger.TriggerInstance.usedItem(itemLookup, ItemsAdds.SUPER_CHILE.get()))
                .save(saver, Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "estabapicantesono").toString());

        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.BURGER.get(),
                        Component.translatable("advancements.ultimatefoods.hamburguesa_gourmet.title"),
                        Component.translatable("advancements.ultimatefoods.hamburguesa_gourmet.description"),
                        null, AdvancementType.TASK, true, true, false
                )
                .addCriterion("eat_burger",
                        ConsumeItemTrigger.TriggerInstance.usedItem(itemLookup, ItemsAdds.BURGER.get()))
                .save(saver, Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "hamburguesa_gourmet").toString());
    }
}
