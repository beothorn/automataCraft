package br.com.isageek.automata.testSupport;

import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class FakeTileEntity extends TileEntity {

    private EntityTick current;

    public FakeTileEntity(EntityTick current) {
        super(null);
        this.current = current;
    }

    public void tick(BlockPos pos, WorldController world){
        current = current.tick(pos, world, Integer.MAX_VALUE);
    }
}
