import java.util.*;
import java.io.*;
import org.newdawn.slick.*;
public class Domino {
	
	public static Image couronne = Royaume.couronne;
	//position du domino
	private int x;
	private int y;
	
	//On definit les attributs de domino présents aussi dans le fichier csv
	private int Nbcouronne1;
	private int type1;
	private int Nbcouronne2;
	private int type2;
	private int NumDomino;
	
	public int direction;
	public boolean chosed;
	

	// types
	public final static int CHAMPS = Royaume.CHAMPS;
	public final static int FORET = Royaume.FORET;
	public final static int MER = Royaume.MER;
	public final static int PRAIRIE = Royaume.PRAIRIE;
	public final static int MINE = Royaume.MINE;
	public final static int MONTAGNE = Royaume.MONTAGNE;
	
	

	// directions
	public final static int D1 = 1;
	public final static int D2 = 2;
	public final static int D3 = 3;
	public final static int D4 = 4;
	
	
	// KEYS
	private final static int UP = Jeu.HAUT;
	private final static int DOWN = Jeu.BAS;
	private final static int TURN_LEFT = Jeu.GAUCHE;
	private final static int TURN_RIGHT = Jeu.DROITE;
	
	
	// Constructeur pour définir iniatialement un domino
	public Domino(int nbcouronne1, int type1, int nbcouronne2, int type2, int numDomino) {
		this.Nbcouronne1 = nbcouronne1;
		this.type1 = type1;
		this.Nbcouronne2 = nbcouronne2;
		this.type2 = type2;
		this.NumDomino = numDomino;
		this.direction = D1;
		this.chosed = false;
		this.x = Jeu.width / 2;
		this.y = Jeu.height / 2;

	}
	
	//Getters et Setters
	public int getNbcouronne1() {
		return Nbcouronne1;
	}

	public int getType1() {
		return type1;
	}

	public int getNbcouronne2() {
		return Nbcouronne2;
	}

	public int getType2() {
		return type2;
	}
		
	public int getNumDomino() {
		return NumDomino;
	}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
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
		default:
			return null;

		}
	}

	

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
		default:
			return 0;

		}
	}
	
	public void turnDomino() {
		/*
		 * if (D1 <= direction && direction < D4) { direction++; } else { direction =
		 * D1; }
		 */
		boolean over = false;
		do {
			System.out.println("Turn Domino ? Y/N");

			switch (Console.inputString()) {
			case "Y":
				direction = direction == D4 ? D1 : ++direction;
				break;
			case "N":
				over = true;
				break;
			default:
				System.out.println("Error !");
				break;

			}
		} while (!over);

	}
	public void turnDminoRight() {
		direction = direction == D4 ? D1 : ++direction;
	}

	public void turnDominoLeft() {
		direction = direction == D1 ? D4 : --direction;
	}
	
	// valid check
		public static boolean crownNumValid(int crownNum) {
			return 0 <= crownNum && crownNum <= 3;
		}

		public static boolean typeValid(int type) {
			return CHAMPS <= type && type <= MONTAGNE;
		}

		public static boolean dominoNumValid(int NumDomino) {
			return 1 <= NumDomino && NumDomino <= 48;
		}

		public boolean dominoValid() {
			return typeValid(type1) || typeValid(type2) || crownNumValid(Nbcouronne1) || crownNumValid(Nbcouronne2)
					|| dominoNumValid(NumDomino);
		}

		public static boolean directionValid(int dir) {
			return D1 <= dir && dir <= D4;
		}
		
		public void printDominoInfo() {
			System.out.println("Domino No." + NumDomino);
			System.out.println("Type 1 : " + typeToString(type1));
			System.out.println("crown number 1 : " + Nbcouronne1);
			System.out.println("Type 2 : " + typeToString(type2));
			System.out.println("crown number 2 : " + Nbcouronne2);
			System.out.println();
		}
		
		public static Image typeToImage(int type) throws SlickException {
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
			default:
				return new Image(Jeu.dominoWidth, Jeu.dominoWidth);

			}
	
		}
		public void update(Input input) {
			
			this.x = input.getMouseX();
			this.y = input.getMouseY();

			if (input.isKeyPressed(TURN_LEFT)) {
				this.turnDominoLeft();
			}
			if (input.isKeyPressed(TURN_RIGHT) || input.isMousePressed(input.MOUSE_RIGHT_BUTTON)) {
				this.turnDminoRight();
			}
		}
}
