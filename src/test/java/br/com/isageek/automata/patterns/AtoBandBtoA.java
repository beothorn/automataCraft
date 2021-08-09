package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

public class AtoBandBtoA {

    @Test
    public void replaceAny(){
        final String[][][] blockWithA = TestHelper.cubeWithSameBlockType("a");
        final String[][][] blockWithB = TestHelper.cubeWithSameBlockType("b");

        final FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, blockWithA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, blockWithB);
        fakeWorld.setSurrounding(15, 0, 0, blockWithA);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);
        fakeWorld.setSurrounding(19, 0, 0, blockWithA);
        fakeWorld.setSurrounding(22, 0, 0, blockWithB);
        fakeWorld.setAt(24, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();

        final String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(blockWithB, actual);

        fakeWorld.tick();

        final String[][][] actual2 = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(blockWithA, actual2);
    }
}
