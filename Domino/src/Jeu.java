import java.io.*;
import java.util.*;
//foret et prairie pb de zoom
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

public  class Jeu extends BasicGame {
//Constructeur
	public Jeu(String title) {
		super(title);
		
	}
	
//On passe a la définition des attributs, certains attributs tels que les listes ou les attributs graphiques ont été défini apres les premiers	
	private final static int ID = 1;				//identificatio de la partie
	
	public static int width, height, dominoWidth;   // graphique on fait appel aux attributs et methodes 
//attributs de classe
	public static int NumeroJoueur;
	public static int NumeroRoi;
	public static int tour = 1;  			//premier tour pour le choix de domino, il ne permet pas de placer les dominos 
	
	public static List<Joueur> joueurList = new ArrayList<>();
	
	
	//on configure les touches haut bas gauche droite du clavier
	public final static int HAUT = Input.KEY_UP;
	public final static int BAS = Input.KEY_DOWN;
	public final static int GAUCHE = Input.KEY_LEFT;
	public final static int DROITE = Input.KEY_RIGHT;
	public final static int CONFIRM = Input.KEY_ENTER;
	public final static int KEY_1 = Input.KEY_SPACE;
	
	public final static int MAIN_SCREEN = 0;
	public static final int INPUT_Joueur_NUMBER = 1;
	public final static int CREAT_JoueurS = 2;
	public final static int SHUFFLE_RoiS = 3;
	public final static int DRAW_DOMINOS = 4;
	public final static int CHOSE_DOMINO = 5;
	public final static int PLACE_DOMINO = 6;
	public final static int Game_OVER = 100;
	
	public static int Jeu_PHASE = 0;
	public static Joueur currentJoueur;
	public static Roi currentRoi;
	public static Domino currentDomino;
	
	public final static Color MESSAGE_COLOR = Color.white;
	public final static Color ALTERNATIVE_MESSAGE = Color.white;
	public final static Color WARNING_COLOR = Color.red;
	public final static Color ALTERNATIVE_MESSAGE2 = Color.white;
	
	
	static List<Roi> roiList = new ArrayList<Roi>();
	
	static TextField textField;		//CHAMP TEXTE POUR pouvoir entrer des caracteres
	static Domino displayedDomino;		//Affichage graphique des domino
	static String displayedString1 = "";
	static String displayedString2 = "";
	static String displayedString3 = "";//affichage graphique de tests
	
	static int createdJoueur = 0;
	static int RoiChoseDomino = 0;
	static int playedRoi = 0;
	static int totaltour;				//initialisation de variables
	
	static Joueur winner;
	static int winnerScore;			// stockage du resultat du jeu
	TrueTypeFont font;				//police
	
//Stockage des images dans des variables
	public static Image FORET_IMAGE;
	public static Image CHAMPS_IMAGE;
	public static Image MER_IMAGE;
	public static Image PRAIRIE_IMAGE;
	public static Image MINE_IMAGE;
	public static Image MONTAGNE_IMAGE;
	public static Image CHATEAU_IMAGE;
	public static Image EMPTY_IMAGE;			//
	
	public static List<Domino> dominoList = new ArrayList<>();;
	static List<Domino> dominoListDraw = new ArrayList<>();;
	static List<Domino> dominoTempList;
	
	static Map<Roi, Domino> RoiToDomino = new LinkedHashMap<Roi, Domino>();     		//association roi et domino dans une linked list
	static Map<Roi, Domino> tempRoiToDomino = new LinkedHashMap<Roi, Domino>();
	
	static boolean RoisShuffled = false;
	static boolean tempRoiToDominoInitialised = false;
	static boolean scoreCalculated = false;
	static boolean lasttourFlag = false;
	
	static float infoX, infoY, d;
	Image background; 
	Image mainScreen;  //image accueil
	Image background2;
	Image background3;
	Image gover;
	
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
				}// condition sur la le nombre de joueurs 
	
	public static boolean isPositionValid(int x) {
		return 1 <= x && x <= 9;    //dimension maximale du royaume
	}

	//Implementation d'une methode qui nous permettra de saisir un entier
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
	
//Position du domino	
	public static int inputPosition() {
		int x = 0;
		do {
			x = inputInt();
			if (!isPositionValid(x)) {
				System.out.println("Saisissez un nombre entre 1 et 5");
			}
		} while (!isPositionValid(x));
		return x;

	}
	
