package 기출문제.파일저장소.강사코드;

import java.io.*;
import java.util.*;

class UserSolution {
	
	static class File {
		int start, end;
		File(int start, int end) {
			this.start = start;
			this.end = end; 
		}
	}

	// key : id
	// value : numbering
	static HashMap<Integer, Integer>hm; 
	static ArrayList<File>[]files; 
	static int idNum; 
	static int emptyMemory;
	// key : 시작 가능한 메모리의 최대 index
	// value : X 
	static TreeMap<Integer, Integer>start;
	// key : 종료 가능한 메모리의 최대 index
	// value : X 
	static TreeMap<Integer, Integer>end; 
	
	public void init(int N) {
		hm = new HashMap<>();
		files = new ArrayList[12005]; // add의 호출횟수 = 12,000
		for(int i = 0; i <= 12000; i++)
			files[i] = new ArrayList<>(); 
		idNum = 0; 
		emptyMemory = N; 
		start = new TreeMap<>();
		end = new TreeMap<>();
		// 시작은 1~N까지
		start.put(1,1); 
		end.put(N,1); 
	    return;
	}
	 
	public int add(int mId, int mSize) {
		
		// 만약 빈 칸이 mSize보다 작다면 -> 저장 불가
		if(emptyMemory < mSize)
			return -1; 
		hm.put(mId, idNum);
		
		int s = start.firstKey();
		int e = end.firstKey();
		// 지금 1열로 채워넣을수 있는 메모리 공간
		int ret = s; 
		
		// 충분히 넣을 수 있는 공간이면
		while (true) {
			if(e-s+1 > mSize) {
				files[idNum].add(new File(s, s + mSize -1));
				start.remove(s); 
				start.put(s + mSize, 1);
				emptyMemory -= mSize; 
				break; 
			}
			
			// 딱 이정도 공간이 남았다면
			else if(e-s+1 == mSize) {
				files[idNum].add(new File(s, e)); 
				start.remove(s);
				end.remove(e); 
				emptyMemory -= mSize;
				break; 
			}
			
			// 모자르다면 -> 분할해서 넣을수 있는지 확인
			else {
				files[idNum].add(new File(s, e));
				start.remove(s);
				end.remove(e); 
				
				mSize -= e-s+1;
				emptyMemory -= e-s+1; 
				
				s = start.firstKey();
				e = end.firstKey();
			}
		}
		idNum++;
	    return ret;
	}
	 
	public int remove(int mId) {
		int idNum = hm.get(mId); 
		int ret = files[idNum].size(); 
		int size = 0; 
		
		for(int i = 0; i < files[idNum].size(); i++) {
			File f = files[idNum].get(i); 
			if(end.get(f.start-1) == null) 
				start.put(f.start, 1);
			else 
				end.remove(f.start-1); 
			
			if(start.get(f.end+1) == null)
				end.put(f.end, 1);
			else
				start.remove(f.end+1); 
			size += f.end - f.start + 1; 
		}
		emptyMemory += size; 
		hm.remove(mId); 
	    return ret;
	}
	 
	public int count(int mStart, int mEnd) {
		int cnt = 0; 
		for(Map.Entry<Integer, Integer>ent : hm.entrySet()) {
			int idx = ent.getValue();
			for(int i = 0; i < files[idx].size(); i++) {
				File f = files[idx].get(i);
				if(f.end < mStart)
					continue;
				if(f.start > mEnd)
					continue;
				// 아니면 겹친다!
				cnt++;
				break; 
			}
		}
	    return cnt;
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