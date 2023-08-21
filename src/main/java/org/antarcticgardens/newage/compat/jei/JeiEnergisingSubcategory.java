package org.antarcticgardens.newage.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.compat.jei.category.sequencedAssembly.JeiSequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import org.antarcticgardens.newage.NewAgeBlocks;
import org.antarcticgardens.newage.content.energiser.EnergisingRecipe;
import org.antarcticgardens.newage.tools.StringFormattingTool;
import org.jetbrains.annotations.NotNull;

public class JeiEnergisingSubcategory extends JeiSequencedAssemblySubCategory {

    AnimatedKinetics energiser;

    public JeiEnergisingSubcategory() {
        super(25);
        energiser = new AnimatedKinetics() {
            @Override
            public void draw(@NotNull PoseStack matrixStack, int xOffset, int yOffset) {
                matrixStack.pushPose();
                matrixStack.translate(xOffset, yOffset, 200);

                blockElement(shaft(Direction.Axis.Z))
                        .rotateBlock(0, 0, getCurrentAngle())
                        .scale(20)
                        .render(matrixStack);

                blockElement(NewAgeBlocks.ENERGISER_T1.getDefaultState())
                        .scale(20)
                        .render(matrixStack);

                blockElement(AllBlocks.DEPOT.getDefaultState())
                        .scale(20)
                        .atLocal(0, 2, 0)
                        .render(matrixStack);

                matrixStack.popPose();
            }
        };
    }

    @Override
    public void draw(SequencedRecipe<?> recipe, PoseStack poseStack, double mouseX, double mouseY, int index) {
        energiser.offset = index;
        poseStack.pushPose();
        poseStack.translate(-7, 50, 0);
        poseStack.scale(.75f, .75f, .75f);
        energiser.draw(poseStack, getWidth() / 2, 0);
        poseStack.popPose();
        Minecraft.getInstance().font.draw(poseStack, StringFormattingTool.formatLong(((EnergisingRecipe)recipe.getAsAssemblyRecipe()).energyNeeded) + " ⚡", 0, 20, 0x1166ff);
    }
}
