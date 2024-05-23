package 기출문제.성적조회._02;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	int MAX = 20_001;
	int UNIT = 10_000;
	int MAX_SCORE = 300_000;
	
	HashMap<Integer, Integer> idToSeq;
	HashMap<Integer, PriorityQueue<Student>> descSearchMap;
	HashMap<Integer, PriorityQueue<Student>> ascSearchMap;
	TreeMap<Integer, PriorityQueue<Integer>> searchMap;
	
	Student[] studentInfo;
	
	int sequence;
	int[] codeForGrade = {0, 10_000_000, 20_000_000, 30_000_000};
	int[] codeForGender = {0, 1_000_000, 2_000_000, 3_000_000};
	
	public void init() {
		idToSeq = new HashMap<>();
		ascSearchMap = new HashMap<>();
		descSearchMap = new HashMap<>();
		searchMap = new TreeMap<>();
		
		studentInfo = new Student[MAX];
		sequence = 0;
	}

	public int addScore(int mId, int mGrade, char mGender[], int mScore) {
		
		int seq = 0;
		if(idToSeq.containsKey(mId)) {
			seq = idToSeq.get(mId);
		} else {
			sequence++;
			seq = sequence;
		} 
		idToSeq.put(mId, seq);
		
		int[] gradeKeys = hashForGrade(mGrade);
		
		int gender = 1;
		if(mGender[0] == 'f') {
			gender = 2;
		}
		int[] genderKeys = hashForGender(gender);
		
		Student student = new Student(mId, seq, mGrade, gender, mScore);
		studentInfo[seq] = student;
		
		int hash1 = makeHash(mGrade, gender);
		PriorityQueue<Student> pq1 = descSearchMap.getOrDefault(hash1, 
				new PriorityQueue<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o2.id, o1.id): Integer.compare(o2.score, o1.score)));
		pq1.add(student);
		descSearchMap.put(hash1, pq1);		
		
		PriorityQueue<Student> pq2 = ascSearchMap.getOrDefault(hash1, 
				new PriorityQueue<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o1.id, o2.id): Integer.compare(o1.score, o2.score)));
		pq2.add(student);
		ascSearchMap.put(hash1, pq2);		
		
		for(int i=0; i<gradeKeys.length; i++) {
			for(int j=0; j<genderKeys.length; j++) {				
				int hash2 = gradeKeys[i] + genderKeys[j] + mScore;
				PriorityQueue<Integer> pq = searchMap.getOrDefault(hash2, new PriorityQueue<>());
				pq.add(mId);
				searchMap.put(hash2, pq);
			}
		}
		
		PriorityQueue<Student> pq = descSearchMap.get(hash1);		
		while(!pq.isEmpty()) {
			Student s = pq.peek();
			if(s.removed) {
				pq.remove();
				continue;
			}
			
			return s.id;
		}
		
		
		return 0;
	}
	
	int makeHash(int grade, int gender) {
		return codeForGrade[grade] + codeForGender[gender];
	}
	
	int[] hashForGrade(int grade) {
		int[][] gradeCode = {{0,0,0,0}, 
				{10_000_000, 40_000_000, 60_000_000, 70_000_000}, 
				{20_000_000, 40_000_000, 50_000_000, 70_000_000}, 
				{30_000_000, 50_000_000, 60_000_000, 70_000_000}};		
		return gradeCode[grade];		
	}
	
	int[] hashForGender(int gender) {
		int[][] genderCode = {{0,0}, 
				{1_000_000, 3_000_000}, 
				{2_000_000, 3_000_000}};	
		return genderCode[gender];
	}	
	
	public int removeScore(int mId) {
		if(!idToSeq.containsKey(mId)) return 0;
		
		int seq = idToSeq.get(mId);
		
		Student student = studentInfo[seq];
		if(student.removed) return 0;
		
		student.removed = true;
		
		int hash = makeHash(student.grade, student.gender);
		if(!ascSearchMap.containsKey(hash)) 
			return 0;

		PriorityQueue<Student> pq = ascSearchMap.get(hash);
		while(!pq.isEmpty()) {
			Student s = pq.peek();
			if(s.removed) {
				pq.remove();
				continue;
			}
			
			return s.id;
		}
			
		
		return 0;
	}

	public int get(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
		int[][] gCode = {{0,0,0,0}, {0,0,4,6},{0,0,0,5}};  
		int gradeKey = 0;
		if(mGradeCnt == 1) {
			gradeKey = codeForGrade[mGrade[0]];
		} else if(mGradeCnt == 3) {
			gradeKey = 70_000_000;
		} else {
			int[] g = new int[2];
			for(int i=0; i<mGradeCnt; i++) {
				g[i] = mGrade[i];
			}
			gradeKey = gCode[g[0]][g[1]] * 10_000_000;
		}
	
		
		int genderKey = 0;
		if(mGenderCnt == 2) {
			genderKey = codeForGender[3];
		} else {
			if(mGender[0][0] == 'm') genderKey = codeForGender[1];
			else genderKey = codeForGender[2];			
		}
		int startKey = gradeKey + genderKey + mScore;
		int endKey = gradeKey + genderKey + MAX_SCORE;	
		
		for(Entry<Integer, PriorityQueue<Integer>> entry : searchMap.subMap(startKey, endKey).entrySet()) {
			PriorityQueue<Integer> pq = entry.getValue();
			while(!pq.isEmpty()) {
				int id = pq.peek();
				int seq = idToSeq.get(id);
				
				Student student = studentInfo[seq];
				if(student.removed) {
					pq.remove();
					continue;
				}
				
				return id;
			}
		}
		
		return 0;
	}
	
	class Student {
		int id;
		int seq;
		int grade;
		int gender;
		int score;
		boolean removed;
		public Student(int id, int seq, int grade, int gender, int score) {		
			this.id = id;
			this.seq = seq;
			this.grade = grade;
			this.gender = gender;
			this.score = score;
		}
	}
}

