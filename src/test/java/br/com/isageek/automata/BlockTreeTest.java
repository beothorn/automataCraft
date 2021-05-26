package br.com.isageek.automata;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static br.com.isageek.automata.BlockStateHolder.b;

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
        BlockTree patterns = new BlockTree("any");
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
        Assert.assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void anyMatcherWorks(){
        String automataBlock = "auto";
        String any = "any";

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
                b(any),b(any),b(any),
                b(any),b(any),b(any),
                b(any),b(any),b(any),

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

        BlockTree patterns = new BlockTree(any);
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
        Assert.assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstAny(){
        String automataBlock = "auto";
        String any = "any";

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
                b(any),b(any),b(any),
                b(any),b(any),b(any),
                b(any),b(any),b(any),

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

        BlockTree patterns = new BlockTree(any);
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
        Assert.assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    @Test
    public void matcherPriorityFirstBlock(){
        String automataBlock = "auto";
        String any = "any";

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
                b(any),b(any),b(any),
                b(any),b(any),b(any),
                b(any),b(any),b(any),

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

        BlockTree patterns = new BlockTree(any);
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
        Assert.assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

}