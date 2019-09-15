/**
 * Pakiet ten posiada jedynie klasê <code>View</code>
 */
package view;
import controller.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * @author Artur
 * Klasa zajmuj¹ca siê interfejsem graficznym, odpowiednik V w modelu MVC
 */
public class View extends JFrame {
	private Controller C;
	private Menu M;
	private Plansza P;
	private Result R;
	/** Jedyny konstruktor tej klasy, tworzy obiekty klas Menu, Plansza, Result
	 *  
	 */
	public View()
	{
		M = new Menu();
		P = new Plansza();
		R = new Result();
	}
	/** Metoda ustawiaj¹ca wskazanie na kontroler w celu stworzenia modelu MVC
	 *  @param c wskazanie do kontrolera do którego bêdzie przypiêty obiekt View
	 */
	public void setController(Controller c)
	{
		C = c;
	}
	/** Metoda, która wyœwietla na planszy nowy stan gry
	 *  @param grupa tablica dwuwymiarowa okreœlaj¹ca uk³ad od³o¿onych wczeœniej klocków
	 *  @param k tablica dwuwymiarowa okreœlaj¹ca po³o¿enie klocka
	 *  @param n tablica dwuwymiarowa okreœlaj¹ca nastepny klocek
	 *  @param w aktualny wynik
	 */
	public void show(boolean[][] grupa, boolean[][] k, boolean[][] n, int w)
	{
		P.show(grupa, k, n, w);
	}
	/** Metoda wlaczajaca menu */
	public void wlMenu()
	{
		add(M);
		setSize(400, 400);
		setVisible(true);
	}
	/** Metoda wylaczajaca menu */
	public void wylMenu()
	{
		setVisible(false);
		remove(M);
	}
	/** Metoda wlaczajaca plansze */
	public void wlPlansza()
	{
		add(P);
		setSize(400, 400);
		setVisible(true);
	}
	/** Metoda wylaczajaca plansze */
	public void wylPlansza()
	{
		setVisible(false);
		remove(P);
	}
	/** Metoda wlaczajaca widok z wynikiem */
	public void wlResult(int wynik)
	{
		R.fill(wynik);
		add(R);
		setSize(400, 400);
		setVisible(true);
	}
	/** Metoda wylaczajaca widok z wynikiem */
	public void wylResult()
	{
		setVisible(false);
		remove(R);
	}
	/** Panel implementujacy menu programu */
	private class Menu extends JPanel
	{
        public Menu()
        {
            setLayout(new GridLayout(2, 1));
            JPanel p1 = new JPanel();
            p1.add(new JLabel("TETRIS"));
            JPanel p2 = new JPanel();
            JButton button1 = new JButton("Uruchom");
            button1.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        C.run();
                    }
                });
            p2.add(button1);
            add(p1);
            add(p2);
        }
	}
	/** Panel implementujacy ramke widoczna podczas gry */
	private class Plansza extends JPanel
	{
	    private Widok widok;
	    private Next next;
	    private JLabel wynik;
	    private JButton StartStop;
	    private boolean[][] klocek;
	    private boolean[][] struktura;
	    private boolean[][] nastepny;
	    private final static int NEXT_HEIGHT = 10;
	    private final static int NEXT_WIDTH = 6;
	    public Plansza()
	    {
	    	setLayout(new BorderLayout());
	    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	klocek = new boolean[Model.HEIGHT][];
	    	for (int i = 0; i < klocek.length; i++) klocek[i] = new boolean[Model.WIDTH];
	    	for (int i = 0; i < klocek.length; i++)
	    		for (int j = 0; j < klocek[i].length; j++) klocek[i][j] = false;
	    	struktura = new boolean[Model.HEIGHT][];
	    	for (int i = 0; i < struktura.length; i++) struktura[i] = new boolean[Model.WIDTH];
	    	for (int i = 0; i < struktura.length; i++)
	    		for (int j = 0; j < struktura[i].length; j++) struktura[i][j] = false;
	    	nastepny = new boolean[NEXT_HEIGHT][];
	    	for (int i = 0; i < NEXT_HEIGHT; i++) nastepny[i] = new boolean[NEXT_WIDTH];
	    	for (int i = 0; i < nastepny.length; i++)
	    		for (int j = 0; j < nastepny[i].length; j++) nastepny[i][j] = false;
	        JButton button1 = new JButton("MENU");
            button1.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        C.stopToMenu();
                    }
                });
	        JPanel p1 = new JPanel();
	        p1.add(button1);
	        wynik = new JLabel("");
	        JPanel p2 = new JPanel();
	        p2.add(wynik);
	        widok = new Widok();
	        next = new Next();
	        JPanel pasek = new JPanel();
	        pasek.setLayout(new GridLayout(4,1));
	        next.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	        widok.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	        JPanel p3 = new JPanel();
	        StartStop = new JButton("STOP");
	        StartStop.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                	if (StartStop.getText().equals("STOP"))
                	{
                		StartStop.setText("START");
                		C.stop();
                	}
                	else
                	{
                		StartStop.setText("STOP");
                		C.wznow();
                	}
                }
            });
	        p3.add(StartStop);
	        pasek.add(next);
	        pasek.add(p2);
	        pasek.add(p3);
	        pasek.add(p1);
	        add(pasek, BorderLayout.EAST);
	        add(widok);
	        Action leftAction = new KAction(C.LEFT);
	        Action rightAction = new KAction(C.RIGHT);
	        Action downAction = new KAction(C.DOWN);
	        Action turnAction = new KAction(C.TURN);
	        InputMap imap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	        imap.put(KeyStroke.getKeyStroke("LEFT"), "left");
	        imap.put(KeyStroke.getKeyStroke("DOWN"), "down");
	        imap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
	        imap.put(KeyStroke.getKeyStroke("ENTER"), "turn");
	        ActionMap amap = getActionMap();
	        amap.put("left", leftAction);
	        amap.put("down", downAction);
	        amap.put("right", rightAction);
	        amap.put("turn", turnAction);
	    }
	    
	    public void show(boolean[][] grupa, boolean[][] k, boolean[][] n, int w)
		{
			struktura = grupa;
			klocek = k;
			nastepny[NEXT_HEIGHT / 2 - 1][1] = n[0][0];
			nastepny[NEXT_HEIGHT / 2 - 1][2] = n[0][1];
			nastepny[NEXT_HEIGHT / 2 - 1][3] = n[0][2];
			nastepny[NEXT_HEIGHT / 2 - 1][4] = n[0][3];
			nastepny[NEXT_HEIGHT / 2][1] = n[1][0];
			nastepny[NEXT_HEIGHT / 2][2] = n[1][1];
			nastepny[NEXT_HEIGHT / 2][3] = n[1][2];
			nastepny[NEXT_HEIGHT / 2][4] = n[1][3];
			wynik.setText(w + "");
			widok.repaint();
			next.repaint();
		}
	    /** Panel implementujacy widok aktualnej gry */
	    class Widok extends JPanel
	    {
	    	public void paintComponent(Graphics g)
	    	{
	    		Graphics2D g2 = (Graphics2D) g;
	    		g2.setPaint(Color.WHITE);
	    		Rectangle2D tlo = new Rectangle2D.Double(0, 0, getSize().getWidth(), getSize().getHeight());
	    		g2.fill(tlo);
	    		g2.draw(tlo);
	    		g2.setPaint(Color.BLACK);
	    		for (int i = 0; i < struktura.length; i++)
	    		{
	    			for (int j = 0; j < struktura[i].length; j++)
	    			{
	    				if (struktura[i][j] == true)
	    				{
	    					Rectangle2D rect = new Rectangle2D.Double(j * getSize().getWidth() / Model.WIDTH, i * getSize().getHeight() / Model.HEIGHT, getSize().getWidth() / Model.WIDTH - 2, getSize().getHeight() / Model.HEIGHT - 2);
	    					g2.fill(rect);
	    					g2.draw(rect);
	    				}
	    			}
	    		}
	    		g2.setPaint(Color.RED);
	    		for (int i = 0; i < klocek.length; i++)
	    		{
	    			for (int j = 0; j < klocek[i].length; j++)
	    			{
	    				if (klocek[i][j] == true)
	    				{
	    					Rectangle2D rect = new Rectangle2D.Double(j * getSize().getWidth() / Model.WIDTH, i * getSize().getHeight() / Model.HEIGHT, getSize().getWidth() / Model.WIDTH - 2, getSize().getHeight() / Model.HEIGHT - 2);
	    					g2.fill(rect);
	    					g2.draw(rect);
	    				}
	    			}
	    		}
	    	}
	    }
	    /** Panel implementujacy widok na nastepny klocek */
	    class Next extends JPanel
	    {
	    	public void paintComponent(Graphics g)
	    	{
	    		Graphics2D g2 = (Graphics2D) g;
	    		g2.setPaint(Color.WHITE);
	    		Rectangle2D tlo = new Rectangle2D.Double(0, 0, getSize().getWidth(), getSize().getHeight());
	    		g2.fill(tlo);
	    		g2.draw(tlo);
	    		g2.setPaint(Color.RED);
	    		for (int i = 0; i < nastepny.length; i++)
	    		{
	    			for (int j = 0; j < nastepny[i].length; j++)
	    			{
	    				if (nastepny[i][j] == true)
	    				{
	    					Rectangle2D rect = new Rectangle2D.Double(j * getSize().getWidth() / NEXT_WIDTH, i * getSize().getHeight() / NEXT_HEIGHT, getSize().getWidth() / NEXT_WIDTH - 2, getSize().getHeight() / NEXT_HEIGHT - 2);
	    					g2.fill(rect);
	    					g2.draw(rect);
	    				}
	    			}
	    		}
	    	}
	    }
	}
	/** Panel pokazujacy uzyskany wynik po zakonczonej grze */
	private class Result extends JPanel
	{
		JLabel w;
        public Result()
        {
            setLayout(new GridLayout(2, 1));
            JPanel p1 = new JPanel();
            p1.add(new JLabel("Wynik: "));
            w = new JLabel("");
            p1.add(w);
            JPanel p2 = new JPanel();
            JButton button1 = new JButton("Wroc do menu");
            button1.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        C.retMenu();
                    }
                });
            p2.add(button1);
            add(p1);
            add(p2);
        }
        /** Metoda wpisujaca uzyskany wynik do etykiety */
        public void fill(int x)
        {
        	w.setText(x + "");
        }
	}
	/** Klasa implementujaca akcje w wyniku nacisniecia klawisza */
	class KAction extends AbstractAction
	{
		public KAction(int klawisz)
		{
			putValue("klawisz", klawisz);
		}
		public void actionPerformed(ActionEvent event)
		{
			int klawisz = (int) getValue("klawisz");
			C.pressed(klawisz);
		}
	}
}
