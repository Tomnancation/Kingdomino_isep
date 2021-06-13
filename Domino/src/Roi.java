import java.util.*;
import java.io.*;
import org.newdawn.slick.*;

import org.newdawn.slick.*;

//1.为什么要定义ID_Roi=1
public class Roi {
	private int id;
	private String couleur;
	static int ID_Roi = 1;
	
	public static Image roi;
	
	public Roi() {
	}

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

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	/**
	 *1.printDominoList，dominoListDraw，getJoueurByRoi都是关于console
	 *2.dominoOk是什么东西 
	 *3.59行有关于AI的代码*/
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
		}  else { // AI
			 if (Console.tour == 1) {
				 int n = Console.getJoueurByRoi(this).chooseBestDominoTurn1(Console.dominoListDraw);
				 d = Console.dominoListDraw.get(n);
				 Console.dominoListDraw.remove(d);
			 } else {
				System.out.println("other rounds");
				Domino pre = Console.RoiToDomino.get(this);
				int x = Console.getJoueurByRoi(this).bestPosition(pre)[1];
				int y = Console.getJoueurByRoi(this).bestPosition(pre)[2];
				int direction = Console.getJoueurByRoi(this).bestPosition(pre)[3];
				int n = Console.getJoueurByRoi(this).chooseBestDomino(pre, x, y, direction, Console.dominoListDraw);
				d = Console.dominoListDraw.get(n);
				Console.dominoListDraw.remove(d);
				System.out.println("AI chose the domino " + d.getNumDomino());
			 }
		}
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
	public void render(Graphics graphics, float x, float y) {
		// graphics.fillRect(this.x, this.y, 200, 200);
		//graphics.drawImage(king, x, y,(int)(Game.dominoWidth),(int)(Game.dominoWidth), y, y, color());
		roi.draw(x,y,Jeu.dominoWidth*1.5f,Jeu.dominoWidth*1.5f,color());
	}
}
