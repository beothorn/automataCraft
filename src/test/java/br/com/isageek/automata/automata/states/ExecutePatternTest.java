package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.testSupport.FakeWorld;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static br.com.isageek.automata.testSupport.TestHelper.blocksOf;
import static br.com.isageek.automata.testSupport.TestHelper.cubeWithSameBlockType;

public class ExecutePatternTest {

    @Test
    public void simplePattern(){
        final FakeWorld fakeWorld = new FakeWorld();

        final BlockPos startBlockPosition = new BlockPos(-10, 0, 0);
        fakeWorld.setAt(startBlockPosition, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(startBlockPosition, true);

        final BlockStateHolder[] match1 = blocksOf("a", FakeWorld.AUTOMATA);
        final BlockStateHolder[] result1 = blocksOf("b");

        final BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        final BlockPos automatonPos = new BlockPos(0, 0, 0);
        fakeWorld.setSurrounding(automatonPos, cubeWithSameBlockType("a"));
        final HashMap<BlockStateHolder, HashSet<BlockPos>> automataPos = new HashMap<>();
        automataPos.put(BlockStateHolder.block(FakeWorld.AUTOMATA), new HashSet<>(Arrays.asList(automatonPos)));
        fakeWorld.setAt(automatonPos, FakeWorld.AUTOMATA);

        final ExecutePattern executePattern = new ExecutePattern(automataPos, patterns);
        executePattern.tick(startBlockPosition, fakeWorld, Integer.MAX_VALUE);

        final String[][][] actual = fakeWorld.getSurroundingIds(automatonPos);
        Assert.assertArrayEquals(cubeWithSameBlockType("b"), actual);
    }

    @Test
    public void turnRedSignalOffGoesBackToSearchWithoutLosingAutomataCreated(){
        final FakeWorld fakeWorld = new FakeWorld();

        final BlockPos startBlockPosition = new BlockPos(-30, 0, 0);
        fakeWorld.setAt(startBlockPosition, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(startBlockPosition, true);

        final BlockStateHolder[] match1 = blocksOf("a", FakeWorld.AUTOMATA);
        final BlockStateHolder[] result1 = blocksOf(FakeWorld.AUTOMATA);

        final BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        final BlockPos automatonPos = new BlockPos(0, 0, 0);
        fakeWorld.setSurrounding(automatonPos, cubeWithSameBlockType("a"));
        final HashMap<BlockStateHolder, HashSet<BlockPos>> automataPos = new HashMap<>();
        automataPos.put(BlockStateHolder.block(FakeWorld.AUTOMATA), new HashSet<>(Arrays.asList(automatonPos)));
        fakeWorld.setAt(automatonPos, FakeWorld.AUTOMATA);

        final ExecutePattern executePattern = new ExecutePattern(automataPos, patterns);
        final EntityTick next = executePattern.tick(startBlockPosition, fakeWorld, Integer.MAX_VALUE);

        final String[][][] actual = fakeWorld.getSurroundingIds(automatonPos);
        Assert.assertArrayEquals(cubeWithSameBlockType(FakeWorld.AUTOMATA), actual);

        fakeWorld.redSignalAt(startBlockPosition, false);

        final LoadReplaceables loadReplaceables = (LoadReplaceables) next.tick(startBlockPosition, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(loadReplaceables);

        final HashSet<BlockPos> automata = loadReplaceables.getReplaceables().get(BlockStateHolder.block(FakeWorld.AUTOMATA));
        Assert.assertEquals(27, automata.size());

    }

}