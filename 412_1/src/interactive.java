
public class interactive extends UserlandProcess
{
	void main() {
		while(true)
		{
			System.out.println("Interactive process");
			try {
			    Thread.sleep(50); // sleep for 50 ms
			} catch (Exception e) { }
			cooperate();
	      OS.Sleep(500);

		}	
	}
}
