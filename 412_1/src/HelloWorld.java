
public class HelloWorld extends UserlandProcess
{
/**
 *  * Test program that runs the message HelloWorld and calls cooperate to switch the process

 */

@Override
void main() 
{
	while(true)
	{
		System.out.println("HelloWorld");
		try {
		    Thread.sleep(50); // sleep for 50 ms
		} catch (Exception e) { }
		cooperate();
      OS.Sleep(100);
      

	}	
}
}
