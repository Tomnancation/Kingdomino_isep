import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
//继承BasicGameState写出固定写法
public class CreerJoueur extends BasicGameState {
	
	private final static int ID = Jeu.CREAT_JoueurS;
	  Font font;
	  TextField textField;


/*
 * 1.font的作用
 * 2.textField是定义什么的
 * 3.init定于图像，render提交数据，update更新数据，*/
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		font = font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF,java.awt.Font.BOLD , 26), false);
		//new UnicodeFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.ITALIC, 26));
	    textField = new TextField(gc, gc.getDefaultFont(), 400, 300, 300, 50);  
	    // <- i get a error here and the fix is to cast argument gc to GUIContext// i replaced your font in the constructor with the container's default font and it works.
	    textField.setText("Player");
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics graph) throws SlickException {
		textField.render(gc, graph);
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
