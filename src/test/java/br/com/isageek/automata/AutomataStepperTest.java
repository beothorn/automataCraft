package br.com.isageek.automata;

import br.com.isageek.automata.forge.WorldController;
import net.minecraft.world.World;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AutomataStepperTest {

    @Test
    public void doesNotLoadWhenThereIsNoStartBlock(){
        AutomataStepper automataStepper = new AutomataStepper("any");
        automataStepper.automataTick(
                new WorldController(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ){
                    @Override
                    public boolean isTerminator(int x, int y, int z) {
                        return false;
                    }

                    @Override
                    public boolean isAutomataOff(int x, int y, int z) {
                        return false;
                    }

                    @Override
                    public boolean isAutomataStart(int x, int y, int z) {
                        return false;
                    }
                }
        );
        Assert.assertFalse(automataStepper.isLoaded());
    }

}