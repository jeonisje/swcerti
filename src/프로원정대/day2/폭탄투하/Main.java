package 프로원정대.day2.폭탄투하;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	public static void main(String[] args) throws IOException {
		char[][] map = {
				{'_', '_','_','_','_'},
				{'_', '_','_','_','_'},
				{'_', '_','_','_','_'},
				{'_', '_','_','_','_'},
				{'_', '_','_','_','_'}
		};
		
		int[] directY = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] directX = {-1, 0, 1, -1, 1, -1, 0, 1};
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int[][] loc = new int[2][2];
		
		for(int i=0; i<2; i++) {
			 st = new StringTokenizer(br.readLine());
			 for(int j=0; j<2; j++) {
				 loc[i][j] = Integer.parseInt(st.nextToken());
			 }
		}
		
 		for(int k=0; k<2; k++) {
 			int locY = loc[k][0];
 			int locX = loc[k][1];
 			
 			for(int i=0; i<8; i++) {
 				int nexty = locY + directY[i];
 				int nextx = locX + directX[i];
 				
 				if(nexty < 0 || nextx < 0 || nexty >= 4 || nextx >=5) continue;
 				map[nexty][nextx] = '#';
 			}
 		}
 		
 		for(int i=0; i<4; i++) {
 			for(int j=0; j<5; j++) {
 				System.out.print(map[i][j] + " ");
 			}
 			System.out.println();
 		}
	}
	
}


/*
1 1
3 3

*/