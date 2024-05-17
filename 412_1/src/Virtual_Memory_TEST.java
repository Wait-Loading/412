//FOR ASSIGNMENT 6 test USE Virtual_Memory_TEST.java file not the Main.java
/***
 * 
 * 
 *    LET THE PROCESS RUN TILL 12 IS PRINTED . IT WILL MARK THE END OF EVERY PROCESS AND OS OPERATION. i.e THERE IS NOTHING LEFT TO RUN OR DO .
 *    IT WILL TAKE  ABOUT A MINUTE OR TWO TO BE PRINTED AS THE TEST TAKES LONG TIME TO EXECUTE.
 *    AT THE END OF THE TEST YOU WILL FIND A FILE CALLED "pagefile" which will be of 99 kilo bytes(for me and it should be the same for you).
 *    PRINTING OF 2 MEANS THAT THE SWAP IS WORKING
 *    PRINTING OF 12 MEANS THE LOAD IS WORKING.
 * 
 */
public class Virtual_Memory_TEST
{
	public static void main(String [] args)
	{
		//pagingprogram is like piggy as described in the assignment
		OS.Startup(new pagingprogram());//The start of the pagging test program 
		int j=0;
		
		/**
		 * Loop to instigate 19 more processes
		 */
		for(int k=0;k<19;k++)
		{
			OS.CreateProcess(new pagingprogram());	
			for(int i=0;i<OS.kernel.Pages_inUSE.length;i++)
			{
				if(OS.kernel.Pages_inUSE[i]!=true)
				{
					j=i;
					break;
				}
			}
			System.out.println("PAGES USED: "+j);//This will be zero as we switch as soon as we allocate , this is a test for lazy allocating as allocation should not mark a page in use
		}
		/*
		 * The below process prints 2 we just use it to test one more process other than above 20  and it just needs 1kb of memory .
		 * Thus, we test both process with small memory-need as well as processes with large memory-need.  
		 */
		OS.CreateProcess(new pagingprogram_2());//The start of the pagging test program 2 


	}
}
