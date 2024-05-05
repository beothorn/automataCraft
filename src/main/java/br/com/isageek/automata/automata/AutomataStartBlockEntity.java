package br.com.isageek.automata.automata;

import br.com.isageek.automata.automata.states.LoadReplaceables;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.atomic.AtomicReference;

public class AutomataStartBlockEntity extends BlockEntity {

    private EntityTick current;
    private long lastTick = 0;

    public AutomataStartBlockEntity(
        AtomicReference<RegistryObject<BlockEntityType<?>>>  blockEntityType,
        BlockPos blockPos,
        BlockState blockState
    ) {
        super(blockEntityType.get().get(), blockPos, blockState);
        current = new LoadReplaceables();
    }

    public void tick(
            BlockPos blockPos,
            WorldController worldController,
            long now
    ){
        current = current.tick(blockPos, worldController, now - lastTick);
        this.lastTick = now;
    }
}
