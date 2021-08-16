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

    private HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables;
    private final BlockTree replacementPattern;
    private long timeSinceLastTick = 0;
    private boolean firstRun = true;
    private final BlockPos startExclusions;
    private final BlockPos endExclusions;

    ExecutePattern(
            final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables,
            final BlockTree replacementPattern
    ) {
        this.replaceables = replaceables;
        this.replacementPattern = replacementPattern;
        this.startExclusions = null;
        this.endExclusions = null;
    }

    ExecutePattern(
            final WorldController worldController,
            final BlockPos pos,
            final BlockPos terminator,
            final HashMap<BlockStateHolder,
            HashSet<BlockPos>> replaceables,
            final BlockTree replacementPattern
    ) {
        worldController.setStateAt(pos, AutomataStartState.EXECUTE);
        this.replaceables = replaceables;
        this.replacementPattern = replacementPattern;
        if(pos.getX() == terminator.getX()){
            if(pos.getZ() < terminator.getZ()){
                this.startExclusions = pos.offset(-1, -1, -1);
                this.endExclusions = terminator.offset(1, 1, 1);
            }else{
                this.startExclusions = terminator.offset(-1, -1, -1);
                this.endExclusions = pos.offset(1, 1, 1);
            }
        }else {
            if (pos.getX() < terminator.getX()) {
                this.startExclusions = pos.offset(-1, -1, -1);
                this.endExclusions = terminator.offset(1, 1, 1);
            } else {
                this.startExclusions = terminator.offset(-1, -1, -1);
                this.endExclusions = pos.offset(1, 1, 1);
            }
        }
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

        final boolean shouldWaitSoItDoesntRunTooFast = this.timeSinceLastTick < minimalTickInterval;
        final boolean slowDownToGiveTimeToProcessBlocks = allBlocksToReplaceCount > throttleAfterAutomataCount && this.timeSinceLastTick < (allBlocksToReplaceCount / 2.5);
        if( (shouldWaitSoItDoesntRunTooFast || slowDownToGiveTimeToProcessBlocks) && !this.firstRun) {
            return this;
        }

        final boolean startBlockIsNotReceivingRedSignal = !worldController.hasNeighborSignal(center);
        if(startBlockIsNotReceivingRedSignal){
            final LoadReplaceables loadReplaceables = new LoadReplaceables(worldController, center, this.replaceables);
            return loadReplaceables.tick(center, worldController, delta);
        }

        final Set<Map.Entry<BlockStateHolder, HashSet<BlockPos>>> positionsForBlockSateHolder = this.replaceables.entrySet();
        final Set<BlockStateHolder> blockStateHolders = this.replaceables.keySet();

        final HashMap<BlockStateHolder, HashSet<BlockPos>> newReplaceables = new HashMap<>();
        for (final BlockStateHolder blockStateHolder: blockStateHolders) {
            newReplaceables.put(blockStateHolder, new HashSet<>());
        }


        final HashMap<BlockPos, BlockStateHolder> replacements = new LinkedHashMap<>();

        for (final Map.Entry<BlockStateHolder, HashSet<BlockPos>> entry : positionsForBlockSateHolder) {
            final HashSet<BlockPos> blockPositions = entry.getValue();

            for (final BlockPos blockBeingEvaluated : blockPositions) {
                if(
                        this.startExclusions != null
                        && this.endExclusions != null
                        && blockBeingEvaluated.getX() >= this.startExclusions.getX()
                        && blockBeingEvaluated.getY() >= this.startExclusions.getY()
                        && blockBeingEvaluated.getZ() >= this.startExclusions.getZ()
                        && blockBeingEvaluated.getX() <= this.endExclusions.getX()
                        && blockBeingEvaluated.getY() <= this.endExclusions.getY()
                        && blockBeingEvaluated.getZ() <= this.endExclusions.getZ()
                ){
                    continue;
                }

                final BlockStateHolder[] toBeReplaced = worldController.surrounding(blockBeingEvaluated);
                final BlockStateHolder[] replacement = this.replacementPattern.getReplacementFor(toBeReplaced);

                if (replacement == null) {
                    continue;
                }

                int i = 0;
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            final BlockPos currentPos = blockBeingEvaluated.offset(x, y, z);
                            if(
                                this.startExclusions != null
                                && this.endExclusions != null
                                && currentPos.getX() >= this.startExclusions.getX()
                                && currentPos.getY() >= this.startExclusions.getY()
                                && currentPos.getZ() >= this.startExclusions.getZ()
                                && currentPos.getX() <= this.endExclusions.getX()
                                && currentPos.getY() <= this.endExclusions.getY()
                                && currentPos.getZ() <= this.endExclusions.getZ()
                            ){
                                continue;
                            }

                            final BlockStateHolder blockStateHolder = replacement[i++];
                            final boolean isNotBedrock = !worldController.isBedrock(currentPos);
                            final boolean isNotMatchAll = !blockStateHolder.descriptionId.equals(BlockTree.ANY.descriptionId);
                            final boolean shouldBeReplaced = isNotBedrock && isNotMatchAll;
                            if (shouldBeReplaced) {
                                replacements.put(currentPos, blockStateHolder);
                            }
                            if(newReplaceables.containsKey(blockStateHolder)){
                                newReplaceables.get(blockStateHolder).add(currentPos);
                            }
                        }
                    }
                }
            }
        }

        final Set<Map.Entry<BlockPos, BlockStateHolder>> replacementEntries = replacements.entrySet();
        for (final Map.Entry<BlockPos, BlockStateHolder> replacementEntry : replacementEntries) {
            final BlockPos key = replacementEntry.getKey();
            final BlockStateHolder value = replacementEntry.getValue();
            if(blockStateHolders.contains(value)){
                final HashSet<BlockPos> allBlockPos = this.replaceables.get(value);
                allBlockPos.add(key);
            }
            worldController.setBlock(value, key);
        }

        this.replaceables = newReplaceables;

        this.timeSinceLastTick = 0;
        this.firstRun = false;
        return this;
    }

    HashMap<BlockStateHolder, HashSet<BlockPos>> getReplaceables() {
        return this.replaceables;
    }
}
