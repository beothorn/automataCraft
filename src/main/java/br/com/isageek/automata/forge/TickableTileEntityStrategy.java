package br.com.isageek.automata.forge;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;

public class TickableTileEntityStrategy {

    private EntityTick current;
    private final EntityClock entityClock;
    private final WorldController worldController;
    private long lastTick = 0;

    public TickableTileEntityStrategy(
            BlockEntity be,
        WorldController worldController,
        EntityTick initial,
        EntityClock entityClock
    ) {
        this.worldController = worldController;
        current = initial;
        this.entityClock = entityClock;
        be.
    }

    public void tick() {
        worldController.set(level);
        long now = entityClock.currentTimeMillis();
        current = current.tick(getBlockPos(), worldController, now - lastTick);
        this.lastTick = now;
    }

}
