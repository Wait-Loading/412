public class pagingprogram extends UserlandProcess 
{
	/**
	 * This is the main method for the test of the paging assignment 6.
	 */
	@Override
	void main() 
	{
		
		
		
		OS.AllocateMemory(102400);//We allocate a lot of memory
		OS.Sleep(5);
		  for(int j=1;j<102400;j++) 
		  {
			  Write(j, (byte) j);//we Write in a lot
		  }
		  OS.Sleep(2);// We switch
		 
		System.out.println(Read(12));//This will only be printed once as there is no switch process after this . Hence , we don't want to do anything.

	}
}