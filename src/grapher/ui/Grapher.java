package grapher.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import java.util.Vector;

import static java.lang.Math.*;

import grapher.fc.*;


public class Grapher extends JPanel implements MouseListener{
	
	enum State {UP , D_L , D_R, DRAG_L , DRAG_R}
	State state =State.UP ;
	static final int MARGIN = 40;
	static final int STEP = 5;

	static final BasicStroke dash = new BasicStroke(1, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,1.f,new float[] { 4.f, 4.f },0.f);

	int W = 400;
	int H = 300;
	
	double xmin, xmax;
	double ymin, ymax;

	
	
	Vector<Function> functions;
	
	Listener listener;
	
	
	public Grapher() {
		super();
		xmin = -PI/2.; xmax = 3*PI/2;
		ymin = -1.5;   ymax = 1.5;
		
		functions = new Vector<Function>();	
		listener=new Listener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
	}	
	
	
//************************************
	public class Listener extends MouseInputAdapter implements MouseWheelListener{
		
		Point left=null;
		Point right=null;
		Rectangle zone=null;
		
		
		
		public void mousePressed(MouseEvent e){
			
			left=null;
			right=null;
			
			switch(state){
				case UP:
					switch(e.getButton()){
			
						case MouseEvent.BUTTON1: left=e.getPoint();
							state= State.D_L;
							break;
						case MouseEvent.BUTTON3: right=e.getPoint();
							state= State.D_R;
								 break;
						default : throw new RuntimeException();
							
			}
//				default : throw new RuntimeException();
			}
		}
		
		public void mouseDragged(MouseEvent e){
			
			Point mouse=e.getPoint();
			
			switch(state){
			case D_L:
				translate(mouse.x-left.x,left.y-mouse.y);
				left = mouse;
				break;
			case D_R:
				zone = new Rectangle(right);				 
				zone.add(mouse);
				repaint();  
				break;
			default : throw new RuntimeException();
			}
			
			
		}
		
		public void mouseReleased(MouseEvent e){
			switch(state){
			case D_L:
				state=State.UP;
				break;
			case D_R:
				zoom(e.getPoint(),20);
				 break;
			default : throw new RuntimeException();	 
				}
		}
		
		public void paint(Graphics2D g2) { 
			if(zone !=null){
				g2.draw(zone);
			}	
		}
		
		public void mouseWheelMoved(MouseWheelEvent e){
			int rot=e.getWheelRotation();
			Point mouse=e.getPoint();
			zoom(mouse,rot);
		}
		
		public void mouseMoved(MouseEvent e) {
			if (e.getPoint() != null)
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
			    else
			    	setCursor(Cursor.getDefaultCursor());
			      }

	}

		  

	public void add(String expression) {
		add(FunctionFactory.createFunction(expression));
	}
	
	public void add(Function function) {
		functions.add(function);
		repaint();
	}
		
	public Dimension getPreferredSize() { return new Dimension(W, H); }
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		W = getWidth();
		H = getHeight();

		Graphics2D g2 = (Graphics2D)g;

