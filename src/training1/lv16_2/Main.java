package training1.lv16_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static char[][] map = {
			{'A', 'B', 'K', 'T'},
			{'K', 'F', 'C', 'F'},
			{'B', 'B', 'Q', 'Q'},
			{'T', 'P', 'Z', 'F'}			
	};
	
	static int[] dat = new int[200];
	
	static char A, B;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		char A = st.nextToken().charAt(0);
		char B = st.nextToken().charAt(0);
		init();
				
		System.out.println(dat[A] + dat[B]);
		
	}
	
	static void init() {
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[i].length; j++) {
				char c = map[i][j];
				dat[c] += 1;
			}
		}
	}
}
