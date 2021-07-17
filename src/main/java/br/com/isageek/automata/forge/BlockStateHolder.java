package br.com.isageek.automata.forge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Objects;

public class BlockStateHolder {
    public final BlockState blockState;
    public final String descriptionId;

    public static BlockStateHolder block(String descriptionId){
        return new BlockStateHolder(null, descriptionId);
    }

    public static BlockStateHolder block(BlockState b){
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
        if(blockState == null) return block.getDescriptionId().equals(descriptionId);
        return blockState.getBlock() == block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockStateHolder that = (BlockStateHolder) o;
        return descriptionId.equals(that.descriptionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptionId);
    }
}
