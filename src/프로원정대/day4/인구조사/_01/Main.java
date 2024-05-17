package 프로원정대.day4.인구조사._01;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int countryId = 0;
		HashMap<String, Integer> nameToId = new HashMap<>();
		HashMap<Integer, Integer> countByIds = new HashMap<>();
		
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			int k =  Integer.parseInt(st.nextToken());
			int[] ids = new int[k];
			for(int j=0; j<k; j++) {
				String country = st.nextToken();			
				if(!nameToId.containsKey(country)) {
					countryId++;
					nameToId.put(country, countryId);
					ids[j] = countryId;
				} else {
					ids[j] = nameToId.get(country);
				}				
			}
			Arrays.sort(ids);
			for(int id=0; id<k-1; id++) {
				for(int j=id+1; j<k; j++) {
					int hash = getHash(ids[id], ids[j]);
					
					int count = countByIds.getOrDefault(hash, 0);
					countByIds.put(hash, count + 1);
				}
			}
		}
		
		st = new StringTokenizer(br.readLine());		
		int M = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());
			String country1 = st.nextToken();			
			String country2 = st.nextToken();
			
			int id1 = nameToId.get(country1);
			int id2 = nameToId.get(country2);
			
			if(id1 > id2) {
				int temp = id1;
				id1 = id2; 
				id2 = temp;
			}
			
			int hash = getHash(id1, id2);
			int ans = 0;
			if(countByIds.containsKey(hash)) ans = countByIds.get(hash);
			System.out.print(ans + " "); 
		}
		
		return;
		
	}
	
	static int getHash(int num1, int num2) {
		return num1 * 10_000 + num2;
	}
}
