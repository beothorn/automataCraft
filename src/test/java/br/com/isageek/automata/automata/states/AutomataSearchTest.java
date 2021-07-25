package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static br.com.isageek.automata.forge.BlockStateHolder.block;
import static br.com.isageek.automata.testSupport.FakeWorld.AUTOMATA;
import static br.com.isageek.automata.testSupport.FakeWorld.TERMINATOR;
import static org.junit.Assert.assertEquals;

public class AutomataSearchTest {

    @Test
    public void findOneAutomata(){
        final FakeWorld fakeWorld = new FakeWorld();
        final HashMap<BlockStateHolder, HashSet<BlockPos>> previousReplaceables = new HashMap<>();
        previousReplaceables.put(block(AUTOMATA), new HashSet<>());

        final AutomataSearch automataSearch = new AutomataSearch(previousReplaceables);
        final BlockPos expected = new BlockPos(0, 10, 0);

        fakeWorld.setAt(expected, AUTOMATA);
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        final AutomataSearch tick = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashSet<BlockPos> automata = tick.getReplaceables().get(BlockStateHolder.block(AUTOMATA));
        assertEquals(1, automata.size());

        assertEquals(expected, automata.iterator().next());
    }

    @Test
    public void getPreviousAutomataList(){
        final FakeWorld fakeWorld = new FakeWorld();
        final BlockPos automata1 = new BlockPos(0, 21, 0);
        final BlockPos automata2 = new BlockPos(0, 10, 0);
        fakeWorld.setAt(automata1, AUTOMATA);
        fakeWorld.setAt(automata2, AUTOMATA);
        final HashMap<BlockStateHolder, HashSet<BlockPos>>  initial = new HashMap<>();
        initial.put(BlockStateHolder.block(AUTOMATA), new HashSet<>(Arrays.asList(automata1)));
        final AutomataSearch automataSearch = new AutomataSearch(initial);
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        final AutomataSearch next = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashSet<BlockPos> automata = next.getReplaceables().get(BlockStateHolder.block(AUTOMATA));
        assertEquals(2, automata.size());
        final List<BlockPos> automataPos = new ArrayList<>(automata);

        assertEquals(automata2, automataPos.get(0));
        assertEquals(automata1, automataPos.get(1));
    }

    @Test
    public void findTwoAutomataThenRemoveOne(){
        final FakeWorld fakeWorld = new FakeWorld();
        final HashMap<BlockStateHolder, HashSet<BlockPos>> previousReplaceables = new HashMap<>();
        previousReplaceables.put(block(AUTOMATA), new HashSet<>());
        final AutomataSearch automataSearch = new AutomataSearch(previousReplaceables);
        final BlockPos automata1 = new BlockPos(0, 10, 0);
        final BlockPos automata2 = new BlockPos(0, 11, 0);
        fakeWorld.setAt(automata1, AUTOMATA);
        fakeWorld.setAt(automata2, AUTOMATA);
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        AutomataSearch next = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashSet<BlockPos> automata = next.getReplaceables().get(BlockStateHolder.block(AUTOMATA));
        assertEquals(2, automata.size());
        final List<BlockPos> automataPos = new ArrayList<>(automata);
        assertEquals(automata1, automataPos.get(1));
        assertEquals(automata2, automataPos.get(0));
        fakeWorld.setAt(0, 11, 0, FakeWorld.AIR);
        next = (AutomataSearch) next.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashSet<BlockPos> nextAutomata = next.getReplaceables().get(BlockStateHolder.block(AUTOMATA));
        assertEquals(1, nextAutomata.size());
        assertEquals(automata1, nextAutomata.iterator().next());
    }


    @Test
    public void ifHasRedstoneSignalAndPatternReturnExecutedState(){
        final FakeWorld fakeWorld = new FakeWorld();
        final AutomataSearch automataSearch = new AutomataSearch();
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final String[][][] matcher = TestHelper.cubeWithSameBlockType("a");
        final String[][][] replacement = TestHelper.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(2, 0, 0, replacement);
        fakeWorld.setSurrounding(5, 0, 0, matcher);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);


        final ExecutePattern next = (ExecutePattern)automataSearch.tick(automataStartPos, fakeWorld, 0);
        Assert.assertNotNull(next);
    }

}