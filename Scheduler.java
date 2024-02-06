import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    private LinkedList<UserlandProcess> processes;
    UserlandProcess currentlyRunning;
    private Timer timer;

    public Scheduler() {
        this.processes = new LinkedList<>();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentlyRunning!= null) 
                {
                	currentlyRunning.requestStop();
                }
            }
        }, 0, 250);
    }

    public int CreateProcess(UserlandProcess up) {
        processes.add(up);
        if (getCurrentlyRunning() == null) {
            SwitchProcess();
        }
        return processes.size() - 1;
    }

    public void SwitchProcess() {
        if (getCurrentlyRunning() != null && !getCurrentlyRunning().isDone()) {
            processes.add(getCurrentlyRunning());
        }
        setCurrentlyRunning(processes.poll());   
       
    }

    public UserlandProcess getCurrentlyRunning() {
        return currentlyRunning;
    }

	public void setCurrentlyRunning(UserlandProcess currentlyRunning) {
		this.currentlyRunning = currentlyRunning;
	}

	
}
