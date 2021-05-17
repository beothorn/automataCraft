# Voxsophon

## Idea

Not sure if this is possible.

A minecraft mod that adds a block that displaces other blocks.

The block is programmable with other blocks by giving two patterns, previous state and nex state.

Suppose you want to create a voxsophon that pulls a dirty block one position up.

To code this into the voxsophon you start with an obsidian block and 
add two blocks to represent aa any block matcher and the voxsophon itself.

After that you add 6 blocks for the previous state and 7 for the next state.

The order of the blocks is
Obsidian one y position up
Block Any
Block Voxsophon
the previous state
UP DOWN LEFT RIGHT BACK FRONT
and then the next state
UP CENTER DOWN LEFT RIGHT BACK FRONT
Obsidian

The amount of blocks on the previous state must be the same as the next state.

So for example, if you want to make the voxsophon pull a dirt block up 
the y axis you would have:

A: air
O: obsidian
D: dirt
X: a different block
Y: another different block

```
  v place voxsophon here
O               O
 XYADXXXXDYXXXXX
```
  
Explanation:
First x is a block to represent any block, second y is a block to represent the voxsophon.
None of them is dirt or air.

Then on the previous state the block above (UP) the voxsophon is air
A

The one bellow is dirt

D

and we don´t care about the rest

XXXX

Then starts the next state, remember 
the amount of blocks on the previous state must match the previous state.

We want dirt to be up and the voxsophon to stay in center.

DYXXXXX

