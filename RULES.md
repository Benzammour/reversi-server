# Rules

## Basic rules
- Reversi pieces usually have a black and a white side (in this version represented with `1` and `2`). 
- On your turn, you place one piece on the board with your color (i.e. `1` or `2`) facing up
	* You have to place the piece so that an opponent's piece is flanked by your pieces
- All of the opponent's pieces between your pieces are then turned over to become your color.

### Aim of the game
The object of the game is to own more pieces than your opponent when the game is over. The game is over when neither player has a move. Usually, this means the board is full.

## Extended rules
In this version of reversi we use custom maps to play the game, how they are built and what they look like is explained [here](#board-structure). However, besides free and occupied tiles there are _holes_, those represent tiles which neither player can capture nor move on. This adds a new level of complexity to the game, due to the sheer variety of maps available.

### Board structure

The board starts with two integers separated by an space which represent the width and height of the map. The next lines represent the map layout where `0` represents a free tile, `1` and `2` represent player tiles (where they start out), and `-` represents a [hole](#extended-rules) in the map. The biggest a map can be in either direction is 50 tiles, so the biggest map would be a 50x50 map.

An example of a map:

```
6 6
0 0 0 0 0 0
0 0 0 0 0 0
0 0 1 2 0 0
0 0 2 1 0 0
0 0 0 0 0 0
0 0 0 0 0 0
```