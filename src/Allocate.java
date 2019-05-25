import java.util.ArrayList;
import java.util.Random;

//割り当てアルゴリズム
//ちょいちょい何だろうか感
public class Allocate {
	public ArrayList<Bid> maxValue(ArrayList<ArrayList<Bid>> bid, Random random) {
		ArrayList<Bid> cand = new ArrayList<Bid>();
		int border;
		int number;
		for (ArrayList<Bid> eachTask : bid) {
			if (eachTask.get(0).task.allocated())
				continue;
			border = -100;
			number = -1;
			for (int i = 0; i < eachTask.size(); i++) {
				if (eachTask.get(i).allocated())
					continue;
				if (eachTask.get(i).value() < border)
					continue;
				else if (eachTask.get(i).value() > border) {
					number = i;
					border = eachTask.get(i).value();
				} else {
					if (eachTask.get(i).tempPreference() < eachTask.get(number).tempPreference()) {
						number = i;
					} else if (eachTask.get(i).tempPreference() == eachTask.get(number).tempPreference())
						if (random.nextDouble() < 0.5)
							continue;
						else {
							number = i;
						}
					else {
						continue;
					}
				}
			}
			if (number != -1)
				cand.add(eachTask.get(number));
		}
		return cand;
	}

	public ArrayList<Bid> maxPreference(ArrayList<Bid> bid) {
		ArrayList<Bid> cand = new ArrayList<Bid>();
		int count = 1;
		while (cand.isEmpty()) {
			for (Bid b : bid) {
				if (b.tempPreference() == count)
					cand.add(b);
			}
			count++;
		}
		return cand;
	}

	public ArrayList<Bid> maxValueAgain(ArrayList<Bid> bid) {
		ArrayList<Bid> cand = new ArrayList<Bid>();
		cand.add(bid.get(0));
		int border = bid.get(0).value();
		for (int i = 1; i < bid.size(); i++) {
			if (bid.get(i).value() < border)
				continue;
			else if (bid.get(i).value() > border) {
				cand.clear();
				cand.add(bid.get(i));
				border = bid.get(i).value();
			} else {
				cand.add(bid.get(i));
			}
		}
		return cand;
	}

	public Bid SRNF(ArrayList<ArrayList<Bid>> bid, ArrayList<Bid> cand, Random random) {
		int t;
		int number = 0;
		for (t = 0; t < bid.size(); t++) {
			if (bid.get(t).get(0).taskNumber() == cand.get(0).taskNumber()) {
				number = bid.get(t).size();
				break;
			}
		}
		while (cand.size() > 1) {
			for (int i = t; i < bid.size(); i++) {
				if (bid.get(i).get(0).taskNumber() == cand.get(1).taskNumber()) {
					if (number > bid.get(i).size()) {
						cand.remove(1);
						t = i;
						break;
					} else if (number < bid.get(i).size()) {
						cand.remove(0);
						t = i;
						break;
					} else {
						if (random.nextDouble() > 0.5) {
							cand.remove(0);
						} else {
							cand.remove(1);
						}
						t = i;
						break;
					}
				}
			}
		}
		return cand.get(0);
	}

}
