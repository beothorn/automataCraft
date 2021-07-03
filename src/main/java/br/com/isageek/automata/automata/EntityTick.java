package br.com.isageek.automata.automata;

import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

public interface EntityTick {

    public EntityTick tick(
            BlockPos center,
            WorldController worldController
    );

}
