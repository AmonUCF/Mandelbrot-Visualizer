
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;




public class ScreenListener implements MouseListener, KeyListener{

	Calculator calc;
	Panel panel;
	long lastTimeClicked = Integer.MIN_VALUE;
	int lastButton = -1;
	
	public ScreenListener(Panel panel) {
		calc = panel.calc;
		this.panel = panel;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		long time = System.nanoTime();
		int button = e.getButton();
		if (time-lastTimeClicked < 200000000 && button == lastButton) {
			Panel.menu=!Panel.menu;
			return;
		}
		lastButton=button;
		lastTimeClicked=time;
		
		if (e.getButton()==MouseEvent.BUTTON3 && calc.getZoom()<=1){calc.setZoom(1); return;}
		
		double r = calc.PixToWorld(e.getX(), calc.W/2, calc.dX);
		double i = calc.PixToWorld(e.getY(), calc.H/2, calc.dY);
		
		if (e.getButton()==MouseEvent.BUTTON1)
			calc.setZoom(calc.getZoom()*2);
		else if (e.getButton()==MouseEvent.BUTTON3 )
			calc.setZoom(calc.getZoom()/2);
		else if (e.getButton()==MouseEvent.BUTTON2) {
			try {
				
				String a = String.valueOf(calc.dX);
				String b = String.valueOf(calc.dY);
				String c = String.valueOf(calc.getZoom());
				ImageIO.write(Panel.getImage(), "png", new File(a+"-"+b+"-"+c+".png"));
			} catch (IOException ex) {
				
			}
			return;
			
		}
		
		calc.dX=r;
		calc.dY=i;
		
		
		if (calc.getZoom()==1) calc.dX=calc.dY=0;
		
		calc.needUpdate=true;
	
		synchronized(panel.calcThread){
			if (!calc.isSleeping){
				calc.forceRestart=true;
			}
			else
				panel.calcThread.notify();
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getKeyCode());
		if (e.getKeyCode()==KeyEvent.VK_M) {
			Panel.menu=!Panel.menu;
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getKeyCode());
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}	
