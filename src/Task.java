import java.util.Random;

//Task
public class Task {

	final int taskReward = Main.taskReward;

	int taskNumber;
	int taskResource[] = new int[3];
	int reward;
	int deadline;
	int generateTime;
	//割り当て済みか否か
	int allocated;
	 //なにこれ以下三つ
	int original_deadline;
	//多分だけどflagをみて報酬を変えてる makeBid参考
	int flag = 0;//slice/100の確率でflagがたつ、なんだろ
	static int slice = 0;

	public Task(int taskNum, int[] taskRes, int deadline, Random random) {
		random.nextInt(100);
		//何番目のタスクか
		this.taskNumber = taskNum;
		this.taskResource = taskRes;
		if (taskReward == 0)
			for (int i = 0; i < taskRes.length; i++)
				this.reward += taskRes[i];
			else
				this.reward = random.nextInt(90)+90;
		this.deadline = deadline;
		this.generateTime = deadline;
		this.allocated =0;
		this.original_deadline = deadline;
		if (random.nextInt(100) < slice) {
			this.flag = 1;
		}


	}

	public Task() {
	}

	public int taskNumber() {
		return this.taskNumber;
	}

	public void resetTask(int n) {
		this.taskNumber = n;
	}

	public int reward() {
		return this.reward;
	}

	public int[] resource() {
		return this.taskResource;
	}

	public int deadline() {
		return this.deadline;
	}

	public int generateTime(){
		return this.generateTime;
	}

	public void allocated(int n){
		this.allocated = n;
	}

	public boolean allocated(){
		if (allocated == 1)
			return true;
		else
			return false;
	}

	public boolean elapsedTime(){
		this.deadline--;
		if(this.deadline < 5)
			return true;
		else
			return false;
	}
	//slice関連
	public void slice() {
		slice++;
	}
	public static void slice(int n) {
		slice += n;
	}
	public void reset() {
		slice = 0;
	}

	public String toString() {
		return this.taskNumber + "," + this.taskResource[0] + "," + this.taskResource[1] + "," + this.taskResource[2]
				+ "," + this.reward + "," + this.deadline;
	}

}
