package it.uniroma1.lcl.pensieroprofondo.qa;

import it.uniroma1.lcl.Topic;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
/**
 * Classe per la gestione della grafica. Crea un finestra con due aree:
 * una riservata alla domanda e una per la risposta.
 * In aggiunta, sono presenti due bottoni, per inviare la domanda a PP
 * e per fare un reset delle aree precedentemente compilate.
 * La finestra di dialogo è pensata per utenti non abituati a usare la 
 * shell. 
 *
 */
public class PensieroProfondoPanel extends JFrame implements ActionListener 
{
	
	JTextArea areaDomanda,areaRisposta;
	JButton buttonInvia,buttonReset;
	private PensieroProfondo pp;
	
	/** ArrayAnswers con possibili risposte */
	private static Answer[] answers = {
											new Answer("I don't know! :-("), 
											new Answer("Ask me something more interesting..."), 
											new Answer("What do you mean ??"),
											new Answer("42...."),
											new Answer("I have to think about...come back after 7.5 milion years !!")
									  };
	
	
	public PensieroProfondoPanel(PensieroProfondo pp)
	{
		this.pp = pp;
		
		/** Creo finestra di dialogo */
		this.setSize(500,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       

		Border etched = (Border) BorderFactory.createEtchedBorder();
		areaDomanda = new JTextArea(10, 40);
		areaRisposta = new JTextArea(10, 40);
		
		//areaRisposta.disable();
		JPanel panel= new JPanel();
		JScrollPane scrol = new JScrollPane(areaDomanda);
		JScrollPane scrol2 = new JScrollPane(areaRisposta);
		buttonInvia=new JButton("ENTER");
		buttonReset=new JButton("RESET");
		JScrollPane scrol3 = new JScrollPane(buttonInvia);
		JScrollPane scrol4 = new JScrollPane(buttonReset);
		buttonInvia.addActionListener(this);
		buttonReset.addActionListener(this);

		panel.add(scrol, BorderLayout.NORTH); 
		panel.add(scrol3, BorderLayout.CENTER); 
		panel.add(scrol4, BorderLayout.CENTER); 
		panel.add(scrol2, BorderLayout.SOUTH);  
    
		panel.setBorder(etched);

		this.add(panel);
    
		this.setVisible(true);

	}
	

	/** Gestione dell'area di domanda-risposta */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		JButton button = (JButton)e.getSource();
		
		if(button.equals(buttonInvia))
		{
			areaRisposta.setText(this.getTestoRisposta(areaDomanda.getText()));		
		}				
		else
			if(button.equals(buttonReset))
			{
				areaDomanda.setText("");
				areaRisposta.setText("");
			}
			
	}
	
	
	
	/** Metodo privato della classe PP, usato per recuperare il testo della risposta. */
	private String getTestoRisposta(String testoDomanda)
	{
		String testoRisposta="";
		
		List<Object> answerList = pp.query(testoDomanda);
		
		if(answerList.size()==0)
			testoRisposta = randomAnswer().getText();
		else
			for(Object o : answerList)
			{
				
				if(o instanceof Topic)
				{
					String answer=((Topic)o).getLabel();
					testoRisposta+=answer+"\n ";
				}
				
				else if (o instanceof it.uniroma1.lcl.Type)
				{
					String answer=((it.uniroma1.lcl.Type)o).getType();
					answer=answer.substring(answer.lastIndexOf(".")+1);	
					answer=answer.replace("_", " ");
					testoRisposta+="Is a "+answer+"\n ";
				}
				
				else if (o instanceof Answer)
				{
					String answer=((Answer)o).getText();
					testoRisposta+=answer+"\n ";
				}   
	   		 
	  } 
	 
	 return testoRisposta;
	}
	
	
	/** Genero numeri pseudo-randomici per scegliere una frase di risposta casuale */
	public Answer randomAnswer()
	{
		Random random = new Random();
		int num = random.nextInt(answers.length);
		return answers[num];
	}

}
