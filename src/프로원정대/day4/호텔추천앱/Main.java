package 프로원정대.day4.호텔추천앱;


import java.io.*;
import java.util.*;

class UserSolution {	
	HashMap<Integer, TreeSet<Room>> hashForRoom;	
	ArrayList<Room>[] roomInfoByHotel;
  
	public void init(int N, int roomCnt[]) {
		hashForRoom = new HashMap<>();
		roomInfoByHotel = new ArrayList[N+1];
		for(int i=0; i<=N; i++) {
			roomInfoByHotel[i] = new ArrayList<>();
		}
      	return; 
	}

	public void addRoom(int hotelID, int roomID, int roomInfo[]) {
		Room room = new Room(hotelID, roomID, roomInfo);
		roomInfoByHotel[hotelID].add(room);
		int hashCode = room.hashCode;
		TreeSet<Room> tm = hashForRoom.getOrDefault(hashCode, 
				new TreeSet<Room>((o1, o2) -> o1.price == o2.price ? Integer.compare(o1.roomId, o2.roomId) : Integer.compare(o1.price, o2.price)));
		tm.add(room);
		hashForRoom.put(hashCode, tm);
		
      	return; 
	}

	public int findRoom(int requirements[]) {	
		int hashCode = requirements[2] * 10000 + requirements[3] * 100 + requirements[4] * 10 + requirements[5];
		
		TreeSet<Room> set = hashForRoom.get(hashCode);
		if(set == null) return -1;
		
		for(Room room : set) {			
			TreeMap<Integer,Integer> reserved = room.reserved;			
			
			Integer prev = reserved.floorKey(requirements[0]);
			Integer next = reserved.ceilingKey(requirements[0]);
			
			if(prev != null && reserved.get(prev) >  requirements[0])	continue; // 등록할 수 없는 스케쥴
			if(next != null && next <  requirements[1]) continue; // 등록할 수 없는 스케쥴
			
			reserved.put(requirements[0], requirements[1]);
			return room.roomId;
		}
		
	    return -1;
	}

	public int riseCosts(int hotelID) {
		int sum = 0;
		for(Room room : roomInfoByHotel[hotelID]) {
			hashForRoom.get(room.hashCode).remove(room);
			int newPrice = (int)(room.price * 1.1); 
			room.price = newPrice;;
			hashForRoom.get(room.hashCode).add(room);
			sum += newPrice ;
		}
		
	    return sum;
	}
	
	class Room {
		int hotelId;
		int roomId;
		int price;
		int hashCode;
		TreeMap<Integer, Integer> reserved;
		public Room(int hotelId, int roomId, int[] roomInfo) {
			this.hotelId = hotelId;
			this.roomId = roomId;
			this.price = roomInfo[4];
			this.hashCode = roomInfo[0] * 10000 + roomInfo[1] * 100 + roomInfo[2] * 10 + roomInfo[3];
			reserved = new TreeMap<>();
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
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans != ret) System.err.println("--------------------오류----------------------");
		//System.out.println("[" + q + "] " + cmd + " " + ans + "=" + ret + "(" +Arrays.deepToString(o)+ ")");
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		int TC, MARK;

		 System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day4\\호텔추천앱\\sample_input.txt"));
		 br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("ms=>" + (System.currentTimeMillis() - start));
	}
}