package 강사자료.당근마켓;


import java.io.*;
import java.util.*;

class UserSolution {
	
	static int carrotID; 
	// key : 3개의 tag에 대한 hashCode
	// value : 이 hashCode에 해당하는 당근 ID
	static HashMap<Integer, ArrayList<Integer>>threeTag; 
	// index : carrotID (1~30,000)
	static int[] carrotPrice; 
	// index : 1개의 tag 번호
	// value : 이 tag에 해당하는 carrot ID
	static ArrayList<Integer>[] oneTag;
	
	// 모든 tag = int로 변환
	int tagID;
	// Key : tag (string)
	// value : 이 tag에 부여한 tagID 
	static HashMap<String, Integer>hm;
	
	// index : carrot ID
	// value : 0 = 아직 존재, 1 = 팔렸다
	static int[] sold; 
	
	static int getHash(String a, String b, String c) {
		// a-b-c , c-b-a => 다 동일한 hahsCode를 가질수 있도록
		// 작 + 중 + 큰 
		
		// id값들을 가져와야합니다.
		Integer aID = hm.get(a);		
		Integer bID = hm.get(b);		
		Integer cID = hm.get(c);
		
		if(aID == null || bID == null || cID == null)
			return -1; // 유효하지 않은 조합이다
			
		// Integer[] temp = {aId, bId, cID}; 
		// Arrays.sort(temp); 
		
		//제일 작은거
		int smallest = Math.min(cID, Math.min(aID, bID));  
		int biggest = Math.max(cID, Math.max(aID, bID));  
		// 둘다 아닌것 = 중간것
		int middle = -1; 
		if(aID != smallest && aID != biggest)
			middle = aID; 
		else if(bID != smallest && bID != biggest)
			middle = bID; 
		else if(cID != smallest && cID != biggest)
			middle = cID; 
		
		// hash return 
		// tag의 범위 = 1 ~ 30 
		// 즉, 2자리수 
		// smallest << 2 x 10000
		// + middle << 2 x 100 
		// + biggest
		return smallest * 10000 + middle * 100 + biggest; 
	}
	
	public void init(int N) {
		carrotID = 1;
		tagID = 1;
		threeTag = new HashMap<>(); 
		// threetag의 범위를 알면 먼저 arraylist 활성화를 해놓곘지만... 모르니까
		carrotPrice = new int[30001]; // addCarrot의 호출 횟수 = 30,000 = 최대 3만개의 당근이 존재할 수 있다.
		// N = 총 tag의 개수
		oneTag = new ArrayList[N+1];
		for(int i = 0; i <= N; i++)
			oneTag[i] = new ArrayList<>(); 
		hm = new HashMap<>(); 
		sold = new int[30001]; 
	}
	
	public void addCarrot(int price, int tagCnt, String tagName[]) {
		// 이 price의 당근이 이 tagCnt개수의 tag들을 보유한다.
		// 당근 번호 = carrotID
		
		// 이 당근은 이 price를 가진다
		carrotPrice[carrotID] = price; 
		
		// 1 tag 등록
		for(int i = 0; i < tagCnt; i++) {
			String tag = tagName[i]; 
			// 만약 이게 처음 보는 tag라면, 일단 tagID 등록
			if(hm.get(tag) == null) 
				hm.put(tag, tagID++);
			// 이 tag의 tagID = id 
			int id = hm.get(tag); 
			// 이 id (1개의 tag id)를 가진 carrot = carrotID이다
			oneTag[id].add(carrotID); 
		}
		
		// 3 tag 조합 만들어서 다 등록
		for(int i = 0; i < tagCnt; i++) {
			for(int j = i + 1; j < tagCnt; j++) {
				for(int k = j + 1; k < tagCnt; k++) {
					String a = tagName[i];
					String b = tagName[j];
					String c = tagName[k];
					// hashCode 생성 
					int hash = getHash(a, b, c);
					// 만약 이 3개의 조합이 처음이라면 -> arraylist 활성화 
					if(threeTag.get(hash) == null)
						threeTag.put(hash, new ArrayList<>());
					// carrotID 당근은 이 hash를 가진다!
					threeTag.get(hash).add(carrotID); 
				}
			}
		}
		// 다음당근
		carrotID++; 
	}

	public int sellCarrot(String tag1, String tag2, String tag3) {
		int de = 1; 
		
		// tag1, tag2, tag3을 모두 충족하는 가장 싼 carrot을 찾으려고 합니다.
		int hash = getHash(tag1, tag2, tag3); 
		
		// 만약 hash == -1 => 이런 3개 tag 조합을 가진 당근이 없다면 -1 return
		// hash 자체는 다 등록은 되어있을수 있으니까 -> 어떤 xxxxxxx값의 hash를 생성할 수는 있다.
		if(hash == -1)
			return -1; 
		
		// 이 hash에 해당하는 모든 carrot들의 가격을 (carrotPrice) 확인하면서
		// 최소값을 갱신
		int minPrice = Integer.MAX_VALUE; 
		int minID = -1; 
		
		// 하지만, 실제로 여기서 가져오려고 할때, 하나의 당근이 이 3개의 tag를 가지고 있지 않았다면,
		// threeTag에 등록이 되어있지 않겠네요. 
		ArrayList<Integer>temp = threeTag.get(hash);
		
		if(temp == null)
			 return -1;
		
		for(int i = 0; i < temp.size(); i++) {
			int cID = temp.get(i);
			
			// 만약 팔린 당근이라면 -> 최저가 갱신에 포함되면 안됨
			if(sold[cID] == 1)
				continue;
			
			int cPrice = carrotPrice[cID]; 
			// nPrice = Math.min(cPrice, minPrice); 
			// 더 작은 값을 찾았다!
			if(cPrice < minPrice) {
				minPrice = cPrice; 
				minID = cID;
			}
		}
		
		// 없는 경우라면
		if(minPrice == Integer.MAX_VALUE)
			return -1;
		
		// 얘가 팔린다면 => 팔린다고 기록을 해줘야되네요.
		sold[minID] = 1; 
		return minPrice;
	}

	public void updatePrice(String tag1, int addPrice) {
		// 이 tag1을 가진 모든 carrot의 price만 업데이트
		int id = hm.get(tag1);
		// oneTag[id] arraylist 에 등록된 당근번호 다 보면서 쭉 업데이트 
		for(int i = 0; i < oneTag[id].size(); i++) {
			// carrot ID 
			int cID = oneTag[id].get(i); 
			carrotPrice[cID] += addPrice; 
		}
	}
}

public class Main {

}
