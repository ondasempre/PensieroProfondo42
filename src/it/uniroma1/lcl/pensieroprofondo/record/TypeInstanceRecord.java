package it.uniroma1.lcl.pensieroprofondo.record;

/**
 * Classe TypeIstanceRecord - ha come capmpi il tipe e l'ID.
 *  Viene utilizzata dalla classe parser ai fini dell'indicizzazione.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class TypeInstanceRecord
{

	private String type;
	private String topicId;

	public TypeInstanceRecord(String type, String topicId)
	{
		this.type=type;
		this.topicId=topicId;
	}
	
	public String getType() 
	{
		return type;
	}
	
	public void setType(String type) 
	{
		this.type = type;
	}
	
	public String getTopicId() 
	{
		return topicId;
	}
	
	public void setTopicId(String topicId) 
	{
		this.topicId = topicId;
	}
	
}
