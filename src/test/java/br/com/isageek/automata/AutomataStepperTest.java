package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.Assert;
import org.junit.Test;

import static br.com.isageek.automata.forge.BlockStateHolder.b;

public class AutomataStepperTest {

    WorldController validPatternWorld = new WorldController(null, null, null, null, null, null, null, null, null) {
        public boolean isAutomataOff(int x, int y, int z) {
            return false;
        }

        public boolean isTerminator(int x, int y, int z) {
            if (x == -8 && y == 0 && z == 0) return true;
            return false;
        }

        public boolean isAutomataStart(int x, int y, int z) {
            if (x == -1 && y == 0 && z == 0) return true;
            return false;
        }

        public BlockStateHolder[] surrounding(int x, int y, int z) {
            BlockStateHolder[] match = {
                    b("a"), b("a"), b("a"),
                    b("a"), b("a"), b("a"),
                    b("a"), b("a"), b("a"),

                    b("a"), b("a"), b("a"),
                    b("a"), b("a"), b("a"),
                    b("a"), b("a"), b("a"),

                    b("a"), b("a"), b("a"),
                    b("a"), b("a"), b("a"),
                    b("a"), b("a"), b("a"),
            };
            BlockStateHolder[] replacement = {
                    b("b"), b("b"), b("b"),
                    b("b"), b("b"), b("b"),
                    b("b"), b("b"), b("b"),

                    b("b"), b("b"), b("b"),
                    b("b"), b("b"), b("b"),
                    b("b"), b("b"), b("b"),

                    b("b"), b("b"), b("b"),
                    b("b"), b("b"), b("b"),
                    b("b"), b("b"), b("b"),
            };
            if (x == -6 && y == 0 && z == 0) return match;
            if (x == -3 && y == 0 && z == 0) return replacement;
            return null;
        }
    };

    @Test
    public void doesNotLoadWhenThereIsNoStartBlock(){
        AutomataStepper automataStepper = new AutomataStepper("any");
        automataStepper.automataTick(new WorldController(null,null,null,null,null,null,null,null,null){
            public boolean isTerminator(int x, int y, int z) { return false;}
            public boolean isAutomataOff(int x, int y, int z) {return false;}
            public boolean isAutomataStart(int x, int y, int z) {return false;}
        });
        Assert.assertFalse(automataStepper.isLoaded());
    }

    @Test
    public void doesNotRunWhenThereIsAnOffBlock(){
        AutomataStepper automataStepper = new AutomataStepper("any");
        automataStepper.automataTick(validPatternWorld);
        Assert.assertTrue(automataStepper.isLoaded());
        automataStepper.automataTick(new WorldController(null,null,null,null,null,null,null,null,null){
            public boolean isAutomataOff(int x, int y, int z) {return true;}
            public boolean isTerminator(int x, int y, int z) {return false;}

            public void setBlock(int x, int y, int z, BlockState blockState) {
                throw new RuntimeException("Should not set");
            }
        });
    }

    @Test
    public void doesNotLoadWhenThereIsNoTerminator(){
        AutomataStepper automataStepper = new AutomataStepper("any");
        automataStepper.automataTick(new WorldController(null,null,null,null,null,null,null,null,null){
            public boolean isTerminator(int x, int y, int z) { return false;}
            public boolean isAutomataOff(int x, int y, int z) {return false;}

            public boolean isAutomataStart(int x, int y, int z) {
                if(x == -1 && y == 0 && z == 0) return true;
                return false;
            }
        });
        Assert.assertFalse(automataStepper.isLoaded());
    }

    @Test
    public void loadSimplePatternXAxisDescending(){
        AutomataStepper automataStepper = new AutomataStepper("a");

        automataStepper.automataTick(validPatternWorld);
        Assert.assertTrue(automataStepper.isLoaded());
    }

}