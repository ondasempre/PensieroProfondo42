package it.uniroma1.lcl.pensieroprofondo.record;

/**
 * Classe TopicLabelRecord - ha come campi l'ID e la label associati al topic.
 * Viene utilizzata dalla classe parser ai fini dell'indicizzazione.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 * 
 */
public class TopicLabelRecord 
{
	private String topicId;
	private String label;

	public TopicLabelRecord(String topicId, String label)
	{
		this.topicId = topicId;
		this.label = label;
	}
	
	/** Ritorna l'ID del topic*/
	public String getTopicId() 
	{
		return topicId;
	}
	
	/** Imposta l'ID del topic */
	public void setTopicId(String topicId) 
	{
		this.topicId = topicId;
	}
	
	/** Ritorna la label del topic*/
	public String getLabel() 
	{
		return label;
	}
	
	/** Imposta la label del topic */
	public void setLabel(String label)
	{
		this.label = label;
	}
	
}
