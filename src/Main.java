import java.util.ArrayList;
import java.util.Random;

public class Main {

	//戦略
	final static int EDF = 0;
	final static int HRF = 1;
	final static int SPTF = 2;
	final static int CEF = 3;
	final static int ELEARN = 4;//使うのか?？
	final static int RLEARN = 5;
	final static int LANDOM = 6;

	//agent
	//random
	final static int RANDOM = 0;
	//(high) ,bias
	final static int BIAS = 1;
	//high ,low, bias
	final static int MIXED = 2;
	//high & low
	final static int GOODBAD = 3;

	// taskのリソース量
	//リソース和
	final static int TASK_REWARD = 0;
	//ランダムな値（社会的価値）
	final static int TASK_RANDOM = 1;
	// 割り当ての共通効用
	final static int REWARD = 0;
	final static int PROCESSTIME = 1;
	//割り当てのアルゴリズム
	final static int CPLEX = 0;
	final static int SRNF = 1;
	//Agent数とbias用のリソースサイズ？
	final static int LENGTH = 3;
	final static int AGENT = 300;
	//deadlineについて
	final static int deadlineUnder = 15;
	final static int deadlineRange = 10;
	//出力形式
	final static int little = 0;
	final static int large = 1;
	final static int QVALUE = 2;
	final static int no = 3;

	final static int ON = 0;
	final static int OFF = 1;
	//Manager数
	final static int MANAGER = 2;

	static int Switch = OFF;
	static int penalty = -5;
	static int SIMULATIONTIME = 16000;
	static int Value = REWARD;
	static int bidNumber = 5;//希望順位リストのサイズ
	static int agentType = GOODBAD;
	static int taskReward = TASK_REWARD;
	static int Loop = 3;//Loop回数
	static int increment = 1;//システム負荷をいくつ先まであげてくかincremet*2ごと
	static int taskLoad = 18;//システム負荷
	static int taskLoad2 = 0;
	static int tpt = 9;//1ticのタスク発生数
	static int tpt2 = 9;//task_per_num
	static int strategy = RLEARN;
	static int method =SRNF;
	static int Output = little;

