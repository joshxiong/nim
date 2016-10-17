import java.io.*;
import java.util.*;

public class nimSym3 {
	// for calculating P positions of a game that is symmetrical in P positions
	// i.e. All the permutation of a P position is P position
	// In this case, sub positional space based on symmetry is used to speed up
	// the calculation

	public static final String GAME = "NimGen";
	public static final boolean isMisere = false;
	public static final String fileNameInputPP="";
//	public static final String fileNameInputPP = GAME + "\\ini.txt";
//	public static final String fileNameInputPP = GAME + "\\60-60-60_" + GAME + "_23.txt";
//	public static final String fileNameInputPP = GAME + "\\1-300-300_" + GAME + "_sorted.txt";
//	set array of maximum value of each pile
	public static final int[] MAX_VALUES = {60,60,60};
	public static final int[] MAX_VALUES_LIST = setMax();
	public static final ArrayList<boolean[]> gameRules = setRules();

	public static ArrayList<boolean[]> setRules() {
		ArrayList<boolean[]> gameRules = new ArrayList<boolean[]>();
		// set up game rules
//		gameRules.add(new boolean[] { true, false, false, false });
//		gameRules.add(new boolean[] { false, true, false, false });
//		gameRules.add(new boolean[] { false, false, true, false });
//		gameRules.add(new boolean[] { false, false, false, true });
//		gameRules.add(new boolean[] { true, true, false, false });
//		gameRules.add(new boolean[] { true, false, true, false });
//		gameRules.add(new boolean[] { true, false, false, true });
//		gameRules.add(new boolean[] { false, true, true, false });
//		gameRules.add(new boolean[] { false, true, false, true });
//		gameRules.add(new boolean[] { false, false, true, true });
//		gameRules.add(new boolean[] {true, true, true, false});
//		gameRules.add(new boolean[] {true, true, false, true});
//		gameRules.add(new boolean[] {true, false, true, true});
//		gameRules.add(new boolean[] {false, true, true, true});
//		gameRules.add(new boolean[] {true, true, true, true});
		
		gameRules.add(new boolean[] {true, false, false});
		gameRules.add(new boolean[] {false, true, false});
		gameRules.add(new boolean[] {false, false, true});
//		gameRules.add(new boolean[] {true, true, false});
//		gameRules.add(new boolean[] {true, false, true});
//		gameRules.add(new boolean[] {false, true, true});
//		gameRules.add(new boolean[] {true, true, true});
		return gameRules;
	}

	public static int[] setMax() {
		int[] max = new int[MAX_VALUES.length];
		for (int i = 0; i < MAX_VALUES.length; i++)
			max[i] = MAX_VALUES[i] + 1;
		return max;
	}

	public static void union(ArrayList<int[]> positionList1,
			ArrayList<int[]> positionList2, boolean removeDuplicate) {
		boolean skip = false;

		if (removeDuplicate) {
			for (int[] position2 : positionList2) {
				skip = false;
				for (int[] position1 : positionList1)
					if (Arrays.equals(position2, position1)) {
						skip = true;
						break;
					}
				if (!skip)
					positionList1.add(position2);
			}
		} else {
			for (int[] position : positionList2)
				positionList1.add(position);
		}
	}

	public static int belongToList(int[] position, ArrayList<int[]> list,
			int index) {
		int i = index;

		for (int k = 0; k < i; k++) {
			if (Arrays.equals(position, list.get(k))) {
				i = k; // location of position in list
				break;
			} // the index will be retained, if position is not in list
		}
		return i;
	}

	public static ArrayList<int[]> addAllMoves(ArrayList<int[]> ppositionList,
			ArrayList<int[]> unknownList, ArrayList<boolean[]> moveRules,
			int step) {

		ArrayList<int[]> allMoves = new ArrayList<int[]>();
		int counter = 1;

		for (int[] position : ppositionList) {

			if (counter == 1) {
				allMoves = addAllMovesToPosition(position, unknownList,
						moveRules, step);
				counter++;
			} else {
				ArrayList<int[]> possibleMovesFromPosition = addAllMovesToPosition(
						position, unknownList, moveRules, step);
				union(allMoves, possibleMovesFromPosition, false);
				counter++;
			}
		}
		return allMoves;
	}
	
