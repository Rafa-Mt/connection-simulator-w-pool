import java.util.ArrayList;

public class ControllerThread extends Thread {
    private int iterations;
    private int jump;
    private int totalIterations;
    private int totalThreads;
    private ArrayList<ConnectionThread> threadList;
    private boolean ended;

    public ControllerThread(int iterations, int jump) {
        super();
        this.iterations = iterations;
        this.jump = jump;
        this.ended = false;
        threadList = new ArrayList<ConnectionThread>();
    }

    @Override
    public void run() {
        for (int cycle : Utils.Range(1, this.iterations)) {
            totalIterations++;
            System.out.print(String.format("Iteration %d (%d threads): ", cycle, this.jump * cycle));
            for (int thread : Utils.Range(1, this.jump * cycle)) {
                totalThreads = this.jump * cycle;
                ConnectionThread newThread = new ConnectionThread(thread, false);
                threadList.add(newThread);
                newThread.start();
            }
            System.err.println("Complete");
        }
        exit(0);
    }

    public synchronized void exit(int callerId) {
        if (!ended) {
            ended = true;
            for (ConnectionThread thread : threadList) {
                thread.interrupt();
                String caller = callerId == thread.id ? "<-" : "";
                System.out.println(String.format("| %3d | %8s | %2s ", thread.id, thread.state, caller));
            }
            System.out.println(String.format("Exit\n\tAt iteration %d\n\tAt thread %d", totalIterations, totalThreads));
            System.exit(-1);
        }
    }
}
