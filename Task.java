import java.util.ArrayList;

public class Task {
    public int ID;
    public int L; 
    public int V; 
    public ArrayList<Integer> prerequsites;
    
    public Task(int ID, int V, int L) { 
        this.ID = ID;
        this.L = L;
        this.V = V;
        prerequsites = new ArrayList<Integer>();
    }
    
    public boolean isValid(TaskList cur) {
        if(cur.pathList.contains(ID)) return false; //Cannot have used the same task
        for(int prereq: prerequsites) {
            if(!cur.pathList.contains(prereq)) //current path must contain all prerequisites
                return false;
        }
        return true;
    }
}
