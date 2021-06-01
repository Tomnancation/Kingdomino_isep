import java.util.*;
import java.io.*;
import org.newdawn.slick.*;
public class Domino {
	
	public static Image couronne = Royaume.couronne;
	//position du domino
	private int x;
	private int y;
	
	//On definit les attributs de domino pr¨¦sents aussi dans le fichier csv
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
	
	// colors
	public final static Color CHAMPS_COLOR = Royaume.CHAMPS_COLOR;
	public final static Color FORET_COLOR = Royaume.FORET_COLOR;
	public final static Color MER_COLOR = Royaume.MER_COLOR;
	public final static Color PRAIRIE_COLOR = Royaume.PRAIRIE_COLOR;
	public final static Color MINE_COLOR = Royaume.MINE_COLOR;
	public final static Color MONTAGNE_COLOR = Royaume.MONTAGNE_COLOR;


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
	
	
	// Constructeur pour d¨¦finir iniatialement un domino
	public Domino(int Nbcouronne1, int type1, int Nbcouronne2, int type2, int numDomino) {
		this.Nbcouronne1 = Nbcouronne1;
		this.type1 = type1;
		this.Nbcouronne2 = Nbcouronne2;
		this.type2 = type2;
		this.NumDomino = numDomino;
		this.direction = D1;
		this.chosed = false;
		this.x = Jeu.width / 2;
		this.y = Jeu.height / 2;

	}
	
	public static Image crown = Royaume.couronne;
	
	
	
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
	public void turnDminoDroite() {
		direction = direction == D4 ? D1 : ++direction;
	}

	public void turnDominoGauche() {
		direction = direction == D1 ? D4 : --direction;
	}
	
	// valid check
		public static boolean NbcouronneValid(int Nbcouronne) {
			return 0 <= Nbcouronne && Nbcouronne <= 3;
		}

		public static boolean typeValid(int type) {
			return CHAMPS <= type && type <= MONTAGNE;
		}

		public static boolean dominoNumValid(int NumDomino) {
			return 1 <= NumDomino && NumDomino <= 48;
		}

		public boolean dominoValid() {
			return typeValid(type1) || typeValid(type2) || NbcouronneValid(Nbcouronne1) || NbcouronneValid(Nbcouronne2)
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
		
		public void renderFixed(Graphics graphics, float x, float y) throws SlickException {

			graphics.setColor(Jeu.ALTERNATIVE_MESSAGE);
			graphics.drawString("No." + String.valueOf(NumDomino), x, y - Jeu.dominoWidth * 0.5f);
			typeToImage(type1).draw(x, y, Jeu.dominoWidth, Jeu.dominoWidth);
			graphics.setColor(Jeu.MESSAGE_COLOR);
			graphics.drawString(String.valueOf(Nbcouronne1), x + Jeu.dominoWidth / 2, y + Jeu.dominoWidth / 2);

			
			typeToImage(type2).draw(x + Jeu.dominoWidth, y, Jeu.dominoWidth, Jeu.dominoWidth);
			graphics.setColor(Jeu.MESSAGE_COLOR);
			graphics.drawString(String.valueOf(Nbcouronne2), x + 3 * Jeu.dominoWidth / 2, y + Jeu.dominoWidth / 2);

		}
		
		public void render(Graphics graphics) throws SlickException {

			typeToImage(type1).draw(x, y, Jeu.dominoWidth, Jeu.dominoWidth);
			graphics.setColor(Jeu.MESSAGE_COLOR);
			graphics.drawString(String.valueOf(Nbcouronne1), x + Jeu.dominoWidth / 2, y + Jeu.dominoWidth / 2);

			switch (direction) {
			case D1:
				
				typeToImage(type2).draw(x + Jeu.dominoWidth, y, Jeu.dominoWidth, Jeu.dominoWidth);
				graphics.setColor(Jeu.MESSAGE_COLOR);
				graphics.drawString(String.valueOf(Nbcouronne2), x + 3 * Jeu.dominoWidth / 2, y + Jeu.dominoWidth / 2);
				break;

			case D2:
				typeToImage(type2).draw(x, y + Jeu.dominoWidth, Jeu.dominoWidth, Jeu.dominoWidth);
				graphics.setColor(Jeu.MESSAGE_COLOR);
				graphics.drawString(String.valueOf(Nbcouronne2), x + Jeu.dominoWidth / 2, y + 3 * Jeu.dominoWidth / 2);
				break;

			case D3:
				
				typeToImage(type2).draw(x - Jeu.dominoWidth, y, Jeu.dominoWidth, Jeu.dominoWidth);
				graphics.setColor(Jeu.MESSAGE_COLOR);
				graphics.drawString(String.valueOf(Nbcouronne2), x - Jeu.dominoWidth / 2, y + Jeu.dominoWidth / 2);
				break;

			case D4:
				
				typeToImage(type2).draw(x, y - Jeu.dominoWidth, Jeu.dominoWidth, Jeu.dominoWidth);
				graphics.setColor(Jeu.MESSAGE_COLOR);
				graphics.drawString(String.valueOf(Nbcouronne2), x + Jeu.dominoWidth / 2, y - Jeu.dominoWidth / 2);
				break;

			}
		}
		
		public static Color typeToColor(int type) {
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
			default:
				return null;

			}
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
				this.turnDominoGauche();
			}
			if (input.isKeyPressed(TURN_RIGHT) || input.isMousePressed(input.MOUSE_RIGHT_BUTTON)) {
				this.turnDminoDroite();
			}
		}
		
		public static void main(String[] args) throws FileNotFoundException {

		}
}
