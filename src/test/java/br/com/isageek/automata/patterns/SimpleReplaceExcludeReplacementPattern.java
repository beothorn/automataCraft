package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

public class SimpleReplaceExcludeReplacementPattern {

    @Test
    public void simpleReplaceExcludeReplacementPattern(){
        final String[][][] match = TestHelper.cubeWithSameBlockType("a");
        final String[][][] result = TestHelper.cubeWithSameBlockType("b");
        final String[][][] expected = TestHelper.cubeWithSameBlockType("b");

        final FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, TestHelper.cubeWithSameBlockType("a"));
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();

        Assert.assertArrayEquals(expected, fakeWorld.getSurroundingIds(0, 0, 0));

        Assert.assertArrayEquals(result, fakeWorld.getSurroundingIds(12, 0, 0));
        Assert.assertArrayEquals(match, fakeWorld.getSurroundingIds(15, 0, 0));
    }

}
