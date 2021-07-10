package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.*;

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
        String[][][] match = TestHelper.cubeWithSameBlockType(ANY);

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

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();

        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(expected, actual);
    }

}
