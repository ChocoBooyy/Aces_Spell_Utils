package net.acetheeldritchking.aces_spell_utils.client.ribbon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.acetheeldritchking.aces_spell_utils.ribbon.RibbonConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
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
        VertexConsumer consumer = buffers.getBuffer(RibbonRenderTypes.RIBBON);
        Matrix4f matrix = poseStack.last().pose();
        for (Map.Entry<Integer, ActiveRibbon> entry : RibbonManager.active().entrySet()) {
            Entity entity = level.getEntity(entry.getKey());
            Vec3 head = entity == null ? null : entity.getPosition(partialTick);
            emit(matrix, consumer, entry.getValue(), cam, head);
        }
        buffers.endBatch(RibbonRenderTypes.RIBBON);
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
        int r = (config.color() >> 16) & 0xFF;
        int g = (config.color() >> 8) & 0xFF;
        int b = config.color() & 0xFF;
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
            Vec3 s0 = side.scale(config.width() * t0);
            Vec3 s1 = side.scale(config.width() * t1);
            int a0 = (int) (config.alpha() * t0 * 255.0F);
            int a1 = (int) (config.alpha() * t1 * 255.0F);
            consumer.addVertex(matrix, (float) (a.x - s0.x), (float) (a.y - s0.y), (float) (a.z - s0.z)).setColor(r, g, b, a0);
            consumer.addVertex(matrix, (float) (a.x + s0.x), (float) (a.y + s0.y), (float) (a.z + s0.z)).setColor(r, g, b, a0);
            consumer.addVertex(matrix, (float) (c.x + s1.x), (float) (c.y + s1.y), (float) (c.z + s1.z)).setColor(r, g, b, a1);
            consumer.addVertex(matrix, (float) (c.x - s1.x), (float) (c.y - s1.y), (float) (c.z - s1.z)).setColor(r, g, b, a1);
        }
    }
}
