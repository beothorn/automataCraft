package br.com.isageek.automata.automata;

import net.minecraft.util.IStringSerializable;

public enum AutomataStartState implements IStringSerializable {
   SEARCH("search"),
   LOAD("load"),
   EXECUTE("execute");

   private final String name;

   AutomataStartState(String p_i231882_3_) {
      this.name = p_i231882_3_;
   }

   public String toString() {
      return this.getSerializedName();
   }

   public String getSerializedName() {
      return this.name;
   }
}