# reversi-server

This project is based on a lab done in university in which we developed AIs to play against each other. We however used an extended version of reversi but this is the standard reversi with the official rules.

## Network Specification

- 1: contains map
- 2: assigns player number
- 3: request move from player
- 4: response with move
- 5: announces move to every client
- 6: announces disqualification of player
- 7: announces that game has ended
