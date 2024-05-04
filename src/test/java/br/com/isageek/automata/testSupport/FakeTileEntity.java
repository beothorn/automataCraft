package br.com.isageek.automata.testSupport;

import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.core.BlockPos;

public class FakeTileEntity{

    private EntityTick current;

    public FakeTileEntity(EntityTick current) {
        this.current = current;
    }

    public void tick(BlockPos pos, WorldController world){
        current = current.tick(pos, world, Integer.MAX_VALUE);
    }
}
