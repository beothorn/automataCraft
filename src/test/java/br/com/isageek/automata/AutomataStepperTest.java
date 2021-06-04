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
        FakeWorld fakeWorld = new FakeWorld();
        automataStepper.automataTick(fakeWorld, 42);
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

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(-1, 0 ,0, AUTOMATA_START);

        automataStepper.automataTick(fakeWorld, 42);
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

        FakeWorld fakeWorld = new FakeWorld();

        String[][][] matcher = PatternsTest.cubeWithSameBlockType("a");
        String[][][] replacement = PatternsTest.cubeWithSameBlockType("b");

        fakeWorld.setAt(0, 0, 0, AUTOMATA);
        fakeWorld.setAt(-10, 0, 0, AUTOMATA_START);
        fakeWorld.setSurrounding(-12, 0, 0, replacement);
        fakeWorld.setSurrounding(-15, 0, 0, matcher);
        fakeWorld.setAt(-17, 0, 0, TERMINATOR);

        automataStepper.automataTick(fakeWorld, 42);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld, 42);
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

        FakeWorld fakeWorld = new FakeWorld();
        fakeWorld.setAt(0, 0, 0, AUTOMATA);
        fakeWorld.setAt(1, 0, 0, AUTOMATA_START);
        fakeWorld.setSurrounding(3, 0, 0, replacement);
        // Pattern is reversed
        fakeWorld.setSurrounding(6, 0, 0, matcher);
        fakeWorld.setAt(8, 0, 0, TERMINATOR);

        automataStepper.automataTick(fakeWorld, 42);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld, 42);
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

        FakeWorld fakeWorld = new FakeWorld();
        fakeWorld.setSurrounding(0, 0, 0, state);
        fakeWorld.setAt(0, 0, 0, AUTOMATA);
        fakeWorld.setAt(10, 0, 0, AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, replacement);
        fakeWorld.setSurrounding(15, 0, 0, matcher);
        fakeWorld.setAt(17, 0, 0, TERMINATOR);

        automataStepper.automataTick(fakeWorld, 42);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld, 42);
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
        AutomataStepper automataStepper = new AutomataStepper(
                AIR,
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, PatternsTest.cubeWithSameBlockType("X"));

        fakeWorld.setAt(0, 0, 0, AUTOMATA);

        fakeWorld.setAt(10, 0, 0, AUTOMATA_START);
        String[][][] result = PatternsTest.cubeWithSameBlockType(AIR);
        fakeWorld.setSurrounding(12, 0, 0, result);
        String[][][] match = PatternsTest.cubeWithSameBlockType(AIR);
        fakeWorld.setSurrounding(15, 0, 0, match);

        fakeWorld.setAt(17, 0, 0, TERMINATOR);

        automataStepper.automataTick(fakeWorld, 42);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld, 42);
        fakeWorld.setAt(0, 0, 0, "X");
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(PatternsTest.cubeWithSameBlockType("X"), actual);
    }

    @Test
    public void limitTicks(){
        AutomataStepper automataStepper = new AutomataStepper(
                AIR,
                b(AIR),
                b(WATER),
                b(LAVA),
                b(OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, PatternsTest.cubeWithSameBlockType("X"));

        fakeWorld.setAt(0, 0, 0, AUTOMATA);

        fakeWorld.setAt(10, 0, 0, AUTOMATA_START);
        String[][][] result = PatternsTest.cubeWithSameBlockType(AUTOMATA_PLACEHOLDER);
        fakeWorld.setSurrounding(12, 0, 0, result);
        String[][][] match = PatternsTest.cubeWithSameBlockType(AIR);
        fakeWorld.setSurrounding(15, 0, 0, match);

        fakeWorld.setAt(17, 0, 0, TERMINATOR);

        automataStepper.automataTick(fakeWorld, 1000);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld, 1000);
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(PatternsTest.cubeWithSameBlockType(AUTOMATA), actual);

        fakeWorld.setSurrounding(0, 0, 0, PatternsTest.cubeWithSameBlockType("X"));
        automataStepper.automataTick(fakeWorld, 0);
        String[][][] actual2 = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(PatternsTest.cubeWithSameBlockType("X"), actual2);
    }

}