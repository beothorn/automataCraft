package br.com.isageek.automata.forge;

import net.minecraft.util.math.BlockPos;

public interface EntityTick {

    public EntityTick tick(
            BlockPos center,
            WorldController worldController
    );

}
