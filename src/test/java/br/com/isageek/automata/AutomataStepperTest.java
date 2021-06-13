package br.com.isageek.automata;

import br.com.isageek.automata.patterns.PatternsTest;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.FakeWorld.*;
import static br.com.isageek.automata.forge.BlockStateHolder.b;

public class AutomataStepperTest {

    @Test
    public void doesNotLoadWhenThereIsNoStartBlock(){
        AutomataStepper automataStepper = new AutomataStepper(
                AIR,
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );
        FakeWorld fakeWorld = new FakeWorld(automataStepper);
        fakeWorld.tick();
        Assert.assertFalse(automataStepper.isLoaded());
    }

    @Test
    public void doesNotLoadWhenThereIsNoTerminator(){
        AutomataStepper automataStepper = new AutomataStepper(
                AIR,
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(-1, 0 ,0, AUTOMATA_START);

        fakeWorld.tick();
        Assert.assertFalse(automataStepper.isLoaded());
    }

    @Test
    public void loadSimplePatternXAxisDescending(){
        AutomataStepper automataStepper = new AutomataStepper(
                "a",
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        String[][][] matcher = PatternsTest.cubeWithSameBlockType("a");
        String[][][] replacement = PatternsTest.cubeWithSameBlockType("b");

        fakeWorld.setAt(0, 0, 0, AUTOMATA);
        fakeWorld.setAt(-10, 0, 0, AUTOMATA_START);
        fakeWorld.setSurrounding(-12, 0, 0, replacement);
        fakeWorld.setSurrounding(-15, 0, 0, matcher);
        fakeWorld.setAt(-17, 0, 0, TERMINATOR);

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.tick();
        String[][][] result = fakeWorld.getSurroundingIds(0, 0, 0);

        String[][][] expected = PatternsTest.cubeWithSameBlockType("b");
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void loadSimplePatternXAxisAscending(){
        AutomataStepper automataStepper = new AutomataStepper(
                "a",
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        String[][][] matcher = PatternsTest.cubeWithSameBlockType("a");
        String[][][] replacement = PatternsTest.cubeWithSameBlockType("b");

        FakeWorld fakeWorld = new FakeWorld(automataStepper);
        fakeWorld.setAt(0, 0, 0, AUTOMATA);
        fakeWorld.setAt(1, 0, 0, AUTOMATA_START);
        fakeWorld.setSurrounding(3, 0, 0, replacement);
        // Pattern is reversed
        fakeWorld.setSurrounding(6, 0, 0, matcher);
        fakeWorld.setAt(8, 0, 0, TERMINATOR);

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.tick();
        String[][][] result = fakeWorld.getSurroundingIds(0, 0, 0);

        String[][][] expected = PatternsTest.cubeWithSameBlockType("b");
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void loadComplexPatternXAxisAscending(){
        AutomataStepper automataStepper = new AutomataStepper(
                "anyMatcher",
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        String[][][] state = {
            {
                {"b", "c", "d"},
                {"e", "f", "g"},
                {"h", "i", "j"}
            },
            {
                {"k", "l", "m"},
                {"n", "o", "p"},
                {"q", "r", "s"}
            },
            {
                {"t", "u", "v"},
                {"w", "x", "y"},
                {"z", "stayTheSame", "stayTheSame"}
            }
        };

        String[][][] replacement = {
            {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"}
            },
            {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"}
            },
            {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "anyMatcher", "anyMatcher"}
            }
        };

        // Pattern is reversed
        String[][][] matcher = {
            {
                {"anyMatcher", "anyMatcher", "anyMatcher"},
                {"anyMatcher", "anyMatcher", "anyMatcher"},
                {"anyMatcher", "anyMatcher", "anyMatcher"}
            },
            {
                {"k", "l", "m"},
                {"n", "o", "p"},
                {"q", "r", "s"}
            },
            {
                {"t", "u", "v"},
                {"w", "x", "y"},
                {"z", "anyMatcher", "anyMatcher"}
            }
        };

        FakeWorld fakeWorld = new FakeWorld(automataStepper);
        fakeWorld.setSurrounding(0, 0, 0, state);
        fakeWorld.setAt(0, 0, 0, AUTOMATA);
        fakeWorld.setAt(10, 0, 0, AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, replacement);
        fakeWorld.setSurrounding(15, 0, 0, matcher);
        fakeWorld.setAt(17, 0, 0, TERMINATOR);

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.tick();
        String[][][] result = fakeWorld.getSurroundingIds(0, 0, 0);

        String[][][] expected = {
            {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"}
            },
            {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"}
            },
            {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "stayTheSame", "stayTheSame"}
            }
        };
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void replaceAllWithAir(){
        String[][][] state = PatternsTest.cubeWithSameBlockType("SomeBlock");
        String[][][] match = PatternsTest.cubeWithSameBlockType(AIR);
        String[][][] result = PatternsTest.cubeWithSameBlockType(AIR_PLACEHOLDER);
        String[][][] expected = PatternsTest.cubeWithSameBlockType(AIR);

        PatternsTest.testPattern(state, match, result, expected);
    }

    @Test
    public void replaceAirWithX(){
        String[][][] state = PatternsTest.cubeWithSameBlockType(AIR);
        String[][][] match = PatternsTest.cubeWithSameBlockType(AIR_PLACEHOLDER);
        String[][][] result = PatternsTest.cubeWithSameBlockType("X");
        String[][][] expected = PatternsTest.cubeWithSameBlockType("X");

        PatternsTest.testPattern(state, match, result, expected);
    }

    @Test
    public void replacesNothing(){
        AutomataStepper automataStepper = PatternsTest.vanillaStepper();

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setSurrounding(0, 0, 0, PatternsTest.cubeWithSameBlockType("X"));

        fakeWorld.setAt(0, 0, 0, AUTOMATA);

        fakeWorld.setAt(10, 0, 0, AUTOMATA_START);
        String[][][] result = PatternsTest.cubeWithSameBlockType(AIR);
        fakeWorld.setSurrounding(12, 0, 0, result);
        String[][][] match = PatternsTest.cubeWithSameBlockType(AIR);
        fakeWorld.setSurrounding(15, 0, 0, match);

        fakeWorld.setAt(17, 0, 0, TERMINATOR);

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.tick();
        fakeWorld.setAt(0, 0, 0, "X");
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(PatternsTest.cubeWithSameBlockType("X"), actual);
    }

    @Test
    public void usesYRotationBlock(){
        AutomataStepper automataStepper = PatternsTest.vanillaStepper();

        String[][][] match = PatternsTest.cubeWithSameBlockType(AIR);
        match[0][1][1] = "SomeBlock";
        match[1][1][1] = AUTOMATA_Y_ROTATION;

        String[][][] result = PatternsTest.cubeWithSameBlockType("Matched");

        FakeWorld fakeWorld = new FakeWorld(automataStepper);
        fakeWorld.setAt(0, 0, 0, AUTOMATA);

        fakeWorld.setAt(10, 0, 0, AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);

        fakeWorld.setAt(17, 0, 0, TERMINATOR);

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.setAt(-1, 0, 0, "SomeBlock");
        fakeWorld.tick();
        Assert.assertArrayEquals(PatternsTest.cubeWithSameBlockType("Matched"), fakeWorld.getSurroundingIds(0, 0, 0));

        fakeWorld.setSurrounding(0, 0, 0, PatternsTest.cubeWithSameBlockType(AIR));
        fakeWorld.setAt(0, 0, 0, AUTOMATA);

        fakeWorld.setAt(1, 0, 0, "SomeBlock");
        fakeWorld.tick();
        Assert.assertArrayEquals(PatternsTest.cubeWithSameBlockType("Matched"), fakeWorld.getSurroundingIds(0, 0, 0));
    }

}