//cplex
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.cplex.IloCplex;

public class CPLEX {
	public void cplex(ArrayList<Task> task,
			ArrayList<ArrayList<Bid>> bid, ArrayList<ArrayList<Bid>> agentBid,
			ArrayList<Bid> taskList) throws Throwable {
		if (bid.isEmpty())
			return;
		try {
			PrintStream ps = null;
			PrintStream stdout = System.out;
			try {
		//		System.out.println("TEST CHECK POINT 1");

				ps = new PrintStream(new File("nul")); // // ←nulデバイスのＳｔｒｅａｍをつくる
				System.setOut(ps); // // ←標準出力をnulデバイスに割り当て直す
		//		System.out.println("TEST CHECK POINT 2");

			IloCplex cplex = new IloCplex();
			HashMap<Integer, Integer> age = new HashMap<Integer, Integer>();
			for (int i = 0; i < agentBid.size(); i++) {
				age.put(agentBid.get(i).get(0).agentNumber(), i);
			}
			IloIntVar[][] box = new IloIntVar[bid.size()][agentBid.size()];
			IloIntVar[][] transpose = new IloIntVar[agentBid.size()][bid
					.size()];
			IloNumExpr sumRow = cplex.intExpr();
			IloNumExpr sumColumn = cplex.intExpr();
			int[][] value = new int[bid.size()][agentBid.size()];
			int[][] order = new int[bid.size()][agentBid.size()];
			Bid[][] bidArray = new Bid[bid.size()][agentBid.size()];
			for (int i = 0; i < box.length; i++) {
				box[i] = cplex.intVarArray(agentBid.size(), 0, 1);

			}

			for (int i = 0; i < box.length; i++)
				for (int j = 0; j < box[i].length; j++)
					transpose[j][i] = box[i][j];
			for (int i = 0; i < bid.size(); i++) { // 価値と希望順位の格納
				for (int j = 0; j < bid.get(i).size(); j++) {
					bidArray[i][age.get(bid.get(i)
							.get(j).agentNumber())] = bid.get(i).get(j);
					value[i][age.get(bid.get(i)
							.get(j).agentNumber())] = bid.get(i).get(j).value();
					order[i][age.get(bid.get(i)
							.get(j).agentNumber())] = bid.get(i).get(j)
							.preferentialNumber();
				}
			}

			for (int i = 0; i < box.length; i++) { // not to generate
													// dissatisfuction
				for (int j = 0; j < box[0].length; j++) {
					if (order[i][j] <= 1)
						continue;
					else {
						for (int k = 0; k < box.length; k++) {
							if (order[k][j] >= order[i][j] || order[k][j] == 0) {

								continue;
							} else {
								IloIntExpr high = cplex.intExpr();
								high = cplex.sum(high, box[i][j]);
								for (int l = 0; l < box[0].length; l++) {
									if (value[k][l] == 0 || l == j)
										continue;
									if (value[k][l] < value[k][j]) {
										high = cplex.sum(high, box[k][l]);
									}
								}
								cplex.addLe(high, 1);
							}
						}
					}
				}
			}

			for (int i = 0; i < box.length; i++)
				// 最大化する値
				sumRow = cplex.sum(sumRow, cplex.scalProd(value[i], box[i]));

			cplex.addMaximize(sumRow);
			for (int i = 0; i < box.length; i++)
				// タスクの重複回避
				cplex.addGe(1, cplex.sum(box[i]));

			for (int i = 0; i < transpose.length; i++)
				// エージェントの重複回避
				cplex.addGe(1, cplex.sum(transpose[i]));

			for (int i = 0; i < box[0].length; i++)
				for (int j = 0; j < box.length; j++)
					if (value[j][i] == 0)
						sumColumn = cplex.sum(box[j][i], sumColumn);

			cplex.addEq(0, sumColumn);

				if (cplex.solve()) {
					System.setOut(stdout);
			//		System.out
			//				.println("Solution status = " + cplex.getStatus());////
//					System.out.println("Solution value = "
			//				+ cplex.getObjValue());
					double[][] val = new double[box.length][box[0].length];
					for (int i = 0; i < box.length; i++) {
						val[i] = cplex.getValues(box[i]);
					}
					for (int i = 0; i < val.length; i++) {
						for (int j = 0; j < val[0].length; j++) {
							if (val[i][j] == 1) {
								taskList.add(bidArray[i][j]);
								for(int l = 0; l < task.size();l++){
									if(task.get(l).taskNumber() == bidArray[i][j].taskNumber()){
										task.remove(l);
										break;
									}
								}
								for (int k = 0; k < agentBid.size(); k++) {
									if (bidArray[i][j].agentNumber() == agentBid.get(k).get(0)
													.agentNumber()) {
										agentBid.remove(k);
										break;
									}
								}
								break;
							}
						}
					}
				}
				cplex.end();
			} catch (Throwable e) {
				throw e;
			} finally {
				if (ps != null) {
					ps.close();
				}
			}


		} catch (IloException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}
}