package Ejemplos;

import javax.swing.*;

public class vent extends JFrame{

	private static final long serialVersionUID = 1604875979060156694L;
	private JtextFieldConParteNoEditable textf;

	public static void main(String[] args) {
		new vent();
	}
	
	public vent() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBounds(10, 10, 633, 393);
		getContentPane().setLayout(null);
		
		textf = new JtextFieldConParteNoEditable();
		textf.setBounds(10, 28, 228, 29);
		getContentPane().add(textf);
		textf.setColumns(10);
		
		textf.setParteNoEditable("skjdhhj> ");
		textf.setText2("hola");
		repaint();

	}
}
