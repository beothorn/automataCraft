package br.com.isageek.automata.patterns;

import br.com.isageek.automata.AutomataStepper;
import br.com.isageek.automata.FakeWorld;
import br.com.isageek.automata.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.FakeWorld.*;

public class AutomataMoveOneBlock {

    @Test
    public void automataMoveOneBlock(){

        String[][][] result = {
            {
                {AIR, AIR, AIR},
                {AIR, AIR, AIR},
                {AIR, AIR, AIR}
            },
            {
                {AIR, AIR, AIR},
                {AIR, AIR_PLACEHOLDER, AUTOMATA_PLACEHOLDER},
                {AIR, AIR, AIR}
            },
            {
                {AIR, AIR, AIR},
                {AIR, AIR, AIR},
                {AIR, AIR, AIR}
            }
        };
        String[][][] match = TestHelper.cubeWithSameBlockType(AIR);

        String[][][] expected = {
            {
                {AIR, AIR, AIR},
                {AIR, AIR, AIR},
                {AIR, AIR, AIR}
            },
            {
                {AIR, AIR, AIR},
                {AIR, AIR, AUTOMATA},
                {AIR, AIR, AIR}
            },
            {
                {AIR, AIR, AIR},
                {AIR, AIR, AIR},
                {AIR, AIR, AIR}
            }
        };

        AutomataStepper automataStepper = PatternsTest.vanillaStepper();

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.doubleTick();
        String[][][] actual1 = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(expected, actual1);
        fakeWorld.doubleTick();
        String[][][] actual2 = fakeWorld.getSurroundingIds(0, 0, 1);
        Assert.assertArrayEquals(expected, actual2);
    }

}
