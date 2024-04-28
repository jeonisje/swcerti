package training1.lv16_7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int[] dat = new int[150];
	
	static int N = 3;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			String s = st.nextToken();
			for(int j=0; j<s.length(); j++) {
				dat[s.charAt(j)] = 1;
			}
		}
		
		if(dat['M'] == 1) {
			System.out.println("M이 존재합니다");
		} else {
			System.out.println("M이 존재하지 않습니다");
		}
	}
}
