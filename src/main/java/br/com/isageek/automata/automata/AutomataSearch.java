package br.com.isageek.automata.automata;

import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;

public class AutomataSearch implements EntityTick {

    private static final int MAX_SEARCH_RADIUS = 20;

    private HashSet<BlockPos> automata;

    public AutomataSearch(HashSet<BlockPos> automata){
        this.automata = automata;
    }

    public AutomataSearch(){
        this(new HashSet<>());
    }

    @Override
    public EntityTick tick(BlockPos center, WorldController worldController) {

        ArrayList<BlockPos> toBeRemoved = new ArrayList<>();
        for (BlockPos p: automata) {
            if(!worldController.isAutomata(p)){
                toBeRemoved.add(p);
            }
        }

        for (BlockPos p: toBeRemoved) {
            automata.remove(p);
        }

        for (int x = -MAX_SEARCH_RADIUS; x <= MAX_SEARCH_RADIUS; x++) {
            for (int y = -MAX_SEARCH_RADIUS; y <= MAX_SEARCH_RADIUS; y++) {
                for (int z = -MAX_SEARCH_RADIUS; z <= MAX_SEARCH_RADIUS; z++) {
                    BlockPos currentPos = center.offset(x, y, z);
                    if(worldController.isAutomata(currentPos)){
                        automata.add(currentPos);
                    }
                }
            }
        }

        if(worldController.hasNeighborSignal(center)){
            PatternLoad patternLoad = new PatternLoad(automata);
            return patternLoad.tick(center, worldController);
        }

        return this;
    }

    public HashSet<BlockPos> getAutomata() {
        return automata;
    }
}
