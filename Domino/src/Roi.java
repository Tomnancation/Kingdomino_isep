import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class Roi {
	private int id;
	private String couleur;
	static int ID_Roi = 1;
	
	public static Image roi;

	//Constructeur
	
	public Roi(String coleur) {
		this.id = ID_Roi;
		this.couleur = coleur;
		ID_Roi++;
	}
	
	// Getters and Setters 
	public int getId() {
		return id;
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String coleur) {
		this.couleur = coleur;
	}
	
	public Domino draw() {
		Domino d = null;
		Console.printDominoList(Console.dominoListDraw);
		boolean dominoOk = false;
		if (Console.getJoueurByRoi(this).joueurType.equals("Person")) {
			do {
				try {
					Console.printRoiInfo(this);
					System.out.println("choisissez un domino");
					int n = Console.saisirInt();
					d = Console.dominoListDraw.get(n);  // on prend un domino
					Console.dominoListDraw.remove(d);  // on retire un domino de la pioche
					dominoOk = true;
				} catch (Exception e) {
					System.out.println("Erreur.");
					// e.printStackTrace();
				}
			} while (!dominoOk);
		} // else { // AI
			// if (Console.round == 1) {
				// int n = Console.getPlayerByKing(this).chooseBestDominoTurn1(Console.dominoListDraw);
				// d = Console.dominoListDraw.get(n);
				// Console.dominoListDraw.remove(d);
			// } else {
				//System.out.println("other rounds");
				//Domino pre = Console.RoiToDomino.get(this);
				//int x = Console.getPlayerByKing(this).bestPosition(pre)[1];
				//int y = Console.getPlayerByKing(this).bestPosition(pre)[2];
				//int direction = Console.getPlayerByKing(this).bestPosition(pre)[3];
				//int n = Console.getPlayerByKing(this).chooseBestDomino(pre, x, y, direction, Console.dominoListDraw);
				//d = Console.dominoListDraw.get(n);
				//Console.dominoListDraw.remove(d);
				//System.out.println("AI chose the domino " + d.getNumDomino());
		return d;

			//}
		}
	public Color color() {
		switch (couleur) {
		case "blue":
			return Color.blue;
		case "red":
			return Color.red;
		case "green":
			return Color.green;
		case "pink":
			return Color.pink;
		case "yellow":
			return Color.yellow;
		case "cyan":
			return Color.cyan;
		default:
			return null;
		}
	}
}
