package br.com.isageek.automata.forge;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TickableTileEntityStrategy extends TileEntity implements ITickableTileEntity {

    private EntityTick current;
    private final EntityClock entityClock;
    private final WorldController worldController;
    private long lastTick = 0;

    public TickableTileEntityStrategy(
            TileEntityType<TickableTileEntityStrategy> tileEntityType,
            WorldController worldController,
            EntityTick initial,
            EntityClock entityClock
    ) {
        super(tileEntityType);
        this.worldController = worldController;
        current = initial;
        this.entityClock = entityClock;
    }

    @Override
    public void tick() {
        worldController.set(level);
        long now = entityClock.currentTimeMillis();
        current = current.tick(getBlockPos(), worldController, now - lastTick);
        this.lastTick = now;
    }

}
