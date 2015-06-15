package it.uniroma1.lcl.pensieroprofondo.qa.questions;

/**
 * Classe domanda (per gruppi da due) - Classe che implementa la question: Dimmi X tpo
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import sun.print.resources.serviceui;

import it.uniroma1.lcl.Topic;
import it.uniroma1.lcl.Type;
import it.uniroma1.lcl.pensieroprofondo.qa.Question;
import it.uniroma1.lcl.pensieroprofondo.search.Searcher;

public class tellMeXTypeQuestion implements Question 
{	

	/** Utillizza espressioni regolari per identificare il type richiesto*/
	public static List<String> QEXP=
			Arrays.asList("^(T|t)ell\\s*(me){0,1}\\s+\\d+\\s+.*movi(s){0,1}\\s*\\.{0,1}\\s*$",
					      "^(T|t)ell\\s*(me){0,1}\\s+\\d+\\s+.*actor(s){0,1}\\s*\\.{0,1}\\s*$",
					      "^(T|t)ell\\s*(me){0,1}\\s+\\d+\\s+.*director(s){0,1}\\s*\\.{0,1}\\s*$",
					      "^(T|t)ell\\s*(me){0,1}\\s+\\d+\\s+.*citie(s){0,1}\\s*\\.{0,1}\\s*$");	
	
	public static List<String> QSPLIT=Arrays.asList("^(T|t)ell\\s*(me){0,1}|movie(s){0,1}| |\\.",
													"^(T|t)ell\\s*(me){0,1}|actor(s){0,1}| |\\.",
													"^(T|t)ell\\s*(me){0,1}|director(s){0,1}| |\\.",
													"^(T|t)ell\\s*(me){0,1}|citie(s){0,1}| |\\.");	
	
	public static List<String> freeBaseTerm = Arrays.asList("film.film","film.actor","film.director","location.citytown");
	
	@Override
	public List<Object> getAnswer(String questionString) 
	{
		List<Object> answerList=new ArrayList(); 	//la lista che deve contenere l'elenco dei risultati
		
		for(int i=0;i<QEXP.size();i++) 				//scorro le espressioni regolari associate a questa domanda
		{
			String qexp=QEXP.get(i);	  		    //prendo una espressione regolare
			if(questionString.matches(qexp))		//se la domanda posta viene riconosciuta
			{
				String qsplit=QSPLIT.get(i);		//prendo la corrispondente espressione che "splitta" la stringa togliendo 
													//le parti fisse della domanda
				
				List<String> listOfTerms=Arrays.asList(questionString.split(qsplit));  //ottengo la lista dei termini
				
				String topicString="";
				
				for (String s : listOfTerms) //prendo gli oggetti della lista e li concateno in una stringa
					topicString += s + " ";
				topicString=topicString.trim();
			
				int number = Integer.parseInt(topicString);  //ricavo il numero di type richiesto
				//System.out.println(number);
				Type type=new Type(freeBaseTerm.get(i));
				Searcher searcher=Searcher.getInstance();							
				List <Topic> listOfTopics=searcher.getTopics(type);
				int counter=0;
			
				for(Topic t:listOfTopics)  // risposta con lista di type
				{
					//answerList.add(t.getLabel());
					answerList.add(t);
					counter++;
					if(counter==number)
						break;
				}
			}
		}
	
		return answerList;
	}

}
