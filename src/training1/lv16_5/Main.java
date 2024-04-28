package training1.lv16_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Main {
	static LinkedList<Character> list = new LinkedList<>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		String s = st.nextToken();
		for(int i=0; i<s.length(); i++) {
			list.add(s.charAt(i));
		}
		
		st = new StringTokenizer(br.readLine());
		int idx = Integer.parseInt(st.nextToken());
		
		list.remove(idx);
		
		for(char c : list) {
			System.out.print(c);
		}
	}
}
