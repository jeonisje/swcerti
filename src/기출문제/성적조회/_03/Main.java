package 기출문제.성적조회._03;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int MAX = 20_001;
	int UNIT = 10_000;
	int MAX_SCORE = 300_000;
	
	HashMap<Integer, Student> studentInfo;
	HashMap<Integer, Integer> removed;
	
	PriorityQueue<Student>[][] ascQueue;
	PriorityQueue<Student>[][] descQueue;
	
	ArrayList<Student>[] scoreGroup;
	int scoreGroupCnt; 
	
	public void init() {
		studentInfo = new HashMap<>();
		removed = new HashMap<>();
		
		ascQueue = new PriorityQueue[4][2];
		descQueue = new PriorityQueue[4][2];
		
		for(int i=1; i<4; i++) {
			for(int j=0; j<2; j++) {
				ascQueue[i][j] = new PriorityQueue<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.score, o2.score));
				descQueue[i][j] = new PriorityQueue<>((o1, o2) -> o2.score == o1.score ? Integer.compare(o2.id, o1.id) : Integer.compare(o2.score, o1.score));
			}
		}
		scoreGroupCnt = MAX_SCORE/UNIT+1;
		scoreGroup = new ArrayList[scoreGroupCnt];
		
		for(int i=0; i<scoreGroupCnt; i++) {
			scoreGroup[i] = new ArrayList<>();
		}
		
	}

	public int addScore(int mId, int mGrade, char mGender[], int mScore) {		
		if(removed.containsKey(mId)) removed.remove(mId);
		
		int gender = 0;
		if(mGender[0] == 'f') {
			gender = 1;
		}
		Student student = new Student(mId,  mGrade, gender, mScore);
		ascQueue[mGrade][gender].add(student);
		descQueue[mGrade][gender].add(student);
		studentInfo.put(mId, student);

		
		int scoreLoc = mScore / UNIT;
		scoreGroup[scoreLoc].add(student);
		
		while(!descQueue[mGrade][gender].isEmpty()) {
			Student s = descQueue[mGrade][gender].peek();
			if(removed.containsKey(s.id)) {
				descQueue[mGrade][gender].remove();
				continue;
			}
			
			return s.id;
		}
			
		return 0;
	}
	
	public int removeScore(int mId) {
		if(!studentInfo.containsKey(mId)) return 0;		
		if(removed.containsKey(mId)) return 0;
		
		removed.put(mId, mId);
		Student student = studentInfo.get(mId);
		
		
		while(!ascQueue[student.grade][student.gender].isEmpty()) {
			Student s = ascQueue[student.grade][student.gender].peek();
			if(removed.containsKey(s.id)) {
				ascQueue[student.grade][student.gender].remove();
				continue;
			}
			
			return s.id;
		}
			
		return 0;
	}

	public int get(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
		int[] targetGrade = new int[4];
		int[] targetGender = new int[2];
		
		for(int i=0; i<mGradeCnt; i++) {
			targetGrade[mGrade[i]] = 1;
		}
		
		for(int i=0; i<mGenderCnt; i++) {
			if(mGender[i][0] == 'm')
				targetGender[0] = 1;
			else
				targetGender[1] = 1;
		}
		
		int minId =  0;
		int minScore = Integer.MAX_VALUE;
		for(int findGroup = mScore / UNIT; findGroup <scoreGroupCnt; findGroup++) {
			if(scoreGroup[findGroup].isEmpty()) continue;
			//Collections.sort(scoreGroup[findGroup], ((o1, o2) -> o1.score==o2.score ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.score, o2.score)));
			for(Student s : scoreGroup[findGroup]) {
				if(targetGrade[s.grade] == 0) 
					continue;
				if(targetGender[s.gender] == 0) 
					continue;
				if(s.score < mScore) 
					continue;
				if(removed.containsKey(s.id)) 
					continue;
				
				if(s.score < minScore) {
					minScore = s.score;
					minId = s.id;
				} else if(s.score == minScore) {
					minId = Math.min(minId, s.id);
				}
				
				if(minId != 0) return minId;
			}
			
			
		}
		
	/*
		
		while(findGroup < scoreGroupCnt) {
			for(Student s : scoreGroup[findGroup]) {
				if(targetGrade[s.grade] == 0) continue;
				if(targetGender[s.gender] == 0) continue;
				if(s.score < mScore) continue;
				
				if(minScore < s.score) {
					minScore = s.score;
					minId = s.id;
				} else if(minScore == s.score) {
					minId = Math.min(minId, s.id);
				}
			}
			
			if(minId != 0) return minId;
			findGroup++;
		}
		*/
		
		
		return 0;
	}
	
	class Student {
		int id;
		int grade;
		int gender;
		int score;
		public Student(int id, int grade, int gender, int score) {		
			this.id = id;
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
		if(ans!=ret)  System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\성적조회\\sample_input2.txt"));

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
