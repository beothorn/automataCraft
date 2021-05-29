package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;

import static br.com.isageek.automata.forge.BlockStateHolder.b;

public class FakeWorld extends WorldController {

    public static String AUTOMATA_OFF = "AutomataOff";
    public static String AUTOMATA_START = "AutomataStart";
    public static String TERMINATOR = "Terminator";
    public static String AIR = "air";

    private BlockStateHolder[][][] fakeWorld;
    private static final int CENTER = 20;

    public boolean calledSet = false;

    public FakeWorld() {
        super(null, null, null, null, null, null, null, null, null);
        fakeWorld = new BlockStateHolder[40][40][40];
        for (int x = 0; x < CENTER * 2; x++) {
            for (int y = 0; y < CENTER * 2; y++) {
                for (int z = 0; z < CENTER * 2; z++) {
                    fakeWorld[x][y][z] = b(AIR);
                }
            }
        }
    }

    public void setAt(int x, int y, int z, String id){
        calledSet = true;
        fakeWorld[CENTER+x][CENTER+y][CENTER+z] = b(id);
    }

    public void setSurrounding(int x, int y, int z, String[][][] surroundingIds){
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    fakeWorld[x+ix+CENTER][y+iy+CENTER][z+iz+CENTER] = b(surroundingIds[ix + 1][iy + 1][iz + 1]);
                }
            }
        }
    }

    public boolean isAutomataOff(int x, int y, int z) {
        return fakeWorld[CENTER+x][CENTER+y][CENTER+z].descriptionId == AUTOMATA_OFF;
    }

    public boolean isTerminator(int x, int y, int z) {
        return fakeWorld[CENTER+x][CENTER+y][CENTER+z].descriptionId == TERMINATOR;
    }

    public boolean isAutomataStart(int x, int y, int z) {
        return fakeWorld[CENTER+x][CENTER+y][CENTER+z].descriptionId == AUTOMATA_START;
    }

    public BlockStateHolder[] surrounding(int x, int y, int z) {
        BlockStateHolder[] result = new BlockStateHolder[27];
        int i = 0;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[i++] = fakeWorld[x+ix+CENTER][y+iy+CENTER][z+iz+CENTER];
                }
            }
        }

        return result;
    }
}
