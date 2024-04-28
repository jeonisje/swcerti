package exercise.M10_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
	
	static int H, W, N;
	
	static HashMap<Integer, ArrayList<String>> map = new HashMap<>();
	
	public static void main(String[] args) throws IOException {
		//BufferedReader br = new BufferedReader(new FileReader("src/exercise/M10_2/input2.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		H = Integer.parseInt(st.nextToken());
		W = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<H; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<W; j++) {
				int num = Integer.parseInt(st.nextToken());
				
				StringBuffer sb = new StringBuffer();
				sb.append("(").append(i).append(",").append(j).append(")");
				
				
				
				if(map.containsKey(num)) {
					ArrayList<String> list  = map.get(num);
					list.add(sb.toString());					
				} else {
					map.put(num, new ArrayList(Arrays.asList(sb.toString())));
				}
			}
		}
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());	
		
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			int num = Integer.parseInt(st.nextToken());
			if(map.containsKey(num)) {
				for(String str : map.get(num)) {
					System.out.print(str + " ");
				}
			} else {
				System.out.print("none");
			}
			System.out.println();
		}
	}
}
