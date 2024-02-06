import java.util.ArrayList;


public class OS {
	public enum CallType {
	    CREATE_PROCESS,
	    SWITCH_PROCESS
	}

    private static Kernel kernel;
     static CallType currentCall;
     static ArrayList<Object> parameters = new ArrayList<>();
     static Object returnValue;

    public static int CreateProcess(UserlandProcess up) {
        parameters.clear();
        parameters.add(up);
        currentCall = CallType.CREATE_PROCESS;
        switchToKernel();
        returnValue=0;
        return (int) returnValue;
    }

    public static void Startup(UserlandProcess init) {
        kernel = new Kernel();
        CreateProcess(init);
        CreateProcess(new idle_process());
    }

    private static void switchToKernel() {
        kernel.start();
        
        if (kernel.scheduler.getCurrentlyRunning() != null) {
            try {
            	kernel.scheduler.currentlyRunning.stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else
        {try {
		    Thread.sleep(10); // sleep for ms
		} catch (Exception e) { }
        	
        }
      
        
    }

	public static void switchProcess() {
		// TODO Auto-generated method stub
		parameters.clear();
		currentCall=CallType.SWITCH_PROCESS;
		switchToKernel();
		
	}
}
