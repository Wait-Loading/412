import java.io.IOException;

public interface Device {
	    int Open(String s) throws IOException;
	    void Close(int id) throws IOException;
	    byte[] Read(int id,int size) throws IOException;
	    void Seek(int id,int to) throws IOException;
	    int Write(int id, byte[] data) throws IOException;
	}


