
public class idle_process extends UserlandProcess 
{

	@Override
	void main() 
	{
		while (true) 
		{
        try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        cooperate();
	    }
		
	}

	
}
