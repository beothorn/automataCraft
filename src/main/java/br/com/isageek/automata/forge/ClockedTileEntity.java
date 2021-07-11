package br.com.isageek.automata.forge;

import br.com.isageek.automata.AutomataMod;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClockedTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger(ClockedTileEntity.class);

    private EntityTick current;
    private EntityClock entityClock;
    private final WorldController worldController;
    private long lastTick = 0;

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
        long timeSinceLastTick = entityClock.currentTimeMillis() - lastTick;
        LOGGER.debug("Delta "+timeSinceLastTick);
        LOGGER.debug("Tick at "+entityClock.currentTimeMillis());
        worldController.set(level);
        long now = entityClock.currentTimeMillis();
        current = current.tick(getBlockPos(), worldController, now - lastTick);
        this.lastTick = now;
    }

}
