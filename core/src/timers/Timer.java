package timers;

public class Timer {
	
	private int endTime;
	private int counter;
	
	public Timer(int endTime, boolean isDuration){
		this.endTime = endTime;
		if (isDuration) counter = 0;
		else counter = endTime + 1;
	}
	
	public void restart(){ counter = 0; }
	
	public void countUp(){ counter++; }
	
	public void countDown(){ counter--; }
	
	public void setEndTime(int endTime){ this.endTime = endTime; }
	
	public boolean timeUp(){ return (counter > endTime); }
	
	public int getCounter(){ return counter; }
	
	public int getEndTime(){ return endTime; }
	
	public void end(){ counter = endTime + 1; }
	
}