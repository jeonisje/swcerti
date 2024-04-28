package exercise.M09_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Main {
	static int N;
	static Map<String, Integer> map = new TreeMap<String, Integer>();
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		//BufferedReader br = new BufferedReader(new FileReader("src/exercise/M09_1/input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		
		
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			
			String name = st.nextToken();
			int num = Integer.parseInt(st.nextToken());
			
			if(!map.containsKey(name)) {
				map.put(name, num);
			}			
		}
		

		for(Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() +  " " + entry.getValue());
		}
		
	}
}
