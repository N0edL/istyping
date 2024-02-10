package xyz.noedl.istyping.networking;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.noedl.istyping.IsTyping;
import xyz.noedl.istyping.networking.packets.NotifyClientTyping;
import xyz.noedl.istyping.networking.packets.NotifyServerTyping;

public class NetworkChannel {
    public static SimpleChannel NETWORK_CHANNEL;

    private static int packetId = 0;

    public static void register() {
        NETWORK_CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(IsTyping.MOD_ID, "main"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        NETWORK_CHANNEL.registerMessage(packetId++, NotifyServerTyping.class, NotifyServerTyping::toBytes, NotifyServerTyping::new, NotifyServerTyping::handle);
        NETWORK_CHANNEL.registerMessage(packetId++, NotifyClientTyping.class, NotifyClientTyping::toBytes, NotifyClientTyping::new, NotifyClientTyping::handle);
    }
}
