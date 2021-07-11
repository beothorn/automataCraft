package br.com.isageek.automata.automata;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static br.com.isageek.automata.testSupport.FakeWorld.TERMINATOR;
import static org.junit.Assert.assertEquals;

public class AutomataSearchTest {

    @Test
    public void findOneAutomata(){
        FakeWorld fakeWorld = new FakeWorld();
        AutomataSearch automataSearch = new AutomataSearch();
        BlockPos expected = new BlockPos(0, 10, 0);

        fakeWorld.setAt(expected, FakeWorld.AUTOMATA);
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        AutomataSearch tick = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld);
        HashSet<BlockPos> automata = tick.getAutomata();
        assertEquals(1, automata.size());

        assertEquals(expected, automata.iterator().next());
    }

    @Test
    public void getPreviousAutomataList(){
        FakeWorld fakeWorld = new FakeWorld();
        BlockPos automata1 = new BlockPos(0, 21, 0);
        BlockPos automata2 = new BlockPos(0, 10, 0);
        fakeWorld.setAt(automata1, FakeWorld.AUTOMATA);
        fakeWorld.setAt(automata2, FakeWorld.AUTOMATA);
        HashSet<BlockPos> initial = new HashSet<>();
        initial.add(automata1);
        AutomataSearch automataSearch = new AutomataSearch(initial);
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        AutomataSearch next = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld);
        HashSet<BlockPos> automata = next.getAutomata();
        assertEquals(2, automata.size());
        List<BlockPos> automataPos = new ArrayList<>(automata);

        assertEquals(automata2, automataPos.get(0));
        assertEquals(automata1, automataPos.get(1));
    }

    @Test
    public void findTwoAutomataThenRemoveOne(){
        FakeWorld fakeWorld = new FakeWorld();
        AutomataSearch automataSearch = new AutomataSearch();
        BlockPos automata1 = new BlockPos(0, 10, 0);
        BlockPos automata2 = new BlockPos(0, 11, 0);
        fakeWorld.setAt(automata1, FakeWorld.AUTOMATA);
        fakeWorld.setAt(automata2, FakeWorld.AUTOMATA);
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        AutomataSearch next = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld);
        HashSet<BlockPos> automata = next.getAutomata();
        assertEquals(2, automata.size());
        List<BlockPos> automataPos = new ArrayList<>(automata);
        assertEquals(automata1, automataPos.get(1));
        assertEquals(automata2, automataPos.get(0));
        fakeWorld.setAt(0, 11, 0, FakeWorld.AIR);
        next = (AutomataSearch) next.tick(automataStartPos, fakeWorld);
        HashSet<BlockPos> nextAutomata = next.getAutomata();
        assertEquals(1, nextAutomata.size());
        assertEquals(automata1, nextAutomata.iterator().next());
    }


    @Test
    public void ifHasRedstoneSignalAndPatternReturnExecutedState(){
        FakeWorld fakeWorld = new FakeWorld();
        AutomataSearch automataSearch = new AutomataSearch();
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        String[][][] matcher = TestHelper.cubeWithSameBlockType("a");
        String[][][] replacement = TestHelper.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(2, 0, 0, replacement);
        fakeWorld.setSurrounding(5, 0, 0, matcher);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);


        ExecutePattern next = (ExecutePattern)automataSearch.tick(automataStartPos, fakeWorld);
        Assert.assertNotNull(next);
    }

}