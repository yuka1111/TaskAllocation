//学習の動き
import java.util.ArrayList;
import java.util.Random;

public class Learning {
	final static int EDF = Main.EDF;
	final static int HRF = Main.HRF;
	final static int SPTF = Main.SPTF;
	final static int CEF = Main.CEF;
	final static int ELEARN = Main.ELEARN;
	final static int RLEARN = Main.RLEARN;
	final static int LANDOM = Main.LANDOM;

	double rate = 0.1;
	double epsilon = 0.1;//0.05か0.01でいいかも
	double t = 50;
	double slesh = 0.5;

	int howmany = 4;
	final static int MANAGER = Main.MANAGER;

	public void update(Agent age, double reward) {
		age.strategy_qvalue[age.agentStrategy()] = (1 - rate) * age.strategy_qvalue[age.agentStrategy()] + rate * reward;
	}

	public void update(Agent age, double reward, double agentReward) {
		age.strategy_qvalue[age.agentStrategy()] = (1 - rate) * age.strategy_qvalue[age.agentStrategy()] + rate * (reward + agentReward);
	}

	public void manager_update(Agent age, double reward) {
		age.manager_qvalue[age.managerNumber] = (1 - rate) * age.strategy_qvalue[age.managerNumber] + rate * reward;
	}

	public int manager_greedy(Agent age, Random random) {
		int num = 0;
		if(random.nextDouble() <  epsilon)
			num = age.managerNumber(random.nextInt(MANAGER));
		else
			num = age.managerNumber(number(age.manager_qvalue, random));
		return num;
	}

	public int greedy(Agent age, Random random) {
		int type = 0;

		if (Main.strategy == LANDOM) {
			type = age.agentStrategy(random.nextInt(howmany));
		} else if (random.nextDouble() < epsilon) {
			type = age.agentStrategy(random.nextInt(howmany));
		} else {
			type = age.agentStrategy(number(age.strategy_qvalue, random));
		}

		return type;
	}

	public int sftmax(Agent age, Random random) {
		int type = 0;
		double agentStrategy[] = new double[howmany];
		double agentStrategy_sum = 0;
		for (int i = 0; i < howmany; i++) {
			agentStrategy[i] = Math.exp(age.strategy_qvalue[i] / t);
			agentStrategy_sum += agentStrategy[i];
		}
		for (int i = 0; i < howmany; i++) {
			agentStrategy[i] = agentStrategy[i] / agentStrategy_sum;
		}
		double number = random.nextDouble();
		if (number < agentStrategy[0]) {
			type = age.agentStrategy(0);
		} else if (number < agentStrategy[0] + agentStrategy[1]) {
			type = age.agentStrategy(1);
		} else if (number < agentStrategy[0] + agentStrategy[1] + agentStrategy[2]) {
			type = age.agentStrategy(2);
		} else {
			type = age.agentStrategy(3);
		}
		return type;
	}

	public void down() {
		if (this.t > slesh)
			this.t -= 0.1;
	}

	public double t() {
		return this.t;
	}

	public double max(double[] a) {

		double c = a[0];

		for (int i = 1; i < a.length; i++) {
			if (c < a[i]) {
				c = a[i];
			}
		}

		return c;
	}

	public int number(double[] a, Random random) {
		//一番大きいQ値を出す、等しい場合はランダム
		double c = a[0];
		ArrayList<Integer> s = new ArrayList<Integer>();
		s.add(0);
		for (int i = 1; i < a.length; i++) {
			if (c < a[i]) {
				c = a[i];
				s.clear();
				s.add(i);
			}
			if (c == a[i]) {
				s.add(i);
			}
		}
		if (s.size() == 1) {
			return s.get(0);
		} else {
			return s.get(random.nextInt(s.size()));
		}
	}
}