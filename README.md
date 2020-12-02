# PM1-IT20taZH-berp-bles-zubj-gruppe3-fischbein-projekt3-catan
Project catan from group fischbein is a multiplayer game. It is based on the original *Siedler von Catan* board game.
It implements a game with 2-4 players where the players play against each other. 
They have the original board with 19 fields to build different structures.
Players can build roads, settlements and citie, where settlements and cities donate points to the players. The first player who reaches 7 points wins the game.

## Siedler Game
### Start
To start a new game, aou have to start the main-method in the class gameController. 
#### Initialize
If you do so, a new window pops out and you are asked, how many players want to play. 
Please enter there a number between 2 and 4. If you have entered the right number, the game starts
and prints out your game board. You can't choose a faction, the game sets it automatically for you.

#### Phase 1: Foundation Phase
##### First Settlement
One after another, the players can set their first settlement at a freely selectable position. The only regulations there are:
* The choosen point has to be a corner of a field on the game board.
* There has to be at least one adjacent resource field around the corner.
* It is necessary, that on the three adjacent corners of the corner, where the player wants to build his settlement, is no other settlement already build. 

They then need to build a road from their settlement to an adjacent corner. The street needs to be directly connected with the settlement and the edge, where
the player wants to build his street needs to be free.
##### Second Settlement
When the last player has build his first settlement, he can directly set his second settlement. It then goes the other way around, until every person
has build two settlements and two roads. The players gain directly after they've build there second settlement resource cards for the adjacent resource fields of the second settlement. The building regulations stay the same as with the first settlement.

#### Phase 2: Playing Phase
After the foundation phase, each player can follow the float chart below for when it's his turn.
The window, where you can enter your commands will lead you through the game. It shows you every time aour available commands and tells you, if your
input is invalid and what you have to correct, to create a valid command.

![Playing Phases](doc/GameDiagramm.png)

## Class Diagramm
Below, you can find the class diagramm for the siedler project. It shows you all our classes and their datafields and methods.

![Class Diagramm](doc/Klassendiagramm.png)

## Test Protocol
Below, you can find the equivalence partitionings for the siedlerGame class. More informations can be found in the pdf.

Negative
1. Illegal placement position
3. Build with not enough resources
8. Trade where player does not have enough resources
9. Trade where bank does not have enough resources
10. No player has won game
13. Robbery no player has more than 7 resources
17. Thief placement at field with no settlements around
20. Resources are payed out
21. No Resources are payed out
23. player has no structures left to build

Positive
2. Legal placement position
4. Build with enough resources
7. Trade where player and bank have enough resources
11. One player has won game
12. More than one player has won game
14. Robbery one player has more than 7 resources
15. Robbery more than one player has 7 resources
18. Thief placement at field with one settlement around
22. Player structures left to build

