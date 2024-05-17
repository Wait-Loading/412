
public class demotion_test extends UserlandProcess
{
	void main() {
		while(true)
		{
			System.out.println("Demoting process");
			try {
			    Thread.sleep(50); // sleep for 50 ms
			} catch (Exception e) { }
			cooperate();

		}	
	}
}
