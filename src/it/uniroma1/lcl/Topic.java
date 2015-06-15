package it.uniroma1.lcl;
/**
 * Classe Topic - espone i metodi setter/getter per impostare
 * l'ID de topic e il relativo label.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 * 
 */
public class Topic 
{
	/** ID del topic, generalmente della forma m.56abt34 */
	private String id;
	/** #label relativo al topic */
	private String label;
	
	/** Costruttore di default della classe Topic */
	public Topic() { }
	
	/** 
	 * Costruttore con parametri al quale passiamo 
	 * ID e label visti come stringhe. 
	 *  
	 * */
	public Topic(String id, String label)
	{
		this.id=id;
		this.label=label;
	}
	/** Ritorna l'ID del Topic */
	public String getId() 
	{
		return id;
	}

	/** Setta l'ID del Topic */
	public void setId(String id) 
	{
		this.id = id;
	}

	/** Ritorna il label del Topic */
	public String getLabel() 
	{
		return label;
	}
	
	/** Setta il label del Topic */
	public void setLabel(String label) 
	{
		this.label = label;
	}
	
	public String toString()
	{
		return id+" "+label;
	}

}
