/**
 * Pakiet do przychowywania klasy <code>Controller</code>
 */
package controller;
import view.*;
import model.*;

/**
 * @author Artur
 * Klasa s³u¿y do sterowania dzia³ania programem
 */
public class Controller {
	private Model M;
	private View V;
	private boolean zatrzymany;
	/** Sta³a reprezentuj¹ca wciœniêty klawisz lewo */
	public final static int LEFT = 0;
	/** Sta³a reprezentuj¹ca wciœniêty klawisz prawo */
	public final static int RIGHT = 1;
	/** Sta³a reprezentuj¹ca wciœniêty klawisz dó³ */
	public final static int DOWN = 2;
	/** Sta³a reprezentuj¹ca wciœniêty klawisz obracania klocka */
	public final static int TURN = 3;
	/** Jedyny konstruktor tej klasy
	 * Jako argumenty wywo³ania przyjmuje obiekty klas View i Model w celu dostarczenia klasie wskazañ na pozosta³e elementu modelu MVC
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
	 * Metoda uzupe³niaj¹ca wskazania w modelu MVC i rozpoczynaj¹ca dzia³anie programu
	 */
	public void start()
	{
		V.setController(this);
		M.setController(this);
		V.wlMenu();
	}
	/** Metoda wywo³ywana przez Model w celu poinformowania kontrolera o przegranej grze, a tak¿e o ostatecznym wyniku
	 * @param x ostateczny wynik gry 
	 * */
	public void lost(int x)
	{
		V.wylPlansza();
		V.wlResult(x);
	}
	/** Metoda wywo³ywana przez View w celu poinformowania kontrolera o wciœniêtym klawiszu
	 * @param x kod wciœniêtego klawisza : LEFT, RIGHT, DOWN, TURN
	 *  */
	public void pressed(int x)
	{
		if (zatrzymany == true) return;
		if (x == LEFT || x == RIGHT) M.move(x);
		else if (x == DOWN) M.drop();
		else M.turn();
	}
	/** Metoda wywo³ywana przez View w przypadku wciœniêcia przycisku STOP podczas dzia³ania programu, powoduje powrót do menu programu */
	public void stopToMenu()
	{
		V.wylPlansza();
		M.stop();
		V.wlMenu();
	}
	/** Metoda wywo³ywana przez View w przypadku wciœniêcia przycisku powrotu do menu podczas wyœwietlania koñcowego rezultatu gry, powoduje powrót do menu */
	public void retMenu()
	{
		V.wylResult();
		V.wlMenu();
	}
	/** Metoda wywo³ywana po wciœniêciu przycisku Uruchom w menu, powoduje rozpoczêcie gry */
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
