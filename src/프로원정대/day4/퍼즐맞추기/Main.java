package 프로원정대.day4.퍼즐맞추기;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int M;
	
	static HashMap<Integer, PriorityQueue<Location>> hashMap = new HashMap<>();
	static int[][] map;
	static int[][] used;
	static int[] hashConstant = {1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		used = new int[N][N];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int i=0; i<N-2; i++) {
			for(int j=0; j<N-2; j++) {
				int hashCode = getHash(i, j);
				PriorityQueue<Location> q = hashMap.getOrDefault(hashCode
						, new PriorityQueue<Location>((o1, o2) -> o1.row == o2.row ? Integer.compare(o1.col, o2.col) : Integer.compare(o1.row, o2.row)));
				q.add(new Location(i, j));
				hashMap.put(hashCode, q);
				
			}
		}
		
		int count = 0;
		for(int k=0; k<M; k++) {
			int[][] piece = new int[3][3];
			for(int i=0; i<3; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<3; j++) {
					piece[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			int findingHash = getHash(piece);
			if(hashMap.containsKey(findingHash)) {
				PriorityQueue<Location> q = hashMap.get(findingHash);
				while(!q.isEmpty()) {
					Location loc = q.remove();
					if(!available(loc.row, loc.col)) continue;
					
					putPiece(loc.row, loc.col);
					count++;
					break;
				}
			}
		}
		
		System.out.println(count);
	}
	
	static int getHash(int row, int col) {		
		int hashCode = 0;
		int idx = 0;
		for(int i=row; i<row+3; i++) {
			for(int j=col; j<col+3; j++) {				
				hashCode += ((map[i][j]) %10) * hashConstant[idx];
				idx++;
			}
		}		
		return hashCode;
	}
	
	static int getHash(int[][] piece) {		
		int hashCode = 0;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				int idx = i * 3 + j;
				hashCode += ((piece[i][j]) %10) * hashConstant[idx];
			}
		}		
		return hashCode;
	}
	
	static boolean available(int row, int col) {
		int startRow = Math.max(row - 1, 0);
		int endRow = Math.min(row + 1, N);
		int startCol = Math.max(col - 1, 0);
		int endCol = Math.min(col + 1, N);
		
		for(int i=startRow; i<=endRow; i++) {
			for(int j=startCol; j<=endCol; j++) {
				if(used[i][j] == 1) return false;
			}
		}
		
		return true;
	}
	
	static void putPiece(int row, int col) {
		int startRow = Math.max(row - 1, 0);
		int endRow = Math.min(row + 1, N);
		int startCol = Math.max(col - 1, 0);
		int endCol = Math.min(col + 1, N);
		
		for(int i=startRow; i<=endRow; i++) {
			for(int j=startCol; j<=endCol; j++) {
				used[i][j] = 1;
			}
		}
		
	}
	
	static class Location {
		int row;
		int col;
		public Location(int row, int col) {		
			this.row = row;
			this.col = col;
		}
	}
}
