package org.antarcticgardens.newage.content.electricity.connector;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.antarcticgardens.newage.NewAgeRenderTypes;
import org.antarcticgardens.newage.config.NewAgeConfig;
import org.antarcticgardens.newage.content.electricity.wire.ElectricWireItem;
import org.antarcticgardens.newage.content.electricity.wire.WireType;

import java.util.Map;

import static org.antarcticgardens.newage.tools.RaycastUtil.pickBlockFromPos;

public class ElectricalConnectorRenderer implements BlockEntityRenderer<ElectricalConnectorBlockEntity> {
    public static final float SAG_FACTOR = 0.92f;

    public static final int[] TOO_LONG1 = { 150, 0, 0, 255 };
    public static final int[] TOO_LONG2 = { 204, 0, 0, 255 };

    public ElectricalConnectorRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(ElectricalConnectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        VertexConsumer consumer = buffer.getBuffer(NewAgeRenderTypes.WIRE);
        BlockPos pos = blockEntity.getBlockPos();
        Vector3f from = new Vector3f(0.0f, 0.0f, 0.0f);

        for (Map.Entry<BlockPos, WireType> e : blockEntity.getConnectorPositions().entrySet()) {
            if (e.getKey().hashCode() > blockEntity.getBlockPos().hashCode())
                continue;

            poseStack.pushPose();

            poseStack.translate(0.5f, 0.5f, 0.5f);
            Matrix4f pose = poseStack.last().pose();

            Vector3f to = new Vector3f(
                    e.getKey().getX() - pos.getX(),
                    e.getKey().getY() - pos.getY(),
                    e.getKey().getZ() - pos.getZ()
            );

            renderWire(consumer, pose, from, to, blockEntity, e.getValue().getColor1(), e.getValue().getColor2());

            poseStack.popPose();
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            ItemStack itemInHand = player.getMainHandItem();

            if (!(itemInHand.getItem() instanceof ElectricWireItem))
                itemInHand = player.getOffhandItem();

            if (itemInHand.getItem() instanceof ElectricWireItem wire) {
                BlockPos bound = wire.getBoundConnector(itemInHand);

                if (bound != null && bound.equals(blockEntity.getBlockPos())) {
                    Vec3 eyePos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                    Vec3 endPos = eyePos.add(player.getViewVector(partialTick).normalize().scale(2.0f));

                    HitResult hit = pickBlockFromPos(blockEntity.getLevel(), eyePos, player.getViewVector(partialTick), Minecraft.getInstance().gameMode.getPickRange());

                    if (hit instanceof BlockHitResult blockHit) {
                        Vec3 vec = eyePos.add(blockHit.getLocation().subtract(eyePos).scale(0.9f));

                        if (eyePos.distanceTo(endPos) > eyePos.distanceTo(vec))
                            endPos = vec;
                    }

                    Vector3f to = new Vector3f(
                            (float) (endPos.x - pos.getX() - 0.5),
                            (float) (endPos.y - pos.getY() - 0.5),
                            (float) (endPos.z - pos.getZ() - 0.5)
                    );

                    double distance = endPos.distanceToSqr(bound.getX(), bound.getY(), bound.getZ());

                    if (distance > Mth.square(ElectricWireItem.MAX_DISTANCE * 2))
                        return;

                    int[] color1 = wire.getWireType().getColor1();
                    int[] color2 = wire.getWireType().getColor2();

                    if (Minecraft.getInstance().gameMode != null && hit instanceof BlockHitResult blockHit) {
                        if (blockEntity.getLevel().getBlockEntity(blockHit.getBlockPos()) instanceof ElectricalConnectorBlockEntity connector) {
                            if (connector.isConnected(blockEntity.getBlockPos()))
                                return;

                            to = new Vector3f(
                                    blockHit.getBlockPos().getX() - pos.getX(),
                                    blockHit.getBlockPos().getY() - pos.getY(),
                                    blockHit.getBlockPos().getZ() - pos.getZ()
                            );

                            distance = connector.getBlockPos().distSqr(blockEntity.getBlockPos());
                        }
                    }

                    if (distance > Mth.square(ElectricWireItem.MAX_DISTANCE)) {
                        color1 = TOO_LONG1;
                        color2 = TOO_LONG2;
                    }

                    poseStack.pushPose();

                    poseStack.translate(0.5f, 0.5f, 0.5f);
                    Matrix4f pose = poseStack.last().pose();

                    renderWire(consumer, pose, to, from, blockEntity, color1, color2);

                    poseStack.popPose();
                }
            }
        }
    }

