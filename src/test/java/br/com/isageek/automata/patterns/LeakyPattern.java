package br.com.isageek.automata.patterns;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

public class LeakyPattern {

    @Test
    public void patternShouldNotLeak(){
        final String[][][] blockWithA = TestHelper.cubeWithSameBlockType(FakeWorld.STONE);

        final FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, blockWithA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setBlock(BlockStateHolder.block(FakeWorld.STONE), new BlockPos(12, 0, 0));
        fakeWorld.setSurrounding(15, 0, 0, blockWithA);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();

        final String shouldBeEmpty = fakeWorld.getBlockStateHolderAt(new BlockPos(15, -2, 0)).descriptionId;
        Assert.assertEquals(FakeWorld.AIR, shouldBeEmpty);
    }

}
