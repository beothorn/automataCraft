package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import org.junit.Test;

import java.util.Arrays;

import static br.com.isageek.automata.BlockTree.ANY;
import static br.com.isageek.automata.TestHelper.blocksOf;
import static br.com.isageek.automata.forge.BlockStateHolder.block;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BlockTreeTest {

    @Test
    public void findResultForMatch(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = blocksOf("a", automataBlock);

        BlockStateHolder[] match1 = blocksOf("a");
        BlockStateHolder[] result1 = blocksOf("b");

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockStateHolder[] expected = blocksOf("b");

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void anyMatcherWorks(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                block("c"), block("d"), block("e"),
                block("f"), block("g"), block("h"),
                block("i"), block("j"), block("k"),

                block("a"), block("a"), block("a"),
                block("a"), block(automataBlock), block("a"),
                block("a"), block("a"), block("a"),

                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
        };

        BlockStateHolder[] match1 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),

                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
        };

        BlockStateHolder[] result1 = blocksOf("b");

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockStateHolder[] expected = blocksOf("b");

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstAny(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = blocksOf("a", automataBlock);

        // This matches first
        BlockStateHolder[] match1 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),

                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
        };

        BlockStateHolder[] result1 = blocksOf("b");

        BlockStateHolder[] match2 = blocksOf("a");
        BlockStateHolder[] result2 = blocksOf("c");

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = blocksOf("b");

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstBlock(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = blocksOf("a", automataBlock);

        // This matches first
        BlockStateHolder[] match1 = blocksOf("a");

        BlockStateHolder[] result1 = blocksOf("c");

        BlockStateHolder[] match2 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),

                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
                block("a"), block("a"), block("a"),
        };

        BlockStateHolder[] result2 = blocksOf("c");

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = blocksOf("c");

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFallback(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = blocksOf("a", automataBlock);

        // This matches first
        BlockStateHolder[] match1 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, block("b")
        };
        BlockStateHolder[] result1 = blocksOf("b");

        BlockStateHolder[] match2 = blocksOf("a", automataBlock);
        BlockStateHolder[] result2 = blocksOf("c");

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = blocksOf("c");

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void shouldMatchAllRotations(){
        final String any =  ANY.descriptionId;

        String[][][] match = {
                {
                        {any, any, any},
                        {any, any, any},
                        {any, any, any}
                },
                {
                        {any, any, any},
                        {any, any, "block"},
                        {any, any, any}
                },
                {
                        {any, any, any},
                        {any, any, any},
                        {any, any, any}
                }
        };

        String[][][] result = {
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "replacement"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                }
        };

        BlockTree blockTree = new BlockTree();
        BlockStateHolder[] match1 = TestHelper.flatten(match);
        BlockStateHolder[] result1 = TestHelper.flatten(result);
        blockTree.addPatternRotateY(match1, result1);

        String[][][] state1 = {
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "block"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                }
        };
        String[][][] stateReplaced1 = {
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "replacement"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                }
        };
        BlockStateHolder[] state1flatten = TestHelper.flatten(state1);
        BlockStateHolder[] replaced1 = blockTree.getReplacementFor(state1flatten);
        BlockStateHolder[] expected1 = TestHelper.flatten(stateReplaced1);
        assertEquals(Arrays.toString(expected1), Arrays.toString(replaced1));

        String[][][] state2 = {
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"block", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                }
        };
        String[][][] stateReplaced2 = {
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"replacement", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                }
        };
        BlockStateHolder[] state2flatten = TestHelper.flatten(state2);
        BlockStateHolder[] replaced2 = blockTree.getReplacementFor(state2flatten);
        BlockStateHolder[] expected2 = TestHelper.flatten(stateReplaced2);
        assertEquals(Arrays.toString(expected2), Arrays.toString(replaced2));

        String[][][] state3 = {
                {
                        {"air", "air", "air"},
                        {"air", "block", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                }
        };
        String[][][] stateReplaced3 = {
                {
                        {"air", "air", "air"},
                        {"air", "replacement", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                }
        };
        BlockStateHolder[] state3flatten = TestHelper.flatten(state3);
        BlockStateHolder[] replaced3 = blockTree.getReplacementFor(state3flatten);
        BlockStateHolder[] expected3 = TestHelper.flatten(stateReplaced3);
        assertEquals(Arrays.toString(expected3), Arrays.toString(replaced3));

        String[][][] state4 = {
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "block", "air"},
                        {"air", "air", "air"}
                }
        };
        String[][][] stateReplaced4 = {
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "air", "air"},
                        {"air", "air", "air"}
                },
                {
                        {"air", "air", "air"},
                        {"air", "replacement", "air"},
                        {"air", "air", "air"}
                }
        };
        BlockStateHolder[] state4flatten = TestHelper.flatten(state4);
        BlockStateHolder[] replaced4 = blockTree.getReplacementFor(state4flatten);
        BlockStateHolder[] expected4 = TestHelper.flatten(stateReplaced4);
        assertEquals(Arrays.toString(expected4), Arrays.toString(replaced4));
    }

    @Test
    public void shouldClear(){
        BlockTree blockTree = new BlockTree();
        blockTree.addPattern( new BlockStateHolder[]{BlockStateHolder.block("a")}, new BlockStateHolder[]{BlockStateHolder.block("b")});
        assertArrayEquals(new BlockStateHolder[]{BlockStateHolder.block("b")}, blockTree.getReplacementFor(new BlockStateHolder[]{BlockStateHolder.block("a")}));
        blockTree.clear();
        assertArrayEquals(null, blockTree.getReplacementFor(new BlockStateHolder[]{BlockStateHolder.block("a")}));
    }

}
