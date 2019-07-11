import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Manager {
	int managerNumber;//初期化とともに増やす
	MakeObject object = new MakeObject();
	Others other = new Others();;
	//割り当てのアルゴリズム
	final static int CPLEX = Main.CPLEX;
	final static int SRNF = Main.SRNF;
	final static int MANAGER = Main.MANAGER;

	final static int EDF = Main.EDF;
	final static int HRF = Main.HRF;
	final static int SPTF = Main.SPTF;
	final static int CEF = Main.CEF;
	final static int ELEARN = Main.ELEARN;
	final static int RLEARN = Main.RLEARN;
	final static int LANDOM = Main.LANDOM;
	final static int ON = Main.ON;
	final static int OFF = Main.OFF;
	public static int time =1;//timeは共有

	ArrayList<Agent> agents = new ArrayList<Agent>();
	ArrayList<Task> task1 = new ArrayList<Task>();
	ArrayList<Bid> changingList = new ArrayList<Bid>();
	ArrayList<Agent> busyAgent = new ArrayList<Agent>();
	ArrayList<ArrayList<Bid>> bid = new ArrayList<ArrayList<Bid>>();
	ArrayList<ArrayList<Bid>> agentBid = new ArrayList<ArrayList<Bid>>();
	ArrayList<ArrayList<Bid>> envyList = new ArrayList<ArrayList<Bid>>();

	ArrayList<Bid> allocation = new ArrayList<Bid>();
	//bidリスト
	ArrayList<Bid> cand = new ArrayList<Bid>();
	Bid item;
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

	CPLEX cplex = new CPLEX();
	Allocate allocate = new Allocate();
	Delete delete = new Delete();
	Learning learning = new Learning();

	public Manager(int number, ArrayList<Agent> agents) {
		this.agents = agents;
		this.managerNumber = number;
	}

	public void makeTask(int taskload, Random random, double[] prob) {
		object.makeTask(task1, other.poisson(taskload, random), random, prob);
	}

	public void makeBid(Random random) {
//		Agent[] agents1 = (Agent[]) agents.toArray(new Agent[agents.size()]) ;
		//なおしたい
		object.makeBid(agents, task1, bid, agentBid, random);
		//空のリスト削除
		for(int i =0; i< bid.size(); i++) {
			if (bid.get(i).isEmpty()) {
				bid.remove(i);
				i--;
			}
		}
//		agents.clear();
//		agents = new ArrayList<>(Arrays.asList(agents1));
	}

	public void allocate(int strategy, int method, Random random, int sw, int penalty) {
		//割り当て開始
		start = System.nanoTime();
		//CPLEXで最適解を求めるとき
		if (method ==  CPLEX) {
			try {
				cplex.cplex(task1, bid, agentBid, allocation);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}else if (method == SRNF) {//SRNFを利用するとき
			while (bid.isEmpty() != true) {
				//step1 最も高い価値を宣言した集合を生成
				cand = allocate.maxValue(bid, random);
				if (cand.isEmpty()) {break;}
				//step1 希望順位で集合をソート
				cand = allocate.maxPreference(cand);
				//step2 タスク割り当て
				cand = allocate.maxValueAgain(cand);
				if (cand.size() >1)
					item = allocate.SRNF(bid, cand, random);
				else
					item = cand.get(0);

				//step3 不満解決
				for (int i = 0; i < envyList.size(); i++) {
					for (int j = 0; j < envyList.get(i).size(); j++) {
						if (envyList.get(i).get(j).taskNumber() == item.taskNumber()
								&& envyList.get(i).get(j).value() > item.value())
							changingList.add(envyList.get(i).get(j));//不満を持つエージェントリスト
					}
				}
				//不満がなくなるまで
				if (changingList.size() > 0) {
					int num = 0;
					if (changingList.size() > 1) {
						int ini = changingList.get(0).value();
						for (int i = 1; i < changingList.size(); i++) {
							if (changingList.get(i).value() > ini) {
								ini = changingList.get(i).value();
								num = i;
							}
						}
					}
					item = changingList.get(num);

					for (int k = 0; k < allocation.size(); k++) {
						if (changingList.get(num).agentNumber() == allocation.get(k).agentNumber()) {
							task1.add(allocation.get(k).task());
							allocation.get(k).task().allocated(0);
							allocation.remove(k);
							break;
						}
					}
					for (int i = 0; i < envyList.size(); i++) {
						if (envyList.get(i).get(0).agentNumber() == changingList.get(num)
								.agentNumber()) {
							if (changingList.get(num).preferentialNumber() == 1) {
								envyList.remove(i);
								break;
							}
							for (int j = 0; j < envyList.get(i).size(); j++) {
								if (envyList.get(i).get(j).preferentialNumber() >= changingList.get(num)
										.preferentialNumber()) {
									envyList.get(i).remove(j);
									j--;
								}
							}
						}
					}
					changingList.clear();
				}
				//step4
				//割り当て済みリストに追加
				allocation.add(item);
				//Agent削除
				delete.DeleteAgent(bid, agentBid, envyList, item);
				//タスク削除
				delete.DeleteTask(bid, task1, item);
			}
//			System.out.println(allocation.size());

			//step5 後処理
			for (int i = 0; i < task1.size(); i++) {
				for (int j = 0; j < envyList.size(); j++) {
					for (int k = 0; k < envyList.get(j).size(); k++) {
						if(task1.get(i) == envyList.get(j).get(k).task()) {
							changingList.add(envyList.get(j).get(k));
						}
					}
				}
				if(changingList.size() > 0) {
					int num = 0;
					if (changingList.size() > 1) {
						int ini = changingList.get(0).value();
						for (int j = 1; j < changingList.size(); j++) {
							if (changingList.get(j).value() > ini) {
								ini = changingList.get(j).value();
								num = j;
							}
						}
					}
					for (int k = 0; k < allocation.size(); k++) {
						if (changingList.get(num).agentNumber() == allocation.get(k).agentNumber()) {
							task1.add(allocation.get(k).task());
							allocation.get(k).task().allocated(0);
							allocation.remove(k);
							allocation.add(changingList.get(num));
							break;
						}
					}
					for(int k = 0; k < envyList.size();k++) {
						if(envyList.get(k).get(0).agentNumber() == changingList.get(num).agentNumber()) {
							for(int j = 0; j < envyList.get(k).size();j++) {
								if(envyList.get(k).get(j) == changingList.get(num)) {
									if (changingList.get(num).preferentialNumber() == 1) {
										envyList.remove(k);
										break;
									}
									for (int l = 0; l < envyList.get(k).size(); l++) {
										if (envyList.get(k).get(l).preferentialNumber() >= changingList.get(num)
												.preferentialNumber()) {
											envyList.get(k).remove(l);
											l--;
										}
									}
								}
							}
						}
					}
					task1.remove(i);
					changingList.clear();
					i--;
				}
			}
		}
		//割り当て終了
		end = System.nanoTime();
		//割り当て時間
		calcTime = end - start;
//		System.out.println(calcTime);

		//deadline をすぎたタスクを廃棄タスクに
		for (int i = 0; i < task1.size(); i++) {
			if (task1.get(i).elapsedTime()) {
				drop++;
//				System.out.println(drop);
				task1.remove(i);
				i--;
			}
		}
		//タスクをゲットしたAgentを確認して学習を更新
//		System.out.println(busyAgent.size());
		for (int i = 0; i < busyAgent.size(); i++) {
			if (busyAgent.get(i).elapsedTime()) {
				sum += busyAgent.get(i).getBid().reward();
				value += busyAgent.get(i).getBid().value();
				processTime += busyAgent.get(i).getBid().processTime();
				duration += busyAgent.get(i).getBid().duration();
//				System.out.println("sum "+sum);

				//Agent学習
				if (strategy == ELEARN || strategy == RLEARN) {
					if(sw == ON) {
						learning.update(busyAgent.get(i), busyAgent.get(i).reward(),busyAgent.get(i).QValue());
					}else {
						learning.update(busyAgent.get(i), busyAgent.get(i).reward());
						learning.manager_update(busyAgent.get(i), busyAgent.get(i).reward());
					}
					learning.greedy(busyAgent.get(i), random);
					learning.manager_greedy(busyAgent.get(i), random);
				}
				busyAgent.remove(i);
				i--;
			}
		}
		//Agent学習のQ値を更新
		if (strategy == ELEARN)
			for (ArrayList<Bid> b : agentBid) {
				learning.update(b.get(0).agent(), penalty);
				learning.greedy(b.get(0).agent(), random);
			}
		else if (strategy == RLEARN)//タスク希望を出していたが得られなかった方のAgentの学習を更新
			for (ArrayList<Bid> b : agentBid) {
				learning.update(b.get(0).agent(), 0);
				learning.greedy(b.get(0).agent(), random);
				learning.manager_update(b.get(0).agent(), 0);
				learning.manager_greedy(b.get(0).agent(), random);
			}

		//KEEP?
		KEEP.addAll(0, allocation);
		while (KEEP.size() > 51)//51ってなに??
			KEEP.remove(KEEP.size() - 1);
		other.calculate(KEEP);
//割り当て確認
//		for (Bid b : allocation) {
//			System.out.print(b.agentNumber()+",");
//		}
//		System.out.println();

		for (Bid b : allocation) {
			//bias変化があるとき？
			if (strategy == ELEARN) {
				b.agent().reward(other.bias(b));
				if(sw == ON) {
					b.agent().addUtility(b.reward());
					b.agent().calc();
					b.agent().bias(b.reward());
				}
			}else if (strategy == RLEARN)
				b.agent().reward(b.value());

			//busyAgentに追加
			b.agent().addBid(b);
			for (Agent agent :agents)
				if(b.agentNumber() ==agent.agentNumber()) {
					agent.busy(b.processTime());//必要時間をつっこむ
				}
//			System.out.println("aaa");
			busyAgent.add(b.agent());
		}

	}
	public double save() {
		//値の保存
		bid.clear();
		agentBid.clear();
		envyList.clear();
		allocation.clear();
//			System.out.println(time);
//		System.out.println(sum);
		sum = drop = processTime = duration = value = 0;
		allEnd = System.nanoTime();
		return allEnd;
	}

	public void agent_update(ArrayList<ArrayList<Agent>> agents)  {
		ArrayList<Agent> agents1 = new ArrayList<Agent>();
		ArrayList<Agent> change = new ArrayList<Agent>();
		for(int i =0 ; i<MANAGER; i++) {
			agents1 = agents.get(i);
			Iterator<Agent> o = agents1.iterator();
			while(o.hasNext()) {
				Agent agent = o.next();
				if(agent.managerNumber != i ) {
					o.remove();
					change.add(agent);
				}
			}
		}

		for(int i =0 ; i<MANAGER; i++) {
			agents1 = agents.get(i);
			for(Agent agent :change)
				if(agent.managerNumber == i ) {
					agents1.add(agent);
				}
		}

		}

	public double get_drop_sum() {
		return drop;
	}
}



