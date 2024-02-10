package xyz.noedl.istyping.capability;

public class IsTypingCapability implements IIsTypingCapability{
    private boolean isTyping = false;

    @Override
    public boolean isTyping() {
        return isTyping;
    }

    @Override
    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
