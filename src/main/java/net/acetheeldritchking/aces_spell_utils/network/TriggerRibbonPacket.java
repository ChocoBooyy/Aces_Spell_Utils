package net.acetheeldritchking.aces_spell_utils.network;

import net.acetheeldritchking.aces_spell_utils.AcesSpellUtils;
import net.acetheeldritchking.aces_spell_utils.client.ribbon.RibbonManager;
import net.acetheeldritchking.aces_spell_utils.ribbon.RibbonConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class TriggerRibbonPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TriggerRibbonPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AcesSpellUtils.MOD_ID, "trigger_ribbon"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TriggerRibbonPacket> STREAM_CODEC = CustomPacketPayload.codec(TriggerRibbonPacket::write, TriggerRibbonPacket::new);

    private final int entityId;
    private final boolean attach;
    private final int color;
    private final float width;
    private final int length;
    private final float alpha;

    public TriggerRibbonPacket(int entityId, boolean attach, int color, float width, int length, float alpha) {
        this.entityId = entityId;
        this.attach = attach;
        this.color = color;
        this.width = width;
        this.length = length;
        this.alpha = alpha;
    }

    public TriggerRibbonPacket(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        attach = buf.readBoolean();
        color = buf.readInt();
        width = buf.readFloat();
        length = buf.readInt();
        alpha = buf.readFloat();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(attach);
        buf.writeInt(color);
        buf.writeFloat(width);
        buf.writeInt(length);
        buf.writeFloat(alpha);
    }

    public static void handle(TriggerRibbonPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (packet.attach) {
                RibbonManager.attach(packet.entityId, new RibbonConfig(packet.color, packet.width, packet.length, packet.alpha));
            } else {
                RibbonManager.detach(packet.entityId);
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
