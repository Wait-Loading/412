public class test_messages 
{
	 public static void main(String[] args) 
	    {
	       OS.Startup(new HelloWorld());//getting started with hello world as it was  in the rubrics
	       OS.CreateProcess(new GoodbyeWorld());//Then the goodbye world
	       OS.CreateProcess_priority(new Ping(),OS.priority.realtime);//The ping process
           OS.CreateProcess(new Pong());//The Pong Process
	    }

}