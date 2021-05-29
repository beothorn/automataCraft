package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class AutomataStepper {

    private BlockTree blockTree;
    private boolean loaded = false;

    public AutomataStepper(String descriptionIdForBlockRepresentingAny){
        blockTree = new BlockTree(descriptionIdForBlockRepresentingAny);
    }

    public boolean isLoaded(){
        return loaded;
    }

    public void automataTick(WorldController worldController) {

        if(worldController.isTerminator(1, 0, 0)){
            worldController.remove(1, 0, 0);
            return;
        }
        if(worldController.isTerminator(0, 0, -1)){
            worldController.remove(0, 0, -1);
            return;
        }
        if(worldController.isTerminator(0, 0, 1)){
            worldController.remove(0, 0, 1);
            return;
        }

        if(worldController.isAutomataOff(-1, 0, 0)
            || worldController.isAutomataOff(1, 0, 0)
            || worldController.isAutomataOff(0, 0, -1)
            || worldController.isAutomataOff(0, 0, 1)
        ){
            return;
        }

        if(worldController.isAutomataStart(-1, 0, 0) && !loaded){
            if(worldController.isTerminator(-8, 0,  0)){
                loaded = true;
                BlockStateHolder[] match1 = worldController.surrounding(-6, 0, 0);
                BlockStateHolder[] result1 = worldController.surrounding(-3, 0, 0);
                blockTree.addPattern(match1, result1);
            }
        }else{
            if(loaded){
                BlockStateHolder[] toReplace = blockTree.getReplacementFor(worldController.surrounding());
                if(toReplace != null){
                    int i = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                BlockStateHolder blockStateHolder = toReplace[i++];

                                if(
                                    !worldController.isBedrock(x, y, z)
                                    && !worldController.isAutomata(x, y, z)
                                ){
                                    if(worldController.isAutomataPlaceholder(blockStateHolder)){
                                        worldController.setBlockAutomata(
                                            x,
                                            y,
                                            z
                                        );

                                        AutomataTileEntity blockEntity = (AutomataTileEntity) worldController.getBlockEntity(x, y, z);
                                        blockEntity.setAutomataStepper(this);
                                    } else{

                                        BlockState blockState = blockStateHolder.blockState;
                                        if(worldController.isAirPlaceholder(blockStateHolder)){
                                            blockState = Blocks.AIR.defaultBlockState();
                                        }
                                        if(worldController.isWaterPlaceholder(blockStateHolder)){
                                            blockState = Blocks.WATER.defaultBlockState();
                                        }
                                        if(worldController.isLavaPlaceholder(blockStateHolder)){
                                            blockState = Blocks.LAVA.defaultBlockState();
                                        }
                                        if(worldController.isBedrockPlaceholder(blockStateHolder)){
                                            blockState = Blocks.OBSIDIAN.defaultBlockState();
                                        }
                                        worldController.setBlock(
                                            x,
                                            y,
                                            z,
                                            blockState
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
