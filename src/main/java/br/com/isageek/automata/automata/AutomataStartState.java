package br.com.isageek.automata.automata;

import net.minecraft.util.IStringSerializable;

public enum AutomataStartState implements IStringSerializable {
   LOAD_REPLACEABLES("loadReplaceables"),
   SEARCH("search"),
   LOAD("load"),
   EXECUTE("execute");

   private final String name;

   AutomataStartState(final String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return this.getSerializedName();
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}