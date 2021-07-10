package br.com.isageek.automata.forge;

import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ClockedTileEntityTest {

    @Test
    public void fastTick(){

        AtomicInteger tickCount = new AtomicInteger(0);

        EntityTick initial = new EntityTick() {
            @Override
            public EntityTick tick(BlockPos center, WorldController worldController) {
                tickCount.incrementAndGet();
                return this;
            }
        };
        EntityClock entityClock = () -> 0;
        ClockedTileEntity clockedTileEntity = new ClockedTileEntity(
                null,
                new WorldController(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                initial,
                entityClock
        );

        clockedTileEntity.tick();
        clockedTileEntity.tick();

        Assert.assertEquals(2, tickCount.get());
    }

    @Test
    public void slowTick(){

        AtomicInteger tickCount = new AtomicInteger(0);
        AtomicInteger lastTick = new AtomicInteger(1);

        EntityTick initial = new EntityTick() {
            @Override
            public EntityTick tick(BlockPos center, WorldController worldController) {
                tickCount.incrementAndGet();
                return this;
            }
        };
        EntityClock entityClock = () -> lastTick.getAndAdd(1000);
        ClockedTileEntity clockedTileEntity = new ClockedTileEntity(
                null,
                new WorldController(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                initial,
                entityClock
        );

        clockedTileEntity.tick();
        clockedTileEntity.tick();
        clockedTileEntity.tick();

        Assert.assertEquals(2, tickCount.get());
    }
}