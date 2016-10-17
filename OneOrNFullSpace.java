

import java.io.*;
import java.util.*;

public class OneOrNFullSpace {

	public static final int MAX1 = 8 + 1;
	public static final int MAX2 = 10 + 1;
	public static final int MAX3 = 100 + 1;
	public static final int MAX4 = 100 + 1;
	public static final int[] MAX_VALUES_LIST = { MAX1, MAX2, MAX3, MAX4 };

	public static void complement(ArrayList<int[]> all, ArrayList<int[]> positionList) {
		for (int[] position : positionList)
			for (int i = all.size() ; i > 0; ){
				i--;
				if (Arrays.equals(position, all.get(i)))
					all.remove(i);
			}
		return;
	}

	public static void union(ArrayList<int[]> positionList1, ArrayList<int[]> positionList2, boolean removeDuplicate) {
		int skip;

		if (removeDuplicate){
			for (int[] position2 : positionList2) {
				skip=0;
				for (int[] position1 : positionList1)
					if (Arrays.equals(position2, position1)) {
						skip=1;
						break;
					}
			     if (skip==0)
			    	 positionList1.add(position2);
			}
		} else {
			for (int[] position : positionList2)
				positionList1.add(position);
		}
		return;
	}

	public static ArrayList<int[]> intersection(ArrayList<int[]> positionList1, ArrayList<int[]> positionList2) {
		ArrayList<int[]> samePositions = new ArrayList<int[]>();
		for (int[] position2 : positionList2)
			for (int[] position1 : positionList1)
				if (Arrays.equals(position2, position1)) {
					samePositions.add(position2);
					break;
				}
		return samePositions;
	}

	public static boolean doesItIntersect(ArrayList<int[]> positionList1, ArrayList<int[]> positionList2) {
		boolean yes = false;
		for (int[] position2 : positionList2) {
			for (int[] position1 : positionList1)
				if (Arrays.equals(position2, position1)) {
					yes=true;
					break;
				}
			if (yes) break;
		}
		return yes;
	}
	
	public static ArrayList<int[]> addMoves(ArrayList<int[]> ppositionList) {
		ArrayList<int[]> allMoves = new ArrayList<int[]>();
		int skip;
		int firstP;
		
		firstP=1;
		for (int[] position : ppositionList) {
			
			if (firstP ==1) {
				allMoves = addMovesToPosition(position);
				firstP = 0;

			} else {
				ArrayList<int[]> possibleMovesFromPosition = addMovesToPosition(position);
/*
			// remove duplicated positions from moves 
			ArrayList<int[]> samePositions = intersection(allMoves,possibleMovesFromPosition);
			complement(possibleMovesFromPosition, samePositions);
			// only add new positions to allMoves 
			for (int[] move : possibleMovesFromPosition)
				allMoves.add(move);
*/
		 
				for (int[] position2 : possibleMovesFromPosition) {
					skip=0;
					for (int[] position1 : allMoves) 
						if (Arrays.equals(position2, position1)) {
							skip=1;
							break;
						}
					if (skip==0)
							allMoves.add(position2);
				}
			}
		}
		return allMoves;
		
//		for (int[] position : ppositionList) {			
//				ArrayList<int[]> possibleMovesFromPosition = addMovesToPosition(position);
//				for (int[] position2 : possibleMovesFromPosition) 
//					allMoves.add(position2);
//		}
		
	}


