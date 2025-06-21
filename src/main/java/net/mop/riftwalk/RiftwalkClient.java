package net.mop.riftwalk;

import net.fabricmc.api.ClientModInitializer;
import net.mop.riftwalk.client.ModKeybinds;

public class RiftwalkClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeybinds.registerKeybinds();
    }
}
