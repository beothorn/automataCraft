package br.com.isageek.automata.automata;

import br.com.isageek.automata.FakeWorld;
import br.com.isageek.automata.patterns.PatternsTest;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.FakeWorld.*;

public class PatternLoadTest {

    @Test
    public void nothingToLoadJustWaits(){
        FakeWorld fakeWorld = new FakeWorld();

        PatternLoad patternLoad = new PatternLoad(null);

        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(0, 0, 0, true);

        PatternLoad next = (PatternLoad) patternLoad.tick(automataStartPos, fakeWorld);
        Assert.assertNotNull(next);
    }

    @Test
    public void turnRedSignalOffGoesBackToSearch(){
        FakeWorld fakeWorld = new FakeWorld();

        PatternLoad patternLoad = new PatternLoad(null);

        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(0, 0, 0, false);

        AutomataSearch next = (AutomataSearch) patternLoad.tick(automataStartPos, fakeWorld);
        Assert.assertNotNull(next);
    }

    @Test
    public void loadsASimplePattern(){
        FakeWorld fakeWorld = new FakeWorld();
        PatternLoad patternLoad = new PatternLoad(null);
        BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(0, 0, 0, true);

        String[][][] matcher = PatternsTest.cubeWithSameBlockType("a");
        String[][][] replacement = PatternsTest.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(2, 0, 0, replacement);
        fakeWorld.setSurrounding(5, 0, 0, matcher);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);
        ExecutePattern next = (ExecutePattern) patternLoad.tick(automataStartPos, fakeWorld);
        Assert.assertNotNull(next);
    }


}