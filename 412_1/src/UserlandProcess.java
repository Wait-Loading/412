import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
public abstract class UserlandProcess implements Runnable {
	Thread java_thread;
	Boolean quantum;
	Semaphore semaphore;
	LinkedList<KernelMessages> message = new LinkedList<KernelMessages>();
	int id;

	// Memory management variables
	public static byte[] memory = new byte[1024 * 1024]; // 1MB memory
	static int[][] TLB = new int[2][2]; // TLB with 2 entries
	static int[] Page_Mapping = new int[100]; // Page mapping array

	public UserlandProcess() {
		java_thread = new Thread(this);
		semaphore = new Semaphore(0);
		quantum = false;
		java_thread.start();
	}

	void requestStop() {
		quantum = true;
	}

	abstract void main();

	boolean isStopped() {
		return semaphore.availablePermits() == 0;
	}

	boolean isDone() {
		return !java_thread.isAlive();
	}

	void start() {
		semaphore.release();
	}

	void stop() throws InterruptedException {
		semaphore.acquire();
	}

	public void run() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		main();
	}

	void cooperate() {
		if (quantum) {
			quantum = false;
			OS.switchProcess();
		}
	}

	public void sleep(int milliseconds) {
		OS.Sleep(milliseconds);
	}

	public String open(String s) {
		return s;
	}

	// Memory management methods
	/**
	 * The byte Read(int address)
	 * Read(3100)
	 * Virtual address = 3100, Virtual Page = 3100 / 1024 = 3, Page Offset = 3100%1024 = 28
	 * We look in the TLB and find that this is in physical page 7.
	 * Page offset (still) = 28, Physical address = 7 * 1024 + 28 = 7196
	 * @param address
	 * @return The read file
	 */
	byte Read(int address) 
	{
		int Virtual_Page = address / 1024;
		int Page_Offset = address % 1024;
		int Physical_Page = FindMapping(Virtual_Page);
		
		if (Physical_Page != -1) 
		{
			int physicalAddress = Physical_Page * 1024 + Page_Offset;
			return memory[physicalAddress]; 
		}
		else
		{
			OS.GetMapping(Virtual_Page);
			Physical_Page = FindMapping(Virtual_Page);
			if(Physical_Page<0)//if we found a new page
			{
				Physical_Page=OS.GetMapping(Virtual_Page);
			}
			int Physical_Address = Physical_Page * 1024 + Page_Offset;
			
			return memory[Physical_Address];
		}
	}
	/** 
	 * The write method that write value to the give address
	 * @param address The address to which we write stuff
	 * @param value The value to be wrote
	 */
	void Write(int address, byte value) {
		int Virtual_Page = address / 1024;
		int Page_Offset = address % 1024;
		int Physical_Page = FindMapping(Virtual_Page);

		if (Physical_Page != -1) 
		{
			int physicalAddress = Physical_Page * 1024 + Page_Offset;
			memory[physicalAddress] = value;
		}
		else
		{
			OS.GetMapping(Virtual_Page);
			Physical_Page = FindMapping(Virtual_Page);
			if(Physical_Page<0)//if we found a new page
			{
				Physical_Page=OS.GetMapping(Virtual_Page);
			}
		
			int Physical_Address = Physical_Page * 1024 + Page_Offset;
			if(Physical_Address>=0)
			{
				memory[Physical_Address] = value;
			}
			else
			{
				System.out.println("Write fail for address: " +address);
				
				
			}
		}
	}
	/**
	 * The methode to find the TLB mapping
	 * @param Virtual_Page The page to be found
	 * @return -1 if mapping is not found or the page physical Number
	 */
	int FindMapping(int Virtual_Page)
	{
		for (int i = 0; i < 2; i++) 
		{
			if (TLB[i][0] == Virtual_Page)
			{
				return TLB[i][1];
			}
		}
		return -1;
	}
}