	public static ArrayList<int[]> addAllMoves(ArrayList<int[]> ppositionList,
			ArrayList<int[]> unknownList, ArrayList<boolean[]> moveRules,
			int step, boolean b) {

		ArrayList<int[]> allMoves = new ArrayList<int[]>();
		int counter = 1, mult200 = 1;
		for (int[] position : ppositionList) {
			if (counter == 1) {
				allMoves = addAllMovesToPosition(position, unknownList,
						moveRules, step);
				counter++;
			} else {
				ArrayList<int[]> possibleMovesFromPosition = addAllMovesToPosition(
						position, unknownList, moveRules, step);
				union(allMoves, possibleMovesFromPosition, false);
				if (counter == 200 * mult200) {
					System.out.println("    Finished setting NPositions & UPositions"
							+ " from first " + (200 * mult200) + " PPositions");
					mult200++;
				}
				counter++;
			}
		}
		return allMoves;
	}

	public static int updateUnknownList(ArrayList<int[]> unknownList,
			int[] position, int index) {
		int i = index;
		for (int k = i + 1; k < unknownList.size(); k++) {
			if (Arrays.equals(position, unknownList.get(k))) {
				unknownList.remove(k);
				i = k; // update new starting point for next loop
				break;
			} // the index will be retained, if position is not in unknownList
				// (i.e. in NPositionList)
		}
		return i;
	}

	public static ArrayList<int[]> addAllMovesToPosition(int[] position,
			ArrayList<int[]> unknownList, ArrayList<boolean[]> moveRules,
			int step) {
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		int[] move;
		int i, j, k, index;
		boolean outBounds = false;
		// remove PPosition from unknown
		for (k = 0; k < unknownList.size(); k++) {
			if (Arrays.equals(position, unknownList.get(k))) {
				unknownList.remove(k);
				k--;
				break;
			}
		}

		// generate all moves using moveRules
		for (boolean[] rule : moveRules) {
			move = position.clone();
			index = k; // starting loop position for checking unknownList:
						// index+1
			while (!outBounds) {
				for (i = 0; i < MAX_VALUES_LIST.length; i++) {
					if (rule[i]) {
						move[i] += step;
						if (move[i] > (MAX_VALUES_LIST[i] - 1)) {
							outBounds = true;
							break;
						}
					}
				}
				if (!outBounds) {
					// get index at which "move" is removed from unknownList
					j = updateUnknownList(unknownList, move, index);
					if (j > index) { // i.e. the move is in unknown, but not in
										// current NPosition
						possibleMoves.add(move.clone());
						index = j - 1; // move new index back by 1, as
										// unknowList at j is replaced by j+1
					}
				}
			}
			outBounds = false; // reset out bounds flag
		}

		return possibleMoves;
	}

	public static boolean checkOutBounds(int[] position, int index) {
		if (position[index] > (MAX_VALUES_LIST[index] - 1))
			return true;
		for (int i = 0; i < index; i++)
			if (position[i] > position[index])
				return true;
		for (int i = index + 1; (i < MAX_VALUES_LIST.length); i++)
			if (position[index] > position[i])
				return true;
		return false;
	}

