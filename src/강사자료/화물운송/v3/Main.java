package 강사자료.화물운송.v3;



import java.io.*;
import java.util.*;

//황순영 pro님 Ver.
class UserSolution {
	static int num;
	static ArrayDeque<Node> q;
	static ArrayList<Node>[] route;
	
	static class Node {
		int id;
		int weight;
		
		Node(int id, int weight){
			this.id = id;
			this.weight = weight;
		}
	}
	
	public void init(int N, int K, int[] sCity, int[] eCity, int[] mLimit) {
		num = N;
		route = new ArrayList[num];
		q = new ArrayDeque<>();
		for (int i=0; i<num; i++)
			route[i] = new ArrayList<>();
		
		for (int i=0; i<K; i++) {
			route[sCity[i]].add(new Node(eCity[i], mLimit[i]));
		}
	}

	public void add(int sCity, int eCity, int mLimit) {
		route[sCity].add(new Node(eCity, mLimit));
	}
	
	static int bfs(Node nsCity, int eCity) {
		int[] visited = new int[num];
		for (int i=0; i<num; i++)
			visited[i] = -1;
		
		q.add(nsCity);
		
		while (!q.isEmpty()) {
			Node now = q.remove();
			
			for (Node next: route[now.id]) {
				int min = Math.min(now.weight, next.weight);
				if (visited[next.id] >= min)
					continue;
				visited[next.id] = min;
				q.add(new Node(next.id, min));
			}	
		}
		
		if (visited[eCity] == -1)
			return -1;
		
		return visited[eCity];
	}
	
	public int calculate(int sCity, int eCity) {
		return bfs(new Node(sCity, 30001), eCity);		 
	}
}
public class Main {

}
