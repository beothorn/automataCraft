package br.com.isageek.automata.forge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class BlockStateHolder {
    public final BlockState blockState;
    public final String descriptionId;

    public static BlockStateHolder b(String descriptionId){
        return new BlockStateHolder(null, descriptionId);
    }

    public static BlockStateHolder b(BlockState b){
        return new BlockStateHolder(b, b.getBlock().getDescriptionId());
    }

    public BlockStateHolder(
        BlockState blockState,
        String descriptionId
    ) {
        this.blockState = blockState;
        this.descriptionId = descriptionId;
    }

    @Override
    public String toString() {
        return descriptionId;
    }

    public boolean is(Block block) {
        if(blockState == null) return block.getDescriptionId() == descriptionId;
        return blockState.getBlock() == block;
    }
}
