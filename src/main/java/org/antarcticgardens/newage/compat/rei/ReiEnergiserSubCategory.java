package org.antarcticgardens.newage.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.rei.category.animations.AnimatedKinetics;
import com.simibubi.create.compat.rei.category.sequencedAssembly.ReiSequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.Minecraft;;
import net.minecraft.network.chat.Component;
import org.antarcticgardens.newage.compat.RenderingUtil;
import org.antarcticgardens.newage.content.energiser.EnergisingRecipe;
import org.antarcticgardens.newage.tools.StringFormattingTool;

public class ReiEnergiserSubCategory extends ReiSequencedAssemblySubCategory {

    private final AnimatedKinetics energiser;

    public ReiEnergiserSubCategory() {
        super(25);
        energiser = new NotAnimatedEnergiser();
    }

    @Override
    public void draw(SequencedRecipe<?> recipe, PoseStack ms, double mouseX, double mouseY, int index) {
        energiser.offset = index;
        ms.pushPose();
        ms.translate(-7, 50, 0);
        ms.scale(.75f, .75f, .75f);
        energiser.draw(ms, getWidth() / 2, 0);
        ms.popPose();
        RenderingUtil.drawCenteredStringWithoutShadow(ms, Minecraft.getInstance().font, Component.literal(StringFormattingTool.formatLong(((EnergisingRecipe)recipe.getAsAssemblyRecipe()).energyNeeded) + " ⚡"), 13, 20, 0x1166ff);
    }
}
