package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.ANY;

public class ReplaceAllBlocksOfType {

    @Test
    public void replaceAllBlocksOfType(){
        String[][][] result = {
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
        String[][][] match = TestHelper.cubeWithSameBlockType(ANY, "a");

        String[][][] expected = {
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

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, TestHelper.cubeWithSameBlockType("a"));
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
