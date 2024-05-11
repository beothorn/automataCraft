package br.com.isageek.automata.structures;

import net.minecraft.world.level.levelgen.structure.Structure;

public abstract class NamedStructure extends Structure{

    protected NamedStructure(StructureSettings p_226558_) {
        super(p_226558_);
    }

    public abstract String name();

}
