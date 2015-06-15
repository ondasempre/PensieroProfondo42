package it.uniroma1.lcl.pensieroprofondo.qa;

import java.util.List;

/**
 * Questa interfaccia rappresenta una generica domanda; classi che implementano questa
 * interfaccia devono riconoscere una particolare struttura di domanda e rispondere.
 * 
 * @author ondasempre@gmail.com
 *@author elisa.magalotti@gmail.com
 */
public interface Question 
{
	/**
	 *  Questo metodo restituisce
	 *  null: se non è in grado di rispondere alla domanda
	 *  una String: contenente la risposta
	 *
	 */
	
	public List<Object> getAnswer(String questionString);

}
