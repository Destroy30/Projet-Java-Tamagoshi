 package tamagoshi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Cette classe va permettre de récupérer des valeurs saisies au clavier par l'utilisateur
 */

public class Utilisateur {
	
	/**
	 * Cette méthode va permettre de récupérer une entrée clavier
	 * @return Une représentation en String de ce qui a été tapé dans l'entrée standard
	 */

  public static String saisieClavier(){

    /*il faut gérer les exceptions car l'entrée standard 
    peut ne pas être disponible : le constructeur de la 
    classe InputStreamReader peut renvoyer une exception.*/
    try{ 
      BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
      return clavier.readLine();
    }
    catch(IOException e){
      e.printStackTrace();
      System.exit(0);
      return null;
    }
  }
}

