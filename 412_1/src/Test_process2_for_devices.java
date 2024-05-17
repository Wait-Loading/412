
public class Test_process2_for_devices extends UserlandProcess
{
	void main() 
	{
		/**
		 * this process will try to read the message read on the Process_for_devicetesting.java
		 */
		OS.seek(0, 0);
		OS.read(0, 4);
		OS.Sleep(2500);
		OS.write(0, new byte[] {22,123,100,43});//Now we will write something on the device and then the other process will read and give the output.
		OS.Sleep(10);
		
	}
}
