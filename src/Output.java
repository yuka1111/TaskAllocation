import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

//出力の記述詳細
public class Output {

	final static int slice = 100;
	final static int EDF = Main.EDF;
	final static int HRF = Main.HRF;
	final static int SPTF = Main.SPTF;
	final static int CEF = Main.CEF;

	static int loop = Main.Loop;

	public void changeRatio(double[] sum,double[] value, double[] drop, double[] pTime, double[] duration, double[] rate, double[][][] q,double[][] calc,
			int bias, String str, String age, String val, String rew, String met) {
		try {
			FileWriter fw = new FileWriter("/Users/y.ishihara/Documents/タスク割り当て/Output/" + str
					+","+ age +","+ val+"," + rew+"," +met+ ".csv", true); // ※１
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			double[] Sum = new double[sum.length / slice];
			double[] Value = new double[sum.length / slice];
			double[] Drop = new double[sum.length / slice];
			double[] PTime = new double[sum.length / slice];
			double[] Duration = new double[sum.length / slice];
			double[] Rate = new double[sum.length / slice];
			double[][][] Q = new double[sum.length][4][2];
			double[][] Calc = new double[sum.length][2];
			for (int i = 0; i < Sum.length; i++) {
				for (int j = i * slice; j < i * slice + slice; j++) {
					Sum[i] += sum[j] / slice;
					Drop[i] += drop[j] / slice;
					Value[i] += value[j] / slice;
					PTime[i] += pTime[j] / slice;
					Duration[i] += duration[j] / slice;
					Rate[i] += rate[j] / slice;
					Calc[i][0] += calc[j][0] / slice;
					Q[i][0][0] += q[j][0][0] / slice;
					Q[i][1][0] += q[j][1][0]/ slice;
					Q[i][2][0] += q[j][2][0] / slice;
					Q[i][3][0] += q[j][3][0] / slice;
					Q[i][0][1] += q[j][0][1] / slice;
					Q[i][1][1] += q[j][1][1]/ slice;
					Q[i][2][1] += q[j][2][1] / slice;
					Q[i][3][1] += q[j][3][1] / slice;
				}
			}
			pw.println(bias);
			if (Main.strategy < 4) {
				pw.println(",reward,drop,value,pTime,duration,workRate,calc,CALC");
				for (int i = 0; i < Sum.length; i++) {

					pw.println(i * slice + "," + Sum[i] / loop + "," + Drop[i] / loop + "," + Value[i] / loop + "," + PTime[i] / loop + ","
							+ Duration[i] / loop + "," + Rate[i] / loop+"," + Calc[i][0] / (loop*1000000000) + "," + Calc[i][1]/(loop*1000000000));

				}
			} else {
				pw.println(",reward,drop,value,pTime,duration,workRate,calc,CALC,,EDF,HRF,SPTF,CEF,,EDF,HRF,SPTF,CEF");
				for (int i = 0; i < Sum.length; i++) {

					pw.println(i * slice + "," + (Sum[i] / loop) + "," + (Drop[i] / loop) + "," + Value[i] / loop + "," + PTime[i] / loop + ","
							+ Duration[i] / loop + "," + Rate[i] / loop +"," + calc[i][0] / (loop*1000000000) + "," + calc[i][1]/(loop*1000000000)+ "," + i * slice + "," + Q[i][0][0] / (Main.AGENT *loop)
							+ "," + Q[i][1][0] / (Main.AGENT*loop) + "," + Q[i][2][0] / (Main.AGENT*loop)+ "," + Q[i][3][0] / (Main.AGENT*loop)+ ","+ i * slice + "," + Q[i][0][1] / (Main.AGENT *loop)
							+ "," + Q[i][1][1] / (Main.AGENT*loop) + "," + Q[i][2][1] / (Main.AGENT*loop)+ "," + Q[i][3][1] / (Main.AGENT*loop));
				}
			}
			System.out.println("終了");
			pw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void taskLoad(double[] sum,double[] value, double[] drop, double[] pTime, double[] duration, double[] rate, double[][][] q,double[][] calc,
			int bias, String str, String age, String val, String rew, String met) {
		try {
			FileWriter fw = new FileWriter("/Users/y.ishihara/Documents/タスク割り当て/Output/" + Task.slice + str
					+","+ age +","+ val+"," + rew+"," +met+ ".csv", true); // ※１
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			if (Main.strategy < 4) {
				pw.println(bias);
				pw.println(",reward,value,drop,pTime,duration,workRate,calcAllot,calcAll,value");
				for (int i = 0; i < sum.length; i++) {
					pw.println(((i * 2) + Main.taskLoad) + "," + sum[i] / loop + "," + value[i] / loop + "," + drop[i] / loop + ","
							+ pTime[i] / loop + "," + duration[i] / loop + "," + rate[i] / loop +"," + calc[i][0] / (loop*1000000000) + "," + calc[i][1]/(loop*1000000000));
				}
			} else {
				pw.println(bias);
				pw.println(",reward,value,drop,pTime,duration,workRate,calcAllot,calcAll,,EDF,HRF,SPTF,CEF,,EDF,HRF,SPTF,CEF");
				for (int i = 0; i < sum.length; i++) {
					pw.println(((i * 2) + Main.taskLoad) + "," + sum[i] / loop + "," + value[i] / loop + "," + drop[i] / loop + ","
							+ pTime[i] / loop + "," + duration[i] / loop + "," + rate[i] / loop+"," + calc[i][0] / (loop*1000000000) + "," + calc[i][1]/(loop*1000000000) + ","
							+ ((i * 2) + Main.taskLoad) + "," + q[i][0][0] / Main.AGENT + "," + q[i][1][0] / Main.AGENT + ","
							+ q[i][2][0] / Main.AGENT + "," + q[i][3][0] / Main.AGENT + ","+ ((i * 2) + Main.taskLoad) + "," + q[i][0][1] / Main.AGENT + "," + q[i][1][1] / Main.AGENT + ","
									+ q[i][2][1] / Main.AGENT + "," + q[i][3][1] / Main.AGENT + ",");
				}
			}
			pw.println();
			System.out.println("終了");
			pw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public void Qvalue(double[][][] q,ArrayList<Integer> agent,ArrayList<Agent> AGENT,
			int bias, String str, String age, String val, String rew, String met) {
		try {
			FileWriter fw = new FileWriter("/Users/y.ishihara/Documents/タスク割り当て/Output/" + str
					+","+ age +","+ val+"," + rew+"," +met+ ".csv", true); // ※１
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			double[][][] Q = new double[q.length][q[0].length/slice][4];
			for(int person = 0; person < q.length; person++)
			for (int i = 0; i < Q[person].length; i++) {
				for (int j = i * slice; j < i * slice + slice; j++) {
					Q[person][i][0] += q[person][j][0] / slice;
					Q[person][i][1] += q[person][j][1] / slice;
					Q[person][i][2] += q[person][j][2] / slice;
					Q[person][i][3] += q[person][j][3] / slice;
				}
			}
			pw.println(bias);
				for(int person = 0; person < q.length; person++) {
					pw.println(AGENT.get(person).toString());
					pw.print(",");
					for(int i = 0; i < Q[person].length; i++) {
						pw.print(i*slice + ",");
					}
					pw.println();
					for(int j = 0; j < 4; j++) {
						switch(j) {
						case EDF:
							pw.print("EDF" + ",");
							break;
						case HRF:
							pw.print("HRF" + ",");
							break;
						case SPTF:
							pw.print("SPTF" + ",");
							break;
						case CEF:
							pw.print("CEF" + ",");
							break;
						}
						for (int i = 0; i < Q[person].length; i++) {
					pw.print(Q[person][i][j]+",");
					}
						pw.println();
				}
				pw.println();
			}
			System.out.println("終了");
			pw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
