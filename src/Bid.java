
public class Bid {

	final static int  REWARD = Main.REWARD;
	final static int PROCESSTIME = Main.PROCESSTIME;
	final static int Value = Main.Value;
	Task task;
	Agent agent;
	int reward;
	int preferentialNumber;
	int tempPreference;
	int processTime;
	int duration;
	int value;
	int allocated;

	public Bid(Task task, Agent agent, int rew, int pNum, int tpNum, int pTime) {
		this.task = task;
		this.agent = agent;
		this.reward = rew;
		this.preferentialNumber = pNum;
		this.tempPreference = tpNum;
		this.processTime = pTime;
		if (Value == REWARD)
			this.value = rew;
		else if (Value == PROCESSTIME) {
			//this.value = -pTime;
			this.value = rew / pTime;
		}
		this.allocated = 0;
		this.duration = pTime + (task.generateTime() - task.deadline());
	}

	public double match() {
		return this.reward / this.processTime;
	}

	public int value() {
		return this.value;
	}

	public Task task() {
		return this.task;
	}

	public Agent agent() {
		return this.agent;
	}

	public boolean allocated() {
		if (allocated == 1)
			return true;
		else
			return false;
	}

	public void allocated(int n) {
		this.allocated = n;
	}

	public int preferentialNumber() {
		return this.preferentialNumber;
	}

	public int tempPreference() {
		return this.tempPreference;
	}

	public int agentNumber() {
		return this.agent.agentNumber();
	}

	public int reward() {
		return this.reward;
	}

	public int duration() {
		return this.duration;
	}

	public int processTime() {
		return this.processTime;
	}

	public int taskNumber() {
		return this.task.taskNumber();
	}

	public int deadline() {
		return this.task.deadline();
	}

	public void prefPlus(int count) {
		this.preferentialNumber += count;
		this.tempPreference += count;
	}

	public void tempUp() {
		this.tempPreference--;
	}

	public void tempDown() {
		this.tempPreference++;
	}

	public String toString() {
		return this.taskNumber() + "," + this.value() + "," + this.preferentialNumber + "  ";
	}
}
