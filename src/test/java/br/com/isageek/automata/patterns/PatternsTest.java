package br.com.isageek.automata.patterns;

import br.com.isageek.automata.AutomataStepper;
import br.com.isageek.automata.FakeWorld;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.FakeWorld.*;
import static br.com.isageek.automata.forge.BlockStateHolder.b;

public class PatternsTest {

    public static void testPattern(
        String[][][] state,
        String[][][] match,
        String[][][] result,
        String[][][] expected
    ) {
        AutomataStepper automataStepper = new AutomataStepper(
                AIR,
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld(automataStepper);
        fakeWorld.setSurrounding(0, 0, 0, state);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);
        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld);
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(expected, actual);
    }

    public static String[][][] cubeWithSameBlockType(String blockId){
        return new String[][][]{
                {
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId}
                },
                {
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId}
                },
                {
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId}
                }
        };
    }

    @Test
    public void createBlockWhenAnotherBlockIsAdded(){
        AutomataStepper automataStepper = new AutomataStepper(
                "anyMatcher",
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        String[][][] state = PatternsTest.cubeWithSameBlockType(AIR);
        String[][][] match1 = PatternsTest.cubeWithSameBlockType("anyMatcher");
        match1[0][0][1] = "keyBlock";
        String[][][] result1 = PatternsTest.cubeWithSameBlockType(AIR);
        result1[1][0][0] = "replacement";
        result1[1][1][1] = AUTOMATA_PLACEHOLDER;
        String[][][] match2 = PatternsTest.cubeWithSameBlockType("anyMatcher");
        String[][][] result2 = PatternsTest.cubeWithSameBlockType(AIR);
        result2[1][0][0] = AIR;
        result2[1][1][1] = AUTOMATA_PLACEHOLDER;
        String[][][] expected = PatternsTest.cubeWithSameBlockType(AIR);
        expected[1][1][1] = AUTOMATA;

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, result1);
        fakeWorld.setSurrounding(15, 0, 0, match1);
        fakeWorld.setSurrounding(18, 0, 0, result2);
        fakeWorld.setSurrounding(21, 0, 0, match2);
        fakeWorld.setAt(23, 0, 0, FakeWorld.TERMINATOR);
        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());


        // If key block is present, replace the other block
        state[0][0][1] = "keyBlock";
        fakeWorld.setSurrounding(0, 0, 0, state);
        automataStepper.automataTick(fakeWorld);
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        expected[1][0][0] = "replacement";
        Assert.assertArrayEquals(expected, actual);

        // If key block is NOT present, removes the other block
        state[0][0][1] = AIR;
        state[1][0][0] = "replacement";
        fakeWorld.setSurrounding(0, 0, 0, state);
        automataStepper.automataTick(fakeWorld);
        String[][][] actual2 = fakeWorld.getSurroundingIds(0, 0, 0);

        expected[1][0][0] = AIR;
        Assert.assertArrayEquals(expected, actual2);

    }

    @Test
    public void multiplePatternsSimpleTickClock(){
        AutomataStepper automataStepper = new AutomataStepper(
                "a",
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(0, 0, 0, AUTOMATA);
        fakeWorld.setAt(-10, 0, 0, AUTOMATA_START);
        int cubePosition = -12;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("c"));
        cubePosition -= 3;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("b"));
        cubePosition -= 3;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("b"));
        cubePosition -= 3;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("a"));
        cubePosition -= 2;
        fakeWorld.setAt(cubePosition, 0, 0, TERMINATOR);

        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld);
        String[][][] resultTickOne = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(cubeWithSameBlockType("b"), resultTickOne);

        automataStepper.automataTick(fakeWorld);
        String[][][] resultTickTwo = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(cubeWithSameBlockType("c"), resultTickTwo);
    }
}
