package 알고리즘별.union_find.섬의개수;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[][] map;
	static int[] parent;
	static int[] sizeByID;
	
	static int maxSize;
	static int count;
	
	static int sumMaxSize;
	static int sumCount;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	static int MAX = 101;
	
	static int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		
		if(a == pa && b == pb) {
			int aa=1;
		}
		else if (a != pa && b != pb) {
			count--;
		} 
		if( a == pa && b != pb && sizeByID[pb] > 1) {
			count--;
		}
			
		
		//sumCount += count;
		
		int size = sizeByID[pa] +  sizeByID[pb];
		maxSize = Math.max(size, maxSize);
		//sumMaxSize += maxSize;
		sizeByID[pa] = size;
		
		parent[pb] = pa;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());		
		
		N = Integer.parseInt(st.nextToken());
		
		map = new int[MAX][MAX];
		parent = new int[N+1];
		sizeByID = new int[N+1];		
		
		maxSize = 1;
		count = 0;
		sumCount = 0;
		
		for(int i=1; i<=N; i++) {
			parent[i] = i;
			sizeByID[i] = 1;
		}
		
		for(int i=1; i<=N; i++) {
			st = new StringTokenizer(br.readLine());		
			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());
			
			map[row][col] = i;
			
			boolean flag = false;
			for(int j=0; j<4; j++) {
				int nextRow = row + directRow[j];
				int nextCol = col + directCol[j];
				if(nextRow < 0 || nextCol < 0 || nextRow >= MAX|| nextCol >= MAX) continue;
				
				//System.out.println("idx : " + i + " => map[" + nextRow + "][" + nextCol + "] = " +  map[nextRow][nextCol]);
				if(map[nextRow][nextCol] != 0) {					
					union(map[nextRow][nextCol], i);
					flag= true;
					
				}
				
			}
			
			if(!flag) {
				count++;
				//sumCount = sumCount + count;
				//sumMaxSize += maxSize;
			}
			
			sumCount += count;
			sumMaxSize += maxSize;
			
			//System.out.println("y =>" + row + ", x => " + col + " =======> 섬개수 : " + count + ", 최대크기 : " + maxSize );
		}		
		
		System.out.println(sumCount);
		System.out.println(sumMaxSize);
	}
}



/*



29
3 7
6 2
4 8
6 9
3 5
1 3
3 2
0 3
9 4
1 7
5 4
4 9
9 0
9 9
8 3
7 2
3 9
6 7
7 9
7 3
2 3
8 2
1 9
4 5
7 6
7 5
2 1
1 8
1 0

==>
306
82


y =>3, x => 7 =======> 섬개수 : 1, 최대크기 : 1
y =>6, x => 2 =======> 섬개수 : 2, 최대크기 : 1
y =>4, x => 8 =======> 섬개수 : 3, 최대크기 : 1
y =>6, x => 9 =======> 섬개수 : 4, 최대크기 : 1
y =>3, x => 5 =======> 섬개수 : 5, 최대크기 : 1
y =>1, x => 3 =======> 섬개수 : 6, 최X대크기 : 1
y =>3, x => 2 =======> 섬개수 : 7, 최대크기 : 1
y =>0, x => 3 =======> 섬개수 : 7, 최대크기 : 2
y =>9, x => 4 =======> 섬개수 : 8, 최대크기 : 2
y =>1, x => 7 =======> 섬개수 : 9, 최대크기 : 2
y =>5, x => 4 =======> 섬개수 : 10, 최대크기 : 2
y =>4, x => 9 =======> 섬개수 : 10, 최대크기 : 2
y =>9, x => 0 =======> 섬개수 : 11, 최대크기 : 2
y =>9, x => 9 =======> 섬개수 : 12, 최대크기 : 2
y =>8, x => 3 =======> 섬개수 : 13, 최대크기 : 2
y =>7, x => 2 =======> 섬개수 : 13, 최대크기 : 2
y =>3, x => 9 =======> 섬개수 : 13, 최대크기 : 3
y =>6, x => 7 =======> 섬개수 : 14, 최대크기 : 3
y =>7, x => 9 =======> 섬개수 : 14, 최대크기 : 3
y =>7, x => 3 =======> 섬개수 : 13, 최대크기 : 4
y =>2, x => 3 =======> 섬개수 : 13, 최대크기 : 4
y =>8, x => 2 =======> 섬개수 : 13, 최대크기 : 5
y =>1, x => 9 =======> 섬개수 : 14, 최대크기 : 5
y =>4, x => 5 =======> 섬개수 : 14, 최대크기 : 5
y =>7, x => 6 =======> 섬개수 : 15, 최대크기 : 5
y =>7, x => 5 =======> 섬개수 : 15, 최대크기 : 5
y =>2, x => 1 =======> 섬개수 : 16, 최대크기 : 5
y =>1, x => 8 =======> 섬개수 : 15, 최대크기 : 5

y =>1, x => 0 =======> 섬개수 : 16, 최대크기 : 5

*/
