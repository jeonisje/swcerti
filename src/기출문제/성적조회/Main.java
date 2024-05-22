package 기출문제.성적조회;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

class UserSolution {
	int MAX = 20_001;
	int UNIT = 10_000;
	int MAX_SCORE = 300_000;
	
	HashMap<Integer, Integer> idToSeq;
	HashMap<Integer, TreeSet<Student>> descSearchMap;
	HashMap<Integer, TreeSet<Student>> ascSearchMap;
	HashMap<Integer, ArrayList<Integer>> searchMap;
	
	
	Student[] studentInfo;
	int[] removed;
	
	int sequence;
	int[] codeForGrade = {0, 1_000_000, 100_000, 10_000};
	int[] codeForGender = {0, 1_000, 100};
	
	
	
	public void init() {
		idToSeq = new HashMap<>();
		ascSearchMap = new HashMap<>();
		descSearchMap = new HashMap<>();
		searchMap = new HashMap<>();
		
		studentInfo = new Student[MAX];
		removed = new int[MAX];
		sequence = 0;
	}

	public int addScore(int mId, int mGrade, char mGender[], int mScore) {
		
		int seq = 0;
		if(idToSeq.containsKey(mId)) {
			seq = idToSeq.get(mId);
			removed[seq] = 0;
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
		
		int scoreKey = mScore / UNIT;
		
		Student student = new Student(mId, seq, mGrade, gender, mScore);
		studentInfo[seq] = student;
		
		int hash1 = makeHash(mGrade, gender);
		TreeSet<Student> set1 = descSearchMap.getOrDefault(hash1, 
				new TreeSet<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o2.id, o1.id): Integer.compare(o2.score, o1.score)));
		set1.add(student);
		descSearchMap.put(hash1, set1);		
		
		TreeSet<Student> set2 = ascSearchMap.getOrDefault(hash1, 
				new TreeSet<>((o1, o2) -> o1.score == o2.score ? Integer.compare(o1.id, o2.id): Integer.compare(o1.score, o2.score)));
		set2.add(student);
		ascSearchMap.put(hash1, set2);		
		
		for(int i=0; i<gradeKeys.length; i++) {
			for(int j=0; j<genderKeys.length; j++) {				
				int hash2 = gradeKeys[i] + genderKeys[j] + scoreKey;
				ArrayList<Integer> list = searchMap.getOrDefault(hash2, new ArrayList<>());
				list.add(seq);
				searchMap.put(hash2, list);
			}
		}
		
		for(Student s : descSearchMap.get(hash1)) {
			if(removed[s.seq] == 1) continue;
			return s.id;
		}
		
		
		return 0;
	}
	
	int makeHash(int grade, int gender) {
		return codeForGrade[grade] + codeForGender[gender];
	}
	
	int[] hashForGrade(int grade) {
		int[][] makingCode = {{0,0}, {2, 3}, {1, 3}, {1, 2}};
		
		int[] result = new int[4];
		int gradeCode = codeForGrade[grade];
		
		int idx = 0;		
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				result[idx] = gradeCode + (codeForGrade[makingCode[grade][0]] * i) +  (codeForGrade[makingCode[grade][1]] * j);
				idx++;
			}
		}
		
		return result;		
	}
	
	int[] hashForGender(int gender) {
		int[] makingCode = {0, 2, 1};
		int[] result = new int[2];
		int genderCode = codeForGender[gender];
		
		for(int i=0; i<2; i++) {
			result[i] = genderCode + (codeForGender[makingCode[gender]] * i);
		}
		
		return result;
	}
	
	
	
	public int removeScore(int mId) {
		if(!idToSeq.containsKey(mId)) return 0;
		
		int seq = idToSeq.get(mId);
		if(removed[seq] == 1) return 0;
		removed[seq] = 1;
		
		Student student = studentInfo[seq];
		
		int hash = makeHash(student.grade, student.gender);
		if(!ascSearchMap.containsKey(hash)) 
			return 0;
		
		for(Student s : ascSearchMap.get(hash)) {
			if(removed[s.seq] == 1) continue;
			return s.id;
		}
		
		
			
		
		return 0;
	}

	public int get(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
		
		int gradeKey = 0;
		for(int i=0; i<mGradeCnt; i++) {
			gradeKey += codeForGrade[mGrade[i]];
		}
		
		int genderKey = 0;
		if(mGenderCnt == 2) {
			genderKey = codeForGender[1] + codeForGender[2];
		} else {
			if(mGender[0][0] == 'm') genderKey = codeForGender[1];
			else genderKey = codeForGender[2];
			
		}
		
		int min = Integer.MAX_VALUE;
		int minId = 0;
		int scoreKey = mScore / UNIT;
		
		int hash = gradeKey + genderKey;
		while(scoreKey < MAX_SCORE / UNIT) {
			int hashKey = hash + scoreKey;
			if(!searchMap.containsKey(hashKey)) {
				scoreKey++;
				continue;
			}
			
			for(int seq : searchMap.get(hashKey)) {
				if(removed[seq] == 1) continue;
				Student s =studentInfo[seq];
				if(s.score < mScore) continue;
				if(s.score < min) {
					min = s.score;
					minId = s.id;
				} else if (s.score == min) {
					minId = Math.min(minId, s.id);
				}
				
			}
			if(minId != 0) break;
			scoreKey++;
		}
		
		
		return minId;
	}
	
	class Student {
		int id;
		int seq;
		int grade;
		int gender;
		int score;
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
