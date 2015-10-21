

public class ComplexNumber {
	private double real, imag;
	
	public ComplexNumber(double a, double b){
		real=a;
		imag=b;
	}
	
	
	public ComplexNumber multiply(ComplexNumber c){
		
		double tempR =this.real*c.real-this.imag*c.imag;
		double tempI =this.real*c.imag+this.imag*c.real;
		this.real = tempR;
		this.imag = tempI;
		return this;
	}
	
	
	
	public ComplexNumber add(ComplexNumber c){
		
		this.real+=c.real;
		this.imag+=c.imag;
		return this;
	}
	

	
	public double GetReal(){
		return real;
	}
	public double GetImag(){
		return imag;
	}
	
	public double magnitude() {
		double x = this.GetReal(), y = this.GetImag();
		return Math.sqrt(x*x+y*y);
	}
	public void SetReal(double r) {
		this.real=r;
	}
	public void SetImag(double i){
		this.imag=i;
	}
}
