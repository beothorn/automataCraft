# Voxsophon

## Idea

A minecraft mod that adds a block that displaces other blocks.

The block is programmable with other blocks by giving two patterns, previous state and nex state.

Suppose you want to create a voxsophon that pulls a dirty block one position up.

To code this into the voxsophon you start with a redstone block, then the Voxsophon
and then add 6 blocks representing the previous state and 7 for the next state.

After that the terminator block.

The Voxsophon never moves when in contact with a redstone block. 

Onm the pattern, a block of Redstone is the Voxsophon and a 
block of Lapis Lazuli matches any block.

The order of the blocks is:

For the previous state

UP DOWN LEFT RIGHT BACK FRONT

and then the next state

UP CENTER DOWN LEFT RIGHT BACK FRONT

The amount of blocks of one type on the previous state must be the same as the next state.

So for example, if you want to make the voxsophon pull one dirt block up 
the y axis you would have:

🟠 : Voxsophon block

🟥 : Block of Redstone

🟦 : Block of Lapis Lazuli

🟨 : air

🟪 : crafting table

🟫 : dirt

```
🟥      🟠         🟨 🟫  🟦   🟦   🟦   🟦     🟨 🟥    🟫   🟦  🟦    🟦  🟦      🟪
Start | Voxsophon | UP DOWN LEFT RIGHT BACK FRONT | UP CENTER DOWN LEFT RIGHT BACK FRONT | Terminator
            ^Previous                       ^Next
```
  
Explanation:

First block is a Redstone block. It is the first for two reasons. It gives the direction
of the pattern and it holds the Voxsophon in place.

Second one is the Voxsophon. If the pattern is correct it will change textures to show
it is loaded.

First 6 blocks describes one possible configuration of blocks.

It is understood that the Voxsophon is at the center.

We only care if the block above is free (air) and the one below is dirt.

So up is air, down is dirt, left right back and front are Lapis Lazuli, wich means any.

After that comes 7 blocks describing what should be the next configuration after one tick.

It is 7 because the center block now is included.

On the next configuration, we just swap air and dirt from the up and down positions.

Up is dirt, center is the Voxsophon, down is air and we use "any" for the rest.

The amount of blocks in must be the amount out, so one air, one dirt and 
one Voxsophon.

More than one pattern can be added after the first one.

The last block is the termination block (a crafting table).

If you want more than one pattern just add the next sequence and then the terminator at the end.