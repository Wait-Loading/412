import java.util.Random;

public class RandomDevice implements Device
{
  Random[] RandomDev= new java.util.Random[10];
  /**
   * Open() will create a new Random device and put it in an empty spot in the array.
   */
	@Override
	public int Open(String s) 
	{
		// TODO Auto-generated method stub
		 for (int i = 0; i < RandomDev.length; i++) 
		 {
	            if (RandomDev[i] == null)
	            {
	                if (s != null && s!="") 
	                {
	                	RandomDev[i] = new Random(Integer.parseInt(s));
	                } 
	                else 
	                {
	                	RandomDev[i] = new Random();
	                }
	                return i;
	            }
	        }
	        return -1; 
		
	}
/**
 * Close will null the device entry. 
 */
	@Override
	public void Close(int id) 
	{
		// TODO Auto-generated method stub
		 if (id >= 0 && id < RandomDev.length)
		    {
			 RandomDev[id] = null;
	        }
		 else
		    {
			 System.out.println("Exception CLOSE index out of bound");
		    }	
		 
	}
/**
 * Read will create/fill an array with random values. 
 */
	@Override
	public byte[] Read(int id, int size) 
	{
		  if (id >= 0 && id < RandomDev.length && RandomDev[id] != null) 
		    {
	            byte[] data = new byte[size];
	            RandomDev[id].nextBytes(data);
	            return data;
	        }
		  else
	        return null; 
	}
/**
 * Seek will read random bytes but not return them.
 */
	@Override
	public void Seek(int id, int to) {
		// TODO Auto-generated method stub
		 if (id >= 0 && id < RandomDev.length && RandomDev[id] != null) 
		 {
	            for (int i = 0; i < to; i++) 
	            {
	            	RandomDev[id].nextInt(); // read random bytes but not return them
	            }
	        }
		
	}
/**
 * Write will return 0 length and do nothing (since it doesn’t make sense)
 */
	@Override
	public int Write(int id, byte[] data) {
		// TODO Auto-generated method stub
		return 0;
	}
}
