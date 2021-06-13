import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Joueur {
	String NomJoueur;
	int id;
	String CouleurRoi;
	Royaume[][] land;
	String joueurType;
	int NumeroRoi;
	int score;
	private List<Roi> rois = new ArrayList<>();
	int[][][] scoreList;
	
	public static final int LAND_DIMENSION = 9;
	public static final int MAX_DIMENSION = 5;
	
	public static final float RENDER_START_PT_X = Jeu.height * 0.15f;
	public static final float RENDER_START_PT_Y = Jeu.height * 0.1f;
	
	static int Joueur_ID = 1;
	// print in csv
	
	int finalScore;
	boolean chateauCenter ;
	int totalCrownNum = 0;
	int emptyAreaNum = 0;
	int singleEmptyAreaNum = 0;
	
	public Joueur() {

	}

	
	public Joueur(String name, String CouleurRoi, int NumeroRoi) {
		// TODO Auto-generated constructor stub
		this.NomJoueur = name;
		this.id = Joueur_ID;
		this.land = intialiseLand();
		this.CouleurRoi = CouleurRoi;
		this.NumeroRoi = NumeroRoi;
		this.joueurType = "Person";
		Joueur_ID++;
	}
	
	
	//Getters
	public String getNomJoueur() {
		return NomJoueur;
	}
	public int getId() {
		return id;
	}
	public String getCouleurRoi() {
		return CouleurRoi;
	}
	public Royaume[][] getLand() {
		return land;
	}
	public int getNumeroRoi() {
		return NumeroRoi;
	}
	
	public List<Roi> getRois() {
		return rois;
	}
	
	public String getJoueurType() {
		return joueurType;
	}

	public void setJoueurType(String joueurType) {
		this.joueurType = joueurType;
	}

	
	public void setName(String name) {
		NomJoueur = name;
	}
	// On initialise le royaume de chaque joueur
	public static Royaume[][] intialiseLand() { 
		Royaume[][] royaume = new Royaume[LAND_DIMENSION][LAND_DIMENSION]; // on créer un royaume
		for (int i = 0; i != royaume.length; i++) {
			for (int j = 0; j != royaume[i].length; j++) {
				royaume[i][j] = (i == LAND_DIMENSION / 2 && j == LAND_DIMENSION / 2) ? new Royaume(Royaume.CHATEAU) : new Royaume();
			} // on attribue des coordonées a chaque case du royaume
		}
		return royaume;
	}
	// on attribue a chaque couleur de roi un Id
	public static int colorToId(String kingColor) {
		switch (kingColor) {
		case "red":
			return 1;
		case "yellow":
			return 3;
		case "blue":
			return 5;
		case "pink":
			return 7;
		default:
			return -1;
		}
	}
	
	// méthode que l'on va utiliser pour mettre a jour le royaume apres chaque changement
	public void printLand() {
		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {
				System.out.print(Royaume.typeToString(land[i][j].getType()) + " ");
			}
			System.out.println();
		}
	}

	// Avant de Placer un Domino on doit verifier que l'emplacement est libre
	public boolean isLandOccupied(Domino d, int x, int y) {
		switch (d.getDirection()) {
		case 1:
			// if : detect if out of border
			if (y < 8) {
				return (this.land[x][y].estOccupe() || this.land[x][y + 1].estOccupe());
			}
			return true;
		case 2:
			if (x < 8) {
				return (this.land[x][y].estOccupe() || this.land[x + 1][y].estOccupe());
			}
			return true;
		case 3:
			if (y > 0) {
				return (this.land[x][y].estOccupe() || this.land[x][y - 1].estOccupe());
			}
			return true;
		case 4:
			if (x > 0) {
				return (this.land[x][y].estOccupe() || this.land[x - 1][y].estOccupe());
			}
			return true;
		default:
			return true;
		}
	}
	
	public boolean isPlaceOk(Domino domino, int x, int y) { //methode pour placer le dominer selon la case ou le joueur va cliquer

		int type1 = domino.getType1();
		int type2 = domino.getType2();
		boolean piece1up = false;
		boolean piece1down = false;
		boolean piece1left = false;
		boolean piece1right = false;

		boolean piece2up = false;
		boolean piece2down = false;
		boolean piece2left = false;
		boolean piece2right = false;

		switch (domino.getDirection()) {
		// if in the corner, only detect some sides to avoid out of bounds
		case 1:
			if (x > 0) {
				piece1up = (land[x - 1][y].getType() == type1) || (land[x - 1][y].getType() == Royaume.CHATEAU);
				piece2up = (land[x - 1][y + 1].getType() == type2) || (land[x - 1][y + 1].getType() == Royaume.CHATEAU);
			}
			if (x < 8) {
				piece1down = (land[x + 1][y].getType() == type1) || (land[x + 1][y].getType() == Royaume.CHATEAU);
				piece2down = (land[x + 1][y + 1].getType() == type2) || (land[x + 1][y + 1].getType() == Royaume.CHATEAU);
			}
			if (y > 0) {
				piece1left = (land[x][y - 1].getType() == type1) || (land[x][y - 1].getType() == Royaume.CHATEAU);
			}
			if (y < 7) {
				piece2right = (land[x][y + 2].getType() == type2) || (land[x][y + 2].getType() == Royaume.CHATEAU);
			}
			return (piece1up || piece1down || piece1left || piece2up || piece2down || piece2right);

		case 2:
			if (x > 0) {
				piece1up = (land[x - 1][y].getType() == type1) || (land[x - 1][y].getType() == Royaume.CHATEAU);
			}
			if (y > 0) {
				piece1left = (land[x][y - 1].getType() == type1) || (land[x][y - 1].getType() == Royaume.CHATEAU);
				piece2left = (land[x + 1][y - 1].getType() == type2) || (land[x + 1][y - 1].getType() == Royaume.CHATEAU);
			}
			if (y < 8) {
				piece1right = (land[x][y + 1].getType() == type1) || (land[x][y + 1].getType() == Royaume.CHATEAU);
				piece2right = (land[x + 1][y + 1].getType() == type2) || (land[x + 1][y + 1].getType() == Royaume.CHATEAU);
			}
			if (x < 7) {
				piece2down = (land[x + 2][y].getType() == type2) || (land[x + 2][y].getType() == Royaume.CHATEAU);
			}
			return (piece1up || piece1left || piece1right || piece2down || piece2left || piece2right);
		case 3:
			if (x > 0) {
				piece1up = (land[x - 1][y].getType() == type1) || (land[x - 1][y].getType() == Royaume.CHATEAU);
				piece2up = (land[x - 1][y - 1].getType() == type2) || (land[x - 1][y - 1].getType() == Royaume.CHATEAU);
			}
			if (x < 8) {
				piece1down = (land[x + 1][y].getType() == type1) || (land[x + 1][y].getType() == Royaume.CHATEAU);
				piece2down = (land[x + 1][y - 1].getType() == type2) || (land[x + 1][y - 1].getType() == Royaume.CHATEAU);
			}
			if (y < 8) {
				piece1right = (land[x][y + 1].getType() == type1) || (land[x][y + 1].getType() == Royaume.CHATEAU);
			}
			if (y > 1) {
				piece2left = (land[x][y - 2].getType() == type2) || (land[x][y - 2].getType() == Royaume.CHATEAU);
			}
			return (piece1up || piece1down || piece1right || piece2up || piece2down || piece2left);

		case 4:
			if (x < 8) {
				piece1down = (land[x + 1][y].getType() == type1) || (land[x + 1][y].getType() == Royaume.CHATEAU);
			}
			if (y > 0) {
				piece1left = (land[x][y - 1].getType() == type1) || (land[x][y - 1].getType() == Royaume.CHATEAU);
				piece2left = (land[x - 1][y - 1].getType() == type2) || (land[x - 1][y - 1].getType() == Royaume.CHATEAU);
			}
			if (y < 8) {
				piece1right = (land[x][y + 1].getType() == type1) || (land[x][y + 1].getType() == Royaume.CHATEAU);
				piece2right = (land[x - 1][y + 1].getType() == type2) || (land[x - 1][y + 1].getType() == Royaume.CHATEAU);
			}
			if (x > 1) {
				piece2up = (land[x - 2][y].getType() == type2) || (land[x - 2][y].getType() == Royaume.CHATEAU);
			}
			return (piece1down || piece1left || piece1right || piece2up || piece2left || piece2right);
		}
		return true;
	}
	
	public boolean isDimensionOk() { //methode pour verifier qu'apres placement d'un domino les dimensions du royaume sont respectées ( maximum 5x5)
		// for rows and columns
		int rowDimension = 0;
		int columnDimension = 0;
		int xDimension, yDimension;
		for (int i = 0; i != land.length; i++) {
			xDimension = 0;
			yDimension = 0;
			for (int j = 0; j != land[i].length; j++) {
				if (land[i][j].estOccupe()) {
					xDimension = 1;
				}
				if (land[j][i].estOccupe()) {
					yDimension = 1;
				}
				if (j == land[i].length - 1) {
					rowDimension += xDimension;
					columnDimension += yDimension;
				}
			}
		}
		if (rowDimension > 5 || columnDimension > 5) { // si les colones ou les lignes depasse 5 alors la fonction renvoie false
			return false;
		}
		return true;
	}

	public void removeDominoFromLand(Domino domino, int x, int y) { // Si lorsque l'on place le domino les dimensions du royaume depasse 5x5 on utilisera cette methode pour suprimer le domoni
		switch (domino.getDirection()) {
		case Domino.D1:
			this.land[x][y] = new Royaume();
			this.land[x][y + 1] = new Royaume();
			break;
		case Domino.D2:
			this.land[x][y] = new Royaume();
			this.land[x + 1][y] = new Royaume();
			break;
		case Domino.D3:
			this.land[x][y] = new Royaume();
			this.land[x][y - 1] = new Royaume();
			break;
		case Domino.D4:
			this.land[x][y] = new Royaume();
			this.land[x - 1][y] = new Royaume();
			break;
		default:

			break;
		}
	}
	
	public void placeDomino(Domino domino, int x, int y) { //méthode pour placer les domino
		int type1 = domino.getType1();
		int nbCouronne1 = domino.getNbcouronne1();
		int type2 = domino.getType2();
		int nbcouronne2 = domino.getNbcouronne2();
		switch (domino.getDirection()) {
		case Domino.D1:
			this.land[x][y].setType(type1);
			this.land[x][y].setNbCouronne(nbCouronne1);
			this.land[x][y].setStat(true);

			this.land[x][y + 1].setType(type2);
			this.land[x][y + 1].setNbCouronne(nbcouronne2);
			this.land[x][y + 1].setStat(true);
			break;
		case Domino.D2:
			this.land[x][y].setType(type1);
			this.land[x][y].setNbCouronne(nbCouronne1);
			this.land[x][y].setStat(true);

			this.land[x + 1][y].setType(type2);
			this.land[x + 1][y].setNbCouronne(nbcouronne2);
			this.land[x + 1][y].setStat(true);
			break;
		case Domino.D3:
			this.land[x][y].setType(type1);
			this.land[x][y].setNbCouronne(nbCouronne1);
			this.land[x][y].setStat(true);

			this.land[x][y - 1].setType(type2);
			this.land[x][y - 1].setNbCouronne(nbcouronne2);
			this.land[x][y - 1].setStat(true);
			break;
		case Domino.D4:
			this.land[x][y].setType(type1);
			this.land[x][y].setNbCouronne(nbCouronne1);
			this.land[x][y].setStat(true);

			this.land[x - 1][y].setType(type2);
			this.land[x - 1][y].setNbCouronne(nbcouronne2);
			this.land[x - 1][y].setStat(true);
			break;
		default:

			break;
		}
	}
	/**
	 * 1.[9][9][4]的[4]是什么？
	 * 2.为什么scoreList一开始等于-1和scorelist代表了什么
	 * 3.setDirection，getNumDomino跟Domino有关
	 * 4.isLandOccupied，isPlaceOk，placeDomino，isDimensionOk，removeDominoFromLand跟joueur有关系
	 * 5。
	 * */
	
	public boolean detectPositions(Domino domino) {
		int availablePositions = 0;
		scoreList = new int[9][9][4];
		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				for (int k = 0; k < 4; k++) { // for direction 1 to 4
					scoreList[i][j][k] = -1; // initialisation
					domino.setDirection(k + 1);
					if (!isLandOccupied(domino, i, j) && isPlaceOk(domino, i, j)) {
						placeDomino(domino, i, j);
						if (isDimensionOk()) {
							availablePositions = availablePositions + 1;
							scoreList[i][j][k] = finalScore();
						}
						removeDominoFromLand(domino, i, j);
					}
				}
			}
		}
		if (availablePositions == 0) {
			System.out.println(
					"Pas de position disponible pour domino" + domino.getNumDomino() + "! Vous devez jeter ce domino");
			// discard domino code
			return false;
		} else {
			return true;
		}

	}
	
	public class Location {  // classe pour creer et initialiser un point avec des coordonées x et y donées
		int x, y;

		public Location(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public int[] get1stDominoPostion() {
		int[] position = new int[2];
		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {
				if (land[i][j].estOccupe()) {
					position[1] = i;
					position[2] = j;
				}
			}
		}
		return position;
	}
	
	public Royaume[][] land5x5() {
		Royaume[][] tempArea = new Royaume[MAX_DIMENSION][MAX_DIMENSION];
		int[] position = get1stDominoPostion();
		for (int i = position[0]; i < MAX_DIMENSION; i++) {
			for (int j = position[1]; j < MAX_DIMENSION; j++) {
				tempArea[i - position[0]][j - position[0]] = land[i][j];
			}
		}
		return tempArea;
	}
	//如果detectPositions检测有位置就可以执行putDomino方法
	public void place(Domino domino) {
		if (detectPositions(domino)) {
			putDomino(domino);
		} else {
			System.out.println("Aucun poste disponible ! Domino rejeté");
		}
	}
	/*printLand,getId,calculateScore跟Joueur有关系
	 *
	 *
	 */
	public void putDomino(Domino domino) {
			int x = bestPosition(domino)[1];
			int y = bestPosition(domino)[2];
			int direction = bestPosition(domino)[3];

			domino.setDirection(direction);
			placeDomino(domino, x, y);
			printLand();
			System.out.println("Score actuel du joueur " + getId() + " est " + calculateScore());

		}

	
	public int calculateScore() {  //méthode de calcul du score a la fin de la partie
		score = 0;
		int[][] nbcouronne = new int[9][9];
		for (int i = 0; i < 9; i++) { // ligne
			for (int j = 0; j < 9; j++) { // colonne
				nbcouronne[i][j] = -1; // initialisation
			}
		}

		for (int i = 0; i < 9; i++) { // ligne
			for (int j = 0; j < 9; j++) { // colonne
				if (nbcouronne[i][j] == -1 && land[i][j].estOccupe()) {
					Location location = new Location(i, j);
					findRoyaume(location, nbcouronne);
				}
			}
		}

		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				if (nbcouronne[i][j] != -1)
					score += nbcouronne[i][j];
			}
		}
		return score;
	}
	
	public void render(Graphics graphics) {
		this.renderAreaInLand(graphics);
		this.renderInfo(graphics, Jeu.ALTERNATIVE_MESSAGE);
	}
	
	public void renderAreaInLand(Graphics graphics) { //affichage graphique du royaume
		float x = RENDER_START_PT_X;
		float y = RENDER_START_PT_Y;

		for (int i = 0; i < LAND_DIMENSION; i++) {
			for (int j = 0; j < LAND_DIMENSION; j++) {

				int xij = (int) x + j * Jeu.dominoWidth;
				int yij = (int) y + i * Jeu.dominoWidth;

				land[i][j].render(graphics, xij, yij);

			}
		}
	}
	
	public void renderInfo(Graphics graphics, Color color) {
		float x = Jeu.width * 0.75f;
		float y = Jeu.height * 0.2f;
		float d = Jeu.width * 0.015f;

		graphics.setColor(color);
		graphics.drawString("Player No." + id, x, y + d);
		graphics.drawString("Player name : " + NomJoueur, x, y + 2 * d);
		graphics.drawString("King color : " + CouleurRoi, x, y + 3 * d);
		renderRois(graphics, x, y + 10 * d);

	}
	
	public void renderLandMini(Graphics graphics, float x, float y) { //affichage graphique des royaume sur la minimap
		for (int i = 0; i < LAND_DIMENSION; i++) {
			for (int j = 0; j < LAND_DIMENSION; j++) {

				int xij = (int) (x + j * Jeu.dominoWidth * 0.2);
				int yij = (int) (y + i * Jeu.dominoWidth * 0.2);

				land[i][j].renderMini(graphics, xij, yij);

			}
		}
	}
	
	public void renderRois(Graphics graphics, float x, float y) { //affichage graphique des rois
		float d = Jeu.width * 0.1f;
		int i = 0;
		for (Roi k : rois) {
			k.render(graphics, x + i * d, y);
			i++;
		}
	}
	
	
	public void findRoyaume(Location location, int[][] crownNum) {
		Deque<Location> stackCrown = new LinkedList<Location>();
		stackCrown.push(location); // On met la location dans le stack
		int x = location.x;
		int y = location.y;
		int counter = land[x][y].getNbCouronne();
		crownNum[x][y] = -2;

		// Depth-First-Search
		while (!stackCrown.isEmpty()) {
			x = stackCrown.peek().x;
			y = stackCrown.peek().y;

			// up
			if (x > 0 && crownNum[x - 1][y] == -1 && land[x][y].getType() == land[x - 1][y].getType()) {
				crownNum[x - 1][y] = -2;
				stackCrown.push(new Location(x - 1, y));
				counter += land[x - 1][y].getNbCouronne();
				continue;
			}

			// right
			if (y < 8 && crownNum[x][y + 1] == -1 && land[x][y].getType() == land[x][y + 1].getType()) {
				crownNum[x][y + 1] = -2;
				stackCrown.push(new Location(x, y + 1));
				counter += land[x][y + 1].getNbCouronne();
				continue;
			}

			// down
			if (x < 8 && crownNum[x + 1][y] == -1 && land[x][y].getType() == land[x + 1][y].getType()) {
				crownNum[x + 1][y] = -2;
				stackCrown.push(new Location(x + 1, y));
				counter += land[x + 1][y].getNbCouronne();
				continue;
			}

			// right
			if (y > 0 && crownNum[x][y - 1] == -1 && land[x][y].getType() == land[x][y - 1].getType()) {
				crownNum[x][y - 1] = -2;
				stackCrown.push(new Location(x, y - 1));
				counter += land[x][y - 1].getNbCouronne();
				continue;
			}
			stackCrown.pop(); // remove the top location in the stack
		}

		for (int i = 0; i < 9; i++) { // rows
			for (int j = 0; j < 9; j++) { // columns
				if (crownNum[i][j] == -2)
					crownNum[i][j] = counter;
			}
		}
		// Location elem = stackCrown.peek();
	}
	public int finalScore() {
		int finalScore = calculateScore();
		int landNum = 0;
		// si le royaume est complet
		for (int i = 0; i < 9; i++) { // ligne
			for (int j = 0; j < 9; j++) { // colonne
				if (land[i][j].estOccupe()) {
					landNum++;
				}
			}
		}
		if (landNum == 25) {
			finalScore += 5;
			System.out.println("Land complet");
		}

		// if castle is in the middle
		int[] border = new int[2];
		boolean top = false;
		boolean left = false;

		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {
				if (land[i][j].estOccupe()) {
					border[0] = i; // top border
					top = true;
					break;
				}
			}
			if (top) { // lorsqu'on arrive a la limite en hauteur la boucle s'arrete
				break;
			}
		}
		for (int i = 0; i != land.length; i++) {
			for (int j = 0; j != land[i].length; j++) {

				if (land[j][i].estOccupe()) {
					border[1] = i; // left border
					left = true;
					break;
				}
			}
			if (left) {
				break;
			}
		}

		if (land[border[0] + 2][border[1] + 2].getType() == Royaume.CHATEAU) {
			finalScore += 10;
			System.out.println("Castle in the middle");
			this.chateauCenter = true;

		}

		this.finalScore = finalScore;

		verifieLand(border[0], border[1]);

		return finalScore;

	}
	public void verifieLand(int borderTop, int borderLeft) {
		//System.out.println((borderTop + MAX_DIMENSION) + " " + (borderLeft + MAX_DIMENSION));
		int tmpEmptyAreaNum = 0;
		int tmpSingleEmptyAreaNum = 0;
		
		for (int i = borderTop; i != borderTop + MAX_DIMENSION; i++) {
			for (int j = borderLeft; j != borderLeft + MAX_DIMENSION; j++) {
				if (!land[i][j].estOccupe()) {
					tmpEmptyAreaNum++;

					if (isEmptyAreaSingle(i, j, borderTop, borderLeft)) {
						tmpSingleEmptyAreaNum++;
						//System.out.println(i + " " + j);
					}

				} else {
					totalCrownNum += land[i][j].getNbCouronne();
				}
			}
		}
		emptyAreaNum = tmpEmptyAreaNum;
		singleEmptyAreaNum = tmpSingleEmptyAreaNum;
	}
	
	
	//Methode pour verifier si les alentours d'une case sont libres, on utilisera cette methode pour verifier si le joueur peut placer un domino
	public boolean isEmptyAreaSingle(int i, int j, int borderTop, int borderLeft) {
		if (i == borderTop && j == borderLeft) {
			return land[i + 1][j].estOccupe() && land[i][j + 1].estOccupe();
		} else if (i == borderTop && j == borderLeft + MAX_DIMENSION - 1) {
			return land[i][j - 1].estOccupe() && land[i + 1][j].estOccupe();
		} else if (i == borderTop + MAX_DIMENSION - 1 && j == borderLeft) {
			return land[i - 1][j].estOccupe() && land[i][j + 1].estOccupe();
		} else if (i == borderTop + MAX_DIMENSION - 1 && j == borderLeft + MAX_DIMENSION - 1) {
			return land[i - 1][j].estOccupe() && land[i][j - 1].estOccupe();
		} else if (i == borderTop) {
			return land[i][j - 1].estOccupe() && land[i + 1][j].estOccupe() && land[i][j + 1].estOccupe();
		} else if (j == borderLeft) {
			return land[i - 1][j].estOccupe() && land[i + 1][j].estOccupe() && land[i][j + 1].estOccupe();
		} else if (i == borderTop + MAX_DIMENSION - 1) {
			return land[i - 1][j].estOccupe() && land[i][j - 1].estOccupe() && land[i][j + 1].estOccupe();
		} else if (j == borderLeft + MAX_DIMENSION - 1) {
			return land[i - 1][j].estOccupe() && land[i][j - 1].estOccupe() && land[i + 1][j].estOccupe();
		} else {
			return land[i - 1][j].estOccupe() && land[i][j - 1].estOccupe() && land[i + 1][j].estOccupe()
					&& land[i][j + 1].estOccupe();
		}

	}
	/**
	 * 1.为什么bestPosition(domino)[0]
	 * 2.D1是什么
	 * 3.max是什么
	 * 4.index是什么*/
		// for turn 1
	public int chooseBestDominoTurn1(List<Domino> dominoList) {
		int max = bestPosition(dominoList.get(0))[0];
		int index = 0;
		for (Domino domino : dominoList) {
			if (bestPosition(domino)[0] > max){
				max = bestPosition(domino)[0];
				index = dominoList.indexOf(domino);
			}
			domino.setDirection(Domino.D1);
		}
		return index;
	}
	// other turns
	public int chooseBestDomino(Domino preDomino, int preX, int preY, int preDirection, List<Domino> dominoList) {
		preDomino.setDirection(preDirection);
		placeDomino(preDomino, preX, preY);
		int max = bestPosition(dominoList.get(0))[0];
		int index = 0;
		for (Domino domino : dominoList) {
			if (bestPosition(domino)[0] > max) {
				max = bestPosition(domino)[0];
				index = dominoList.indexOf(domino);
			}
			domino.setDirection(Domino.D1);
		}
		removeDominoFromLand(preDomino, preX, preY);
		preDomino.setDirection(Domino.D1);
		return index;
	}
	/*
	 * 1.returnArray是什么/returnArray的4个对象和index的3个对象有着什么关系
	 * 2.4个direction是什么
	 * 3.Math.abs(i - 4) + Math.abs(j - 4) < Math.abs(index[0] - 4) + Math.abs(index[1] - 4)*/
	public int[] bestPosition(Domino domino) {
		// detectPositions(domino);
					int[] returnArray = new int[4];
					if (detectPositions(domino)) {
						int max = scoreList[0][0][0];
						int[] index = new int[3];

						for (int i = 0; i < 9 ; i++) { // rows
							for (int j = 0; j < 9; j++) { // columns
								for (int k = 0; k < 4; k++) { // for direction 1 to 4
									if (scoreList[i][j][k] > max) {
										max = scoreList[i][j][k];
										index[0] = i;
										index[1] = j;
										index[2] = k;
									} else if (scoreList[i][j][k] == max) {
										// To maximise the chance where castle is in the middle
										if (Math.abs(i - 4) + Math.abs(j - 4) < Math.abs(index[0] - 4) + Math.abs(index[1] - 4)) {
											{
												max = scoreList[i][j][k];
												index[0] = i;
												index[1] = j;
												index[2] = k;
											}
										}
									}
								}
							}
						}
						returnArray[0] = max; // score
						returnArray[1] = index[0]; // x
						returnArray[2] = index[1]; // y
						returnArray[3] = index[2] + 1; // direction
					} else {
						returnArray[0] = -1; // score
						returnArray[1] = 0; // x
						returnArray[2] = 0; // y
						returnArray[3] = 1; // direction
					}
					return returnArray;
				}
	
	public void printResultInCsv() throws IOException {

		FileWriter fw = null;

		try {
			fw = new FileWriter(new File("results.csv"), true);
			fw.append(finalScore + "," + chateauCenter + "," + emptyAreaNum + "," + singleEmptyAreaNum + ","
					+ totalCrownNum + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			fw.close();
		}

	}
}
