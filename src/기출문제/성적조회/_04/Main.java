package 기출문제.성적조회._04;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	TreeMap<Student, Integer>[][] tm;
	HashMap<Integer, Integer> scoreById;
	HashMap<Integer, Integer> infoById;
	
	
	public void init() {
		tm = new TreeMap[2][4];
		for(int i=0; i<2; i++) {
			for(int j=1; j<=3; j++) {
				tm[i][j] = new TreeMap<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.score, o2.score));
			}
		}
		scoreById = new HashMap<>();
		infoById = new HashMap<>();
		
		return;
	}

	public int addScore(int mId, int mGrade, char mGender[], int mScore) {		
		int gender = 0;
		if(mGender[0] == 'f') 
			gender = 1;
		
		int info = mGrade * 10 + gender;
		Student student = new Student(mId, mScore);
		scoreById.put(mId, mScore);
		infoById.put(mId, info);
		
		tm[gender][mGrade].put(student, mScore);
					
		return tm[gender][mGrade].lastKey().id;
	}
	
	public int removeScore(int mId) {
		if(!scoreById.containsKey(mId)) return 0;
		
		int score = scoreById.get(mId);
		int info = infoById.get(mId);
		
		int gender = info % 10;
		int grade = info / 10;
		
		Student student = new Student(mId, score);
		tm[gender][grade].remove(student);
		
		if(tm[gender][grade].isEmpty()) return 0;
		
			
		return tm[gender][grade].firstKey().id;
	}

	public int get(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
		int min = Integer.MAX_VALUE;
		int id = 0;
		for(int i=0; i<mGenderCnt; i++) {
			int gender = 0;
			if(mGender[i][0] == 'f')
				gender = 1;
			
			for(int j=0; j<mGradeCnt; j++) {
				if(tm[gender][mGrade[j]].isEmpty()) continue;
				Student find = new Student(-1, mScore);
				Student found = tm[gender][mGrade[j]].ceilingKey(find);
				if(found == null) continue;
				
				if(found.score < min) {
					min = found.score;
					id = found.id;
				} else if(found.score == min) {
					id = Math.min(id, found.id);
				}
			}
		}
		
		
		return id;
	}
	
	class Student {
		int id;
		int score;
		public Student(int id, int score) {		
			this.id = id;
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