nimSym3.java calculates P-positions for games in which the rules are symmetric; that is, if the moveset contains a move in which one can remove stones from k piles, it must contain all (n choose k) moves for which one can remove stones from k piles. The symmetry here is used to speed up the calculation, as we only need to consider the case in which the piles are in ascending order.

nimAsym2.java calculates P-positions for games with any moveset, not necessarily a symmetric one. The program runs slower, as it must consider every possible pile in the worst case.

OneorNFullSpace.java calculates P-positions for the following game: at every step, one can either remove stones from one pile, or one can remove the same number of stones from all non-empty piles, which means that the moveset changes based on the number of non-empty piles remaining.

Each of the folders contains a list of P-positions up to a particular bound (indicated in the file name) for a given game.