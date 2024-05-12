package 기출문제.주차장;


import java.io.*;
import java.util.*; 

class UserSolution {
	int MAX_CAR = 70_002;
	int BIG_VALUE = 300_001;
	
	int baseTime, baseFee, unitTime, unitFee, capacity;
	
	HashMap<Integer, Integer> idToSeq;
	
	HashMap<Integer, Car> parking;
	TreeMap<Car, Integer> waiting1;
	TreeMap<Car, Integer> waiting2;
	
	Car[] carInfo;
	
	int sequence;
	
  
	public void init(int mBaseTime, int mBaseFee, int mUnitTime, int mUnitFee, int mCapacity) {
		this.baseTime = mBaseTime;
		this.baseFee = mBaseFee;
		this.unitTime = mUnitTime;
		this.unitFee = mUnitFee;
		this.capacity = mCapacity;
		
		idToSeq = new HashMap<>();
		
		parking = new HashMap<>();
		waiting1 = new TreeMap<>((o1, o2) -> Integer.compare(o1.waitingStartTime, o2.waitingStartTime));
		waiting2 = new TreeMap<>((o1, o2) -> o2.calValue == o1.calValue ? Integer.compare(o1.waitingStartTime, o2.waitingStartTime) : Integer.compare(o2.calValue, o1.calValue));
		
		carInfo = new Car[MAX_CAR];
		
		sequence = 0;
		
		return;
	}

	public int arrive(int mTime, int mCar) {
		Car car = null;
		int seq = 0;
		if(!idToSeq.containsKey(mCar)) {
			sequence++;
			
			seq = sequence;
			idToSeq.put(mCar, sequence);
			car = new Car(seq, mTime, 0, 0, 0, 0);
			carInfo[seq] = car;
		} else {
			seq = idToSeq.get(mCar);
			car = carInfo[seq];
		}		
		
		if(parking.size() < capacity) {
			car.parkingStartTime = mTime;
			parking.put(seq, car);
		} else {
			car.waitingStartTime = mTime;
			if(car.totalParkingTime == 0 && car.totalWaitingTime == 0) {
				waiting1.put(car, seq);
			} else {
				waiting2.put(car, seq);
			}
		}
		
		return waiting1.size() + waiting2.size();
	}

	public int leave(int mTime, int mCar) {
		int seq = idToSeq.get(mCar);
		
		Car car = carInfo[seq];
		
		if(parking.containsKey(seq)) {
			car = carInfo[seq];
		
			car.parkingTime = (mTime - car.parkingStartTime);
			car.totalParkingTime += car.parkingTime;
			car.calValue = car.totalWaitingTime - car.totalParkingTime;		
			
			int parkingFee = getParkingFee(car);
			parking.remove(seq);
			

			for(int i=parking.size() ; i<capacity; i++) {
				parkingCar(mTime);
			}
						
			return parkingFee;
		} else {
			if(waiting1.containsKey(car)) {
				Car waitingCar = carInfo[seq];
				waiting1.remove(waitingCar);
				waitingCar.totalWaitingTime += (mTime - waitingCar.waitingStartTime);
				waitingCar.cal();
				
			} else {
				Car waitingCar = carInfo[seq];
				waiting2.remove(waitingCar);
				waitingCar.totalWaitingTime += (mTime - waitingCar.waitingStartTime);
				waitingCar.cal();
				
			}
						
			return -1;
		}
		
		
	}
	
	void parkingCar(int time) {		
		if(waiting1.size() == 0 && waiting2.size() == 0) return;
		
		
		Car target = null;
		
		if(waiting2.size() == 0) {
			target = waiting1.firstKey();
			waiting1.pollFirstEntry();
		} else if(waiting1.size() == 0) {
			target = waiting2.firstKey();
			waiting2.pollFirstEntry();
		} else {
			Car car1 = waiting1.firstKey();
			Car car2 = waiting2.firstKey();
			
			int waitingTime1 = time - car1.waitingStartTime;
			int waitingTime2 = car2.calValue + (time - car2.waitingStartTime);
			
			if(waitingTime1 == waitingTime2) {
				if(car1.waitingStartTime < car2.waitingStartTime) {
					target = car1;
					waiting1.pollFirstEntry();
				} else {
					target = car2;
					waiting2.pollFirstEntry();
				}
			} else if(waitingTime1 > waitingTime2) {
				target = car1;
				waiting1.pollFirstEntry();
			} else {
				target = car2;
				waiting2.pollFirstEntry();
			}
		}
		
		
		
		target.parkingStartTime = time;
		target.totalWaitingTime += (time - target.waitingStartTime);
		
		parking.put(target.seq, target);
		
		
	}
	
	int getParkingFee(Car car) {		
		if(car.parkingTime <= baseTime) return baseFee;
		return (int)(baseFee + Math.ceil((double)(car.parkingTime - baseTime) / unitTime) * unitFee); 
	}
	
	class Car {
		int seq;
		int parkingStartTime;
		int parkingTime;
		int totalParkingTime;		
		int waitingStartTime;
		int totalWaitingTime;
		int calValue;
		public Car(int seq, int parkingStartTime, int parkingTime, int totalParkingTime, int waitingStartTime, int totalWaitingTime) {
			this.seq = seq;
			this.parkingStartTime = parkingStartTime;
			this.parkingTime = parkingTime;
			this.totalParkingTime = totalParkingTime;
			this.waitingStartTime = waitingStartTime;
			this.totalWaitingTime = totalWaitingTime;
			cal();
		}
		
		private void cal() {
			this.calValue = totalWaitingTime - totalParkingTime;
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
		if(ans!=ret) System.err.println("====================오류========================");
		System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}


	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//주차장//sample_input3.txt"));
		
		
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