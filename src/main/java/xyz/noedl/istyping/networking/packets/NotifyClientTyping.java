package xyz.noedl.istyping.networking.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.noedl.istyping.config.Config;

import java.util.UUID;
import java.util.function.Supplier;

public class NotifyClientTyping {
    protected boolean isTyping;
    protected UUID player;

    public NotifyClientTyping(boolean isTyping, UUID player) {
        this.isTyping = isTyping;
        this.player = player;
    }

    public NotifyClientTyping(ByteBuf buf) {
        isTyping = buf.readBoolean();
        player = new UUID(buf.readLong(), buf.readLong());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isTyping);
        buf.writeLong(player.getMostSignificantBits());
        buf.writeLong(player.getLeastSignificantBits());
    }

    public static void handle(NotifyClientTyping msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Make sure it's only executed on the physical client
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientTypingHandler.handlePacket(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
}
