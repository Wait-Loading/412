
public class Ping extends UserlandProcess
{

	@Override
	void main() {
		// TODO Auto-generated method stub
		System.out.println("I am ping : " +id);
		//OS.Sleep(70);
		int i=0;
		while(true)
	{//We use get pid by name here so it will work as rubrics
		KernelMessages km= new KernelMessages(0, OS.GetPidByName("Pong"), i++, "ping".getBytes());//this the message that we want to send to the pong 
		OS.SendMessage(km);//Send the kernal message to pong
		//OS.WaitForMessage();
		KernelMessages currentmessage= OS.WaitForMessage();//This will make the process wait for its message 
		if(currentmessage!=null)
		System.out.println(currentmessage);
		OS.Sleep(50);
		}	
	}
}