	public static void main(String[] args) {
		System.out.println("開始");
		MakeObject object = new MakeObject();
		Others other = new Others();
		Allocate allocate = new Allocate();
		Delete delete = new Delete();
		Output output = new Output();
		Learning learning = new Learning();
		CPLEX cplex = new CPLEX();
		Task TASK = new Task();

		//Outputで使う文字列
		String str = other.PrintTactics(strategy);
		String age = other.PrintAgent(agentType);
		String val = other.PrintValue(Value);
		String rew = other.PrintReward(taskReward);
		String met = other.PrintMethod(method);

		int tempValue[][] = new int[4][2];

		if (Output == large && increment != 1) {
			System.out.println("something is wrong");
			System.exit(1);
		}
		//大きいループ
		for (int numbers = 0; numbers < 1; numbers++) {
			//次に大きいループ
			for (int bias = 0; bias < 1; bias++) {
				//0:normal, 1:mix
				int bias0 =1;
				int bias1 =1;
//				if (bias == 1)
//					taskLoad = 20;

//		if(Output == little)
//			switch(bias) {
//			case 0:
//				taskLoad = 62;    //learn74
//				increment = 8;
//				break;
//			case 1:
//				taskLoad = 62;    //learn82
//				increment = 8;
//				break;
//			case 2:
//				taskLoad = 44;    //learn62
//				increment = 8;
//				break;
//			case 3:
//				taskLoad = 34;    //learn52
//				increment = 8;
//				break;
//			default:
//				break;
//			}

				//prob(<-bias)はノーマル環境(0:bias)か混合環境(1:bias)か
				double[] prob0 = other.bias(bias0);
				double[] prob1 = other.bias(bias1);

				double[] IncSum = new double[increment];
				double[] IncValue = new double[increment];
				double[] IncDrop = new double[increment];
				double[] IncWorkRate = new double[increment];
				double[] IncProcessTime = new double[increment];
				double[] IncDuration = new double[increment];
				double[][][] IncQ = new double[increment][4][2];
				double[][] IncCalcTime = new double[increment][2];

				double[] largeSum = new double[SIMULATIONTIME];
				double[] largeValue = new double[SIMULATIONTIME];
				double[] largeDrop = new double[SIMULATIONTIME];
				double[] largeWorkRate = new double[SIMULATIONTIME];
				double[] largeProcessTime = new double[SIMULATIONTIME];
				double[] largeDuration = new double[SIMULATIONTIME];
				double[][] largeCalcTime = new double[SIMULATIONTIME][2];
				double[][][] largeQ = new double[SIMULATIONTIME][4][2];
				double[][][] agentQvalue = null;
				ArrayList<Integer> agentQ = null;
				ArrayList<Agent> AgentQ = null;
				int [] loop_saved = new int[SIMULATIONTIME];

				for (int inc =0; inc < increment; inc++) {
					//システム負荷を２ずつ増加
					double[] loopSum = new double[SIMULATIONTIME];
					double[] loopValue = new double[SIMULATIONTIME];
					double[] loopDrop = new double[SIMULATIONTIME];
					double[] loopWorkRate = new double[SIMULATIONTIME];
					double[] loopProcessTime = new double[SIMULATIONTIME];
					double[] loopDuration = new double[SIMULATIONTIME];
					double[][][] loopQ = new double[SIMULATIONTIME][4][2];
					double[][] loopCalcTime = new double[SIMULATIONTIME][2];

					for(int loop = 0;loop < Loop; loop++) {
						int taskload = taskLoad;
						int taskload2 = taskLoad2;

						int task = tpt;
						int task2 = tpt2;

//						System.out.println(Task.slice+",");
						ArrayList<Bid> KEEP = new ArrayList<Bid>();
						double sum = 0;
						double value = 0;
						//廃棄タスク
						double drop = 0;
						double processTime = 0;
						double duration = 0;
						double calcTime = 0;
						double allStart = 0;
						double allEnd = 0;
						double start = 0;
						double end = 0;

						int processedNumber = 0;

						int time = 1;
						Random random = new Random(loop * 30 + 1000001);
						//agentの集合
						ArrayList<Agent> agents = new ArrayList<Agent>();
						ArrayList<Agent> agents1 = new ArrayList<Agent>();
//						Agent[] agent = new Agent[AGENT];
						ArrayList<ArrayList<Agent>> agentList = new ArrayList<ArrayList<Agent>>();
						ArrayList<Manager> managers = new ArrayList<Manager>();
						//taskのプールリスト
//						ArrayList<Task> task1 = new ArrayList<Task>();
						//希望リストの集合B
						ArrayList<Bid> changingList = new ArrayList<Bid>();
						ArrayList<Agent> busyAgent = new ArrayList<Agent>();
//						ArrayList<ArrayList<Bid>> bid = new ArrayList<ArrayList<Bid>>();
						ArrayList<ArrayList<Bid>> agentBid = new ArrayList<ArrayList<Bid>>();
						ArrayList<ArrayList<Bid>> envyList = new ArrayList<ArrayList<Bid>>();

						ArrayList<Bid> allocation = new ArrayList<Bid>();
						//bidリスト
						ArrayList<Bid> cand = new ArrayList<Bid>();
						Bid item;
						//Agent生成
						for( int i = 0; i < MANAGER; i++) {
							if(i==0) {
								object.makeAgent(i, agents, agentType, random, AGENT/2,0);
								managers.add(new Manager(i, agents));
							}

							if(i==1) {
								object.makeAgent(i, agents1, agentType, random, AGENT/2, AGENT/2);
								managers.add(new Manager(i, agents1));
							}

							agentList.add(managers.get(i).agents);
						}
//agent確認の出力
//						System.out.println(managers.get(0).agents.size()+":"+managers.get(1).agents.size());
//						ArrayList<Agent> aaa = managers.get(0).agents;
//						for(Agent a:aaa)
//							System.out.print(a.agentNumber);
//						System.out.println();
//						ArrayList<Agent> bbb = managers.get(1).agents;
//						for(Agent a:bbb)
//							System.out.print(a.agentNumber);
//						System.out.println();

						//Qvalue出力のための設定
//						if (Output == QVALUE) {
//							AgentQ = new ArrayList<Agent>();
//							agentQ = new ArrayList<Integer>();
//							for (Agent a : agent) {
//								if (a.agentResource[0] == 2 && a.agentResource[1] == 2 && a.agentResource[2] == 2) {
//									agentQ.add(a.agentNumber());
//									AgentQ.add(a);
//								}
//								if (a.agentResource[0] == 3 && a.agentResource[1] ==3 && a.agentResource[2] == 3) {
//									agentQ.add(a.agentNumber());
//									AgentQ.add(a);
//								}
//								if (a.agentResource[0] == 4 && a.agentResource[1] == 4 && a.agentResource[2] == 4) {
//									agentQ.add(a.agentNumber());
//									AgentQ.add(a);
//								}
//							}
//							agentQvalue = new double[agentQ.size()][SIMULATIONTIME][4];
//						}


						//シミュレーションタイム計測開始・実行ループ
						while (time < SIMULATIONTIME) {
							allStart = System.nanoTime();

							//Task生成
							if(time==1000 || time==7500){
								bias1+=1;
								prob1 = other.bias(bias1);
//								task= task-1;
//								task2= task2+1;
							}
//							for(int i = 0; i<MANAGER; i++) {
//								if(i==0)
//									managers.get(i).makeTask(taskload,random,prob);
//								else {
//									managers.get(i).makeTask(taskload2,random,prob);
//								}
//							}
							for(int i = 0; i<MANAGER; i++) {
							if(i==0)
								managers.get(i).makeTask_fixed(task,random,prob0);
							else {
								managers.get(i).makeTask_fixed(task2,random,prob1);
							}
						}
//task確認の出力
//							ArrayList<Task> aaa = managers.get(0).task1;
//							for(Task t:aaa)
//								System.out.print(t.taskNumber);
//							System.out.println();
//							ArrayList<Task> bbb = managers.get(1).task1;
//							for(Task a:bbb)
//								System.out.print(a.taskNumber);
//							System.out.println();

							//希望リストB作成
							for(int i = 0; i<MANAGER; i++) {


								for (int j = 0; j < managers.get(i).task1.size();j++) {
									managers.get(i).bid.add(new ArrayList<Bid>());
								}
//								System.out.println(managers.get(i).agents.size());
								managers.get(i).makeBid(random);
							}

							//割り当て
							for(int i = 0; i<MANAGER; i++) {
								managers.get(i).allocate(strategy, method, random, Switch, penalty);
							}
//							if(managers.get(0).get_drop_sum() >0)
//							System.out.println(loop+","+time+","+managers.get(0).get_drop_sum());
//							System.out.println(managers.get(1).get_drop_sum());

//							System.out.println("0,"+taskload+","+managers.get(0).agents.size()+","+managers.get(0).get_drop_sum()+","+managers.get(0).tpt
//									+", 1,"+taskload2+","+managers.get(1).agents.size()+","+managers.get(1).get_drop_sum()+","+managers.get(1).tpt);

							System.out.print(time+",0,"+task+","+managers.get(0).agents.size()+","+managers.get(0).get_drop_sum()
									+", 1,"+task2+","+managers.get(1).agents.size()+","+managers.get(1).get_drop_sum()+",");

							for(int i = 0; i<MANAGER; i++) {
								int high, low;
								high = managers.get(i).get_high_agent();
								low = managers.get(i).agents.size()-high;
								System.out.print(","+high+","+low);
							}
							System.out.println();

							for(int i = 0; i<MANAGER; i++) {
								managers.get(i).save();//drop etc 初期化
								//member_agent更新
								managers.get(i).agent_update(agentList);
							}


							//値の保存
							processedNumber = allocation.size();
							if (processedNumber != 0) {
								loop_saved[time] = loop;
								loopSum[time] = sum;
								loopValue[time] = value;
								loopDrop[time] = drop;
								loopProcessTime[time] = processTime / processedNumber;
								loopDuration[time] = duration / processedNumber;
							} else {
								loop_saved[time] = 0;
								loopSum[time] = 0;
								loopValue[time] = 0;
								loopDrop[time] = drop;
								loopProcessTime[time] = 0;
								loopDuration[time] = 0;
							}
							loopCalcTime[time][0] = calcTime;

							tempValue[0][0]=tempValue[1][0]=tempValue[2][0]=tempValue[3][0]=0;
							tempValue[0][1]=tempValue[1][1]=tempValue[2][1]=tempValue[3][1]=0;
//							for (Agent a : agent) {
//								if(a.sum() > 12)
//									tempValue[a.agentStrategy()][0]++;
//								else
//									tempValue[a.agentStrategy()][1]++;
//							}
//							for(int i = 0; i < 4; i++) {
//								loopQ[time][i][0] = tempValue[i][0];
//								loopQ[time][i][1] = tempValue[i][1];
//							}
//							if(Output == QVALUE) {
//								for(int i = 0; i < agentQ.size(); i++) {
//									for(int j = 0; j < 4; j++)
//										agentQvalue[i][time][j] = agent[agentQ.get(i)].strategy_qvalue[j];
//								}
//							}
							loopWorkRate[time] = (double) busyAgent.size() / AGENT;
							largeWorkRate[time] = (double) busyAgent.size() / AGENT;
							time++;
							if(time > 1000)
								if((time-50000) % ((SIMULATIONTIME-1)/100) == 0 && (Output == large || Output == QVALUE)) {
									TASK.slice();
									//		other.stdout();
								}

//						if(time%1000 == 0)
//							taskload += 1;
						}//while終了
//						if (Output == little) {
//							for (int i = SIMULATIONTIME - 1; i > SIMULATIONTIME - 1001; i--) {
//								IncSum[inc] += loopSum[i] / 1000;
//								IncValue[inc] += loopValue[i] / 1000;
//								IncDrop[inc] += loopDrop[i] / 1000;
//								IncProcessTime[inc] += loopProcessTime[i] / 1000;
//								IncDuration[inc] += loopDuration[i] / 1000;
//								IncWorkRate[inc] += loopWorkRate[i] / 1000;
//								IncQ[inc][0][0] += loopQ[i][0][0] / 1000;
//								IncQ[inc][1][0] += loopQ[i][1][0] / 1000;
//								IncQ[inc][2][0] += loopQ[i][2][0] / 1000;
//								IncQ[inc][3][0] += loopQ[i][3][0] / 1000;
//								IncQ[inc][0][1] += loopQ[i][0][1] / 1000;
//								IncQ[inc][1][1] += loopQ[i][1][1] / 1000;
//								IncQ[inc][2][1] += loopQ[i][2][1] / 1000;
//								IncQ[inc][3][1] += loopQ[i][3][1] / 1000;
//								IncCalcTime[inc][0] += loopCalcTime[i][0]/1000;
//								IncCalcTime[inc][1] += loopCalcTime[i][1]/1000;
////								System.out.println("incSum"+IncSum[inc]);
//							}
//						}
						if(Output == large) {
							for(int i = 0; i < SIMULATIONTIME - 1; i++) {
								largeSum[i] += loopSum[i];
								largeValue[i] += loopValue[i];
								largeDrop[i] += loopDrop[i];
								largeProcessTime[i] += loopProcessTime[i];
								largeDuration[i] += loopDuration[i];
								largeCalcTime[i][0] += loopCalcTime[i][0];
								largeCalcTime[i][1] += loopCalcTime[i][1];
								largeQ[i][0][0] += loopQ[i][0][0];
								largeQ[i][1][0] += loopQ[i][1][0];
								largeQ[i][2][0] += loopQ[i][2][0];
								largeQ[i][3][0] += loopQ[i][3][0];
								largeQ[i][0][1] += loopQ[i][0][1];
								largeQ[i][1][1] += loopQ[i][1][1];
								largeQ[i][2][1] += loopQ[i][2][1];
								largeQ[i][3][1] += loopQ[i][3][1];
//								if(i % 100 == 0)
//									System.out.println(largeSum[i]);
							}
						}

						System.out.println(loop + "周目終わり");
						if(Output == large)
							TASK.reset();
					}//loop
					if (Output == large)
						output.changeRatio(largeSum,largeValue, largeDrop, largeProcessTime, largeDuration, largeWorkRate, largeQ,largeCalcTime,
								bias, str,
								age, val, rew,met);
					if(Output == QVALUE)
						output.Qvalue(agentQvalue,agentQ,AgentQ,bias,str,age,val,rew,met);

				}//increment
				if (Output == little)
					output.taskLoad(IncSum,IncValue, IncDrop, IncProcessTime, IncDuration, IncWorkRate, IncQ,IncCalcTime, bias, str, age, val,
							rew,met);
			}//bias

		}//numbers

	}//main method

}//Main
