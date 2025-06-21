package net.mop.riftwalk.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RiftbladeItem extends SwordItem {
    private static final int COOLDOWN_TICKS = 300; // 10 seconds (20 ticks per second)
    private static final float TELEPORT_RANGE = 100.0f; // Maximum teleport range

    public RiftbladeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // Check if the player is on cooldown
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            if (world.isClient) {
                player.sendMessage(Text.translatable("item.riftwalk.riftblade.cooldown"), true);
            }
            return new TypedActionResult<>(ActionResult.PASS, itemStack);
        }

        // Get where the player is looking using raycast
        HitResult hitResult = player.raycast(TELEPORT_RANGE, 0.0f, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            BlockPos hitPos = blockHit.getBlockPos();
            BlockPos targetPos = hitPos.offset(blockHit.getSide());

            // Check if there's enough space for the player (2 blocks high)
            if (!world.isClient() && world.isAir(targetPos) && world.isAir(targetPos.up())) {
                // Store original position for particles
                double originalX = player.getX();
                double originalY = player.getY();
                double originalZ = player.getZ();

                // Teleport the player to the target position
                player.teleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);

                // Add particles at both locations
                if (world instanceof ServerWorld serverWorld) {
                    // Departure particles
                    serverWorld.spawnParticles(
                            ParticleTypes.PORTAL,
                            originalX, originalY + 0.5, originalZ,
                            40, 0.2, 0.8, 0.2, 0.1
                    );

                    // Arrival particles
                    serverWorld.spawnParticles(
                            ParticleTypes.PORTAL,
                            targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5,
                            40, 0.2, 0.8, 0.2, 0.1
                    );
                }

                // Play teleport sounds
                world.playSound(null, originalX, originalY, originalZ,
                        SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);

                // Add cooldown
                player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            }

            return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
        }

        return new TypedActionResult<>(ActionResult.PASS, itemStack);
    }
}