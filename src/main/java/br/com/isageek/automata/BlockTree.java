package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockOperations;
import br.com.isageek.automata.forge.BlockStateHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BlockTree {

    void clear() {
        this.root = null;
    }

    private static class Node {
        final LinkedHashMap<String, Node> nextBlockStateHolder = new LinkedHashMap<>();
        public BlockStateHolder[] result;
    }

    private Node root;

    public static final BlockStateHolder ANY = BlockStateHolder.block("ANY");

    public void addPatternRotateY(
            final BlockStateHolder[] match,
            final BlockStateHolder[] result
    ) {
        this.addPattern(match, result);
        final BlockStateHolder[] match90 = BlockOperations.rotateY(match);
        final BlockStateHolder[] result90 = BlockOperations.rotateY(result);
        this.addPattern(match90, result90);
        final BlockStateHolder[] match180 = BlockOperations.rotateY(match90);
        final BlockStateHolder[] result180 = BlockOperations.rotateY(result90);
        this.addPattern(match180, result180);
        final BlockStateHolder[] match270 = BlockOperations.rotateY(match180);
        final BlockStateHolder[] result270 = BlockOperations.rotateY(result180);
        this.addPattern(match270, result270);
    }

    public void addPattern(
            final BlockStateHolder[] match,
            final BlockStateHolder[] result
    ) {
        if(this.root == null){
            this.root = new Node();
        }
        Node current = this.root;
        for (int i = 0; i < match.length; i++) {
            final BlockStateHolder currentState = match[i];
            if(current.nextBlockStateHolder.containsKey(currentState.descriptionId)){
                current = current.nextBlockStateHolder.get(currentState.descriptionId);
            }else{
                final Node value = new Node();
                current.nextBlockStateHolder.put(currentState.descriptionId, value);
                current = value;
            }
        }
        current.result = result;
    }

    private static BlockStateHolder[] internalGetReplacementFor(final BlockStateHolder[] state, Node current, final int index) {
        for (int i = index; i < state.length; i++) {
            final BlockStateHolder currentState = state[i];
            String matchingBlock = currentState.descriptionId;

            if(current.nextBlockStateHolder.containsKey(matchingBlock)){
                if(current.nextBlockStateHolder.containsKey(ANY.descriptionId)){
                    final Set<Map.Entry<String, Node>> entries = current.nextBlockStateHolder.entrySet();
                    for (final Map.Entry<String, Node> entry: entries) {
                        final String descriptionId = entry.getKey();
                        if(descriptionId.equals(currentState.descriptionId) || descriptionId.equals(ANY.descriptionId)){
                            final BlockStateHolder[] blockStateHolders = internalGetReplacementFor(state, entry.getValue(), i+1);
                            if(blockStateHolders != null) {
                                return blockStateHolders;
                            }
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

    public BlockStateHolder[] getReplacementFor(final BlockStateHolder[] state) {
        if(this.root == null){
            return null;
        }
        return BlockTree.internalGetReplacementFor(state, this.root, 0);
    }

    private static void printOn(final StringBuffer a, final Node n){
        final Set<Map.Entry<String, Node>> entries = n.nextBlockStateHolder.entrySet();
            entries.forEach( (e) -> {
                a.append(" -> " +e.getKey());
                a.append("\n\t");
                printOn( a, e.getValue());
            }
        );
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer();
        BlockTree.printOn(stringBuffer, this.root);
        return stringBuffer.toString();
    }
}
