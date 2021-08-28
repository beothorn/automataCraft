package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ABCRepeatitionPattern {

    @Test
    public void replaceAny(){

        ABCRepeatitionPattern.testForDirection(1, 0);
        ABCRepeatitionPattern.testForDirection(-1, 0);
        ABCRepeatitionPattern.testForDirection(0, 1);
        ABCRepeatitionPattern.testForDirection(0, -1);
    }

    private static void testForDirection(final int directionX, final int directionZ) {
        final String[][][] blockWithA = TestHelper.cubeWithSameBlockType("a");
        final String[][][] blockWithB = TestHelper.cubeWithSameBlockType("b");
        final String[][][] blockWithC = TestHelper.cubeWithSameBlockType("c");

        final FakeWorld fakeWorld = new FakeWorld();

        int cursor = 10;

        fakeWorld.redSignalAt(cursor * directionX, 0, cursor * directionZ, true);
        fakeWorld.setAt(cursor * directionX, 0, cursor * directionZ, FakeWorld.AUTOMATA_START);
        cursor += 2;
        fakeWorld.setSurrounding(cursor * directionX, 0, cursor * directionZ, blockWithA);
        cursor += 3;
        fakeWorld.setSurrounding(cursor * directionX, 0, cursor * directionZ, blockWithB);
        cursor += 2;
        fakeWorld.setAt(cursor * directionX, 0, cursor * directionZ, FakeWorld.TERMINATOR);
        cursor += 2;
        fakeWorld.setSurrounding(cursor * directionX, 0, cursor * directionZ, blockWithB);
        cursor += 3;
        fakeWorld.setSurrounding(cursor * directionX, 0, cursor * directionZ, blockWithC);
        cursor += 2;
        fakeWorld.setAt(cursor * directionX, 0, cursor * directionZ, FakeWorld.TERMINATOR);
        cursor += 2;
        fakeWorld.setSurrounding(cursor * directionX, 0, cursor * directionZ, blockWithC);
        cursor += 3;
        fakeWorld.setSurrounding(cursor * directionX, 0, cursor * directionZ, blockWithA);
        cursor += 2;
        fakeWorld.setAt(cursor * directionX, 0, cursor * directionZ, FakeWorld.TERMINATOR);

        fakeWorld.setSurrounding(0, 0, 0, blockWithA);
        assertArrayEquals(blockWithA, fakeWorld.getSurroundingIds(0, 0, 0));
        fakeWorld.tick();
        assertArrayEquals(blockWithB, fakeWorld.getSurroundingIds(0, 0, 0));
        fakeWorld.tick();
        assertArrayEquals(blockWithC, fakeWorld.getSurroundingIds(0, 0, 0));
        fakeWorld.tick();
        assertArrayEquals(blockWithA, fakeWorld.getSurroundingIds(0, 0, 0));
    }
}
