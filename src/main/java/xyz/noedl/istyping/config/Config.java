package xyz.noedl.istyping.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static class Client {
        public final ForgeConfigSpec.ConfigValue<Boolean> showAsIcon;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("istyping");
            this.showAsIcon = builder.comment("Show if the player is in chat with an icon if true, or with text if false.").define("Show as Icon", true);
            builder.pop();
        }
    }

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        Pair<Client, ForgeConfigSpec> clientForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientForgeConfigSpecPair.getLeft();
        CLIENT_SPEC = clientForgeConfigSpecPair.getRight();
    }
}
