package xyz.noedl.istyping;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.noedl.istyping.capability.IIsTypingCapability;
import xyz.noedl.istyping.capability.IsTypingCapability;
import xyz.noedl.istyping.capability.IsTypingManager;
import xyz.noedl.istyping.config.Config;
import xyz.noedl.istyping.content.Gui;
import xyz.noedl.istyping.networking.NetworkChannel;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(IsTyping.MOD_ID)
public class IsTyping {
    // Directly reference mod ID
    public static final String MOD_ID = "istyping";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public IsTyping() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, IsTypingManager::onStackAttachCapabilities);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the Gui class as an event subscriber
        MinecraftForge.EVENT_BUS.register(new Gui());

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkChannel.register();
        IsTypingManager.registerCapabilities();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event) {
        // Send a message in chat when the mod is reloaded
    }
}
