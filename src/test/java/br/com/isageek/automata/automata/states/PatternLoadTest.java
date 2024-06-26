package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import net.minecraft.core.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.TERMINATOR;

public class PatternLoadTest {

    @Test
    public void nothingToLoadGoesBackToSearch(){
        final FakeWorld fakeWorld = new FakeWorld();

        final PatternLoad patternLoad = new PatternLoad();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final AutomataSearch next = (AutomataSearch) patternLoad.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void turnRedSignalOffGoesBackToSearch(){
        final FakeWorld fakeWorld = new FakeWorld();

        final PatternLoad patternLoad = new PatternLoad();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, false);

        final AutomataSearch next = (AutomataSearch) patternLoad.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void loadsASimplePatternXPlus(){
        final FakeWorld fakeWorld = new FakeWorld();
        final PatternLoad patternLoad = new PatternLoad();
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final String[][][] matcher = TestHelper.cubeWithSameBlockType("a");
        final String[][][] replacement = TestHelper.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(2, 0, 0, replacement);
        fakeWorld.setSurrounding(5, 0, 0, matcher);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);
        final ExecutePattern next = (ExecutePattern) patternLoad.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void loadsASimplePatternXMinus(){
        final FakeWorld fakeWorld = new FakeWorld();
        final PatternLoad patternLoad = new PatternLoad();
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final String[][][] matcher = TestHelper.cubeWithSameBlockType("a");
        final String[][][] replacement = TestHelper.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(-2, 0, 0, replacement);
        fakeWorld.setSurrounding(-5, 0, 0, matcher);
        fakeWorld.setAt(-7, 0, 0, TERMINATOR);
        final ExecutePattern next = (ExecutePattern) patternLoad.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void loadsASimplePatternZPlus(){
        final FakeWorld fakeWorld = new FakeWorld();
        final PatternLoad patternLoad = new PatternLoad();
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final String[][][] matcher = TestHelper.cubeWithSameBlockType("a");
        final String[][][] replacement = TestHelper.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(0, 0, 2, replacement);
        fakeWorld.setSurrounding(0, 0, 5, matcher);
        fakeWorld.setAt(0, 0, 7, TERMINATOR);
        final ExecutePattern next = (ExecutePattern) patternLoad.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void loadsASimplePatternZMinus(){
        final FakeWorld fakeWorld = new FakeWorld();
        final PatternLoad patternLoad = new PatternLoad();
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final String[][][] matcher = TestHelper.cubeWithSameBlockType("a");
        final String[][][] replacement = TestHelper.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(0, 0, -2, replacement);
        fakeWorld.setSurrounding(0, 0, -5, matcher);
        fakeWorld.setAt(0, 0, -7, TERMINATOR);
        final ExecutePattern next = (ExecutePattern) patternLoad.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void loadsAPatternWithTwoEntries(){
        final FakeWorld fakeWorld = new FakeWorld();
        final PatternLoad patternLoad = new PatternLoad();
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final String[][][] matcher = TestHelper.cubeWithSameBlockType("a");
        final String[][][] replacement = TestHelper.cubeWithSameBlockType("b");

        fakeWorld.setSurrounding(2, 0, 0, replacement);
        fakeWorld.setSurrounding(5, 0, 0, matcher);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);
        fakeWorld.setAt(14, 0, 0, TERMINATOR);
        final ExecutePattern next = (ExecutePattern) patternLoad.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

//
//    @Test
//    public void usesYRotationBlock(){
//        AutomataStepper automataStepper = PatternsTest.vanillaStepper();
//
//        String[][][] match = TestHelper.cubeWithSameBlockType(AIR);
//        match[0][1][1] = "SomeBlock";
//        match[1][1][1] = AUTOMATA_Y_ROTATION;
//
//        String[][][] result = TestHelper.cubeWithSameBlockType("Matched");
//
//        FakeWorld fakeWorld = new FakeWorld(automataStepper);
//        fakeWorld.setAt(0, 0, 0, AUTOMATA);
//
//        fakeWorld.setAt(10, 0, 0, AUTOMATA_START);
//        fakeWorld.redSignalAt(10, 0, 0, true);
//        fakeWorld.setSurrounding(12, 0, 0, result);
//        fakeWorld.setSurrounding(15, 0, 0, match);
//
//        fakeWorld.setAt(17, 0, 0, TERMINATOR);
//
//        fakeWorld.tick();
//        Assert.assertTrue(automataStepper.isLoaded());
//
//        fakeWorld.setAt(-1, 0, 0, "SomeBlock");
//        fakeWorld.doubleTick();
//        Assert.assertArrayEquals(TestHelper.cubeWithSameBlockType("Matched"), fakeWorld.getSurroundingIds(0, 0, 0));
//
//        fakeWorld.setSurrounding(0, 0, 0, TestHelper.cubeWithSameBlockType(AIR));
//        fakeWorld.setAt(0, 0, 0, AUTOMATA);
//
//        fakeWorld.setAt(1, 0, 0, "SomeBlock");
//        fakeWorld.doubleTick();
//        Assert.assertArrayEquals(TestHelper.cubeWithSameBlockType("Matched"), fakeWorld.getSurroundingIds(0, 0, 0));
//    }
}