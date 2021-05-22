package br.com.isageek.voxsophon;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class VoxsophonMoverTileEntity extends TileEntity implements ITickableTileEntity {

    List<BlockState> replication;

    public VoxsophonMoverTileEntity(
        TileEntityType<VoxsophonMoverTileEntity> tileEntityType
    ) {
        super(tileEntityType);
    }

    public void setReplication(List<BlockState> newReplication){
        replication = newReplication;
    }

    @Override
    public void tick() {
        try {
            World world = getLevel();
            BlockPos blockPos = getBlockPos();

            BlockPos newBlockPosition = blockPos.offset(0, 1, 0);

            if (replication != null) {
                ArrayList<BlockState> newReplication = new ArrayList<>();
                for (int i = 0; i < replication.size(); i++) {
                    BlockState toReplicate = replication.get(i);
                    if (i < replication.size() - 1) {
                        newReplication.add(toReplicate);
                    }
                    BlockPos toReplicatePos = blockPos.offset(i + 1, 0, 0);
                    world.setBlock(
                            toReplicatePos,
                            toReplicate,
                            0,
                            0
                    );
                }

                if (newReplication.size() > 0) {
                    BlockState blockState = getBlockState();
                    world.setBlock(
                            newBlockPosition,
                            blockState,
                            0,
                            0
                    );
                    VoxsophonMoverTileEntity blockEntity = (VoxsophonMoverTileEntity) world.getBlockEntity(newBlockPosition);
                    blockEntity.setReplication(newReplication);
                }
            } else {
                ArrayList<BlockState> newReplication = new ArrayList<>();
                BlockPos one = blockPos.offset(1, 0, 0);
                BlockPos two = blockPos.offset(2, 0, 0);
                BlockPos three = blockPos.offset(3, 0, 0);
                newReplication.add(world.getBlockState(one));
                newReplication.add(world.getBlockState(two));
                newReplication.add(world.getBlockState(three));
                BlockState blockState = getBlockState();
                world.setBlock(
                        newBlockPosition,
                        blockState,
                        0,
                        0
                );
                VoxsophonMoverTileEntity blockEntity = (VoxsophonMoverTileEntity) world.getBlockEntity(newBlockPosition);
                blockEntity.setReplication(newReplication);
            }

            world.removeBlock(
                    blockPos,
                    true
            );
        }catch (Exception e){
            System.out.println(e);
        }

//        BlockPos up = blockPos.offset(0, 1, 0);
//        BlockPos down = blockPos.offset(0, -1, 0);
//        BlockPos left = blockPos.offset(-1, 0, 0);
//        BlockPos right = blockPos.offset(1, 0, 0);
//        BlockPos front = blockPos.offset(0, 0, -1);
//        BlockPos back = blockPos.offset(0, 0, 1);
//
//
//        BlockState blockStateCenter = world.getBlockState(blockPos);
//        BlockState blockStateUp = world.getBlockState(up);
//        BlockState blockStateDown = world.getBlockState(down);
//        BlockState blockStateLeft = world.getBlockState(left);
//        BlockState blockStateRight = world.getBlockState(right);
//        BlockState blockStateFront = world.getBlockState(front);
//        BlockState blockStateBack = world.getBlockState(back);
//
//
//        Boolean loaded = blockStateCenter.getValue(VoxsophonBlock.loaded);
//
//        if(copyCount > 0){
//            world.setBlock(
//                    down,
//                    blockStateUp,
//                    0,
//                    0
//            );
//
//            world.setBlock(
//                    up,
//                    blockStateDown,
//                    0,
//                    0
//            );
//
//            blockStateCenter.setValue(VoxsophonBlock.loaded, false);
//        }

//        if(
//            loaded
//            && blockStateDown.getBlock() == Blocks.GRASS_BLOCK
//            && blockStateUp.getBlock() == Blocks.AIR
//        ){
//            world.setBlock(
//                    down,
//                    blockStateUp,
//                    0,
//                    0
//            );
//
//            world.setBlock(
//                    up,
//                    blockStateDown,
//                    0,
//                    0
//            );
//
//            blockStateCenter.setValue(VoxsophonBlock.loaded, false);
//        }
    }
}
