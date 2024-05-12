package br.com.isageek.automata.forge;

import net.minecraft.core.BlockPos;

public interface EntityTick {

    EntityTick tick(
        BlockPos center,
        WorldController worldController,
        long delta
    );

}
