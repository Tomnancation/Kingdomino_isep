import java.io.*;
import java.util.*;

public class Console {
	public static int NumeroJoueur;
	public static int NumeroRoi;
	public static int tour=1;
	public static List<Joueur> joueurList = new ArrayList<>();
	public static int maxTour;
	
	public static List<Integer> ordreJoueur;
	
	public static List<Domino> dominoList;
	static List<Domino> dominoListDraw;
	static List<Roi> roiList = new ArrayList<>();
	static Map<Roi, Domino> RoiToDomino = new LinkedHashMap<Roi, Domino>();
	
	//Getters
	public static int getNumeroJoueur() {
		return NumeroJoueur;
	}
	public static int getNumeroRoi() {
		return NumeroRoi;
	}
	
	// On fixe le nombre de joueurs entre 2 et 4
	// !!
	public static boolean numJoueurOK(int joueurN) {
		return 2 <= joueurN && joueurN <= 4;
	}
	
	// Valid check

	public static boolean gameOver() {

		return (tour <= maxTour) ? false : true;

	}
	

	//methode dans le cas ou le joueur ne va pas saisir un entier on lui affiche une erreur
	public static int saisirInt() {
		int nombre = 0;
		Scanner scan = new Scanner(System.in);
		boolean bonneValeur = false;
		//Vérifiez si le joueur entre un numéro
		do {
			try {
				nombre = scan.nextInt();
				scan.nextLine();
				bonneValeur = true;
			} catch (Exception e) {
				System.out.println("Erreur : Veuillez saisir un nombre entier.");
				scan.nextLine();
			}
		} while (!bonneValeur);
		return nombre; // n est le nombre de joueur retourn�
	}
	
	public static boolean bonnePosition(int x) {
		return 0 <= x && x <= 8;
	}
	
	// !!
	public static int saisirPosition() {
		int x = 0;
		do {
			x = saisirInt();
			if (!bonnePosition(x)) {
				System.out.println("Saisissez un nombre entre 0 et 8");
			}
		} while (!bonnePosition(x));
		return x;

	}
	
	// methode pour le nom du joueur
	public static String inputString() {
		String string = null;
		boolean bonneValeur = false;
		Scanner scan = new Scanner(System.in);
		do {
			try {
				string = scan.nextLine();
				bonneValeur = true;
			} catch (Exception e) {
				System.out.println("Erreur : Veuillez entrer une chaine de caractere valide.");
				scan.nextLine();
			}
		} while (!bonneValeur);
		return string;
	}
	
