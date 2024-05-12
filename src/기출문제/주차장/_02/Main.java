package 기출문제.주차장._02;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap; 

class UserSolution {
	int MAX_CAR = 70_002;
	int BIG_VALUE = 300_001;
	
	int baseTime, baseFee, unitTime, unitFee, capacity;
	
	HashMap<Integer, Integer> idToSeq;
	
	HashMap<Integer, Car> parking;
	TreeMap<Car, Integer> waiting;
	
	int[] totalWaiting;
	int[] totalParking;
	int[] status;
	
	Car[] carInfo;
	
	int parkingCount;
	int waitingCount;
	
	int sequence;
	
  
	public void init(int mBaseTime, int mBaseFee, int mUnitTime, int mUnitFee, int mCapacity) {
		this.baseTime = mBaseTime;
		this.baseFee = mBaseFee;
		this.unitTime = mUnitTime;
		this.unitFee = mUnitFee;
		this.capacity = mCapacity;
		
		idToSeq = new HashMap<>();
		
		parking = new HashMap<>();
		waiting = new TreeMap<>((o1, o2) -> o1.timeDiff == o2.timeDiff  ? Integer.compare(o1.startTime, o2.startTime) :  Integer.compare(o2.timeDiff, o1.timeDiff));		
		
		totalWaiting = new int[MAX_CAR];
		totalParking = new int[MAX_CAR];
		status = new int[MAX_CAR];
		carInfo = new Car[MAX_CAR];
		
		sequence = 0;
		parkingCount = 0;
		waitingCount = 0;
		
		return;
	}

	public int arrive(int mTime, int mCar) {
		int seq = 0;
		Car car = null;
		if(!idToSeq.containsKey(mCar)) {
			sequence++;
			
			seq = sequence;
			idToSeq.put(mCar, sequence);
			car = new Car(seq, 0, 0);
			carInfo[seq] = car;
		} else {
			seq = idToSeq.get(mCar);
			car = carInfo[seq];
		}		
		if(parkingCount < capacity) {	
			car.startTime = mTime;
			parking.put(seq, car);
			parkingCount++;
			status[seq] = 1;
		} else {
			car.timeDiff = totalWaiting[seq] - totalParking[seq] - mTime;
			car.startTime = mTime;
			waiting.put(car, seq);
			waitingCount++;
			status[seq] = 2;
		}
		
		return waitingCount;
	}

	public int leave(int mTime, int mCar) {
		int seq = idToSeq.get(mCar);
		
		Car car = carInfo[seq];
		
		if(status[seq] == 1) {
			int parkingTime = mTime - car.startTime;
			totalParking[seq] += parkingTime;
			
			int parkingFee = getParkingFee(parkingTime);
			parking.remove(seq);
			parkingCount--;
			status[seq] = 0;
			
			parkingCar(mTime);
		
			return parkingFee;
		} else {		
			Car waitingCar = carInfo[seq];
			waiting.remove(car);
			totalWaiting[seq] += (mTime - car.startTime);		
			
			waitingCount--;	
			return -1;
		}
		
		
	}
	
	void parkingCar(int time) {		
		if(waiting.size() == 0) return;
		
		Car car = waiting.pollFirstEntry().getKey();	
		int seq = car.seq;
		totalWaiting[car.seq] += (time - car.startTime);		
		car.startTime = time;	
		status[seq] = 1;
		
		parking.put(car.seq, car);
		parkingCount++;		
		waitingCount--;
	}
	
	int getParkingFee(int parkingTime) {		
		if(parkingTime <= baseTime) return baseFee;
		return (int)(baseFee + Math.ceil((double)(parkingTime - baseTime) / unitTime) * unitFee); 
	}
	
	class Car {
		int seq;
		int timeDiff;
		int startTime;
		
		public Car(int seq, int timeDiff, int startTime) {
			this.seq = seq;
			this.timeDiff = timeDiff;
			this.startTime = startTime;
		}		
	}
}

public class Main {
	private final static int CMD_INIT = 1;
	private final static int CMD_ARRIVE = 2;
	private final static int CMD_LEAVE = 3;

	private final static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception {
		int q = Integer.parseInt(br.readLine());

		int basetime, basefee, unittime, unitfee, capacity, mtime, mcar;
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
				case CMD_INIT:
					basetime = Integer.parseInt(st.nextToken());
					basefee = Integer.parseInt(st.nextToken());
					unittime = Integer.parseInt(st.nextToken());
					unitfee = Integer.parseInt(st.nextToken());
					capacity = Integer.parseInt(st.nextToken());
					usersolution.init(basetime, basefee, unittime, unitfee, capacity);					
					okay = true;
					break;
				case CMD_ARRIVE:
					mtime = Integer.parseInt(st.nextToken());
					mcar = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.arrive(mtime, mcar);
					print(i, "arrive", ans, ret, mtime, mcar);
					if (ret != ans)
						okay = false;
					break;
				case CMD_LEAVE:
					mtime = Integer.parseInt(st.nextToken());
					mcar = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.leave(mtime, mcar);
					print(i, "leave", ans, ret, mtime, mcar);
					if (ret != ans)
						okay = false;
					break;
				default:
					okay = false;
					break;
			}
		}
		return okay;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}


	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//주차장//sample_input.txt"));
		
		
		int TC, MARK;

		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}