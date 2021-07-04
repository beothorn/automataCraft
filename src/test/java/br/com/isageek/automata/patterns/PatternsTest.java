package br.com.isageek.automata.patterns;

import br.com.isageek.automata.AutomataStepper;
import br.com.isageek.automata.FakeWorld;
import br.com.isageek.automata.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.FakeWorld.*;
import static br.com.isageek.automata.forge.BlockStateHolder.block;

public class PatternsTest {

    public static void testPattern(
        String[][][] state,
        String[][][] match,
        String[][][] result,
        String[][][] expected
    ) {
        AutomataStepper automataStepper = vanillaStepper();

        FakeWorld fakeWorld = new FakeWorld(automataStepper);
        fakeWorld.setSurrounding(0, 0, 0, state);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);
        fakeWorld.tick(); // One tick to load
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.tick(); // One tick to read world state
        fakeWorld.tick(); // One tick to replace blocks
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(expected, actual);
    }

    public static AutomataStepper vanillaStepper() {
        return new AutomataStepper(
                AIR,
                block(AIR),
                block(WATER),
                block(LAVA),
                block(OBSIDIAN)
        );
    }

    @Test
    public void createBlockWhenAnotherBlockIsAdded(){
        AutomataStepper automataStepper = new AutomataStepper(
                "anyMatcher",
                block(AIR),
                block(WATER),
                block(LAVA),
                block(OBSIDIAN)
        );

        String[][][] state = TestHelper.cubeWithSameBlockType(AIR);
        String[][][] match1 = TestHelper.cubeWithSameBlockType("anyMatcher");
        match1[0][0][1] = "keyBlock";
        String[][][] result1 = TestHelper.cubeWithSameBlockType(AIR);
        result1[1][0][0] = "replacement";
        result1[1][1][1] = AUTOMATA_PLACEHOLDER;
        String[][][] match2 = TestHelper.cubeWithSameBlockType("anyMatcher");
        String[][][] result2 = TestHelper.cubeWithSameBlockType(AIR);
        result2[1][0][0] = AIR;
        result2[1][1][1] = AUTOMATA_PLACEHOLDER;
        String[][][] expected = TestHelper.cubeWithSameBlockType(AIR);
        expected[1][1][1] = AUTOMATA;

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result1);
        fakeWorld.setSurrounding(15, 0, 0, match1);
        fakeWorld.setSurrounding(18, 0, 0, result2);
        fakeWorld.setSurrounding(21, 0, 0, match2);
        fakeWorld.setAt(23, 0, 0, FakeWorld.TERMINATOR);
        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());


        // If key block is present, replace the other block
        state[0][0][1] = "keyBlock";
        fakeWorld.setSurrounding(0, 0, 0, state);
        fakeWorld.doubleTick();
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        expected[1][0][0] = "replacement";
        Assert.assertArrayEquals(expected, actual);

        // If key block is NOT present, removes the other block
        state[0][0][1] = AIR;
        state[1][0][0] = "replacement";
        fakeWorld.setSurrounding(0, 0, 0, state);
        fakeWorld.doubleTick();
        String[][][] actual2 = fakeWorld.getSurroundingIds(0, 0, 0);

        expected[1][0][0] = AIR;
        Assert.assertArrayEquals(expected, actual2);

    }


}
