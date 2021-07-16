package br.com.isageek.automata.forge;

import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ClockedTileEntityTest {

    @Test
    public void tick(){

        AtomicInteger tickCount = new AtomicInteger(0);
        AtomicInteger delta = new AtomicInteger(0);

        EntityTick initial = new EntityTick() {
            @Override
            public EntityTick tick(BlockPos center, WorldController worldController, long delta) {
                tickCount.incrementAndGet();
                return this;
            }
        };
        EntityClock entityClock = () -> 1000;
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
                        null
                ),
                initial,
                entityClock
        );

        clockedTileEntity.tick();
        clockedTileEntity.tick();

        Assert.assertEquals(2, tickCount.get());
    }
}