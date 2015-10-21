import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class Mandelbrot extends JFrame{

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private Panel panel;
	private MouseListener mouse;
	
	public static Dimension size = new Dimension(1000,750);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mandelbrot window = new Mandelbrot();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Mandelbrot() {
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setPreferredSize(size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Panel(size.width, size.height);
		mouse = new ScreenListener(panel);
		frame.getContentPane().add(panel);
		frame.setTitle("Mandelbrot Visualizer - Amon Al-Khatib");
		panel.addMouseListener(mouse);
		frame.setResizable(false);
		frame.pack();
	}

}
