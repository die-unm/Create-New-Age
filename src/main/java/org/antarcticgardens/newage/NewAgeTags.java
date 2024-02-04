package org.antarcticgardens.newage;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class NewAgeTags {
    public static final TagKey<Item> MAGNET_TAG = new TagKey<>(Registry.ITEM_REGISTRY, new ResourceLocation(CreateNewAge.MOD_ID, "magnet"));

    public static final TagKey<Block> CUSTOM_MAGNET_TAG = new TagKey<>(Registry.BLOCK_REGISTRY, new ResourceLocation(CreateNewAge.MOD_ID, "custom_magnet"));


}
