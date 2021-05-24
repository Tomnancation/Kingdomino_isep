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
	
	public final static Color MESSAGE_COLOR = Color.white;
	public final static Color ALTERNATIVE_MESSAGE = Color.black;
	public final static Color WARNING_COLOR = Color.red;
	
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
	static List<Roi> kingList = new ArrayList<Roi>();
	static Map<Roi, Domino> kingToDomino = new LinkedHashMap<Roi, Domino>();
	static Map<Roi, Domino> tempKingToDomino = new LinkedHashMap<Roi, Domino>();
	
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
		Image background; 
		Image mainScreen; 
		Image panel;

		background = new Image("board2.png");
		panel = new Image("yangpizhi.png");
		mainScreen = new Image("main.jpg");

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
		// tempList = dominoList.subList(0, kingNum);
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
	
	public static List configPlayerOrder() {
		List tempList = new ArrayList<Joueur>();
		for (Roi k : kingList) {
			for (Joueur p : joueurList) {
				if (k.getCouleur() == p.getCouleurRoi()) {
					tempList.add(p.getId());
				}
			}
		}
		return tempList;
	}
	

	
	
}


	

