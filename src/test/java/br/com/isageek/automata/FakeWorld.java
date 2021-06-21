package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.Coord;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static br.com.isageek.automata.forge.BlockStateHolder.block;
import static br.com.isageek.automata.forge.Coord.coord;

public class FakeWorld extends WorldController {

    public static final String WATER = "Water";
    public static final String LAVA = "Lava";
    public static final String OBSIDIAN = "Obsidian";
    public static final String STONE = "stone";
    public static final String AUTOMATA = "Automata";
    public static final String AUTOMATA_START = "AutomataStart";
    public static final String AUTOMATA_PLACEHOLDER = "AutomataPlaceholder";
    public static final String AIR_PLACEHOLDER = "AirPlaceholder";
    public static final String WATER_PLACEHOLDER = "WaterPlaceholder";
    public static final String LAVA_PLACEHOLDER = "LavaPlaceholder";
    public static final String BEDROCK_PLACEHOLDER = "BedrockPlaceholder";
    public static final String AUTOMATA_Y_ROTATION = "AutomataYRotation";
    public static final String TERMINATOR = "Terminator";
    public static final String BEDROCK = "Bedrock";
    public static final String AIR = "air";

    private BlockStateHolder[][][] fakeWorld;

    private AutomataStepper automataStepper;

    private Map<Coord, AutomataTileEntity> entitiesOnPositions = new LinkedHashMap<>();

    private ArrayList<Coord> destroyCalls = new ArrayList<>();

    private static final int WORLD_CENTER = 100;

    private Coord cursor = coord(0, 0, 0);

    public boolean calledSet = false;

    public FakeWorld(AutomataStepper automataStepper) {
        super(null, null, null, null, null, null, null, null, null, null);
        this.automataStepper = automataStepper;
        fakeWorld = new BlockStateHolder[WORLD_CENTER * 2][WORLD_CENTER * 2][WORLD_CENTER * 2];
        for (int x = 0; x < WORLD_CENTER * 2; x++) {
            for (int y = 0; y < WORLD_CENTER * 2; y++) {
                for (int z = 0; z < WORLD_CENTER * 2; z++) {
                    fakeWorld[x][y][z] = block(AIR);
                }
            }
        }
    }

    public void tick(){
        tick(AutomataTileEntity.EVAL_EVERY_TICKS);
    }

    public void doubleTick(){
        tick(AutomataTileEntity.EVAL_EVERY_TICKS);
        tick(AutomataTileEntity.EVAL_EVERY_TICKS);
    }

    public void tick(int ticks){
        for (int i = 0; i < ticks; i++) {
            if(i == ticks - 1){
                System.out.println("Last tick");
            }
            Set<Map.Entry<Coord, AutomataTileEntity>> entries = entitiesOnPositions.entrySet();
            for (Map.Entry<Coord, AutomataTileEntity> entry : entries) {
                cursor = entry.getKey();
                entry.getValue().tick();
            }
        }
    }

    public void setAt(int x, int y, int z, String id){
        calledSet = true;
        x = x + cursor.x;
        y = y + cursor.y;
        z = z + cursor.z;
        fakeWorld[WORLD_CENTER +x][WORLD_CENTER +y][WORLD_CENTER +z] = block(id);
        if(id.equals(AUTOMATA)){
            AutomataTileEntity automataTileEntity = new AutomataTileEntity(null, this);
            automataTileEntity.setAutomataStepper(automataStepper);
            entitiesOnPositions.put(Coord.coord(x, y, z), automataTileEntity);
            automataTileEntity.setPosition(new BlockPos(x, y, z));
        }else{
            entitiesOnPositions.remove(Coord.coord(x, y, z));
        }
    }

    @Override
    public void setBlock(int x, int y, int z, BlockStateHolder blockState) {
        setAt(x, y, z, blockState.descriptionId);
    }

