# reversi-server

This projects acts as a platform to play the game reversi (or othello), though this version is slightly extended (it uses the concept of holes, check [rules](RULES.md) to learn more). The motiviation behind this is to create a platform which enables people to write AIs and compete with them against each other.

## Getting Started

1. Get Java and Ant
2. Clone this repository:
	* ssh: `git clone git@github.com:Benzammour/reversi-server.git`
	* https: `git clone https://github.com/Benzammour/reversi-server.git`
3. Build project with `ant jar`
4. Go into directory `bin/jar`
5. Run jar with `java -jar reversi-server.jar [options]`

## Write a Client

If you want to write a client for this server I suggest you make yourself familiar with the [rules](RULES.md) of the game and read the [network specification](networkspecification.md).

## Contributing

Please don't submit PRs to the `master` but instead to the `dev` branch.

## Authors

* **Samir Benzammour** - [benzammour](https://github.com/benzammour)

See also the list of [contributors](https://github.com/Benzammour/reversi-server/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgements

* **i2 Chair**  - [Website](https://moves.rwth-aachen.de/)

## Footnote

This project is based on a lab done in university in which we developed AIs to play against each other. However, we used an extremely extended version of the game - which this game is not. I kept this fairly simple (with the extension of hole, see [rules](RULES.md) for more) to enable people to jump right into developing the client without having to read a million rules beforehand.
