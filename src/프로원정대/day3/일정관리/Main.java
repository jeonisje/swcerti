package 프로원정대.day3.일정관리;

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
		
		TreeMap<Integer, Integer> lectures = new TreeMap<>();
		int count = 0;
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			int start = Integer.parseInt(st.nextToken());
			int end = Integer.parseInt(st.nextToken());
			
			
			if(lectures.floorKey(start) == null) {
				if(lectures.ceilingKey(start) == null) {
					lectures.put(start, end);
					count++;
				} else {
					int lectStart = lectures.ceilingKey(start);
					int lectEnd = lectures.get(lectStart);
					
					if(end < lectStart) {
						lectures.put(start, end);
						count++;
					}
				}
			} else {
				int lectStart1 = lectures.floorKey(start);
				int lectEnd1 = lectures.get(lectStart1);
				if(start > lectEnd1) {
					if(lectures.ceilingKey(start) == null) {
						lectures.put(start, end);
						count++;
					} else {
						int lectStart2 = lectures.ceilingKey(start);
						
						if(end < lectStart2) {
							lectures.put(start, end);
							count++;						
						}
					}
						
				}
			}
			
			
			
		}
		
		System.out.println(count);
	}
	
}
