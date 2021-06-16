import org.newdawn.slick.*;

public class Royaume  { 
	
	private int type;
	private int NbCouronne;
	private boolean estOccupe;
	
	public static Image couronne;
	// initialisation du chateau, il est vide a la base
	
	public Royaume() {
		this.type = 0;
		this.NbCouronne = 0;
		this.estOccupe = false;
	}

	public Royaume(int type) {
		this.type = type;
		this.NbCouronne = 0;
		this.estOccupe = true;
	}
	
	// types
	public final static int CHAMPS = 1;
	public final static int FORET = 2;
	public final static int MER = 3;
	public final static int PRAIRIE = 4;
	public final static int MINE = 5;
	public final static int MONTAGNE = 6;
	public final static int CHATEAU = 100;
	
	// couleurs du miniLand  
	public final static Color CHAMPS_COLOR = Color.yellow;
	public final static Color FORET_COLOR = new Color(0, 66,37);// darkgreen
	public final static Color MER_COLOR = new Color(0,127,255);
	public final static Color PRAIRIE_COLOR = new Color(240, 232, 145); // greenyellow
	public final static Color MINE_COLOR = new Color(169,169,169);
	public final static Color MONTAGNE_COLOR = new Color(165, 42, 42); // brown
	public final static Color CASTLE_COLOR = Color.white;
	public final static Color EMPTY_COLOR = new Color(110, 1, 1);


	// Getters and setters
	public int getType() {
		return type;
	}

	public int getNbCouronne() {
		return NbCouronne;
	}

	public boolean estOccupe() {
		return estOccupe;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setNbCouronne(int nbCouronne) {
		NbCouronne = nbCouronne;
	}
	
	public void setStat(boolean stat) {
		estOccupe = stat;
	}
	
	
	public Color typeToColor() {
		switch (type) {
		case CHAMPS:
			return CHAMPS_COLOR;
		case FORET:
			return FORET_COLOR;
		case MER:
			return MER_COLOR;
		case PRAIRIE:
			return PRAIRIE_COLOR;
		case MINE:
			return MINE_COLOR;
		case MONTAGNE:
			return MONTAGNE_COLOR;
		case CHATEAU:
			return CASTLE_COLOR;
		default:
			return EMPTY_COLOR;

		}
	}
	
	
	
	//methode to string qui retourne la valeur de chaque type
	public static int typeToInt(String type) {
		switch (type) {
		case "Champs":
			return CHAMPS;
		case "Foret":
			return FORET;
		case "Mer":
			return MER;
		case "Prairie":
			return PRAIRIE;
		case "Mine":
			return MINE;
		case "Montagne":
			return MONTAGNE;
		case "Chateau":
			return CHATEAU;
		default:
			return 0;

		}
	}

	public Image typeToImage() throws SlickException {
		switch (type) {
		case CHAMPS:
			return Jeu.CHAMPS_IMAGE;
		case FORET:
			return Jeu.FORET_IMAGE;
		case MER:
			return Jeu.MER_IMAGE;
		case PRAIRIE:
			return Jeu.PRAIRIE_IMAGE;
		case MINE:
			return Jeu.MINE_IMAGE;
		case MONTAGNE:
			return Jeu.MONTAGNE_IMAGE;
		case CHATEAU:
			return Jeu.CHATEAU_IMAGE;
		default:
		case 0:
			return Jeu.EMPTY_IMAGE;

		}
	}
	
	public static String typeToString(int type) {
		switch (type) {
		case CHAMPS:
			return "Champs";
		case FORET:
			return "Foret";
		case MER:
			return "Mer";
		case PRAIRIE:
			return "Prairie";
		case MINE:
			return "Mine";
		case MONTAGNE:
			return "Montagne";
		case CHATEAU:
			return "Castle";
		default:
			return null;

		}
	}
	public void render(Graphics graphics, float x, float y) {
		
		try {
			typeToImage().draw(x, y, Jeu.dominoWidth, Jeu.dominoWidth);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (NbCouronne != 0) {
			graphics.setColor(Color.white);
			graphics.drawString(String.valueOf(NbCouronne), x + Jeu.dominoWidth / 2, y + Jeu.dominoWidth / 2);
		}

	}
	
	public void renderMini(Graphics graphics, float x, float y) {
		graphics.setColor(typeToColor());
		graphics.fillRect(x, y, Jeu.dominoWidth * 0.2f, Jeu.dominoWidth * 0.2f);
		
	}
	


	
}