	public static ArrayList<int[]> addMovesToPosition(int[] position) {
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		int[] move;
		int outBounds=0;

		for (int l = 0; l < MAX_VALUES_LIST.length; l++) {
			move = position.clone();
			//System.out.println("clone: " + move[l]+":"+Arrays.toString(move)+"\n");
			for (int i = move[l]; i < (MAX_VALUES_LIST[l]-1); ) {
				i++;
				move[l] = i;				
				possibleMoves.add(move.clone());
			}
		}


		move = position.clone();
		for (int i = position[0]; i < (MAX1-1); i++) {
			for (int l = 0; l < MAX_VALUES_LIST.length; l++) {
				move[l] += 1;
				if (move[l] > (MAX_VALUES_LIST[l]-1)) {
					outBounds=1;
					break;
				}
			}
			if (outBounds==0)
				possibleMoves.add(move.clone());
			else
				break;
		}
/*		
		for (int[]  position4: possibleMoves)
			System.out.println(Arrays.toString(position4)+"\n");
*/
		return possibleMoves;
	}

/*	
	public static ArrayList<int[]> addMovesToPosition(int[] position) {
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		int[] move;

		for (int l = 0; l < MAX_VALUES_LIST.length; l++)
			for (int i = 1; i < MAX_VALUES_LIST[l]; i++) {
				move = position.clone();
				move[l] += i;
				if (!isOutOfBounds(move))
					possibleMoves.add(move);
			}

		for (int i = 1; i < MAX1; i++) {
			move = position.clone();
			for (int l = 0; l < MAX_VALUES_LIST.length; l++)
				move[l] += i;
			if (!isOutOfBounds(move))
				possibleMoves.add(move);
		}
		return possibleMoves;
	}
*/	
	public static ArrayList<int[]> subtractMoves(ArrayList<int[]> ppositionList, boolean onlyOneMove){
		ArrayList<int[]> allMoves = new ArrayList<int[]>();
		int skip;
		for (int[] position : ppositionList) {
			ArrayList<int[]> possibleMovesFromPosition = subtractMovesFromPosition(position, onlyOneMove);
/*
			//remove duplicated positions from moves 
			ArrayList<int[]> samePositions = intersection(allMoves,possibleMovesFromPosition);
			complement(possibleMovesFromPosition, samePositions);
			//only add new positions to allMoves
			for (int[] move : possibleMovesFromPosition)
				allMoves.add(move);
*/
			for (int[] position2 : possibleMovesFromPosition) {
				skip=0;
				for (int[] position1 : allMoves)
					if (Arrays.equals(position2, position1)) {
						skip=1;
						break;
					}
			     if (skip==0)
			    	 allMoves.add(position2);
			}
		}
		return allMoves;
	}
	
	public static ArrayList<int[]> subtractMovesFromPosition(int[] position, boolean onlyOneMove) {
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		int[] move;
		int outBounds=0;
		
		if (!onlyOneMove){ //Moves beyond subtract 1 
		/* Accounts for the moves (-x,0,0,...)  x>1 and permutations */
			for (int l = 0; l < MAX_VALUES_LIST.length; l++) {
				move = position.clone();
				for (int i = (position[l]-1); i > 0; ) {
					i--;
					move[l] = i;
					possibleMoves.add(move.clone());
				}
			}
		/* Accounts for the moves (-x,-x,-x,...)  x>1 */
			int j = 0;
			move = position.clone();
			for (int i = position[0]; i > 0; i--) {
				for (int l = 0; l < MAX_VALUES_LIST.length; l++) {
					move[l] -= 1;
					if (move[l] < 0) {
						outBounds = 1;
					    break;
					}
				}
				if (outBounds==0) {
					if (j>0) possibleMoves.add(move.clone());
					j++;
				} else {
					break;
				}
			}

		} else { //only subtract 1 moves
			for (int l = 0; l < MAX_VALUES_LIST.length; l++) {
					move = position.clone();
					move[l] -= 1;
					if (move[l] > -1)
						possibleMoves.add(move);
			}

			/* Accounts for the moves (-1,-1,-1,...) */		
			move = position.clone();
			for (int l = 0; l < MAX_VALUES_LIST.length; l++) {
				move[l] -= 1;
				if (move[l] < 0) {
					outBounds = 1;
					break;
				}
			}
			if (outBounds==0)
				possibleMoves.add(move);
		}
					
		return possibleMoves;
	}

	public static boolean isOutOfBounds(int[] position) {
		boolean isOOB = false;
		for (int i = 0; i < MAX_VALUES_LIST.length && !isOOB; i++)
			isOOB = (position[i] < 0) || (position[i] > MAX_VALUES_LIST[i] - 1);
		return isOOB;
	}