	// This is a simple variation of addAllMovesToPosition for reducing size of
	// search of unknown list using addition moves of a unknown position
	public static void reduceListFromUnknownViaAddMoves(int[] position,
			ArrayList<int[]> unknownList, ArrayList<boolean[]> moveRules,
			int step) {

		// ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		int[] move;
		int i, j, k, index;
		boolean outBounds = false;

		// remove the unknown Position from unknownList for next iteration
		for (k = 0; k < unknownList.size(); k++) {
			if (Arrays.equals(position, unknownList.get(k))) {
				unknownList.remove(k);
				k--;
				break;
			}
		}

		// generate all moves using moveRules
		for (boolean[] rule : moveRules) {
			move = position.clone();
			index = k; // starting loop position for checking unknownList:
						// index+1
			while (!outBounds) {
				for (i = 0; i < MAX_VALUES_LIST.length; i++) {
					if (rule[i]) {
						move[i] += step;
						if (checkOutBounds(move, i)) {
							outBounds = true;
							break;
						}
					}
				}
				if (!outBounds) {
					// get index at which "move" is removed from unknownList
					j = updateUnknownList(unknownList, move, index);
					if (j > index) { // i.e. the move is in unknown, but not in
										// current NPosition
						// possibleMoves.add(move.clone());
						index = j - 1; // move new index back by 1, as
										// unknowList at j is replaced by j+1
					}
				}
			}
			outBounds = false; // reset out bounds flag
		}
	}

	public static boolean IsThePPosition(int[] position,
			ArrayList<int[]> unknownList, ArrayList<boolean[]> moveRules,
			int step) {

		// check whether subtraction moves are not on unknownList
		// Search order: -1 step for all rules; then -2 step for all rules; ....
		// when out of bounds using a rule, this rule is removed for next
		// iteration
		// Any time a move is found on unknownList, terminate search, exclude
		// from PPosition
		// Only when all subtraction moves from a position are not on
		// unknownList, declare as P

		boolean isP = true;
		boolean outBounds = false;
		int[] move;
		int i, j, k, delta, index;
		ArrayList<boolean[]> rulesCopy = new ArrayList<boolean[]>();

		// make a copy of rules, so it can be modified locally
		for (boolean[] rule : moveRules) {
			rulesCopy.add(rule.clone());
		}

		// find location of position in unkownList, use as max index for the
		// rest of searches
		index = unknownList.size();
		for (k = 0; k < index; k++) {
			if (Arrays.equals(position, unknownList.get(k))) {
				index = k; // update new end point for next search
				break;
			}
		}

		// check subtraction moves by the same steps over all rules, until all
		// out bounds
		delta = 0;
		while (isP && (rulesCopy.size() > 0)) {
			delta += step; // increase subtraction size after go over all rules
			for (k = rulesCopy.size(); k > 0;) {
				k--;
				move = position.clone();
				for (i = 0; i < MAX_VALUES_LIST.length; i++) {
					if (rulesCopy.get(k)[i]) {
						move[i] -= delta;
						if (move[i] < 0) {
							outBounds = true;
							rulesCopy.remove(k);
							break;
						}
					}
				}
				if (outBounds) {
					outBounds = false; // reset the out bounds flag
				} else {
					// get index at which "move" is located in unknownList
					j = belongToList(move, unknownList, index);
					if (j < index) { // i.e. the move is located in unknownList
						isP = false;
						break;
					}
				}
			}
		}
		return isP;
	}

	public static boolean IsThePPositionViaNP(int[] position,
			ArrayList<int[]> npositionList, ArrayList<boolean[]> moveRules,
			int step) {

		// check whether subtraction moves are all on N position List
		// Search order: -1 step for all rules; then -2 step for all rules; ....
		// when out of bounds using a rule, this rule is removed for next
		// iteration
		// Any time a move is NOT found on npositionList, terminate search,
		// exclude from PPosition
		// Only when all subtraction moves from a position are on npositionList,
		// declare as P

		boolean isP = true;
		boolean outBounds = false;
		int[] move;
		int i, j, k, delta, index;
		ArrayList<boolean[]> rulesCopy = new ArrayList<boolean[]>();

		// make a copy of rules, so it can be modified locally
		for (boolean[] rule : moveRules) {
			rulesCopy.add(rule.clone());
		}

		// find location of position in unkownList, use as max index for the
		// rest of searches
		index = npositionList.size();

		// check subtraction moves by the same steps over all rules, until all
		// out bounds
		delta = 0;
		while (isP && (rulesCopy.size() > 0)) {
			delta += step; // increase subtraction size after go over all rules
			for (k = rulesCopy.size(); k > 0;) {
				k--;
				move = position.clone();
				for (i = 0; i < MAX_VALUES_LIST.length; i++) {
					if (rulesCopy.get(k)[i]) {
						move[i] -= delta;
						if (move[i] < 0) {
							outBounds = true;
							rulesCopy.remove(k);
							break;
						}
					}
				}
				if (outBounds) {
					outBounds = false; // reset the out bounds flag
				} else {
					// get index at which "move" is located in npositionList
					j = belongToList(move, npositionList, index);
					if (j == index) { // i.e. the move is NOT located in
										// NPosition list
						isP = false;
						break;
					}
				}
			}
		}
		return isP;
	}

