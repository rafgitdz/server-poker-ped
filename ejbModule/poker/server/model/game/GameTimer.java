package poker.server.model.game;

/**
 * @author PokerServerGroup
 * 
 *         Model class : GameTimer
 */

public class GameTimer   {

	private  long startTime;
	public long getStartTime() {
		return startTime;
	}
	
	private  final long LIMIT_PLAYER = 30*1000;
	private final long LIMIT_BLIND = 180*1000;

	

	public  long controlTime(long limit) {
		this.start();
		
//		while (System.nanoTime() - startTime < limit) {
		while (System.currentTimeMillis() - startTime < limit) {
			
		}
		
		return System.currentTimeMillis() ;
	}

	
	public  void start() {
//		startTime = System.nanoTime();
		startTime = System.currentTimeMillis();
	}


	public long playerTime(){
		return controlTime(LIMIT_PLAYER);
	}
	public long updateBlind(){
		return controlTime(LIMIT_BLIND);
	}
}
