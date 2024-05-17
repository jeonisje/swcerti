package 프로원정대.day4.인구조사.강사자료;

import java.io.*;
import java.util.*;

//인구 조사

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
	static StringTokenizer st;
	
	static int getHash(int a, int b) {
		// FRANCE KOREA 던, KOREA FRANCE건 둘다 동일한 hash로 관리해줘야 합니다.
		// 1 3 / 3 1 -=> 우리만의 규칙으로 해결
		// ** 작은거부터 붙여준다
		int[] arr = {a, b};
		Arrays.sort(arr);
		return arr[0] * 1000 + arr[1]; 
	}

	public static void main(String[] args) throws IOException {
		
		int id = 1; 
		// key : 국가 / 피 string
		// value : 얘에 대한 ID
		HashMap<String, Integer>hm = new HashMap<>(); 
		
		// key : hash
		// value : 이 hash에 해당하는 아이들의 수
		HashMap<Integer, Integer>res = new HashMap<>(); 
		
		int N = Integer.parseInt(br.readLine()); // 아이들의 수
		// 아이들에 대한 입력 
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			// i번째 아이가 몇개의 피가 섞여있는가? => 이 정보가 들어옵니다.
			int bloodCnt = Integer.parseInt(st.nextToken());
			
			String[]temp = new String[bloodCnt]; 
			
			// bloodCnt만큼의 피(국가)의 정보가 들어옵니다.
			for(int j = 0; j < bloodCnt; j++) {
				// 이 bloodCnt개의 피에 대해 2개의 combination들을 만들어봐야 합니다.
				temp[j] = st.nextToken(); 
				//만약 이 temp[j] 국가가 처음 나온 국가 (피) 라면 -> 이 국가를 id 등록을 해줄겁니다.
				if(hm.get(temp[j]) == null) {
					hm.put(temp[j], id);
					id++; 
				}
			}
			// int de = 1;
			
			// 조합 만들기
			for(int j = 0; j < bloodCnt; j++) {
				for(int k = j + 1; k < bloodCnt; k++) {
					int a = hm.get(temp[j]); 
					int b = hm.get(temp[k]); 
					// 요 a와 b를 가지고 hash를 생성해줄겁니다.
					// -> int로 변환할수 있는 id를 부여를 해줄겁니다.
					int hash = getHash(a, b); 
					//System.out.println(temp[j] + " " + temp[k] + " " + hash);
					//int de = 1;
					if(res.get(hash) == null)
						res.put(hash, 1);
					else
						res.put(hash, res.get(hash) + 1);
				}
				//int de = 1; 
			}
		}
		
		// Query
		int Q = Integer.parseInt(br.readLine());
		for(int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			int a = hm.get(st.nextToken());
			int b = hm.get(st.nextToken());
			int hash = getHash(a, b); 
			if(res.get(hash) == null)
				System.out.print("0 ");
			else
				System.out.print(res.get(hash) + " ");
		}
		
	}
}