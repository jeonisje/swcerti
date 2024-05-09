package 강사자료.호텔추천앱;


import java.io.*;
import java.util.*;

class UserSolution {
	
	static class Room implements Comparable <Room>{
		int hotelID; // 어떤 호텔에 있는가? 
		int price; // 이 방의 가격 
		int ID; // 이 방의 번호 
		int roomHash; // 이 방의 type (Hashing)
		
		// 이 방에 대한 예약 상황
		TreeMap<Integer, Integer>reservations = new TreeMap<>(); 
      
		Room(int hotelID, int price, int ID, int roomHash) {
			this.hotelID = hotelID;
			this.price = price;
			this.ID = ID;
			this.roomHash = roomHash; 
		}
		@Override
		public int compareTo(Room o) {
			// 가격 낮은 순 
			if(this.price < o.price) return -1;
			if(this.price > o.price) return 1;
			// ID 낮은 순
			if(this.ID < o.ID) return -1;
			if(this.ID > o.ID) return 1;
			return 0; 
		}
	}
	
	// index : 호텔 번호
	// value : 이 호텔에 어떤 "방"들이 있는가? 
	static ArrayList<Room>hotels[]; 
	
	// KEY : 어떤 "type"의 방인가? => hashFunc을 통해서 "옵션 결합"
	// VALUE : 우선순위순으로 관리 (PQ = 특정 값을 찾아서 price 수정이 용이하지 못하니까) 
	static HashMap<Integer, TreeMap<Room, Integer>>rooms;
	
	static int HashFunc(int[] roomInfo) {
		// [0] : 지역, [1] : 침대 수, [2] : 객실 종류, [3] : 조망 종류
		int loc = roomInfo[0]; // 10까지
		int bedCnt = roomInfo[1]; // 10까지 
		int roomType = roomInfo[2]; // 4까지 
		int viewType = roomInfo[3]; // 4까지
		// 겹치지 않을 (HashMap의 성능을 O(1)로 만들어줄 수 있도록) KEY를 생성
		return loc * 10000 + bedCnt * 100 + roomType * 10 + viewType; 
	}
	
	public void init(int N, int roomCnt[]) {
		// N : 호텔의 개수 (1~N번까지 있다.)
		// roomCnt[] : K+1번 호텔에 존재하는 방의 개수 => 배열처럼 사용할 경우에 필요 
		// 저희는 HashMap으로 쓸꺼니까...딱히 필요는 없는 정보로 보입니다.
		hotels = new ArrayList[N+1];
		rooms = new HashMap<>();
		// 초기화
		for(int i = 0; i <= N; i++) 
			hotels[i] = new ArrayList<>();
	}

	// init 후, 모든 다른 API 호출 전에 쫙 다 호출됨
	// N x roomCnt[k] = 1,000 x 100 = 10만번까지 호출 가능
	// 10,000 x hashing (1) x 1 (hashmap add) x logN (treemap add, log1,log2...logN)
	// = 10,000 x log(10,000) = ~160,000 
	// => 모든 방의 type이 동일할때의 극단적인 경우
	public void addRoom(int hotelID, int roomID, int roomInfo[]) {
		// hotelID번 호텔에 roomID번 방은 roomInfo[]정보를 가진 방이다. 
		// roomInfo : [0] : 지역, [1] : 침대 수, [2] : 객실 종류, [3] : 조망 종류, [4] : 가격
		
		// 0~3번의 4개의 => 어떤 type의 방인가? 
		// type을 생성하기 위한 HashFunc
		int roomHash = HashFunc(roomInfo); // 방 type
		int price = roomInfo[4]; 
		
		// Room 생성 
		Room now = new Room(hotelID, price, roomID, roomHash);
		
		// #1. 이 호텔에는 이 방이 존재한다
		hotels[hotelID].add(now); 
		
		// #2. 이 타입에 대한 방을 저장 => price, ID순으로 우선순위를 가지도록 하는 구조
		if(rooms.get(roomHash) == null)
			rooms.put(roomHash, new TreeMap<>());
		rooms.get(roomHash).put(now, 1);
	}
	
