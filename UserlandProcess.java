import java.util.concurrent.Semaphore;

public abstract  class UserlandProcess implements Runnable 
{
	  Thread java_thread;//The userland thread
	  Boolean quantum;//The A check for the quntum, It is true if it has expired
	  Semaphore semaphore;//The semaphore to start and stop the thread 
	    public UserlandProcess() 
	    {
	        java_thread = new Thread(this);//The creates a thread
	        semaphore = new Semaphore(0);//Intilized the semaphore 
	        quantum = false;
	        java_thread.start();//starting the thread
	    }
	  void requestStop()//we want the quantum to stop
	  {
		  quantum=true;
	  }
	  abstract void main();
	  boolean isStopped()
	  {
		  if (semaphore.availablePermits() == 0)//checks if there are any permits available 
		return true;
		  else
			  return false;
	  }
	  boolean isDone()//Checks the thread is alive or not
	  {
		  if(java_thread.isAlive())
		     return false;
		  else
			  return true;    	 
	  }
	  void start()
	  {
		  semaphore.release();
	  }
	  void stop() throws InterruptedException 
	  {
		  try {
	            semaphore.acquire();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	  }
	  public void run() 
	  {
		  try { 
			semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  main();
          
	  }
	  void cooperate()
	  {
		  if(quantum) 
		  {
			  quantum= false;
		     OS.switchProcess();
		  }
		  
	  }
	  public void launch()
	  {
	        java_thread.start();
	  }
}
