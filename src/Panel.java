import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static BufferedImage image;
	public Calculator calc;
	public Thread calcThread;
	public static boolean menu = false;
	public static BufferedImage getImage() {
		return image;
	}

	public Panel(int width, int height) {
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	
		clearImage(image);
		
		calc = new Calculator(width, height, this);
		
		calcThread = new Thread(calc);
		calcThread.start();
	}
	
	private void clearImage(BufferedImage image) {
		Graphics2D g = image.createGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
	}
	
	public void repaint() {
		
		Graphics g = getGraphics();
		if (g==null)return;
		
		Graphics g2 = image.getGraphics();
		
		if(!menu){
			g2.setFont(new Font("TimesRoman",Font.PLAIN, 12));
			DrawText(g2, "Right Click - Zoom in", 20, 20);
			DrawText(g2, "Left Click - Zoom out", 20, 35);
			DrawText(g2, "Double Click - More Info", 20, 50);
		}
		else {
			g2.setFont(new Font("TimesRoman",Font.PLAIN, 20));
			DrawText(g2,"X: "+String.valueOf(calc.dX)
					.substring(0, Math.min(String.valueOf(calc.dX).length(), 11)), 20,20);
			DrawText(g2,"Y: "+String.valueOf(calc.dY)
					.substring(0, Math.min(String.valueOf(calc.dY).length(), 11)), 20,40);
			DrawText(g2,"Magnification: "+String.valueOf((int)calc.getZoom())+"x",20,60);
		}
		g.drawImage(image, 0, 0, null);
	}
	
	private void DrawText(Graphics g, String s , int x, int y) {
		
		g.setColor(Color.BLACK);
		g.drawString(s, x-1, y-1);
		g.drawString(s, x-1, y+1);
		g.drawString(s, x+1, y-1);
		g.drawString(s, x+1, y+1);
		g.setColor(Color.white);
		g.drawString(s, x, y);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

}
