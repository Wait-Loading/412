public class Main 
{
    public static void main(String[] args) 
    {
       OS.Startup(new HelloWorld());//We start the OS by giving it a process to run
        OS.CreateProcess(new GoodbyeWorld());//We create another process in the OS 
    }
}