import java.io.IOException;
import java.util.StringTokenizer;

public class VFS implements Device {
	private Device[] devices = new Device[10];
	private int[] ids = new int[10];//The array that stores the id of the devices
	/**
	 * Open decides which device to open
	 */
	@Override
	public int Open(String s) 
	{

		StringTokenizer st = new StringTokenizer(s);//A string tokenizer to divides string into a tokens
		String Device_Name = st.nextToken();// The device name
		String arg ="";//The default argument to be passed with the device name 
		if(st.hasMoreTokens())
			arg = st.nextToken();//if an argument is present the we want to add those arguments as well
		Device device;
		if (Device_Name.equalsIgnoreCase("file")) //If the device name is file 
		{
			device = new FakeFileSystem();
		} 
		else //anything other than file
		{
			device = new RandomDevice();
		} 
		int Id = 0;//The id that the open of devices return
		try 
		{
			Id = device.Open(arg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < devices.length; i++) 
		{
			if (devices[i] == null) 
			{
				devices[i] = device;
				ids[i] = Id;
				return i;
			}
		}

		return -1; // No empty spot found
	}

	@Override
	public void Close(int id) 
	{
		if (id >= 0 && id < devices.length && devices[id] != null) 
		{
			try 
			{
				devices[id].Close(ids[id]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ids[id] = -1;

			devices[id] = null;
		}
	}

	@Override
	public byte[] Read(int id, int size) {
		if (id >= 0 && id < devices.length && devices[id] != null)
		{
			try {
				return devices[id].Read(ids[id], size);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * PASSES THE PARAMETERS TO THE DEVICE'S SEEK
	 */

	@Override
	public void Seek(int id, int to) 
	{
		if (id >= 0 && id < devices.length && devices[id] != null) {
			try {
				devices[id].Seek(ids[id], to);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 *    PASSES THE PARAMETERS TO THE DEVICE'S WRITE
	 */
	@Override
	public int Write(int id, byte[] data) 
	{
		if (id >= 0 && id < devices.length && devices[id] != null)
		{
			try 
			{
				return devices[id].Write(ids[id], data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
}