		// background
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, W, H);
		
		g2.setColor(Color.BLACK);
		listener.paint(g2);
		// box
		g2.translate(MARGIN, MARGIN);
		W -= 2*MARGIN;
		H -= 2*MARGIN;
		if(W < 0 || H < 0) { 
			return; 
		}
		
		g2.drawRect(0, 0, W, H);
		
		g2.drawString("x", W, H+10);
		g2.drawString("y", -10, 0);
		
	
		// plot
		g2.clipRect(0, 0, W, H);
		g2.translate(-MARGIN, -MARGIN);

		// x values
		final int N = W/STEP + 1;
		final double dx = dx(STEP);
		double xs[] = new double[N];
		int    Xs[] = new int[N];
		for(int i = 0; i < N; i++) {
			double x = xmin + i*dx;
			xs[i] = x;
			Xs[i] = X(x);
		}
		
		for(Function f : functions) {
			// y values
			int Ys[] = new int[N];
			for(int i = 0; i < N; i++) {
				Ys[i] = Y(f.y(xs[i]));
			}
			
			if(functions.lastElement().toString()=="sin(x)"){;
				g2.setColor(Color.BLUE);
				g2.drawPolyline(Xs, Ys, N);
			
			}
			else if(functions.lastElement().toString()=="cos(x)"){
				g2.setColor(Color.RED);
				g2.drawPolyline(Xs, Ys, N);

			}else if(functions.lastElement().toString()=="tan(x)"){
				g2.setColor(Color.ORANGE);
				g2.drawPolyline(Xs, Ys, N);
								}
			else if(functions.lastElement().toString()=="x*x"){
				g2.setColor(Color.CYAN);
				g2.drawPolyline(Xs, Ys, N);
			}
			else if(functions.lastElement().toString()=="x*x*x"){
				g2.setColor(Color.GREEN);
				g2.drawPolyline(Xs, Ys, N);
				}
			
		}
		g2.setColor(Color.black);
		g2.setClip(null);

		// axes
		drawXTick(g2, 0);
		drawYTick(g2, 0);
		
		double xstep = unit((xmax-xmin)/10);
		double ystep = unit((ymax-ymin)/10);

		g2.setStroke(dash);
		for(double x = xstep; x < xmax; x += xstep)  { drawXTick(g2, x); }
		for(double x = -xstep; x > xmin; x -= xstep) { drawXTick(g2, x); }
		for(double y = ystep; y < ymax; y += ystep)  { drawYTick(g2, y); }
		for(double y = -ystep; y > ymin; y -= ystep) { drawYTick(g2, y); }
//		if(functions!=null){
//		functions.remove(functions.firstElement());}
	}
	
//	private Color createRandomColor() { 
//		return new Color((new Double(Math.random() * 128)).intValue() + 128, (new Double(Math.random() * 128)).intValue() + 128, (new Double(Math.random() * 128)).intValue() + 128); 
//		}
	protected double dx(int dX) { return  (double)((xmax-xmin)*dX/W); }
	protected double dy(int dY) { return -(double)((ymax-ymin)*dY/H); }

	protected double x(int X) { return xmin+dx(X-MARGIN); }
	protected double y(int Y) { return ymin+dy((Y-MARGIN)-H); }
	
	protected int X(double x) { 
		int Xs = (int)round((x-xmin)/(xmax-xmin)*W);
		return Xs + MARGIN; 
	}
	protected int Y(double y) { 
		int Ys = (int)round((y-ymin)/(ymax-ymin)*H);
		return (H - Ys) + MARGIN;
	}
		
	protected void drawXTick(Graphics2D g2, double x) {
		if(x > xmin && x < xmax) {
			final int X0 = X(x);
			g2.drawLine(X0, MARGIN, X0, H+MARGIN);
			g2.drawString((new Double(x)).toString(), X0, H+MARGIN+15);
		}
	}
	
	protected void drawYTick(Graphics2D g2, double y) {
		if(y > ymin && y < ymax) {
			final int Y0 = Y(y);
			g2.drawLine(0+MARGIN, Y0, W+MARGIN, Y0);
			g2.drawString((new Double(y)).toString(), 5, Y0);
		}
	}
	
	protected static double unit(double w) {
		double scale = pow(10, floor(log10(w)));
		w /= scale;
		if(w < 2)      { w = 2; } 
		else if(w < 5) { w = 5; }
		else           { w = 10; }
		return w * scale;
	}
	

	protected void translate(int dX, int dY) {
		double dx = dx(dX);
		double dy = dy(dY);
		xmin -= dx; xmax -= dx;
		ymin -= dy; ymax -= dy;
		repaint();	
	}
	
	protected void zoom(Point center, int dz) {
		double x = x(center.x);
		double y = y(center.y);
		double ds = exp(dz*.01);
		xmin = x + (xmin-x)/ds; xmax = x + (xmax-x)/ds;
		ymin = y + (ymin-y)/ds; ymax = y + (ymax-y)/ds;
		repaint();	
	}
	
	protected void zoom(Point p0, Point p1) {
		double x0 = x(p0.x);
		double y0 = y(p0.y);
		double x1 = x(p1.x);
		double y1 = y(p1.y);
		xmin = min(x0, x1); xmax = max(x0, x1);
		ymin = min(y0, y1); ymax = max(y0, y1);
		repaint();	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}