
public class GoodbyeWorld extends UserlandProcess 
{
@Override
/**
 * Test program that runs the message goodbyeworld and calls cooperate to switch the process
 */
void main() 
{
	while(true)
	{
		System.out.println("GoodbyeWorld");
		try {
		    Thread.sleep(50); // sleep for 50 ms
		} catch (Exception e) { }
		cooperate();
		OS.Sleep(100);
	}	
}
}
