package br.com.isageek.automata.patterns;

import br.com.isageek.automata.testSupport.FakeWorld;
import br.com.isageek.automata.testSupport.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.testSupport.FakeWorld.*;

public class ReloadPattern {

    @Test
    public void replaceAny(){
        String[][][] result = {
                {
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER},
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER},
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER}
                },
                {
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER},
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER},
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER}
                },
                {
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER},
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER},
                        {AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER, AUTOMATA_PLACEHOLDER}
                }
        };
        String[][][] match = TestHelper.cubeWithSameBlockType(ANY);

        String[][][] expected = {
                {
                        {AUTOMATA, AUTOMATA, AUTOMATA},
                        {AUTOMATA, AUTOMATA, AUTOMATA},
                        {AUTOMATA, AUTOMATA, AUTOMATA}
                },
                {
                        {AUTOMATA, AUTOMATA, AUTOMATA},
                        {AUTOMATA, AUTOMATA, AUTOMATA},
                        {AUTOMATA, AUTOMATA, AUTOMATA}
                },
                {
                        {AUTOMATA, AUTOMATA, AUTOMATA},
                        {AUTOMATA, AUTOMATA, AUTOMATA},
                        {AUTOMATA, AUTOMATA, AUTOMATA}
                }
        };

        FakeWorld fakeWorld = new FakeWorld();

        fakeWorld.setSurrounding(0, 0, 0, TestHelper.cubeWithSameBlockType("Dirt"));
        fakeWorld.setAt(0, 0, 0, FakeWorld.AUTOMATA);
        fakeWorld.setAt(10, 0, 0, FakeWorld.AUTOMATA_START);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.setSurrounding(12, 0, 0, result);
        fakeWorld.setSurrounding(15, 0, 0, match);
        fakeWorld.setAt(17, 0, 0, FakeWorld.TERMINATOR);

        fakeWorld.tick();

        String[][][] actual = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(expected, actual);

        String[][][] result2 = {
                {
                        {ANY, ANY, ANY},
                        {ANY, ANY, ANY},
                        {ANY, ANY, ANY}
                },
                {
                        {ANY, ANY, ANY},
                        {ANY, "a", ANY},
                        {ANY, ANY, ANY}
                },
                {
                        {ANY, ANY, ANY},
                        {ANY, ANY, ANY},
                        {ANY, ANY, ANY}
                },
        };
        String[][][] expected2 = {
                {
                        {"a", "a", "a"},
                        {"a", "a", "a"},
                        {"a", "a", "a"}
                },
                {
                        {"a", "a", "a"},
                        {"a", "a", "a"},
                        {"a", "a", "a"}
                },
                {
                        {"a", "a", "a"},
                        {"a", "a", "a"},
                        {"a", "a", "a"}
                }
        };
        fakeWorld.redSignalAt(10, 0, 0, false);
        fakeWorld.tick();
        fakeWorld.setSurrounding(12, 0, 0, result2);
        fakeWorld.redSignalAt(10, 0, 0, true);
        fakeWorld.tick();
        String[][][] actual2 = fakeWorld.getSurroundingIds(0, 0, 0);
        Assert.assertArrayEquals(expected2, actual2);
    }
}
