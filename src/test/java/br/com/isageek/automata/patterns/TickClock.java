package br.com.isageek.automata.patterns;

import br.com.isageek.automata.AutomataStepper;
import br.com.isageek.automata.FakeWorld;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.FakeWorld.*;

public class TickClock {

    @Test
    public void tickClock(){
        String[][] ignore = {
            {AIR, AIR, AIR},
            {AIR, AIR, AIR},
            {AIR, AIR, AIR}
        };
        String someBlock = "SomeBlock";
        String[][][] blockUp = { ignore, {
            {AIR, someBlock, AIR},
            {AIR, AUTOMATA_PLACEHOLDER, AIR},
            {AIR, AIR_PLACEHOLDER, AIR}
        }, ignore};
        String[][][] blockDown = {ignore, {
            {AIR, AIR_PLACEHOLDER, AIR},
            {AIR, AUTOMATA_PLACEHOLDER, AIR},
            {AIR, someBlock, AIR}
        }, ignore};
        String[][][] expectedUp = {ignore, {
            {AIR, someBlock, AIR},
            {AIR, AUTOMATA, AIR},
            {AIR, AIR, AIR}
        }, ignore};
        String[][][] expectedDown = {ignore, {
                {AIR, AIR, AIR},
                {AIR, AUTOMATA, AIR},
                {AIR, someBlock, AIR}
        }, ignore};

        AutomataStepper automataStepper = PatternsTest.vanillaStepper();

        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.setSurrounding(12, 0, 0, blockUp);
        fakeWorld.setSurrounding(15, 0, 0, blockDown);
        fakeWorld.setSurrounding(18, 0, 0, blockDown);
        fakeWorld.setSurrounding(21, 0, 0, blockUp);
        fakeWorld.setAt(23, 0, 0, FakeWorld.TERMINATOR);


        fakeWorld.setAt(0, -1, 0, someBlock); // starts up

        fakeWorld.tick();
        Assert.assertTrue(automataStepper.isLoaded());

        fakeWorld.doubleTick();
        String[][][] actual1 = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(expectedDown, actual1);
        fakeWorld.doubleTick();
        String[][][] actual2 = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(expectedUp, actual2);
    }
}
