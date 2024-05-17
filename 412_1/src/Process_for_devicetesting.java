
public class Process_for_devicetesting extends UserlandProcess
{
/**
 *  
 *  * Test program that runs the message HelloWorld and calls cooperate to switch the process
 *  Test INSTRUCTION: RUN Devices_test.java
 *   Use the Console to see how the OS is opening various devices and reading , writing on it. 
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
 *  
 */

@Override
void main() 
{
	
		OS.open("Random");
		OS.read(0, 1);
		OS.close(0);
		OS.read(0, 1);
		
		
		System.out.println("\n\n\n\nTEST FOR FILLING ALL WITH RANDOM DEVICES and see how it goes\n\n\n\n-------------------------------------------------------");
		for(int i=0;i<=10;i++)
		{
			System.out.println("The Random device opening");

			OS.open("Random "+String.valueOf(i));
			OS.read(i, 3);
			System.out.println("---------------------------------------------------------");
			
		}
		for(int i=0;i<10;i++)
		{		OS.close(i);

		}
		System.out.println("\n\n\n\n---------------------------------------------------------");

		OS.open("file HIIAMJAY.txt");
		OS.write(0, new byte[] {21,34,23,43});
		OS.seek(0, 0);
		OS.read(0,4);
		OS.close(0);
        OS.read(0, 1);
        
		System.out.println("---------------------------------------------------------");

		System.out.println("\n\n\n\nTEST FOR FILLING ALL WITH RANDOM DEVICES and FILE devices see how it goes\n\n\n\n-------------------------------------------------------");

        for(int i=0;i<=10;i++)
        {
        	if(i%2==0)
        	{
        OS.open("file HIIAMJAY_"+String.valueOf(i)+".txt");
		OS.write(i, new byte[] {21,34,23,43});
		OS.seek(i, 0);
		OS.read(i,4);
        	}
        	else
        	{

    			OS.open("Random "+String.valueOf(i));
    			OS.read(i, 3);
        	}
			System.out.println("---------------------------------------------------------");

        }
        for(int i=0;i<10;i++)
		{		
        	OS.close(i);
		}
		System.out.println("\n\n\n\n---------------------------------------------------------\n\n\n\n");
		System.out.println("Now we will test if multiple process can excess the same device we will open a device");
		System.out.println("\n\n\n\n---------------------------------------------------------\n\n\n\n");

        OS.open("file HIIAMJAY.txt");
        
		OS.write(0, new byte[] {21,34,23,43});//This will write and then the Test_process2_for_devices.java will try to read this 
		//OS.seek(0, 0);
		//OS.read(0,4);
		cooperate();
	     OS.Sleep(100);

		OS.read(0,4);//This will read the stuff written by the other process and print it

     //OS.Sleep(1);
}
}