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

    public AutomataTileEntity(
            TileEntityType<AutomataTileEntity> tileEntityType,
            WorldController worldController
    ) {
        super(tileEntityType);
        this.worldController = worldController;
    }

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
            Block caveAir,
            Block yRotation
    ) {
        this(tileEntityType, new WorldController(
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
    }

    public void setAutomataStepper(AutomataStepper automataStepper){
        this.automataStepper = automataStepper;
        setStateToLoaded();
    }

    /***
     * This will update the texture from unloaded to loaded
     *
     */
    private void setStateToLoaded() {
        if(this.level != null){
            BlockState blockState = getBlockState().setValue(AutomataBlock.loaded, true);
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
        currentTickCounter++;
        if(currentTickCounter < EVAL_EVERY_TICKS){
            return;
        }
        BlockPos center = getBlockPos();
        worldController.set(level);

        if(automataStepper.isLoaded()){
            if(nextReplacement != null){
                automataStepper.replace(worldController, nextReplacement, center);
                nextReplacement = null;
            }else{
                nextReplacement = automataStepper.getReplacement(worldController, center);
            }
        }else{
            if(!automataStepper.findStart(worldController, center)){
                worldController.destroyBlock(center);
                return;
            }
            if(automataStepper.load(worldController, center)){
                setStateToLoaded();
            }
        }

        currentTickCounter = 0;
    }

}
