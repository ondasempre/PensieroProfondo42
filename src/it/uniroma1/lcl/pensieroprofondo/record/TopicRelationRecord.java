package it.uniroma1.lcl.pensieroprofondo.record;

/**
 * Classe TipocRelationRecord - ha come campi i due topic e la proprietà associata.
 * Viene utilizzata dalla classe parser ai fini dell'indicizzazione.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class TopicRelationRecord 
{
	/** ID1 property ID2*/
	String topic1Id;
	String typeProperty;
	String topic2Id;
	
	public TopicRelationRecord(String topic1Id, String typeProperty, String topic2Id)
	{
		this.topic1Id = topic1Id;
		this.typeProperty = typeProperty;
		this.topic2Id = topic2Id;
	}
	
	public String getTopic1Id() 
	{
		return topic1Id;
	}
	
	public void setTopic1Id(String topic1Id) 
	{
		this.topic1Id = topic1Id;
	}
	
	public String getTypeProperty() 
	{
		return typeProperty;
	}
	
	public void setTypeProperty(String typeProperty) 
	{
		this.typeProperty = typeProperty;
	}
	
	public String getTopic2Id() 
	{
		return topic2Id;
	}
	
	public void setTopic2Id(String topic2Id) 
	{
		this.topic2Id = topic2Id;
	}
	
}