//	On parcourt la liste de domino on passe d'un domino a l'autre
		public static List dupliqueDominoList(List l) {
			List<Domino> dominoTempList = new ArrayList<Domino>();
			Iterator<Domino> iter = l.iterator();
			while (iter.hasNext()) {
				dominoTempList.add(iter.next());
			}
			return dominoTempList;
		}
//	Methode pour saisir une chaine de caractere on l'utilise pour ecrire le nom des joueurs	
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
	//saisir le nombre de joueurs au tout début du jeu	
		public static int inputNumeroJoueur() {
			int n;
			
			displayedString1 = "WELCOME ! Veuillez saisir un nombre de joueurs entre 2 et 4 "; //
			do {
				n = inputInt();
				if (!NumeroJoueurValid(n)) {
					System.out.println("Erreur : Veuillez entrer un nombre entier entre 2 et 4.");
				}
			} while (!NumeroJoueurValid(n));
			return n;

		}
		//configuration de la methode tour
		public static void tour() {

			dominoListDraw = drawDominos();    
			System.out.println("___________________________________________________");
			dominoListDraw = sortDominoByNum(dominoListDraw); //On ordonne les dominos
			System.out.println("___________________________________________________");
			List dominoTempList = dupliqueDominoList(dominoListDraw);
			printDominoList(dominoTempList);
			if (tour == 1) { //tuiles de départ pour y placer le royaume cf video
				Collections.shuffle(roiList);  //on melange less rois pour faire la pioche
			}
			System.out.println("___________________________________________________");
			System.out.println("L'ordre de ce tour : ");
			printroiList(roiList);
			System.out.println("___________________________________________________");

			System.out.println("___________________________________________________");
			Map tempRoiToDomino = new LinkedHashMap<Roi, Domino>();

			for (Roi k : roiList) {
				Joueur p = getJoueurByRoi(k);
				if (tour != 1) {
					// on place domino
					int x, y;
					Domino domino = RoiToDomino.get(k);
					do {
						domino.turnDomino(); //METHODE QUI PERMET DE retourner un domino avec les touches du clavier
						x = inputPosition() - 1;
						y = inputPosition() - 1;
					} while (!p.isPlaceOk(domino, x, y) && !p.isLandOccupied(domino, x, y));
					p.placeDomino(domino, x, y);
				}
				tempRoiToDomino.put(k, k.draw());
			}

			RoiToDomino = tempRoiToDomino;

			System.out.println("___________________________________________________");
			printRoiToDomino();
			System.out.println("___________________________________________________");
			printDominoList(dominoTempList);
			roiList = configroiListForNextTurn(dominoTempList);
			printroiList(roiList);
			tour++;
		}
	// On termine le jeu s'il n'ya plus de dominos a placer	
		public void gameOver() {
			if (dominoList.size() == 0)
				Jeu_PHASE = 100;
			displayedString1 = "Vous avez placé tous les dominos ";
		}	
		public static void printroiList(List<Roi> l) {
			Iterator<Roi> iterator = l.iterator();
			while (iterator.hasNext()) {
				printRoiInfo(iterator.next());
			}
			System.out.println("Nombre de rois : " + l.size());
		}
		public static void printRoiInfo(Roi r) {
			System.out.println("-------------------");
			System.out.println(r.getCouleur() + " Roi");
			System.out.println("Roi id : " + r.getId());
			System.out.println("-------------------");
		}
		
		
