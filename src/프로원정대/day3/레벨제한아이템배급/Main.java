package 프로원정대.day3.레벨제한아이템배급;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		//HashMap<Integer, Integer> removed = new HashMap<>();
		
		st = new StringTokenizer(br.readLine());
		TreeMap<Integer, Integer> items = new TreeMap<>();
		for(int i=0; i<M; i++) {			
			items.put(Integer.parseInt(st.nextToken()), 1);
		}
		
		
		
		st = new StringTokenizer(br.readLine());
		int[] members = new int[N];
		for(int i=0; i<N; i++) {
			members[i] = Integer.parseInt(st.nextToken());
		}
		
		int count = 0;
		for(int i=0; i<N; i++) {
			if(items.floorKey(members[i]) == null) break;
			
			int item = items.floorKey(members[i]);
			count++;
			//removed.put(item, 1);
			items.remove(item);
		}
		
		System.out.println(count);
		
	}
}
