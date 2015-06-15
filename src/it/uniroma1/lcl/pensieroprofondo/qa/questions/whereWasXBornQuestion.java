package it.uniroma1.lcl.pensieroprofondo.qa.questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.uniroma1.lcl.Topic;
import it.uniroma1.lcl.pensieroprofondo.qa.Question;
import it.uniroma1.lcl.pensieroprofondo.search.Searcher;

/**
 * Classe domanda - Classe che implementa la question: Dove è nato X?
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class whereWasXBornQuestion implements Question 
{

	public static List<String> QEXP=
			Arrays.asList("^(W|w)here\\s+was\\s+.*\\s+born\\s*\\?{0,1}$");//\\s+was\\s+.*\\s+born\\s*?{0,1}\\s*$");	
	
	public static List<String> QSPLIT=Arrays.asList("(W|w)here\\s+was\\s+|born\\s*|\\?");
	
	public static List<String> freeBaseTerm=Arrays.asList("location.location.people_born_here");
	
	@Override
	public List<Object> getAnswer(String questionString) 
	{
			List<Object> answerList=new ArrayList(); 	//la lista che deve contenere l'elenco dei risultati
			
			for(int i=0;i<QEXP.size();i++) 				//scorro le espressioni regolari associate a questa domanda
				{
					String qexp=QEXP.get(i);	  	    //prendo una espressione regolare
					if(questionString.matches(qexp))	//se la domanda posta viene riconosciuta
					{
						//System.out.println("Trovato elemento "+i);
						String qsplit=QSPLIT.get(i);	//prendo la corrispondente espressione che "splitta" la stringa togliendo 
														//le parti fisse della domanda
						List<String> listOfTerms=Arrays.asList(questionString.split(qsplit));  //ottengo la lista dei termini
						String topicString="";
						
						for (String s : listOfTerms)
							topicString += s + " ";
						topicString=topicString.trim();
						System.out.println(topicString); 
						Searcher searcher=Searcher.getInstance();
						List<Topic> queryTopic=searcher.getTopicsByLabel(topicString);
						if(queryTopic.size()>0)
						{
							List<Topic> topicList=(List<Topic>)searcher.getQuery(null,null, freeBaseTerm.get(i), queryTopic.get(0));
							if(topicList!=null)
								for(Topic t:topicList)
									answerList.add(t);  
						}
					} 
			}
	
	 return answerList;
	}

}
