# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=FAQwxgLg9gTgBAYQDYEsCmA7CwAOIYQpgp5ZwDKaMAblbvocaRHABIgYAmSdeBRJDi0o0iaevyZC4AERAQQAQTBg0AZzXBO8kACMQatHG0L9h4MADmMKAFcccAMTW0mOACU0llGogx5KFAYcAAKNqoaTgDuABYoEOLI6GQAtAB8FFS0MABccADetoYwGCAAtmgANHgaUbCclXBoZSAoSAC+wCLZ6Wwc3FR5hcWlFdUGanUwDU0tbZ3sXDzwvd1ieTBePgkwABSb3r5UcJsAjrbqEACUXVlicL1yCsoRanmWaBAAqsW7RVQ3J5KFTqNQPDImPQGNB5cgAUQAMnCEAAVOD-ErlIwAMxsZXRxWAQJeoIeqzuqjyGFsSCQt1EqnBsh0JI0eTAm3kaB+VD+IyxjRqk3qjWarSQgJZIMivUhZhhcAAkgA5eHuNEY0ZVOBCqYzMVtJXKlEAeQJdDWjMeUte7M5CUUtggMT5VC1kue0rBsp08ryKrVGv5YzgICdMRRUAA1m4VabQ+GiTbSSlyQyFWHnZGYxh6dR7qmMosBrkPFsjjBPGoadhi8syRluoMCpqBQms9HMJ0myteklMBA8gAmAAMI4AOhhhm625mI52MJ1gJhOBYLNY7A5HJtOHAEVBvMEwlBXtE4glgP3Uo2ss3p5iQ7r6t3bysi-1lkNW4+JnqFh-jjTfNKTgJADxQDBdjAw9PHOS4bktIxrU9W04A+b5igAIQATx5B80FdfCPWBU8fVMaFYURZEgxnH9anqOAADF3BNABZc14AAdVYOF3DhDitTgABeOAAH4kxQlMgPWDi4QAD22TQQCQFgMXkxTgDgLTMnTJliS9O00C5R1nUI91NO0-TSIhX0KKNQMBNncNs1jY0zTnCytKsqSb3TPI5xc3NtJ04CkN6Otm2giCqxrTy+iWQCgObUcJynb9tQChdOmCnsG0QVAB2HMdJ3vLVGkynNOjQJBDA4uAMCgFhsTsLg4sQvKItLOEYBsSt1BwIJzGCzq8p7PIABYRwARhKioNBAD48gAIjw+rGrgZrbC4Jbsu03LC3y5JBzgSaZqnOa1AWhUVuKNampazgduXVqVzXNcN3sJxdCQC44AAKSgCC4AAcSxUJwlJRxYniRICuvEK7wqzBGksLFFRkQUkBAbCqAQKAwJgF8aEA98EtLfIkYwFG0YxnUsZxmA8YJ-8yaZRC8gAK0BjBQYqXYuYg3m0Fgi5fAQikkIyby2TgahlJQEw0BM+cc12SniNZb0bPIwxKKRVFHIqJiWPYucAH1oBzMFuN4-jLbcETxOl71pJA9KADV5dXZTVODNBPdQVccolvTkxlsAYjQMAoyFxQ5baPQ2nibDdlRip0cx7Hcfx2AbmC52mTlOz4X1tEACpjbYtCsWtni+Or9OZGEsS4oLg72YbpX46x77xGCn3O7j8U9B4OL87DrXjFs3W4C+EIZEUFF+LT9QKDhNEcHp7OCbN9Lm73m365X9Hm-E4Lx8kmVXYVFevhwRWg-PrT2oOzq8mrL0qwGjAhqf+KS1Gq+IqqV8i7SfvtPscNjopRKmApoNUjArzuqGbuI8+5-xfuFACXUeqwC-oNdBT8RrtyASdEcABmWaoIrrLSFhtGkSA4CwDgGAHO8AFA5ien-BGvYMhXmOpNSh51qGLTgEtOh2IGFMPgKwgmcAOGYC4dpV61Var22CBBOWgc2oh1ftgvI3Ver4J-oQ-+9YSHE1LKdKh81RErVKOGWAKAABeaBHpwIgXwqBE1po2MunYr4DjnRONce456q5XrrhsJ9RwONaRQCiHuA8dgWDHlPI4b64AoyXigWzUhFNnJZTzCTMxiNCmVWAMQ3yoU8jQRSWrcpmBxa6WQiRUEeRNFe2Mo0yC6sJJtJlNrKEM8ZBUSXpXU24YLYLlrrbeRC5T79M1oAvy7YVaYEVBgLRCsuTexUmswKmztkPx0S0rBZN362E-v1AhcViFJVLDAqcHjXx5X4cA2By4EHzJzHATpgcAhBA2uKNxpzQodX0XAQxeCbkmLudglZ2Q8gAFZirCNsddRiILdzQD+VsrpCQfmKJeZYt53i4CopARdGhYisVtDcfMvFxyuREowE9FRrU3pWGiVuWAHAPh7m2CDGu4MTyQ2hhefheTLFDEpkTbITI34FDlZUhF19anbCFmoBpHYczNPBa0zWeRmUOh6Tq9ZGANZekLtPBUJdqKGyMMxKu5t1GzPruoxZbd1UHIXAHBWoB9mU39Y-LyE8bU6ztVRA2FdnXsRXpoC+AyXbVJkgmhE2wwUFnOSWd4NdjG-y0vc6pyU0X5HTdsElCqDrvLgE88tNcM2+Cqt8z1-yA3BzOaTXNULcF9TUN-QtpTeE8J8WdfI1KAlBJiCEtxSjn6vJreS6x6L-HXUCXOWdYSOWri5R9LcOBbAwE3kYBA9ojB0LSeK88sMjrSqRcqnp1MKjKixPKkpSqCm6uRp3V9FQWYAJ9RyIyCQha7GA1yIWIt4J5gLFLCexqCVKzNX0tuQy-Rr1Lo6iZazplWzgIfO2CzHZLOtRYmpjqQ2Bt9rRf2Xss1Wng5fN4LDz3KjQFEMDK8-3anSnnSy4ayLDIVAGXiaI4xmgTXAVOWIeONAgwkTgOErhwHdooBEXw4TkGk6JRool+NhuY4itNaNQ0hWzd2z8iBz1QdhUO4to661luPjIPN6dOBVsSl4o6Hypwubc2gRUHmvlqIWe2szmDLPNmhf2wdpiHNjTIeOyd67p1bvnTwslPmkt+JpfYzdMAXFzqqi9Tl+6nAJGUvlIy8BFA4E3kQQFR4IaRChjenJd6Hmyp6e+t8w7uvfsXKq1mQGeD4Dqw1sATXzWBX1XB5kzHEMAtNYNmbC4rXWSnpGvWDq95xtw26gjdciO-JI961NIFg30YHld7RnaDVMeTeyMbMBFC0iBLsAzC3k0RuE3kUZSJxn7YxGoAA3LIMZ-F9sJvBwD9eUOTYHZma3QTPrITIBq6C+7Fn+vVfwOQK5rwC3xbVV1pzVKRHXTe4wyELCXvFbBV5w6hVye5bsdTrbIA6eY+3a2sL+K7t7V0TmqzMXifwpG2TldE7KfLQ3Y4wroSMueOZ1gMdbPUsFaK9u0ru6uX66AA
```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
