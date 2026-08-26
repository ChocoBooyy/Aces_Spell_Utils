package net.acetheeldritchking.aces_spell_utils.client.ribbon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.acetheeldritchking.aces_spell_utils.ribbon.RibbonConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public final class RibbonRenderer {
    private RibbonRenderer() {
    }

    public static void render(PoseStack poseStack, Vec3 cam, float partialTick) {
        if (RibbonManager.active().isEmpty()) {
            return;
        }
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        Matrix4f matrix = poseStack.last().pose();
        // each pass grabs its own consumer and finishes with it before the next pass starts, since both
        // ribbon types share one buffer and requesting a second consumer flushes the first mid-batch
        renderPass(matrix, buffers, cam, partialTick, level, false);
        renderPass(matrix, buffers, cam, partialTick, level, true);
        // ending a type that received no geometry is a no-op, so both are flushed rather than tracked
        buffers.endBatch(RibbonRenderTypes.RIBBON);
        buffers.endBatch(RibbonRenderTypes.RIBBON_ADDITIVE);
    }

    private static void renderPass(Matrix4f matrix, MultiBufferSource.BufferSource buffers, Vec3 cam, float partialTick, ClientLevel level, boolean additive) {
        RenderType type = additive ? RibbonRenderTypes.RIBBON_ADDITIVE : RibbonRenderTypes.RIBBON;
        VertexConsumer consumer = buffers.getBuffer(type);
        for (Map.Entry<Integer, ActiveRibbon> entry : RibbonManager.active().entrySet()) {
            ActiveRibbon ribbon = entry.getValue();
            if (ribbon.config().additive() != additive) {
                continue;
            }
            Entity entity = level.getEntity(entry.getKey());
            Vec3 head = entity == null ? null : entity.getPosition(partialTick);
            emit(matrix, consumer, ribbon, cam, head);
        }
    }

    private static Vec3 pointAt(List<Vec3> points, int i, int last, Vec3 head) {
        return i == last && head != null ? head : points.get(i);
    }

    private static void emit(Matrix4f matrix, VertexConsumer consumer, ActiveRibbon ribbon, Vec3 cam, Vec3 head) {
        List<Vec3> points = ribbon.points();
        int last = points.size() - 1;
        if (last < 1) {
            return;
        }
        RibbonConfig config = ribbon.config();
        for (int i = 0; i < last; i++) {
            // the head point is interpolated with the partial tick so the strip stays attached to the entity between ticks
            Vec3 a = pointAt(points, i, last, head).subtract(cam);
            Vec3 c = pointAt(points, i + 1, last, head).subtract(cam);
            Vec3 seg = c.subtract(a);
            double len = seg.length();
            // a zero length segment has no direction, and a point at the camera has no view vector
            if (len < 1.0E-6 || a.lengthSqr() < 1.0E-12) {
                continue;
            }
            Vec3 dir = seg.scale(1.0 / len);
            Vec3 side = dir.cross(a.scale(-1.0).normalize()).normalize();
            float t0 = (float) i / last;
            float t1 = (float) (i + 1) / last;
            Vec3 s0 = side.scale(config.width().at(t0));
            Vec3 s1 = side.scale(config.width().at(t1));
            int c0 = config.color().at(t0);
            int c1 = config.color().at(t1);
            int r0 = (c0 >> 16) & 0xFF;
            int g0 = (c0 >> 8) & 0xFF;
            int b0 = c0 & 0xFF;
            int r1 = (c1 >> 16) & 0xFF;
            int g1 = (c1 >> 8) & 0xFF;
            int b1 = c1 & 0xFF;
            int a0 = Mth.clamp((int) (config.alpha().at(t0) * 255.0F), 0, 255);
            int a1 = Mth.clamp((int) (config.alpha().at(t1) * 255.0F), 0, 255);
            consumer.addVertex(matrix, (float) (a.x - s0.x), (float) (a.y - s0.y), (float) (a.z - s0.z)).setColor(r0, g0, b0, a0);
            consumer.addVertex(matrix, (float) (a.x + s0.x), (float) (a.y + s0.y), (float) (a.z + s0.z)).setColor(r0, g0, b0, a0);
            consumer.addVertex(matrix, (float) (c.x + s1.x), (float) (c.y + s1.y), (float) (c.z + s1.z)).setColor(r1, g1, b1, a1);
            consumer.addVertex(matrix, (float) (c.x - s1.x), (float) (c.y - s1.y), (float) (c.z - s1.z)).setColor(r1, g1, b1, a1);
        }
    }
}
