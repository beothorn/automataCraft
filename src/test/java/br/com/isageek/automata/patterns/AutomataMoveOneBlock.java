package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.*;

public class AutomataMoveOneBlock {

    @Test
    public void automataMoveOneBlock(){

        final String[][][] result = {
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
        final String[][][] match = TestHelper.cubeWithSameBlockType(ANY, AIR);

        final String[][][] expected = {
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

        final FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, match);
        fakeWorld.setSurrounding(15, 0, 0, result);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();

        final String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(expected, actual);
    }

}
