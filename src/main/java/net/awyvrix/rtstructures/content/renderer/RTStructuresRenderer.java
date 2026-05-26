package net.awyvrix.rtstructures.content.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.content.renderer.renderType.*;
import net.awyvrix.rtstructures.content.tools.linkTool.LinkToolItem;
import net.awyvrix.rtstructures.content.tools.linkTool.StructureNode;
import net.awyvrix.rtstructures.content.tools.structureTool.CommandsState;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.HashMap;
import java.util.OptionalDouble;

@EventBusSubscriber(modid = RTStructuresFramework.MOD_ID, value = Dist.CLIENT)
public final class RTStructuresRenderer {

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;
        Item item = mc.player.getMainHandItem().getItem();
        boolean holdingTool = item instanceof StructureToolItem || item instanceof LinkToolItem;

        if (!holdingTool && !CommandsState.alwaysDisplay) return;
        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buffer.getBuffer(BOX_LINES);
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        // Single Block
        for (BlockRenderType type : BlockRenderType.values()) {
            BlockPos pos = type.getPos();

            if (pos == null) continue;
            BoxColor color = type.getColor();
            AABB box = new AABB(pos).inflate(type.getInflate());
            LevelRenderer.renderLineBox(poseStack, consumer, box, color.r(), color.g(), color.b(), 1f);
        }

        consumer = buffer.getBuffer(RenderType.lines());

        // Half Block
        for (HalfBlockRenderType type : HalfBlockRenderType.values()) {
            BlockPos pos = type.getPos();

            if (pos == null) continue;
            BoxColor color = type.getColor();
            AABB box = createHalfBox(pos).inflate(type.getInflate());
            LevelRenderer.renderLineBox(poseStack, consumer, box, color.r(), color.g(), color.b(), 1f);
        }

        // Half Block Map
        for (HalfBlockMapRenderType type : HalfBlockMapRenderType.values()) {
            HashMap<BlockPos, StructureNode> map = type.getPos();
            for (BlockPos pos : map.keySet()) {
                BoxColor color = type.getColor();
                AABB box = createHalfBox(pos).inflate(type.getInflate());
                LevelRenderer.renderLineBox(poseStack, consumer, box, color.r(), color.g(), color.b(), 1f);
            }
        }

        // Area
        for (AreaRenderType type : AreaRenderType.values()) {
            BlockPos pos1 = type.getPos1();
            BlockPos pos2 = type.getPos2();

            if (pos1 == null || pos2 == null) continue;
            BoxColor color = type.getColor();
            AABB box = createBox(pos1, pos2).inflate(type.getInflate());;
            LevelRenderer.renderLineBox(poseStack, consumer, box, color.r(), color.g(), color.b(), 1f);
        }

        // Sized Area
        for (SizedBoxRenderType type : SizedBoxRenderType.values()) {
            BlockPos pos = type.getPos();

            if (pos == null) continue;
            AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + type.getSizeX(),
                    pos.getY() + type.getSizeY(),
                    pos.getZ() + type.getSizeZ()
            ).inflate(type.getInflate());;

            BoxColor color = type.getColor();
            LevelRenderer.renderLineBox(poseStack, consumer, box, color.r(), color.g(), color.b(), 1.0f);
        }

        poseStack.popPose();
        buffer.endBatch(RenderType.lines());
    }

    private static AABB createBox(BlockPos pos1, BlockPos pos2) {
        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX()) + 1;
        int maxY = Math.max(pos1.getY(), pos2.getY()) + 1;
        int maxZ = Math.max(pos1.getZ(), pos2.getZ()) + 1;

        return new AABB (minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static AABB createHalfBox(BlockPos pos) {
        return new AABB (pos.getX() + 0.75, pos.getY() + 0.75, pos.getZ() + 0.75,
                pos.getX() + 0.25, pos.getY() + 0.25, pos.getZ() + 0.25);
    }

    private RTStructuresRenderer() {}

    public static final RenderType BOX_LINES = RenderType.create(
            "box_lines",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setTransparencyState(RenderType.NO_TRANSPARENCY)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .setCullState(RenderType.NO_CULL)
                    .setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );
}