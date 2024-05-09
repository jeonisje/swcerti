package 강사자료.식당의가치;


import java.io.*;
import java.util.*;

class UserSolution {
	
	// key : 식당 이름
	// value : 해당 식당의 점수 
	static HashMap<String, Integer>score; 
	
	// key : substring (현존하는 식당에 포함된 모든 부분 문자열들) 
	// value : 이 substring을 포함한 식당중 가장 높은 점수
	static HashMap<String, Integer>maxScore; 
	
	// 그래프
	// 출발지 정보 : (1~N까지) = N + 1
	static ArrayList<Integer>[]al ; 
	
	// index : 도시 번호
	// value : 이 도시에 존재하는 식당들 
	static ArrayList<String>[] restaurants; 
	
	static int n; 
  
	public void init(int N, int M, int mRoads[][]) {
		score = new HashMap<>();
		maxScore = new HashMap<>();
		al = new ArrayList[N+1];
		restaurants = new ArrayList[N+1]; 
		for(int i = 0; i <= N; i++) {
			al[i] = new ArrayList<>();
			restaurants[i] = new ArrayList<>(); 
		}
		
		// 주어진 M개의 간선정보로 그래프 구성
		for(int i = 0; i < M; i++) {
			int from = mRoads[i][0]; 
			int to = mRoads[i][1];
			// 양방향 연결 
			al[from].add(to);
			al[to].add(from); 
		}
		n = N; 
      	return; 
	}

	public void addRestaurant(int mCityID, char mName[]) {
		// 이 mCityID번 도시에 새로운 mName을 가진 식당이 추가된다. 
		// 식당 등록
		String name = String.valueOf(mName); 
		score.put(name, 0); // 처음 들어오는 식당 = 0점부터 시작한다. 
		
		// 이 도시에는 이 식당이 존재한다.
		restaurants[mCityID].add(name); 
      	return; 
	}

	public void addValue(char mName[], int mScore) {
		// mName의 식당이 mScore만큼 점수를 누적한다. 
		String name = String.valueOf(mName); 
		
		// 점수 누적
		score.put(name, score.get(name) + mScore); 
		
		int nowScore = score.get(name); 
		
		// bestValue를 처리하기 위해 -> 어떤 식당의 점수가 갱신되었으니,
		// 이 식당의 모든 substring을 만들어보면서 -> maxScore 갱신 
		for(int i = 0; i < name.length(); i++) {
			StringBuilder sb = new StringBuilder(); 
			for(int j = i; j < name.length(); j++) {
				sb.append(name.charAt(j));
				String substring = sb.toString(); 
//				System.out.println(substring);
//				int de = 1; 
				
				// maxScore 갱신
				// 만약 처음 발생하는 substring이라면 
				if(maxScore.get(substring) == null)
					maxScore.put(substring, nowScore);
				else {
					if(nowScore > maxScore.get(substring))
						maxScore.put(substring, nowScore); 
				}
			}
		}
      	return; 
	}

	public int bestValue(char mStr[]) {
		// int de = 1; 
		// null => 왜 없을까
		// 저희 입장에서는 한번도 점수를 부여받은적이 없으면 -> maxScore에는 존재하지 않음
		// 다만, 입력에서는 무조건 존재하는 substring이 들어올거라고 했으니까
		// --> 즉, 0점을 가지고 있는 식당
		String name = String.valueOf(mStr); 
		if(maxScore.get(name) == null)
			// 존재는 하지만 한번도 평점을 받지 못한 식당! 
			return 0; 
		return maxScore.get(name);
	}

	public int regionalValue(int mCityID, int mDist) {
		// mCityID번 도시에서 출발해서, mDist이하의 도로를 거쳐서 갈 수 있는 식당들중
		// 가장 높은 점수 3개의 합 리턴
		
		// -> 탐색 
		ArrayDeque<Integer>q = new ArrayDeque<>();
		q.add(mCityID);
		
		int[] visited = new int[n+1]; 
		visited[mCityID] = 1; 
		
		// maxheap
		PriorityQueue<Integer>pq = new PriorityQueue<>(Collections.reverseOrder()); 
		
		while(!q.isEmpty()) {
			int now = q.remove();
			
			// mDist 이하로만 볼것이니
			if(visited[now]-1 > mDist)
				break; 
			
			// 위에서 걸리기 전까지 노드들에 대해서만 뭔가를 하면 된다. 
			// now라는 도시에 존재하는 모든 식당들 봐야 합니다 -> 점수 알아야 하니까
			for(String name : restaurants[now]) {
				int s = score.get(name); 
				pq.add(s); 
			}
			
			for(int next : al[now]) {
				if(visited[next] != 0)
					continue;
				visited[next] = visited[now] + 1;
				q.add(next); 
			}
		}
		
		// 여기까지 오면 -> 탐색은 종료되었고
		// mDist 이하에 존재하는 모든 식당의 점수 = 다 pq안에 있습니다. 
		
		int res = 0; 
		if(pq.size() < 3 ) {
			while(!pq.isEmpty()) 
				res += pq.remove(); 
		}
		else {
			for(int i = 0; i < 3; i++)
				res += pq.remove(); 
		}
		
		return res;
	}
}
public class Main {

}
