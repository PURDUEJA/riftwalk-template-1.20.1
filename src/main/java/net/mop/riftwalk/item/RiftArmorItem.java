package net.mop.riftwalk.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RiftArmorItem extends ArmorItem {
    private static final int DIMENSION_COOLDOWN = 6000; // 5 minutes (300 seconds × 20 ticks)
    private static final Map<UUID, Long> lastDimensionTravelMap = new HashMap<>();
    private static final Map<UUID, Map<RegistryKey<World>, Vec3d>> lastPositionsMap = new HashMap<>();

    public RiftArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    // Check if player is wearing full set of Rift armor
    private boolean hasFullSet(PlayerEntity player) {
        for (ItemStack armorStack : player.getArmorItems()) {
            if (!(armorStack.getItem() instanceof RiftArmorItem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof ServerPlayerEntity player) {
            if (player.isSneaking() && player.age % 20 == 0 && hasFullSet(player)) {
                // Full set effects - apply Night Vision when sneaking
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 900, 0, false, false));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    // This method would be called from a keybind or custom right-click action
    public static boolean attemptDimensionalTravel(PlayerEntity player, RegistryKey<World> targetDimension) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return false;
        }

        World world = player.getWorld();
        UUID playerUUID = player.getUuid();

        // Check cooldown
        if (lastDimensionTravelMap.containsKey(playerUUID)) {
            long lastTravelTime = lastDimensionTravelMap.get(playerUUID);
            if (world.getTime() - lastTravelTime < DIMENSION_COOLDOWN) {
                int remainingSeconds = (int)((DIMENSION_COOLDOWN - (world.getTime() - lastTravelTime)) / 20);
                player.sendMessage(Text.literal("Dimensional rift on cooldown: " + remainingSeconds + "s remaining"), true);
                return false;
            }
        }

        ServerWorld targetWorld = serverPlayer.getServer().getWorld(targetDimension);
        if (targetWorld == null) {
            player.sendMessage(Text.literal("Target dimension not found!"), true);
            return false;
        }

        // Store current position for this dimension
        storePosition(playerUUID, world.getRegistryKey(), player.getPos());

        // Get position in target dimension (use stored position or spawn point)
        Vec3d targetPos = getStoredPosition(playerUUID, targetDimension);
        if (targetPos == null) {
            BlockPos spawnPos = targetWorld.getSpawnPos();
            targetPos = new Vec3d(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        }

        // Pre-teleport effects
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0f, 1.0f);

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.PORTAL,
                    player.getX(), player.getY() + 0.5, player.getZ(),
                    100, 0.5, 1.0, 0.5, 0.1
            );
        }

        // Teleport to the destination dimension
        serverPlayer.teleport(targetWorld,
                targetPos.x, targetPos.y, targetPos.z,
                player.getYaw(), player.getPitch());

        // Post-teleport effects at destination
        targetWorld.playSound(null, targetPos.x, targetPos.y, targetPos.z,
                SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0f, 1.0f);

        targetWorld.spawnParticles(
                ParticleTypes.PORTAL,
                targetPos.x, targetPos.y + 0.5, targetPos.z,
                100, 0.5, 1.0, 0.5, 0.1
        );

        // Set cooldown
        lastDimensionTravelMap.put(playerUUID, world.getTime());

        return true;
    }

    private static void storePosition(UUID playerUUID, RegistryKey<World> dimension, Vec3d pos) {
        lastPositionsMap.computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(dimension, pos);
    }

    private static Vec3d getStoredPosition(UUID playerUUID, RegistryKey<World> dimension) {
        if (lastPositionsMap.containsKey(playerUUID)) {
            return lastPositionsMap.get(playerUUID).get(dimension);
        }
        return null;
    }
}