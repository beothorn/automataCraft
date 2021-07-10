package br.com.isageek.automata.forge;

import br.com.isageek.automata.AutomataMod;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClockedTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    private EntityTick current;
    private EntityClock entityClock;
    private final WorldController worldController;
    private long lastDuration = 0;
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
        if(lastDuration < 1000){
            doTick();
        }else{
            if(AutomataMod.DEBUG) LOGGER.info("Postponed tick at "+entityClock.currentTimeMillis());
            long timeSinceLastTick = entityClock.currentTimeMillis() - lastTick;
            if(timeSinceLastTick > lastDuration){
                doTick();
            }
        }
    }

    private void doTick() {
        if(AutomataMod.DEBUG) LOGGER.info("Tick at "+entityClock.currentTimeMillis());
        worldController.set(level);
        long before = entityClock.currentTimeMillis();
        current = current.tick(getBlockPos(), worldController);
        long after = entityClock.currentTimeMillis();
        lastTick = entityClock.currentTimeMillis();
        lastDuration = after - before;
    }
}
