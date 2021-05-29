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
        FakeWorld fakeWorld = new FakeWorld();
        fakeWorld.setAt(1, 0, 0, FakeWorld.AUTOMATA_OFF);

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

        fakeWorld.setAt(-1, 0, 0, FakeWorld.AUTOMATA_START);

        fakeWorld.setSurrounding(-3, 0, 0, new String[][][]{
            {
                {"b", "b", "b"},
                {"b", "b", "b"},
                {"b", "b", "b"}
            },
            {
                {"b", "b", "b"},
                {"b", "b", "b"},
                {"b", "b", "b"}
            },
            {
                {"b", "b", "b"},
                {"b", "b", "b"},
                {"b", "b", "b"}
            }
        });

        fakeWorld.setSurrounding(-6, 0, 0, new String[][][]{
            {
                {"a", "a", "a"},
                {"a", "a", "a"},
                {"a", "a", "a"}
            },
            {
                {"a", "a", "a"},
                {"a", "a", "a"},
                {"a", "a", "a"}
            },
            {
                {"a", "a", "a"},
                {"a", "a", "a"},
                {"a", "a", "a"}
            }
        });

        fakeWorld.setAt(-8, 0, 0, FakeWorld.TERMINATOR);

        automataStepper.automataTick(fakeWorld);
        Assert.assertTrue(automataStepper.isLoaded());

        automataStepper.automataTick(fakeWorld);
        String[][][] result = fakeWorld.getSurroundingIds(0, 0, 0);

        Assert.assertArrayEquals(
            new String[][][]{
                    {
                            {"b", "b", "b"},
                            {"b", "b", "b"},
                            {"b", "b", "b"}
                    },
                    {
                            {"b", "b", "b"},
                            {"b", "b", "b"},
                            {"b", "b", "b"}
                    },
                    {
                            {"b", "b", "b"},
                            {"b", "b", "b"},
                            {"b", "b", "b"}
                    }
            },
            result
        );
    }

}