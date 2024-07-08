# Board Game Nurikabe

Repository for a university group project. The aim of the project is to create a working Nurikabe puzzle game. Project members: Karol Babik, Bartosz Jasiński, Jakub Skorupa, Aleksandra Rostkowska

## Project Description

The project involves creating a Nurikabe logic game, which is a popular puzzle game focused on creating paths in a grid of tiles. The game involves identifying which tiles are part of the "black river" (black tiles) and which form "islands" (white tiles) according to specific rules:

1. Each island area must contain exactly one number and must correspond exactly to that number.
2. Black tiles must form one continuous river.
3. There cannot be groups of black tiles larger than 2x2.
4. Each white area (island) must have at least one number and a size equal to that number.

## Game

After starting the programme, the user can generate a new random board via the user interface or load a ready board from a specified file. The board is then displayed on the screen and the player can make moves. At any time during the game, the user can save the game state to a file, or load a previously saved saved game state. The player also has the option to export the board to a PNG file. When the player has finished solving Nurikabe uses the Send Solution button and the programme checks the correctness of the solution and verifies the answer, informing the player of any errors.
