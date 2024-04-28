package training1.lv165_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
	static ArrayList<Character> list = new ArrayList<>();
	static HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		String s = st.nextToken();
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			list.add(c);
			
			if(map.containsKey(c)) {
				ArrayList<Integer> l = map.get(c);
				l.add(i);
			} else {
				map.put(c, new ArrayList<Integer>(Arrays.asList(i)));
			}
		}
		
		st = new StringTokenizer(br.readLine());
		char c1 = st.nextToken().charAt(0);
		
		st = new StringTokenizer(br.readLine());
		char c2 = st.nextToken().charAt(0);
		
		ArrayList<Integer> l = map.get(c1);
		for(int n : l) {
			list.set(n, c2);
		}
		
		for(char c : list) {
			System.out.print(c);
		}
	}
}
