package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.AutomataMod;
import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;

public class AutomataSearch implements EntityTick {

    private final HashSet<BlockPos> automata;

    public AutomataSearch(HashSet<BlockPos> automata){
        this.automata = automata;
    }
    public AutomataSearch(WorldController worldController, BlockPos pos, HashSet<BlockPos> automata){
        worldController.setStateAt(pos, AutomataStartState.SEARCH);
        this.automata = automata;
    }

    public AutomataSearch(){
        this.automata = new HashSet<>();
    }

    @Override
    public EntityTick tick(BlockPos center, WorldController worldController, long delta) {

        if(!worldController.hasNeighborSignal(center)) return this;

        ArrayList<BlockPos> toBeRemoved = new ArrayList<>();
        for (BlockPos p: automata) {
            if(!worldController.isAutomata(p)){
                toBeRemoved.add(p);
            }
        }

        for (BlockPos p: toBeRemoved) {
            automata.remove(p);
        }

        for (int x = -AutomataMod.MAX_SEARCH_RADIUS; x <= AutomataMod.MAX_SEARCH_RADIUS; x++) {
            for (int y = -AutomataMod.MAX_SEARCH_RADIUS; y <= AutomataMod.MAX_SEARCH_RADIUS; y++) {
                for (int z = -AutomataMod.MAX_SEARCH_RADIUS; z <= AutomataMod.MAX_SEARCH_RADIUS; z++) {

                    BlockPos currentPos = center.offset(x, y, z);
                    if(worldController.isAutomata(currentPos)){
                        automata.add(currentPos);
                    }
                }
            }
        }

        PatternLoad patternLoad = new PatternLoad(worldController, center, automata);
        return patternLoad.tick(center, worldController, delta);
    }

    public HashSet<BlockPos> getAutomata() {
        return automata;
    }
}
