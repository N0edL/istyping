package xyz.noedl.istyping.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.noedl.istyping.capability.IsTypingManager;
import xyz.noedl.istyping.config.Config;

import java.util.function.Supplier;

public class ClientTypingHandler {
    public static void handlePacket(NotifyClientTyping msg, Supplier<NetworkEvent.Context> ctx) {
        Logger LOGGER = LogManager.getLogger();
        NetworkPlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(msg.player);
        if (Minecraft.getInstance().world != null) {
            Minecraft.getInstance().world.getPlayerByUuid(msg.player).getCapability(IsTypingManager.ISTYPING).ifPresent(cap -> cap.setTyping(msg.isTyping));
        }
        if (playerInfo == null) {
            LOGGER.error("PlayerInfo is null!");
            return;
        }
        String playerName = playerInfo.getGameProfile().getName();
        if (playerName == null) {
            LOGGER.error("Player name is null!");
            return;
        }
        String modifiedPlayerName = playerName + (Config.CLIENT.showAsIcon.get() ? " 1" : " [Typing]");
        playerInfo.setDisplayName(new StringTextComponent(msg.isTyping ? modifiedPlayerName : playerName));
    }
}
