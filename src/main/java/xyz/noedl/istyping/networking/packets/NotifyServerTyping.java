package xyz.noedl.istyping.networking.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import xyz.noedl.istyping.capability.IsTypingManager;
import xyz.noedl.istyping.networking.NetworkChannel;

import java.util.UUID;
import java.util.function.Supplier;

public class NotifyServerTyping {
    private boolean isTyping;
    private UUID player;

    public NotifyServerTyping(boolean isTyping, UUID player) {
        this.isTyping = isTyping;
        this.player = player;
    }

    public NotifyServerTyping(ByteBuf buf) {
        isTyping = buf.readBoolean();
        player = new UUID(buf.readLong(), buf.readLong());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isTyping);
        buf.writeLong(player.getMostSignificantBits());
        buf.writeLong(player.getLeastSignificantBits());
    }

    public static void handle(NotifyServerTyping msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ctx.get().getSender().getServerWorld().getPlayerByUuid(msg.player).getCapability(IsTypingManager.ISTYPING).ifPresent(cap -> cap.setTyping(msg.isTyping));
            NetworkChannel.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new NotifyClientTyping(msg.isTyping, msg.player));
        });
        ctx.get().setPacketHandled(true);
    }
}
