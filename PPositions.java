import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


public class PPositions {
	
	static final int size = 3;
	static String GAME = "NimPlusWytGen";
//	static String filepath = GAME + "\\30-30-30_" + GAME + "_";
//	static String addition = "";
	static String addition = "_" + GAME;
	static String filepath = GAME + "\\60-60-60_";
	static final int number = 60;
	
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
				int[] position = new int[size];
				for (int i = 0; i < position.length; i++)
					position[i] = Integer.parseInt(array[i]);
				values.add(position);
			}
		}
		return values;
	}
	
	public static boolean isPositionGreater(int[] position1, int[] position2) {
		int[] difference = new int[size];
		for (int i = 0; i < difference.length; i++)
			difference[i] = position1[i] - position2[i];
		for (int i = 0; i < difference.length; i++)
			if (difference[i] < 0)
				return false;
//		if ()
//		if (difference[0] + difference[2] == difference[1])
//			return true;
//		Arrays.sort(difference);
		if (difference[0] == 0 || difference[1] == 0 || difference[2] == 0)
			return true;
		if (difference[1] == difference[2])
			return true;
//		if (difference[0] == difference[1] || difference[1] == difference[2])
//			return true;
//		if (difference[0] + difference[1] == difference[2])
//			return true;
		return false;
		
	}
	
	public static void main(String[] args) throws Exception {
		
//		for (int i = 1; i < number; i++) {
			ArrayList<int[]> arr = setPPos(filepath + 60 + addition + ".txt");
			for (int[] p: arr) {
				if (p[0] == p[1] + p[2])
					System.out.println(Arrays.toString(p));
			}
//		}
		
//		ArrayList<int[]> previous = setPPos(filepath + 1 + addition + ".txt");
//		ArrayList<int[]> current = setPPos(filepath + 2 + addition + ".txt");
//		ArrayList<int[]> pairs = new ArrayList<int[]>();
//		
//		
//		for (int i = 1; i < previous.size(); i++) {
//			pairs.add(new int[] {0,0,0});
//			pairs.add(previous.get(i));
//		}
//		int previousstartingindex = 1;
//		int currentstartingindex = 1;
//		for (int index = 1; index < number; index++) {
//			previous = setPPos(filepath + index + addition + ".txt");
//			current = setPPos(filepath + (index + 1) + addition + ".txt");
//			previousstartingindex = currentstartingindex;
//			currentstartingindex = previous.size();
//			for (int i = currentstartingindex; i < current.size(); i++) {
//				for (int j = previousstartingindex; j < previous.size(); j++) {
//					if (isPositionGreater(current.get(i), previous.get(j))) {
//						pairs.add(previous.get(j));
//						pairs.add(current.get(i));
//					}
//				}
//			}
//		}
//		
//		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filepath + addition + "Pairs_" + number + ".txt")));
//		for (int i = 0; i < pairs.size(); i+=2) {
//			out.println(Arrays.toString(pairs.get(i)) + "," + Arrays.toString(pairs.get(i+1)));
//		}
//		
//		out.close();
		
//		final int number2 = 15;
//		final int[] gen = new int[number + 1 + number2];
//		gen[0] = 1;
//		for (int i = 1; i < number + 1; i++) {
//			ArrayList<int[]> ar = setPPos(filepath + i + "_ConsecutiveGen.txt");
//			gen[i] = ar.size();
//		}
//		for (int i = 1; i < number2 + 1; i++) {
//			ArrayList<int[]> ar = setPPos(filepath2 + i + "_ConsecutiveGen.txt");
//			gen[number + i] = ar.size();
//		}
//		System.out.println(Arrays.toString(gen));
//		for (int i = gen.length - 1; i > 0; i--) {
//			gen[i] -= gen[i-1];
//		}
//		System.out.println(Arrays.toString(gen));
//		for (int i = 0; i < gen.length; i++) {
//			gen[i] = (gen[i] - 1) / 2;
//		}
//		System.out.println(Arrays.toString(gen)); 
		
			
	}

	
	
}
