package it.uniroma1.lcl.pensieroprofondo.qa.questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.uniroma1.lcl.Topic;
import it.uniroma1.lcl.Type;
import it.uniroma1.lcl.pensieroprofondo.qa.Answer;
import it.uniroma1.lcl.pensieroprofondo.qa.Question;
import it.uniroma1.lcl.pensieroprofondo.search.Searcher;

/**
 * Classe domanda - Classe che implementa la question: Cosa è X?
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class whatIsXQuestion implements Question 
{

	 public static List<String> QEXP=
				Arrays.asList("^(W|w)hat\\s*(is|'\\s*s).*\\s*?{0,1}\\s*$");	
		public static List<String> QSPLIT=Arrays.asList("^(W|w)hat\\s*(is|'\\s*s)|\\s*\\?\\s*$");	
	
	@Override	
	public List<Object> getAnswer(String questionString)
	{
		Boolean isAPerson=false;
		
		List<Object> answerList=new ArrayList(); 	//la lista che deve contenere l'elenco dei risultati
	
		for(int i=0;i<QEXP.size();i++) 				//scorro le espressioni regolari associate a questa domanda
		{
			String qexp=QEXP.get(i);	   			//prendo una espressione regolare
			if(questionString.matches(qexp))		//se la domanda posta viene riconosciuta
			{
	
				String qsplit=QSPLIT.get(i);		//prendo la corrispondente espressione che "splitta" la stringa togliendo 
													//le parti fisse della domanda
				List<String> listOfTerms=Arrays.asList(questionString.split(qsplit));  //ottengo la lista dei termini
				String topicString="";
				
				for (String s : listOfTerms)
					topicString += s + " ";
				topicString=topicString.trim();
				System.out.println(topicString);  
				Searcher searcher=Searcher.getInstance();
				
				List<Topic> tlist= searcher.getTopicsByLabel(topicString);
				
				for(Topic atopic:tlist)
				{
					List<Type> typeList=searcher.getTypes(atopic);
					for(Type ss:typeList)
					{
						if(ss.getType().contains("people.person"))
							isAPerson=true;
						if(!ss.getType().contains("topic"))
							answerList.add(ss);
					}
				} 
				
				if(isAPerson)
					answerList.add(new Answer("It's not a thing, it's a real person!"));
			}
	 
	}
		
		return answerList;
}

}
