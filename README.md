# ella-agi

A tentative attempt to implement Ella Hoeppner's [proposal](https://docs.google.com/document/d/1s2HT3UNAr9PaddbmHy7n2_BXp6bouWHcRMLcpyWFbuA/edit) for an AGI.

## Installation

First, make sure you have Leiningen [installed](https://leiningen.org/#install). You will need it to run and/or build the code.

## Usage

In the project's root, run

    $ lein repl

This will load up the project's repl. Then, to load the main namespace:

    > (defn reload [] (use 'ella-agi.core :reload-all))
    > (reload)

(You can also use this function to load any changes you make).

Then, run

    > (-main)

to invoke the app's main function. This will generate a bunch of theories and claims and find problems. It returns a set of sets of conflicting claims.

You can inspect the created claims and their lineages by typing

    > @claims

That probably prints a whole lot of stuff, so pretty printing it may be helpful:

    > (pprint @claims)

The more you run `(-main)`, the bigger the collection of `@claims` will get, and the more problems you will find.

## Todo

Problem solving

## License

Copyright © 2019 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
