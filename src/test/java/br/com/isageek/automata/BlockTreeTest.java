package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import org.junit.Test;

import java.util.Arrays;

import static br.com.isageek.automata.BlockTree.ANY;
import static br.com.isageek.automata.forge.BlockStateHolder.block;
import static org.junit.Assert.assertEquals;

public class BlockTreeTest {

    @Test
    public void findResultForMatch(){
        String automataBlock = "auto";
        BlockStateHolder[] currentState = new BlockStateHolder[]{
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block(automataBlock), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };

        BlockStateHolder[] match1 = new BlockStateHolder[]{
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
            BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };
        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockStateHolder[] expected = new BlockStateHolder[]{
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
            BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void anyMatcherWorks(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                BlockStateHolder.block("c"), BlockStateHolder.block("d"), BlockStateHolder.block("e"),
                BlockStateHolder.block("f"), BlockStateHolder.block("g"), BlockStateHolder.block("h"),
                BlockStateHolder.block("i"), BlockStateHolder.block("j"), BlockStateHolder.block("k"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block(automataBlock), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };

        BlockStateHolder[] match1 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockStateHolder[] expected = new BlockStateHolder[]{
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstAny(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block(automataBlock), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };

        // This matches first
        BlockStateHolder[] match1 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };

        BlockStateHolder[] match2 = new BlockStateHolder[]{
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };
        BlockStateHolder[] result2 = new BlockStateHolder[]{
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
        };

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = new BlockStateHolder[]{
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstBlock(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block(automataBlock), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };

        // This matches first
        BlockStateHolder[] match1 = new BlockStateHolder[]{
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
        };

        BlockStateHolder[] match2 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };
        BlockStateHolder[] result2 = new BlockStateHolder[]{
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = new BlockStateHolder[]{
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFallback(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block(automataBlock), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };

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
                ANY, ANY, BlockStateHolder.block("b")
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),

                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
                BlockStateHolder.block("b"), BlockStateHolder.block("b"), BlockStateHolder.block("b"),
        };

        BlockStateHolder[] match2 = new BlockStateHolder[]{
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block(automataBlock), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),

                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
                BlockStateHolder.block("a"), BlockStateHolder.block("a"), BlockStateHolder.block("a"),
        };
        BlockStateHolder[] result2 = new BlockStateHolder[]{
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
        };

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = new BlockStateHolder[]{
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),

                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
                BlockStateHolder.block("c"), BlockStateHolder.block("c"), BlockStateHolder.block("c"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

}