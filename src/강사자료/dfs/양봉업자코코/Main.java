package 강사자료.dfs.양봉업자코코;

import java.io.*;
import java.util.*;

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;

	static int N, M;
	static int[][] MAP;
	static int[][] visited;

	static int[] ydir = { -1, -2, -1, 1, 2, 1 };
	static int[] xdir = { -1, 0, 1, 1, 0, -1 };
	static int MAX = Integer.MIN_VALUE;

	static void dfs(int y, int x, int cnt, int sum) {
		if (cnt == 4) {
			if (sum > MAX)
				MAX = sum;
			return;
		}

		for (int i = 0; i < 6; i++) {
			int ny = y + ydir[i];
			int nx = x + xdir[i];
			if (ny < 0 || nx < 0 || ny >= N * 2 || nx >= M || MAP[ny][nx] == 0)
				continue;
			if (visited[ny][nx] == 1)
				continue;
			visited[ny][nx] = 1;
			dfs(ny, nx, cnt + 1, sum + MAP[ny][nx]);
			visited[ny][nx] = 0;
		}
	}

	static int[][] Ystamp = { { -1, -1 }, { -1, 1 }, { 0, 0 }, { 2, 0 } };

	static int[][] reverseYstamp = { { -2, 0 }, { 0, 0 }, { 1, -1 }, { 1, 1 } };

	static void stamp(int y, int x, int[][] dir) {
		int sum = 0;
		for (int i = 0; i < 4; i++) {
			int ny = y + dir[i][0];
			int nx = x + dir[i][1];
			if (ny < 0 || nx < 0 || ny >= N * 2 || nx >= M || MAP[ny][nx] == 0)
				continue;
			sum += MAP[ny][nx];
		}
		if (sum > MAX)
			MAX = sum;
	}

	public static void main(String[] args) throws IOException {
		int testCase = Integer.parseInt(br.readLine());
		for (int t = 1; t <= testCase; t++) {
			st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());

			MAP = new int[N * 2][M];
			visited = new int[N * 2][M];
			MAX = Integer.MIN_VALUE;

			for (int i = 0; i < N * 2; i += 2) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < M; j++) {
					if (j % 2 == 0)
						MAP[i][j] = Integer.parseInt(st.nextToken());
					else
						MAP[i + 1][j] = Integer.parseInt(st.nextToken());
				}
			}
			for (int i = 0; i < N * 2; i++) {
				for (int j = 0; j < M; j++) {
					if (MAP[i][j] != 0 && visited[i][j] == 0) {
						visited[i][j] = 1;
						dfs(i, j, 1, MAP[i][j]);
						stamp(i, j, Ystamp);
						stamp(i, j, reverseYstamp);
						visited[i][j] = 0;
					}
				}
			}
			System.out.println("#" + t + " " + MAX);
		}
	}
}
