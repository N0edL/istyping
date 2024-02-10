package xyz.noedl.istyping.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import xyz.noedl.istyping.IsTyping;

import javax.annotation.Nonnull;

public final class IsTypingManager {
    @CapabilityInject(IIsTypingCapability.class)
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static Capability<IIsTypingCapability> ISTYPING = null;
    public static final ResourceLocation ISTYPING_NAME = new ResourceLocation(IsTyping.MOD_ID, "is_typing");

    private IsTypingManager() {}

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(
                IIsTypingCapability.class,
                new Capability.IStorage<IIsTypingCapability>() {
                    public INBT writeNBT(Capability<IIsTypingCapability> capability, IIsTypingCapability instance, Direction side) {
                        if (capability != ISTYPING) return null;
                        final CompoundNBT nbt = new CompoundNBT();
                        nbt.putBoolean("isTyping", instance.isTyping());
                        return nbt;
                    }
                    public void readNBT(Capability<IIsTypingCapability> capability, IIsTypingCapability instance, Direction side, INBT nbt) {
                        if (capability != ISTYPING) return;
                        if (nbt.getId() != 10) {
                            throw new IllegalStateException("The NBT type " + nbt.getClass().getSimpleName() + " is not suitable for the capability " + capability);
                        }
                        instance.setTyping(((CompoundNBT) nbt).getBoolean("isTyping"));
                    }
                },
                IsTypingCapability::new
        );
    }

    // This attaches the capability to every ItemStack as an example
    // You can also do this for other capability providers
    public static void onStackAttachCapabilities(@Nonnull final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            final IIsTypingCapability capability = new IsTypingCapability();
            event.addCapability(ISTYPING_NAME, IsTypingProvider.from(ISTYPING, () -> capability));
        }
    }
}
