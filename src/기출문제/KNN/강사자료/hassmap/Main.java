package 기출문제.KNN.강사자료.hassmap;

import java.io.*;
import java.util.*;
//Hashmap Ver.
class UserSolution {	
	static class Dot implements Comparable <Dot>{
		int y;
		int x;
		int c;
		int dist;
		int deleted; 
		Dot(int y, int x, int c, int dist) {
			this.y = y;
			this.x = x;
			this.c = c;
			this.dist = dist;
		}
		@Override
		public int compareTo(Dot o) {
			if(dist < o.dist) return -1;
			if(dist > o.dist) return 1;
			if(x < o.x) return -1;
			if(x > o.x) return 1;
			if(y < o.y) return -1;  
			if(y > o.y) return 1;
			return 0; 
		}
	}
	
	static HashMap<Integer, Integer>hm; 
	static HashMap<Integer, Dot>[][]map;
	static Dot[] dots;
	static int k;
	static int len; 
	static int idNum; 
	static int size; 
	
	int[] ydir = {-1, 1, 0, 0, -1, -1, 1, 1, 0};
	int[] xdir = {0, 0, -1, 1, -1, 1, -1, 1, 0}; 
	
	public void init(int K, int L) {
		k = K;
		len = L;
		size = 4000 / len; 
		map = new HashMap[size+1][size+1];
		hm = new HashMap<>(); 
		idNum = 0; 
		dots = new Dot[20001]; 
	    return; 
	}

	public void addSample(int mID, int mX, int mY, int mC) {
		hm.put(mID, idNum); 
		int y = mY / len;
		int x = mX / len;
		if(map[y][x] == null)
			map[y][x] = new HashMap<>(); 
		map[y][x].put(idNum, new Dot(mY, mX, mC, 0)); 
		dots[idNum] = new Dot(mY, mX, mC, 0); 
		idNum++; 
	    return; 
	}

	public void deleteSample(int mID) {
		int id = hm.get(mID);
		int y = dots[id].y / len;
		int x = dots[id].x / len;
		map[y][x].remove(id); 
	    return; 
	}

	public int predict(int mX, int mY) {
		int y = mY / len;
		int x = mX / len;
		
		PriorityQueue<Dot>pq = new PriorityQueue<>(); 
		
		for(int i = 0; i < 9; i++) {
			int ny = y + ydir[i];
			int nx = x + xdir[i];
			if(ny < 0 || nx < 0 || ny > size || nx > size)
				continue;
			if(map[ny][nx] == null)
				continue;
			for(Integer id : map[ny][nx].keySet()) {
				int dist = Math.abs(mY - dots[id].y) + Math.abs(mX - dots[id].x);
				if(dist > len)
					continue;
				pq.add(new Dot(dots[id].y, dots[id].x, dots[id].c, dist)); 
			}
		}
		
		if(pq.size() < k)
			return -1;
		
		int maxCnt = Integer.MIN_VALUE;
		int res = Integer.MAX_VALUE; 
		int[] cnt = new int[11]; 
		for(int i = 0; i < k; i++) {
			Dot now = pq.remove(); 
			cnt[now.c]++;
			if(cnt[now.c] > maxCnt) {
				maxCnt = cnt[now.c];
				res = now.c; 
			}
			if(cnt[now.c] == maxCnt) {
				if(now.c < res)
					res = now.c; 
			}
		}
	    return res; 
	}
}
public class Main {

}
