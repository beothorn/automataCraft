package br.com.isageek.automata.automata;

import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.testSupport.FakeWorld;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

import static br.com.isageek.automata.testSupport.TestHelper.blocksOf;
import static br.com.isageek.automata.testSupport.TestHelper.cubeWithSameBlockType;

public class ExecutePatternTest {

    @Test
    public void simplePattern(){
        FakeWorld fakeWorld = new FakeWorld();

        BlockPos startBlockPosition = new BlockPos(-10, 0, 0);
        fakeWorld.setAt(startBlockPosition, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(startBlockPosition, true);

        BlockStateHolder[] match1 = blocksOf("a");
        BlockStateHolder[] result1 = blocksOf("b");

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockPos automatonPos = new BlockPos(0, 0, 0);
        fakeWorld.setSurrounding(automatonPos, cubeWithSameBlockType("a"));
        HashSet<BlockPos> automataPos = new HashSet<>();
        automataPos.add(automatonPos);
        fakeWorld.setAt(automatonPos, FakeWorld.AUTOMATA);

        ExecutePattern executePattern = new ExecutePattern(automataPos, patterns);
        executePattern.tick(startBlockPosition, fakeWorld, Integer.MAX_VALUE);

        String[][][] actual = fakeWorld.getSurroundingIds(automatonPos);
        Assert.assertArrayEquals(cubeWithSameBlockType("b"), actual);
    }

    @Test
    public void turnRedSignalOffGoesBackToSearchWithoutLosingAutomataCreated(){
        FakeWorld fakeWorld = new FakeWorld();

        BlockPos startBlockPosition = new BlockPos(-30, 0, 0);
        fakeWorld.setAt(startBlockPosition, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(startBlockPosition, true);

        BlockStateHolder[] match1 = blocksOf("a");
        BlockStateHolder[] result1 = blocksOf(FakeWorld.AUTOMATA);

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockPos automatonPos = new BlockPos(0, 0, 0);
        fakeWorld.setSurrounding(automatonPos, cubeWithSameBlockType("a"));
        HashSet<BlockPos> automataPos = new HashSet<>();
        automataPos.add(automatonPos);
        fakeWorld.setAt(automatonPos, FakeWorld.AUTOMATA);

        ExecutePattern executePattern = new ExecutePattern(automataPos, patterns);
        EntityTick next = executePattern.tick(startBlockPosition, fakeWorld, Integer.MAX_VALUE);

        String[][][] actual = fakeWorld.getSurroundingIds(automatonPos);
        Assert.assertArrayEquals(cubeWithSameBlockType(FakeWorld.AUTOMATA), actual);

        fakeWorld.redSignalAt(startBlockPosition, false);

        AutomataSearch nextAutomataSearch = (AutomataSearch) next.tick(startBlockPosition, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(nextAutomataSearch);

        HashSet<BlockPos> automata = nextAutomataSearch.getAutomata();
        Assert.assertEquals(27, automata.size());

    }

}