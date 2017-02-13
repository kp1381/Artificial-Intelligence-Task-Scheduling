import java.util.ArrayList;
import java.util.Collections;

public class TaskList {
    public ArrayList<Integer> pathList;
    public int value = 0;
    public int time = 0;
    public int numStates = 0;
    public boolean isAnswer = false;
    
    public TaskList() {
        pathList = new ArrayList<Integer>();
    }
    
    public TaskList(TaskList oldPath) {
        pathList = new ArrayList<Integer>();
        pathList.addAll(oldPath.pathList);
        value = oldPath.value;
        time = oldPath.time;
    }
    
    public TaskList addTask(Task t) {
        pathList.add(t.ID);
        value += t.V;
        time += t.L;
        return this;
    }

    //To String function for debugging
    public String toString() {
        String res = "[ ";
        for(int id: pathList) {
            res += id + ", ";
        }
        return res + " ]";
    }
    
    //Hash Function
    //Hash is a sorted String of the TaskList Members
    public String hash() { 
        String pathstring = "";
        Collections.sort(pathList);
        for(int id: pathList) {
            pathstring += id + " ";
        }
        return pathstring + "";
    }
}
