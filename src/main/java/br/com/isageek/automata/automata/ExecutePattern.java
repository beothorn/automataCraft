package br.com.isageek.automata.automata;

import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class ExecutePattern implements EntityTick{

    private HashSet<BlockPos> automataPositions;
    private BlockTree replacementPattern;

    public ExecutePattern(HashSet<BlockPos> automataPositions, BlockTree replacementPattern) {
        this.automataPositions = automataPositions;
        this.replacementPattern = replacementPattern;
    }

    @Override
    public EntityTick tick(BlockPos center, WorldController worldController) {
        return null;
    }
}
