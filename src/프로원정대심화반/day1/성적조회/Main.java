package 프로원정대심화반.day1.성적조회;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	
	HashMap<Integer, Integer> keyById;
	HashMap<Integer, Integer> scoreById;
	HashMap<Integer, Integer> removed;
	
	HashMap<Integer, TreeMap<Student, Integer>> tmAsc;
	
	public void init() {
		keyById = new HashMap<>();
		removed = new  HashMap<>();		
		scoreById = new HashMap<>();
		tmAsc = new HashMap<>();	
	
		return;
	}

	public int addScore(int mId, int mGrade, char mGender[], int mScore) {
		int gender = 0;
		
		if(mGender[0] == 'f') {
			gender = 1;
		}
		
		//int key = mGrade * 10_000_000 + gender * 1_000_000 + mScore;
		int key = mGrade * 10 + gender;
		keyById.put(mId, key);
		scoreById.put(mId, mScore);
		
		Student student = new Student(mId, mScore);		
		
		TreeMap<Student, Integer> tm = tmAsc.getOrDefault(key, new TreeMap<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.score, o2.score)));
		tm.put(student, mId);
		tmAsc.put(key, tm);				
		
		return	tmAsc.get(key).lastKey().id;
	
	}

	public int removeScore(int mId) {		
		if(!keyById.containsKey(mId)) return 0;
		
		int key = keyById.get(mId);
		int score = scoreById.get(mId);		
		
		Student student = new Student(mId, score);		
		tmAsc.get(key).remove(student);
		
	
		if(tmAsc.get(key).isEmpty()) return 0;
		return tmAsc.get(key).firstKey().id;
	}

	public int get(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
		
		int min = Integer.MAX_VALUE;
		int ans = 0;
		for(int i=0; i<mGradeCnt; i++) {
			for(int j=0; j<mGenderCnt; j++) {
				int gender = 0;
				if(mGender[j][0] == 'f') {
					gender = 1;
				}				
				int key = mGrade[i] * 10 + gender;					
				Student find = new Student(-1, mScore);	
				
				Student found =  tmAsc.get(key).ceilingKey(find);
				if(found == null) continue;
				if(found.score < min) {
					min = found.score;
					ans = found.id;
				} else if(min == found.score) {
					ans = Math.min(ans, found.id);
				}
			}
		}
		
		
		
		return ans;
	}
	
	class Student {
		int id;
		int score;
		public Student(int id,int score) {		
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

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day1\\성적조회\\sample_input.txt"));

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