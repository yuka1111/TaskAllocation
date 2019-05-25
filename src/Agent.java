//Agent
import java.util.Random;

public class Agent {
	int agentNumber;
	int agentResource[] = new int[3];
	int agentStrategy;
	static int strategy = Main.strategy;
	int sum;//resourceの合計
	int counter;//Agentの初期化回数？
	double Qvalue;
	double manager_qvalue[] = new double[Main.MANAGER];
	double strategy_qvalue[] = new double[4];
	double reward;
	//utility=効用？
	double utility;
	double stdUtility;
	int[] utilityValue = new int[20];
	int status;//なに,busyで値をつっこんでるっぽ
	Bid bid;

	public Agent(int agentNum, int[] agentRec, Random random) {
		this.agentNumber = agentNum;
		this.agentResource = agentRec;
		if (strategy < 4)
			this.agentStrategy = strategy;
		else
			this.agentStrategy = random.nextInt(4);
		this.status = 0;
		for(int resource : agentResource) {
			sum += resource;
		}
		this.counter = 0;
	}

	public int[] resource() {
		return this.agentResource;
	}
	public int sum() {
		return this.sum;
	}

	public int agentNumber() {
		return this.agentNumber;
	}

	public int agentStrategy() {
		return this.agentStrategy;
	}

	public int agentStrategy(int n) {
		this.agentStrategy = n;
		return n;
	}
	public void addUtility(int n) {
		this.utilityValue[counter] = n;
		this.counter++;
		if(counter == utilityValue.length)
			counter = 0;
	}
	public Bid getBid() {
		return this.bid;
	}
	public void addBid(Bid bid) {
		this.bid = bid;
	}
	public void calc() {
		int t = 0;
		int s = 0;
		for(int i = 0; i < utilityValue.length; i++) {
			t += utilityValue[i];
			s += Math.pow(utilityValue[i],2);
		}
		this.utility = t/utilityValue.length;
		this.stdUtility = Math.sqrt(s-Math.pow(this.utility,2))/utilityValue.length;
	}

	public void bias(int rew) {
		this.Qvalue = (rew - this.utility)/stdUtility;
	}

	public void busy(int n) {
		this.status = n;
	}

	public boolean elapsedTime() {
		this.status--;
		if(status < 0){
			System.out.println("yabai" + agentNumber);
			System.exit(1);
		}
		if (this.status == 0)
			return true;
		else
			return false;
	}
	public double QValue() {
		return this.Qvalue;
	}
	public void reward(double d) {
		this.reward = d;
	}

	public double reward() {
		return this.reward;
	}

	public String toString() {
		return this.agentNumber + "," + this.agentResource[0] + "," + this.agentResource[1] + ","
				+ this.agentResource[2] + "," + this.agentStrategy;
	}

	public String Qvalue() {
		return this.strategy_qvalue[0]+ "," + this.strategy_qvalue[1] + "," + this.strategy_qvalue[2] + "," + this.strategy_qvalue[3];
	}
}
