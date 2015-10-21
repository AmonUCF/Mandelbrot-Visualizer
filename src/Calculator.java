import java.awt.Color;
import java.awt.Graphics2D;


public class Calculator implements Runnable{
	
	private static final int MAX_ITERS = 500;
	public int W, H, SCALE=1000;
	
	public double dX, dY;
	public ComplexNumber offset;
	
	private static long pixelsPerUnit;
	
	private static ComplexNumber c = new ComplexNumber(0,0);
	private static ComplexNumber Z = new ComplexNumber(0,0);
	
	private static double smoothColor;
	public boolean needUpdate=true;
	public boolean isSleeping=false;
	public boolean forceRestart=false;
	private Panel panel;
	
	private static double zoomScale=1;
	
	public Calculator (int width, int height, Panel panel) {
		this.panel=panel;
		W=width;
		H=height;
		pixelsPerUnit=(int)Math.round((double)Math.min(W, H) / ((double)6 * zoomScale));
		
		
		dX=dY=0.0;
	}
	
	public void Flood(Panel panel, int pixSize, int level) {
		
		
		int x = W/2-pixSize*level, y = H/2-pixSize*level;
		if (x<0 && y<0) return;
		
		double R = 0, I = 0;
		
		for (int i=0;i<2*level-1;i++) {
			R=PixToWorld(x,W/2,dX);
			I=PixToWorld(y,H/2,dY);
			c.SetImag(I);c.SetReal(R);
			int iters = Iterate(c);
			
		
			if (x>=0&&x<W && y>=0 && y<H) Draw(iters,x,y,pixSize,pixSize);
			x+=pixSize;
		}
		
		for (int i=0;i < 2*level-1;i++) {
			R=PixToWorld(x,W/2,dX);
			I=PixToWorld(y,H/2,dY);
			c.SetImag(I);c.SetReal(R);
			int iters = Iterate(c);
			
			
			if (x>=0&&x<W && y>=0 && y<H) Draw(iters,x,y,pixSize,pixSize);
			y+=pixSize;
		}
		for (int i=2*level-1;i>=0;i--) {
			R=PixToWorld(x,W/2,dX);
			I=PixToWorld(y,H/2,dY);
			c.SetImag(I);c.SetReal(R);
			int iters = Iterate(c);
			
			
			if (x>=0&&x<W && y>=0 && y<H) Draw(iters,x,y,pixSize,pixSize);
			x-=pixSize;
		}
		for (int i=2*level-1;i>=0;i--) {
			R=PixToWorld(x,W/2,dX);
			I=PixToWorld(y,H/2,dY);
			c.SetImag(I);c.SetReal(R);
			int iters = Iterate(c);
			
			
			if (x>=0&&x<W && y>=0 && y<H) Draw(iters,x,y,pixSize,pixSize);
			y-=pixSize;
		}
		
		if (forceRestart) return;
		panel.repaint();
		Flood(panel, pixSize, level+1);
	}
	
	public void Draw(int iter, int x, int y, int w, int h) {
		Graphics2D g = Panel.getImage().createGraphics();

		Color c = new Color(Color.HSBtoRGB((float)(.55f+10*smoothColor), .85f, 1f));
		
		if (iter==MAX_ITERS) c = Color.BLACK;
		g.setColor(c);
		g.fillRect(x, y, w, h);
		g.dispose();
	}
	
	
	private ComplexNumber JulietSetFunction(ComplexNumber Z, ComplexNumber c){
		Z=Z.multiply(Z);
		return Z.add(c);
	}

	private int Iterate(ComplexNumber c) {
		int itrs = 0;
		Z.SetReal(0);
		Z.SetImag(0);
		
		if (c.GetImag()*c.GetImag()+c.GetReal()*c.GetReal() < .3 && c.GetReal()<.25) return MAX_ITERS;
		smoothColor = Math.exp(-Z.magnitude());
		
		
		while (itrs < MAX_ITERS){
			Z = JulietSetFunction(Z, c);
		
			smoothColor += Math.exp(-Z.magnitude());
			double sum = Z.GetReal()*Z.GetReal()+Z.GetImag()*Z.GetImag();
			
		
			if (sum > 4) break;
			itrs++;
		}
		
		smoothColor/=MAX_ITERS;
		
		return itrs;
	}

	public double PixToWorld(int q, int middle, double offset) {
		
		return offset+((double)(q-middle) / ((double)pixelsPerUnit*zoomScale));
	}
	
	public void setZoom(double z) {
		zoomScale=z;
	}
	public double getZoom(){
		return zoomScale;
	}
	
	public void Refresh() {
		for (int i = 16; i > 0; i/=4)
			Flood(panel, i, 1);
	}

	@Override
	public void run() {
		while (needUpdate){
			forceRestart=false;
			isSleeping=false;
			Refresh();
			if (forceRestart)continue;
			needUpdate=false;
			try {
				synchronized(panel.calcThread){
					isSleeping=true;
					panel.calcThread.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