	public static void getNextLists(ArrayList<int[]> ppositionList, 
									ArrayList<int[]> npositionList, ArrayList<int[]> unknownList) {
		
		ArrayList<int[]> newPPositionList = new ArrayList<int[]>();
		
		if (npositionList.size() < unknownList.size() / 5){
			for (int[] position : unknownList){
				ArrayList<int[]> oneMove = subtractMovesFromPosition(position, true);
				if (oneMove.size() == intersection(oneMove, npositionList).size()){
					oneMove = subtractMovesFromPosition(position, false);
					if (oneMove.size() == intersection(oneMove, npositionList).size()){
						newPPositionList.add(position);
					}
				}
			}
		} else {
			for (int[] position : unknownList){
				ArrayList<int[]> oneMove = subtractMovesFromPosition(position, true);
				if (!doesItIntersect(oneMove, unknownList)){
					oneMove = subtractMovesFromPosition(position, false);
					if (!doesItIntersect(oneMove, unknownList))
						newPPositionList.add(position);
				}
			}
		}
		
		ArrayList<int[]> newNPositionList = addMoves(newPPositionList);
		union(ppositionList, newPPositionList, false);
		union(npositionList, newNPositionList, true);
		complement(unknownList, newPPositionList);
		complement(unknownList, newNPositionList);
		return;
	}

	public static void removeZero(ArrayList<int[]> positionList) {
		for (int i = positionList.size() - 1; i > 0; i--)
			if (Arrays.equals(positionList.get(i), new int[] { 0, 0, 0, 0 }))
				positionList.remove(i);
	}

	public static void print(ArrayList<int[]> positionList) {
		for (int i = 0; i < positionList.size(); i++)
			System.out.println(Arrays.toString(positionList.get(i)));
	}

	public static void collapse(ArrayList<int[]> positionList) {
		for (int i = positionList.size() - 1; i >= 0; i--)
			for (int j = 0; j < i; j++)
				if (Arrays.equals(positionList.get(i), positionList.get(j))) {
					positionList.remove(i);
					break;
				}
	}

	public static void sortList(ArrayList<int[]> positionList) {
		for (int i = 0; i < positionList.size(); i++)
			for (int j = 0; j < positionList.size(); j++) {
				int[] a1 = positionList.get(i);
				int[] a2 = positionList.get(j);
				if (a1[0] < a2[0])
					Collections.swap(positionList, i, j);
				else if (a1[0] == a2[0])
					if (a1[1] < a2[1])
						Collections.swap(positionList, i, j);
					else if (a1[1] == a2[1])
						if (a1[2] < a2[2])
							Collections.swap(positionList, i, j);
						else if (a1[2] == a2[2])
							if (a1[3] < a2[3])
								Collections.swap(positionList, i, j);
			}
	}

	public static void sortPositions(ArrayList<int[]> positionList) {
		for (int[] position : positionList)
			Arrays.sort(position);
	}