	// 10,000 x 100,000log(100,000) (모든 방에 대해서 search)
	public int findRoom(int requirements[]) {
		// requirements = [0] : 체크인 날짜, [1] : 체크아웃 날짜, [2] : 지역, [3] : 침대 수, [4] : 객실 종류, [5] : 조망 종류
		// #1. 이 조건에 해당하는 방을 찾고,
		int checkin = requirements[0];
		int checkout = requirements[1];
		
		// 2~5까지가 room에대한 정보
		int[] info = {requirements[2], requirements[3], requirements[4], requirements[5]}; 
		int roomHash = HashFunc(info);
		
		// 예약할수 있는 개실이 없는 경우 
		// #1. 주어진 조건에 부합하는 방 자체가 없다
		if(rooms.get(roomHash) == null)
			return -1;
		
		// #2. 그 조건에 부합하는 방들중 싼거부터 확인하면서
		// 예약 가능한 방을 찾아봐야합니다.
		for(Map.Entry<Room, Integer>ent : rooms.get(roomHash).entrySet()) {
			// 싼거부터 접근해서 봅니다.
			// now = 이 requirements에 적합한 방 중 싼거를 순서대로 접근하고 있다. 
			Room now = ent.getKey(); 
			
			// -> 예약 현황에서 가서 확인
			int hotelID = now.hotelID;
			int ID = now.ID; 
			
			// 이 방에 대한 예약 현황
			// reservations[hotelID].get(ID); 
			
			// 지금 checkin 시간 기준으로,
			// #1. 바로 전 예약 확인 => 나보다 먼저 들어온 사람이 내가 checkin할려고 하는 시간보다 늦게 나가면 => 예약 못함
			Integer prev = now.reservations.floorKey(checkin);
			
			// #2. 바로 다음 예약 확인 => 나보다 뒤에 들어올 사람이 내가 checkout할려고하는 시간보다 빨리 들어오면 => 예약 못함
			Integer next = now.reservations.higherKey(checkin);
			
			// 이 전 사람의 체크아웃 시간이 내가 체크인할려고 하는 시간보다 늦으면 X 
			if(prev != null && now.reservations.get(prev) > checkin)
				continue; // 여긴 예약 못하니까 다음거 보자
			
			// 다음 사람의 체크인 시간이 내가 체크아웃 할려고하는 시간 보다 이르다면 X 
			if(next != null && next < checkout)
				continue; 
			
			// 예약이 가능하다!
			now.reservations.put(checkin, checkout); 
			// 정상 예약 가능 
			return ID; 
		}
		
		// #2. 모든 가능성을 봤는데, 예약 자체가 (시간이 겹쳐서) 불가능하다. 
	    return -1;
	}

	public int riseCosts(int hotelID) {
		// 이 hotelID번 호텔에 존재하는 모든 방들의 price를 변경
		int sum = 0; 
		for(int i =0; i < hotels[hotelID].size(); i++) {
			Room now = hotels[hotelID].get(i); 
			
			// ==> room에 이미 들어가있는 이 방에 대한 정보도 같이 업데이트
			// #1. rooms에서 얘를 삭제
			rooms.get(now.roomHash).remove(now); // log(방의 개수)
			
			// #2. price를 업데이트
			now.price += now.price / 10; 
			// ==> 업데이트가 된 방을 다시 rooms에 넣어줘야겠죠
			rooms.get(now.roomHash).put(now, 1); // log(방의 개수)
			
			// 계속해서 price가 업데이트가 될때, treemap은 우선순위를 정상적으로 유지가 될겁니다. 
			sum += now.price; 
		}
	    return sum;
	}
}

public class Main {

}
