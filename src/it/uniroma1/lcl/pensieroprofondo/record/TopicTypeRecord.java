package it.uniroma1.lcl.pensieroprofondo.record;

/**
 * Classe TopicTypeRelation - ha come campi l'ID del topic ed il type.
 * Viene utilizzata dalla classe parser ai fini dell'indicizzazione.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class TopicTypeRecord
{

	private String topicId;
	private String type;
	
	public TopicTypeRecord(String topicId, String type)
	{
		this.topicId=topicId;
		this.type=type;
	}
	
	public String getTopicId() 
	{
		return topicId;
	}
	
	public void setTopicId(String topicId) 
	{
		this.topicId = topicId;
	}
	
	public String getType() 
	{
		return type;
	}
	
	public void setType(String type) 
	{
		this.type = type;
	}
		
}
