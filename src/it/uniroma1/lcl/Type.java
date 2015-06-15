package it.uniroma1.lcl;

/**
 * Classe Type - per ogni Topic esistono n Type associati.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 *
 */
public class Type 
{
	/** Stringa con il nome del Type*/
	String type;
	
	/** Costruttore della classe Type */
	public Type(String type)
	{
		this.type=type;
	}
	
	/** Ritorna il nome del tipo */
	public String getType() 
	{
		return type;
	}

	/** Setta il nome del tipo */
	public void setType(String type) 
	{
		this.type = type;
	}
	
}
