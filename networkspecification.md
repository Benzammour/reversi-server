# Network specification

The Server uses a socket connection to communicate with the clients, there is a certain [message structure](#message-structure) and [protocol](#protocol) in place to ensure correct communication between both parties

## Message structure
The messages have a certain structure:  
`Type (8-Bit-Integer) | Length of the message n (32-Bit-Integer) | Message data (n bytes)`

Following message types have been defined:
- **1**: sends map to client as a string
- **2**: assigns the client a certain player number as an _8-bit integer_
- **3**: requests a move from player: it's a _32-bit integer_ indicating the timelimit (in ms) followed by an _8-bit integer_ that indicates the max. depth limit (0 means unlimited depth limit)
- **4**: responses to server with a move: x and y coordinates as _16-bit integer_ (each one)
- **5**: announces move made by client to every client: same fields as **4** with additional _8-bit integer_ to indicate the player that made the move 
- **6**: announces disqualification of player: _8-bit integer_ indicating disqualified player
- **7**: announces that game has ended: has no contents

## Protocol

1. Client connects to server using a TCP socket connection.
2. When all clients are connected the server sends them the map (**1**)
3. Sends all clients their player ID/number (**2**)
4. Throughout game server and client exchange messages of type **3** through **6**
5. Game Ends (**7**)