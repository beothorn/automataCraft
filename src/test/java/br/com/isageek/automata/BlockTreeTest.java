package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import org.junit.Test;

import java.util.Arrays;

import static br.com.isageek.automata.BlockTree.ANY;
import static br.com.isageek.automata.forge.BlockStateHolder.b;
import static org.junit.Assert.assertEquals;

public class BlockTreeTest {

    @Test
    public void findResultForMatch(){
        String automataBlock = "auto";
        BlockStateHolder[] currentState = new BlockStateHolder[]{
            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),

            b("a"),b("a"),b("a"),
            b("a"),b(automataBlock),b("a"),
            b("a"),b("a"),b("a"),

            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),
        };

        BlockStateHolder[] match1 = new BlockStateHolder[]{
            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),

            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),

            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),
            b("a"),b("a"),b("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),

            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),

            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
        };
        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockStateHolder[] expected = new BlockStateHolder[]{
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),

            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),

            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
            b("b"),b("b"),b("b"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void anyMatcherWorks(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                b("c"),b("d"),b("e"),
                b("f"),b("g"),b("h"),
                b("i"),b("j"),b("k"),

                b("a"),b("a"),b("a"),
                b("a"),b(automataBlock),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };

        BlockStateHolder[] match1 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
        };

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);

        BlockStateHolder[] expected = new BlockStateHolder[]{
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstAny(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b(automataBlock),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };

        // This matches first
        BlockStateHolder[] match1 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
        };

        BlockStateHolder[] match2 = new BlockStateHolder[]{
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };
        BlockStateHolder[] result2 = new BlockStateHolder[]{
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),

                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),

                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
        };

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = new BlockStateHolder[]{
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstBlock(){
        String automataBlock = "auto";

        BlockStateHolder[] currentState = new BlockStateHolder[]{
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b(automataBlock),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };

        // This matches first
        BlockStateHolder[] match1 = new BlockStateHolder[]{
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };
        BlockStateHolder[] result1 = new BlockStateHolder[]{
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),

                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),

                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
        };

        BlockStateHolder[] match2 = new BlockStateHolder[]{
                ANY, ANY, ANY,
                ANY, ANY, ANY,
                ANY, ANY, ANY,

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),

                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
                b("a"),b("a"),b("a"),
        };
        BlockStateHolder[] result2 = new BlockStateHolder[]{
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),

                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
                b("b"),b("b"),b("b"),
        };

        BlockTree patterns = new BlockTree();
        patterns.addPattern(match1, result1);
        patterns.addPattern(match2, result2);

        BlockStateHolder[] expected = new BlockStateHolder[]{
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),

                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),

                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
                b("c"),b("c"),b("c"),
        };

        BlockStateHolder[] actual = patterns.getReplacementFor(currentState);
        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

}