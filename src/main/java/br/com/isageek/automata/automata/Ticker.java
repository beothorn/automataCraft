package br.com.isageek.automata.automata;

import br.com.isageek.automata.automata.states.LoadReplaceables;
import br.com.isageek.automata.forge.EntityClock;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.SystemEntityClock;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

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
        Level level,
        BlockPos blockPos,
        BlockState blockState,
        BlockEntity blockEntity
    ) {
        worldController.set(level);
        long now = entityClock.currentTimeMillis();
        ((AutomataStartBlockEntity) blockEntity).tick(blockPos,worldController,now);
    }
}
