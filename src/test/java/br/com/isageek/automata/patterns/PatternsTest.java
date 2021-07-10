package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.*;
import static br.com.isageek.automata.forge.BlockStateHolder.block;

public class PatternsTest {

    public static void testPattern(
        String[][][] state,
        String[][][] match,
        String[][][] result,
        String[][][] expected
    ) {
        FakeWorld fakeWorld = new FakeWorld();
        fakeWorld.setSurrounding(0, 0, 0, state);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);
        fakeWorld.tick(); // One tick to load

        fakeWorld.tick(); // One tick to read world state
        fakeWorld.tick(); // One tick to replace blocks
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void createBlockWhenAnotherBlockIsAdded(){
        /*
         * Important when you have a sticky piston move a block
         * near the automata and have a pattern that can be turned
         * on and off
         */
        String[][][] state = TestHelper.cubeWithSameBlockType(AIR);

        String[][][] match1 = TestHelper.cubeWithSameBlockType(ANY);
        match1[0][0][1] = "keyBlock";

        String[][][] result1 = TestHelper.cubeWithSameBlockType(AIR);
        result1[1][0][0] = "replacement";
        result1[1][1][1] = AUTOMATA_PLACEHOLDER;

        String[][][] match2 = TestHelper.cubeWithSameBlockType(ANY);

        String[][][] result2 = TestHelper.cubeWithSameBlockType(AIR);
        result2[1][0][0] = AIR_PLACEHOLDER;
        result2[1][1][1] = AUTOMATA_PLACEHOLDER;

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result1);
        fakeWorld.setSurrounding(15, 0, 0, match1);
        fakeWorld.setSurrounding(18, 0, 0, result2);
        fakeWorld.setSurrounding(21, 0, 0, match2);
        fakeWorld.setAt(23, 0, 0, FakeWorld.TERMINATOR);

        // If key block is present, replace the other block
        fakeWorld.setSurrounding(0, 0, 0, state);
        fakeWorld.setAt(0, 0, 1, "keyBlock");
        fakeWorld.tick();
        String actual = fakeWorld.getAt(1, 0, 0);
        Assert.assertEquals("replacement", actual);

        // If key block is NOT present, removes the other block
        fakeWorld.setSurrounding(0, 0, 0, state);
        fakeWorld.setAt(0, 0, 1, AIR);
        fakeWorld.tick();
        String actual2 = fakeWorld.getAt(1, 0, 0);
        Assert.assertEquals(AIR, actual2);

    }


}
