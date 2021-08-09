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
    private final BlockPos[][] exclusions;

    ExecutePattern(
            final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables,
            final BlockTree replacementPattern
    ) {
        this.replaceables = replaceables;
        this.replacementPattern = replacementPattern;
        this.exclusions = new BlockPos[][]{};
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

        this.exclusions = new BlockPos[9][2];
        int count = 0;
        if(pos.getX() == terminator.getX()){
            final BlockPos start;
            final BlockPos end;
            if(pos.getZ() < terminator.getZ()){
                start = pos;
                end = terminator;
            }else{
                start = terminator;
                end = pos;
            }

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    this.exclusions[count][0] = start.offset(x, y, 0);
                    this.exclusions[count][1] = end.offset(x, y, 0);
                    count += 1;
                }
            }

        }else{
            final BlockPos start;
            final BlockPos end;
            if(pos.getX() < terminator.getX()){
                start = pos;
                end = terminator;
            }else{
                start = terminator;
                end = pos;
            }

            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    this.exclusions[count][0] = start.offset(0, y, z);
                    this.exclusions[count][1] = end.offset(0, y, z);
                    count += 1;
                }
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
                            final BlockPos currentPos = a.offset(x, y, z);
                            boolean onExcludeArea = false;
                            for (final BlockPos[] exclusion: this.exclusions) {
                                final BlockPos excludeStart = exclusion[0];
                                final BlockPos excludeEnd = exclusion[1];
                                if(
                                        currentPos.getX() >= excludeStart.getX()
                                        && currentPos.getY() >= excludeStart.getY()
                                        && currentPos.getZ() >= excludeStart.getZ()
                                        && currentPos.getX() <= excludeEnd.getX()
                                        && currentPos.getY() <= excludeEnd.getY()
                                        && currentPos.getZ() <= excludeEnd.getZ()
                                ){
                                    onExcludeArea = true;
                                    continue;
                                }
                            }

                            if(onExcludeArea) {
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