	public static boolean isOutOfBounds(int[] position) {
		boolean isOOB = false;
		for (int i = 0; (i < MAX_VALUES_LIST.length) && !isOOB; i++)
			isOOB = (position[i] < 0)
					|| (position[i] > (MAX_VALUES_LIST[i] - 1));
		return isOOB;
	}

	public static void getNextLists(ArrayList<int[]> ppositionList,
			ArrayList<int[]> npositionList, ArrayList<int[]> unknownList,
			ArrayList<boolean[]> gameRules, int step) {

		ArrayList<int[]> newPPositionList = new ArrayList<int[]>();

		ArrayList<int[]> subUnknownList = new ArrayList<int[]>();
		for (int[] position1 : unknownList) {
			subUnknownList.add(position1.clone());
		}
		sortPositions(subUnknownList);
		collapse(subUnknownList);

		if (npositionList.size() < unknownList.size() / 4) {
			for (int i = 0; i < subUnknownList.size(); i++) {
				int[] position = subUnknownList.get(i).clone();
				if (IsThePPositionViaNP(position, npositionList, gameRules,
						step)) {
					union(newPPositionList, permutations(position), false);
				}
			}

		} else if (npositionList.size() < unknownList.size() / 1.2) {

			for (int i = 0; i < subUnknownList.size(); i++) {
				int[] position = subUnknownList.get(i).clone();
				if (IsThePPositionViaNP(position, npositionList, gameRules,
						step)) {
					union(newPPositionList, permutations(position), false);
				} else {
					reduceListFromUnknownViaAddMoves(position, subUnknownList,
							gameRules, step);
					--i;
				}
			}

		} else {

			for (int i = 0; i < subUnknownList.size(); i++) {
				int[] position = subUnknownList.get(i).clone();
				if (IsThePPosition(position, unknownList, gameRules, step)) {
					union(newPPositionList, permutations(position), false);
				} else {
					reduceListFromUnknownViaAddMoves(position, subUnknownList,
							gameRules, step);
					--i;
				}
			}
		}
		
		checkGeneration(newPPositionList);

		ArrayList<int[]> newNPositionList = addAllMoves(newPPositionList,
				unknownList, gameRules, step);
		union(ppositionList, newPPositionList, false);
		union(npositionList, newNPositionList, false);
	}