	// cette methode combine les deux methodes precedentes, le nombre doit etre entre 2 et 4 et doit etre un nombre
	public static int saisirNumJoueur() {
		int nombre;
		System.out.println("Saisissez le nombre de joueur (chiffre entier 2 a 4) : ");
		do {
			nombre = saisirInt();
			if (!numJoueurOK(nombre)) {
				System.out.println("Erreur : Veuillez entrer un nombre entier entre 2 et 4.");
			}
		} while (!numJoueurOK(nombre));
		return nombre;

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
	
	public static void printDominoInfo2(Domino d) {
		System.out.println("Domino No." + d.getNumDomino());
		System.out.println("Type 1 : " + Domino.typeToString(d.getType1()));
		System.out.println("crown number 1 : " + d.getNbcouronne1());
		System.out.println("Type 2 : " + Domino.typeToString(d.getType2()));
		System.out.println("crown number 2 : " + d.getNbcouronne2());
		System.out.println();
	}
	
	public static Joueur getJoueurByRoi(Roi k) {
		for (Joueur p : joueurList) {
			if (p.getRois().contains(k)) {
				return p;
			}
		}
		return null;
	}
	
	public static void printRoiInfo(Roi k) {
		System.out.println("-------------------");
		System.out.println(k.getCouleur() + " roi");
		System.out.println("roi id : " + k.getId());
		System.out.println("-------------------");
	}
	public static void printroiList(List<Roi> l) {
		Iterator<Roi> iterator = l.iterator();
		while (iterator.hasNext()) {
			printRoiInfo(iterator.next());
		}
		System.out.println("Nombre de rois : " + l.size());
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

	
	public static List configRoiListForNextTurn(List dominoTempList) {
		List<Roi> temproiList = new ArrayList<>();
		Iterator<Domino> iter = dominoTempList.iterator();
		while (iter.hasNext()) {
			Roi k = getRoiFromDomino(iter.next());
			temproiList.add(k);
		}
		return temproiList;

	}
	
	public static void inisialisation() {
		// initialiser
		dominoList = loadDominos("dominos.csv"); // dominos.csv
		Collections.shuffle(dominoList);

	}

	public static void printJoueurInfo(Joueur p) {
		System.out.println("Joueur No." + p.getId());
		System.out.println("Joueur name : " + p.getNomJoueur());
		System.out.println("roi number : " + p.getNumeroRoi());
		System.out.println("roi color : " + p.getCouleurRoi());
		System.out.println("Joueur type : " + p.getJoueurType());

		printroiList(p.getRois());
		p.printLand();
		System.out.println();
	}
	
	public static void printJoueurList(List l) {
		Iterator<Joueur> iterator = l.iterator();
		while (iterator.hasNext()) {
			printJoueurInfo(iterator.next());
		}
	}
	// initialisation des joueur
	public static void joueursInit() {
		NumeroJoueur = 2;
		printJoueurList(joueurList);
		NumeroRoi = (NumeroJoueur == 2 || NumeroJoueur == 4) ? 4 : 3;
		maxTour = (NumeroJoueur == 2) ? 7 : 13;

		System.out.println("nombre de rois : " + NumeroRoi);
		System.out.println("___________________________________________________");
		dominoList = configDominoList();

	}

	
	
	public static List configDominoList() {
		List<Domino> tempList = new ArrayList<Domino>();
		tempList = dominoList.subList(0, NumeroJoueur * 12);
		return tempList;
	}

	public static List<Domino> drawDominos() {
		List<Domino> tempList;
		tempList = dominoList.subList(0, NumeroRoi);
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

	public static List<Domino> sortDominoByNum(List l) {
		List<Domino> tempList = new ArrayList<Domino>();
		do {
			tempList.add(dominoNumMin(l));
			l.remove(dominoNumMin(l));
		} while (l.size() != 0);
		return tempList;
	}
	
	public static List dupliqueDominoList(List l) {
		List<Domino> dominoTempList = new ArrayList<Domino>();
		Iterator<Domino> iter = l.iterator();
		while (iter.hasNext()) {
			dominoTempList.add(iter.next());
		}
		return dominoTempList;
	}
	
	public static void printroiToDomino() {
		Set keys = RoiToDomino.keySet();
		Iterator<Roi> rois = keys.iterator();
		while (rois.hasNext()) {
			Roi roi = rois.next();
			Domino d = RoiToDomino.get(roi);
			printRoiInfo(roi);
			printDominoInfo2(d);

		}
	}
	

	
	public static void tour() {

		System.out.println("Round : " + tour);

		if (tour != maxTour) {
			dominoListDraw = drawDominos();
			// printDominoList(dominoListDraw);
			System.out.println("___________________________________________________");
			dominoListDraw = sortDominoByNum(dominoListDraw);
			// printDominoList(dominoListDraw);
		}
		System.out.println("___________________________________________________");
		List dominoTempList = dupliqueDominoList(dominoListDraw);

		Map temproiToDomino = new LinkedHashMap<Roi, Domino>();

		if (tour == 1) {
			Collections.shuffle(roiList);
			for (Roi k : roiList) {
				Joueur p = getJoueurByRoi(k);
				temproiToDomino.put(k, k.draw()); // choose domino
			}
		}

		System.out.println("___________________________________________________");
		System.out.println("L'ordre de ce tour : ");
		printroiList(roiList);
		System.out.println("___________________________________________________");

		System.out.println("___________________________________________________");
		
		for (Roi k : roiList) {
			Joueur p = getJoueurByRoi(k);
			// printPlayerInfo(p);
			if (tour != 1) {

				if (tour < maxTour) {
					temproiToDomino.put(k, k.draw()); // choose domino
				}

				// place domino
				Domino domino = RoiToDomino.get(k);

				
					int x, y;
					boolean putOk = false;
					do {
						do {
							p.printLand();
							domino.printDominoInfo();
							domino.setDirection(Domino.D1);
							p.detectPositions(domino);
							domino.setDirection(Domino.D1);
							domino.turnDomino();
							x = saisirPosition();
							y = saisirPosition();
						} while (p.isLandOccupied(domino, x, y) || !p.isPlaceOk(domino, x, y));
						p.placeDomino(domino, x, y);
						if (!p.isDimensionOk()) {
							p.removeDominoFromLand(domino, x, y);
							System.out.println("Dimension out of bounds !");
						} else {
							putOk = true;
							p.printLand();
							System.out.println("Current score of player " + p.getId() + " is " + p.calculateScore());

						}
					} while (!putOk);

				} 
			}
		
		RoiToDomino = temproiToDomino;

		System.out.println("___________________________________________________");
		printroiToDomino();
		System.out.println("___________________________________________________");
		roiList = configRoiListForNextTurn(dominoTempList);

		tour++;
	
	}
	public static void creatPlayers() {
		ArrayList<String> listColor = new ArrayList<String>(Arrays.asList("red", "yellow", "green", "pink"));
		Joueur p;
		// Collections.shuffle(listColor);
		System.out.println(listColor);
		int roiPerJoueur = NumeroJoueur == 2 ? 2 : 1;
		System.out.println("Nombre de joueur : " + NumeroJoueur);
		for (int i = 1; i <= NumeroJoueur; i++) {
			System.out.println("Saisissez le nom du joueur No." + i);
			String name = inputString();
			name = name.isEmpty() ? "Player" + Integer.toString(i) : name;
			p = new Joueur(name, listColor.get(i - 1), roiPerJoueur);
			

			for (int j = 0; j < roiPerJoueur; j++) {
				Roi k = new Roi(listColor.get(i - 1));
				roiList.add(k);
				p.getRois().add(k);
			}
			joueurList.add(p);
		}
	}
	
	
	public static List<Domino> loadDominos(String filePath) {
		List<Domino> dominoList = new ArrayList<Domino>();
		Scanner scanner;
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

				// importer les dominos dans dominoList
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
	
	
	public static void main(String[] args) {

		// initialisation
		for (int i = 0; i < 2000; i++) {
			inisialisation();
			System.out.println("___________________________________________________");
			System.out.println("___________________________________________________");
			// créer des joueurs
			joueursInit();
			System.out.println("___________________________________________________");
			System.out.println("___________________________________________________");
			// changer de tour
			while (!gameOver()) {
					tour();
			}

			System.out.println("GAME OVER");
			for (Joueur p : joueurList) {
				int finalScore = p.finalScore();
				p.printLand();
				System.out.println("Final score of player " + p.getId() + " is " + finalScore);
				System.out.println(p.finalScore + "," + p.chateauCenter + "," + p.emptyAreaNum + ","
						+ p.singleEmptyAreaNum + "," + p.totalCrownNum + "\n");
				try {
					p.printResultInCsv();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			System.out.println("___________________________________________________");
			tour = 1;
			joueurList.removeAll(joueurList);
			dominoList.removeAll(dominoList);
			dominoListDraw.removeAll(dominoListDraw);
			roiList.removeAll(roiList);
			RoiToDomino.clear();
		}
	}
	
}


