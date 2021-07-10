package br.com.isageek.automata.forge;

import br.com.isageek.automata.testSupport.TestHelper;
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

        BlockStateHolder[] actual = BlockOperations.rotateY(TestHelper.flatten(pattern));
        BlockStateHolder[] expected = TestHelper.flatten(rotatedY);

        Assert.assertArrayEquals(expected, actual);

    }

}