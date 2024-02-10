package xyz.noedl.istyping.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IsTypingProvider implements ICapabilityProvider {
    private final Capability<IIsTypingCapability> capability;
    private final LazyOptional<IIsTypingCapability> implementation;

    protected IsTypingProvider(@Nonnull final Capability<IIsTypingCapability> capability, @Nonnull final LazyOptional<IIsTypingCapability> implementation) {
        this.capability = capability;
        this.implementation = implementation;
    }

    @Nonnull
    public static IsTypingProvider from(@Nonnull final Capability<IIsTypingCapability> cap, @Nonnull final NonNullSupplier<IIsTypingCapability> impl) {
        return new IsTypingProvider(cap, LazyOptional.of(impl));
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == this.capability) return this.implementation.cast();
        return LazyOptional.empty();
    }
}
