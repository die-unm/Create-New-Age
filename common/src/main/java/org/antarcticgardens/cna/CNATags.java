package org.antarcticgardens.cna;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class CNATags {
    public enum Block {
        MAGNET,
        STOPS_RADIATION;
        
        public final TagKey<net.minecraft.world.level.block.Block> blockTag;
        public final TagKey<net.minecraft.world.item.Item> itemTag;
        
        Block() {
            ResourceLocation location = new ResourceLocation(CreateNewAge.MOD_ID, Lang.asId(name()));
            blockTag = TagKey.create(Registries.BLOCK, location);
            itemTag = TagKey.create(Registries.ITEM, location);
        }
        
        public static void load() {  }
        
        public static void generate(RegistrateTagsProvider<net.minecraft.world.level.block.Block> provider) {
            TagGen.CreateTagsProvider<net.minecraft.world.level.block.Block> prov = 
                    new TagGen.CreateTagsProvider<>(provider, net.minecraft.world.level.block.Block::builtInRegistryHolder);
            
            prov.tag(MAGNET.blockTag)
                    .add(Blocks.RESPAWN_ANCHOR);
            
            prov.tag(createMagneticForgeTag(4))
                    .add(Blocks.RESPAWN_ANCHOR);
        }
    }
    
    public enum Item {
        NUCLEAR_FUEL("nuclear/fuel"),
        HAZMAT_SUIT("hazmat_suit");
        
        public final TagKey<net.minecraft.world.item.Item> tag;
        
        Item(String id) {
            tag = TagKey.create(Registries.ITEM, new ResourceLocation(CreateNewAge.MOD_ID, id));
        }
        
        public static void load() {  }

        public static void generate(RegistrateTagsProvider<net.minecraft.world.item.Item> provider) {
            TagGen.CreateTagsProvider<net.minecraft.world.item.Item> prov =
                    new TagGen.CreateTagsProvider<>(provider, net.minecraft.world.item.Item::builtInRegistryHolder);

            prov.tag(HAZMAT_SUIT.tag)
                    .add(Items.LEATHER_HELMET)
                    .add(Items.LEATHER_CHESTPLATE)
                    .add(Items.LEATHER_LEGGINGS)
                    .add(Items.LEATHER_BOOTS)
                    .add(AllItems.GOGGLES.get());
        }
    }
    
    public static class Common {
        public static TagKey<net.minecraft.world.item.Item>
                #if CNA_FABRIC
                NUGGETS_COPPER = AllTags.forgeItemTag("copper_nuggets"), 
                NUGGETS_ZINC = AllTags.forgeItemTag("zinc_nuggets"), 
                PLATES_COPPER = AllTags.forgeItemTag("copper_plates"), 
                PLATES_IRON = AllTags.forgeItemTag("iron_plates"),
                PLATES_GOLD = AllTags.forgeItemTag("gold_plates");
                #else
                NUGGETS_COPPER = AllTags.forgeItemTag("nuggets/copper"), 
                NUGGETS_ZINC = AllTags.forgeItemTag("nuggets/zinc"), 
                PLATES_COPPER = AllTags.forgeItemTag("plates/copper"), 
                PLATES_IRON = AllTags.forgeItemTag("plates/iron"),
                PLATES_GOLD = AllTags.forgeItemTag("plates/gold");
                #endif
    }
    
    public static TagKey<net.minecraft.world.item.Item> createNuclearEnergyTag(int energy) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(CreateNewAge.MOD_ID, "nuclear/energy_" + energy));
    }

    public static TagKey<net.minecraft.world.level.block.Block> createMagneticForgeTag(int force) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(CreateNewAge.MOD_ID, "magnet/force_" + force));
    }
    
    static void load() {
        Block.load();
        Item.load();
    }
}
