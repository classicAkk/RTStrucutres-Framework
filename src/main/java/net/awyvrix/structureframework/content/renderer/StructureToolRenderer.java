package net.awyvrix.structureframework.content.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.awyvrix.structureframework.RTStructureFramework;
import net.awyvrix.structureframework.content.structureTool.CommandsState;
import net.awyvrix.structureframework.content.structureTool.StructureToolItem;
import net.awyvrix.structureframework.content.structureTool.StructureToolState;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = RTStructureFramework.MOD_ID, value = Dist.CLIENT)
public final class StructureToolRenderer {
    public static AABB previewBox = null;

    // Selection
    private static final float SEL_R = 0f;
    private static final float SEL_G = 1f;
    private static final float SEL_B = 0f;

    // Pos1
    private static final float POS1_R = 0.2f;
    private static final float POS1_G = 0.2f;
    private static final float POS1_B = 1f;

    // Pos2
    private static final float POS2_R = 1f;
    private static final float POS2_G = 0.2f;
    private static final float POS2_B = 0.2f;

    // Anchor
    private static final float ANCHOR_R = 0f;
    private static final float ANCHOR_G = 1f;
    private static final float ANCHOR_B = 1f;

    // Place Anchor
    private static final float PLACE_ANCHOR_R = 1f;
    private static final float PLACE_ANCHOR_G = 0f;
    private static final float PLACE_ANCHOR_B = 1f;

    // Preview
    private static final float PREVIEW_R = 1f;
    private static final float PREVIEW_G = 0.2f;
    private static final float PREVIEW_B = 0.2f;

    private static final float ALPHA = 1f;

    private StructureToolRenderer() {}

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;
        boolean holdingTool = mc.player.getMainHandItem().getItem() instanceof StructureToolItem;

        if (!holdingTool && !CommandsState.alwaysDisplay) return;
        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buffer.getBuffer(RenderType.lines());

        poseStack.pushPose();
        poseStack.translate(
                -camPos.x,
                -camPos.y,
                -camPos.z
        );

        if (StructureToolState.pos1 != null && StructureToolState.pos2 != null) {
            renderSelectionBox(
                    poseStack,
                    consumer,
                    StructureToolState.pos1,
                    StructureToolState.pos2,
                    SEL_R,
                    SEL_G,
                    SEL_B,
                    ALPHA
            );

            renderFilledSelectionBox(
                    poseStack,
                    consumer,
                    StructureToolState.pos1,
                    StructureToolState.pos2,
                    SEL_R,
                    SEL_G,
                    SEL_B,
                    1.0f
            );
        }

        if (StructureToolState.pos1 != null) {
            renderSingleBlockBox(
                    poseStack,
                    consumer,
                    StructureToolState.pos1,
                    POS1_R,
                    POS1_G,
                    POS1_B,
                    ALPHA
            );
        }
        if (StructureToolState.pos2 != null) {
            renderSingleBlockBox(
                    poseStack,
                    consumer,
                    StructureToolState.pos2,
                    POS2_R,
                    POS2_G,
                    POS2_B,
                    ALPHA
            );
        }

        if (StructureToolState.anchor != null) {
            renderSingleBlockBox(
                    poseStack,
                    consumer,
                    StructureToolState.anchor,
                    ANCHOR_R,
                    ANCHOR_G,
                    ANCHOR_B,
                    ALPHA
            );
        }
        if (StructureToolState.placeAnchor != null) {
            renderSingleBlockBox(
                    poseStack,
                    consumer,
                    StructureToolState.placeAnchor,
                    PLACE_ANCHOR_R,
                    PLACE_ANCHOR_G,
                    PLACE_ANCHOR_B,
                    ALPHA
            );
        }

        if (previewBox != null) {
            LevelRenderer.renderLineBox(
                    poseStack,
                    consumer,
                    previewBox,
                    PREVIEW_R,
                    PREVIEW_G,
                    PREVIEW_B,
                    ALPHA
            );

            LevelRenderer.renderLineBox(
                    poseStack,
                    consumer,
                    previewBox,
                    PREVIEW_R,
                    PREVIEW_G,
                    PREVIEW_B,
                    1.0f
            );
        }

        poseStack.popPose();
        buffer.endBatch(RenderType.lines());
    }

    private static void renderSingleBlockBox(
            PoseStack poseStack,
            VertexConsumer consumer,
            BlockPos pos,
            float r,
            float g,
            float b,
            float a
    ) {
        AABB box = new AABB(pos);
        LevelRenderer.renderLineBox(
                poseStack,
                consumer,
                box,
                r,
                g,
                b,
                a
        );
    }

    private static void renderSelectionBox(
            PoseStack poseStack,
            VertexConsumer consumer,
            BlockPos pos1,
            BlockPos pos2,
            float r,
            float g,
            float b,
            float a
    ) {
        AABB box = createBox(pos1, pos2);
        LevelRenderer.renderLineBox(
                poseStack,
                consumer,
                box,
                r,
                g,
                b,
                a
        );
    }

    private static void renderFilledSelectionBox(
            PoseStack poseStack,
            VertexConsumer consumer,
            BlockPos pos1,
            BlockPos pos2,
            float r,
            float g,
            float b,
            float a
    ) {
        AABB box = createBox(pos1, pos2);
        LevelRenderer.renderLineBox(
                poseStack,
                consumer,
                box,
                r,
                g,
                b,
                a
        );
    }

    private static AABB createBox(BlockPos pos1, BlockPos pos2) {
        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());

        int maxX = Math.max(pos1.getX(), pos2.getX()) + 1;
        int maxY = Math.max(pos1.getY(), pos2.getY()) + 1;
        int maxZ = Math.max(pos1.getZ(), pos2.getZ()) + 1;

        return new AABB(
                minX,
                minY,
                minZ,
                maxX,
                maxY,
                maxZ
        );
    }

    public static void setPreviewBox(BlockPos pos, int sizeX, int sizeY, int sizeZ) {
        previewBox = new AABB(
                pos.getX(),
                pos.getY(),
                pos.getZ(),

                pos.getX() + sizeX,
                pos.getY() + sizeY,
                pos.getZ() + sizeZ
        );
    }

    public static void clearPreviewBox() {
        previewBox = null;
    }
}