//pour le prochain tour on garde la partie suivante et on ajoute de nouveaux elements au chateau
		public static List configroiListForNextTurn(List dominoTempList) {
			List<Roi> tempRoiList = new ArrayList<>();
			Iterator<Domino> iter = dominoTempList.iterator();
			while (iter.hasNext()) {
				Roi k = getRoiFromDomino(iter.next());
				tempRoiList.add(k);
			}
			return tempRoiList;

		}
		
		public static Roi getRoiFromDomino(Domino d) {
			List tempList = new ArrayList<>();
			Roi roi = null;
			for (Roi k : RoiToDomino.keySet()) {
				if (RoiToDomino.get(k).equals(d)) {
					roi = k;
				}
			}
			return roi;
		}
		//CHARGEMENT DU DOMINO.CSV
	public static List loadDominos(String filePath) {
		List dominoList = new ArrayList<Domino>();   // On creer une liste de dominos
		Scanner scanner;                             // pour avoir acces au clavier
		int NbCouronne1, type1, NbCouronne2, type2, NumDomino;  //colonnes du tableau
		boolean loadSuccessful = false;
		do {
			try {
				File file = new File(filePath);
				scanner = new Scanner(file);
				scanner.useDelimiter(",|\n|\r");

				for (int i = 0; i <= 4; i++) { 
					scanner.next();
				}

				// load dominos in dominoList
				for (int i = 0; i < 48; i++) {
					System.out.println(scanner.next());

					NbCouronne1 = Integer.parseInt(scanner.next()); //parseint remplace next line
					type1 = Domino.typeToInt(scanner.next());
					NbCouronne2 = Integer.parseInt(scanner.next());
					type2 = Domino.typeToInt(scanner.next());
					NumDomino = Integer.parseInt(scanner.next());

					Domino d = new Domino(NbCouronne1, type1, NbCouronne2, type2, NumDomino); //on definit un domino par ses attributs

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
	//associe joueur a roi ainsi si on a un roi on peut definir le joueur qui lui correspond
	public static Joueur getJoueurByRoi(Roi k) {
		for (Joueur p : joueurList) {
			if (p.getRois().contains(k)) {
				return p;
			}
		}
		return null;
	}
//Affichage des infos joueur	
	public static void printJoueurInfo(Joueur p) {
		System.out.println("Joueur No." + p.getId());
		System.out.println("Joueur name : " + p.getNomJoueur());
		System.out.println("Roi number : " + p.getNumeroRoi());
		System.out.println("Roi color : " + p.getCouleurRoi());
		printroiList(p.getRois());
		p.printLand();
		System.out.println();
	}
	
	public static void finalScore() {
		winner = joueurList.get(0);
		winner.calculateScore();
		winnerScore = winner.score;
		for (Joueur p : joueurList) {
			int score = p.calculateScore();
			if (score > winnerScore) {
				winner = p;
				winnerScore = score;
			}
		}
		System.out.println("GAME OVER");
		System.out.println("Le gagnant est : " + winner.getNomJoueur());
		System.out.println("Score final joueur" + winner.getId() + " est " + winnerScore);
		scoreCalculated = true;
	}
		
	public static void creatJoueurs() {
		ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("red", "yellow", "green", "pink"));
		
		int roiPerJoueur = NumeroJoueur == 2 ? 2 : 1;
		System.out.println("Nombre de joueur : " + NumeroJoueur);
		for (int i = 1; i <= NumeroJoueur; i++) {
			System.out.println("Saisissez le nom du joueur No." + i);
			String name = inputString(); //on appelle la methode input string
			name = name.isEmpty() ? "Player" + Integer.toString(i) : name;
			Joueur p = new Joueur(name, listColor.get(i - 1), roiPerJoueur);
			
			for (int j = 0; j < roiPerJoueur; j++) {
				Roi k = new Roi(listColor.get(i - 1));
				roiList.add(k);
				p.getRois().add(k);
			}
			joueurList.add(p);
		}
	}

	//alterner les rois quand on choisit les domi
	public static Map dupliqueTempRoiToDomino() {
		Map roiToDomino = new LinkedHashMap<Roi, Domino>();  //associer un roi a un domino
		Set keys = tempRoiToDomino.keySet();  //ensemble d'element clé
		Iterator<Roi> rois = keys.iterator();
		while (rois.hasNext()) {
			Roi roi = rois.next();
			Domino d = tempRoiToDomino.get(roi);
			roiToDomino.put(roi, d);
		}
		return roiToDomino;
	}
//on alterne l'affichage des infos de joueurs	
	public static void printJoueurList(List l) {
		Iterator<Joueur> iterator = l.iterator();
		while (iterator.hasNext()) {
			printJoueurInfo(iterator.next());
		}
	}
//methode qui permet d'afficher le jeu en petite dimension pour suivre l'avancement 	
	public static void renderMiniLand(Graphics graphics) {
		float x = width * 0.50f; //position du miniLand (bleu en bas d'ecran a droite)
		float y = height * 0.83f;  //position du miniLand
		int i = 0;
		for (Joueur p : joueurList) {
			p.renderLandMini(graphics, x + i * 10 * dominoWidth * 0.2f, y); //affichage en fonction du nombre de joueurs
			i++;
		}
	}
	
	// methode qui va permettre d'importer toutes nos images 
	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		width = gameContainer.getWidth();    // on importe la methode largeur
		height = gameContainer.getHeight();  // on importe la methode longueur
		dominoWidth = (int) (width * 0.05);
		dominoList = loadDominos("dominos.csv"); // dominos.csv
		Collections.shuffle(dominoList);     // on m�lange la liste des domino pour pouvoir les tirer aleatoirement
		//printDominoList(dominoList);
		
		// chargement des images dans le fichier ressource
		Roi.roi = new Image("Ressources/roi.png");

		Domino.couronne = new Image("Ressources/Crown2.png");
		FORET_IMAGE = new Image("Ressources/foret.png");
		CHAMPS_IMAGE = new Image("Ressources/champs.png");
		MER_IMAGE = new Image("Ressources/mer.png");
		MINE_IMAGE = new Image("Ressources/mines.png");
		MONTAGNE_IMAGE = new Image("Ressources/montagne.png");
		PRAIRIE_IMAGE = new Image("Ressources/prairie.jpg");
		CHATEAU_IMAGE = new Image("Ressources/chateau.jpg");
		EMPTY_IMAGE = new Image("Ressources/empty.jpg");
		background2 = new Image("Ressources/backg2.jpeg");
		background = new Image("Ressources/board2.png");
		mainScreen = new Image("Ressources/main.jpg");
		background3 = new Image("Ressources/backg3.jpeg");
		gover = new Image("Ressources/pan.jpeg");
		font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF, java.awt.Font.BOLD, (int) (height * 0.025)),
				false);
		textField = new TextField(gameContainer, font, (int) (width * 0.5 - width * 0.25),
				(int) (height * 0.5 - height * 0.1), (int) (width * 0.5), (int) (height * 0.08)); //centrer le champ text field et definir ses mesures ainsi que la police qu'on utilisera
		textField.setBackgroundColor(Color.gray);
		textField.setBorderColor(Color.white);


	}
	@Override
	public void update(GameContainer gameContainer, int delta) throws SlickException {
		Input input = gameContainer.getInput();

		float x, y;
		int i1, i2;
		switch (Jeu_PHASE) {

		case MAIN_SCREEN:
			if ( input.isKeyPressed(Input.KEY_1)||input.isKeyPressed(CONFIRM) ) {
				Jeu_PHASE++;
			}
			break;

		case INPUT_Joueur_NUMBER:
			
			displayedString1 = "Bienvenue ! Veuillez saisir le nombre de joueurs souhaité (nombre compris entre 2 et 4): ";
			int n = 0;
			if (input.isKeyPressed(CONFIRM)) {
				try {
					n = Integer.parseInt(textField.getText());
				} catch (Exception e) {
					displayedString2 = "Erreur ! Veuillez entrer un nombre entier entre 2 et 4.";
					textField.setText("");
				}

				if (!NumeroJoueurValid(n)) { // on verifie la saisie
					displayedString2 = "Veuillez entrer un nombre entier entre 2 et 4.";
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

		case CREAT_JoueurS: // recuperation du nombre de joueur 
			
			displayedString1 = "";
			displayedString2 = "";

			displayedString1 = "Vous avez saisi : " + String.valueOf(NumeroJoueur); //conversion en string

			ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("rose", "jaune", "vert", "bleu"));
			int RoiPerJoueur = NumeroJoueur == 2 ? 2 : 1;

			if (createdJoueur < NumeroJoueur) {
				
				displayedString2 = "Saisissez le nom du joueur qui veut choisir le roi en " + listColor.get(createdJoueur);
				String name = "";
				if (input.isKeyPressed(input.KEY_ENTER)) {
					name = textField.getText(); // apres la saisie, on recupere le nom du joueur
					name = name.isEmpty() ? "Joueur" + Integer.toString(createdJoueur + 1) : name;
					Joueur p;  // on lui donne les attributs de la classe joueur

					p = new Joueur(name, listColor.get(createdJoueur), RoiPerJoueur);
					
					for (int j = 0; j < RoiPerJoueur; j++) {
						Roi k = new Roi(listColor.get(createdJoueur));
						roiList.add(k);
						p.getRois().add(k);
					}
					joueurList.add(p);
					createdJoueur++;
					System.out.println(joueurList);
					System.out.println(roiList);
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
				Collections.shuffle(roiList);
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

			if (RoiChoseDomino < roiList.size()) {
				currentRoi = roiList.get(RoiChoseDomino);
				currentJoueur = getJoueurByRoi(currentRoi);

			}
			
			if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
					int posX = input.getMouseX();
					int posY = input.getMouseY();

					
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

			if (RoiChoseDomino >= NumeroRoi && input.isKeyPressed(CONFIRM)) {
				RoiChoseDomino = 0;
				tempRoiToDominoInitialised = false;
				displayedString1 = "";
				displayedString2 = "";
				if (tour == 1) {
					Jeu_PHASE = DRAW_DOMINOS;
					RoiToDomino = dupliqueTempRoiToDomino();
					roiList = configroiListForNextTurn(dominoTempList);
					tour++;

				} else {
					Jeu_PHASE = PLACE_DOMINO;
					printRoiToDomino();

				}

			}

			break;

		case PLACE_DOMINO:

			if (playedRoi < roiList.size()) {
				currentRoi = roiList.get(playedRoi);
				currentJoueur = getJoueurByRoi(currentRoi);
				currentDomino = RoiToDomino.get(currentRoi);
				currentDomino.update(input);
			}

			if (playedRoi < NumeroRoi) {
//On change la position du domino avec la touche gauche du clavier
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
										displayedString2 = "La dimension limite est 5 X 5"; // lorsqu'on depasse le royaume
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
				

				}
			

			if (playedRoi >= NumeroRoi && input.isKeyPressed(CONFIRM)) {

				if (tour < totaltour) {
					Jeu_PHASE = DRAW_DOMINOS;
				} else if (tour == totaltour) {
					Jeu_PHASE = PLACE_DOMINO;
				} else {
					Jeu_PHASE = Game_OVER;

				}
				
				playedRoi = 0;
				currentJoueur = null;
				currentDomino = null;
				RoiToDomino = dupliqueTempRoiToDomino();
				for (Joueur p : joueurList) {
					printJoueurInfo(p);
				}
				roiList = configroiListForNextTurn(dominoTempList);
				dominoTempList.clear();
				tour++;
				if (dominoList.isEmpty()) {
					lasttourFlag = true;

				}

			}

			break;

		case Game_OVER:

			if (!scoreCalculated) {
				finalScore();
			}
			displayedString3 = "LE GAGNANT EST : " + winner.getNomJoueur()+" "+ "AVEC UN SCORE DE "+ winnerScore;

			break;

		}


	}
	
	
	public static void printDominoList(List l) {
		int i = 0;
		Iterator<Domino> iterator = l.iterator();
		while (iterator.hasNext()) {
			System.out.println("No." + i);
			printDominoInfo2(iterator.next());
			i++;
		}
		System.out.println("longueur de la list : " + l.size());
	}
	
	public static void printRoiToDomino() {
		Set keys = RoiToDomino.keySet();
		Iterator<Roi> rois = keys.iterator();
		while (rois.hasNext()) {//quand on passe au roi suivant
			Roi roi = rois.next();
			Domino d = RoiToDomino.get(roi);  //roi choisit un domino
			printRoiInfo(roi);
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
			dominoList.remove(0); // for quand on a besoin de l'indice
		}
		// tempList = dominoList.subList(0, NumeroRoi);
		return tempList;
	}
//	algo pour determiner le plus petit domino 
	public static Domino dominoNumMin(List<Domino> liste) {
		Domino min = liste.get(0);
		for (int i = 0; i < liste.size(); i++)
			if (liste.get(i).getNumDomino() <= min.getNumDomino()) {
				min = liste.get(i);
			}
		return min;
	}
	
	//On pioche les plus petits dominos ,trier les dominos en fonction de leur numero 
	public static List<Domino> sortDominoByNum(List l) {
		List<Domino> tempList = new ArrayList<Domino>();
		do {
			tempList.add(dominoNumMin(l));
			l.remove(dominoNumMin(l));
		} while (l.size() != 0);
		return tempList;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.setFont(font);
		textField.setFocus(true);

		if (Jeu_PHASE == PLACE_DOMINO || Jeu_PHASE == DRAW_DOMINOS || Jeu_PHASE == CHOSE_DOMINO
				|| Jeu_PHASE == SHUFFLE_RoiS) {
			background.draw(0, 0, width, height);  //image packground
			 //emplacement de l'image en forme de carte
		}

		switch (Jeu_PHASE) {

		case MAIN_SCREEN:
			graphics.setColor(MESSAGE_COLOR);
			mainScreen.draw(0, 0, width, height);   //image d'accueil
			graphics.drawString("Veuillez cliquer sur la touche du clavier ENTREE pour continuer vers le jeu", width * 0.4f - width * 0.15f,
					height * 0.5f); // ecriture et position du texte

			break;
			
		case INPUT_Joueur_NUMBER:
			background3.draw(0, 0, width, height);
			graphics.setColor(MESSAGE_COLOR); //saisir le nombre de joueur
			textField.render(gameContainer, graphics);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
			graphics.setColor(WARNING_COLOR);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.2f);

			break;

		case CREAT_JoueurS:  // Message pour spécifier le nombre de joueurs qu'on a choisi etet pour demander de saisir les noms
			background2.draw(0, 0, width, height);
			graphics.setColor(MESSAGE_COLOR);
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.25f);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.5f - height * 0.2f);
			textField.render(gameContainer, graphics);

			break;

		case SHUFFLE_RoiS:  //alterner les rois
			
			float x = width * 0.125f;
			float y = height * 0.3f; //emplacement des rois lorsqu'on lance le jeu
			int i = 1;
			Iterator<Roi> iter = roiList.iterator();
			while (iter.hasNext()) {
				iter.next().render(graphics, i * x, y);
				i++;
			}
			break;

		case DRAW_DOMINOS:  // pioche
			graphics.drawString("Round : " + Jeu.tour, Jeu.width * 0.75f, Jeu.height * 0.2f);  //infos sur le tour
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
			i = 0;
			x = width * 0.125f;		//emplacement du domino a choisir
			y = height * 0.3f;

			infoX = Jeu.width * 0.75f;		//emplacement des infos joueurs on les met vers la droite
			infoY = Jeu.height * 0.2f;
			d = Jeu.width * 0.015f;  

			graphics.setColor(Jeu.ALTERNATIVE_MESSAGE);
			graphics.drawString("Round : " + Jeu.tour, infoX, infoY);  //config des messages d'accueil
			if (currentJoueur != null) {
				graphics.drawString(currentJoueur.getNomJoueur() + " choisissez un domino", infoX, infoY + d);
			}
			if (currentRoi != null) {
				currentRoi.render(graphics, infoX + 4 * d, infoY + 10 * d);
			}
			if (!dominoListDraw.isEmpty()) {// si la liste de piochage nest pas vide on renvoie les prochains dominos a choisir
				Iterator<Domino> iterDomino = dominoListDraw.iterator();
				while (iterDomino.hasNext()) {
					Domino domino = iterDomino.next();
					if (!domino.chosed) {
						domino.renderFixed(graphics, x + 3 * dominoWidth * i, y);
						i++; //on itere les dominos
					}
				}
			}
			renderMiniLand(graphics);

			break;

		case PLACE_DOMINO: // positionner les dominos sur le jeu
			infoX = Jeu.width * 0.75f;
			infoY = Jeu.height * 0.2f; 
			d = Jeu.width * 0.015f;  //On garde les informations a la droite
			graphics.setColor(Jeu.ALTERNATIVE_MESSAGE);
			graphics.drawString("Round : " + Jeu.tour, infoX, infoY);

			if (currentJoueur != null) {
				currentJoueur.render(graphics); //construction du royaume
			}
			if (currentDomino != null) {
				currentDomino.render(graphics); //construction du royaume
			}
			renderMiniLand(graphics);  //le mini graphique en bas qui montre l'evolution du jeu
			graphics.drawString(displayedString1, width * 0.5f - width * 0.25f, height * 0.09f);
			graphics.setColor(WARNING_COLOR);
			graphics.drawString(displayedString2, width * 0.5f - width * 0.25f, height * 0.095f);

			break;

		case Game_OVER:
			gover.draw(0, 0, width, height);
			if (winner != null) {
				graphics.setColor(ALTERNATIVE_MESSAGE2); //configuration des messages pour le gagnant
				graphics.drawString(displayedString3, width * 0.6f - width * 0.25f, height * 0.05f);
				
				renderMiniLand(graphics);

				
			}

		}

	}

	
	
}


	

