package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockOperations;
import br.com.isageek.automata.forge.BlockStateHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BlockTree {

    private static class Node {
        public final LinkedHashMap<String, Node> nextBlockStateHolder = new LinkedHashMap<>();
        public BlockStateHolder[] result;
    }

    private Node root;

    public static final BlockStateHolder ANY = BlockStateHolder.block("ANY");

    public static final int AUTOMATA_BLOCK_POSITION = 13;

    public void addPatternRotateY(
            BlockStateHolder[] match,
            BlockStateHolder[] result
    ) {
        addPattern(match, result);
        final BlockStateHolder[] match90 = BlockOperations.rotateY(match);
        final BlockStateHolder[] result90 = BlockOperations.rotateY(result);
        addPattern(match90, result90);
        final BlockStateHolder[] match180 = BlockOperations.rotateY(match90);
        final BlockStateHolder[] result180 = BlockOperations.rotateY(result90);
        addPattern(match180, result180);
        final BlockStateHolder[] match270 = BlockOperations.rotateY(match180);
        final BlockStateHolder[] result270 = BlockOperations.rotateY(result180);
        addPattern(match270, result270);
    }

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
                if(current.nextBlockStateHolder.containsKey(ANY.descriptionId)){
                    current = current.nextBlockStateHolder.get(ANY.descriptionId);
                    continue;
                }
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

    private BlockStateHolder[] internalGetReplacementFor(BlockStateHolder[] state, Node current, int index) {
        for (int i = index; i < state.length; i++) {
            if(i == AUTOMATA_BLOCK_POSITION){
                current = current.nextBlockStateHolder.get(ANY.descriptionId);
                continue;
            }

            BlockStateHolder currentState = state[i];
            String matchingBlock = currentState.descriptionId;

            if(current.nextBlockStateHolder.containsKey(matchingBlock)){
                if(current.nextBlockStateHolder.containsKey(ANY.descriptionId)){
                    Set<Map.Entry<String, Node>> entries = current.nextBlockStateHolder.entrySet();
                    for (Map.Entry<String, Node> entry: entries) {
                        String descriptionId = entry.getKey();
                        if(descriptionId == currentState.descriptionId || descriptionId == ANY.descriptionId){
                            BlockStateHolder[] blockStateHolders = internalGetReplacementFor(state, entry.getValue(), i+1);
                            if(blockStateHolders != null) return blockStateHolders;
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

    public BlockStateHolder[] getReplacementFor(BlockStateHolder[] state) {
        return internalGetReplacementFor(state, root, 0);
    }
}
