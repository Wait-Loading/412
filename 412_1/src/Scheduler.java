import java.time.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler 
{
   // private LinkedList<PCB> Processes;//The linked list that store the processes
    PCB currentlyRunning;// a public reference to the UserlandProcess that is currently running
    private Timer timer;//a private instance of the Timer class 
    private Clock clock; // New Clock instance
    private LinkedList<PCB> SleepingProcesses; // New list for sleeping processes
    private LinkedList<PCB> realtime;//A new list of real_time processes
    private LinkedList<PCB> interactive;//A new list of interactive processes
    private LinkedList<PCB> background;//A new list of background processes
    private LinkedList<PCB> Waiting;//A new list of Waiting processes

/**schedule (using the timer) the interrupt for every 250ms. 
 * Inside the interrupt, call “requestStop()” on the currently running process.
 * 
 */
    public Scheduler() 
    {
       // this.Processes = new LinkedList<>();
        this.timer = new Timer();
        this.clock = Clock.systemDefaultZone(); // Initialize the Clock instance
        realtime= new LinkedList<>();
        interactive= new LinkedList<>();
        background=new LinkedList<>();
        SleepingProcesses=new LinkedList<>();
        Waiting=new LinkedList<>();
        this.timer.schedule(new TimerTask() 
        {
            @Override
            public void run() 
            {
                if (currentlyRunning!= null) 
                {
                	currentlyRunning.getulp().requestStop();
                }
            }
        }, 0, 2);
    }
/**
 * CreateProcess should add the PCB process to the list of real time processes and, 
 * if nothing else is running, call switchProcess() to get it started. 
 * @param pcb the process we want to work with
 * @return
 */
    public int CreateProcess(PCB pcb)
    {
        realtime.add(pcb);
        if (getCurrentlyRunning() == null)
        {
            SwitchProcess();
        }
        return 0;
    }
    /**
     * SwitchProcess’ job, overall, is to take the currently running process see if it needs to be demoted and put it 
     * at the end of the list of the respective of it's priority.
     *  It then implements a takes the head of the list and runs it. 
     *  a probabilistic model. If there are real-time processes, 
     *  then 6/10 we will run a real-time process,
     *   3/10 we will run an interactive process (if there is one) 
     *   otherwise 1/10 we will run a background process. Otherwise, 
     *   if there are interactive process(es), 
     *   we will ¾ run interactive and ¼ run background. 
     *   If there are only background, then use the first of those.
     */
    public void SwitchProcess() 
    {
    	/**
    	 * if all the processes(who should be sleeping) are sleeping  and we have process that do not call 
    	 * sleep then we will be stuck here to avoid that we also check if we have to wake up any one
    	 * before we change the process 
    	 */PCB  previous = null ;
    	if(getCurrentlyRunning() !=null)
    		previous = getCurrentlyRunning();
    	 for (int i =0;i<SleepingProcesses.size();i++)
         {
         	PCB pulled=SleepingProcesses.get(i);
         	if (pulled.wakeuptime<=clock.millis())
         	{
             	SleepingProcesses.remove(i);

         		if (pulled.p==OS.priority.realtime)
         	    {
         		realtime.add(pulled);
         	    }
         		else if (pulled.p==OS.priority.interactive)
         		{
         			interactive.add(pulled);
         		}
         		else
         		{
         			background.add(pulled);
         		}
         	}
         	
         }
    	 
        if (getCurrentlyRunning() != null && !getCurrentlyRunning().isDone() && !SleepingProcesses.contains(getCurrentlyRunning()))//if there is a currently running process which is't sleeping we have to add it back to the respective list before we start the new process
        {
        	if (getCurrentlyRunning().demotion>=5)//if the currently running needs to be demoted
            {
        		if (getCurrentlyRunning().p==OS.priority.realtime)
        	    {
        			interactive.add(getCurrentlyRunning());
        			getCurrentlyRunning().p=OS.priority.interactive;
        			System.out.println("demoted " + getCurrentlyRunning().get_pid()+" to interactive _______________________________________");
        			getCurrentlyRunning().demotion=0;
        	    }
        		else if (getCurrentlyRunning().p==OS.priority.interactive)
        		{
        			background.add(getCurrentlyRunning());
        			getCurrentlyRunning().p=OS.priority.background;

        			System.out.println("demoted " + getCurrentlyRunning().get_pid()+" to background_____________________________________________");
        			getCurrentlyRunning().demotion=0;


        		}
        		
            }
        	else 
        	{
        	if (getCurrentlyRunning().p==OS.priority.realtime)
    	    {
    		realtime.add(getCurrentlyRunning());
    	    }
    		else if (getCurrentlyRunning().p==OS.priority.interactive)
    		{
    			interactive.add(getCurrentlyRunning());
    		}
    		else
    		{
    			background.add(getCurrentlyRunning());
    		}
        	}
        	
        }
        
        if(getCurrentlyRunning()!=null)
        {if(getCurrentlyRunning().isDone())
        {
        	if(getCurrentlyRunning().devices[0]!=null)
        	{
        		System.out.println("\n\n\n\nRESETTING ALL THE DEVICES");
        	}
       for(int i=0;i<10;i++)
       {
    	  
    	   if(getCurrentlyRunning().devices[i]!=null)
    	   {
    	     getCurrentlyRunning().devices[i]=-1;
    	   }
    	   }
        }
        }
        
        random_setCurrently_Running();
        if(currentlyRunning.shouldwait)
        {
        	Waiting.add(currentlyRunning);
            random_setCurrently_Running();
        }
       System.out.println(" RUNNING= "+currentlyRunning.Process_name);
     
    }
    /**
     * The method that applies the probability model and sets 
     * the running process to choose from the three processes list.
     */
    public void random_setCurrently_Running() 
    {
        double random ;
        if(!realtime.isEmpty())
        {
        	random = Math.random();
            if (random < 0.6 && !realtime.isEmpty())            // this will happen 6/10  times

         {
            setCurrentlyRunning(realtime.poll());    
        } 
        else if (random < 0.9 && !interactive.isEmpty())             // this will happen 3/10  times

        {
            setCurrentlyRunning(interactive.poll());    
        }
        else if (!background.isEmpty())            // this will happen 1/10  times

        {
            setCurrentlyRunning(background.poll());   
        }
        else
        {
            setCurrentlyRunning(realtime.poll());    
        }
        }
        else if(!interactive.isEmpty())
        {        random = Math.random();

        	if (random < 0.75 && !interactive.isEmpty())                 // This will happen 3/4  times

            {
                setCurrentlyRunning(interactive.poll());    
            }
            else if (!background.isEmpty()) // This will happen 1/4  times
            {
                setCurrentlyRunning(background.poll());   
            }
        }
        else if(!background.isEmpty())
        {
        	setCurrentlyRunning(background.poll());   
        }

        
    }

    /**
     * @return currently running process
     */
    public PCB getCurrentlyRunning()
    {
        return currentlyRunning;
    }
    /**
     * 
     * @param currentlyRunning the process that we want to set the currentlyrunning process to
     */
	public void setCurrentlyRunning(PCB currentlyRunning) 
	{
		this.currentlyRunning = currentlyRunning;
	}
	/**
	 * The method that puts the processes to sleep
	 * @param milliseconds
	 */
	public void sleep(int milliseconds)
	{
		 if (currentlyRunning != null) 
	        {
	          currentlyRunning.setWakeUpTime(clock.millis() + milliseconds);
	            SleepingProcesses.add(currentlyRunning);
	            SwitchProcess();
	        }
	}
	/**
	 * The new create process that takes in pcb and the priority to work with
	 * @param pcb 
	 * @param priority
	 */
	public void CreateProcess_priority(PCB pcb, OS.priority priority) 
	{
		 if (priority==OS.priority.realtime)
	    {
		realtime.add(pcb);
	    }
		else if (priority==OS.priority.interactive)
		{
			interactive.add(pcb);
		}
		else
		{
			background.add(pcb);
		}
		 if (getCurrentlyRunning() == null)
	        {
	            SwitchProcess();
	        }
		 
	}
	/**
	 * @return the pid of the currently running process
	 */
	int GetPid() 
	{
		return currentlyRunning.get_pid();
	}
	/**
	 * 
	 * @param Name
	 * @return the pid of the named process
	 */ 
	int GetPidByName(String Name)
	{
		for(int i=0;i<realtime.size();i++)//get from Realtime process
		{
			if(Name==realtime.get(i).Process_name)
				return realtime.get(i).get_pid();
		}
		for(int i=0;i<interactive.size();i++)//get from interactive
		{
			if(Name==interactive.get(i).Process_name)
				return interactive.get(i).get_pid();
		}
		for(int i=0;i<background.size();i++)//get from background
		{
			if(Name==background.get(i).Process_name)
				return background.get(i).get_pid();
		}
		for(int i=0;i<SleepingProcesses.size();i++)//get from sleeping 
		{
			if(Name==SleepingProcesses.get(i).Process_name)
				return SleepingProcesses.get(i).get_pid();
		}
		for(int i=0;i<Waiting.size();i++)//get from waiting
		{
			if(Name==Waiting.get(i).Process_name)
				return Waiting.get(i).get_pid();
		}
		return -1;
	}
	/**
	 * The method to stop the processes from waiting
	 */
	public void Stopwaiting() 
	{
		for(int i=0;i<Waiting.size();i++)
		{
			if(!Waiting.get(i).shouldwait)
			{
				if (Waiting.get(i).p==OS.priority.realtime)
         	    {
         		realtime.add(Waiting.get(i));
         	    }
         		else if (Waiting.get(i).p==OS.priority.interactive)
         		{
         			interactive.add(Waiting.get(i));
         		}
         		else
         		{
         			background.add(Waiting.get(i));
         		}
				Waiting.remove(i);
			}
		}
	}
	public PCB getRandomProcess() 
	{
		Random random = new Random();
	double selectprocess=	Math.abs(   random.nextDouble(2)+1);
	if(selectprocess==1 &&realtime.size()>0 )
	{
		//random = new Random();
		//double process=	random.nextDouble(realtime.size())+0;
		return realtime.get(0);
	}
	else if(selectprocess==2&& interactive.size()>0)
	{
		return interactive.get(0);

	}
	else if(selectprocess==3&&background.size()>0)
	{
		//random = new Random();
		//double process=	random.nextDouble(background.size())+0;
		return background.get(0);
	}
	else if(realtime.size()>0 )
	{
		return realtime.get(0);
	}
	else if(interactive.size()>0 )
	{
		return interactive.get(0);
	}
	else if(background.size()>0 )
	{
		return background.get(0);
	}
	else if(SleepingProcesses.size()>0 )
	{
		return SleepingProcesses.get(0);
	}
	else
		return null;
	
	
	}
}
