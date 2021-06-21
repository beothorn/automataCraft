package br.com.isageek.automata.patterns;

import br.com.isageek.automata.AutomataStepper;
import br.com.isageek.automata.FakeWorld;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.FakeWorld.*;

public class TwoBlocks {

    @Test
    public void twoBlocks(){

        String[][][] match = {
                {
                        {AIR_PLACEHOLDER, AIR_PLACEHOLDER, AIR_PLACEHOLDER},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR_PLACEHOLDER, AIR_PLACEHOLDER, AIR_PLACEHOLDER},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR_PLACEHOLDER, AIR_PLACEHOLDER, AIR_PLACEHOLDER},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                }
        };

        String[][][] result = {
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR, STONE, AIR},
                        {AIR, AUTOMATA_PLACEHOLDER, AIR},
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
        fakeWorld.setAt(1, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.doubleTick();

        String actual1 = fakeWorld.getAt(0, -1, 0);
        Assert.assertEquals(STONE, actual1);
        String actual2 = fakeWorld.getAt(1, -1, 0);
        Assert.assertEquals(STONE, actual2);
    }

}
