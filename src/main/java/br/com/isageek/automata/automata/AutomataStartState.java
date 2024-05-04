package br.com.isageek.automata.automata;

import net.minecraft.util.StringRepresentable;

public enum AutomataStartState implements StringRepresentable {
   LOAD_REPLACEABLES("loadreplaceables"),
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