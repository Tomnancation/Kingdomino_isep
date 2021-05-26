import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;

public class Jeu extends BasicGame {

	public Jeu(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	public static int width, height, dominoWidth;

	public static int NumeroJoueur;
	public static int NumeroRoi;
	public static int tour = 1;
	public static List<Joueur> joueurList = new ArrayList<>();

	
	//on configure les touches haut bas gauche droite du clavier
	public final static int HAUT = Input.KEY_UP;
	public final static int BAS = Input.KEY_DOWN;
	public final static int GAUCHE = Input.KEY_LEFT;
	public final static int DROITE = Input.KEY_RIGHT;
	public final static int CONFIRM = Input.KEY_ENTER;
	
	public final static int MAIN_SCREEN = 0;
	public static final int INPUT_Joueur_NUMBER = 1;
	public final static int CREAT_JoueurS = 2;
	public final static int SHUFFLE_RoiS = 3;
	public final static int DRAW_DOMINOS = 4;
	public final static int CHOSE_DOMINO = 5;
	public final static int PLACE_DOMINO = 6;
	public final static int GAME_OVER = 100;
	
	public final static Color MESSAGE_COLOR = Color.white;
	public final static Color ALTERNATIVE_MESSAGE = Color.black;
	public final static Color WARNING_COLOR = Color.red;
	
	static List<Roi> roiList = new ArrayList<Roi>();
	
	static TextField textField;
	static Domino displayedDomino;
	static String displayedString1 = "";
	static String displayedString2 = "";
	
	static int createdJoueur = 0;
	static int RoiChoseDomino = 0;
	static int playedRoi = 0;
	static int totaltour;


	static boolean kingsShuffled = false;
	static boolean tempRoiToDominoInitialised = false;

	
	public static Image CHAMPS_IMAGE;
	public static Image FORET_IMAGE;
	public static Image MER_IMAGE;
	public static Image PRAIRIE_IMAGE;
	public static Image MINE_IMAGE;
	public static Image MONTAGNE_IMAGE;
	public static Image CHATEAU_IMAGE;
	public static Image EMPTY_IMAGE;
	
	public static List<Domino> dominoList = new ArrayList<>();;
	static List<Domino> dominoListDraw = new ArrayList<>();;
	static List<Domino> dominoTempList;
	static List<Roi> RoiList = new ArrayList<Roi>();
	static Map<Roi, Domino> RoiToDomino = new LinkedHashMap<Roi, Domino>();
	static Map<Roi, Domino> tempRoiToDomino = new LinkedHashMap<Roi, Domino>();
	
	static boolean RoisShuffled = false;
	static boolean tempKingToDominoInitialised = false;
	static boolean scoreCalculated = false;
	static boolean lasttourFlag = false;
	
	public static int GAME_PHASE = 0;
	public static Joueur currentJoueur;
	public static Roi currentRoi;
	public static Domino currentDomino;
	
	// Valid check
		public static boolean NumeroJoueurValid(int JoueurN) {
			return 2 <= JoueurN && JoueurN <= 4;
		}
		
		public static List dupliqueDominoList(List l) {
			List<Domino> dominoTempList = new ArrayList<Domino>();
			Iterator<Domino> iter = l.iterator();
			while (iter.hasNext()) {
				dominoTempList.add(iter.next());
			}
			return dominoTempList;
		}
		
		public static void tour() {

			dominoListDraw = drawDominos();
			printDominoList(dominoListDraw);
			System.out.println("___________________________________________________");
			dominoListDraw = sortDominoByNum(dominoListDraw);
			printDominoList(dominoListDraw);
			System.out.println("___________________________________________________");
			List dominoTempList = dupliqueDominoList(dominoListDraw);
			printDominoList(dominoTempList);
			if (tour == 1) {
				Collections.shuffle(kingList);
			}
			System.out.println("___________________________________________________");
			System.out.println("L'ordre de ce tour : ");
			printKingList(kingList);
			System.out.println("___________________________________________________");

			System.out.println("___________________________________________________");
			Map tempKingToDomino = new LinkedHashMap<King, Domino>();

			for (King k : kingList) {
				Player p = getPlayerByKing(k);
				// printPlayerInfo(p);
				if (tour != 1) {
					// place domino
					int x, y;
					Domino domino = kingToDomino.get(k);
					do {
						domino.turnDomino();
						x = inputPosition() - 1;
						y = inputPosition() - 1;
					} while (!p.isPlaceOk(domino, x, y) && !p.isLandOccupied(domino, x, y));
					p.placeDomino(domino, x, y);
				}
				tempKingToDomino.put(k, k.draw());
			}

			kingToDomino = tempKingToDomino;

			System.out.println("___________________________________________________");
			printKingToDomino();
			System.out.println("___________________________________________________");
			printDominoList(dominoTempList);
			kingList = configKingListForNextTurn(dominoTempList);
			printKingList(kingList);

			// dominoTempList.clear();
			tour++;
		}
		
		public static void printRoiList(List<Roi> l) {
			Iterator<Roi> iterator = l.iterator();
			while (iterator.hasNext()) {
				printRoiInfo(iterator.next());
			}
			System.out.println("Nombre de rois : " + l.size());
		}
		public static void printRoiInfo(Roi k) {
			System.out.println("-------------------");
			System.out.println(k.getColor() + " king");
			System.out.println("king id : " + k.getId());
			System.out.println("-------------------");
		}

	public static List loadDominos(String filePath) {
		List dominoList = new ArrayList<Domino>();   // On créer une liste de dominos
		Scanner scanner;                             // pour avoir accès au clavier
		int NbCouronne1, type1, NbCouronne2, type2, NumDomino;
		boolean loadSuccessful = false;
		do {
			try {
				File file = new File(filePath);
				scanner = new Scanner(file);
				scanner.useDelimiter(",|\n|\r");

				for (int i = 0; i <= 4; i++) { // header
					scanner.next();
				}

				// load dominos in dominoList
				for (int i = 0; i < 48; i++) {
					System.out.println(scanner.next());

					NbCouronne1 = Integer.parseInt(scanner.next());
					type1 = Domino.typeToInt(scanner.next());
					NbCouronne2 = Integer.parseInt(scanner.next());
					type2 = Domino.typeToInt(scanner.next());
					NumDomino = Integer.parseInt(scanner.next());

					Domino d = new Domino(NbCouronne1, type1, NbCouronne2, type2, NumDomino);

					if (d.dominoValid())
						dominoList.add(d);

				}
				loadSuccessful = true;
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Erreur lors du chargement du fichier dominos.csv");
				System.out.println("Verifiez que le fichier est bien a l'enplacement : " + filePath);
				System.out.println("Ou saisissez l'emplacement manuelment :");
				Scanner scan = new Scanner(System.in);
				String path = scan.nextLine();
				filePath = path.isEmpty() ? filePath : path;
			}
		} while (!loadSuccessful);
		return dominoList;
	}
	public static Joueur getJoueurByRoi(Roi k) {
		for (Joueur p : joueurList) {
			if (p.getRois().contains(k)) {
				return p;
			}
		}
		return null;
	}
	public static void printJoueurInfo(Joueur p) {
		System.out.println("Joueur No." + p.getId());
		System.out.println("Joueur name : " + p.getJoueurName());
		System.out.println("Roi number : " + p.getKingNum());
		System.out.println("Roi color : " + p.getRoicolor());
		printRoiList(p.getRois());
		p.printLand();
		System.out.println();
	}


	
	// methode qui va permettre d'importer toutes nos images 
	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		width = gameContainer.getWidth();    // on importe la méthode largeur
		height = gameContainer.getHeight();  // on importe la methode longueur
		dominoWidth = (int) (width * 0.05);
		dominoList = loadDominos("dominos.csv"); // dominos.csv
		Collections.shuffle(dominoList);     // on mélange la liste des domino pour pouvoir les tirer aléatoirement
		//printDominoList(dominoList);
		
		// chargement des images
		Roi.roi = new Image("roi.png");

		Domino.couronne = new Image("Crown2.png");

		CHAMPS_IMAGE = new Image("champs.png");
		FORET_IMAGE = new Image("foret.png");
		MER_IMAGE = new Image("mer.png");
		MINE_IMAGE = new Image("mines.png");
		MONTAGNE_IMAGE = new Image("montagne.png");
		PRAIRIE_IMAGE = new Image("prairie.png");
		CHATEAU_IMAGE = new Image("chateau.jpg");
		EMPTY_IMAGE = new Image("empty.jpg");
		Image backgtour; 
		Image mainScreen; 
		Image panel;

		backgtour = new Image("board2.png");
		panel = new Image("yangpizhi.png");
		mainScreen = new Image("main.jpg");

	}
	@Override
	public void update(GameContainer gameContainer, int delta) throws SlickException {
		// System.out.println("current phase" + GAME_PHASE);
		Input input = gameContainer.getInput();

		float x, y;
		int i1, i2;
		switch (GAME_PHASE) {

		case MAIN_SCREEN:
			if (input.isKeyPressed(CONFIRM)) {
				GAME_PHASE++;
			}
			break;

		case INPUT_Joueur_NUMBER:
			displayedString1 = "Saisissez le nombre de joueur (chiffre entier 2 a 4) : ";
			int n = 0;
			if (input.isKeyPressed(CONFIRM)) {
				try {
					n = Integer.parseInt(textField.getText());
				} catch (Exception e) {
					displayedString2 = "Erreur : Veuillez entrer un nombre entier entre 2 et 4.";
					textField.setText("");
				}

				if (!NumeroJoueurValid(n)) {
					displayedString2 = "Erreur : Veuillez entrer un nombre entier entre 2 et 4.";
					textField.setText("");
				} else {
					NumeroJoueur = n;
					NumeroRoi = (NumeroJoueur == 2 || NumeroJoueur == 4) ? 4 : 3;
					textField.setText("");
					totaltour = (NumeroJoueur == 2) ? 6 : 12;
					GAME_PHASE++;
				}
			}
			break;

		case CREAT_JoueurS:

			displayedString1 = "";
			displayedString2 = "";

			displayedString1 = "Nombre de joueur : " + String.valueOf(NumeroJoueur);

			ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("red", "yellow", "green", "blue"));
			int RoiPerJoueur = NumeroJoueur == 2 ? 2 : 1;

			if (createdJoueur < NumeroJoueur) {

				displayedString2 = "Saisissez le nom du joueur No." + String.valueOf(createdJoueur + 1);
				String name = "";
				if (input.isKeyPressed(input.KEY_ENTER)) {
					name = textField.getText();
					name = name.isEmpty() ? "Joueur" + Integer.toString(createdJoueur + 1) : name;
					Joueur p;
					// = new Joueur(name, listColor.get(createdJoueur), RoiPerJoueur);

					if (name.equals("AI")) {
						p = new AI(name, listColor.get(createdJoueur), RoiPerJoueur);
					} else {
						p = new Joueur(name, listColor.get(createdJoueur), RoiPerJoueur);
					}
					for (int j = 0; j < RoiPerJoueur; j++) {
						Roi k = new Roi(listColor.get(createdJoueur));
						RoiList.add(k);
						p.getRois().add(k);
					}
					joueurList.add(p);
					createdJoueur++;
					System.out.println(joueurList);
					System.out.println(RoiList);
					textField.setText("");

				}

			}

			else {
				displayedString1 = "";
				displayedString2 = "";
				dominoList = configDominoList();

				GAME_PHASE = tour == 1 ? SHUFFLE_RoiS : DRAW_DOMINOS;
			}
			break;

		case SHUFFLE_RoiS:

			if (!RoisShuffled) {
				Collections.shuffle(RoiList);
				if (input.isKeyPressed(CONFIRM)) {
					RoisShuffled = true;

				}
			}

			if (input.isKeyPressed(CONFIRM)) {
				GAME_PHASE = DRAW_DOMINOS;
				// RoisShuffled = false;
				displayedString1 = "";
				displayedString2 = "";
			}
			break;

		case DRAW_DOMINOS:

			dominoListDraw = drawDominos();
			dominoListDraw = sortDominoByNum(dominoListDraw);
			dominoTempList = dupliqueDominoList(dominoListDraw);
			GAME_PHASE = CHOSE_DOMINO;
			System.out.println(dominoListDraw);
			displayedString1 = "";
			displayedString2 = "";

			break;

		case CHOSE_DOMINO:

			x = width * 0.125f;
			y = height * 0.3f;

			if (!tempRoiToDominoInitialised) {

				tempRoiToDominoInitialised = true;
				System.out.println(tempRoiToDomino);
			}

			if (RoiChoseDomino < RoiList.size()) {
				currentRoi = RoiList.get(RoiChoseDomino);
				currentJoueur = getJoueurByRoi(currentRoi);

			}
			// currentRoi.choseDomino(input);
			if (currentJoueur.joueurType.equals("Person")) {
				if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
					int posX = input.getMouseX();
					int posY = input.getMouseY();

					// System.out.println(x + " " + y);
					// System.out.println(x * 3 * Game.dominoWidth + " " + (x + Game.dominoWidth *
					// (2 + 3)));
					// System.out.println(x * 3 * Game.dominoWidth + " " + (x + Game.dominoWidth *
					// (2 + 3 * 2)));
					// System.out.println();
					// System.out.println(posX + " " + posY);

					for (int i4 = 0; i4 < dominoListDraw.size(); i4++) {
						if ((x + i4 * 3 * dominoWidth < posX) && (posX < (x + dominoWidth * (2 + 3 * i4)))
								&& ((y < posY) && (posY < (y + Jeu.dominoWidth * 2)))) {
							Domino d = dominoListDraw.get(i4);
							dominoListDraw.remove(d);
							tempRoiToDomino.put(currentRoi, d);
							RoiChoseDomino++;
							System.out.println(RoiChoseDomino);
							System.out.println(currentRoi.getId());

						}
					}

				}
			} else {
				if (!dominoListDraw.isEmpty()) {
					if (Jeu.tour == 1) {

						int index = currentJoueur.chooseBestDominoTurn1(dominoListDraw);
						Domino d = dominoListDraw.get(index);
						tempRoiToDomino.put(currentRoi, d);
						dominoListDraw.remove(index);
						RoiChoseDomino++;

					} else {

						Domino pre = RoiToDomino.get(currentRoi);
						int bestX = currentJoueur.bestPosition(pre)[1];
						int bestY = currentJoueur.bestPosition(pre)[2];
						int bestDirection = currentJoueur.bestPosition(pre)[3];
						int index = currentJoueur.chooseBestDomino(pre, bestX, bestY, bestDirection, dominoListDraw);
						Domino d = dominoListDraw.get(index);
						tempRoiToDomino.put(currentRoi, d);
						dominoListDraw.remove(d);
						RoiChoseDomino++;

					}
				}
			}

