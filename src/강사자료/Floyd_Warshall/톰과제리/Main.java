package 강사자료.Floyd_Warshall.톰과제리;

import java.io.*;
import java.util.*;

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int[][] dist;
	static int[][] distb; // 베리의 괴롭힘(페널티)을 누적한 최단거리
	static Berry[] b;     // 베리의 1. 괴롭힘 시간, 2. 마을을 저장
	static int[] btime;   // DAT - key : 마을, value : 괴롭힘 시간
	static int n, m, q;

	static class Berry implements Comparable<Berry> {
		int time, town;

		Berry(int time, int town) {
			this.time = time;
			this.town = town;
		}

		public int compareTo(Berry next) {
			if (time < next.time)
				return -1;
			if (time > next.time)
				return 1;
			if (town < next.town)
				return -1;
			if (town > next.town)
				return 1;
			return 0;
		}
	}

	static void fw() {
		for (int k = 1; k <= n; k++) {
			// k = 경유지 -> 방해받는 시간이 적은 도시 순으로 확인, 최단거리 갱신
			int idx = b[k].town;   // 현재 베리의 마을 번호
			int ptime = b[k].time; // 현재 베리의 괴롭힘 시간
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					// 만약 경유지를 통해 i-> idx, idx-> j 로 갈 수 없다면 pass
					if (dist[i][idx] == Integer.MAX_VALUE || dist[idx][j] == Integer.MAX_VALUE)
						continue;
					// i -> idx -> j 경유해서 가는 것이 더 빠르다면 최단거리 갱신
					if (dist[i][j] > dist[i][idx] + dist[idx][j])
						dist[i][j] = dist[i][idx] + dist[idx][j];
					// 각 마을 중 베리가 괴롭힐 수 있는 시간이 가장 긴 것을 찾고
					int maxtime = Math.max(btime[i], Math.max(btime[j], ptime));
					// (i -> j 까지의 최단거리 + 베리의 괴롭힘 시간)이 i -> j 까지의 현재까지의 누적 경로보다 작다면 갱신 
					if (distb[i][j] > dist[i][j] + maxtime)
						distb[i][j] = dist[i][j] + maxtime;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {

		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken()); // 도시의 개수
		m = Integer.parseInt(st.nextToken()); // 도로의 개수
		q = Integer.parseInt(st.nextToken()); // 질문의 개수

		dist = new int[n + 1][n + 1];  
		distb = new int[n + 1][n + 1]; 
		b = new Berry[n + 1]; 
		btime = new int[n + 1];

		// 개가 괴롭히는 (시간, 도시) 입력
		st = new StringTokenizer(br.readLine());
		for (int i = 1; i <= n; i++) {
			int time = Integer.parseInt(st.nextToken());
			btime[i] = time;
			b[i] = new Berry(time, i);
		}

		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (i == j) {
					dist[i][j] = 0; // 자기 자신은 방문하지 않음.
					distb[i][j] = 0;
				} else {
					dist[i][j] = Integer.MAX_VALUE; // djikstra처럼 최대값부터
					distb[i][j] = Integer.MAX_VALUE;
				}
			}
		}

		// 마을 입력
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			int cost = Integer.parseInt(st.nextToken());
			// 인접행렬
			dist[from][to] = cost;
			dist[to][from] = cost;
		}

		// Berry에게 괴롭힘을 받는 시간이 적은것부터 정렬
		Arrays.sort(b, 1, n + 1);

		// 플로이드 와샬
		fw();

		for (int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine());
			int s = Integer.parseInt(st.nextToken());
			int t = Integer.parseInt(st.nextToken());
			// s->t 까지 갈수 없다면 -1
			if (distb[s][t] == Integer.MAX_VALUE)
				System.out.println(-1);
			// 아니면 베리의 괴롭힘을 누적한 최단거리를 출력
			else
				System.out.println(distb[s][t]);
		}
	}
}