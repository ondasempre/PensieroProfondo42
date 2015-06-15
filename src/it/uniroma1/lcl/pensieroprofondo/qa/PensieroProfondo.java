package it.uniroma1.lcl.pensieroprofondo.qa;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;

//import org.apache.tools.ant.listener.AnsiColorLogger;





import it.uniroma1.lcl.Graph;
import it.uniroma1.lcl.Topic;
import it.uniroma1.lcl.Type;
import it.uniroma1.lcl.pensieroprofondo.index.Indexer;
import it.uniroma1.lcl.pensieroprofondo.search.Searcher;

/**
 * Classe PENSIERO PROFONDO - Classe principale del progetto. Contiene il main eseguito a run time.
 * Secondo specifiche contiene due metodi per la gestione dell'esecuzione e la gestione delle domande,
 * rispettivamente run() e query().
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 * 
 */
public class PensieroProfondo 
{
	/** domande (in forma di classe) che si possono fare a PP */
	public static final String QUESTIONPATH = "./src/it/uniroma1/lcl/pensieroprofondo/qa/questions";
	public static final String CLASSPATH = "it.uniroma1.lcl.pensieroprofondo.qa.questions.";
	/** variabile booleana per la gestione dell'avvio */
	private static boolean START = true;
	
	/** ArrayAnswers con possibili risposte random */
	private static Answer[] answers = {
											new Answer("I don't know! :-("), 
											new Answer("Ask me something more interesting..."), 
											new Answer("What do you mean ??"),
											new Answer("42...."),
											new Answer("I have to think about...come back after 7.5 milion years !!")
									  };
	
	/** variabile booleana per specificare se lanciare la grafica o meno */
	private boolean panelon;
	
	/** Costruttore di default */
	public PensieroProfondo()
	{
		start();
		run();
	}
	
	/** 
	 * Messaggio iniziale di PP, viene stampato solo una volta. 
	 * Qui pensiero profondo ci permette di lanciare una GUI per utente.
	 * 
	 * */
	private void start()
	{
		if(START)
		{
			START=false;
			System.out.println("I'm deep thought , I will reply to your questions");
			System.out.println("Do you wont me to show myself as a panel ?[Y/n]");
			
			Scanner in = new Scanner(System.in);
			
			if (in.nextLine().toLowerCase().equals("y"))
			{
				panelon = true;
				new PensieroProfondoPanel(this);
			}
		}
	}

	/** Metodo per entrare nel loop infinito. Permette all'utente di fare domande a Pensiero Profondo. */
	private void run()
	{	
		/** Se è stata scelta la versione grafia disattivo la shell */
		if(panelon)
			return;
		
		/** Leggo riga per riga con l'oggetto Scanner */
		Scanner in = new Scanner(System.in);
		String text;
		
		/** Digitare "bye" per uscire dal ciclo infinito. */
		while(!(text = in.nextLine()).toLowerCase().equals("bye") )
		{
			// Crea la lista delle risposte
			 List<Object> answerList = this.query(text);
			  
			 /** Se non abbiamo rispose da tornare scegliamo una risposta random da un array */
			 if(answerList.size() == 0)
			 {
				 System.out.println(randomAnswer().getText());
			 }
			 else
				 /** esiste una risposta */
				 for(Object o:answerList)
				 {
					 /** contiene un Topic */
					 if(o instanceof Topic)
					 {
						 String answer = ((Topic)o).getLabel();
						 System.out.println(answer);
					 }
					 /** Specifico percorso di Type perché questo tipo di oggetto è presente anche nei pacchetti delle interfacce grafiche */
					 else if (o instanceof it.uniroma1.lcl.Type)	
					 {
						 String answer = ((it.uniroma1.lcl.Type)o).getType();
						 answer = answer.substring(answer.lastIndexOf(".")+1);	
						 answer = answer.replace("_", " ");
						 System.out.println("Is a "+answer);
					 }
					 /** */
					 else if (o instanceof Answer)
					 {
						 String answer = ((Answer)o).getText();
						 System.out.println(answer);
					 }   
			  } 
		}
		
		in.close();
	}
	
	/** Genero numeri pseudo-randomici per scegliere una frase di risposta casuale */
	public Answer randomAnswer()
	{
		Random random = new Random();
		int num = random.nextInt(answers.length);
		return answers[num];
	}
	
	
	/** data in input una singola domanda sotto forma di stringa, restituisce la o le risposte a tale domanda. */
	public List<Object> query(String question)
	{
		 File f = new File(QUESTIONPATH);
		 
		 List<Object> answerList = new ArrayList<>();
		 
		 ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
		 
		 for(String filename : names)
		 {
			 try
			 {
				 /** Uso della reflection */
				 filename = CLASSPATH + filename.substring(0,filename.indexOf("."));
				 /** Creo il punto di accesso alla classe */
				 Class className = Class.forName(filename);
				 /** Nuova istanza della classe*/
				 Question q = (Question)className.newInstance();
				 /** Domanda interpretata con lista di oggetti */
				 answerList = q.getAnswer(question);
				 
				 if(answerList.size()>0)
					 break; 
			 } catch(Exception e) {
				 //e.printStackTrace(); 
			}
		  		  
		 }
		 return answerList;
			 
	}
	
	public static void main(String argv[])
	{
		/** Controllo argomenti per lanciare l'indexer() */
		if(argv.length > 1)
		{
			new Indexer(argv[1]);
		}
		
		///	 -- GESTIONE GRAFO --
		
		 Searcher searcher = (Searcher)Searcher.getInstance();   
	    
		 //System.out.println(searcher.getQuery(null, new Topic("m.017g21" , "Roger Waters"), "film.film.directed_by", null));
		
		 List<Topic> tl = new ArrayList();  
	     // Creo tre topic e li aggiungo alla lista 
	     tl.add(searcher.getTopic("m.058ymm"));
	     tl.add(searcher.getTopic("m.01ycck"));
	     tl.add(searcher.getTopic("m.017g21"));
	     
	     List<it.uniroma1.lcl.Type> ttl = new ArrayList();
	     
	     ttl.add(new it.uniroma1.lcl.Type("award.ranked_item"));
	     ttl.add(new it.uniroma1.lcl.Type("media_common.adaptation"));
	     //ttl.add(searcher.getTypes(searcher.getTopic("m.058ymm")).get(1));
	     
	     Graph graph = new Graph(tl,ttl);
	     
	     graph.printNodes();
	     graph.printArcs();
		
//		Searcher sr = (Searcher)Searcher.getInstance();
//		System.out.println(sr.getTypes(sr.getTopic("m.0h37m9h")).get(0));
	     
	     new PensieroProfondo();
			
	}

	/** Metodo privato della classe PP, usato per recuperare il testo della risposta. */
	private String getTestoRisposta(String testoDomanda)
	{
		String testoRisposta="";
		
		List<Object> answerList=this.query(testoDomanda);
		
		if(answerList.size()==0)
			testoRisposta= "I don't know! :-(";
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

}
