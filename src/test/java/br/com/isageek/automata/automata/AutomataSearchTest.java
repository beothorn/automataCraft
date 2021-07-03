package br.com.isageek.automata.automata;

import br.com.isageek.automata.FakeWorld;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class AutomataSearchTest {

    @Test
    public void findOneAutomata(){
        FakeWorld fakeWorld = new FakeWorld();
        AutomataSearch automataSearch = new AutomataSearch();
        fakeWorld.setAt(0, 10, 0, FakeWorld.AUTOMATA);
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        AutomataSearch tick = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld);
        HashSet<BlockPos> automata = tick.getAutomata();
        assertEquals(1, automata.size());
        BlockPos expected = new BlockPos(0, 10, 0);
        assertEquals(expected, automata.iterator().next());
    }

    @Test
    public void findTwoAutomataThenRemoveOne(){
        FakeWorld fakeWorld = new FakeWorld();
        AutomataSearch automataSearch = new AutomataSearch();
        fakeWorld.setAt(0, 10, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(0, 11, 0, FakeWorld.AUTOMATA);
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        AutomataSearch next = (AutomataSearch) automataSearch.tick(automataStartPos, fakeWorld);
        HashSet<BlockPos> automata = next.getAutomata();
        assertEquals(2, automata.size());
        List<BlockPos> automataPos = automata.stream().collect(Collectors.toList());
        assertEquals(new BlockPos(0, 10, 0), automataPos.get(1));
        assertEquals(new BlockPos(0, 11, 0), automataPos.get(0));
        fakeWorld.setAt(0, 11, 0, FakeWorld.AIR);
        next = (AutomataSearch) next.tick(automataStartPos, fakeWorld);
        HashSet<BlockPos> nextAutomata = next.getAutomata();
        assertEquals(1, nextAutomata.size());
        BlockPos expected = new BlockPos(0, 10, 0);
        assertEquals(expected, nextAutomata.iterator().next());
    }

    @Test
    public void ifHasRedstoneSignalButNoPatternReturnPatternSearchState(){
        FakeWorld fakeWorld = new FakeWorld();
        AutomataSearch automataSearch = new AutomataSearch();
        fakeWorld.redSignalAt(0, 0, 0, true);
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        PatternLoad next = (PatternLoad) automataSearch.tick(automataStartPos, fakeWorld);
    }

}