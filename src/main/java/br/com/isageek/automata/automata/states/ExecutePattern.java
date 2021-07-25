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

    private final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables;
    private final BlockTree replacementPattern;
    private long timeSinceLastTick = 0;

    ExecutePattern(
            final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables,
            final BlockTree replacementPattern
    ) {
        this.replaceables = replaceables;
        this.replacementPattern = replacementPattern;
    }

    ExecutePattern(
            final WorldController worldController,
            final BlockPos pos,
            final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables,
            final BlockTree replacementPattern
    ) {
        worldController.setStateAt(pos, AutomataStartState.EXECUTE);
        this.replaceables = replaceables;
        this.replacementPattern = replacementPattern;
    }

    @Override
    public EntityTick tick(
            final BlockPos center,
            final WorldController worldController,
            final long delta
    ) {
        this.timeSinceLastTick += delta;
        final int minimalTickInterval = AutomataMod.EXECUTE_MINIMAL_TICK_INTERVAL;
        final int throttleAfterAutomataCount = AutomataMod.EXECUTE_THROTTLE_AFTER_AUTOMATA_COUNT;

        int allBlocksToReplaceCount = 0;

        final Collection<HashSet<BlockPos>> allPos = this.replaceables.values();
        for (final HashSet<BlockPos> posSet : allPos) {
            allBlocksToReplaceCount += posSet.size();
        }

        if(
                (this.timeSinceLastTick < minimalTickInterval)
                || (allBlocksToReplaceCount > throttleAfterAutomataCount && this.timeSinceLastTick < (allBlocksToReplaceCount/2.5))) {
            return this;
        }

        if(!worldController.hasNeighborSignal(center)){
            final LoadReplaceables loadReplaceables = new LoadReplaceables(this.replaceables);
            return loadReplaceables.tick(center, worldController, delta);
        }

        final Set<Map.Entry<BlockStateHolder, HashSet<BlockPos>>> entries = this.replaceables.entrySet();

        for (final Map.Entry<BlockStateHolder, HashSet<BlockPos>> entry : entries) {
            final HashSet<BlockPos> blockPositions = entry.getValue();

            final HashMap<BlockPos, BlockStateHolder> replacements = new LinkedHashMap<>();

            for (final BlockPos a : blockPositions) {
                final BlockStateHolder[] toBeReplaced = worldController.surrounding(a);
                final BlockStateHolder[] replacement = this.replacementPattern.getReplacementFor(toBeReplaced);

                if (replacement == null) {
                    continue;
                }

                int i = 0;
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            final BlockStateHolder blockStateHolder = replacement[i++];
                            final BlockPos currentPos = a.offset(x, y, z);
                            final boolean isNotBedrock = !worldController.isBedrock(currentPos);
                            final boolean isNotMatchAll = !blockStateHolder.descriptionId.equals(BlockTree.ANY.descriptionId);
                            final boolean shouldBeReplaced = isNotBedrock && isNotMatchAll;
                            if (shouldBeReplaced) {
                                if (!replacements.containsKey(currentPos)) {
                                    replacements.put(currentPos, blockStateHolder);
                                }
                            }
                        }
                    }
                }
            }
            final Set<Map.Entry<BlockPos, BlockStateHolder>> replacementEntries = replacements.entrySet();
            final Set<BlockStateHolder> replaceableBlocks = this.replaceables.keySet();
            for (final Map.Entry<BlockPos, BlockStateHolder> replacementEntry : replacementEntries) {
                final BlockPos key = replacementEntry.getKey();
                final BlockStateHolder value = replacementEntry.getValue();
                if(replaceableBlocks.contains(value)){
                    final HashSet<BlockPos> allBlockPos = this.replaceables.get(value);
                    allBlockPos.add(key);
                }
                worldController.setBlock(value, key);
            }
        }

        this.timeSinceLastTick = 0;
        return this;
    }

    HashMap<BlockStateHolder, HashSet<BlockPos>> getReplaceables() {
        return this.replaceables;
    }
}
