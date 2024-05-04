package br.com.isageek.automata.structures;

import br.com.isageek.automata.AutomataMod;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VanillaStructure extends Structure<NoneFeatureConfiguration> {

    private static final Logger LOGGER = LogManager.getLogger(VanillaStructure.class);
    private final String resourceLocation;

    public VanillaStructure(
            final Codec<NoneFeatureConfiguration> codec,
            final String resourceLocation
    ) {
        super(codec);
        this.resourceLocation = resourceLocation;
    }

    @Override
    public IStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return (
            final Structure<NoneFeatureConfiguration> structureIn,
            final int chunkX,
            final int chunkZ,
            final MutableBoundingBox mutableBoundingBox,
            final int referenceIn,
            final long seedIn
        ) ->  new VanillaStructure.Start(
            structureIn,
            chunkX,
            chunkZ,
            mutableBoundingBox,
            referenceIn,
            seedIn,
                this.resourceLocation
        );
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        private final String resourceLocation;

        Start(
                final Structure<NoFeatureConfig> structureIn,
                final int chunkX,
                final int chunkZ,
                final MutableBoundingBox mutableBoundingBox,
                final int referenceIn,
                final long seedIn,
                final String resourceLocation
        ) {
            super(
                    structureIn,
                    chunkX,
                    chunkZ,
                    mutableBoundingBox,
                    referenceIn,
                    seedIn
            );
            this.resourceLocation = resourceLocation;
        }

        //Registry.STRUCTURE_FEATURE.get(new ResourceLocation(s.toLowerCase(Locale.ROOT)))

        @Override
        public void generatePieces(
                final DynamicRegistries dynamicRegistryManager,
                final ChunkGenerator chunkGenerator,
                final TemplateManager templateManagerIn,
                final int chunkX,
                final int chunkZ,
                final Biome biomeIn,
                final NoFeatureConfig config
        ) {


            this.generatePieces(dynamicRegistryManager, chunkGenerator, templateManagerIn, chunkX, chunkZ, this.resourceLocation);
        }

        private void generatePieces(final DynamicRegistries dynamicRegistryManager, final ChunkGenerator chunkGenerator, final TemplateManager templateManagerIn, final int chunkX, final int chunkZ, final String resourceLocation) {
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            final int x = (chunkX << 4) + 7;
            final int z = (chunkZ << 4) + 7;

            /*
             * We pass this into addPieces to tell it where to generate the structure.
             * If addPieces's last parameter is true, blockpos's Y value is ignored and the
             * structure will spawn at terrain height instead. Set that parameter to false to
             * force the structure to spawn at blockpos's Y value instead. You got options here!
             */
            final BlockPos blockpos = new BlockPos(x, 0, z);

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> {

                        return dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                                // The path to the starting Template Pool JSON file to read.
                                //
                                // Note, this is "structure_tutorial:run_down_house/start_pool" which means
                                // the game will automatically look into the following path for the template pool:
                                // "resources/data/structure_tutorial/worldgen/template_pool/run_down_house/start_pool.json"
                                // This is why your pool files must be in "data/<modid>/worldgen/template_pool/<the path to the pool here>"
                                // because the game automatically will check in worldgen/template_pool for the pools.
                                .get(new ResourceLocation(AutomataMod.MOD_ID, resourceLocation));
                    },

                            // How many pieces outward from center can a recursive jigsaw structure spawn.
                            // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
                            // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
                            // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
                            10),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                    this.pieces, // The list that will be populated with the jigsaw pieces after this method.
                    this.random,
                    false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                    // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                    true);  // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
            // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.

            // Sets the bounds of the structure once you are finished.
            this.calculateBoundingBox();

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            VanillaStructure.LOGGER.log(Level.DEBUG, resourceLocation + " structure at " +
                    this.pieces.get(0).getBoundingBox().x0 + " " +
                    this.pieces.get(0).getBoundingBox().y0 + " " +
                    this.pieces.get(0).getBoundingBox().z0);
        }
    }
}
