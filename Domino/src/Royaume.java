import org.newdawn.slick.*;

public class Royaume extends Joueur { // !! On a rajouté extends Joueur
	
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
	
	// methode qui retourne le nom du type
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
			return "Chateau";
		default:
			return null;
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
	
	
	//
	
	


}
