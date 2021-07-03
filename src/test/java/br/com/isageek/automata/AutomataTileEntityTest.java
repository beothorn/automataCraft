package br.com.isageek.automata;

import br.com.isageek.automata.patterns.PatternsTest;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AutomataTileEntityTest {

    @Test
    public void shouldDestroyBlockIfNoStartBlockIsFound(){
        AutomataStepper automataStepper = PatternsTest.vanillaStepper();
        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);

        fakeWorld.tick();
        ArrayList<BlockPos> destroyCalls = fakeWorld.getDestroyCalls();
        Assert.assertEquals(1, destroyCalls.size());
        Assert.assertEquals(destroyCalls.get(0), new BlockPos(0, 0, 0));

        Assert.assertEquals(FakeWorld.AIR, fakeWorld.getAt(0, 0, 0));
    }

    @Test
    public void shouldNotDestroyBlockIfStartBlockIsFoundButIsUnloaded(){
        AutomataStepper automataStepper = PatternsTest.vanillaStepper();
        FakeWorld fakeWorld = new FakeWorld(automataStepper);

        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(1, 0, 0, FakeWorld.AUTOMATA_START);

        fakeWorld.tick();
        ArrayList<BlockPos> destroyCalls = fakeWorld.getDestroyCalls();
        Assert.assertEquals(0, destroyCalls.size());

        Assert.assertEquals(FakeWorld.AUTOMATA, fakeWorld.getAt(0, 0, 0));
    }



}