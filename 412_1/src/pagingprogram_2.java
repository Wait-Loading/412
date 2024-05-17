public class pagingprogram_2 extends UserlandProcess 
{
	/**
	 * This is the main method for the test of the paging assignment 5.
	 */
	@Override
	void main() 
	{
		OS.AllocateMemory(1024);
		Write(12, (byte)2);
		System.out.println(Read(12));
		int j = OS.AllocateMemory(1024);// WE allocate the memory again
		System.out.println(j);//The start address is 0 as we have deleted all the pages from before
		OS.Sleep(1);
	}
}