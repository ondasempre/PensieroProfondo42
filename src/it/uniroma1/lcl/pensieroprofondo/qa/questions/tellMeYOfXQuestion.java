package it.uniroma1.lcl.pensieroprofondo.qa.questions;

/**
 * Classe domanda (per gruppi da due) - Classe che implementa la question: Dimmi Y di X?
 * 
 * @author ondasempre.gmail.com
 * @author elisa.magalotti@gmail.com
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.uniroma1.lcl.Topic;
import it.uniroma1.lcl.pensieroprofondo.qa.Question;
import it.uniroma1.lcl.pensieroprofondo.search.Searcher;

public class tellMeYOfXQuestion implements Question 
{

	/** Utillizza espressioni regolari per identificare il type richiesto*/
	public static List<String> QEXP=
			Arrays.asList("^(T|t)ell\\s+me\\s+the\\s+genre\\s+of.*\\s*\\.{0,1}\\s*$",
					"^(T|t)ell\\s+me\\s+the\\s+actors\\s+of.*\\s*\\.{0,1}\\s*$");	
	
	public static List<String> QSPLIT=Arrays.asList("^(T|t)ell\\s+me\\s+the\\s+genre\\s+of|\\s*\\.\\s*$",
													"^(T|t)ell\\s+me\\s+the\\s+actors\\s+of|\\s*\\.\\s*$");	
	@Override
	public List<Object> getAnswer(String questionString) 
	{
		List<Object> answerList=new ArrayList();	 //la lista che deve contenere l'elenco dei risultati
		 
		 for(int i=0;i<QEXP.size();i++) 			 //scorro le espressioni regolari associate a questa domanda
		 {
			 String qexp=QEXP.get(i);	   			 //prendo una espressione regolare
			 if(questionString.matches(qexp))		 //se la domanda posta viene riconosciuta
			 {
				 String qsplit=QSPLIT.get(i);		 //prendo la corrispondente espressione che "splitta" la stringa togliendo 
				 									 //le parti fisse della domanda
				 
				 List<String> listOfTerms=Arrays.asList(questionString.split(qsplit));  //ottengo la lista dei termini
			  	 String topicString="";
			  	 
			  	 for (String s : listOfTerms)
			  		topicString += s + " ";
			  	 topicString=topicString.trim();
			  	 System.out.println(topicString);
			  	 Searcher searcher=Searcher.getInstance();
			  	 List<Topic> queryTopic=searcher.getTopicsByLabel(topicString);
			  	 
			  	 switch(i)
			  	 {
			  	 	case 0://tell me the genre
			  	 		if(queryTopic.size()>0)
			  	 		{
			  	 			List<Topic> topicList=(List<Topic>)searcher.getQuery(null,null, "film.film_genre.films_in_this_genre", queryTopic.get(0));
			  	 			if(topicList!=null)
			  	 				for(Topic t:topicList)
			  	 					answerList.add(t);  
			  	 		}
				  
			  	 		break;
			  	 
			  	 	case 1://tell me the actors
			  	 		if(queryTopic.size()>0)
			  	 		{
			  	 			List<Topic> topicList=(List<Topic>)searcher.getQuery(null,queryTopic.get(0), "film.film.starring", null);
			  	 			List<Topic> topicList2;
			  	 			//System.out.println("Risposta lunga "+topicList.size());
			  	 			if(topicList!=null)
			  	 				for(Topic t:topicList)
			  	 				{
			  	 					topicList2=(List<Topic>)searcher.getQuery(null,t, "film.performance.actor", null); 
			  	 					for(Topic t2:topicList2)
			  	 						answerList.add(t2);    
			  	 				}
					   
					  }	
				 break;
			  	}
			 }
			}
	 return answerList;
	}

}
