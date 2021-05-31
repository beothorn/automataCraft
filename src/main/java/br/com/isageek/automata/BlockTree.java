package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;

import java.util.LinkedHashMap;
import java.util.Set;

public class BlockTree {

    private static class Node {
        public final LinkedHashMap<String, Node> nextBlockStateHolder = new LinkedHashMap<>();
        public BlockStateHolder[] result;
    }

    private Node root;


    public static final BlockStateHolder ANY = BlockStateHolder.b("ANY");

    private static final int AUTOMATA_BLOCK_POSITION = 13;

    public void addPattern(
        BlockStateHolder[] match,
        BlockStateHolder[] result
    ) {
        if(root == null){
            root = new Node();
        }
        Node current = root;
        for (int i = 0; i < match.length; i++) {
            if(i == AUTOMATA_BLOCK_POSITION){
                Node value = new Node();
                current.nextBlockStateHolder.put(ANY.descriptionId, value);
                current = value;
                continue;
            }
            BlockStateHolder currentState = match[i];
            if(current.nextBlockStateHolder.containsKey(currentState.descriptionId)){
                current = current.nextBlockStateHolder.get(currentState.descriptionId);
            }else{
                Node value = new Node();
                current.nextBlockStateHolder.put(currentState.descriptionId, value);
                current = value;
            }
        }
        current.result = result;
    }

    public BlockStateHolder[] getReplacementFor(BlockStateHolder[] state) {
        Node current = root;
        for (int i = 0; i < state.length; i++) {
            if(i == AUTOMATA_BLOCK_POSITION){
                current = current.nextBlockStateHolder.get(ANY.descriptionId);
                continue;
            }

            BlockStateHolder currentState = state[i];
            String matchingBlock = currentState.descriptionId;

            if(current.nextBlockStateHolder.containsKey(matchingBlock)){
                if(current.nextBlockStateHolder.containsKey(ANY.descriptionId)){
                    Set<String> values = current.nextBlockStateHolder.keySet();
                    for (String s: values) {
                        if(s == currentState.descriptionId || s == ANY.descriptionId){
                            matchingBlock = s;
                            break;
                        }
                    }
                }
            }else{
                if(current.nextBlockStateHolder.containsKey(ANY.descriptionId)){
                    matchingBlock = ANY.descriptionId;
                }else{
                    return null;
                }
            }

            current = current.nextBlockStateHolder.get(matchingBlock);
        }
        return current.result;
    }
}