    private void renderWire(VertexConsumer consumer, Matrix4f pose, Vector3f from, Vector3f to, ElectricalConnectorBlockEntity blockEntity, int[] color1, int[] color2) {
        Vector3f lastSection = from;
        Vector3f direction = new Vector3f(to.x(), to.y(), to.z());
        direction.sub(from);
        direction.normalize();
        float distance = (float) new Vec3(to).distanceTo(new Vec3(from));
        int sections = (int) Math.ceil(distance * NewAgeConfig.getClient().wireSectionsPerMeter.get());
        float perSection = distance / sections;

        for (int i = 0; i <= sections; i++) {
            int[] color = (i % 2 == 0) ? color1 : color2;
            Vector3f sectionTo = direction.copy();
            sectionTo.mul(perSection * i);
            sectionTo.add(0.0f, catenary(i, distance, sections), 0.0f);
            var somewhere = sectionTo.copy();
            somewhere.add(from);
            wireSection(consumer, pose, lastSection, somewhere, color, calculateLighting(blockEntity, lastSection, sectionTo));
            lastSection = sectionTo;
        }
    }

    @Override
    public boolean shouldRenderOffScreen(ElectricalConnectorBlockEntity blockEntity) {
        return true;
    }

    private int calculateLighting(BlockEntity entity, Vector3f pos, Vector3f pos1) {
        BlockPos blockPos = new BlockPos(entity.getBlockPos()).offset(Math.round(pos.x()), Math.round(pos.y()), Math.round(pos.z()));
        BlockPos blockPos1 = new BlockPos(entity.getBlockPos()).offset(Math.round(pos1.x()), Math.round(pos1.y()), Math.round(pos1.z()));

        int sky = entity.getLevel().getBrightness(LightLayer.SKY, blockPos);
        int block = entity.getLevel().getBrightness(LightLayer.BLOCK, blockPos);
        int sky1 = entity.getLevel().getBrightness(LightLayer.SKY, blockPos1);
        int block1 = entity.getLevel().getBrightness(LightLayer.BLOCK, blockPos1);

        return LightTexture.pack(Math.max(block, block1), Math.max(sky, sky1));
    }

    private float catenary(double x, double length, int sections) {
        double a = length / ElectricWireItem.MAX_DISTANCE * SAG_FACTOR;
        x = (x / sections * 2 - 1);
        return (float) ((Math.cosh(x) - Math.cosh(1.0f)) * a);
    }

    private void wireSection(VertexConsumer consumer, Matrix4f pose, Vector3f from, Vector3f to, int[] color, int light) {
        Vector3f direction = to.copy();
                direction.sub(from);
                direction.normalize();
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);

        if (isVertical(direction, up))
            up = new Vector3f(1.0f, 0.0f, 0.0f);

        pose = new Matrix4f(pose);
        pose.translate(from);
        pose.multiply(Quaternion.fromXYZ(direction));

        int r = color[0];
        int g = color[1];
        int b = color[2];
        int z = color[3];

        float f = NewAgeConfig.getClient().wireThickness.get().floatValue() / 2;
        float distance = (float) new Vec3(from).distanceTo(new Vec3(to));

        consumer.vertex(pose, -f, -f, 0.0f).color(r, g, b, z).uv2(light).endVertex();
        consumer.vertex(pose, -f, -f, distance).color(r, g, b, z).uv2(light).endVertex();
        consumer.vertex(pose, f, f, distance).color(r, g, b, z).uv2(light).endVertex();
        consumer.vertex(pose, f, f, 0.0f).color(r, g, b, z).uv2(light).endVertex();

        consumer.vertex(pose, f, -f, 0.0f).color(r, g, b, z).uv2(light).endVertex();
        consumer.vertex(pose, f, -f, distance).color(r, g, b, z).uv2(light).endVertex();
        consumer.vertex(pose, -f, f, distance).color(r, g, b, z).uv2(light).endVertex();
        consumer.vertex(pose, -f, f, 0.0f).color(r, g, b, z).uv2(light).endVertex();
    }

    private boolean isVertical(Vector3f vec, Vector3f up) {
        var uup = up;
        uup.mul(-1.0f);
        return vec.equals(up) || vec.equals(uup);
    }
}