	public static void checkGeneration(ArrayList<int[]> ppositionlist) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < ppositionlist.size(); i++) {
			for (int j = i+1; j < ppositionlist.size(); j++) {
				int[] first = ppositionlist.get(i);
				int[] second = ppositionlist.get(j);
				boolean[] b = isPositionGreater(first, second);
				if (b[0])
					if (b[1])
						indices.add(i);
					else 
						indices.add(j);
			}
		}
		Collections.sort(indices);
		Collections.reverse(indices);
		System.out.println(indices);
		int last = -1;
		for (int i : indices) {
			if (i != last)
				ppositionlist.remove(i);
			last = i;
		}
	}
	
	public static boolean[] isPositionGreater(int[] position1, int[] position2) {
		int[] difference = new int[MAX_VALUES_LIST.length];
		for (int i = 0; i < difference.length; i++)
			difference[i] = position1[i] - position2[i];
		Arrays.sort(difference);
		if ((difference[0] < 0 && difference[difference.length - 1] > 0) 
				|| (difference[0] == 0 && difference[difference.length - 1] == 0))
			return new boolean[] {false, false};
		if (difference[0] < 0)
			return new boolean[] {true, false};
		if (difference[0] >= 0)
			return new boolean[] {true, true};
		System.out.println("You suck!");
		return new boolean[] {false, true};
	}
	

	public static void print(ArrayList<int[]> positionList) {
		for (int i = 0; i < positionList.size(); i++)
			System.out.println(Arrays.toString(positionList.get(i)));
	}

	public static void collapse(ArrayList<int[]> positionList) {
		for (int i = positionList.size() - 1; i > 0; i--)
			for (int j = 0; j < i; j++)
				if (Arrays.equals(positionList.get(i), positionList.get(j))) {
					positionList.remove(i);
					break;
				}
	}

	// insertion sort
	public static void sortList(ArrayList<int[]> positionList) {
		int i, j;
		for (i = 1; i < positionList.size(); i++) {
			j = i;
			while ((j > 0) && compare(positionList.get(j - 1), positionList.get(j))) {
				Collections.swap(positionList, j - 1, j);
				j--;
			}
		}
	}

	public static boolean compare(int[] position1, int[] position2) {
		for (int i = 0; i < position1.length; i++) {
			if (position1[i] < position2[i])
				return false;
			if (position1[i] > position2[i])
				return true;
		}
		return false;
	}

	public static void sortPositions(ArrayList<int[]> positionList) {
		for (int[] position : positionList)
			Arrays.sort(position);
	}

	public static ArrayList<int[]> permutations(int[] position) {
		ArrayList<int[]> permut = new ArrayList<int[]>();
		permut = permutationAll(position);
		collapse(permut);
		for (int i = permut.size() - 1; i > 0; i--)
			if (isOutOfBounds(permut.get(i)))
				permut.remove(i);
		return permut;
	}

	public static ArrayList<int[]> permutationAll(int[] position) {
		ArrayList<int[]> perms = new ArrayList<int[]>();
		if (position.length == 1) {
			perms.add(position);
			return perms;
		}
		for (int i = 0; i < position.length; i++) {
			int[] position2 = new int[position.length - 1];
			for (int j = 0; j < i; j++)
				position2[j] = position[j];
			for (int j = i; j < position.length - 1; j++)
				position2[j] = position[j + 1];

			ArrayList<int[]> p = permutationAll(position2);
			for (int[] pos : p) {
				int[] pos2 = new int[pos.length + 1];
				System.arraycopy(pos, 0, pos2, 1, pos.length);
				pos2[0] = position[i];
				perms.add(pos2);
			}
		}
		return perms;
	}

	public static ArrayList<int[]> setPPosFromFile(String filePathName)
			throws Exception {
		ArrayList<int[]> values = new ArrayList<int[]>();
		ArrayList<int[]> permut = new ArrayList<int[]>();
		ArrayList<int[]> fullValues = new ArrayList<int[]>();

		values = setPPos(filePathName);
		sortPositions(values);
		collapse(values);
		sortList(values);

		for (int[] position : values) {
			permut = permutations(position);
			union(fullValues, permut, false);
		}
		return fullValues;
	}

	public static ArrayList<int[]> setPPos(String filePathName)
			throws Exception {
		ArrayList<int[]> values = new ArrayList<int[]>();
		try (BufferedReader br = new BufferedReader(
				new FileReader(filePathName))) {
			String line = br.readLine();
			while (line != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(line);
				line = br.readLine();
				String s = sb.toString().replaceAll("[\\[\\]\\p{Blank}]", "");
				String[] array = s.split(",");
				int[] position = new int[MAX_VALUES_LIST.length];
				for (int i = 0; i < position.length; i++)
					position[i] = Integer.parseInt(array[i]);
				values.add(position);
			}
		}
		return values;
	}

	public static String getFilePathName() {
		String filePathName = (MAX_VALUES_LIST[0] - 1) + "";
		for (int i = 1; i < MAX_VALUES_LIST.length; i++)
			filePathName += "-" + (MAX_VALUES_LIST[i] - 1);
		return filePathName;
	}

	public static String getFilePathName(int iteration) {
		String filePathName = (MAX_VALUES_LIST[0] - 1) + "";
		for (int i = 1; i < MAX_VALUES_LIST.length; i++)
			filePathName += "-" + (MAX_VALUES_LIST[i] - 1);
		filePathName += "_" + GAME + "_" + iteration;
		return filePathName;
	}

	@SuppressWarnings("unchecked")
	public static void writeToFile(ArrayList<int[]> ppositionList,
			String filePathName, boolean isLast) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(GAME + "\\" + filePathName + ".txt"),
				"UTF-8"));
		for (int i = 0; i < ppositionList.size(); i++) {
			writer.write(Arrays.toString(ppositionList.get(i)));
			writer.newLine();
		}
		writer.close();
		if (isLast) {
			ArrayList<int[]> pposListCopy = (ArrayList<int[]>) ppositionList
					.clone();
			BufferedWriter sortedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(GAME + "\\" + filePathName
							+ "_" + GAME + "_sorted.txt"), "UTF-8"));
			sortPositions(pposListCopy);
			collapse(pposListCopy);
			sortList(pposListCopy);
			for (int i = 0; i < pposListCopy.size(); i++) {
				sortedWriter.write(Arrays.toString(pposListCopy.get(i)));
				sortedWriter.newLine();
			}
			sortedWriter.close();
		}
	}

	public static String toMinutesAndSeconds(long time) {
		int minutes = (int) (time / 60000);
		int seconds = (int) ((time / 60000.0 - minutes) * 60);
		return minutes + " minutes, " + seconds + " seconds";
	}

	// insertion sort: ascending order
	public static void sortBooleanList(ArrayList<boolean[]> bList) {
		int i, j;
		for (i = 1; i < bList.size(); i++) {
			j = i;
			while ((j > 0) && compareBoolean(bList.get(j - 1), bList.get(j))) {
				Collections.swap(bList, j - 1, j);
				j--;
			}
		}
	}

	// compare two positions: true when b1 > b2 (starting form lower index)
	public static boolean compareBoolean(boolean[] b1, boolean[] b2) {
		for (int i = 0; i < b1.length; i++) {
			if ((b1[i] == false) && (b2[i] == true))
				return false;
			if ((b1[i] == true) && (b2[i] == false))
				return true;
		}
		return false;
	}

	public static ArrayList<int[]> setInitalUnknownList(int[] ranges, int piles) {
		ArrayList<int[]> unknown = new ArrayList<int[]>();
		int[] indices = new int[piles];
		for (int i = 0; i < piles; i++)
			indices[i] = 0; // set initial values
		unknown.add(indices.clone());
		do {
			advanceIndices(indices, ranges, piles);
			// System.out.println( java.util.Arrays.toString(indices) );
			unknown.add(indices.clone());
		} while (!allMaxed(indices, ranges, piles));
		return unknown;
	}

	// Advances 'indices' to the next in sequence.
	public static void advanceIndices(int[] indices, int[] ranges, int n) {
		for (int i = n - 1; i >= 0; i--) {
			if (indices[i] + 1 == ranges[i]) {
				indices[i] = 0;
			} else {
				indices[i] += 1;
				break;
			}
		}
	}

	// Tests if indices are in final position.
	public static boolean allMaxed(int[] indices, int[] ranges, int n) {
		for (int i = n - 1; i >= 0; i--) {
			if (indices[i] != ranges[i] - 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final int size = 100000;
		long[] times = new long[size + 2];
		int j, step;

		step = 1;
		char[] crule = new char[MAX_VALUES_LIST.length];
		System.out.print("Setting game rules (" + GAME + ") with bounds: ");
		for (j = 0; j < (MAX_VALUES_LIST.length - 1); j++)
			System.out.print((MAX_VALUES_LIST[j] - 1) + "-");
		System.out.println((MAX_VALUES_LIST[j] - 1) + " ...");
		for (int m = 0; m < gameRules.size(); m++) {
			for (int n = 0; n < MAX_VALUES_LIST.length; n++) {
				if (gameRules.get(m)[n])
					crule[n] = 'T';
				else
					crule[n] = 'F';
			}
			String srule = new String(crule);
			System.out.println("  " + (m + 1) + ": " + srule);
		}
		ArrayList<boolean[]> moveRules = new ArrayList<boolean[]>();
		for (int m = 0; m < gameRules.size(); m++) {
			moveRules.add(gameRules.get(m).clone());
		}
		// sortBooleanList(moveRules); //sort rules for higher efficiency in
		// addMove

		times[0] = System.currentTimeMillis();

		// setting initial unknown list
		ArrayList<int[]> unknownPositionList = new ArrayList<int[]>();
		int[] ranges = new int[MAX_VALUES_LIST.length];
		for (int i = 0; i < MAX_VALUES_LIST.length; i++)
			ranges[i] = MAX_VALUES_LIST[i];
		unknownPositionList = setInitalUnknownList(ranges, ranges.length);

		ArrayList<int[]> knownPPositionList = new ArrayList<int[]>();
		ArrayList<int[]> knownNPositionList = new ArrayList<int[]>();
		System.out.print("Setting initial PPositions ");
		if (fileNameInputPP.length() == 0) {
			System.out.println("as  "
					+ Arrays.toString(unknownPositionList.get(0)));
			knownPPositionList.add(unknownPositionList.get(0));
		} else {
			knownPPositionList = setPPosFromFile(fileNameInputPP);
			// knownPPositionList = setPPos(fileNameInputPP);
			System.out.println("from " + fileNameInputPP + "  .... ");
			// knownPPositionList = setPPosFromSorted(fileNameInputPP);
		}

		times[1] = System.currentTimeMillis();
		System.out.println("Finished setting PPositions: "
				+ knownPPositionList.size() + " in "
				+ toMinutesAndSeconds(times[1] - times[0]));

		System.out.println("Starting to set initial NPositions with "
				+ gameRules.size() + " rules ...");
		times[0] = System.currentTimeMillis();
		knownNPositionList = addAllMoves(knownPPositionList,
				unknownPositionList, moveRules, step, true);
		if (isMisere)
			knownNPositionList.add(unknownPositionList.remove(0));

		times[1] = System.currentTimeMillis();
		System.out.println("Finished setting " + knownNPositionList.size()
				+ " NPositions and " + unknownPositionList.size()
				+ " unknownPosition in "
				+ toMinutesAndSeconds(times[1] - times[0]));

		times[0] = System.currentTimeMillis();
		for (int i = 0; i < size + 1; i++) {
			getNextLists(knownPPositionList, knownNPositionList,
					unknownPositionList, moveRules, step);
			times[i + 1] = System.currentTimeMillis();
			int pSize = knownPPositionList.size();
			int nSize = knownNPositionList.size();
			int uSize = unknownPositionList.size();
			System.out.println((i + 1) + ": "
					+ toMinutesAndSeconds(times[i + 1] - times[i]) + ": "
					+ pSize + ", " + nSize + ", " + uSize + ", "
					+ (pSize + nSize + uSize));
			if (uSize == 0) {
				System.out.println("Finished calculation in "
						+ toMinutesAndSeconds(times[i + 1] - times[0]) + ".");
				writeToFile(knownPPositionList, getFilePathName(), true);
				break;
			}
			writeToFile(knownPPositionList, getFilePathName(i + 1), false);
		}

	}

}
