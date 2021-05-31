package br.com.isageek.automata;

import br.com.isageek.automata.forge.WorldController;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutomataTileEntity extends TileEntity implements ITickableTileEntity {

    private final WorldController worldController;
    private AutomataStepper automataStepper;

    private int wait = 30;

    private static final Logger LOGGER = LogManager.getLogger();

    public AutomataTileEntity(
            TileEntityType<AutomataTileEntity> tileEntityType,
            Block automata,
            Block automataPlaceholder,
            Block start,
            Block termination,
            Block automataOff,
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
                automataOff,
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
        if(wait > 0){
            wait--;
            return;
        }
        worldController.set(level, getBlockPos());
        automataStepper.automataTick(worldController);
    }

}