	public static ArrayList<int[]> setPPos(String filePathName) throws Exception {
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

	/**
	 * 
	 * @return
	 */
	public static String getFilePathName() {
		String filePathName = (MAX1 - 1) + "";
		for (int i = 1; i < MAX_VALUES_LIST.length; i++)
			filePathName += "-" + (MAX_VALUES_LIST[i] - 1);
		return filePathName;
	}

	/**
	 * 
	 * @param iteration
	 * @return
	 */
	public static String getFilePathName(int iteration) {
		String filePathName = (MAX1 - 1) + "";
		for (int i = 1; i < MAX_VALUES_LIST.length; i++)
			filePathName += "-" + (MAX_VALUES_LIST[i] - 1);
		filePathName += "_" + iteration;
		return filePathName;
	}

	/**
	 * 
	 * @param ppositionList
	 * @param filePathName
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void writeToFile(ArrayList<int[]> ppositionList,
			String filePathName, boolean isLast) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(filePathName + ".txt"), "UTF-8"));
		for (int i = 0; i < ppositionList.size(); i++) {
			writer.write(Arrays.toString(ppositionList.get(i)));
			writer.newLine();
		}
		writer.close();
		if (isLast) {
			ArrayList<int[]> pposListCopy = (ArrayList<int[]>) ppositionList.clone();
			BufferedWriter sortedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filePathName
							+ "_sorted.txt"), "UTF-8"));
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
	
	public static String toMinutesAndSeconds(long time){
		int minutes = (int) (time / 60000);
		int seconds = (int) ((time / 60000.0 - minutes) * 60);
		return minutes + " minutes, " + seconds + " seconds";
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ArrayList<int[]> all = new ArrayList<int[]>();
		ArrayList<int[]> unknownPositionList = new ArrayList<int[]>();
		int[][] all1 = new int[MAX1 * MAX2 * MAX3 * MAX4][MAX_VALUES_LIST.length];
		int in = 0;
		for (int i = 0; i < MAX1; i++)
			for (int j = 0; j < MAX2; j++)
				for (int k = 0; k < MAX3; k++)
					for (int l = 0; l < MAX4; l++) {
						all1[in][0] = i;
						all1[in][1] = j;
						all1[in][2] = k;
						all1[in][3] = l;
						in++;
					}
		for (int i = 0; i < all1.length; i++) {
			all.add(all1[i]);
			unknownPositionList.add(all1[i]);
		}

		ArrayList<int[]> knownPPositionList = new ArrayList<int[]>();
		ArrayList<int[]> knownNPositionList = new ArrayList<int[]>();
		
		
//		ArrayList<int[]> unknownPositionList = new ArrayList<int[]>();
//		ArrayList<int[]> newPPositionList = new ArrayList<int[]>();
//		ArrayList<int[]> newNPositionList = new ArrayList<int[]>();
//		.add(new int[] {0, 0, 0, 0});
/*		
		for (int[]  position: knownNPositionList)
			System.out.println(Arrays.toString(position)+"\n");
*/		
/*
		System.out.println((0 ) +  ": " + knownPPositionList.size() + 
				", " + knownNPositionList.size() + ", " + unknownPositionList.size());
*/			
		long[] t = new long[7];
		t[0] = System.currentTimeMillis();
		knownPPositionList = setPPos("6-10-100-100.txt");
		t[1] = System.currentTimeMillis();
		System.out.println("Set P-Positions: " + toMinutesAndSeconds(t[1] - t[0]));
		knownNPositionList = addMoves(knownPPositionList);
		t[2] = System.currentTimeMillis();
		System.out.println("Set N-Positions (added): " + toMinutesAndSeconds(t[2] - t[1]));
		
		union(knownNPositionList, subtractMoves(knownPPositionList, true), true);
		t[3] = System.currentTimeMillis();
		System.out.println("Set N-Positions (subtract one): " + toMinutesAndSeconds(t[3] - t[2]));
		union(knownNPositionList, subtractMoves(knownPPositionList, false), true);
		t[4] = System.currentTimeMillis();
		System.out.println("Set N-Positions (subtract all): " + toMinutesAndSeconds(t[4] - t[3]));
		complement(unknownPositionList, knownPPositionList);
		t[5] = System.currentTimeMillis();
		System.out.println("Set U-Positions (P-Position): " + toMinutesAndSeconds(t[5] - t[4]));
		complement(unknownPositionList, knownNPositionList);
		t[6] = System.currentTimeMillis();
		System.out.println("Set U-Positions (N-Position): " + toMinutesAndSeconds(t[6] - t[5]));
	

		
		final int size = 100000;
		long[] times = new long[size + 2];
		times[0] = System.currentTimeMillis();
		
		for (int i = 0; i < size + 1; i++){
			getNextLists(knownPPositionList, knownNPositionList, unknownPositionList);
			times[i + 1] = System.currentTimeMillis();
			System.out.println((i + 1) + ": " + toMinutesAndSeconds(times[i+1] - times[i]) + ": " + knownPPositionList.size() + 
					", " + knownNPositionList.size() + ", " + unknownPositionList.size()+ ", " 
					+ (unknownPositionList.size()+knownPPositionList.size()+knownNPositionList.size()));
			if (unknownPositionList.size() == 0){
				writeToFile(knownPPositionList, getFilePathName(), true);
				break;
			}
			writeToFile(knownPPositionList, getFilePathName(i + 1), false);
		}
		
	}
}
