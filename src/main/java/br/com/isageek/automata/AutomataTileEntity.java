package br.com.isageek.automata;

import br.com.isageek.automata.forge.WorldController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public class AutomataTileEntity extends TileEntity implements ITickableTileEntity {

    private final WorldController worldController;
    private AutomataStepper automataStepper;

    private final int evaluateOnEveryNTicks = 15;
    private int currentTickCounter = 0;

    private static final Logger LOGGER = LogManager.getLogger();

    public AutomataTileEntity(
            TileEntityType<AutomataTileEntity> tileEntityType,
            Block automata,
            Block automataPlaceholder,
            Block start,
            Block termination,
            Block airPlaceholder,
            Block waterPlaceholder,
            Block lavaPlaceholder,
            Block bedrockPlaceholder,
            Block caveAir
    ) {
        super(tileEntityType);
        worldController = new WorldController(
                automata,
                termination,
                start,
                automataPlaceholder,
                airPlaceholder,
                waterPlaceholder,
                lavaPlaceholder,
                bedrockPlaceholder,
                caveAir
        );
    }

    public void setAutomataStepper(AutomataStepper automataStepper){
        this.automataStepper = automataStepper;
        updateLoadState(automataStepper);
    }

    private void updateLoadState(AutomataStepper automataStepper) {
        if(this.level != null){
            boolean loaded = automataStepper.isLoaded();
            BlockState blockState = getBlockState().setValue(AutomataBlock.loaded, loaded);
            this.level.setBlock(
                    getBlockPos(),
                    blockState,
                    0,
                    0
            );
        }
    }

    @Override
    public void tick() {
        try {
            internalTick();
        }catch (Exception e){
            LOGGER.error(e);
        }
    }

    private void internalTick() {
        if(currentTickCounter < evaluateOnEveryNTicks){
            currentTickCounter++;
            return;
        }
        worldController.set(level, getBlockPos());
        boolean previousLoaded = automataStepper.isLoaded();
        automataStepper.automataTick(worldController);
        if(previousLoaded != automataStepper.isLoaded()){
            updateLoadState(automataStepper);
        }
        currentTickCounter = 0;
    }

}
