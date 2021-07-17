package br.com.isageek.automata.forge;

import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class TickableTileEntityStrategyTest {

    @Test
    public void tick(){

        AtomicInteger tickCount = new AtomicInteger(0);

        EntityTick initial = new EntityTick() {
            @Override
            public EntityTick tick(BlockPos center, WorldController worldController, long delta) {
                tickCount.incrementAndGet();
                return this;
            }
        };
        EntityClock entityClock = () -> 1000;
        TickableTileEntityStrategy tickableTileEntityStrategy = new TickableTileEntityStrategy(
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

        tickableTileEntityStrategy.tick();
        tickableTileEntityStrategy.tick();

        Assert.assertEquals(2, tickCount.get());
    }
}