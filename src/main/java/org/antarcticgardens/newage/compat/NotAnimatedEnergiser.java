package org.antarcticgardens.newage.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.Direction;
import org.antarcticgardens.newage.NewAgeBlocks;

public class NotAnimatedEnergiser extends AnimatedKinetics {
    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

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
}
