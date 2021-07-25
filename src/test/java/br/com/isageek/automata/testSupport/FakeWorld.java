package br.com.isageek.automata.testSupport;

import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.automata.states.LoadReplaceables;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static br.com.isageek.automata.forge.BlockStateHolder.block;

public class FakeWorld extends WorldController {

    private static final String WATER = "Water";
    private static final String LAVA = "Lava";
    private static final String TNT = "tnt";
    public static final String STONE = "stone";
    public static final String AUTOMATA = "Automata";
    public static final String AUTOMATA_START = "AutomataStart";
    public static final String AUTOMATA_PLACEHOLDER = "AutomataPlaceholder";
    public static final String AIR_PLACEHOLDER = "AirPlaceholder";
    private static final String WATER_PLACEHOLDER = "WaterPlaceholder";
    private static final String LAVA_PLACEHOLDER = "LavaPlaceholder";
    private static final String TNT_PLACEHOLDER = "TntPlaceholder";
    private static final String BEDROCK_PLACEHOLDER = "BedrockPlaceholder";
    private static final String AUTOMATA_Y_ROTATION = "AutomataYRotation";
    public static final String TERMINATOR = "Terminator";
    private static final String BEDROCK = "Bedrock";
    public static final String AIR = "air";
    public static final String CAVE_AIR = "caveAir";
    public static final String ANY = "air";

    private final BlockStateHolder[][][] fakeWorld;

    private final boolean[][][] redstoneSignal;

    private final Map<BlockPos, FakeTileEntity> entitiesOnPositions = new LinkedHashMap<>();

    private static final int WORLD_CENTER = 100;

