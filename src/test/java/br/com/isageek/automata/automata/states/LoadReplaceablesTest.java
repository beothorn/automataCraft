package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.testSupport.FakeWorld;
import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static br.com.isageek.automata.forge.BlockStateHolder.block;
import static br.com.isageek.automata.testSupport.FakeWorld.STONE;
import static br.com.isageek.automata.testSupport.FakeWorld.TERMINATOR;
import static java.util.Collections.singletonList;

public class LoadReplaceablesTest {

    @Test
    public void nothingToLoadDoesNothing(){
        final FakeWorld fakeWorld = new FakeWorld();

        final LoadReplaceables loadReplaceables = new LoadReplaceables();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);

        final LoadReplaceables next = (LoadReplaceables) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void noRedSignalDoesNothing(){
        final FakeWorld fakeWorld = new FakeWorld();

        final LoadReplaceables loadReplaceables = new LoadReplaceables();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);

        final LoadReplaceables next = (LoadReplaceables) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        Assert.assertNotNull(next);
    }

    @Test
    public void noBlockBecomesAutomataBlock(){
        final FakeWorld fakeWorld = new FakeWorld();

        final LoadReplaceables loadReplaceables = new LoadReplaceables();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);

        final ExecutePattern next = (ExecutePattern) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables = next.getReplaceables();

        Assert.assertTrue(replaceables.containsKey(block(FakeWorld.AUTOMATA)));
    }

    @Test
    public void stoneBlockAtTheCenter(){
        final FakeWorld fakeWorld = new FakeWorld();

        final LoadReplaceables loadReplaceables = new LoadReplaceables();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        fakeWorld.setAt(2, 0, 0, FakeWorld.STONE);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);

        final ExecutePattern next = (ExecutePattern) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables = next.getReplaceables();

        Assert.assertTrue(replaceables.containsKey(block(FakeWorld.STONE)));
    }

    @Test
    public void automataPlaceholderBlockAtTheCenter(){
        final FakeWorld fakeWorld = new FakeWorld();

        final LoadReplaceables loadReplaceables = new LoadReplaceables();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        fakeWorld.setAt(2, 0, 0, FakeWorld.AUTOMATA_PLACEHOLDER);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);

        final ExecutePattern next = (ExecutePattern) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables = next.getReplaceables();

        Assert.assertTrue(replaceables.containsKey(block(FakeWorld.AUTOMATA)));
    }

    @Test
    public void stoneRemovedFromTheCenter(){
        final FakeWorld fakeWorld = new FakeWorld();

        final LoadReplaceables loadReplaceables = new LoadReplaceables();

        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        fakeWorld.redSignalAt(automataStartPos, true);
        fakeWorld.setAt(2, 0, 0, FakeWorld.STONE);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);

        final ExecutePattern next = (ExecutePattern) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        fakeWorld.redSignalAt(automataStartPos, false);
        final LoadReplaceables startAgain = (LoadReplaceables) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        fakeWorld.setAt(2, 0, 0, FakeWorld.AIR);
        fakeWorld.redSignalAt(automataStartPos, true);
        final ExecutePattern lastState = (ExecutePattern) startAgain.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables = lastState.getReplaceables();
        Assert.assertFalse(replaceables.containsKey(block(FakeWorld.STONE)));
        Assert.assertTrue(replaceables.containsKey(block(FakeWorld.AUTOMATA)));
    }

    @Test
    public void keepPositions(){
        final FakeWorld fakeWorld = new FakeWorld();

        final HashMap<BlockStateHolder, HashSet<BlockPos>> previousReplaceables = new HashMap<>();
        final BlockPos stonePos = new BlockPos(10, 10, 10);
        previousReplaceables.put(block(STONE), new HashSet<>(singletonList(stonePos)));
        final BlockPos automataStartPos = new BlockPos(0, 0, 0);
        final LoadReplaceables loadReplaceables = new LoadReplaceables(fakeWorld, automataStartPos, previousReplaceables);

        fakeWorld.redSignalAt(automataStartPos, true);
        fakeWorld.setAt(10, 10, 10, FakeWorld.STONE);
        fakeWorld.setAt(2, 0, 0, FakeWorld.STONE);
        fakeWorld.setAt(5, 0, 0, FakeWorld.STONE);
        fakeWorld.setAt(7, 0, 0, TERMINATOR);

        final ExecutePattern next = (ExecutePattern) loadReplaceables.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        fakeWorld.redSignalAt(automataStartPos, false);
        final LoadReplaceables startAgain = (LoadReplaceables) next.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        fakeWorld.redSignalAt(automataStartPos, true);
        final ExecutePattern lastState = (ExecutePattern) startAgain.tick(automataStartPos, fakeWorld, Integer.MAX_VALUE);
        final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables = lastState.getReplaceables();
        Assert.assertTrue(replaceables.get(block(STONE)).contains(stonePos));
    }

}