package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.AutomataMod;
import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class ExecutePattern implements EntityTick {

    private final HashSet<BlockPos> automataPositions;
    private final BlockTree replacementPattern;
    private long timeSinceLastTick = 0;

    public ExecutePattern(
            HashSet<BlockPos> automataPositions,
            BlockTree replacementPattern
    ) {
        this.automataPositions = automataPositions;
        this.replacementPattern = replacementPattern;
    }

    public ExecutePattern(
            WorldController worldController,
            BlockPos pos,
            HashSet<BlockPos> automataPositions,
            BlockTree replacementPattern
    ) {
        worldController.setStateAt(pos, AutomataStartState.EXECUTE);
        this.automataPositions = automataPositions;
        this.replacementPattern = replacementPattern;
    }

    @Override
    public EntityTick tick(
            BlockPos center,
            WorldController worldController,
            long delta
    ) {
        timeSinceLastTick += delta;
        int minimalTickInterval = AutomataMod.EXECUTE_MINIMAL_TICK_INTERVAL;
        int throttleAfterAutomataCount = AutomataMod.EXECUTE_THROTTLE_AFTER_AUTOMATA_COUNT;
         if(
                (timeSinceLastTick < minimalTickInterval)
                || (automataPositions.size() > throttleAfterAutomataCount && timeSinceLastTick < (automataPositions.size()/2.5))) {
            return this;
        }
        if(!worldController.hasNeighborSignal(center)){
            AutomataSearch automataSearch = new AutomataSearch(worldController, center, automataPositions);
            return automataSearch.tick(center, worldController, delta);
        }

        HashMap<BlockPos, BlockStateHolder> replacements = new LinkedHashMap<>();
        BlockStateHolder automata = worldController.getAutomata();

        HashSet<BlockPos> toBeRemoved = new HashSet<>();
        for (BlockPos a: automataPositions) {
            BlockStateHolder[] toBeReplaced = worldController.surrounding(a);
            BlockStateHolder[] replacement = replacementPattern.getReplacementFor(toBeReplaced);

            if(replacement == null) continue;

            int i = 0;
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockStateHolder blockStateHolder = replacement[i++];
                        BlockPos currentPos = a.offset(x, y, z);
                        boolean isNotBedrock = !worldController.isBedrock(currentPos);
                        boolean isNotMatchAll = !blockStateHolder.descriptionId.equals(BlockTree.ANY.descriptionId);
                        boolean shouldBeReplaced = isNotBedrock && isNotMatchAll;
                        if(shouldBeReplaced){
                            if(!replacements.containsKey(currentPos)){
                                if(worldController.isAutomata(currentPos)){
                                    toBeRemoved.add(currentPos);
                                }
                                replacements.put(currentPos, blockStateHolder);
                            }
                        }
                    }
                }
            }
        }

        for (BlockPos p: toBeRemoved) {
            automataPositions.remove(p);
        }

        Set<Map.Entry<BlockPos, BlockStateHolder>> replacementEntries = replacements.entrySet();
        for (Map.Entry<BlockPos, BlockStateHolder> entry : replacementEntries) {
            BlockPos key = entry.getKey();
            BlockStateHolder value = entry.getValue();
            if(value.equals(automata)){
                automataPositions.add(key);
            }
            worldController.setBlock(value, key);
        }

        timeSinceLastTick = 0;
        return this;
    }
}
