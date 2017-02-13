import java.io.*;
import java.util.*;

public class Run {
    
	public static void main(String[] args) throws Exception {
        String inputFile = "experiments.txt";
        String outputFile = "results.txt";
        int numRuns = 1000;
        int numTasks = 50;
        int queueSize = 50;
        //Parse command line arguments
        if(args.length == 3) { 
            numRuns = Integer.parseInt(args[0]);
            numTasks = Integer.parseInt(args[1]);
            queueSize = Integer.parseInt(args[2]);
        } else if (args.length > 0) {
            System.out.println("Please input exactly 3 value: Number of Experiments, Total Number of Tasks, and Max Queue. ex: Assignment1_Main.FileDriver 1000 100 50");
        }
        //Choose which method to run
        fullRun(inputFile, numRuns, numTasks, queueSize, outputFile);
        //singleRun(inputFile, numRuns, numTasks, queueSize, outputFile);
        //manualRun();
    }
	
    //Creates random experiments for fullRun and singleRun and writes them to a file. Those methods then read from that file
    public static void generateInputs(String outputFile, int numTasks, int numExperiments, int queueSize) throws IOException {
        //File Write
        BufferedWriter wr = new BufferedWriter(new FileWriter(outputFile));
        Random random = new Random(); //RNG for random number distribution
        int min = (int) (Math.pow(numTasks, 2) * (1 - 2 / Math.sqrt(numTasks)) / 4);
        int max = (int) (Math.pow(numTasks, 2) * (1 + 2 / Math.sqrt(numTasks)) / 4);
        for(int experiment = 1; experiment <= numExperiments; experiment++ ) {
            StringBuilder example = new StringBuilder(numTasks + " ");
            example.append((random.nextInt(max - min + 1) + min) + " "); //Target Value
            example.append((random.nextInt(max - min + 1) + min) + " "); //Deadline
            example.append(queueSize); //queueSize
            example.append("\n");
            for(int id = 0; id < numTasks; id++) { //Add Tasks
                example.append(id + " ");
                example.append((random.nextInt(numTasks + 1)) + " "); //Value
                example.append((random.nextInt(numTasks + 1)) + " \n"); //Time
            }
            //random DAG
            int[] perm = new int[numTasks];
            Arrays.fill(perm, -1);
            for(int i = 0; i < numTasks; i++) {
                if(perm[i] == -1) 
                    perm[i] = i;   
                //random Number between Total Number of Tasks and i
                int j = random.nextInt(numTasks - i) + i;
                if(perm[j] == -1) {
                    perm[j] = j;
                } 
                //Swap
                int temp = perm[j];
                perm[j] = perm[i];
                perm[i] = temp;
            }
            //build Prerequisites
            for(int i = 0; i < numTasks - 1; i++) {
                for(int j = i + 1; j < numTasks; j++) {
                    int coin = random.nextInt(2);//50% create and arc
                    if(coin == 1) 
                        example.append(perm[i] + " " + perm[j] + "\n"); //queueSize
                }
            }
            example.append("\n");
            wr.write(example.toString());
        }
        wr.close();
    }
        
