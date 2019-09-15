/**
 * Pakiet <code>start</code> zawiera tylko jedn¹ klasê <code>Start</code>
 */
package start;
import controller.*;
import view.*;
import model.*;

/**
 * @author Artur
 * Klasa ta ma za zadanie jedynie utworzyæ obiekty M, V, C 
 */
public class Start {

	/**
	 * @param args Nie u¿ywane
	 * 
	 */
	public static void main(String[] args) {
		View V = new View();
		Model M = new Model(V);
		Controller C = new Controller(V, M);
		C.start();
	}

}
