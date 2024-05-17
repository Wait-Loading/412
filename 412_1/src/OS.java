import java.util.ArrayList;
import java.io.*;


public class OS 
{
    //private static reference to the one and only instance of the Kernel class.
    static Kernel kernel;
    
    public enum CallType 
	{
	    CREATE_PROCESS,
	    SWITCH_PROCESS,
	    SLEEP,
	    CreateProcess_priority, open, close, Write, Seek, Read, getpid, getpidbyname, SendMessage, WaitForMessage, GetMapping, AM, FM
	}
    public enum priority{
    	realtime,
    	interactive,
    	background
    }
    public priority p;
     static CallType currentCall;// A static instance of that enum (I called this “currentCall”)
     
     static ArrayList<Object> parameters = new ArrayList<>();//A static array list of parameters to the function; we don’t know what they will be, so we will make it an arraylist of Object.
     static Object returnValue;
/**
 * Creates the process stores it in the scheduler's processes linked list 
 * and we call switch to kernel whcih resumes the kernel's process
 * @param up //The process that we want to add
 * @return 
 */
    public static int CreateProcess(UserlandProcess up)
    {
        parameters.clear();
        parameters.add(up);
        currentCall = CallType.CREATE_PROCESS;
        switchToKernel();
        returnValue=0;
        return (int) returnValue;
    }
    /**
     * Creates the process stores it in the scheduler's processes linked list 
     * and we call switch to kernel whcih resumes the kernel's process
     * @param up //The process that we want to add
     * @return 
     */
        public static int CreateProcess_priority(UserlandProcess up,priority p)
        {
            parameters.clear();
            parameters.add(up);
            parameters.add(p);
            currentCall = CallType.CreateProcess_priority;
            switchToKernel();
            returnValue=0;
            return (int) returnValue;
        }
/**
 * public static void Startup(UserlandProcess init) Creates the Kernel() and calls CreateProcess twice – once for “init” and once for the idle process.
 * @param init the process we want to start
 */
    public static void Startup(UserlandProcess init) 
    {
        try {
			kernel = new Kernel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        CreateProcess(init);
        CreateProcess(new idle_process());
    }
/**
 * First, we call start() on the kernel.
 * Then if the scheduler (you might need an accessor here) has a currenlyRunning, call stop() on it.
 *
 */
    private static void switchToKernel() 
    {
        kernel.start();
        
        if (kernel.scheduler.getCurrentlyRunning() != null)
        {
            kernel.scheduler.currentlyRunning.stop();
        }
        else
        {
        	try {
		    Thread.sleep(10); // sleep for ms
		} catch (Exception e) { }
        	
        }     
    }
/**
 * The method to witch the process in the kernel
 */
	public static void switchProcess() 
	{
		// TODO Auto-generated method stub
		parameters.clear();
		currentCall=CallType.SWITCH_PROCESS;
		switchToKernel();
	}
	
	public static void Sleep(int milliseconds)
	{
		 parameters.clear();
	        parameters.add(milliseconds);
	        currentCall = CallType.SLEEP;
	        switchToKernel();
	      
	}
	/**
	 * The methods that changes the current call to open and clears and adds the parameters as needs
	 * Finally calls switch to kernel .
	 * @param s The string to open the device
	 */
	public static void open(String s)
	{
		parameters.clear();
        parameters.add(s);
		currentCall=CallType.open;
		switchToKernel();
	}
	/**
	 * The methods that changes the current call to close and clears and adds the parameters as needs
	 * Finally calls switch to kernel .
	 * @param id The id of the device to be closed
	 */
	public static void close(int id)
	{
		parameters.clear();
        parameters.add(id);
		currentCall=CallType.close;
		switchToKernel();
	}
	/**
	 * The methods that changes the current call to read and clears and adds the parameters as needs
	 * Finally calls switch to kernel .
	 * @param id the id of the device to read from 
	 * @param size The size of the bytes toi be read
	 */
	public static void read(int id,int size)
	{
		parameters.clear();
        parameters.add(id);
        parameters.add(size);

		currentCall=CallType.Read;
		switchToKernel();
	}
	/**
	 * The methods that changes the current call to seek and clears and adds the parameters as needs
	 * Finally calls switch to kernel .
	 * @param id the id of the device to seek on
	 * @param to the index to which we want to seek
	 */
	public static void seek(int id,int to)
	{
		parameters.clear();
        parameters.add(id);
        parameters.add(to);

		currentCall=CallType.Seek;
		switchToKernel();
	}
	/**
	 * The methods that changes the current call to write and clears and adds the parameters as needs
	 * Finally calls switch to kernel .
	 * @param id id of the device to write on
	 * @param data the data to write on the device
	 */
	public static void write(int id,byte[] data)
	{
		parameters.clear();
        parameters.add(id);
        parameters.add(data);

		currentCall=CallType.Write;
		switchToKernel();
	}
	/**
	 * Switches to kernel
	 * @return the pid  of the current process
	 */
	public static int GetPid()
	{
		
		parameters.clear();
		currentCall=CallType.getpid;
		switchToKernel();
		returnValue= kernel.result;
		if(returnValue!=null&& returnValue.getClass()== new Integer(0).getClass())
			return (int) returnValue;	
		return -1;
	}
	/**
	 * 
	 * @param name The process name of which we want the id of
	 * @return The pid of the process
	 */
	public static int GetPidByName(String name)
	{
		
		parameters.clear();
        parameters.add(name);
		currentCall=CallType.getpidbyname;
		switchToKernel();
		returnValue= kernel.result;
		if(returnValue!=null && returnValue.getClass()== new Integer(0).getClass())
		   {return (int) returnValue;}
		
		return -1;
	}
	/**
	 * sends the message km to the kernel 
	 * @param km
	 */
	public static void SendMessage(KernelMessages  km)
	{
		parameters.clear();
		parameters.add(km);
		currentCall=CallType.SendMessage;
		switchToKernel();
	}
	/**
	 * Tells the kernel is a process needs to wait or not .
	 * @return The message we have received if not the null
	 */
	public static KernelMessages WaitForMessage()
	{
		parameters.clear();
		currentCall=CallType.WaitForMessage;
		switchToKernel();
		returnValue= kernel.result;
		if(returnValue!=null && returnValue.getClass()== new KernelMessages(0, 0, 0, new byte[] {}).getClass())
			return (KernelMessages) returnValue;	
		return null;
	}
	public static int GetMapping(int virtualPageNumber) 
	{
		parameters.clear();
	    parameters.add(virtualPageNumber);
	    currentCall = CallType.GetMapping;
	    switchToKernel();
		returnValue= kernel.result;
		if(returnValue!=null ) {
		return (int) returnValue;}
		return -1;
    }
	 /**
     * Requests memory allocation from the kernel.
     * @param size The size of memory to allocate
     * @return The start virtual address of the allocated memory, or -1 if allocation fails
     */
    public static int AllocateMemory(int size) {
        // Ensure size is a multiple of 1024
        if (size % 1024 != 0) {
            return -1; // Failure
        }
        parameters.clear();
		currentCall=CallType.AM;
		  parameters.add(size);
		switchToKernel();
		returnValue= kernel.result;
		if(returnValue!=null )
			return (int) returnValue;
		return -1;	
    }

    /**
     * Requests memory deallocation from the kernel.
     * @param pointer The virtual address of the memory to free
     * @param size The amount of memory to free
     * @return True if memory deallocation succeeds, false otherwise
     */
    public static boolean FreeMemory(int pointer, int size) {
    
        // Ensure pointer and size are multiples of 1024
        if (pointer % 1024 != 0 || size % 1024 != 0) {
            return false;
        }
        parameters.clear();
		currentCall=CallType.FM;
		  parameters.add(pointer);
		  parameters.add(size);
		switchToKernel();
		returnValue= kernel.result;
		if(returnValue!=null )
			return (boolean) returnValue;
		return false;	
       
    }
}
