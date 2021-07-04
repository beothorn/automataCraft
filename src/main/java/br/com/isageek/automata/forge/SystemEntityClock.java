package br.com.isageek.automata.forge;

public class SystemEntityClock implements EntityClock{
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