    public FakeWorld() {
        super(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        this.fakeWorld = new BlockStateHolder[WORLD_CENTER * 2][WORLD_CENTER * 2][WORLD_CENTER * 2];
        for (int x = 0; x < WORLD_CENTER * 2; x++) {
            for (int y = 0; y < WORLD_CENTER * 2; y++) {
                for (int z = 0; z < WORLD_CENTER * 2; z++) {
                    this.fakeWorld[x][y][z] = block(AIR);
                }
            }
        }
        this.redstoneSignal = new boolean[WORLD_CENTER * 2][WORLD_CENTER * 2][WORLD_CENTER * 2];
    }

    public void tick(){
        final Set<Map.Entry<BlockPos, FakeTileEntity>> entries = this.entitiesOnPositions.entrySet();
        for (final Map.Entry<BlockPos, FakeTileEntity> entry : entries) {
            final BlockPos coord = entry.getKey();
            final FakeTileEntity automataTileEntity = entry.getValue();
            automataTileEntity.tick(coord, this);
        }
    }

    public void setAt(final BlockPos p, final String id){
        this.setAt(p.getX(), p.getY(), p.getZ(), id);
    }

    public void setAt(final int x, final int y, final int z, final String id){
        this.fakeWorld[WORLD_CENTER +x][WORLD_CENTER +y][WORLD_CENTER +z] = block(id);
        if(id.equals(AUTOMATA_START)){
            final FakeTileEntity automataTileEntity = new FakeTileEntity(new LoadReplaceables());
            this.entitiesOnPositions.put(new BlockPos(x, y, z), automataTileEntity);
        }else{
            this.entitiesOnPositions.remove(new BlockPos(x, y, z));
        }
    }

    @Override
    public void setBlock(final BlockStateHolder blockState, final BlockPos p) {
        this.setAt(p.getX(), p.getY(), p.getZ(), blockState.descriptionId);
    }

    public void setSurrounding(final BlockPos p, final String[][][] surroundingIds){
        this.setSurrounding(p.getX(), p.getY(), p.getZ(), surroundingIds);
    }

    public void setSurrounding(final int x, final int y, final int z, final String[][][] surroundingIds){
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    this.fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER] = block(surroundingIds[ix + 1][iy + 1][iz + 1]);
                }
            }
        }
    }

    @Override
    public boolean isTerminator(final BlockPos p) {
        return this.getAt(p).equals(TERMINATOR);
    }

    @Override
    public boolean isAutomata(final BlockPos p) {
        return this.getAt(p).equals(AUTOMATA);
    }

    @Override
    public boolean isBedrock(final BlockPos p) {
        return this.getAt(p).equals(BEDROCK);
    }

    @Override
    public boolean isAutomataPlaceholder(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AUTOMATA_PLACEHOLDER);
    }

    @Override
    public boolean isAirPlaceholder(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AIR_PLACEHOLDER);
    }

    @Override
    public boolean isWaterPlaceholder(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(WATER_PLACEHOLDER);
    }

    @Override
    public boolean isLavaPlaceholder(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(LAVA_PLACEHOLDER);
    }

    @Override
    public boolean isTntPlaceholder(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(TNT_PLACEHOLDER);
    }

    @Override
    public boolean isBedrockPlaceholder(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(BEDROCK_PLACEHOLDER);
    }

    @Override
    public boolean isYRotation(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AUTOMATA_Y_ROTATION);
    }

    @Override
    public boolean isAny(final BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AIR) || blockStateHolder.descriptionId.equals(CAVE_AIR);
    }

    @Override
    public BlockStateHolder[] surrounding(final BlockPos center) {
        final int x = center.getX();
        final int y = center.getY();
        final int z = center.getZ();
        final BlockStateHolder[] result = new BlockStateHolder[27];
        int i = 0;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[i++] = this.fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER];
                }
            }
        }

        return result;
    }

    @Override
    public BlockStateHolder replacePlaceholder(final BlockStateHolder blockState) {
        if(this.isAirPlaceholder(blockState)) {
            return block(AIR);
        }
        if(this.isWaterPlaceholder(blockState)) {
            return block(WATER);
        }
        if(this.isLavaPlaceholder(blockState)) {
            return block(LAVA);
        }
        if(this.isTntPlaceholder(blockState)) {
            return block(TNT);
        }
        if(this.isBedrockPlaceholder(blockState)) {
            return block(BEDROCK);
        }
        if(this.isAutomataPlaceholder(blockState)) {
            return block(AUTOMATA);
        }
        return blockState;
    }

    public String[][][] getSurroundingIds(final BlockPos p) {
        return this.getSurroundingIds(p.getX(), p.getY(), p.getZ());
    }

    public String[][][] getSurroundingIds(final int x, final int y, final int z) {
        final String[][][] result = new String[3][3][3];
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[ix+1][iy+1][iz+1] = this.fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER].descriptionId;
                }
            }
        }
        return result;
    }

    private String getAt(final BlockPos p) {
        return this.getAt(p.getX(), p.getY(), p.getZ());
    }

    public String getAt(final int x, final int y, final int z) {
        if(
                x <= -WORLD_CENTER
                || x >= WORLD_CENTER
                || y <= -WORLD_CENTER
                || y >= WORLD_CENTER
                || z <= -WORLD_CENTER
                || z >= WORLD_CENTER
        ) {
            return AIR;
        }
        return this.fakeWorld[x+ WORLD_CENTER][y+ WORLD_CENTER][z+ WORLD_CENTER].descriptionId;
    }

    public void redSignalAt(final BlockPos p, final boolean signalState) {
        this.redSignalAt(p.getX(), p.getY(), p.getZ(), signalState);
    }

    public void redSignalAt(final int x, final int y, final int z, final boolean signalState) {
        this.redstoneSignal[WORLD_CENTER + x][WORLD_CENTER + y][WORLD_CENTER + z] = signalState;
    }

    @Override
    public boolean hasNeighborSignal(final BlockPos p) {
        return this.redstoneSignal[WORLD_CENTER + p.getX()][WORLD_CENTER + p.getY()][WORLD_CENTER + p.getZ()];
    }

    @Override
    public BlockStateHolder getAutomata() { return block(AUTOMATA); }
    public static BlockStateHolder getAir() { return block(AIR); }

    @Override
    public void setStateAt(final BlockPos pos, final AutomataStartState state){
    }

    @Override
    public BlockStateHolder getBlockStateHolderAt(final BlockPos pos) {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();
        return this.fakeWorld[x+ WORLD_CENTER][y+ WORLD_CENTER][z+ WORLD_CENTER];
    }

    @Override
    public boolean is(final BlockPos p, final BlockStateHolder blockStateHolder) {
        return this.getAt(p).equals(blockStateHolder.descriptionId);
    }
}
