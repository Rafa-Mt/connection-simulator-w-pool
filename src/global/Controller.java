package global;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Controller  {
    private int iterations;
    private int jump;
    private int totalIterations;
    private int totalThreads;
    private ArrayList<ConnectionThread> threadList;
    
    public Controller(int iterations, int jump) {
        super();
        this.iterations = iterations;
        this.jump = jump;
        this.threadList = new ArrayList<>();
    }

    public void start() {
        boolean killCycle = false;
        for (int cycle : range(1, this.iterations)) {
            this.threadList = new ArrayList<>();
            totalIterations++;
            int actualRange = this.jump * cycle;
            System.out.print(String.format("Iteration %d (%d threads): ", cycle, actualRange));

            for (int thread : range(1, actualRange)) {
                this.totalThreads = this.jump * cycle;
                ConnectionThread newThread = new ConnectionThread(thread);
                
                this.threadList.add(newThread);
                newThread.start();
                if (newThread.isInterrupted()) {
                    this.exit();
                    break;
                }
            }

            try { Thread.sleep(5000); } 
            catch (InterruptedException e) {  e.printStackTrace(); }

            for (ConnectionThread thread : this.threadList) {
                if (thread.isInterrupted() || thread.state == "Error") {
                    killCycle = true;
                    break;
                }
            }
            if (killCycle) {
                System.out.println("Error");
                break;
            }
            System.out.println("Complete");
        }
        this.exit();
    }

    public synchronized void exit() {
        int doneThreads = 0;
        int errorThreads = 0;
        System.out.println("Result:");
        for (ConnectionThread thread : this.threadList) {
            thread.interrupt();
            System.out.println(String.format(" | %3d | %8s | ", thread.id, thread.state));
            if (thread.state == "Done") doneThreads++;
            if (thread.state == "Error") errorThreads++;
        }
        System.out.println(
            String.format("\n * Total Iterations: %d \n * Total Threads: %d \n * Successful Threads: %d \n * Failed Threads: %d"
            , this.totalIterations, this.totalThreads, doneThreads, errorThreads)
            );
        System.exit(-1);
    }
    
    public static int[] range(int start, int end) {
        return IntStream.rangeClosed(start, end).toArray();
    }


}
