# AutomataCraft

A minecraft mod that adds a block that replaces a 3x3 block pattern with another blocks.

The block is programmable with other blocks by giving two patterns, 
match blocks and result blocks.

This mod adds:

- An automata block
- A start block
- A termination block
- An air placeholder block
- A water placeholder block
- A magma placeholder block
- A bedrock placeholder block
- A automata placeholder block

To use it add a start block, a 3x3 block construction for the result blocks, another 3x3 block construction for the pattern match blocks and another terminator and the automata block.

After creating the pattern, place the automata block and it will load the pattern.

Empty blocks (air) matches any block type.

# Instalation

First install https://files.minecraftforge.net/net/minecraftforge/forge/

Then open the mod folder and copy the jar from releases.

## Example 1: pulls a dirty block one position up

Suppose you want to create an automata that pulls a dirty block one position up and moves one block.

You could do this:

Image goes here
 
Explanation:

First column has the start block. Anything around it is not taken into consideration.

Second has 9 empty (air) blocks. When evaluated this will match any configuration of blocks.

On the third column, the center block is not considered because it will always be an automata block.
The block under the automata is dirt (that is different from grass)

Fourth column has 9 empty (air) blocks. When evaluated this will match any configuration of blocks.

This is the last column from the matcher blocks. If the blocks around the automata matches this, 
it will replace all blocks with the blocks from the result.

Fifth column has 9 empty (air) blocks, this means that whatever blocks are there, they will not be replaced.

Sixth column has a dirt block in the center, this will replace the automata block.

Seventh column has an Automata placeholder. When the automata runs it will add a new automata block at this position.

Then a terminator and up to 20 ignored blocks before the automata block.

# Priority

Closer to the start block has higher priority in case of ambiguity.

# Example: Simple block multiplier

# Example: Tick clock

# Example: Lava door that opens with block

# Redstone activated with pistone

# Example: Many patterns

# Example: Glass corridor downwards

# Example: Maze solver

TODO:

recipes

youtube lets play video