import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class KernelMessages 
{
     int Send_Pid;
     int Target_Pid;
     int MESSAGE;
     byte[] data;

    // Constructor
    public KernelMessages(int Send_Pid, int Target_Pid, int MESSAGE, byte[] data) 
    {
        this.Send_Pid = Send_Pid;
        this.Target_Pid = Target_Pid;
        this.MESSAGE = MESSAGE;
        this.data = data.clone(); // Make a copy of the data
    }

    // Copy constructor
    public KernelMessages(KernelMessages km) 
    {
    	 this.Send_Pid = km.Send_Pid;
         this.Target_Pid = km.Target_Pid;
         this.MESSAGE = km.MESSAGE;
         this.data = km.data;
    }
    @Override
    public String toString() 
    {
        return ("sender_Pid=" + Send_Pid +", Target_Pid=" + Target_Pid +", MESSAGE=" + MESSAGE +", data=" + new String(data, StandardCharsets.UTF_8));
    }
}
