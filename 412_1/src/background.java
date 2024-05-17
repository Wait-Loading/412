
public class background extends UserlandProcess
{
	void main() {
		while(true)
		{
			System.out.println("background process");
			try {
			    Thread.sleep(50); // sleep for 50 ms
			} catch (Exception e) { }
			cooperate();
	      OS.Sleep(500);

		}	
	}
}
