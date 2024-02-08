package xyz.noedl.istyping.content;



import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import xyz.noedl.istyping.IsTyping;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;


@Mod.EventBusSubscriber(modid = IsTyping.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Gui {

    private static final Logger LOGGER = LogManager.getLogger();
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        ITextComponent message = new StringTextComponent("Welcome to the server! " + event.getPlayer().getDisplayName().getString());
        event.getPlayer().sendMessage(message, event.getPlayer().getUniqueID());
    }


    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen gui = event.getGui();
        if (gui instanceof ChatScreen) {
            NetworkPlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(Minecraft.getInstance().player.getUniqueID());
            LOGGER.info("Chat screen detected");
            if (playerInfo == null) {
                LOGGER.error("PlayerInfo is null!");
                return;
            }
            String playerName = playerInfo.getGameProfile().getName();
            if (playerName == null) {
                LOGGER.error("Player name is null!");
                return;
            }

            String modifiedPlayerName = playerName + " [Typing]";
            // Set the modified name back to the player's display name
            playerInfo.setDisplayName(new StringTextComponent(modifiedPlayerName));
            LOGGER.info("Player " + playerName + " is typing");
        }
    }

    private int ticks = 0;
    private static final int LOG_INTERVAL = 200;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player.world.isRemote) {
            if (ticks % LOG_INTERVAL == 0) {
                logPlayerList(event.player.world);
            }
            ticks++;
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote) {
            if (ticks % LOG_INTERVAL == 0) {
                logPlayerList(event.world);
            }
            ticks++;
        }
    }

    private void logPlayerList(World world) {
        LOGGER.info("Player List:");
        for (ServerPlayerEntity player : world.getServer().getPlayerList().getPlayers()) {
            LOGGER.info(player.getDisplayName().getString());
        }
    }

}
