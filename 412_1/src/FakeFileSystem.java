import java.io.RandomAccessFile;
import java.io.IOException;

public class FakeFileSystem implements Device 
{
    private RandomAccessFile[] FAKE_files = new RandomAccessFile[10];//The Random access file array
 
    /**
     * The method that opens file device
     */
    @Override
    public int Open(String s) throws IOException 
    {
        if (s == null || s=="") 
        {
            throw new IllegalArgumentException("Filename cannot be null or empty");//If the name is not given 
        }
        for (int i = 0; i < FAKE_files.length; i++) 
        {
            if (FAKE_files[i] == null) 
            {
                FAKE_files[i] = new RandomAccessFile(s, "rw");
                return i;
            }
        }
        return -1; 
    }

    @Override
    public void Close(int id) throws IOException 
    {
        if (id >= 0 && id < FAKE_files.length && FAKE_files[id] != null) 
        {
            FAKE_files[id].close();
            FAKE_files[id] = null;
        }
    }
    /**
     * Read from the file till the size and return the bytes read from the file
     */
    @Override
    public byte[] Read(int id, int size) throws IOException 
    {
        if (id >= 0 && id < FAKE_files.length && FAKE_files[id] != null) 
        {
            byte[] read = new byte[size];//Make a byte array of the size we want to read
            FAKE_files[id].read(read);//Store what was read on a byte array called read 
            return read;
        }
        return null; 
    }
/**
 * Seek the pointer to the "to" index
 */
    @Override
    public void Seek(int id, int to) throws IOException 
    {
        if (id >= 0 && id < FAKE_files.length)
        {
            FAKE_files[id].seek(to);
        }
    }
    /**
     * writes the data on the  file device id
     */
    @Override
    public int Write(int id, byte[] data) throws IOException 
    {
        if (id >= 0 && id < FAKE_files.length )
        {
        	 FAKE_files[id].write(data);
            return 0;
        }
        return -1; 
    }
}
