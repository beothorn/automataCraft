package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutomataTileEntity extends TileEntity implements ITickableTileEntity {

    private final WorldController worldController;

    private AutomataStepper automataStepper;

    public static final int EVAL_EVERY_TICKS = 30;
    public static final boolean THROW_EXCEPTIONS = true;

    private int currentTickCounter = 0;
    BlockStateHolder[] nextReplacement;

    private static final Logger LOGGER = LogManager.getLogger();
    private Block automata;

    public AutomataTileEntity(
            TileEntityType<AutomataTileEntity> tileEntityType,
            WorldController worldController
    ) {
        super(tileEntityType);
        this.worldController = worldController;
    }

    public AutomataTileEntity(
            TileEntityType<AutomataTileEntity> tileEntityType,
            Block air,
            Block automata,
            Block automataPlaceholder,
            Block start,
            Block termination,
            Block airPlaceholder,
            Block waterPlaceholder,
            Block lavaPlaceholder,
            Block bedrockPlaceholder,
            Block caveAir,
            Block yRotation
    ) {
        this(tileEntityType, new WorldController(
            air,
            automata,
            termination,
            start,
            automataPlaceholder,
            airPlaceholder,
            waterPlaceholder,
            lavaPlaceholder,
            bedrockPlaceholder,
            caveAir,
            yRotation
        ));
        this.automata = automata;
    }

    public void setAutomataStepper(AutomataStepper automataStepper){
        this.automataStepper = automataStepper;
        setLoadedStateTo(true);
    }

    /***
     * This will update the texture
     *
     */
    private void setLoadedStateTo(boolean isLoaded) {
        if(this.level != null){
            BlockState blockState = getBlockState().setValue(AutomataBlock.loaded, isLoaded);
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
            if(THROW_EXCEPTIONS){
                throw e;
            }
        }
    }

    private void internalTick() {
        if( this.level != null && getBlockState().getBlock() != automata) return;
        currentTickCounter++;
        if(currentTickCounter < EVAL_EVERY_TICKS){
            return;
        }
        BlockPos center = getBlockPos();
        worldController.set(level);

        nextReplacement = automataStepper.tick(
                worldController,
                center,
                nextReplacement,
                () -> worldController.destroyBlock(center)
        );

        setLoadedStateTo(automataStepper.isLoaded());

        currentTickCounter = 0;
    }

}
