
public class HelloWorld extends UserlandProcess
{


@Override
void main() {
	while(true)
	{
		System.out.println("HelloWorld");
		try {
		    Thread.sleep(50); // sleep for 50 ms
		} catch (Exception e) { }
		cooperate();


	}	
}
}
