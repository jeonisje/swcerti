package 프로원정대추가문제.영상압축;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int[][]  map;
	static int N;
 	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			String s = st.nextToken();
			for(int j=0; j<N; j++) {
				map[i][j] = s.charAt(j) - '0';
			}
		}

		dc(0, 0, N);

	}
	
	static void dc(int x, int y, int n) {
	    int compressed = 0;
	    int cur = map[x][y]; 
	    for(int i = x; i < x + n; i++)
	        for(int j = y; j < y + n; j++)
	        	// 다른 픽셀이 있다! 
	            if(map[i][j] != cur) 
	            	compressed = 1; 
	            
	    // 다시 압축을 해야 한다면
	    if(compressed == 1) {
	        // pre - 들어가면서 출력
	    	int part = n / 2;
	        System.out.print("(");
	        dc(x, y, part); 
	        dc(x, y + part, part);
	        dc(x + part, y, part);
	        dc(x + part, y + part, part);
	        // post - 나오면서 출력
	        System.out.print(")");
	    }
	    else 
	    	System.out.print(cur);
	}

	

}
