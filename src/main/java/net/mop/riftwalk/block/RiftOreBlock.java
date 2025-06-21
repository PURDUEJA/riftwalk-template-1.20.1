package net.mop.riftwalk.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;

public class RiftOreBlock extends ExperienceDroppingBlock {
    public static final BooleanProperty LIT = BooleanProperty.of("lit");

    public RiftOreBlock(Settings settings) {
        super(settings, UniformIntProvider.create(20, 30));
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        light(state, world, pos);
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        light(state, world, pos);
        super.onBlockBreakStart(state, world, pos, player);
    }

    private static void light(BlockState state, World world, BlockPos pos) {
        if (!state.get(LIT)) {
            world.setBlockState(pos, state.with(LIT, true), Block.NOTIFY_ALL);
            world.scheduleBlockTick(pos, state.getBlock(), 60);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            world.setBlockState(pos, state.with(LIT, false), Block.NOTIFY_ALL);
        }
    }
}