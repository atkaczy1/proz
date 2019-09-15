/**
 * Pakiet do przychowywania klasy <code>Controller</code>
 */
package controller;
import view.*;
import model.*;

/**
 * @author Artur
 * Klasa s�u�y do sterowania dzia�ania programem
 */
public class Controller {
	private Model M;
	private View V;
	private boolean zatrzymany;
	/** Sta�a reprezentuj�ca wci�ni�ty klawisz lewo */
	public final static int LEFT = 0;
	/** Sta�a reprezentuj�ca wci�ni�ty klawisz prawo */
	public final static int RIGHT = 1;
	/** Sta�a reprezentuj�ca wci�ni�ty klawisz d� */
	public final static int DOWN = 2;
	/** Sta�a reprezentuj�ca wci�ni�ty klawisz obracania klocka */
	public final static int TURN = 3;
	/** Jedyny konstruktor tej klasy
	 * Jako argumenty wywo�ania przyjmuje obiekty klas View i Model w celu dostarczenia klasie wskaza� na pozosta�e elementu modelu MVC
	 * @param v wskazanie na View
	 * @param m wskazanie na Model
	 */
	public Controller(View v, Model m)
	{
		M = m;
		V = v;
		zatrzymany = false;
	}
	/**  
	 * Metoda uzupe�niaj�ca wskazania w modelu MVC i rozpoczynaj�ca dzia�anie programu
	 */
	public void start()
	{
		V.setController(this);
		M.setController(this);
		V.wlMenu();
	}
	/** Metoda wywo�ywana przez Model w celu poinformowania kontrolera o przegranej grze, a tak�e o ostatecznym wyniku
	 * @param x ostateczny wynik gry 
	 * */
	public void lost(int x)
	{
		V.wylPlansza();
		V.wlResult(x);
	}
	/** Metoda wywo�ywana przez View w celu poinformowania kontrolera o wci�ni�tym klawiszu
	 * @param x kod wci�ni�tego klawisza : LEFT, RIGHT, DOWN, TURN
	 *  */
	public void pressed(int x)
	{
		if (zatrzymany == true) return;
		if (x == LEFT || x == RIGHT) M.move(x);
		else if (x == DOWN) M.drop();
		else M.turn();
	}
	/** Metoda wywo�ywana przez View w przypadku wci�ni�cia przycisku STOP podczas dzia�ania programu, powoduje powr�t do menu programu */
	public void stopToMenu()
	{
		V.wylPlansza();
		M.stop();
		V.wlMenu();
	}
	/** Metoda wywo�ywana przez View w przypadku wci�ni�cia przycisku powrotu do menu podczas wy�wietlania ko�cowego rezultatu gry, powoduje powr�t do menu */
	public void retMenu()
	{
		V.wylResult();
		V.wlMenu();
	}
	/** Metoda wywo�ywana po wci�ni�ciu przycisku Uruchom w menu, powoduje rozpocz�cie gry */
	public void run()
	{
		V.wylMenu();
		V.wlPlansza();
		M.start();
	}
	public void stop()
	{
		M.stop();
		zatrzymany = true;
	}
	public void wznow()
	{
		M.wznow();
		zatrzymany = false;
	}
}
