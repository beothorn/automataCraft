package br.com.isageek.automata.automata;

import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class ExecutePattern implements EntityTick {

    private HashSet<BlockPos> automataPositions;
    private BlockTree replacementPattern;

    public ExecutePattern(
            HashSet<BlockPos> automataPositions,
            BlockTree replacementPattern
    ) {
        this.automataPositions = automataPositions;
        this.replacementPattern = replacementPattern;
    }

    @Override
    public EntityTick tick(BlockPos center, WorldController worldController) {
        if(!worldController.hasNeighborSignal(center)){
            AutomataSearch automataSearch = new AutomataSearch(automataPositions);
            return automataSearch.tick(center, worldController);
        }

        HashMap<BlockPos, BlockStateHolder> replacements = new LinkedHashMap<>();
        BlockStateHolder automata = worldController.getAutomata();

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
                            BlockStateHolder blockState = blockStateHolder;
                            if(worldController.isAutomataPlaceholder(blockStateHolder)) blockState = automata;
                            if(worldController.isAirPlaceholder(blockStateHolder)    )  blockState = worldController.getAir();
                            if(worldController.isWaterPlaceholder(blockStateHolder)  )  blockState = worldController.getWater();
                            if(worldController.isLavaPlaceholder(blockStateHolder)   )  blockState = worldController.getLava();
                            if(worldController.isBedrockPlaceholder(blockStateHolder))  blockState = worldController.getObsidian();
                            if(!replacements.containsKey(currentPos)){
                                replacements.put(currentPos, blockState);
                            }
                        }
                    }
                }
            }
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

        return this;
    }

    @Override
    public int minimunDuration() {
        return automataPositions.size() ;
    }
}
