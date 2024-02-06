import java.util.concurrent.Semaphore;

public class Kernel implements Runnable {
    private Thread thread;
    private Semaphore semaphore;
    Scheduler scheduler;

    public Kernel() {
        this.thread = new Thread(this);
        this.semaphore = new Semaphore(0);
        this.scheduler = new Scheduler();
        thread.start();
    }
       void start()
       {
    	   semaphore.release();
       }
    @Override
    public void run() {
        while (true) {
            try {
                semaphore.acquire();
                switch (OS.currentCall) 
                {
                    case CREATE_PROCESS:
                    {
                        scheduler.CreateProcess((UserlandProcess) OS.parameters.get(0));
                        break;
                    }
                    case SWITCH_PROCESS:
                    {
                        scheduler.SwitchProcess();
                        break;
                    }
                }
                if (scheduler.currentlyRunning != null) 
                {
                	scheduler.currentlyRunning.start();      
                }  
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
