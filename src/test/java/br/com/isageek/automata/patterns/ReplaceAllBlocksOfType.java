package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.ANY;

public class ReplaceAllBlocksOfType {

    @Test
    public void replaceAllBlocksOfType(){
        final String[][][] result = {
                {
                        {"b", "b", "b"},
                        {"b", "b", "b"},
                        {"b", "b", "b"}
                },
                {
                        {"b", "b", "b"},
                        {"b", "b", "b"},
                        {"b", "b", "b"}
                },
                {
                        {"b", "b", "b"},
                        {"b", "b", "b"},
                        {"b", "b", "b"}
                }
        };
        final String[][][] match = TestHelper.cubeWithSameBlockType(ANY, "a");

        final String[][][] expected = {
                {
                        {"b", "b", "b"},
                        {"b", "b", "b"},
                        {"b", "b", "b"}
                },
                {
                        {"b", "b", "b"},
                        {"b", "b", "b"},
                        {"b", "b", "b"}
                },
                {
                        {"b", "b", "b"},
                        {"b", "b", "b"},
                        {"b", "b", "b"}
                }
        };

        final FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, TestHelper.cubeWithSameBlockType("a"));
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