    public void setSurrounding(int x, int y, int z, String[][][] surroundingIds){
        x = x + cursor.x;
        y = y + cursor.y;
        z = z + cursor.z;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER] = block(surroundingIds[ix + 1][iy + 1][iz + 1]);
                }
            }
        }
    }

    @Override
    public boolean isTerminator(int x, int y, int z) {
        x = x + cursor.x;
        y = y + cursor.y;
        z = z + cursor.z;
        if(
                x + WORLD_CENTER >= WORLD_CENTER * 2
                        || x + WORLD_CENTER <  0
                        || z + WORLD_CENTER >=  WORLD_CENTER * 2
                        || z + WORLD_CENTER <  0
                        || y + WORLD_CENTER >=  WORLD_CENTER * 2
                        || y + WORLD_CENTER <  0
        ){
            return false;
        }
        return fakeWorld[WORLD_CENTER + x][WORLD_CENTER + y][WORLD_CENTER + z].descriptionId.equals(TERMINATOR);
    }

    @Override
    public boolean isAutomataStart(int x, int y, int z) {
        x = x + cursor.x;
        y = y + cursor.y;
        z = z + cursor.z;
        if(
               x + WORLD_CENTER >= WORLD_CENTER * 2
            || x + WORLD_CENTER <  0
            || z + WORLD_CENTER >=  WORLD_CENTER * 2
            || z + WORLD_CENTER <  0
            || y + WORLD_CENTER >=  WORLD_CENTER * 2
            || y + WORLD_CENTER <  0
        ){
            return false;
        }
        return fakeWorld[WORLD_CENTER + x][WORLD_CENTER + y][WORLD_CENTER + z].descriptionId.equals(AUTOMATA_START);
    }

    @Override
    public boolean isBedrock(int x, int y, int z) {
        x = x + cursor.x;
        y = y + cursor.y;
        z = z + cursor.z;
        if(
                x + WORLD_CENTER >= WORLD_CENTER * 2
                        || x + WORLD_CENTER <  0
                        || z + WORLD_CENTER >=  WORLD_CENTER * 2
                        || z + WORLD_CENTER <  0
                        || y + WORLD_CENTER >=  WORLD_CENTER * 2
                        || y + WORLD_CENTER <  0
        ){
            return false;
        }
        return fakeWorld[WORLD_CENTER + x][WORLD_CENTER + y][WORLD_CENTER + z].descriptionId.equals(BEDROCK);
    }

    @Override
    public boolean isAutomataPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AUTOMATA_PLACEHOLDER);
    }

    @Override
    public boolean isAirPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AIR_PLACEHOLDER);
    }

    @Override
    public boolean isWaterPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(WATER_PLACEHOLDER);
    }

    @Override
    public boolean isLavaPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(LAVA_PLACEHOLDER);
    }

    @Override
    public boolean isBedrockPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(BEDROCK_PLACEHOLDER);
    }

    @Override
    public boolean isYRotation(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AUTOMATA_Y_ROTATION);
    }

    @Override
    public BlockStateHolder[] surrounding(int x, int y, int z) {
        x = x + cursor.x;
        y = y + cursor.y;
        z = z + cursor.z;
        BlockStateHolder[] result = new BlockStateHolder[27];
        int i = 0;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[i++] = fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER];
                }
            }
        }

        return result;
    }

    @Override
    public BlockStateHolder replacePlaceholder(BlockStateHolder blockState) {
        if(isAirPlaceholder(blockState)) return block(AIR);
        if(isWaterPlaceholder(blockState)) return block(WATER);
        if(isLavaPlaceholder(blockState)) return block(LAVA);
        if(isBedrockPlaceholder(blockState)) return block(BEDROCK);
        return blockState;
    }

    @Override
    public void setBlockAutomata(int x, int y, int z) {
        setAt(x, y, z, AUTOMATA);
    }

    @Override
    public TileEntity getBlockEntity(int x, int y, int z) {
        return entitiesOnPositions.get(Coord.coord(x, y, z));
    }

    @Override
    public void destroyBlock() {
        destroyCalls.add(cursor);
        int x = cursor.x;
        int y = cursor.y;
        int z = cursor.z;
        fakeWorld[x+ WORLD_CENTER][y+ WORLD_CENTER][z+ WORLD_CENTER] = block(AIR);
    }

    public String[][][] getSurroundingIds(int x, int y, int z) {
        String[][][] result = new String[3][3][3];
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[ix+1][iy+1][iz+1] = fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER].descriptionId;
                }
            }
        }
        return result;
    }

    public String getAt(int x, int y, int z) {
        return fakeWorld[x+ WORLD_CENTER][y+ WORLD_CENTER][z+ WORLD_CENTER].descriptionId;
    }

    public ArrayList<Coord> getDestroyCalls() {
        return destroyCalls;
    }
}
