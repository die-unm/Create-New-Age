package org.antarcticgardens.newage;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class NewAgeCreativeTab extends CreativeModeTab {
    public NewAgeCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return NewAgeBlocks.ENERGISER_T1.asStack();
    }
}
