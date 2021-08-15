package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.*;

public class TwoBlocks {

    @Test
    public void twoBlocks(){

        final String[][][] match = {
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

        final String[][][] result = {
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

        final FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(1, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, match);
        fakeWorld.setSurrounding(15, 0, 0, result);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();

        final String actual1 = fakeWorld.getAt(0, -1, 0);
        Assert.assertEquals(STONE, actual1);
        final String actual2 = fakeWorld.getAt(1, -1, 0);
        Assert.assertEquals(STONE, actual2);
    }

}
