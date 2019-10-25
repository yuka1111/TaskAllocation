//Agentとtaskをdeleteする
import java.util.ArrayList;

public class Delete{
	public void DeleteAgent(ArrayList<ArrayList<Bid>> bid, ArrayList<ArrayList<Bid>> agentBid, ArrayList<ArrayList<Bid>> envyList, Bid item){
		int agentNumber = item.agentNumber();
		int taskNumber = item.taskNumber();
		int counter;
		int chacker;
		for(int i = 0; i < agentBid.size(); i++){
			if(agentNumber == agentBid.get(i).get(0).agentNumber()){
				for(Bid b : agentBid.get(i)){
					b.allocated(1);
				}
				int count = 0;
				ArrayList<Bid> envy = new ArrayList<Bid>();
				while(true){
					if(agentBid.get(i).get(count).preferentialNumber() < item.preferentialNumber()){
						envy.add(agentBid.get(i).get(count));
						count++;
						continue;
					}
					break;
				}
				if(envy.isEmpty() != true){
					envyList.add(envy);
				}
				agentBid.remove(i);
				break;
			}
		}
		for(ArrayList<Bid> b : bid){
			if(b.get(0).taskNumber() == taskNumber){
				for(Bid t : b){
					counter = 0;
					if(t.allocated())
						continue;
					for(int i = counter; i < agentBid.size(); i++){
						if(t.agentNumber() == agentBid.get(i).get(0).agentNumber()){
							for(int j = 0; j < agentBid.get(i).size(); j++){
								if(t.taskNumber() == agentBid.get(i).get(j).taskNumber()){
									chacker = 0;
									for(int k = j-1; k > 0; k--){
										if(agentBid.get(i).get(j).tempPreference() == agentBid.get(i).get(k).tempPreference() && agentBid.get(i).get(k).allocated()!=true){
											chacker = 1;
											break;
										}
									}
									for(int k = j+1; k < agentBid.get(i).size(); k++){
										if(agentBid.get(i).get(j).tempPreference() == agentBid.get(i).get(k).tempPreference() && agentBid.get(i).get(k).allocated()!=true){
											chacker = 1;
											break;
										}
									}
									if(chacker == 0){
										for(int k = j+1; k < agentBid.get(i).size(); k++){
											if(agentBid.get(i).get(k).allocated()!=true){
												agentBid.get(i).get(k).tempUp();
											}
										}
									}
								}
							}
							counter = i;
							t.allocated(1);
							break;
						}
					}
				}
				break;
			}
		}
	}
	public void DeleteTask(ArrayList<ArrayList<Bid>> bid, ArrayList<Task> task, Bid item){
		for(int i = 0; i < task.size(); i++){
			if(item.taskNumber() == task.get(i).taskNumber()){
				task.get(i).allocated(1);
				task.remove(i);
				break;
			}
		}
		for(int i = 0; i < bid.size(); i++){
			if(bid.get(i).get(0).taskNumber() == item.taskNumber()){
				bid.remove(i);
				break;
			}
		}
	}
}
