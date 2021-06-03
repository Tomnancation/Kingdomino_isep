import java.awt.Font;
import java.io.*;
import java.util.*;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public  class Jeu extends BasicGame {

	public Jeu(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	private final static int ID = 1;
	public static int width, height, dominoWidth;

	public static int NumeroJoueur;
	public static int NumeroRoi;
	public static int tour = 1;
	public static List<Joueur> joueurList = new ArrayList<>();

	public static List<Integer> OrdreJoueur;
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
	public final static int Jeu_OVER = 100;
	
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
	
	static Joueur winner;
	static int winnerScore;
	TrueTypeFont font;
	
	static Map<Roi, Domino> kingToDomino = new LinkedHashMap<Roi, Domino>();
	static Map<Roi, Domino> tempKingToDomino = new LinkedHashMap<Roi, Domino>();


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
	
	public static int Jeu_PHASE = 0;
	public static Joueur currentJoueur;
	public static Roi currentRoi;
	public static Domino currentDomino;
	
	static float infoX, infoY, d;
	Image background;
	Image mainScreen;
	Image panel;

	// Getters
		public int getNumeroJoueur() {
			return NumeroJoueur;
		}

		public int getNumeroRoi() {
			return NumeroRoi;
		}
		// Valid check
				public static boolean NumeroJoueurValid(int JoueurN) {
					return 2 <= JoueurN && JoueurN <= 4;
				}
	
	public static boolean isPositionValid(int x) {
		return 1 <= x && x <= 9;
	}

	public static int inputInt() {
		int n = 0;
		Scanner scan = new Scanner(System.in);
		boolean valeurOK = false;

		do {
			try {
				n = scan.nextInt();
				scan.nextLine();
				valeurOK = true;
			} catch (Exception e) {
				System.out.println("Erreur : Veuillez saisir un nombre entier.");
				scan.nextLine();
			}
		} while (!valeurOK);

		return n;
	}
	
	public static int inputPosition() {
		int x = 0;
		do {
			x = inputInt();
			if (!isPositionValid(x)) {
				System.out.println("Saisissez un nombre entre 1 et 9");
			}
		} while (!isPositionValid(x));
		return x;

	}
	
	
		public static List dupliqueDominoList(List l) {
			List<Domino> dominoTempList = new ArrayList<Domino>();
			Iterator<Domino> iter = l.iterator();
			while (iter.hasNext()) {
				dominoTempList.add(iter.next());
			}
			return dominoTempList;
		}
		
		public static String inputString() {
			String str = null;
			boolean valeurOK = false;
			Scanner scan = new Scanner(System.in);
			do {
				try {
					str = scan.nextLine();
					valeurOK = true;
				} catch (Exception e) {
					System.out.println("Erreur : Veuillez entrer une chaine de caractere valide.");
					scan.nextLine();
				}
			} while (!valeurOK);
			return str;
		}
		
		public static int inputNumeroJoueur() {
			int n;
			// System.out.println("Saisissez le nombre de joueur (chiffre entier 2 a 4) :
			// ");
			displayedString1 = "Saisissez le nombre de joueur (chiffre entier 2 a 4) : ";
			do {
				n = inputInt();
				if (!NumeroJoueurValid(n)) {
					System.out.println("Erreur : Veuillez entrer un nombre entier entre 2 et 4.");
				}
			} while (!NumeroJoueurValid(n));
			return n;

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
				Collections.shuffle(RoiList);
			}
			System.out.println("___________________________________________________");
			System.out.println("L'ordre de ce tour : ");
			printRoiList(RoiList);
			System.out.println("___________________________________________________");

			System.out.println("___________________________________________________");
			Map tempKingToDomino = new LinkedHashMap<Roi, Domino>();

			for (Roi k : roiList) {
				Joueur p = getJoueurByRoi(k);
				// printPlayerInfo(p);
				if (tour != 1) {
					// place domino
					int x, y;
					Domino domino = RoiToDomino.get(k);
					do {
						domino.turnDomino();
						x = inputPosition() - 1;
						y = inputPosition() - 1;
					} while (!p.isPlaceOk(domino, x, y) && !p.isLandOccupied(domino, x, y));
					p.placeDomino(domino, x, y);
				}
				tempKingToDomino.put(k, k.draw());
			}

			RoiToDomino = tempKingToDomino;

			System.out.println("___________________________________________________");
			printRoiToDomino();
			System.out.println("___________________________________________________");
			printDominoList(dominoTempList);
			roiList = configRoiListForNextTurn(dominoTempList);
			printRoiList(roiList);

			// dominoTempList.clear();
			tour++;
		}
		
		public void gameOver() {
			if (dominoList.size() == 0)
				Jeu_PHASE = 100;
		}
		public static String inputNomJoueur(Input input) {
			String name = "";
			if (input.isKeyPressed(input.KEY_ENTER)) {
				name = textField.getText();
			}
			return name;
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
			System.out.println(k.getCouleur() + " king");
			System.out.println("king id : " + k.getId());
			System.out.println("-------------------");
		}
		
		

		public static List configRoiListForNextTurn(List dominoTempList) {
			List<Roi> tempKingList = new ArrayList<>();
			Iterator<Domino> iter = dominoTempList.iterator();
			while (iter.hasNext()) {
				Roi k = getRoiFromDomino(iter.next());
				tempKingList.add(k);
			}
			return tempKingList;

		}
		
		public static Roi getRoiFromDomino(Domino d) {
			List tempList = new ArrayList<>();
			Roi king = null;
			for (Roi k : RoiToDomino.keySet()) {
				if (RoiToDomino.get(k).equals(d)) {
					// tempList.add(k);
					king = k;
				}
			}
			return king;
		}
		
	public static List loadDominos(String filePath) {
		List dominoList = new ArrayList<Domino>();   // On cr�er une liste de dominos
		Scanner scanner;                             // pour avoir acc�s au clavier
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
		System.out.println("Joueur name : " + p.getNomJoueur());
		System.out.println("Roi number : " + p.getNumeroRoi());
		System.out.println("Roi color : " + p.getCouleurRoi());
		printRoiList(p.getRois());
		p.printLand();
		System.out.println();
	}
	public static void finalScore() {
		winner = joueurList.get(0);
		winner.calculateScore();
		winnerScore = winner.score;
		for (Joueur p : joueurList) {
			int score = p.finalScore();
			if (score > winnerScore) {
				winner = p;
				winnerScore = score;
			}
		}
		System.out.println("The winner is : " + winner.getNomJoueur());
		scoreCalculated = true;
	}
	
	public static void mousePressedPosition(Input input) {
		if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
			int posX = input.getMouseX();
			int posY = input.getMouseY();

			System.out.println(posX + " " + posY);
			System.out.println(width + " " + height);
			System.out.println();
		}
	}
	public static void creatJoueurs() {
		ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("red", "yellow", "green", "pink"));
		// Collections.shuffle(listColor);
		// System.out.println(listColor);
		int roiPerJoueur = NumeroJoueur == 2 ? 2 : 1;
		System.out.println("Nombre de joueur : " + NumeroJoueur);
		for (int i = 1; i <= NumeroJoueur; i++) {
			System.out.println("Saisissez le nom du joueur No." + i);
			String name = inputString();
			name = name.isEmpty() ? "Player" + Integer.toString(i) : name;
			Joueur p = new Joueur(name, listColor.get(i - 1), roiPerJoueur);
			// p.setKings(NumeroJoueur);
			for (int j = 0; j < roiPerJoueur; j++) {
				Roi k = new Roi(listColor.get(i - 1));
				roiList.add(k);
				p.getRois().add(k);
			}
			joueurList.add(p);
		}
	}

	
	public static Map dupliqueTempRoiToDomino() {
		Map kingToDomino = new LinkedHashMap<Roi, Domino>();
		Set keys = tempKingToDomino.keySet();
		Iterator<Roi> kings = keys.iterator();
		while (kings.hasNext()) {
			Roi king = kings.next();
			Domino d = tempKingToDomino.get(king);
			kingToDomino.put(king, d);
		}
		return kingToDomino;
	}
	public static void renderMiniLand(Graphics graphics) {
		float x = width * 0.15f;
		float y = height * 0.83f;
		int i = 0;
		for (Joueur p : joueurList) {
			p.renderLandMini(graphics, x + i * 10 * dominoWidth * 0.2f, y);
			i++;
		}
	}
	public static void printJoueurList(List l) {
		Iterator<Joueur> iterator = l.iterator();
		while (iterator.hasNext()) {
			printJoueurInfo(iterator.next());
		}
	}

	
	// methode qui va permettre d'importer toutes nos images 
	@Override
	public void init(GameContainer JeuContainer) throws SlickException {
		width = JeuContainer.getWidth();    // on importe la m�thode largeur
		height = JeuContainer.getHeight();  // on importe la methode longueur
		dominoWidth = (int) (width * 0.05);
		dominoList = loadDominos("dominos.csv"); // dominos.csv
		Collections.shuffle(dominoList);     // on m�lange la liste des domino pour pouvoir les tirer al�atoirement
		//printDominoList(dominoList);
		
		// chargement des images
		Roi.roi = new Image("Ressources/roi.png");

		Domino.couronne = new Image("Ressources/Crown2.png");

		CHAMPS_IMAGE = new Image("Ressources/champs.png");
		FORET_IMAGE = new Image("Ressources/foret.png");
		MER_IMAGE = new Image("Ressources/mer.png");
		MINE_IMAGE = new Image("Ressources/mines.png");
		MONTAGNE_IMAGE = new Image("Ressources/montagne.png");
		PRAIRIE_IMAGE = new Image("Ressources/prairie.png");
		CHATEAU_IMAGE = new Image("Ressources/chateau.jpg");
		EMPTY_IMAGE = new Image("Ressources/empty.jpg");
		Image backgtour; 
		Image mainScreen; 
		Image panel;

		backgtour = new Image("Ressources/board2.png");
		panel = new Image("Ressources/yangpizhi.png");
		mainScreen = new Image("Ressources/main.jpg");
		
		font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF, java.awt.Font.BOLD, (int) (height * 0.025)),
				false);
		textField = new TextField(JeuContainer, font, (int) (width * 0.5 - width * 0.25),
				(int) (height * 0.5 - height * 0.1), (int) (width * 0.5), (int) (height * 0.05));
		textField.setBackgroundColor(Color.gray);
		textField.setBorderColor(Color.white);


	}
	@Override
	public void update(GameContainer JeuContainer, int delta) throws SlickException {
		// System.out.println("current phase" + Jeu_PHASE);
		Input input = JeuContainer.getInput();

		float x, y;
		int i1, i2;
		switch (Jeu_PHASE) {

		case MAIN_SCREEN:
			if (input.isKeyPressed(CONFIRM)) {
				Jeu_PHASE++;
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
					Jeu_PHASE++;
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
					new Joueur(name, listColor.get(createdJoueur), RoiPerJoueur);

					if (name.equals("AI")) {
						p = new AI(name, listColor.get(createdJoueur), RoiPerJoueur);
					} else {
						p = new Joueur(name, listColor.get(createdJoueur), RoiPerJoueur);
					}
					for (int j = 0; j < RoiPerJoueur; j++) {
						Roi k = new Roi(listColor.get(createdJoueur));
						RoiList.add(k);
						//p.getRois().add(k);
					}
					//joueurList.add(p);
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

				Jeu_PHASE = tour == 1 ? SHUFFLE_RoiS : DRAW_DOMINOS;
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
				Jeu_PHASE = DRAW_DOMINOS;
				// RoisShuffled = false;
				displayedString1 = "";
				displayedString2 = "";
			}
			break;

		case DRAW_DOMINOS:

			dominoListDraw = drawDominos();
			dominoListDraw = sortDominoByNum(dominoListDraw);
			dominoTempList = dupliqueDominoList(dominoListDraw);
			Jeu_PHASE = CHOSE_DOMINO;
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
					// System.out.println(x * 3 * Jeu.dominoWidth + " " + (x + Jeu.dominoWidth *
					// (2 + 3)));
					// System.out.println(x * 3 * Jeu.dominoWidth + " " + (x + Jeu.dominoWidth *
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
					Jeu_PHASE = DRAW_DOMINOS;
					RoiToDomino = dupliqueTempRoiToDomino();
					RoiList = configRoiListForNextTurn(dominoTempList);
					tour++;

				} else {
					Jeu_PHASE = PLACE_DOMINO;
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
					Jeu_PHASE = DRAW_DOMINOS;
				} else if (tour == totaltour) {
					Jeu_PHASE = PLACE_DOMINO;
				} else {
					Jeu_PHASE = Jeu_OVER;

				}
				displayedString1 = "";
				displayedString2 = "";

				playedRoi = 0;
				currentJoueur = null;
				currentDomino = null;
				RoiToDomino = dupliqueTempRoiToDomino();
				for (Joueur p : joueurList) {
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

		case Jeu_OVER:

			if (!scoreCalculated) {
				finalScore();
			}
			displayedString1 = "Jeu OVER   THE WINNER IS : " + winner.getNomJoueur();
			displayedString2 = "SCORE : " + winnerScore;

			break;

		}

		// mousePressedPosition(input);

	}
	
	public static void inisialisation() {
		// initialiser
		dominoList = loadDominos("dominos.csv"); // dominos.csv
		Collections.shuffle(dominoList);
		printDominoList(dominoList);
	}
	
	public static void JoueursInit() {
		// inisialisation des joueurs
		NumeroJoueur = inputNumeroJoueur();
		creatJoueurs();
		printJoueurList(joueurList);
		NumeroRoi = (NumeroJoueur == 2 || NumeroJoueur == 4) ? 4 : 3;
		System.out.println("nombre de rois : " + NumeroRoi);
		System.out.println("___________________________________________________");
		dominoList = configDominoList();
		printDominoList(dominoList);
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
	
	public static void printRoiToDomino() {
		Set keys = RoiToDomino.keySet();
		Iterator<Roi> kings = keys.iterator();
		while (kings.hasNext()) {
			Roi king = kings.next();
			Domino d = RoiToDomino.get(king);
			printRoiInfo(king);
			printDominoInfo2(d);

		}
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
	
	
	// initialisation de la liste, nombre de domino pr�sents dans la liste
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
	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		// TrueTypeFont font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF,
		// java.awt.Font.BOLD, 50),false);
		graphics.setFont(font);
		textField.setFocus(true);

		if (Jeu_PHASE == PLACE_DOMINO || Jeu_PHASE == DRAW_DOMINOS || Jeu_PHASE == CHOSE_DOMINO
				|| Jeu_PHASE == SHUFFLE_RoiS || Jeu_PHASE == Jeu_OVER) {
			background.draw(0, 0, width, height);
			panel.draw(width * 0.7f, 0, panel.getWidth() * 1.3f, height);
		}

		switch (Jeu_PHASE) {

		case MAIN_SCREEN:
			graphics.setColor(MESSAGE_COLOR);
			mainScreen.draw(0, 0, width, height);
			graphics.drawString("Appuyez sur la touche ENTREE pour continuer", width * 0.5f - width * 0.15f,
					height * 0.5f);

			break;

		case INPUT_Joueur_NUMBER:
			graphics.setColor(MESSAGE_COLOR);
			textField.render(gameContainer, graphics);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
			graphics.setColor(WARNING_COLOR);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.2f);

			break;

		case CREAT_JoueurS:
			graphics.setColor(MESSAGE_COLOR);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.2f);
			textField.render(gameContainer, graphics);

			break;

		case SHUFFLE_RoiS:
			float x = width * 0.125f;
			float y = height * 0.3f;
			int i = 1;
			Iterator<Roi> iter = RoiList.iterator();
			while (iter.hasNext()) {
				iter.next().render(graphics, i * x, y);
				i++;
			}
			break;

		case DRAW_DOMINOS:
			graphics.drawString("Round : " + Jeu.tour, Jeu.width * 0.75f, Jeu.height * 0.2f);
			x = width * 0.125f;
			y = height * 0.3f;

			int i1 = 1;
			if (!dominoListDraw.isEmpty()) {
				Iterator<Domino> iterDomino = dominoListDraw.iterator();
				while (iterDomino.hasNext()) {
					Domino domino = iterDomino.next();
					domino.renderFixed(graphics, x + 3 * dominoWidth * i1, y);
					i1++;
				}
			}
			break;

		case CHOSE_DOMINO:
			graphics.setColor(Jeu.ALTERNATIVE_MESSAGE);
			// graphics.drawString("Round : " + Jeu.ROUND, Jeu.width * 0.75f, Jeu.height
			// * 0.2f);
			i = 0;
			x = width * 0.125f;
			y = height * 0.3f;

			infoX = Jeu.width * 0.75f;
			infoY = Jeu.height * 0.2f;
			d = Jeu.width * 0.015f;

			graphics.setColor(Jeu.ALTERNATIVE_MESSAGE);
			graphics.drawString("Round : " + Jeu.tour, infoX, infoY);
			if (currentJoueur != null) {
				graphics.drawString(currentJoueur.getNomJoueur() + " choisissez un domino", infoX, infoY + d);
			}
			if (currentRoi != null) {
				currentRoi.render(graphics, infoX + 4 * d, infoY + 10 * d);
			}
			if (!dominoListDraw.isEmpty()) {
				Iterator<Domino> iterDomino = dominoListDraw.iterator();
				while (iterDomino.hasNext()) {
					Domino domino = iterDomino.next();
					if (!domino.chosed) {
						domino.renderFixed(graphics, x + 3 * dominoWidth * i, y);
						i++;
					}
				}
			}
			renderMiniLand(graphics);

			break;

		case PLACE_DOMINO:
			infoX = Jeu.width * 0.75f;
			infoY = Jeu.height * 0.2f;
			d = Jeu.width * 0.015f;
			graphics.setColor(Jeu.ALTERNATIVE_MESSAGE);
			graphics.drawString("Round : " + Jeu.tour, infoX, infoY);

			if (currentJoueur != null) {
				currentJoueur.render(graphics);
			}
			if (currentDomino != null) {
				currentDomino.render(graphics);
			}
			renderMiniLand(graphics);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.09f);
			graphics.setColor(WARNING_COLOR);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.095f);

			break;

		case Jeu_OVER:
			if (winner != null) {
				graphics.setColor(ALTERNATIVE_MESSAGE);
				graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
				graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f + height * 0.015f);
				renderMiniLand(graphics);

				winner.renderInfo(graphics, ALTERNATIVE_MESSAGE);
			}

		}

	}

	
	
}


	

