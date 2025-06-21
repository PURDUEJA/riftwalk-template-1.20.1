package net.mop.riftwalk.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.mop.riftwalk.item.RiftArmorItem;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.Identifier;
import net.mop.riftwalk.Riftwalk;
import net.minecraft.client.gui.screen.Screen;

public class ModKeybinds {
    public static final String KEY_CATEGORY_RIFTWALK = "key.category.riftwalk";
    public static final String KEY_DIMENSION_TRAVEL = "key.riftwalk.dimension_travel";

    public static KeyBinding dimensionTravelKey;

    public static void registerKeybinds() {
        dimensionTravelKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DIMENSION_TRAVEL,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY_RIFTWALK
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dimensionTravelKey.wasPressed() && client.player != null) {
                // Check if player is wearing full rift armor
                boolean hasFullSet = true;
                for (ItemStack armorStack : client.player.getArmorItems()) {
                    if (!(armorStack.getItem() instanceof RiftArmorItem)) {
                        hasFullSet = false;
                        break;
                    }
                }

                if (hasFullSet) {
                    // If holding control, travel to The End
                    // If holding shift, travel to the Nether
                    // Otherwise travel to Overworld
                    int dimensionId = 0; // default: overworld
                    if (Screen.hasControlDown()) {
                        dimensionId = 2; // end
                    } else if (Screen.hasShiftDown()) {
                        dimensionId = 1; // nether
                    }

                    // Send packet to server
                    var buf = PacketByteBufs.create();
                    buf.writeInt(dimensionId);
                    ClientPlayNetworking.send(new Identifier(Riftwalk.MOD_ID, "dimension_travel"), buf);
                } else {
                    client.player.sendMessage(Text.literal("You need a full set of Rift armor to travel between dimensions"), true);
                }
            }
        });
    }
}