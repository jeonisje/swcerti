package training1.lv16_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Main {
	
	static LinkedList<Character> list = new LinkedList<>();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		String s = st.nextToken();
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			list.add(c);
		}
				
		st = new StringTokenizer(br.readLine());		
		int index = Integer.parseInt(st.nextToken());
		
		list.add(index, 'A');
		
		for(char c : list) {
			System.out.print(c);
		}
		
	}
}
