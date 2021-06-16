package br.com.isageek.automata.forge;

public class BlockOperations {

    public static BlockStateHolder[] rotateY(BlockStateHolder[] toBeRotated){
//        0 : (-1, -1, -1), 1 : (-1, -1,  0), 2 : (-1, -1,  1), 3 : (-1,  0, -1), 4 : (-1,  0,  0), 5 : (-1,  0,  1), 6 : (-1,  1, -1), 7 : (-1,  1,  0), 8 : (-1,  1,  1), 9 : ( 0, -1, -1), 10 : ( 0, -1,  0), 11 : ( 0, -1,  1), 12 : ( 0,  0, -1), 13 : ( 0,  0,  0), 14 : ( 0,  0,  1), 15 : ( 0,  1, -1), 16 : ( 0,  1,  0), 17 : ( 0,  1,  1), 18 : ( 1, -1, -1), 19 : ( 1, -1,  0), 20 : ( 1, -1,  1), 21 : ( 1,  0, -1), 22 : ( 1,  0,  0), 23 : ( 1,  0,  1), 24 : ( 1,  1, -1), 25 : ( 1,  1,  0), 26 : ( 1,  1,  1),
//        0 : ( 1, -1, -1), 1 : ( 0, -1, -1), 2 : (-1, -1, -1), 3 : ( 1,  0, -1), 4 : ( 0,  0, -1), 5 : (-1,  0, -1), 6 : ( 1,  1, -1), 7 : ( 0,  1, -1), 8 : (-1,  1, -1), 9 : ( 1, -1,  0), 10 : ( 0, -1,  0), 11 : (-1, -1,  0), 12 : ( 1,  0,  0), 13 : ( 0,  0,  0), 14 : (-1,  0,  0), 15 : ( 1,  1,  0), 16 : ( 0,  1,  0), 17 : (-1,  1,  0), 18 : ( 1, -1,  1), 19 : ( 0, -1,  1), 20 : (-1, -1,  1), 21 : ( 1,  0,  1), 22 : ( 0,  0,  1), 23 : (-1,  0,  1), 24 : ( 1,  1,  1), 25 : ( 0,  1,  1), 26 : (-1,  1,  1),
        BlockStateHolder[] result = new BlockStateHolder[27];

        result[0] = toBeRotated[18];
        result[1] = toBeRotated[9];
        result[2] = toBeRotated[0];
        result[3] = toBeRotated[21];
        result[4] = toBeRotated[12];
        result[5] = toBeRotated[3];
        result[6] = toBeRotated[24];
        result[7] = toBeRotated[15];
        result[8] = toBeRotated[6];
        result[9] = toBeRotated[19];
        result[10] = toBeRotated[10];
        result[11] = toBeRotated[1];
        result[12] = toBeRotated[22];
        result[13] = toBeRotated[13];
        result[14] = toBeRotated[4];
        result[15] = toBeRotated[25];
        result[16] = toBeRotated[16];
        result[17] = toBeRotated[7];
        result[18] = toBeRotated[20];
        result[19] = toBeRotated[11];
        result[20] = toBeRotated[2];
        result[21] = toBeRotated[23];
        result[22] = toBeRotated[14];
        result[23] = toBeRotated[5];
        result[24] = toBeRotated[26];
        result[25] = toBeRotated[17];
        result[26] = toBeRotated[8];

        return result;
    }
}
