import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class PCB implements Runnable
{
	private static int nextpid = 0;//the next p_id
	private int pid;//the current P_id
	private UserlandProcess ulp;//The instance of Userlandprocess we want to control
	public int demotion=0;//The integer that keeps the count how many times a process calls os.switch rather than os.seep so we can demote the process
	Integer[] devices= new Integer[10];//The devices array to store the number of devices 
	String Process_name;// The process name that we will store the name of the process 
	Thread  thread;// The PCB thread
	Semaphore semaphore;
	long wakeuptime;// The time after which the process should stop sleeping
	LinkedList<KernelMessages> message = new LinkedList<KernelMessages>();//A place where we can store the message that a process receives
	OS.priority p;// The proiority of the process
	Boolean shouldwait= false;// A boolean to store it the process should wait or not 
	Boolean Isrunning=false;
	Boolean ASSIGNED_MEMORY=false;
	//int[] PPN = new int[100];///The physical page number
	VirtualToPhysicalMapping[] VPM= new VirtualToPhysicalMapping[100];//The array of new class we make that we replace the PPN with.
	boolean is_disk_number=false;//An boolean to indicate if we have changed any thing on the disk
	Random random = new Random();  
	/**
	 * The method that returns the current user land process
	 * @return the current user land process
	 */
	public UserlandProcess getulp()
	{
		return ulp;
	}
	/**
	 * 
	 * @return The  pid of the current process
	 */
	public int get_pid()
	{
		return pid;
	}
	/**
	 * The constructor
	 * @param up The process instance
	 * @param p The priority of the process
	 */
	public PCB(UserlandProcess up,OS.priority p)
	{
		this.p=p;
		this.ulp = up;
		ulp.message=message;
		this.pid = nextpid++;
		ulp.id=pid;
		thread= new Thread(this);
		for(int i=0;i<10;i++)
		{
			devices[i]=-1;
		}

		this.semaphore = new Semaphore(0);
		Process_name= ulp.getClass().getSimpleName();
		//thread.start();
	}
	/**
	 * To stop the user_land thread
	 */
	public void stop() 
	{
		try 
		{
			ulp.stop();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (!ulp.isStopped())
		{
			try 
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	/**
	 *  
	 * @return  if the thread is alive or not
	 */
	public boolean isDone() 
	{
		return ulp.isDone();
	}
	void start()
	{
		semaphore.release();
	}
	/**
	 * the current user land process
	 */
	public void run() 
	{
		ulp.start();
	}
	/**
	 * 
	 * @param l the wake uptime of the in long 
	 */
	public void setWakeUpTime(long l) 
	{
		wakeuptime=l;
		// TODO Auto-generated method stub
	}
	/**
	 * Updates TLB with the given virtual page number and its corresponding physical page number.
	 * Also updates the page mapping array.
	 * @param Virtual_Page_Number The virtual page number
	 * @param physicalPageNumber The physical page number
	 */
	public void Update_TLB(int Virtual_Page_Number, int PPN)
	{
		// Randomly choose one of the TLB entries to update
		int entryIndex = random.nextInt(2);

		// Update the chosen TLB entry with the virtual and physical page numbers
		ulp.TLB[entryIndex][0] = Virtual_Page_Number;
		ulp.TLB[entryIndex][1] = PPN;
	}

	/**
	 * Find the memory map entry as before. If the physical page is -1, find a physical page in the “in use” array and assign it. If there isn’t one available, that’s when we have to do a page swap to free one up. 
	 * Start the page swap process by adding a new method to the scheduler:
	 * public KernelLandProcess getRandomProcess()
	 * Get a random process and find a page in the process that has physical memory. If there are none,
	 *  pick a different process and repeat until you find a physical page. Write the victim page to disk, 
	 *  assigning a new block of the swap file if they didn’t have one already. Set the victim’s physical page to -1 and ours to the victim’s old value.
	 * If we got a new physical page (remember – we could get here simply because the TLB didn’t have the data we needed), 
	 * we have to do one of two things – if data was previously written to disk (the on disk page number is not -1)
	 *  then we have to load the old data in and populate the physical page. If no data was ever written to disk, we have to populate the memory with 0’s. 
	 * 
	 * Retrieves the physical page number corresponding to the given virtual page number.
	 * If the mapping is not found in the TLB, performs an OS call to update TLB.
	 * @param Virtual_Page_Number The virtual page number
	 * @return The physical page number
	 */
	public int GetMapping(int Virtual_Page_Number) 
	{
		is_disk_number=false;
		for(int i=0;i<VPM.length;i++)//loop over all the VPN mappings in the current pcb
		{
			if(VPM[i]!=null)//Making sure we have promised it a memory or it it is not null then it can have some memory
			{
				boolean [] pages = OS.kernel.Pages_inUSE;//get the current pages from kernel
				if(VPM[i].PPN==-1 &&VPM[i].diskPageNUmber==-1)//if the current page is -1 and has no disk page allocation that means that this is a fresh mapping and no one stole from it.
				{
					/**
					 * The part of :
					 * If the physical page is -1, find a physical page in the “in use” array and assign it.
					 * is below here
					 */
					for(int j=0;j<pages.length;j++)
					{
						if(!pages[j])
						{
							OS.kernel.Pages_inUSE[j]=true;
							VPM[i].PPN=j;
							break;
						}
					}
					/**
					 * The part of :
					 * If there isn’t one available, that’s when we have to do a page swap to free one up. 
					 * Start the page swap process by adding a new method to the scheduler:
					 * public PCB getRandomProcess()
					 * Get a random process and find a page in the process that has physical memory. 
					 * If there are none, pick a different process and repeat until you find a physical page. 
					 * Write the victim page to disk, assigning a new block of the swap file if they didn’t have one already.
					 *  Set the victim’s physical page to -1 and ours to the victim’s old value.
					 *  is below
					 */
					if(VPM[i].PPN==-1 && VPM[i].diskPageNUmber==-1)
					{
						PCB pcb= OS.kernel.scheduler.getRandomProcess();//getting a random process
						if(pcb!=null)//making sure that there processes to get other than currently running
						{
							for (int k=0;k<pcb.VPM.length;k++)
							{
								while(pcb.VPM[k]==null)//If this PCB has no memory allocated we have to find a new pCB
								{
									pcb= OS.kernel.scheduler.getRandomProcess();
									k=0;// re set the k so we can go get the first VPM of the next part.
								}

								if(pcb.VPM[k]!=null)
								{
									if(pcb.VPM[k].PPN!=-1)//if our PPN is not -1 we have to swap file 
									{
										byte mem[] = new byte[1024];//The buffer page
										int t=0;//index of the buffer page
										//This loop reads the swap the memory 
										for(int l=pcb.VPM[k].PPN;l<(pcb.VPM[k].PPN+1024);l++)//loop over the index of the phical page number in the VPM of the victim pcb
										{
											int index= l*1024;
											if(index<1048576)
											{
												mem[t++]= pcb.getulp().memory[index];
											}					    		
										}
										OS.kernel.vfs.Seek(0, OS.kernel.page_tracker*1024);// seek to the place where we have to write in the file
										OS.kernel.vfs.Write(0, mem);//We WRITE THE MEMORY!!!
										int r=pcb.VPM[k].PPN;//get the return value
										pcb.VPM[k].diskPageNUmber=OS.kernel.page_tracker++;//store the  page_tracker as the diskPageNUmber

										pcb.VPM[k].PPN=-1;// Set the victim’s physical page to -1 
										VPM[i].PPN=r;//Set to the victim's old pcb.
										//pcb.VPM[k].PPN
										Update_TLB(Virtual_Page_Number,VPM[i].PPN);//UPDATE THE TLB AS BEFORE
										{
											return r;
										}

									}

								}

							}

						}
					}				
					Update_TLB(Virtual_Page_Number,VPM[i].PPN);//This is for the normal run with no swamping required
					{
						return i;
					}
				}
				else if(VPM[i].diskPageNUmber!=-1)
				{

					is_disk_number=true;

					OS.kernel.vfs.Seek(0, VPM[i].diskPageNUmber*1024);//seeking to the read part

					byte [] red= OS.kernel.vfs.Read(0, 1024);//reading  and storing the page 
					int ind=VPM[i].diskPageNUmber*1024;//GET the index for the VPM to be places
					for(int d=0;d<red.length;d++)
					{
						getulp().memory[ind]= red[d];//LOading the data from the disk
					}
					Update_TLB(Virtual_Page_Number,VPM[i].PPN);//Updating the TLB
					{
						return i;
					}
				}
			}
			else
			{
				return -1;
			}
		}
		return -1;// When we can't find the mapping we return -1
	}
	/**
	 * The resetTLB after the process reset
	 */
	public void resetTLB()
	{
		for(int i=0;i<ulp.TLB.length;i++)
		{
			for(int j=0;j<ulp.TLB[i].length;j++)
			{
				ulp.TLB[i][j]=-1;
			}
		}
	}
}