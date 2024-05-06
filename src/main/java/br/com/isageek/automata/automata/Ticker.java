package br.com.isageek.automata.automata;

import br.com.isageek.automata.forge.EntityClock;
import br.com.isageek.automata.forge.SystemEntityClock;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class Ticker implements BlockEntityTicker {

    private final EntityClock entityClock;
    private final WorldController worldController;

    public Ticker(
        WorldController worldController
    ) {
        this.worldController = worldController;
        this.entityClock = new SystemEntityClock();
    }

    @Override
    public void tick(
        final @NotNull Level level,
        final @NotNull BlockPos blockPos,
        final @NotNull BlockState blockState,
        final @NotNull BlockEntity blockEntity
    ) {
        System.out.println("blockState "+blockState);
        worldController.set(level);
        long now = entityClock.currentTimeMillis();
        ((AutomataStartBlockEntity) blockEntity).tick(blockPos,worldController,now);
    }
}
