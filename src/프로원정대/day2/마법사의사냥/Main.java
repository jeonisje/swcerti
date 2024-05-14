package 프로원정대.day2.마법사의사냥;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		int[] directY = {-1, -1, 1, 1};
		int[] directX = {-1, 1, -1, 1};
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][N];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		st = new StringTokenizer(br.readLine());
		int range = Integer.parseInt(st.nextToken());
		
		int max = 0;
		int sum = 0;
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				sum = 0;			
					
				for(int k=0; k<4; k++) {					
					int ny = i + directY[k];
					int nx = j + directX[k];
					
					if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
					sum += map[ny][nx];
					
					for(int z=1; z<range; z++) {
						ny = ny + directY[k];
						nx = nx + directX[k];
						
						if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
						sum += map[ny][nx];
					}
				}				
				
				max = Math.max(sum, max);
			}
		}
		
		System.out.println(max);
	}
}
