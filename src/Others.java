import java.util.ArrayList;
import java.util.Random;

//biasの値や出力に使用する文字列定義など
public class Others {

	final static int EDF = Main.EDF;
	final static int HRF = Main.HRF;
	final static int SPTF = Main.SPTF;
	final static int CEF = Main.CEF;
	final static int ELEARN = Main.ELEARN;
	final static int RLEARN = Main.RLEARN;
	final static int LANDOM = Main.LANDOM;

	final int RANDOM = Main.RANDOM;
	final int BIAS = Main.BIAS;
	final int MIXED = Main.MIXED;
	final int LENGTH = Main.LENGTH;

	final static int TASK_REWARD = Main.TASK_REWARD;
	final static int TASK_RANDOM = Main.TASK_RANDOM;
	final static int REWARD = Main.REWARD;
	final static int PROCESSTIME = Main.PROCESSTIME;

	final static int deadlineUnder = Main.deadlineUnder;
	final static int deadlineRange = Main.deadlineRange;
	final static int bidNumber = Main.bidNumber;

	final static int CPLEX = Main.CPLEX;
	final static int SRNF = Main.SRNF;

	double pTime;
	double reward;
	double match;
	double deadline;
	double pTimeStd;
	double rewardStd;
	double matchStd;
	double deadlineStd;

	public double[] bias(int number){
		double[] bias = new double[4];
		switch (number){
		case 0:
			bias[0] = 1;
			bias[1] = 0;
			bias[2] = 0;
			bias[3] = 0;
			break;
		case 1:
			bias[0] = 0.25;
			bias[1] = 0.25;
			bias[2] = 0.25;
			bias[3] = 0.25;
			break;
		case 2:
			bias[0] = 0.1;
			bias[1] = 0.1;
			bias[2] = 0.1;
			bias[3] = 0.7;
			break;
		case 3:
			bias[0] = 0;
			bias[1] = 0;
			bias[2] = 0;
			bias[3] = 1;
			break;
		}
		return bias;
	}
	public String PrintTactics(int n) {
		String S = null;
		System.out.print("Strategy is ");
		switch (n) {
		case EDF:
			S = "EDF";
			System.out.println("EDF");
			break;
		case HRF:
			S = "HRF";
			System.out.println("HRF");
			break;
		case SPTF:
			S = "SPTF";
			System.out.println("SPTF");
			break;
		case CEF:
			S = "CEF";
			System.out.println("SSTF");
			break;
		case ELEARN:
			S = "ELEARN";
			System.out.println("ELEARN");
			break;
		case RLEARN:
			S = "RLEARN";
			System.out.println("RLEARN");
			break;
		case LANDOM:
			S = "RANDOM";
			System.out.println("LANDOM");
			break;
		}
		return S;
	}

	public String PrintAgent(int n) {
		String S = null;
		System.out.print("Agents are ");
		switch (n) {
		case RANDOM:
			S = "RANDOM";
			System.out.println("RANDOM");
			break;
		case BIAS:
			S = "BIAS";
			System.out.println("BIAS");
			break;
		case MIXED:
			S = "MIXED";
			System.out.println("MIXED");
			break;
		case 3:
			S = "GOODBAD";
			System.out.println("GOODBAD");
			break;
		}
		return S;
	}

	public String PrintValue(int n) {
		String S = null;
		System.out.print("Value is ");
		switch (n) {
		case PROCESSTIME:
			S = "PTIME";
			System.out.println("PROCESSTIME");
			break;
		case REWARD:
			S = "REWARD";
			System.out.println("REWARD");
			break;
		}
		return S;
	}

	public String PrintReward(int n) {
		String S = null;
		System.out.print("Reward is ");
		switch (n) {
		case TASK_REWARD:
			S = "TASK_REWARD";
			System.out.println("TASK_REWARD");
			break;
		case TASK_RANDOM:
			S = "TASK_RANDOM";
			System.out.println("TASK_RANDOM");
			break;
		}
		return S;
	}
	public String PrintMethod(int n) {
		String S = null;
		System.out.print("Method is ");
		switch (n) {
		case CPLEX:
			S = "CPLEX";
			System.out.println("CPLEX");
			break;
		case SRNF:
			S = "SRNF";
			System.out.println("SRNF");
			break;
		}
		return S;
	}

	public void calculate(ArrayList<Bid> Bid){
		double a=0;
		double b=0;
		double c=0;
		double d=0;
		this.pTime=this.reward=this.match=this.deadline=0;
		for(Bid bid : Bid){
			a+=Math.pow(bid.processTime(),2);
			b+=Math.pow(bid.reward(),2);
			c+=Math.pow(bid.match(),2);
			d+=Math.pow(bid.deadline(),2);
			this.pTime+=bid.processTime();
			this.reward += bid.reward();
			this.match += bid.match();
			this.deadline += bid.deadline();
		}
		this.pTime = this.pTime / Bid.size();
		this.reward = this.reward / Bid.size();
		this.match = this.match / Bid.size();
		this.deadline = this.deadline / Bid.size();
		this.pTimeStd = Math.sqrt(a-Math.pow(this.pTime,2))/Bid.size();
		this.rewardStd = Math.sqrt(b-Math.pow(this.reward,2))/Bid.size();
		this.matchStd = Math.sqrt(c-Math.pow(this.match,2))/Bid.size();
		this.deadlineStd = Math.sqrt(d-Math.pow(this.deadline,2))/Bid.size();
	}

	public void stdout() {
		System.out.println(pTimeStd+", "+rewardStd+", " +matchStd +", "+deadlineStd);
	}

	public double bias(Bid bid){
		double result = 0;
		switch(bid.agent().agentStrategy()){
		case EDF:
			result = -(bid.deadline()-deadline)/deadlineStd;
			break;
		case HRF:
			result = (bid.reward()-reward)/rewardStd;
			break;
		case SPTF:
			result = -(bid.processTime()-pTime)/pTimeStd;
			break;
		case CEF:
			result = (bid.match()-match)/matchStd;
		}
		return result;
	}
	public int poisson(int lambda, Random random){
		double xp;
		int k = 0;
		xp = random.nextDouble();
		while (xp >= Math.exp(-lambda)) {
			xp = xp * random.nextDouble();
			k = k + 1;
		}
		return (k);
	}
}
