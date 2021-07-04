package br.com.isageek.automata.forge;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ClockedTileEntity extends TileEntity implements ITickableTileEntity {

    private EntityTick current;
    private EntityClock entityClock;
    private final WorldController worldController;
    private long previous = 0;
    private long lastDuration = 0;

    public ClockedTileEntity(
            TileEntityType<ClockedTileEntity> tileEntityType,
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
        long now = entityClock.currentTimeMillis();
        long timeSinceLast = now - previous;
        // wait the same time it took to calculate
        // ie: if it took 1 second, wait 1 second to tick again
        if(timeSinceLast < lastDuration) return;

        worldController.set(level);
        current = current.tick(getBlockPos(), worldController);
        previous = entityClock.currentTimeMillis();
        lastDuration = previous - now;
    }
}
