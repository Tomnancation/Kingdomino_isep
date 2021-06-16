import java.util.*;
import java.io.*;
import org.newdawn.slick.*;

import org.newdawn.slick.*;


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
	
	public Domino draw() {
		Domino d = null;
		Console.printDominoList(Console.dominoListDraw);
		boolean dominoOk = false;
			do {
				try {
					System.out.println("choisissez un domino");
					int n = Console.saisirInt();
					d = Console.dominoListDraw.get(n);  // on prend un domino
					Console.dominoListDraw.remove(d);  // on retire un domino de la pioche
					dominoOk = true;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (!dominoOk);
			return d;	
		}
	public Color color() {
		switch (couleur) {
		case "bleu":
			return Color.blue;
		case "rouge":
			return Color.red;
		case "vert":
			return Color.green;
		case "rose":
			return Color.pink;
		case "jaune":
			return Color.yellow;
		case "cyan":
			return Color.cyan;
		default:
			return null;
		}
	}
	public void render(Graphics graphics, float x, float y) {
		roi.draw(x,y,Jeu.dominoWidth*1.5f,Jeu.dominoWidth*1.5f,color());
	}
}
