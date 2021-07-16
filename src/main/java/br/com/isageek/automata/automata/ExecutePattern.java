package br.com.isageek.automata.automata;

import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ExecutePattern implements EntityTick {

    private HashSet<BlockPos> automataPositions;
    private BlockTree replacementPattern;
    private long timeSinceLastTick = 0;
    private static final Logger LOGGER = LogManager.getLogger(ExecutePattern.class);

    public ExecutePattern(
            HashSet<BlockPos> automataPositions,
            BlockTree replacementPattern
    ) {
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
        int minimalTickInterval = 500;
        int throttleAfterAutomataCount = 8000;
        if(
                (timeSinceLastTick < minimalTickInterval)
                || (automataPositions.size() > throttleAfterAutomataCount && timeSinceLastTick < (automataPositions.size()/2.5))) {
            LOGGER.debug("Skipped size "+automataPositions.size()+" delta: "+ timeSinceLastTick);
            return this;
        }
        LOGGER.debug("automataPositions.size() "+automataPositions.size());
        if(!worldController.hasNeighborSignal(center)){
            AutomataSearch automataSearch = new AutomataSearch(automataPositions);
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
