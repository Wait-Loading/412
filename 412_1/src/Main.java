public class Main 
{
	public static void main(String[] args) 
	{
		//FOR ASSIGNMENT 5 test USE PAGING_TEST.java file not the Main.java
		//For the test the pid: 1 and pid: 6 should get demotion.

		//We should see realtime process more followed by the interactive processes and lastly background process 

		//The hello world and the good bye world sleeps for more than 10 mins so they should be only executed  once

		OS.Startup(new HelloWorld());//This will take pid=0 and the ideal process will take pid 1

		OS.CreateProcess_priority(new GoodbyeWorld(),OS.priority.interactive);//This will take pid 2 
		OS.CreateProcess_priority(new Realtime(),OS.priority.realtime);//This will take pid 3
		OS.CreateProcess_priority(new interactive(),OS.priority.interactive);//This will take pid 4 
		OS.CreateProcess_priority(new background(),OS.priority.background);//This will take pid 5
		OS.CreateProcess_priority(new demotion_test(),OS.priority.realtime);//This will take pid 6
		//FOR ASSIGNMENT 3 test USE Devices_test.java file not the Main.java , if using eclipse make sure to close the previous run before we do the next run
		//FOR ASSIGNMENT 5 test USE PAGING_TEST.java file not the Main.java
		//FOR ASSIGNMENT 6 test USE Virtual_Memory_TEST.java file not the Main.java

	}
}