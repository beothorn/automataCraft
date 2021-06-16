package br.com.isageek.automata.forge;

import br.com.isageek.automata.patterns.PatternsTest;
import org.junit.Assert;
import org.junit.Test;

public class BlockOperationsTest {

    @Test
    public void rotateY(){
        String[][][] pattern = {
            {
                {"(-1, -1, -1)", "(-1, -1,  0)", "(-1, -1,  1)"},
                {"(-1,  0, -1)", "(-1,  0,  0)", "(-1,  0,  1)"},
                {"(-1,  1, -1)", "(-1,  1,  0)", "(-1,  1,  1)"}
            },
            {
                {"( 0, -1, -1)", "( 0, -1,  0)", "( 0, -1,  1)"},
                {"( 0,  0, -1)", "( 0,  0,  0)", "( 0,  0,  1)"},
                {"( 0,  1, -1)", "( 0,  1,  0)", "( 0,  1,  1)"}
            },
            {
                {"( 1, -1, -1)", "( 1, -1,  0)", "( 1, -1,  1)"},
                {"( 1,  0, -1)", "( 1,  0,  0)", "( 1,  0,  1)"},
                {"( 1,  1, -1)", "( 1,  1,  0)", "( 1,  1,  1)"}
            }
        };

        String[][][] rotatedY = {
            {
                {"( 1, -1, -1)", "( 0, -1, -1)" , "(-1, -1, -1)"},
                {"( 1,  0, -1)", "( 0,  0, -1)" , "(-1,  0, -1)"},
                {"( 1,  1, -1)", "( 0,  1, -1)" , "(-1,  1, -1)"}
            },
            {
                {"( 1, -1,  0)", "( 0, -1,  0)", "(-1, -1,  0)"},
                {"( 1,  0,  0)", "( 0,  0,  0)", "(-1,  0,  0)"},
                {"( 1,  1,  0)", "( 0,  1,  0)", "(-1,  1,  0)"}
            },
            {
                {"( 1, -1,  1)", "( 0, -1,  1)", "(-1, -1,  1)"},
                {"( 1,  0,  1)", "( 0,  0,  1)", "(-1,  0,  1)"},
                {"( 1,  1,  1)", "( 0,  1,  1)", "(-1,  1,  1)"}
            }
        };

        BlockStateHolder[] actual = BlockOperations.rotateY(PatternsTest.flatten(pattern));
        BlockStateHolder[] expected = PatternsTest.flatten(rotatedY);

        Assert.assertArrayEquals(expected, actual);

    }

}