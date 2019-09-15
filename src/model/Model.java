/**
 * Pakiet ten zawiera tylko klase <code>Model</code>
 */
package model;
import controller.*;
import view.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

/**
 * @author Artur
 * Klasa implementujaca czesc M w modelu MVC
 * Klasa implementuje dzialanie programu Tetris
 */
public class Model {
	/** Wysokosc planszy */
	public final static int HEIGHT = 30;
	/** Szerokosc planszy */
	public final static int WIDTH = 20;
	private Controller C;
	private View V;
	private int wynik;
	private Timer t;
	private Losownik l;
	private boolean[][] next;
	private Plansza plansza;
	private final static int STEP = 200;
	/** Konstruktor klasy
	 * @param v wskazanie na obiekt View
	 */
	public Model(View v)
	{
		V = v;
		t = new Timer(STEP, new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						down();
					}
				});
		l = new Losownik();
		next = new boolean[2][];
		next[0] = new boolean[4];
		next[1] = new boolean[4];
	}
	/** Metoda ustawia wskazanie na kontroler
	 * @param c wskazanie na kontroler
	 */
	public void setController(Controller c)
	{
		C = c;
	}
	/** Metoda uruchamia gre */
	public void start()
	{
		next = l.losuj();
		plansza = new Plansza();
		plansza.wstaw();
		next = l.losuj();
		wynik = 0;
		t.start();
		V.show(plansza.getGrupa(), plansza.getKlocek(), next, wynik);
	}
	/** Metoda wstrzymuje gre, metoda ta jest wywolywana w przypadku nacisniecia przycisku STOP podczas trwania gry */
	public void stop()
	{
		t.stop();
	}
	public void wznow()
	{
		t.start();
	}
	/** Metoda przemieszcza klocek w lewo lub w prawo
	 * @param x jesli x == Controler.LEFT klocek przemieszczmy w lewo, jesli x == Controler.RIGHT klocek przemieszczamy w prawo */
	public void move(int x)
	{
		plansza.move(x);
		V.show(plansza.getGrupa(), plansza.getKlocek(), next, wynik);
	}
	/** Metoda upuszczajaca klocek */
	public void drop()
	{
		plansza.drop();
		plansza.check();
		plansza.wstaw();
		next = l.losuj();
		V.show(plansza.getGrupa(), plansza.getKlocek(), next, wynik);
	}
	/** Metoda obracajaca klocek */
	public void turn()
	{
		plansza.turn();
		V.show(plansza.getGrupa(), plansza.getKlocek(), next, wynik);
	}
	/** Metoda opuszczajaca klocek o 1 w dol, wywolywana jest regularnie przez Timer t */
	public void down()
	{
		if (plansza.down() == false)
		{
			plansza.check();
			plansza.wstaw();
			next = l.losuj();
		}
		V.show(plansza.getGrupa(), plansza.getKlocek(), next, wynik);
	}
	/** Klasa losujaca nastepny klocek */
	class Losownik
	{
		private boolean[][][] klocki;
		private int LICZBA_KLOCKOW = 7;
		public Losownik()
		{
			klocki = new boolean[LICZBA_KLOCKOW][][];
			boolean[][] klocek = new boolean[2][];
			klocek[0] = new boolean[] {false, false, false, false};
			klocek[1] = new boolean[] {true, true, true, true};
			klocki[0] = klocek;
			klocek = new boolean[2][];
			klocek[0] = new boolean[] {true, false, false, false};
			klocek[1] = new boolean[] {true, true, true, false};
			klocki[1] = klocek;
			klocek = new boolean[2][];
			klocek[0] = new boolean[] {false, true, false, false};
			klocek[1] = new boolean[] {true, true, true, false};
			klocki[2] = klocek;
			klocek = new boolean[2][];
			klocek[0] = new boolean[] {true, true, false, false};
			klocek[1] = new boolean[] {false, true, true, false};
			klocki[3] = klocek;
			klocek = new boolean[2][];
			klocek[0] = new boolean[] {true, true, false, false};
			klocek[1] = new boolean[] {true, true, false, false};
			klocki[4] = klocek;
			klocek = new boolean[2][];
			klocek[0] = new boolean[] {false, false, true, false};
			klocek[1] = new boolean[] {true, true, true, false};
			klocki[5] = klocek;
			klocek = new boolean[2][];
			klocek[0] = new boolean[] {false, true, true, false};
			klocek[1] = new boolean[] {true, true, false, false};
			klocki[6] = klocek;
		}
		public boolean[][] losuj()
		{
			Random generator = new Random();
			boolean[][] wylosowany = klocki[generator.nextInt(LICZBA_KLOCKOW)];
			boolean[][] wynik = new boolean[2][];
			wynik[0] = wylosowany[0].clone();
			wynik[1] = wylosowany[1].clone();
			return wynik;
		}
	}
	/** Klasa implementujaca plansze gry */
	class Plansza
	{
		private boolean[][] klocek;
		private boolean[][] grupa;
		private int x;
		private int y;
		public Plansza()
		{
			klocek = new boolean[HEIGHT][];
			for (int i = 0; i < klocek.length; i++) klocek[i] = new boolean[WIDTH];
			for (int i = 0; i < klocek.length; i++)
				for (int j = 0; j < klocek[i].length; j++) klocek[i][j] = false;
			grupa = new boolean[HEIGHT][];
			for (int i = 0; i < grupa.length; i++) grupa[i] = new boolean[WIDTH];
			for (int i = 0; i < grupa.length; i++)
				for (int j = 0; j < grupa[i].length; j++) grupa[i][j] = false;
		}
		public void wstaw()
		{
			for (int i = 0; i < klocek.length; i++)
				for (int j = 0; j < klocek[i].length; j++) klocek[i][j] = false;
			y = 1;
			x = WIDTH / 2 - 1;
			klocek[y - 1][x - 1] = next[0][0];
			klocek[y - 1][x] = next[0][1];
			klocek[y - 1][x + 1] = next[0][2];
			klocek[y - 1][x + 2] = next[0][3];
			klocek[y][x - 1] = next[1][0];
			klocek[y][x] = next[1][1];
			klocek[y][x + 1] = next[1][2];
			klocek[y][x + 2] = next[1][3];
			for (int i = 0; i < klocek.length; i++)
				for (int j = 0; j < klocek[i].length; j++)
					if (klocek[i][j] == true && grupa[i][j] == true)
					{
						t.stop();
						C.lost(wynik);
					}
		}
		public void drop()
		{
			int[] tab = new int[WIDTH];
			for (int i = 0; i < WIDTH; i++)
			{
				tab[i] = -1;
				for (int j = 0; j < HEIGHT; j++)
				{
					if (klocek[j][i] == true) tab[i] = j;
				}
			}
			int[] odleglosci = new int[WIDTH];
			for (int i = 0; i < WIDTH; i++)
			{
				if (tab[i] != -1) odleglosci[i] = HEIGHT - tab[i] - 1;
				else odleglosci[i] = Integer.MAX_VALUE;
			}
			for (int i = 0; i < WIDTH; i++)
			{
				int x = HEIGHT;
				for (int j = 0; j < HEIGHT; j++)
				{
					if (grupa[j][i] == true)
					{
						x = j;
						break;
					}
				}
				if (tab[i] != -1 && x < HEIGHT) odleglosci[i] = x - tab[i] - 1;
			}
			int odstep = HEIGHT;
			for (int i = 0; i < WIDTH; i++)
			{
				if (odleglosci[i] < odstep) odstep = odleglosci[i];
			}
			for (int i = 0; i < HEIGHT; i++)
			{
				for (int j = 0; j < WIDTH; j++)
				{
					if (klocek[i][j] == true) grupa[i + odstep][j] = true;
				}
			}
		}
		public void move(int ster)
		{
			if (ster == Controller.LEFT)
			{
				boolean flag = false;
				for (int i = 0; i < HEIGHT; i++)
				{
					if (klocek[i][0] == true)
					{
						flag = true;
						break;
					}
				}
				if (flag == true) return;
				boolean[][] temp = new boolean[HEIGHT][];
				for (int i = 0; i < temp.length; i++) temp[i] = new boolean[WIDTH];
				for (int i = 0; i < temp.length; i++)
					for (int j = 0; j < temp[i].length; j++) temp[i][j] = false;
				for (int i = 0; i < klocek.length; i++)
					for (int j = 0; j < klocek[i].length; j++)
					{
						if (klocek[i][j] == true) temp[i][j - 1] = true;
					}
				flag = false;
				for (int i = 0; i < HEIGHT; i++)
					for (int j = 0; j < WIDTH; j++)
					{
						if (temp[i][j] == true && grupa[i][j] == true)
						{
							flag = true;
						}
					}
				if (flag == false)
				{
					klocek = temp;
					x--;
				}
			}
			else
			{
				boolean flag = false;
				for (int i = 0; i < HEIGHT; i++)
				{
					if (klocek[i][WIDTH - 1] == true)
					{
						flag = true;
						break;
					}
				}
				if (flag == true) return;
				boolean[][] temp = new boolean[HEIGHT][];
				for (int i = 0; i < temp.length; i++) temp[i] = new boolean[WIDTH];
				for (int i = 0; i < temp.length; i++)
					for (int j = 0; j < temp[i].length; j++) temp[i][j] = false;
				for (int i = 0; i < klocek.length; i++)
					for (int j = 0; j < klocek[i].length; j++)
					{
						if (klocek[i][j] == true) temp[i][j + 1] = true;
					}
				flag = false;
				for (int i = 0; i < HEIGHT; i++)
					for (int j = 0; j < WIDTH; j++)
					{
						if (temp[i][j] == true && grupa[i][j] == true)
						{
							flag = true;
						}
					}
				if (flag == false)
				{
					klocek = temp;
					x++;
				}
			}
		}
		public boolean down()
		{
			boolean flag = false;
			for (int i = 0; i < WIDTH; i++)
			{
				if (klocek[HEIGHT - 1][i] == true)
				{
					flag = true;
					break;
				}
			}
			if (flag == true)
			{
				for (int i = 0; i < HEIGHT; i++)
				{
					for (int j = 0; j < WIDTH; j++)
					{
						if (klocek[i][j] == true) grupa[i][j] = true;
					}
				}
				return false;
			}
			boolean[][] temp = new boolean[HEIGHT][];
			for (int i = 0; i < temp.length; i++) temp[i] = new boolean[WIDTH];
			for (int i = 0; i < temp.length; i++)
				for (int j = 0; j < temp[i].length; j++) temp[i][j] = false;
			for (int i = 0; i < klocek.length; i++)
				for (int j = 0; j < klocek[i].length; j++)
				{
					if (klocek[i][j] == true) temp[i + 1][j] = true;
				}
			flag = false;
			for (int i = 0; i < HEIGHT; i++)
				for (int j = 0; j < WIDTH; j++)
				{
					if (temp[i][j] == true && grupa[i][j] == true)
					{
						flag = true;
					}
				}
			if (flag == false)
			{
				klocek = temp;
				y++;
				return true;
			}
			else
			{
				for (int i = 0; i < HEIGHT; i++)
				{
					for (int j = 0; j < WIDTH; j++)
					{
						if (klocek[i][j] == true) grupa[i][j] = true;
					}
				}
				return false;
			}
		}
		public void turn()
		{
			boolean[][] temp = new boolean[HEIGHT][];
			for (int i = 0; i < temp.length; i++) temp[i] = new boolean[WIDTH];
			for (int i = 0; i < temp.length; i++)
				for (int j = 0; j < temp[i].length; j++) temp[i][j] = false;
			for (int i = 0; i < HEIGHT; i++)
			{
				for (int j = 0; j < WIDTH; j++)
				{
					if (klocek[i][j] == true)
					{
						int xTemp = y - i + x;
						int yTemp = j - x + y;
						if (xTemp < 0 || xTemp >= WIDTH || yTemp < 0 || yTemp >= HEIGHT) return;
						temp[yTemp][xTemp] = true;
					}
				}
			}
			boolean flag = false;
			for (int i = 0; i < HEIGHT; i++)
				for (int j = 0; j < WIDTH; j++)
				{
					if (temp[i][j] == true && grupa[i][j] == true)
					{
						flag = true;
					}
				}
			if (flag == false)
			{
				klocek = temp;
			}
		}
		public void check()
		{
			for (int i = HEIGHT - 1; i >= 0; i--)
			{
				boolean flag = true;
				for (int j = 0; j < WIDTH; j++)
				{
					if (grupa[i][j] == false)
					{
						flag = false;
						break;
					}
				}
				if (flag == true)
				{
					for (int a = i - 1; a >= 0; a--)
					{
						for (int b = 0; b < WIDTH; b++) grupa[a + 1][b] = grupa[a][b];
					}
					for (int a = 0; a < WIDTH; a++) grupa[0][a] = false;
					i++;
					wynik += 10;
				}
			}
		}
		public boolean[][] getKlocek()
		{
			boolean[][] wynik = new boolean[klocek.length][];
			for (int i = 0; i < klocek.length; i++)
			{
				wynik[i] = klocek[i].clone();
			}
			return wynik;
		}
		public boolean[][] getGrupa()
		{
			boolean[][] wynik = new boolean[grupa.length][];
			for (int i = 0; i < grupa.length; i++)
			{
				wynik[i] = grupa[i].clone();
			}
			return wynik;
		}
	}
}
