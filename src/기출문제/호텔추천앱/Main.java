package 기출문제.호텔추천앱;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

class UserSolution {
	
	int N;
	int[] roomCnt;
	
	ArrayList<Room>[] hotels;	
	HashMap<Integer, TreeSet<Room>> searchMap;
	  
	public void init(int N, int roomCnt[]) {
      	this.N = N;
      	hotels = new ArrayList[N+1];
      	searchMap  = new HashMap<>();
      	
      	for(int i=1; i<=N; i++) {
      		hotels[i] = new ArrayList<>();
      	}
	}

	public void addRoom(int hotelID, int roomID, int roomInfo[]) {
		Room room = new Room(roomID, roomInfo);
		
		hotels[hotelID].add(room);
		TreeSet<Room> set = searchMap.getOrDefault(room.hashCode, new TreeSet<>((o1, o2) -> o1.price == o2.price ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.price, o2.price)));
		set.add(room);
		searchMap.put(room.hashCode, set);
		
		//System.out.println("add ==> " + room.hashCode);
		
	}

	public int findRoom(int requirements[]) {
		int hashCode = requirements[2] * 10000 + requirements[3] * 100 + requirements[4] * 10 + requirements[5];
		
		//System.out.println("find ==> " + hashCode);
		
		TreeSet<Room> set = searchMap.get(hashCode);
		
		if(set == null || set.isEmpty()) return -1;
		
		for(Room room : set) {
		
			if(room.reserved.floorEntry(requirements[0]) != null && room.reserved.floorEntry(requirements[0]).getValue() > requirements[0]) {
				continue;
			}
			if(room.reserved.ceilingEntry(requirements[0]) != null && room.reserved.ceilingEntry(requirements[0]).getKey() < requirements[1]) {
				continue;
			}
			
			room.reserved.put(requirements[0], requirements[1]);
			return room.id;
		}
		
	    return -1;
	}

	public int riseCosts(int hotelID) {
	    int sum = 0;
	    
	    for(Room room : hotels[hotelID]) {
	    	int hashCode = room.hashCode;
	    	searchMap.get(hashCode).remove(room);
	    	
	    	room.price *= 1.1; 
	    	sum += room.price;
	    	
	    	searchMap.get(hashCode).add(room);
	    	
	    }
		
		return sum;
	}
	
	class Room {
		int hashCode;
		
		int id;
		int region;
		int bed;
		int type;
		int view;
		int price;
		
		TreeMap<Integer, Integer> reserved;
		
		Room(int roomID, int info[]) {
			this.id = roomID;
			this.region = info[0];
			this.bed = info[1];
			this.type = info[2];
			this.view = info[3];
			this.price = info[4];
			
			hashCode = info[0] * 10000 + info[1] * 100 + info[2] * 10 + info[3];
			reserved = new TreeMap<>();
			
		}

		@Override
		public int hashCode() {			
			return hashCode;
		}

		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			
			Room room = (Room) o;			
			return id == room.id;
		}
	}
}

public class Main {
	private final static int ADDROOM = 100;
	private final static int FINDROOM = 200;
	private final static int RISECOSTS = 300;
	private final static int END = 400;
	
	private final static UserSolution usersolution = new UserSolution();
	private static BufferedReader br;
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}
	
	private static boolean run() throws Exception {
		
		StringTokenizer st;
		boolean isCorrect = true;
		int cmd, user_ans, correct_ans;
		int roomCnt[],
			hotelID = 0, 
			roomID = 0, 
			roomInfo[] 			= new int[5],
			requirementsInfo[] 	= new int[6];
		
		int n;
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		roomCnt = new int[n];
		for(int i = 0; i < n; i++)
			roomCnt[i] = Integer.parseInt(st.nextToken());
		usersolution.init(n, roomCnt);
		for (int q = 0; ; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {
	        case ADDROOM:
	        	hotelID = Integer.parseInt(st.nextToken());
	        	roomID = Integer.parseInt(st.nextToken());
	            for (int i = 0; i < 5; i++)
	                roomInfo[i] = Integer.parseInt(st.nextToken());
	            usersolution.addRoom(hotelID, roomID, roomInfo);
	            print(q, "addRoom", q, q, hotelID, roomID, roomInfo);
	            break;
	        case FINDROOM:
	            for (int i = 0; i < 6; i++)
	                requirementsInfo[i] = Integer.parseInt(st.nextToken());
	            user_ans = usersolution.findRoom(requirementsInfo);
	            correct_ans = Integer.parseInt(st.nextToken());
	            print(q, "findRoom", correct_ans, user_ans, requirementsInfo);
	            if (user_ans != correct_ans)
	            	isCorrect = false;
	            break;
	        case RISECOSTS:
	        	hotelID = Integer.parseInt(st.nextToken());
	            user_ans = usersolution.riseCosts(hotelID);
	            correct_ans = Integer.parseInt(st.nextToken());
	            print(q, "riseCosts", correct_ans, user_ans, hotelID);
	            if (user_ans != correct_ans)
	            	isCorrect = false;
	            break;
	        case END:
	            return isCorrect;
	        default:
	        	isCorrect = false;
	            break;
	        }
		}
	}

	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//호텔추천앱//sample_input.txt"));
		 
		br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}