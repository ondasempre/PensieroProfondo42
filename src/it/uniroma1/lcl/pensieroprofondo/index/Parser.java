package it.uniroma1.lcl.pensieroprofondo.index;

import java.io.IOException;

import it.uniroma1.lcl.pensieroprofondo.record.TopicLabelRecord;
import it.uniroma1.lcl.pensieroprofondo.record.TopicRelationRecord;
import it.uniroma1.lcl.pensieroprofondo.record.TopicTypeRecord;
import it.uniroma1.lcl.pensieroprofondo.record.TypeInstanceRecord;
/**
 * Classe studiata per estrarre l'informazione utile dalle righe della forma N3.
 * Questa classe è considerata come un singoletto (ne può essere istanziata una sola).
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class Parser 
{
	private static Parser instance = null;

	private static final String prefix = "http://rdf.freebase.com/ns/";
	private int prefixLength = prefix.length();

	private static final String beginTag = "<";
	private static final String endTag = ">";

	/** Costruttore vuoto di default */
	protected Parser() 	{ }
	
	/** Metodo per recuperare la singola istanza della classe */
	public static Parser getInstance() 
	{
		if(instance == null)
		{
			instance = new Parser();
		}
		return instance;
	}
    
	/** Questo metodo stabilisce se la stringa passata è un ID di un topic*/
	private boolean isTopicId(String s)
	{
		if (s.charAt(1) == '.')
		{
			return true;
		}
		return false;
	}
	
	/** 
	 * Metodo per suddividere la riga in tokens. 
	 * Lo schema seguito è del tipo: 
	 * 
	 *  <subject>  <predicate>  <object> .
	 *  
	 *  */
	public Object extractInformationFrom(String line)
	{
        // splitto sul carattere tab per recuperare le sotto-stringhe.
		String[] lineComponents = line.split("\t");
		
		String subject;
		String predicate;
		String object;
		
		// Aggiungo un try-catch per la gestione dell'IndexOutOfBoundsException.
		try 
		{
			
			subject = lineComponents[0];
			predicate = lineComponents[1];
			object = lineComponents[2];
			
		// Recupero il label del topic utilizzando il metodo contains() e incremento l'indice TopicLabel.
		if (predicate.contains("rdf-schema#label"))
		{
			return processTopicLabelRecord(subject, predicate, object);
		}
        // Determino se la riga rappresenta un Type utilizzando sempre il metodo contains() e incremento l'indice relativo. 
		else if (predicate.contains("rdf-syntax-ns#type"))
		{
			return processTopicTypeRecord(subject, predicate, object);
		}
		// Controllo se il predicato sia un tipo istanza e incremento l'indice associato.
		else if (predicate.contains("type.type.instance"))
		{
			return processTypeInstanceRecord(subject, predicate, object);
		}

		else {
			
			String s = "";
			String o = "";
			
			if (subject.contains(prefix) && object.contains(prefix))
			{
				try
				{
									 
					 s = subject.substring(prefixLength+1,subject.indexOf(endTag));
					 o = object.substring(prefixLength+1,object.indexOf(endTag));
					 				
					
				} catch(Exception e) {
					return null;
				}
				// se le stringhe estratte sono ID allora incremento l'indice delle relazioni tra topic.
				if (isTopicId(s) && isTopicId(o))
					return processTopicRelationRecord(subject, predicate, object);
			}
		}
		
	} catch (IndexOutOfBoundsException e) {
			
			//System.out.println(line);
		
		}

		return null;

	}

	/** ID #label : sono n, una per lingua, noi usiamo solo EN.  */
	public Object processTopicLabelRecord(String subject, String predicate, String object) 
	{
		
		String id = "";
		String label = "";
		
		if (subject.contains(prefix))
		{
			id = subject.substring(prefixLength+1,subject.indexOf(endTag));
		}
		// Verifico se nell'oggetto sono presenti i caratteri @en
		if (object.contains("@") && object.substring(object.indexOf("@")+1, object.indexOf("@")+3).equalsIgnoreCase("en"))
		{
			label = object.substring(object.indexOf("\"")+1,object.lastIndexOf("\""));
		}
		
		else return null;

		TopicLabelRecord topicLabelRecord = new TopicLabelRecord(id,label);

		return topicLabelRecord;		
	}

	/** ID #type : ci sono n type in riferimento a un topic */
	public Object processTopicTypeRecord(String subject, String predicate, String object) 
	{
		
		String topicId = "";
		String typeString = "";
		
		if (subject.contains(prefix))
		{
			topicId = subject.substring(prefixLength+1,subject.indexOf(endTag));
		}

		if (object.contains(prefix))
		{
			typeString = object.substring(prefixLength+1,object.indexOf(endTag));
		}

		TopicTypeRecord topicTypeRecord = new TopicTypeRecord(topicId,typeString);

		return topicTypeRecord;

	}

	/** Istance - sono della forma:    type_name  istance  topicID */
	private Object processTypeInstanceRecord(String subject, String predicate, String object) 
	{
		String typeString = "";
		String topicId = "";
		
		if (subject.contains(prefix))
		{
			typeString = subject.substring(prefixLength+1,subject.indexOf(endTag));
		}

		if (object.contains(prefix))
		{
			topicId = object.substring(prefixLength+1,object.indexOf(endTag));
		}

		TypeInstanceRecord typeInstanceRecord = new TypeInstanceRecord(typeString,topicId);

		return typeInstanceRecord;
	}
    
	/** TopicRelation - sono della forma: topic1ID  property  topic2ID */
	public Object processTopicRelationRecord(String subject, String predicate, String object) 
	{
		String topic1Id = "";
		String topic2Id = "";
		String typeProperty = "";
		if (subject.contains(prefix))
		{
			topic1Id = subject.substring(prefixLength+1,subject.indexOf(endTag));
		}

		if (predicate.contains(prefix))
		{
			typeProperty = predicate.substring(prefixLength+1,predicate.indexOf(endTag));
		}

		if (object.contains(prefix))
		{
			topic2Id = object.substring(prefixLength+1,object.indexOf(endTag));
		}
	
		TopicRelationRecord topicRelationRecord = new TopicRelationRecord(topic1Id,typeProperty, topic2Id);

		return topicRelationRecord;

	}

	public static void main(String args[])
	{
		
		String nomeFileGz = args[0];

		String cartellaDestinazione = "index";
		
		char c = (int)'\t';
		
		FreebaseIndexer freebaseIndexer = new FreebaseIndexer(nomeFileGz, cartellaDestinazione);
		
		try 
		{
			freebaseIndexer.index();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
