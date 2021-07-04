package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;

import static br.com.isageek.automata.forge.BlockStateHolder.block;

public class TestHelper {

    public static BlockStateHolder[] blocksOf(String blockDescriptionId){
        BlockStateHolder[] result = new BlockStateHolder[27];
        for (int i = 0; i < 27; i++) {
            result[i] = block(blockDescriptionId);
        }
        return result;
    }

    public static BlockStateHolder[] blocksOf(String blockDescriptionId, String center){
        BlockStateHolder[] result = blocksOf(blockDescriptionId);
        result[13] = block(center);
        return result;
    }

    public static BlockStateHolder[] flatten(String[][][] matrix){
        BlockStateHolder[] result = new BlockStateHolder[27];

        int i = 0;
        for (int ix = 0; ix < 3; ix++) {
            for (int iy = 0; iy < 3; iy++) {
                for (int iz = 0; iz < 3; iz++) {
                    result[i++] =  block(matrix[ix][iy][iz]);
                }
            }
        }

        return result;
    }

    public static String[][][] cubeWithSameBlockType(String blockId){
        return new String[][][]{
                {
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId}
                },
                {
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId}
                },
                {
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId},
                        {blockId, blockId, blockId}
                }
        };
    }
}
