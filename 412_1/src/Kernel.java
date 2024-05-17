import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Kernel implements Runnable,Device
{

	private Thread thread;//The Kernel thread
	private Semaphore semaphore;//The semaphore to control the kernel thread
	Scheduler scheduler;//The schedular
	int created_pid;// A int to store the pid of the created process
	static VFS vfs;//Static as we want to use this in the scheduler class to close all the devices
	Object result;// an Object that I can use to get the return value from kernel to the OS
	HashMap<Integer, PCB> map = new HashMap<>();//a hash map which holds the pid and Processes that have been made before
	boolean[] Pages_inUSE = new boolean[1024];
	//FakeFileSystem SP= new FakeFileSystem();
	int page_tracker=0;
	int page_inuse=0;
	public Kernel() throws IOException 
	{
		this.thread = new Thread(this);
		this.semaphore = new Semaphore(0);
		this.scheduler = new Scheduler();
		vfs= new VFS();
		vfs.Open("file pagefile");
		//SP.Open("pagefile");
		thread.start();//starting the kernel thread (it runs the run method)
	}
	/**
	 * The start to resume the kernel thread
	 */
	void start()
	{
		semaphore.release();

	}
	/**
	 * The run() method is an infinite loop:
while (true)
mySemaphore.acquire() – to see if I should be running
switch on OS.currrentCall – for each of these, call the function that implements them
call run() on the next process to run (we will see this in the scheduler).
Implement the run() method. The only “currentCall” values we are expecting is CreateProcess and SwitchProcess (more on these in the scheduler, below).

	 */
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				
				semaphore.acquire();
				switch (OS.currentCall) 
				{
				case CREATE_PROCESS:
				{

					PCB pcb = new PCB((UserlandProcess) OS.parameters.get(0), OS.priority.realtime);
					created_pid=  scheduler.CreateProcess(pcb);
					map.put(pcb.get_pid(), pcb);

					break;
				}
				case SWITCH_PROCESS:
				{
					scheduler.currentlyRunning.demotion++;
					scheduler.currentlyRunning.resetTLB();
					//FreeMemory(0, 10240);
					scheduler.SwitchProcess();


					break;
				}
				case SLEEP:
				{
					scheduler.currentlyRunning.demotion=0;
					scheduler.sleep((int)OS.parameters.get(0));
					break;
				}
				case CreateProcess_priority:
				{
					PCB pcb = new PCB((UserlandProcess) OS.parameters.get(0), (OS.priority)OS.parameters.get(1));
					scheduler.CreateProcess_priority(pcb,(OS.priority)OS.parameters.get(1));
					map.put(pcb.get_pid(), pcb);
					break;
				}
				case open:
				{
					try {
						String s= (String)OS.parameters.get(0);
						int id= Open(s);
						System.out.println("Opened a device with id:"+id);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case close:
				{
					try {
						int id =(int) OS.parameters.get(0) ;
						Close(id );
						System.out.println("closed a device with id: "+ id );

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case Read:
				{
					try {

						int id=(int)OS.parameters.get(0);
						int to= (int) OS.parameters.get(1);

						byte [] readedbytes=	Read( id,to );
						if(readedbytes!=null)
						{
							//System.out.println("Read the following on the device with id "+(int) OS.parameters.get(0)+" : \n" );
							/*for(int i=0;i<readedbytes.length;i++)
							{
								System.out.print(readedbytes[i]+"\n");
							}*/
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case Seek:
				{
					Seek((int)OS.parameters.get(0) ,(int) OS.parameters.get(1));
					System.out.println(" seeked on the device with id "+(int) OS.parameters.get(0)+ " Not if we have failed to open it" );

					break;
				}
				case Write:
				{
					int result = Write((int)OS.parameters.get(0) ,(byte[]) OS.parameters.get(1));
					if(result!=-1)
						System.out.println(" Wrote on the device with id "+(int) OS.parameters.get(0) +" Not if we have failed to open it ");

					break;
				}
				case getpid:
				{
					result= GetPid();
					break;
				}
				case getpidbyname:
				{
					result= GetPidByName((String)OS.parameters.get(0));
					break;

				}
				case SendMessage:
				{
					SendMessage((KernelMessages)OS.parameters.get(0));
					break;

				}
				case WaitForMessage:
				{
					result= WaitForMessage();
					break;
				}
				case GetMapping:
				{
					result= GetMapping((int)OS.parameters.get(0));
					break;

				}
				case AM:
				{
					result= AllocateMemory((int)OS.parameters.get(0));
					if((int)result!=-1)
					{scheduler.currentlyRunning.ASSIGNED_MEMORY=true;}

					break;

				}
				case FM:
				{
					result= FreeMemory((int)OS.parameters.get(0),(int)OS.parameters.get(1));
					break;
				}
				}
				if (scheduler.currentlyRunning != null) 
				{
					scheduler.currentlyRunning.run();
				}  
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * Frees the memory starting from the specified virtual address and of the specified size.
	 * Returns true if memory was successfully freed, false otherwise.
	 */
	public boolean FreeMemory(int pointer, int size) 
	{
		// Calculate the start page index from the virtual address
		int pageIndex = pointer / 1024;

		// Ensure that the provided pointer is valid
		if (pageIndex < 0 || pageIndex >= Pages_inUSE.length)
		{
			return false; // Invalid pointer
		}

		// Check if the memory starting at the provided pointer was allocated
		if (!Pages_inUSE[pageIndex])
		{
			return false; // Memory was not allocated, nothing to free
		}

		// Mark the pages as free
		int numPages = size / 1024; // Calculate number of pages to free
		VirtualToPhysicalMapping pageToFree = scheduler.currentlyRunning.VPM[pageIndex];
		while(pageToFree!=null&&pageIndex<numPages)
		{
			scheduler.currentlyRunning.VPM[pageIndex++]=null;//MAKING THE VPM NULL THE FREEING OF THE MEMORY
		}
		scheduler.currentlyRunning.resetTLB();;
		pageIndex = pointer / 1024;
		for (int i = pageIndex; i < pageIndex + numPages && i < Pages_inUSE.length; i++) 
		{
			Pages_inUSE[i] = false;
		}
		// Memory freed successfully
		return true;
	}

	/**
	 * fOR ASSIGNEMENT 6 IT HAS BEEN MODIFEID NOT TO MARK THE PAGE IN USE AS TRUE AND INICIATE THE vpm 
	 * The unused have been commented out
	 * @param size
	 * @return
	 */
	private int AllocateMemory(int size)
	{
		int Num_PagesNeeded = size / 1024;
		int Start_Index = 0;
		int index =0;
		for(int k=0;k<scheduler.currentlyRunning.VPM.length;k++) 
		{
			if(scheduler.currentlyRunning.VPM[k]==null)
				break;
			index=k;;
		}
		// Finding a contiguous block of free pages
		for (int i = 0; i < Pages_inUSE.length; i++) 
		{
			boolean Free = true;
			
			if (Free) 
			{
				Start_Index = i;
				break;
			}
		}
		// If a suitable block is found
		if (Start_Index != -1) 
		{
			// Mark pages as in use
			for (int i = Start_Index; i < Start_Index + Num_PagesNeeded; i++)
			{
				//scheduler.currentlyRunning.PPN[index++]= i;
				if(index<100)
				scheduler.currentlyRunning.VPM[index++]= new VirtualToPhysicalMapping();

				//Pages_inUSE[i] = true;
			}
			// Return the start virtual address of the allocated memory
			return Start_Index * 1024;
		} else {
			return -1; // Allocation failed
		}
	}
	@Override
	public int Open(String s) throws IOException 
	{
		int result = -1;
		// TODO Auto-generated method stub
		for(int i=0;i<scheduler.getCurrentlyRunning().devices.length ; i++)
		{
			if((scheduler.getCurrentlyRunning().devices[i])==-1 )
			{
				result=vfs.Open(s);
				scheduler.getCurrentlyRunning().devices[i]=result;
				return i;
			}
		}
		if(result==-1)
		{
			System.out.println("FAILED");
		}

		return result;
	}
	@Override
	public void Close(int id) throws IOException {
		// TODO Auto-generated method stub

		vfs.Close(scheduler.getCurrentlyRunning().devices[id]);
		scheduler.getCurrentlyRunning().devices[id]=-1;

	}
	@Override
	public byte[] Read(int id, int size) throws IOException {
		// TODO Auto-generated method stub
		return	vfs.Read(id, size);

	}
	@Override
	public void Seek(int id, int to) throws IOException {
		// TODO Auto-generated method stub
		vfs.Seek(id, to);

	}
	@Override
	public int Write(int id, byte[] data) throws IOException {
		// TODO Auto-generated method stub
		return vfs.Write(id, data);
	}
	/**
	 * The method in the kernel that returns the Process id of the current process
	 * @return
	 */
	public int GetPid() 
	{
		return scheduler.GetPid();
	}
	/**
	 * The method to return the pid by name 
	 * @param name The name of the process
	 * @return The pid of the process whose name was in the parameters
	 */
	public int GetPidByName(String name) 
	{

		return scheduler.GetPidByName(name);
	}
	/**
	 * The method to send kernel message
	 * @param km The kernel message to be sent 
	 */
	public void SendMessage(KernelMessages km)
	{
		km.Send_Pid= GetPid() ;//Set the sender pid in the kernel message to the current pid
		KernelMessages km2= new KernelMessages(km);
		if(map.containsKey(km2.Target_Pid))
		{
			map.get(km2.Target_Pid).message.add(km2);
			map.get(km2.Target_Pid).shouldwait=false;
			scheduler.Stopwaiting();
		}
	}
	/**
	 * The Wait method that changes the boolean in the PCB to true saying that it should wait till the send message is called 
	 * @return The message from the send message if it was sent.
	 */
	KernelMessages WaitForMessage()
	{
		scheduler.currentlyRunning.shouldwait=true;
		if(scheduler.currentlyRunning.message.size()!=0)
		{
			scheduler.currentlyRunning.shouldwait=false;
			return scheduler.currentlyRunning.message.poll();
		}
		return null;
	}
	public int GetMapping(int VirtualMappingNumber)
	{
		return scheduler.currentlyRunning.GetMapping(VirtualMappingNumber);
		
	}
}