			if (RoiChoseDomino >= NumeroRoi && input.isKeyPressed(CONFIRM)) {
				RoiChoseDomino = 0;
				tempRoiToDominoInitialised = false;
				displayedString1 = "";
				displayedString2 = "";
				if (tour == 1) {
					GAME_PHASE = DRAW_DOMINOS;
					RoiToDomino = dupliqueTempRoiToDomino();
					RoiList = configRoiListForNextTurn(dominoTempList);
					tour++;

				} else {
					GAME_PHASE = PLACE_DOMINO;
					printRoiToDomino();

				}

			}

			break;

		case PLACE_DOMINO:

			if (playedRoi < RoiList.size()) {
				currentRoi = RoiList.get(playedRoi);
				currentJoueur = getJoueurByRoi(currentRoi);
				currentDomino = RoiToDomino.get(currentRoi);
				currentDomino.update(input);
			}

			if (playedRoi < NumeroRoi) {
				if (currentJoueur.joueurType.equals("Person")) {

					if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
						int posX = input.getMouseX();
						int posY = input.getMouseY();

						float x1 = Joueur.RENDER_START_PT_X;
						float y1 = Joueur.RENDER_START_PT_Y;

						boolean putOk = false;
						for (int i = 0; i < Joueur.LAND_DIMENSION; i++) {
							for (int j = 0; j < Joueur.LAND_DIMENSION; j++) {
								float xij = x1 + j * Jeu.dominoWidth;
								float yij = y1 + i * Jeu.dominoWidth;
								if ((xij < posX && posX < xij + dominoWidth) && (yij < posY && posY < yij + dominoWidth)
										&& (!currentJoueur.isLandOccupied(currentDomino, i, j)
												&& currentJoueur.isPlaceOk(currentDomino, i, j))) {
									currentJoueur.placeDomino(currentDomino, i, j);
									if (!currentJoueur.isDimensionOk()) {
										currentJoueur.removeDominoFromLand(currentDomino, i, j);
										System.out.println("Dimension out of bounds !");
										displayedString2 = "La dimension limite est 5 X 5";
									} else if (currentJoueur.isDimensionOk()) {
										putOk = true;
										currentDomino = null;
										currentJoueur.printLand();
										printJoueurInfo(currentJoueur);
										currentJoueur.calculateScore();
									}

								}

							}
						}
						if (putOk || !currentJoueur.detectPositions(currentDomino)) {

							playedRoi++;
						}

					}
				} else {
					if (currentDomino != null) {
						currentJoueur.place(currentDomino);
						currentDomino = null;
						playedRoi++;
					}

				}
			}

			if (playedRoi >= NumeroRoi && input.isKeyPressed(CONFIRM)) {

				if (tour < totaltour) {
					GAME_PHASE = DRAW_DOMINOS;
				} else if (tour == totaltour) {
					GAME_PHASE = PLACE_DOMINO;
				} else {
					GAME_PHASE = GAME_OVER;

				}
				displayedString1 = "";
				displayedString2 = "";

				playedRoi = 0;
				currentJoueur = null;
				currentDomino = null;
				RoiToDomino = dupliqueTempRoiToDomino();
				for (Joueur p : JoueurList) {
					printJoueurInfo(p);
				}
				RoiList = configRoiListForNextTurn(dominoTempList);
				dominoTempList.clear();
				tour++;
				if (dominoList.isEmpty()) {
					lasttourFlag = true;

				}

			}

			break;

		case GAME_OVER:

			if (!scoreCalculated) {
				finalScore();
			}
			displayedString1 = "GAME OVER   THE WINNER IS : " + winner.getJoueurName();
			displayedString2 = "SCORE : " + winnerScore;

			break;

		}

		// mousePressedPosition(input);

	}
	
	public static void printDominoList(List l) {
		int i = 0;
		Iterator<Domino> iterator = l.iterator();
		while (iterator.hasNext()) {
			System.out.println("No." + i);
			printDominoInfo2(iterator.next());
			i++;
		}
		System.out.println("logueuer de la list : " + l.size());
	}
	
	
	// affichage des valeurs du tableau csv
	public static void printDominoInfo2(Domino d) {
		System.out.println("Domino No." + d.getNumDomino());
		System.out.println("Type 1 : " + Domino.typeToString(d.getType1()));
		System.out.println("crown number 1 : " + d.getNbcouronne1());
		System.out.println("Type 2 : " + Domino.typeToString(d.getType2()));
		System.out.println("crown number 2 : " + d.getNbcouronne2());
		System.out.println();
	}
	
	
	// initialisation de la liste, nombre de domino présents dans la liste
	public static List configDominoList() {
		List<Domino> tempList = new ArrayList<Domino>();
		tempList = dominoList.subList(0, NumeroJoueur * 12);
		return tempList;
	}

	// Pioche
	public static List<Domino> drawDominos() {
		List<Domino> tempList = new ArrayList<Domino>();
		for (int i = 0; i < NumeroRoi; i++) {
			tempList.add(dominoList.get(0));
			dominoList.remove(0);
		}
		// tempList = dominoList.subList(0, NumeroRoi);
		return tempList;
	}
	
	public static Domino dominoNumMin(List<Domino> liste) {
		Domino min = liste.get(0);
		for (int i = 0; i < liste.size(); i++)
			if (liste.get(i).getNumDomino() <= min.getNumDomino()) {
				min = liste.get(i);
			}
		return min;
	}
	
	//Determiner le plus petit domino
	public static List<Domino> sortDominoByNum(List l) {
		List<Domino> tempList = new ArrayList<Domino>();
		do {
			tempList.add(dominoNumMin(l));
			l.remove(dominoNumMin(l));
		} while (l.size() != 0);
		return tempList;
	}
	
	// on place les domino par ordre croissant 
	
	public static List configJoueurOrder() {
		List tempList = new ArrayList<Joueur>();
		for (Roi k : RoiList) {
			for (Joueur p : joueurList) {
				if (k.getCouleur() == p.getCouleurRoi()) {
					tempList.add(p.getId());
				}
			}
		}
		return tempList;
	}
	

	
	
}


	

