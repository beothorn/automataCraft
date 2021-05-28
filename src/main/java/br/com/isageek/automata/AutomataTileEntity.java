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

    private final Block start;
    private final Block termination;
    private final Block automataOff;
    private final Block automata;
    private final Block automataPlaceholder;
    private final Block airPlaceholder;
    private final Block waterPlaceholder;
    private final Block lavaPlaceholder;
    private final Block bedrockPlaceholder;
    private final TileEntityType<AutomataTileEntity> tileEntityType;
    private final WorldController worldController;
    private AutomataStepper automataStepper;

    private int wait = 10;

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
            Block bedrockPlaceholder
    ) {
        super(tileEntityType);
        this.tileEntityType = tileEntityType;
        this.automata = automata;
        this.start = start;
        this.termination = termination;
        this.automataOff = automataOff;
        this.automataPlaceholder = automataPlaceholder;
        this.airPlaceholder = airPlaceholder;
        this.waterPlaceholder = waterPlaceholder;
        this.lavaPlaceholder = lavaPlaceholder;
        this.bedrockPlaceholder = bedrockPlaceholder;
        worldController = new WorldController(
                automata,
                termination,
                automataOff,
                start,
                automataPlaceholder,
                airPlaceholder,
                waterPlaceholder,
                lavaPlaceholder,
                bedrockPlaceholder
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
