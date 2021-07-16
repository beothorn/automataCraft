package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.*;

public class KeyBlock {

    @Test
    public void keyBlock(){
        /*
         * Important when you have a sticky piston move a block
         * near the automata and have a pattern that can be turned
         * on and off
         */
        String[][][] result1 = {
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR, "KeyBlockNotPresent", AIR},
                        {AIR, AUTOMATA_PLACEHOLDER, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                }
        };
        String[][][] match1 = {
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR_PLACEHOLDER, AIR}
                },
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                }
        };

        String[][][] result2 = {
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR, "KeyBlockPresent", AIR},
                        {AIR, AUTOMATA_PLACEHOLDER, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                }
        };
        String[][][] match2 = {
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR}
                },
                {
                        {AIR, AIR, AIR},
                        {AIR, AIR, AIR},
                        {AIR, "KeyBlock", AIR}
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
        fakeWorld.setSurrounding(12, 0, 0, result1);
        fakeWorld.setSurrounding(15, 0, 0, match1);
        fakeWorld.setSurrounding(18, 0, 0, result2);
        fakeWorld.setSurrounding(21, 0, 0, match2);
        fakeWorld.setAt(23, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);

        fakeWorld.tick();

        Assert.assertEquals("KeyBlockNotPresent", fakeWorld.getAt(0, -1, 0));

        fakeWorld.setAt(0, 1, 0, "KeyBlock");
        fakeWorld.tick();

        Assert.assertEquals("KeyBlockPresent", fakeWorld.getAt(0, -1, 0));
    }
}
