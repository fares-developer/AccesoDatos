package Ejemplos;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JtextFieldConParteNoEditable extends JTextField{

	private String parteNoEditable; //protect this text
	private int n;
    
    public JtextFieldConParteNoEditable() {
    	super();

    	this.addCaretListener(new CaretListener() {
	        @Override
	        public void caretUpdate(CaretEvent e) {
	            if (e.getDot() < n) {
	                if (!(getText().length() < n))
	                	getCaret().setDot(n);
	            }
	        }
	    });

    	this.addKeyListener(new KeyListener() {
	        @Override
	        public void keyPressed(KeyEvent arg0) {
	            if (getCaret().getDot() <= n) {
	                setText(parteNoEditable + getText().substring(n));
	                arg0.consume();
	            }
	        }
	        
			@Override
			public void keyTyped(KeyEvent e) {

				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {

			}

	    });
    	this.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            String input = getText2(); 

	            System.out.println(input);

	            //setText(parteNoEditable);
	            //setCaretPosition(n);
	        }
	    });
    	setParteNoEditable("");
    }
    
    
    public void setParteNoEditable(String nuevaParte) {
    	parteNoEditable=nuevaParte;
    	setText(parteNoEditable);
    	n = parteNoEditable.length();
        setCaretPosition(n);
    }
    
    public String getText2() {
    	return getText().substring(n).trim(); 
    }
    
    public void setText2(String s) {
    	setText(parteNoEditable+s);
    }
}
