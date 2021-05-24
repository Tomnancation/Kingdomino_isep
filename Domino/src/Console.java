import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Console {
	public static int NumeroJoueur;
	public static int NumeroRoi;
	public static int tour=1;
	public static List<Joueur> joueurList = new ArrayList<>();
	public static int maxTour;
	
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
	public static boolean numJoueurOK(int playerN) {
		return 2 <= playerN && playerN <= 4;
	}
	
	//methode dans le cas ou le joueur ne va pas saisir un entier on lui affiche une erreur
	public static int saisirInt() {
		int nombre = 0;
		Scanner scan = new Scanner(System.in);
		boolean bonneValeur = false;

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
		System.out.println(k.getCouleur() + " king");
		System.out.println("king id : " + k.getId());
		System.out.println("-------------------");
	}
	public static void printKingList(List<Roi> l) {
		Iterator<Roi> iterator = l.iterator();
		while (iterator.hasNext()) {
			printRoiInfo(iterator.next());
		}
		System.out.println("Nombre de rois : " + l.size());
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

	
	public static List configRoiListForNextTurn(List dominoTempList) {
		List<Roi> tempKingList = new ArrayList<>();
		Iterator<Domino> iter = dominoTempList.iterator();
		while (iter.hasNext()) {
			Roi k = getRoiFromDomino(iter.next());
			tempKingList.add(k);
		}
		return tempKingList;

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
	
	public static void round() {

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

		Map tempKingToDomino = new LinkedHashMap<Roi, Domino>();

		if (tour == 1) {
			Collections.shuffle(roiList);
			for (Roi k : roiList) {
				Joueur p = getJoueurByRoi(k);
				tempKingToDomino.put(k, k.draw()); // choose domino
			}
		}

		System.out.println("___________________________________________________");
		System.out.println("L'ordre de ce tour : ");
		printKingList(roiList);
		System.out.println("___________________________________________________");

		System.out.println("___________________________________________________");
		
		for (Roi k : roiList) {
			Joueur p = getJoueurByRoi(k);
			// printPlayerInfo(p);
			if (tour != 1) {

				if (tour < maxTour) {
					tempKingToDomino.put(k, k.draw()); // choose domino
				}

				// place domino
				Domino domino = RoiToDomino.get(k);

				if (p.joueurType.equals("Person")) {
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

				} else {
					System.out.println("AI's turn !");
					p.place(domino);
				}

			}
		}
	
	}
}
