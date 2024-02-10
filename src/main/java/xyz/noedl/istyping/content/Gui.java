package xyz.noedl.istyping.content;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import xyz.noedl.istyping.IsTyping;
import xyz.noedl.istyping.capability.IsTypingManager;
import xyz.noedl.istyping.config.Config;
import xyz.noedl.istyping.networking.NetworkChannel;
import xyz.noedl.istyping.networking.packets.NotifyClientTyping;
import xyz.noedl.istyping.networking.packets.NotifyServerTyping;


@Mod.EventBusSubscriber(modid = IsTyping.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Gui {
    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().getEntityWorld().isRemote) {
            for (ServerPlayerEntity player : event.getPlayer().getEntityWorld().getServer().getPlayerList().getPlayers()) {
                player.getCapability(IsTypingManager.ISTYPING).ifPresent(cap -> {
                    if (cap.isTyping()) {
                        NetworkChannel.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) event.getPlayer())), new NotifyClientTyping(true, player.getUniqueID()));
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getPlayer().getEntityWorld().isRemote) {
            for (ServerPlayerEntity player : event.getPlayer().getEntityWorld().getServer().getPlayerList().getPlayers()) {
                player.getCapability(IsTypingManager.ISTYPING).ifPresent(cap -> {
                    if (cap.isTyping()) {
                        NetworkChannel.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) event.getPlayer())), new NotifyClientTyping(true, player.getUniqueID()));
                    }
                });
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.getCapability(IsTypingManager.ISTYPING).ifPresent(cap -> {
            if (Minecraft.getInstance().currentScreen instanceof ChatScreen != cap.isTyping()) {
                cap.setTyping(Minecraft.getInstance().currentScreen instanceof ChatScreen);
                NetworkChannel.NETWORK_CHANNEL.sendToServer(new NotifyServerTyping(Minecraft.getInstance().currentScreen instanceof ChatScreen, Minecraft.getInstance().player.getUniqueID()));
            }
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderNameplate(final RenderNameplateEvent event) {
        event.getEntity().getCapability(IsTypingManager.ISTYPING).ifPresent(cap -> {
            if (cap.isTyping()) {
                event.setContent(event.getContent().deepCopy().appendString(Config.CLIENT.showAsIcon.get() ? " \uFFD1" : " [Typing]").setStyle(Style.EMPTY.setFontId(new ResourceLocation("istyping", "icons"))));
            }
        });
    }
}