  //Takes 3 command line arguments, generates random input, runs E experiments on every size of example from 10 to N in increments of 10
    public static void fullRun(String inputFile, int numExperiments, int maxTasks, int queueSize, String outputFile) throws IOException {
    	BufferedReader br=null;//results writer
        BufferedWriter wr = new BufferedWriter(new FileWriter(outputFile));
        //Run for number of tasks from 10 to maxTasks in increments of 10
        for(int i = 10;i <= maxTasks; i+=10) {
            //Create Experiments
            generateInputs(inputFile, i, numExperiments, queueSize);
            //Set new Reader
             br = new BufferedReader(new FileReader(inputFile));  
            int numSolutions = 0;
            int largestStates = -1;
            int smallestStates = -1;
            double totalStates = 0.0;
            //Read every Experiment
            for(int experiment = 1; experiment <= numExperiments; experiment++ ) {
                String[] specs = br.readLine().split("\\ ");
                int targetVal = Integer.parseInt(specs[1]);
                int deadline = Integer.parseInt(specs[2]);
                Task[] taskArr = new Task[i];
                System.out.println("Experiment "+ experiment);
                for(int j = 0; j < i; j++) { //Add Tasks
                    String[] task = br.readLine().split("\\ ");
                    int ID = Integer.parseInt(task[0]);
                    int val = Integer.parseInt(task[1]);
                    int time = Integer.parseInt(task[2]);
                    taskArr[ID] = new Task(ID, val, time); //id is incremental
                }
                String line;
                while((line = br.readLine()) != null) {
                    if(line.equals("")) break;
                    String[] DAGrel = line.split("\\ ");
                    int prereq = Integer.parseInt(DAGrel[0]);
                    int task = Integer.parseInt(DAGrel[1]);
                    taskArr[task].prerequsites.add(prereq);
                }
                //Search for solution
                TaskList answer = Search.Driver(taskArr, targetVal, deadline, queueSize);
                if(answer.isAnswer != false) numSolutions++;
                //total States found
                totalStates += answer.numStates;
                //Largest State
                if(largestStates < 0)
                    largestStates = answer.numStates;
                else if(largestStates < answer.numStates)
                    largestStates = answer.numStates;
                //Smallest State
                if(smallestStates < 0)
                    smallestStates = answer.numStates;
                else if(smallestStates > answer.numStates)
                    smallestStates = answer.numStates;
            }
            //output to file
          wr.write("Total Number of Tasks: " + i + "\nNumber of Experiments: " + numExperiments + "\nAnswers | Experiments: " + numSolutions + " | " + numExperiments + "\nLargest Number of States Generated: " + largestStates + "\nSmallest Number of States Generated: " + smallestStates + "\nAverage Number of States Generated: " + (totalStates /numExperiments) + "\n\n");
          wr.flush();
        }
        wr.close();
        br.close();
    }
  //Takes 3 command line arguments, generates random input, runs E experiments on an example of size N
  public static void singleRun(String inputFile, int numExperiments, int maxTasks, int queueSize, String outputFile) throws IOException {
      BufferedReader br = null;
      BufferedWriter wr = new BufferedWriter(new FileWriter(outputFile));//results writer
      int numTasks = maxTasks;
      generateInputs(inputFile, numTasks, numExperiments, queueSize); //Create Experiments
      br = new BufferedReader(new FileReader(inputFile));  
      int numSolutions = 0;
      int largestStates = -1;
      int smallestStates = -1;
      double totalStates = 0.0;
      for(int experiment = 1; experiment <= numExperiments; experiment++ ) {      //Read Examples
          String[] specs = br.readLine().split("\\ ");
          int targetVal = Integer.parseInt(specs[1]);
          int deadline = Integer.parseInt(specs[2]);
          Task[] taskArr = new Task[numTasks];
          for(int i = 0; i < numTasks; i++) { //Add Tasks
              String[] task = br.readLine().split("\\ ");
              int ID = Integer.parseInt(task[0]);
              int val = Integer.parseInt(task[1]);
              int time = Integer.parseInt(task[2]);
              taskArr[ID] = new Task(ID, val, time); //id is incremental
          }            
          String line;
          while((line = br.readLine()) != null) {
              if(line.equals("")) break;
              String[] DAGrel = line.split("\\ ");
              int prereq = Integer.parseInt(DAGrel[0]);
              int task = Integer.parseInt(DAGrel[1]);
              taskArr[task].prerequsites.add(prereq);
          }
          TaskList answer = Search.Driver(taskArr, targetVal, deadline, queueSize);          //Run Driver
          if(answer.isAnswer != false) numSolutions++;
          totalStates += answer.numStates;
          if(largestStates < 0)          //Cheks for Largest State
              largestStates = answer.numStates;
          else if(largestStates < answer.numStates)
              largestStates = answer.numStates;
          if(smallestStates < 0)//Cheks forSmallest State 
              smallestStates = answer.numStates;
          else if(smallestStates > answer.numStates)
              smallestStates = answer.numStates;
      }
      br.close();
      //output to file
      wr.write("Total Number of Tasks: " + numTasks + "\nNumber of Experiments: " + numExperiments + "\nAnswers | Experiments: " + numSolutions + " | " + numExperiments + "\nLargest Number of States Generated: " + largestStates + "\nSmallest Number of States Generated: " + smallestStates + "\nAverage Number of States Generated: " + (totalStates /numExperiments) + "\n\n");
      wr.flush();
      wr.close();
  }
  
  //Manual Search by user inputting DAG
  public static void manualRun() {
      Scanner in = new Scanner(System.in);
      int numTasks = in.nextInt();
      int targetVal = in.nextInt();
      int deadline = in.nextInt();
      int queueSize = in.nextInt();
      Task[] taskArr = new Task[numTasks];
      for(int i = 0; i < numTasks; i++) { //Add Tasks
          int ID = in.nextInt();
          int val = in.nextInt();
          int time = in.nextInt();
          taskArr[ID] = new Task(ID, val, time); //id is incremental
      }
      while(in.hasNext()) { //Add Pre-Reqs
          int prereq = in.nextInt();
          int task = in.nextInt();
          taskArr[task].prerequsites.add(prereq);
      }
      in.close();
      Search.Driver(taskArr, targetVal, deadline, queueSize);
  }

}