public class Main {
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_REMOVE = 300;
	private final static int CMD_QUERY = 400;

	private final static UserSolution usersolution = new UserSolution();

	private static void String2Char(char[] buf, String str) {
		for (int k = 0; k < str.length(); ++k)
			buf[k] = str.charAt(k);
		buf[str.length()] = '\0';
	}
	private static boolean run(BufferedReader br) throws Exception {
		int q = Integer.parseInt(br.readLine());

		int id, grade, score;
		int cmd, ans, ret;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
				case CMD_INIT:
					usersolution.init();
					okay = true;
					break;
				case CMD_ADD:
					char[] gender = new char[7];
					id = Integer.parseInt(st.nextToken());
					grade = Integer.parseInt(st.nextToken());
					String2Char(gender, st.nextToken());
					score = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.addScore(id, grade, gender, score);
					print(i, "addScore", ans, ret, id, grade, gender, score);
					if (ret != ans)
						okay = false;
					break;
				case CMD_REMOVE:
					id = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.removeScore(id);
					print(i, "removeScore", ans, ret, id);
					if (ret != ans)
						okay = false;
					break;
				case CMD_QUERY:
					int gradeCnt, genderCnt;
					int[] gradeArr = new int[3];
					char[][] genderArr = new char[2][7];
					gradeCnt = Integer.parseInt(st.nextToken());
					for (int j = 0; j < gradeCnt; ++j) {
						gradeArr[j] = Integer.parseInt(st.nextToken());
					}
					genderCnt = Integer.parseInt(st.nextToken());
					for (int j = 0; j < genderCnt; ++j) {
						String2Char(genderArr[j], st.nextToken());
					}
					score = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.get(gradeCnt, gradeArr, genderCnt, genderArr, score);
					print(i, "get", ans, ret, gradeCnt, gradeArr, genderCnt, genderArr, score);
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
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\성적조회\\sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}
