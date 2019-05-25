import java.util.ArrayList;
import java.util.Random;

public class MakeObject {

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
	final int GOODBAD = Main.GOODBAD;
	final int LENGTH = Main.LENGTH;

	final static int REWARD = Main.REWARD;
	final static int PROCESSTIME = Main.PROCESSTIME;

	final static int deadlineUnder = Main.deadlineUnder;
	final static int deadlineRange = Main.deadlineRange;
	final static int bidNumber = Main.bidNumber;
	static int Value = Main.Value;

	public void makeBid(Agent[] agent, ArrayList<Task> task, ArrayList<ArrayList<Bid>> bid,
			ArrayList<ArrayList<Bid>> agentBid, Random random) {
		for (int i = 0; i < agent.length; i++) {
			if (agent[i].status > 0)
				continue;
			int value[] = new int[bidNumber];
			int count = 0;
			ArrayList<Bid> forA = new ArrayList<Bid>();
			switch (agent[i].agentStrategy()) {
			case EDF:
				for (int v = 0; v < value.length; v++) {
					value[v] = 100;
				}
				for (Task t : task) {
					int maxmin[] = calculate(agent[i].resource(), t.resource());
					int reward;
					if(t.flag == 1) {
						reward = (int) (t.reward()
						* (t.deadline() - maxmin[0])
						/ (t.original_deadline));
					}
					else
						reward = t.reward;
					if (maxmin[0] > t.deadline())
						continue;
					if (t.deadline() < value[0]) {
						for (int j = 1; j < value.length; j++) {
							if (t.deadline() > value[j]) {
								for (int k = 0; k < j - 1; k++) {
									value[k] = value[k + 1];
								}
								value[j - 1] = t.deadline();
								forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
								break;
							}
							if (t.deadline() == value[j]) {
								if (Value == REWARD) {

									if (reward > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = t.deadline();
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (reward == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
				 								for (int k = 0; k < 4; k++) {
													value[k] = value[k + 1];
												}
												value[j] = t.deadline();
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = t.deadline();
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = t.deadline();
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								if (Value == PROCESSTIME) {
									if (-maxmin[0] > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = t.deadline();
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (-maxmin[0] == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
												for (int k = 0; k < 4; k++) {
													value[k] = value[k + 1];
												}
												value[j] = t.deadline();
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = t.deadline();
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = t.deadline();
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								break;
							}
							if (j == value.length - 1) {
								for (int k = 0; k < 4; k++) {
									value[k] = value[k + 1];
								}
								value[j] = t.deadline();
								forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
							}
						}
					}
				}
				for (int l = 1; l < forA.size(); l++) {
					if (forA.get(l).deadline() == forA.get(l - 1).reward()) {
						if (forA.get(l).value() == forA.get(l - 1).value())
							forA.get(l).prefPlus(count);
						else {
							count++;
							forA.get(l).prefPlus(count);
						}
					} else {
						count++;
						forA.get(l).prefPlus(count);
					}
				}
				break;
			case HRF:
				for (int v = 0; v < value.length; v++) {
					value[v] = 0;
				}
				for (Task t : task) {
					int maxmin[] = calculate(agent[i].resource(), t.resource());
					int reward;
					if(t.flag == 1) {
						reward = (int) (t.reward() / 2 + t.reward()
						* (t.deadline() - maxmin[0])
						/ (2 * t.original_deadline));
					}
					else
						reward = t.reward;
					if (maxmin[0] > t.deadline())
						continue;
					if (reward > value[0]) {

						for (int j = 1; j < value.length; j++) {
							if (reward < value[j]) {
								for (int k = 0; k < j - 1; k++) {
									value[k] = value[k + 1];
								}
								value[j - 1] = reward;

								forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
								break;
							}
							if (reward == value[j]) {
								if (Value == REWARD) {

									if (reward > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = reward;
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (reward == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
												for (int k = 0; k < bidNumber - 1; k++) {
													value[k] = value[k + 1];
												}
												value[j] = reward;
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = reward;
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = reward;
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								if (Value == PROCESSTIME) {
									if (-maxmin[0] > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = reward;
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (-maxmin[0] == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
												for (int k = 0; k < bidNumber - 1; k++) {
													value[k] = value[k + 1];
												}
												value[j] = reward;
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = reward;
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = reward;
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								break;
							}
							if (j == value.length - 1) {
								for (int k = 0; k < bidNumber - 1; k++) {
									value[k] = value[k + 1];
								}
								value[j] = reward;
								forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
							}
						}
					}
				}
				for (int l = 1; l < forA.size(); l++) {
					if (forA.get(l).reward() == forA.get(l - 1).reward()) {
						if (forA.get(l).value() == forA.get(l - 1).value()) {
							forA.get(l).prefPlus(count);
						} else {
							count++;
							forA.get(l).prefPlus(count);
						}
					} else {
						count++;
						forA.get(l).prefPlus(count);
					}
				}
				break;
			case SPTF:
				for (int v = 0; v < value.length; v++) {
					value[v] = -100;
				}
				for (Task t : task) {
					int maxmin[] = calculate(agent[i].resource(), t.resource());
					int reward;
					if(t.flag == 1) {
						reward = (int) (t.reward() / 2 + t.reward()
						* (t.deadline() - maxmin[0])
						/ (2 * t.original_deadline));
					}
					else
						reward = t.reward;
					if (maxmin[0] > t.deadline())
						continue;
					if (-maxmin[0] > value[0]) {
						for (int j = 1; j < value.length; j++) {
							if (-maxmin[0] < value[j]) {
								for (int k = 0; k < j - 1; k++) {
									value[k] = value[k + 1];
								}
								value[j - 1] = -maxmin[0];
								forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
								break;
							}
							if (-maxmin[0] == value[j]) {
								if (Value == REWARD) {

									if (reward > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = -maxmin[0];
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (reward == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
												for (int k = 0; k < 4; k++) {
													value[k] = value[k + 1];
												}
												value[j] = -maxmin[0];
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = -maxmin[0];
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = -maxmin[0];
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								if (Value == PROCESSTIME) {
									if (-maxmin[0] > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = -maxmin[0];
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (-maxmin[0] == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
												for (int k = 0; k < 4; k++) {
													value[k] = value[k + 1];
												}
												value[j] = -maxmin[0];
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = -maxmin[0];
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = -maxmin[0];
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								break;
							}
							if (j == value.length - 1) {
								for (int k = 0; k < 4; k++) {
									value[k] = value[k + 1];
								}
								value[j] = -maxmin[0];
								forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
							}
						}
					}
				}
				for (int l = 1; l < forA.size(); l++) {
					if (forA.get(l).processTime() == forA.get(l - 1).processTime()) {
						if (forA.get(l).value() == forA.get(l - 1).value())
							forA.get(l).prefPlus(count);
						else {
							count++;
							forA.get(l).prefPlus(count);
						}
					} else {
						count++;
						forA.get(l).prefPlus(count);
					}
				}
				break;
			case CEF:
				for (int v = 0; v < value.length; v++) {
					value[v] = 0;
				}
				for (Task t : task) {
					int maxmin[] = calculate(agent[i].resource(), t.resource());
					int reward;
					if(t.flag == 1) {
						reward = (int) (t.reward() / 2 + t.reward()
						* (t.deadline() - maxmin[0])
						/ (2 * t.original_deadline));
					}
					else
						reward = t.reward;
					if (maxmin[0] > t.deadline())
						continue;
					if (reward / maxmin[0] > value[0]) {
						for (int j = 1; j < value.length; j++) {
							if (reward / maxmin[0] < value[j]) {
								for (int k = 0; k < j - 1; k++) {
									value[k] = value[k + 1];
								}
								value[j - 1] = reward / maxmin[0];
								forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
								break;
							}
							if (reward / maxmin[0] == value[j]) {
								if (Value == REWARD) {

									if (reward > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = reward / maxmin[0];
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (reward == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
												for (int k = 0; k < 4; k++) {
													value[k] = value[k + 1];
												}
												value[j] = reward / maxmin[0];
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = reward / maxmin[0];
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = reward / maxmin[0];
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								if (Value == PROCESSTIME) {
									if (-maxmin[0] > forA.get(bidNumber - j - 1).value()) {
										if (j != value.length - 1)
											continue;
										else {
											for (int k = 0; k < 4; k++) {
												value[k] = value[k + 1];
											}
											value[j] = reward / maxmin[0];
											forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
										}
									} else if (-maxmin[0] == forA.get(bidNumber - j - 1).value()) {
										if (random.nextDouble() < 0.5) {
											if (j == value.length - 1) {
												for (int k = 0; k < 4; k++) {
													value[k] = value[k + 1];
												}
												value[j] = reward / maxmin[0];
												forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

												if (forA.size() > bidNumber)
													forA.remove(forA.size() - 1);
											}
											continue;
										} else {
											for (int k = 0; k < j - 1; k++) {
												value[k] = value[k + 1];
											}
											value[j - 1] = reward / maxmin[0];
											forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
											if (forA.size() > bidNumber)
												forA.remove(forA.size() - 1);
											break;
										}
									} else {
										for (int k = 0; k < j - 1; k++) {
											value[k] = value[k + 1];
										}
										value[j - 1] = reward / maxmin[0];
										forA.add(bidNumber - j, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));
										if (forA.size() > bidNumber)
											forA.remove(forA.size() - 1);
										break;
									}
								}
								break;
							}
							if (j == value.length - 1) {
								for (int k = 0; k < 4; k++) {
									value[k] = value[k + 1];
								}
								value[j] = reward / maxmin[0];
								forA.add(0, new Bid(t, agent[i], reward, 1, 1, maxmin[0]));

								if (forA.size() > bidNumber)
									forA.remove(forA.size() - 1);
							}
						}
					}
				}
				for (int l = 1; l < forA.size(); l++) {
					if (forA.get(l).reward() / forA.get(l).processTime() == forA.get(l - 1).reward()
							/ forA.get(l).processTime()) {
						if (forA.get(l).value() == forA.get(l - 1).value())
							forA.get(l).prefPlus(count);
						else {
							count++;
							forA.get(l).prefPlus(count);
						}
					} else {
						count++;
						forA.get(l).prefPlus(count);
					}
				}
				break;
			}
			if (forA.isEmpty() != true) {
				agentBid.add(forA);
				for (int j = 0; j < forA.size(); j++) {
					bid.get(forA.get(j).taskNumber()).add(forA.get(j));
				}
			}
		}
	}

	public void makeTask(ArrayList<Task> task, int generated, Random random, double[] prob) {
		Double checker;
		for (int i = 0; i < task.size(); i++) {
			task.get(i).resetTask(i);
		}
		for (int i = 0; i < generated; i++) {
			checker = random.nextDouble();
			if (checker < prob[0])
				task.add(new Task(task.size(), normalTask(LENGTH, random),
						deadlineUnder + random.nextInt(deadlineRange + 1), random));
			else {
				if (checker < prob[0] + prob[1])
					task.add(new Task(task.size(), biasTask(LENGTH, random, 0),
							deadlineUnder + random.nextInt(deadlineRange + 1), random));
				else if (checker < prob[0] + prob[1] + prob[2])
					task.add(new Task(task.size(), biasTask(LENGTH, random, 1),
							deadlineUnder + random.nextInt(deadlineRange + 1), random));
				else
					task.add(new Task(task.size(), biasTask(LENGTH, random, 2),
							deadlineUnder + random.nextInt(deadlineRange + 1), random));
			}

		}
	}

	public void makeAgent(Agent[] agent, int n, Random random) {
		switch (n) {
		case RANDOM:
			for (int i = 0; i < agent.length; i++) {
				agent[i] = new Agent(i, randomAgent(LENGTH, random), random);
			}
			break;
		case BIAS:
			for (int i = 0; i < agent.length / 4; i++) {
				agent[i] = new Agent(i, highAgent(LENGTH, random), random);
			}
			for (int i = agent.length / 4; i < agent.length; i++) {
				agent[i] = new Agent(i, biasAgent(LENGTH, random, random.nextInt(LENGTH)), random);
			}
			break;
		case MIXED:
			for (int i = 0; i < agent.length / 4; i++) {
				agent[i] = new Agent(i, highAgent(LENGTH, random), random);
			}
			for (int i = agent.length / 4; i < agent.length / 2; i++) {
				agent[i] = new Agent(i, lowAgent(LENGTH, random), random);
			}
			for (int i = agent.length / 2; i < agent.length; i++) {
				agent[i] = new Agent(i, biasAgent(LENGTH, random, random.nextInt(LENGTH)), random);
			}
			break;
		case GOODBAD:
			for (int i = 0; i < agent.length / 2; i++) {
				agent[i] = new Agent(i, lowAgent(LENGTH, random), random);
			}
			for (int i = agent.length / 2; i < agent.length; i++) {
				agent[i] = new Agent(i, highAgent(LENGTH, random), random);
			}
			break;
		default:
			System.out.println("something is wrong.");
			System.exit(1);
		}
	}

	public int[] randomAgent(int length, Random random) {
		int[] resource = new int[length];
		for (int i = 0; i < length; i++) {
			resource[i] = 2 + random.nextInt(6);
		}
		return resource;
	}

	public int[] highAgent(int length, Random random) {
		int[] resource = new int[length];
		for (int i = 0; i < length; i++) {
			resource[i] = 5 + random.nextInt(3);
		}
		return resource;
	}

	public int[] lowAgent(int length, Random random) {
		int[] resource = new int[length];
		for (int i = 0; i < length; i++) {
			resource[i] = 2 + random.nextInt(3);
		}
		return resource;
	}

	public int[] biasAgent(int length, Random random, int number) {
		int[] resource = new int[length];
		for (int i = 0; i < length; i++) {
			if (i == number)
				resource[i] = 7 + random.nextInt(3);
			else
				resource[i] = 2 + random.nextInt(3);
		}
		return resource;
	}

	public int[] normalTask(int length, Random random) {
		int[] resource = new int[length];
		for (int i = 0; i < length; i++) {
			resource[i] = 30 + random.nextInt(31);
		}
		return resource;
	}

	public int[] biasTask(int length, Random random, int number) {
		int[] resource = new int[length];
		for (int i = 0; i < length; i++) {
			if (i == number)
				resource[i] = 70 + random.nextInt(31);
			else
				resource[i] = 20 + random.nextInt(21);
		}
		return resource;
	}

	public int[] calculate(int[] agent, int[] task) {
		int[] maxmin = { (int) Math.ceil(task[0] / agent[0]), (int) Math.ceil(task[0] / agent[0]) };
		for (int i = 1; i < LENGTH; i++) {
			if (maxmin[0] < (int) Math.ceil((double)task[i] / agent[i]))
				maxmin[0] = (int) Math.ceil((double)task[i] / agent[i]);
			if (maxmin[1] > (int) Math.ceil((double)task[i] / agent[i]))
				maxmin[1] = (int) Math.ceil((double)task[i] / agent[i]);
		}
		return maxmin;
	}
}
