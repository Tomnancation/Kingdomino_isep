import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			AppGameContainer apg = new AppGameContainer(new Jeu("Domi'Nation"));
			apg.setDisplayMode((int)(apg.getScreenWidth()*0.8),(int)(apg.getScreenWidth()*0.5), false);
			apg.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
