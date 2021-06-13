package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.Coord;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.tileentity.TileEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static br.com.isageek.automata.forge.BlockStateHolder.b;
import static br.com.isageek.automata.forge.Coord.coord;

public class FakeWorld extends WorldController {

    public static final String WATER = "Water";
    public static final String LAVA = "Lava";
    public static final String OBSIDIAN = "Obsidian";
    public static final String AUTOMATA = "Automata";
    public static final String AUTOMATA_START = "AutomataStart";
    public static final String AUTOMATA_PLACEHOLDER = "AutomataPlaceholder";
    public static final String AIR_PLACEHOLDER = "AirPlaceholder";
    public static final String WATER_PLACEHOLDER = "WaterPlaceholder";
    public static final String LAVA_PLACEHOLDER = "LavaPlaceholder";
    public static final String BEDROCK_PLACEHOLDER = "BedrockPlaceholder";
    public static final String TERMINATOR = "Terminator";
    public static final String BEDROCK = "Bedrock";
    public static final String AIR = "air";

    private BlockStateHolder[][][] fakeWorld;

    private AutomataStepper automataStepper;

    private Map<Coord, AutomataTileEntity> entitiesOnPositions = new LinkedHashMap<>();

    private static final int CENTER = 100;

    private Coord center = coord(0, 0, 0);

    public boolean calledSet = false;

    public FakeWorld(AutomataStepper automataStepper) {
        super(null, null, null, null, null, null, null, null, null);
        this.automataStepper = automataStepper;
        fakeWorld = new BlockStateHolder[CENTER * 2][CENTER * 2][CENTER * 2];
        for (int x = 0; x < CENTER * 2; x++) {
            for (int y = 0; y < CENTER * 2; y++) {
                for (int z = 0; z < CENTER * 2; z++) {
                    fakeWorld[x][y][z] = b(AIR);
                }
            }
        }
    }

    public void tick(int ticks){
        for (int i = 0; i < ticks; i++) {
            Set<Map.Entry<Coord, AutomataTileEntity>> entries = entitiesOnPositions.entrySet();
            for (Map.Entry<Coord, AutomataTileEntity> entry : entries) {
                center = entry.getKey();
                entry.getValue().tick();
            }
        }
    }

    public void setAt(int x, int y, int z, String id){
        calledSet = true;
        fakeWorld[CENTER+x][CENTER+y][CENTER+z] = b(id);
        if(id == AUTOMATA){
            AutomataTileEntity automataTileEntity = new AutomataTileEntity(null, this);
            automataTileEntity.setAutomataStepper(automataStepper);
            entitiesOnPositions.put(Coord.coord(x, y, z), automataTileEntity);
        }
    }

    @Override
    public void setBlock(int x, int y, int z, BlockStateHolder blockState) {
        setAt(x, y, z, blockState.descriptionId);
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

    @Override
    public boolean isTerminator(int x, int y, int z) {
        if(
                x + CENTER >= CENTER * 2
                        || x + CENTER <  0
                        || z + CENTER >=  CENTER * 2
                        || z + CENTER <  0
                        || y + CENTER >=  CENTER * 2
                        || y + CENTER <  0
        ){
            return false;
        }
        return fakeWorld[CENTER+x][CENTER+y][CENTER+z].descriptionId == TERMINATOR;
    }

    @Override
    public boolean isAutomataStart(int x, int y, int z) {
        if(
               x + CENTER >= CENTER * 2
            || x + CENTER <  0
            || z + CENTER >=  CENTER * 2
            || z + CENTER <  0
            || y + CENTER >=  CENTER * 2
            || y + CENTER <  0
        ){
            return false;
        }
        return fakeWorld[CENTER+x][CENTER+y][CENTER+z].descriptionId == AUTOMATA_START;
    }

    @Override
    public boolean isBedrock(int x, int y, int z) {
        if(
                x + CENTER >= CENTER * 2
                        || x + CENTER <  0
                        || z + CENTER >=  CENTER * 2
                        || z + CENTER <  0
                        || y + CENTER >=  CENTER * 2
                        || y + CENTER <  0
        ){
            return false;
        }
        return fakeWorld[CENTER+x][CENTER+y][CENTER+z].descriptionId == BEDROCK;
    }

    @Override
    public boolean isAutomata(int x, int y, int z) {
        if(
                x + CENTER >= CENTER * 2
                        || x + CENTER <  0
                        || z + CENTER >=  CENTER * 2
                        || z + CENTER <  0
                        || y + CENTER >=  CENTER * 2
                        || y + CENTER <  0
        ){
            return false;
        }
        return fakeWorld[CENTER+x][CENTER+y][CENTER+z].descriptionId == AUTOMATA;
    }

    @Override
    public boolean isAutomataPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId == AUTOMATA_PLACEHOLDER;
    }

    @Override
    public boolean isAirPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId == AIR_PLACEHOLDER;
    }

    @Override
    public boolean isWaterPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId == WATER_PLACEHOLDER;
    }

    @Override
    public boolean isLavaPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId == LAVA_PLACEHOLDER;
    }

    @Override
    public boolean isBedrockPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId == BEDROCK_PLACEHOLDER;
    }

    @Override
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

    @Override
    public BlockStateHolder replacePlaceholder(BlockStateHolder blockState) {
        if(isAirPlaceholder(blockState)){
            return b(AIR);
        }
        if(isWaterPlaceholder(blockState)) {
            return b(WATER);
        }
        if(isLavaPlaceholder(blockState)) {
            return b(LAVA);
        }
        if(isBedrockPlaceholder(blockState)) {
            return b(BEDROCK);
        }
        return blockState;
    }

    @Override
    public void setBlockAutomata(int x, int y, int z, boolean loaded) {
        setAt(x, y, z, AUTOMATA);
    }

    @Override
    public TileEntity getBlockEntity(int x, int y, int z) {
        return entitiesOnPositions.get(Coord.coord(x, y, z));
    }

    public String[][][] getSurroundingIds(int x, int y, int z) {
        String[][][] result = new String[3][3][3];
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[ix+1][iy+1][iz+1] = fakeWorld[x+ix+CENTER][y+iy+CENTER][z+iz+CENTER].descriptionId;
                }
            }
        }
        return result;
    }
}
