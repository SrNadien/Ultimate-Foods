package nadiendev.ultimatefoods.datagen.loot;

import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import nadiendev.ultimatefoods.items.ItemsAdds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class UltimateFoodsBlockLootTables extends BlockLootSubProvider {

    public UltimateFoodsBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        // ========== BLOQUES DE BlocksAdds ==========
        
        // STEEL_BLOCK: Al romper el bloque de acero, dropea el bloque de acero
        dropSelf(BlocksAdds.STEEL_BLOCK.get());
        
        // NADIENITE_BLOCK: Al picar el bloque de nadienite, dropea el bloque de nadienite
        dropSelf(BlocksAdds.NADIENITE_BLOCK.get());
        
        // SEXY_XD: Al picar este bloque, dropea ese mismo bloque
        dropSelf(BlocksAdds.SEXY_XD.get());
        
        // ========== MINERALES DE NadieniteOreBlock ==========
        
        // NADIENITE_ORE: Al romper el mineral, NO dropea el bloque, dropea el item del ore
        add(NadieniteOreBlock.NADIENITE_ORE.get(),
            block -> createSingleItemTable(NadieniteOreBlock.NADIENITE_ORE.get().asItem()));
        
        // DEEPSLATE_NADIENITE_ORE: Al romper el mineral, NO dropea el bloque, dropea el item del ore
        add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get(),
            block -> createSingleItemTable(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get().asItem()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // Lista simple de todos los bloques que tienen loot tables
        return java.util.List.of(
            BlocksAdds.STEEL_BLOCK.get(),
            BlocksAdds.NADIENITE_BLOCK.get(),
            BlocksAdds.SEXY_XD.get(),
            NadieniteOreBlock.NADIENITE_ORE.get(),
            NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get()
        );
    }
}