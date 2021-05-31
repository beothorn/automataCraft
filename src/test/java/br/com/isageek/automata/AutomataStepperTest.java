package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import org.junit.Assert;
import org.junit.Test;

public class AutomataStepperTest {

    @Test
    public void doesNotLoadWhenThereIsNoStartBlock(){
        AutomataStepper automataStepper = new AutomataStepper(
                FakeWorld.AIR,
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );
        FakeWorld fakeWorld = new FakeWorld();
        automataStepper.automataTick(fakeWorld);
        Assert.assertFalse(automataStepper.isLoaded());
    }

    @Test
    public void doesNotRunWhenThereIsAnOffBlock(){
        AutomataStepper automataStepper = new AutomataStepper(
                FakeWorld.AIR,
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        rubWithStopAt(automataStepper, 1, 0, 0);
        rubWithStopAt(automataStepper, -1, 0, 0);
        rubWithStopAt(automataStepper, 0, 1, 0);
        rubWithStopAt(automataStepper, 0, -1, 0);
        rubWithStopAt(automataStepper, 0, 0, 1);
        rubWithStopAt(automataStepper, 0, 0, -1);
    }

    private void rubWithStopAt(AutomataStepper automataStepper, int x, int y, int z) {
        FakeWorld fakeWorld = new FakeWorld();
        fakeWorld.setAt(x, y, z, FakeWorld.AUTOMATA_OFF);
        fakeWorld.calledSet = false;
        automataStepper.automataTick(fakeWorld);
        Assert.assertFalse(fakeWorld.calledSet);
    }

    @Test
    public void doesNotLoadWhenThereIsNoTerminator(){
        AutomataStepper automataStepper = new AutomataStepper(
                FakeWorld.AIR,
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(-1, 0 ,0, FakeWorld.AUTOMATA_START);

        automataStepper.automataTick(fakeWorld);
        Assert.assertFalse(automataStepper.isLoaded());
    }

    private String[][][] cubeWithSameBlockType(String blockId){
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
    public void loadSimplePatternXAxisDescending(){
        AutomataStepper automataStepper = new AutomataStepper(
                "a",
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(-10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.setSurrounding(-12, 0, 0, cubeWithSameBlockType("b"));
        fakeWorld.setSurrounding(-15, 0, 0, cubeWithSameBlockType("a"));
        fakeWorld.setAt(-17, 0, 0, FakeWorld.TERMINATOR);

        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld);
        String[][][] result = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(cubeWithSameBlockType("b"), result);
    }

    @Test
    public void loadSimplePatternXAxisAscending(){
        AutomataStepper automataStepper = new AutomataStepper(
                "a",
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(1, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.setSurrounding(3, 0, 0, cubeWithSameBlockType("b"));
        // Pattern is reversed
        fakeWorld.setSurrounding(6, 0, 0, cubeWithSameBlockType("a"));



        fakeWorld.setAt(8, 0, 0, FakeWorld.TERMINATOR);

        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld);
        String[][][] result = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(cubeWithSameBlockType("b"), result);
    }

    @Test
    public void loadComplexPatternXAxisAscending(){
        AutomataStepper automataStepper = new AutomataStepper(
                "anyMatcher",
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, new String[][][]{
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
        });

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, new String[][][]{
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
        });
        // Pattern is reversed
        fakeWorld.setSurrounding(15, 0, 0,new String[][][]{
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
        });

        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld);
        String[][][] result = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(new String[][][]{
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
        }, result);
    }

    @Test
    public void replaceAllWithAir(){
        String[][][] state = cubeWithSameBlockType("SomeBlock");
        String[][][] match = cubeWithSameBlockType(FakeWorld.AIR);
        String[][][] result = cubeWithSameBlockType(FakeWorld.AIR_PLACEHOLDER);
        String[][][] expected = cubeWithSameBlockType(FakeWorld.AIR);

        testPattern(state, match, result, expected);
    }

    @Test
    public void replaceAirWithX(){
        String[][][] state = cubeWithSameBlockType(FakeWorld.AIR);
        String[][][] match = cubeWithSameBlockType(FakeWorld.AIR_PLACEHOLDER);
        String[][][] result = cubeWithSameBlockType("X");
        String[][][] expected = cubeWithSameBlockType("X");

        testPattern(state, match, result, expected);
    }

    private void testPattern(
        String[][][] state,
        String[][][] match,
        String[][][] result,
        String[][][] expected
    ) {
        AutomataStepper automataStepper = new AutomataStepper(
                FakeWorld.AIR,
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();
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

    @Test
    public void replacesNothing(){
        AutomataStepper automataStepper = new AutomataStepper(
                FakeWorld.AIR,
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, cubeWithSameBlockType("X"));

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);

        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        String[][][] match = cubeWithSameBlockType(FakeWorld.AIR);
        fakeWorld.setSurrounding(12, 0, 0, match);
        String[][][] result = cubeWithSameBlockType(FakeWorld.AIR);
        fakeWorld.setSurrounding(15, 0, 0, result);

        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld);
        fakeWorld.setAt(0, 0, 0, "X");
        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(cubeWithSameBlockType("X"), actual);
    }

    @Test
    public void multiplePatternsSimpleTickClock(){
        AutomataStepper automataStepper = new AutomataStepper(
                "a",
                BlockStateHolder.b(FakeWorld.AIR),
                BlockStateHolder.b(FakeWorld.WATER),
                BlockStateHolder.b(FakeWorld.LAVA),
                BlockStateHolder.b(FakeWorld.OBSIDIAN)
        );

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(-10, 0, 0, FakeWorld.AUTOMATA_START);
        int cubePosition = -12;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("c"));
        cubePosition -= 3;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("b"));
        cubePosition -= 3;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("b"));
        cubePosition -= 3;
        fakeWorld.setSurrounding(cubePosition, 0, 0, cubeWithSameBlockType("a"));
        cubePosition -= 2;
        fakeWorld.setAt(cubePosition, 0, 0, FakeWorld.TERMINATOR);

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