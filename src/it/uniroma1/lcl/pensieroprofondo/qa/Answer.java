package it.uniroma1.lcl.pensieroprofondo.qa;
/**
 * Classe risposta. Associa una riga di testo all'oggetto Answer.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class Answer 
{
	private String text;
	
	/** 
	 * Costruttore della classe, a cui passiamo una stringa text.
	 * 
	 * */
	public Answer(String text)
	{
		this.text=text;
	}
	
	/** Metodo per ritornare il testo della risposta. */
	public String getText()
	{
		return this.text;
	}
	
}
