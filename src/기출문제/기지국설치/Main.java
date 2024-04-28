package 기출문제.기지국설치;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

class UserSolution {
	final int SUMMARY_UNIT = 50_000_000;
	final int MAX = 1_000_000_000;
	
	final int MAX_ADD = 25_000;
	
	HashMap<Integer, Integer> idToSeq;				// 빌딩아이디
	TreeSet<Node>[] summaryMap;				// 지도 건물 매핑
	
	Node[] nodeInfo;
	
	int[] removed;
	
	int summaryCount;
	int sequence;
	
	int total;
  
	public void init(int N, int[] mId, int[] mLocation) {
		total = N;
		
		idToSeq = new HashMap<Integer, Integer>();
		
		removed = new int[MAX_ADD];
		nodeInfo = new Node[MAX_ADD];
		//locationBySeq = new int[MAX_ADD];
		
		summaryCount = 1_000_000_000 / SUMMARY_UNIT + 1;
		
		summaryMap = new TreeSet[summaryCount];
		for(int i=0; i<summaryCount; i++) {
			summaryMap[i] = new TreeSet<>((o1, o2) -> Integer.compare(o1.location, o2.location));
			//summaryMap[i] = new ArrayList<>();
		}
		sequence = 0;
		for(int i=0; i<N; i++) {
			sequence++;
			idToSeq.put(mId[i], sequence);			
			int newLocation = mLocation[i] / SUMMARY_UNIT;
			Node node = new Node(sequence, mLocation[i], newLocation);
			nodeInfo[sequence] = node;
			summaryMap[newLocation].add(node);		
		}
	}
	
	public int add(int mId, int mLocation) {
		
		if(idToSeq.containsKey(mId)) {
			int seq = idToSeq.get(mId);
			Node node = nodeInfo[seq];
			int oldLocation = node.location / SUMMARY_UNIT;
			
			if(!summaryMap[oldLocation].contains(node)) total++;
			
			summaryMap[oldLocation].remove(node);			
			
			int newLocation = mLocation / SUMMARY_UNIT; 
			node.location = mLocation;
			node.summaryId = newLocation;
			summaryMap[newLocation].add(node);					
		} else {			
			sequence++;
			idToSeq.put(mId, sequence);
			int newLocation = mLocation / SUMMARY_UNIT; 
			Node node = new Node(sequence, mLocation, newLocation);
			nodeInfo[sequence] = node;
			summaryMap[newLocation].add(node);				
			total++;
		}
		
		return total;
	}
	
	public int remove(int mStart, int mEnd) {
		
		int start = mStart / SUMMARY_UNIT;
		int end = mEnd / SUMMARY_UNIT;
		
		int diff = end - start;
		if(diff > 3) {
			int summaryStart = start + 1;
			int summarEnd = end - 1;
			int count = 0;
			for(int i=summaryStart; i<=summarEnd; i++) {
				if(summaryMap[i].size() == 0) continue;
				count = summaryMap[i].size();
				total -= count;
				summaryMap[i] = new TreeSet<Node>((o1, o2) -> Integer.compare(o1.location, o2.location));	
			}			
			
			int location = Integer.MAX_VALUE;
			while(location > mStart) {
				if(summaryMap[start].size() == 0) break;
				Node node = summaryMap[start].last();
				location = node.location;
				if(location < mStart) continue;
				total--;
				summaryMap[start].pollLast();
			}
			location = 0;
			while(location < mEnd) {
				if(summaryMap[end].size() == 0) break;
				Node node = summaryMap[end].first();
				location = node.location;
				if(location > mEnd) continue;
				total--;
				summaryMap[end].pollFirst();
			}
			
		} else {	
			if(start == end) {
				ArrayList<Integer> list = new ArrayList<>();
				
				for(Node node : summaryMap[start]) {
					if(node.location < mStart) continue;
					if(node.location > mEnd) break;
					list.add(node.seq);
				}
				
				for(int seq : list) {
					summaryMap[start].remove(nodeInfo[seq]);
					total--;
				}
				
			} else {
				for(int i=start; i<=end; i++) {
					int location = Integer.MAX_VALUE;;
					if(i == start) {
						while(location > mStart) {
							if(summaryMap[i].size() == 0) break;
							Node node = summaryMap[i].last();
							location = node.location;
							if(location < mStart) continue;
							total--;
							summaryMap[i].pollLast();
						}
					} else {
						location = 0;
						while(location < mEnd) {
							if(summaryMap[i].size() == 0) break;
							Node node = summaryMap[i].first();
							location = node.location;
							if(location > mEnd) continue;
							total--;
							summaryMap[i].pollFirst();
						}
						
					}
					
				}	
			}
		}
		
		
		return total; 
	}
	
