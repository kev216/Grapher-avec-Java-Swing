package grapher.ui;

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JSplitPane splitButton;
	JSplitPane splitPane;
	JList list;
	JScrollPane scrollPaneTab;
	Grapher grapher = new Grapher();
	
	
	
	Main(String title, String[] expressions) {
		
		super(title);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JToolBar buttons = new JToolBar("Bouton");
		final JButton plus = new JButton("+");
		JButton moins = new JButton("-");
		plus.setSize(grapher.W/4, grapher.H/10);
		moins.setSize(grapher.W/4,grapher.H/10);
		buttons.add(plus);
		buttons.add(moins);
		buttons.setVisible(true);
		buttons.setFloatable(false);
		final DefaultListModel model = new DefaultListModel();
		final JList list = new JList(model);
		list.add(buttons);
		Border border = BorderFactory.createEmptyBorder();
		list.setBorder(border);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setAutoscrolls(true);
	    
		splitButton = new JSplitPane(JSplitPane.VERTICAL_SPLIT,list,buttons);
		splitButton.setDividerLocation(10*grapher.H/12);
		splitButton.setSize(grapher.W/5, grapher.H);	    
		splitButton.setVisible(true);
		add(splitButton);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,splitButton,grapher);
		splitPane.setRightComponent(grapher);
		splitPane.setLeftComponent(splitButton);
		splitPane.setDividerLocation(grapher.W/5);
		splitPane.setSize(grapher.W, grapher.H);
		splitPane.setVisible(true);
		add(splitPane);
//**********************************************		
		final ActionListener ml = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		            String inputValue = JOptionPane.showInputDialog("Please input a value");
		            if (inputValue != null){
		            	int t=1;
		            	model.add(t, inputValue.toString());
		            	t++;
		            	grapher.add(inputValue.toString());
		            	}	
		            }
		};
	 	plus.addActionListener(ml);		
//***********************************************************************************	 
		MouseListener mouseListener = new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 1) {
		            int index = list.locationToIndex(e.getPoint());
		            String inputValue = list.getSelectedValue().toString();
		            if (inputValue == null)
		            	grapher.add(list.getSelectedValue().toString());
		            else
		            	grapher.add(inputValue);
		            //System.out.println("Clicked on Item " + index);
		         }
		    }
		};
	 	list.addMouseListener(mouseListener);
		
//***********************************************************************************
		
		final String[] data ={"sin(x)","cos(x)","tan(x)","x*x"};
		if(data.length!=0){
	    for (int i=0; i<data.length; i++) {
	        model.add(i, data[i]);
	    }
		}

		
//***********************************************************************************
	 	String[] columnNames = {"First Name","Last Name"};
	 	Object[][] dataTable = {{"sin(x)", ""},
	 							{"cos(x)", ""},
	 							{"tan(x)", ""},
	 							{"x*x", ""},
	 							{"x*x*x", ""},
	 							};
	 	
	 	final DefaultTableModel modelTable = new DefaultTableModel(dataTable, columnNames);
	 	final JTable table = new JTable(modelTable);
	 	final String flag = "List";
	 	
	 	final MouseListener mns = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int pos = list.getSelectedIndex();
				if (pos >= 0) {
					model.remove(pos);
					modelTable.removeRow(modelTable.getRowCount()-1);
					grapher.functions.remove(e.getPoint());
					repaint();
					}
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


		};
	 	moins.addMouseListener(mns);
//***********************************************************************************
	 	
	    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() { 
	    	 
	            public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus,int row, int column) { 

	                Component   cell = super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column); 
	                if(column == 1 && row ==0 )
		                cell.setBackground(Color.BLUE);
	                else if(column == 1 && row ==1)  
	                    cell.setBackground(Color.RED);   
	                else if(column == 1 && row ==2)  
	                    cell.setBackground(Color.ORANGE);   
	                else if(column == 1 && row ==3) 
	                	cell.setBackground(Color.CYAN);
	                else if(column == 1 && row ==4) 
	                	cell.setBackground(Color.GREEN);
	                return cell;
	            } 
	        }; 
	     
	        for(int i = 0; i < columnNames.length; i++) { 
	            table.getColumn(columnNames[i]).setCellRenderer(tcr);
	            
	        } 
	                 
	                 	 		
		MenuBar menubar = new MenuBar();
		Menu listM = new Menu("List");
		Menu tableM = new Menu("Table");
		
		Menu fileM = new Menu("Expression");
		
		MenuItem fileM1 = new MenuItem("Add");
		MenuItem fileM2 = new MenuItem("Delete");
		
		MenuItem listM1 = new MenuItem("List");
		MenuItem tableM1 = new MenuItem("Table");
		
		final CheckboxMenuItem fileM3 = new CheckboxMenuItem("Quit",false);
		ItemListener mouseQuit = new ItemListener() {
			
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 1) {
		        	fileM3.setState(true);
		            
		         }
		    }

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
				
			}

			
		};
	 		fileM3.addItemListener(mouseQuit);
		
		
		
		
		
		
		
	 		menubar.add(listM);
	 		menubar.add(tableM);
	 		menubar.add(fileM);

			
			fileM.add(fileM1);
			fileM.add(fileM2);
			fileM.addSeparator();
			fileM.add(fileM3);
			
			listM.add(listM1);
			tableM.add(tableM1);
			
  
		class MenuListener implements ActionListener{

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (e.getActionCommand().equals("Add")){
					   int pos = list.getModel().getSize();
					   String inputValue = JOptionPane.showInputDialog("Please input a value");//,list.getSelectedValue().toString());
					   model.add(pos, inputValue);
					   modelTable.insertRow(modelTable.getRowCount(), new Object[]{"",""});
					   grapher.add(inputValue.toString());
					}
					else if(e.getActionCommand().equals("Delete")){
						  int pos = list.getSelectedIndex();
						    if (pos >= 0) {
						    	model.remove(pos);
								modelTable.removeRow(modelTable.getRowCount()-1);
								grapher.functions.remove(pos);
								grapher.repaint();
						    }
					}
					else if (e.getActionCommand().equals("List")){
						splitPane.setLeftComponent(splitButton);
					}
					else if (e.getActionCommand().equals("Table")){
						splitPane.setLeftComponent(table);
					}
					
				}
				
			}			
			MenuListener m1 = new MenuListener();
			fileM1.addActionListener(m1);
			fileM2.addActionListener(m1);	
			listM1.addActionListener(m1);	
			tableM1.addActionListener(m1);	
			
			this.setMenuBar(menubar);
			

				
			pack();
		 	
			table.addMouseListener(new   MouseAdapter()   {   
	        
			public void mouseClicked(MouseEvent   e)   {   

	                      int row=table.getSelectedRow();
	                      String value=(String) table.getValueAt(row,0);      
	                      Color color = table.getBackground();
	                      grapher.add(value);

	                      
	              }   
	          });   
		 	
	}
	
	
	
	

	public static void main(String[] argv) {
		final String[] expressions = argv;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				new Main("grapher", expressions).setVisible(true); 
			}
		});
	
	}}