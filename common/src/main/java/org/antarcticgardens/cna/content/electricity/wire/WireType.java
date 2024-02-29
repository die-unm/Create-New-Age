package org.antarcticgardens.cna.content.electricity.wire;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.antarcticgardens.cna.CNAItems;
import org.antarcticgardens.cna.CreateNewAge;
import org.antarcticgardens.cna.config.CNAConfig;

public enum WireType {
    COPPER(1024, CNAItems.COPPER_WIRE::asStack),
    OVERCHARGED_IRON(2048, CNAItems.OVERCHARGED_IRON_WIRE::asStack),
    OVERCHARGED_GOLD(4096, CNAItems.OVERCHARGED_GOLDEN_WIRE::asStack),
    OVERCHARGED_DIAMOND(8192, CNAItems.OVERCHARGED_DIAMOND_WIRE::asStack);

    private final long conductivity;
    private final IRegistrateIsAFuckingShitNeverUseIt dropProvider;

    WireType(int conductivity, IRegistrateIsAFuckingShitNeverUseIt dropProvider) {
        this.conductivity = conductivity;
        this.dropProvider = dropProvider;
    }

    public ResourceLocation getTextureLocation() {
        return new ResourceLocation(CreateNewAge.MOD_ID, "textures/wire/" + name().toLowerCase() + ".png");
    }

    public long getConductivity() {
        return (long) (conductivity * CNAConfig.getCommon().conductivityMultiplier.get());
    }

    public ItemStack getDroppedItem() {
        return dropProvider.getDroppedItem();
    }
}