	public int install(int M) {
		return binarySearch(M); 
		//return 0;
	}
	
	int binarySearch(int M) {
		int start = 0;
		int end = MAX / (M-1);
		int mid = 0;
		int max = 0;
		while(start <= end) {
			mid = (start + end) / 2;
			int prevLocation = -1;
			int curSummaryIndex = 0;
			int nextSummaryIndex = 0;
			int count = 0;
			boolean skip = false;
			for(TreeSet<Node> set : summaryMap) {
				skip = false;
				if(set.size() == 0) continue;
				
				for(Node node : set) {
					if(removed[node.seq] == 1) continue;
					if(prevLocation == -1) {
						prevLocation = node.location;		
						
						count = 1;
					} else {
						nextSummaryIndex = prevLocation/ SUMMARY_UNIT + mid / SUMMARY_UNIT;
						curSummaryIndex = node.location / SUMMARY_UNIT;
						if(curSummaryIndex < nextSummaryIndex) {
							continue;
						} 
						
						if(node.location - prevLocation >= mid) {
							count++;
							prevLocation = node.location;
						}
						
						if(count > M) {
							skip = true;
							break;
						}
					}				
				}
				if(skip) break;
			}
			
			if(count > M)  start = mid + 1;			
			else if(count < M) end = mid - 1;
			else {
				start = mid + 1;		
				max = Math.max(max, mid);
			}
		}
		
		
		
		return max;
		
	}
	
	class Node {
		int seq;
		int location;
		int summaryId;
		public Node(int seq, int location, int summaryId) {
			this.seq = seq;
			this.location = location;
			this.summaryId = summaryId;
		}
	}
}


public class Main {
	private final static int CMD_INIT 		= 1;
	private final static int CMD_ADD 		= 2;
	private final static int CMD_REMOVE 	= 3;
	private final static int CMD_INSTALL	 = 4; 

	private final static UserSolution usersolution = new UserSolution();
	
	private static boolean run(BufferedReader br) throws Exception {
		StringTokenizer st;
		
		int Q; 
		int n, mid, mloc, mstart, mend, m;
		int[] midArr = new int[100];
		int[] mlocArr = new int[100];
		int cmd, ans, userAns = 0;
		boolean isCorrect = false; 
		
		st = new StringTokenizer(br.readLine());
		Q = Integer.parseInt(st.nextToken());
		
		for (int q = 0; q <Q; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
				case CMD_INIT:
					isCorrect = true;
					n = Integer.parseInt(st.nextToken());
					for(int i = 0; i < n; i++) {
						midArr[i] = Integer.parseInt(st.nextToken());
						mlocArr[i] = Integer.parseInt(st.nextToken());
					}
					usersolution.init(n, midArr, mlocArr); 
					isCorrect = true;
					break;
				case CMD_ADD:
					mid = Integer.parseInt(st.nextToken());
					mloc = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.add(mid, mloc);
					print(q, "add", ans, userAns, mid, mloc);
					if(ans != userAns)
						isCorrect = false; 
					break;
				case CMD_REMOVE:
					mstart = Integer.parseInt(st.nextToken());
					mend = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.remove(mstart, mend); 
					print(q, "remove", ans, userAns, mstart, mend);
					if(userAns != ans)
						isCorrect = false; 
					break;
				case CMD_INSTALL:
					m = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.install(m); 
					print(q, "install", ans, userAns, m);
					if(userAns != ans)
						isCorrect = false; 
					break;
				default:
					isCorrect = false;
					break;
			}
		}
		return isCorrect;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		int TC, MARK;
	
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\기지국설치\\sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}