package grapher.fc;

public class Test {
	public static void main(String argv[]) {
		for(String expression : argv) {
			Function f = FunctionFactory.createFunction(expression);
			System.out.println("x, " + f);
			for(double x = 0.; x <= 1.; x += .1) {
				System.out.println(x + ", " + f.y(x));
			}
		}
	}
}