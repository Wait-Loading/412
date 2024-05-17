/*
 *  Test INSTRUCTION: Use the Console to see how the OS is opening various devices and reading , writing on it. 
 *  
 *  
 *  We have first random device that checks the opening , reading and closing of a random device
 *  
 *  
 *  Then We have a loop that opens 10 devices and try to open the 11 but fails which means the fail safe is working
 *  
 *  Then we have test for File device where we test for open , close , seek , write and read of the file device.
 *  
 *  Lastly we have a for loop that makes file and random devices  and we are indicating that we close all the devices when the cooperate is called .
 *  
 */
public class Devices_test 
{
	 public static void main(String[] args) 
	    {
	       OS.Startup(new Process_for_devicetesting());
           OS.CreateProcess(new Test_process2_for_devices());
	    }

}
