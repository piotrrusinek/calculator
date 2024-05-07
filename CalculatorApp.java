import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;

import java.awt.BorderLayout;
import java.awt.CardLayout;
//default border layout - CENTER
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class CalculatorApp implements ActionListener{
	
	State state = new FirstNumber();
	
	public void setState(State state) {
		this.state = state;
	}
	
	JFrame window = new JFrame("Calculator");
	JPanel buttonPanel = new JPanel();
    final JTextField tf=new JTextField();  
    JButton[] buttons = new JButton[10];
    JButton plus = new JButton("+");
    JButton minus = new JButton("-");
    JButton multiply = new JButton("*");
    JButton divide = new JButton("/");
    JButton equals = new JButton("=");
    JButton clear = new JButton("C");
    
    {
    	window.setLayout(new BorderLayout());
    	window.getContentPane().add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setLayout(new GridLayout(4,4));
        Font font = new Font("Arial", Font.BOLD, 12);
        tf.setFont(font);
    	tf.setBounds(50,50, 150,20); 
    	tf.setEditable(false);
    	tf.setHorizontalAlignment(JTextField.TRAILING);
    	window.add(tf,BorderLayout.NORTH);
    	
    	for(int i=0; i<10; i++) {
        	buttons[i] = new JButton(String.valueOf(i));
        	buttons[i].addActionListener(this);
        	buttonPanel.add(buttons[i]);
        }
    	
    	plus.addActionListener(this);
        //buttonPanel.add(plus);
        minus.addActionListener(this);
        //buttonPanel.add(minus); 
        multiply.addActionListener(this);
        //buttonPanel.add(multiply); 
        divide.addActionListener(this);
        //buttonPanel.add(divide); 
        equals.addActionListener(this);
        //buttonPanel.add(equals);   
        clear.addActionListener(this);
        //buttonPanel.add(clear);
        
        buttonPanel.add(buttons[1]); buttonPanel.add(buttons[2]); buttonPanel.add(buttons[3]); buttonPanel.add(plus);
        buttonPanel.add(buttons[4]); buttonPanel.add(buttons[5]); buttonPanel.add(buttons[6]); buttonPanel.add(minus);
        buttonPanel.add(buttons[7]); buttonPanel.add(buttons[8]); buttonPanel.add(buttons[9]); buttonPanel.add(multiply);
        buttonPanel.add(buttons[0]); buttonPanel.add(equals); buttonPanel.add(clear); buttonPanel.add(divide);
    }
    
    

    
    public void createAndShowGUI() {
    	window.pack();
    	window.setLocationRelativeTo(null); //has to be after pack()
    	window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
	
	
	public void actionPerformed(ActionEvent e) {
		//tf.setText(e.getActionCommand());
		state = state.addSymbol(e.getActionCommand());
		tf.setText(state.getText());
	}
	
	
	
	public static void main(String[] args) {
		CalculatorApp calc = new CalculatorApp();
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() { calc.createAndShowGUI(); }
        });

	}

}




interface State{
	public abstract State addSymbol(String symbol);
	public abstract String getText();
	
	
}

class FirstNumber implements State{
	
	String first;
	
	public FirstNumber(String text) {
		this.first = text;
	}
	public FirstNumber() {
		this.first = "";
	}
	
	
	public State addSymbol(String symbol) {
		if(symbol.matches("[0-9]")) {
			first += symbol;
			return new FirstNumber(first);
		}
		else if(symbol.equals("C")) {
			return new FirstNumber();
		}
		else if(symbol.equals("=")) return new Equals((first.equals("")) ? 0: Integer.valueOf(first), "",0);
		else return new Operation(first, symbol);
	}
	
	public String getText() {
		return first;
	}
}

class Operation implements State{
	int first;
	String operation;
	
	public Operation(String first, String symbol) {
		if(first.equals("")) this.first = 0;
		else this.first = Integer.valueOf(first);
		
		operation = symbol;
	}
	
	public Operation(int first, String symbol) {
		this.first = first;
		this.operation = symbol;
	}
	
	public State addSymbol(String symbol) {
		if(symbol.matches("[\\+\\-\\*\\/]")) {
			operation = symbol;
			return new Operation(first, symbol);
		}
		else if(symbol.equals("C")) return new FirstNumber();
		else if(symbol.equals("=")) return new Equals(first, operation, first);
		else {
			return new SecondNumber(first, operation, symbol);
		}
	}
	
	public String getText() {
		return Integer.toString(first);
		//return String.valueOf(first);
	}
}


class SecondNumber implements State{
	
	int first;
	String operation;
	String second;
	
	public SecondNumber(int first, String operation, String second) {
		this.first = first;
		this.operation = operation;
		this.second = second;
	}
	
	public State addSymbol(String symbol) {
		if(symbol.matches("[0-9]")) {
			this.second += symbol;
			return new SecondNumber(first, operation, second);
		}
		else if(symbol.equals("C")) return new FirstNumber();
		else if(symbol.equals("=")) return new Equals(first, operation, second);
		else return new Operation(new Equals(first, operation, second).getText(), symbol);
	}
	
	public String getText() {
		return second;
	}
}

class Equals implements State{
	int first;
	int second;
	String operation;
	int result;
	
	public Equals(int first, String operation, String second) {
		this.first = first;
		this.operation = operation;
		this.second = Integer.valueOf(second);
	}
	public Equals(int first, String operation, int second) {
		this.first = first;
		this.operation = operation;
		this.second = second;
	}
	
	public State addSymbol(String symbol) {
		if(symbol.matches("[0-9]")) return new FirstNumber(symbol);
		else if(symbol.equals("C")) return new FirstNumber();
		else if(symbol.equals("=")) return new Equals(Integer.valueOf(this.getText()),operation, second);
		else return new Operation(this.getText(), symbol);
	}
	
	public String getText() {
		switch(operation) {
		case "+":
			return Integer.toString((first+second));
		case "-":
			return Integer.toString((first-second));
		case "*":
			return Integer.toString((first*second));
		case "/":
			if(second==0) return "ERROR: Division by zero";
			return Integer.toString((first/second));
		default:
			return Integer.toString(first);
			
		}
	}
}


