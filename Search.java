import java.util.*;

public class Search {
	//Global variables for a static class is not a great Idea but convenient for me
	private static Task[] taskArr; //Master Task Lookup Table
	private static int targetVal; 
	private static int deadline;
	private static int queueSize;

	//the driver for the search
	public static TaskList Driver(Task[] tasks, int target, int dl, int queueSz) {
		taskArr = tasks;
		targetVal = target;
		deadline = dl;
		queueSize = queueSz;
		TaskList res = BFS(); //Start search
		if(res.isAnswer){
			System.out.println(res.toString() + " Time: " + res.time + " Value: " + res.value + " States: " + res.numStates);
		}else
			System.out.println("No Answer");
		return res;
	}

	//Does BFS until it reaches queueSize, then calls iterativeDeepening
	private static TaskList BFS() { //Stage I: BFS
		Queue<TaskList> currentLayer = new LinkedList<TaskList>(); //queue of arrayLists
		TaskList emptyState = new TaskList();
		emptyState.numStates = 1;
		currentLayer.add(emptyState); //Empty Starting State
		int level = 0; //Level zero is the empty initial state
		int numStates = 1; //empty state
		while(currentLayer.size() != 0 ) { //Loop Through Layers
			if( currentLayer.size() < queueSize ) { //queueSize is not reached
				Queue<TaskList> frontierLayer = new LinkedList<TaskList>();
				Hashtable<String, Boolean> frontier = new Hashtable<String, Boolean>();
				while(!currentLayer.isEmpty()) { //Loop through states in level                   
					TaskList currentPath = currentLayer.poll(); //Pop
					if(currentPath.value >= targetVal && currentPath.time <= deadline) { //If conditions are met
						currentPath.isAnswer = true;
						return currentPath;
					} else if (currentPath.time <= deadline) { //current Path is still valid
						for(int i = 0; i < taskArr.length; i++) { //Explore frontier
							if(taskArr[i].isValid(currentPath)) { //Add children to Stack
								TaskList newPath = new TaskList(currentPath); //New Path with valid Task
								newPath.addTask(taskArr[i]);
								if(frontier.get(newPath.hash()) == null) { //delete repeats/visited and generates new state
									numStates++;
									newPath.numStates = numStates;
									frontierLayer.add(newPath);
									frontier.put(newPath.hash(), true); //add Path to HashTable
								}
							} 
						} 
					}
				}
				currentLayer = frontierLayer; //update queue
			} else {//Stage II Iterative Deepening
				System.out.println("\nIterative Deepening\n");
				return IterativeDeepening(level, numStates);
			}
			level++; //Increase Level
		}
		//Returns empty Path with Total Number of States 
		TaskList noAnswer = new TaskList();
		noAnswer.numStates = numStates;
		return noAnswer;
	}

	//Depth First Search Method
	private static TaskList DFS(TaskList currentPath, int level_limit, int level) {
		currentPath.numStates++; //update the number of States
		if(currentPath.value >= targetVal && currentPath.time <= deadline) { //If conditions are met
			currentPath.isAnswer = true;
			return currentPath;
		}
		System.out.println("Level: " + level + " Current Path: " + currentPath.toString() + " States: " + currentPath.numStates);
		if(level == level_limit) {		//Level Cap Reached
			TaskList nullPath = new TaskList();
			nullPath.numStates = currentPath.numStates;
			return nullPath; //Limit Level
		}
		TaskList answer = new TaskList();//Update new Path
		answer.numStates = currentPath.numStates;
		for(int i = 0; i < taskArr.length; i++) { //Explore frontier
			if(taskArr[i].isValid(currentPath)) { //Add children to Stack
				//New Path with valid Task
				TaskList newPath = new TaskList(currentPath);
				newPath.addTask(taskArr[i]);//Add the number of States that failed
				newPath.numStates = answer.numStates;
				answer = DFS(newPath, level_limit, level + 1);
				if(answer.isAnswer == true) break; //Answer is found 
			} 
		}
		return answer;
	}

	//Iterative Deepening Method, called by BFS when it reaches the max queueSize
	private static TaskList IterativeDeepening(int level, int numOrigStates) {
		TaskList answer = null;
		int numTotalStates = numOrigStates;
		for(int level_cap = level; level_cap <= taskArr.length; level_cap++) { //Capped by number of tasks
			System.out.println("This level cap: " + level_cap); 			//Returns either answer or empty Path with Total Number of States Run through
			answer = DFS(new TaskList(), level_cap, 0); //Aggregate Total Number of States per Level
			numTotalStates += answer.numStates;
			if(answer.isAnswer == true) //Answer is found: Start from Root
				break;
		}
		answer.numStates = numTotalStates;
		return answer;
	}


}