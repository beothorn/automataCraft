package br.com.isageek.automata.structures;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Map;
import java.util.Optional;

public class Rule30 extends NamedStructure {

    private static BootstapContext<Structure> p_256072_;

    public Rule30() {
        super(new Structure.StructureSettings(
                p_256072_.lookup(Registries.BIOME).getOrThrow(BiomeTags.HAS_VILLAGE_PLAINS),
                Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.NONE
        ));
    }

    public Rule30(StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    public StructureType<?> type() {
        return () -> simpleCodec(Rule30::new);
    }

    @Override
    public String name() {
        return "r30";
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenRandom.setLargeFeatureSeed(
            context.seed(),
            context.chunkPos().x,
            context.chunkPos().z
        );
        if (!worldgenRandom.nextBoolean()) {
            return Optional.empty();
        }

        return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, (structurePiecesBuilder) -> {
            generatePieces(structurePiecesBuilder, context);
        });
    }

    private static void generatePieces(
        final StructurePiecesBuilder piecesBuilder,
        final GenerationContext context
    ) {

    }
}


