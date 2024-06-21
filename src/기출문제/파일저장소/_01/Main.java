package 기출문제.파일저장소._01;


import java.io.*;
import java.util.*;

class UserSolution {
	int MAX = 12_001;
	int N;
	
	HashMap<Integer, Integer> idToSeq;
	
	TreeMap<File, Integer> storedMap;
	PriorityQueue<Storage> emptyQ;
	
	ArrayList<File>[] addressByFile;
	
	int sequence;
	int emtpyTotal;
	
	public void init(int N) {
		this.N = N;
		
		idToSeq = new HashMap<>();
		storedMap = new TreeMap<>((o1, o2) -> Integer.compare(o1.address, o2.address));
		emptyQ = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.address, o2.address));
		
		addressByFile = new ArrayList[MAX];
		for(int i=0; i<MAX; i++) {
			addressByFile[i] = new ArrayList<>();
		}
		sequence = 0;
		emtpyTotal = N;
	    return;
	}
	 
	// 12_000
	public int add(int mId, int mSize) {
		sequence++;
		idToSeq.put(mId, sequence);
		
		if(emtpyTotal < mSize) return -1;	
		
		int size = mSize;
		int minAddress = Integer.MAX_VALUE;
		while(!emptyQ.isEmpty()) {
			Storage s = emptyQ.remove();
			
			minAddress = Math.min(minAddress, s.address);
			int sSize = s.size;
			int addEnd = s.address + s.size;
			// 연속되는 주소인지 확인		
			
			while(!emptyQ.isEmpty() && addEnd == emptyQ.peek().address) {
				Storage s2 = emptyQ.remove();				
				sSize += s2.size;
				addEnd = s2.address + s2.size;				
			}
			
			if(sSize == size) {
				File file = new File(sequence, s.address, size);
				addressByFile[sequence].add(file);
				storedMap.put(file, mId);
				emtpyTotal -= mSize;
				return minAddress;
			} else if (size < sSize) {
				File file = new File(sequence, s.address, size);
				addressByFile[sequence].add(file);
				int address = s.address + size;
				int storageSize = sSize - size;
				
				emptyQ.add(new Storage(address, storageSize));
				storedMap.put(file, mId);
				emtpyTotal -= mSize;
				return minAddress;
			} else {
				// 파일 보다 공간이 작을 경울
				File file = new File(sequence, s.address, sSize);
				size -= sSize;
				
				addressByFile[sequence].add(file);
				storedMap.put(file, mId);
			}
		}
		
		if(storedMap.isEmpty()) {
			File file = new File(sequence, 1, size);
			storedMap.put(file, mId);
			addressByFile[sequence].add(file);
			return 1;
		}
		File last = storedMap.lastKey();
		int address = last.address + last.size;
		
		minAddress = Math.min(minAddress, address);		
	
		File file = new File(sequence, address, size);
		storedMap.put(file, mId);
		addressByFile[sequence].add(file);		
		
		emtpyTotal -= mSize;
		
	    return minAddress;
	}
	 
	// 7_000
	public int remove(int mId) {
		int seq = idToSeq.get(mId);
		File last = storedMap.lastKey();
		
		for(File f : addressByFile[seq]) {
			emtpyTotal += f.size;
			storedMap.remove(f);
			if(last.address == f.address) continue;
						
			emptyQ.add(new Storage(f.address, f.size));		
		}
		
	    return addressByFile[seq].size();
	}
	 
	// 1_000
	public int count(int mStart, int mEnd) {
		File from = storedMap.floorKey(new File(0, mStart, 0));
		File to = storedMap.floorKey(new File(0, mEnd, 0));
		
		if(from.address == to.address) {		
			if(from.address >= mStart && from.address <= mEnd 
					|| from.address + from.size > mStart && from.address + from.size <= mEnd
					|| mStart >=  from.address &&  mEnd <= from.address + from.size
					//|| mStart > from.address + from.size && mEnd <= from.address + from.size 
			) 
				return 1;
			return 0;
		}
		
		HashSet<Integer> set = new HashSet<>();
		for(Map.Entry<File, Integer> entry : storedMap.subMap(from, true, to, true).entrySet()) {
			File f = entry.getKey();
			if(f.address >= mStart && f.address <= mEnd 
					|| f.address + f.size > mStart && f.address + f.size <= mEnd  
				 )
				set.add(entry.getValue());
		}
	
	    return set.size();
	}
	
	class Storage {
		int address;
		int size;
		public Storage(int address, int size) {		
			this.address = address;
			this.size = size;
		}		
	}
	
	class File {
		int seq;
		int address;
		int size;
		public File(int seq, int address, int size) {		
			this.seq = seq;
			this.address = address;
			this.size = size;
		}
	}
}


public class Main {
	private static final int CMD_INIT	 = 1;
	private static final int CMD_ADD 	 = 2;
	private static final int CMD_REMOVE	 = 3;
	private static final int CMD_COUNT   = 4; 

	private static UserSolution userSolution = new UserSolution();
	static BufferedReader br;
	static StringTokenizer st;

	public static boolean run(BufferedReader br) throws IOException {
		int q = Integer.parseInt(br.readLine());
		int mid, msize, mstart, mend, n; 
		int cmd, ans, ret;
		boolean okay = false;

		for (int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
			case CMD_INIT:
				n = Integer.parseInt(st.nextToken());
				userSolution.init(n);
				okay = true;
				break; 
			case CMD_ADD:
				mid = Integer.parseInt(st.nextToken());
				msize = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.add(mid, msize);
				print(i, "add", ans, ret, mid, msize);
				if(ans != ret)
					okay = false;
				break; 
			case CMD_REMOVE:
				mid = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.remove(mid);
				print(i, "remove", ans, ret, mid);
				if(ans != ret)
					okay = false;
				break; 
			case CMD_COUNT:
				mstart = Integer.parseInt(st.nextToken());
				mend = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.count(mstart, mend);
				print(i, "count", ans, ret, mstart, mend);
				if(ans != ret)
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
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\파일저장소\\sample_input.txt"));


		br = new BufferedReader(new InputStreamReader(System.in));
		
		st = new StringTokenizer(br.readLine());
		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= TC; ++tc) {
			boolean result = run(br);
			int score = result ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}