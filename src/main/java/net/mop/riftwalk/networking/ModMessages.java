package net.mop.riftwalk.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.mop.riftwalk.Riftwalk;
import net.mop.riftwalk.item.RiftArmorItem;

public class ModMessages {
    public static final Identifier DIMENSION_TRAVEL_ID =
            new Identifier(Riftwalk.MOD_ID, "dimension_travel");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(DIMENSION_TRAVEL_ID,
                (server, player, handler, buf, responseSender) -> {
                    int dimensionId = buf.readInt();

                    server.execute(() -> {
                        RegistryKey<World> targetDimension;

                        switch (dimensionId) {
                            case 1 -> targetDimension = World.NETHER;
                            case 2 -> targetDimension = World.END;
                            default -> targetDimension = World.OVERWORLD;
                        }

                        RiftArmorItem.attemptDimensionalTravel(player, targetDimension);
                    });
                });
    }

    public static void registerS2CPackets() {
        // Any packets from server to client would go here
    }
}