
public class Pong extends UserlandProcess
{

	@Override
	void main() 
	{
		// TODO Auto-generated method stub
		System.out.println("I am pong : " +id);
		int i=0;
		
		while(true)
		{
			KernelMessages km= new KernelMessages(0, OS.GetPidByName("Ping"), i++, "pong".getBytes());
			OS.SendMessage(km);
			KernelMessages currentmessage= OS.WaitForMessage();
			if(currentmessage!=null)
			System.out.println(currentmessage);
			OS.Sleep(5);
		}
		
	}
}
