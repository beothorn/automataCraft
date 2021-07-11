package br.com.isageek.automata.forge;

import net.minecraft.util.math.BlockPos;

public interface EntityTick {

    EntityTick tick(
            BlockPos center,
            WorldController worldController
    );

    int minimunDuration();

}
