package it.uniroma1.lcl.pensieroprofondo.index;

import java.io.IOException;
/**
 * Classe che viene eseguita aggiungendo nomeFileTriple.gz come parametro.
 * Permette la creazione dell'indice istanziando un nuovo oggetto di tipo
 * FreebaseIndexer, con il quale si richiama il metodo index().
 * 
 * @author ondasempre@gmail.com
 *
 */
public class Indexer 
{
	/** Costruttore per creare l'indice in presenza di argomento */
	public Indexer(String args)
	{
		        String nomeFileGz = args;
				//String nomeFileGz = "fb_triples_film.gz";
				String cartellaDestinazione = "index";
				
				// Per eventuali problemi relativi alla codifica consulta:
				//System.out.println(System.getProperty("file.encoding"));
				
				char c = (int)'\t';
				//System.out.println((int)c);
				
				FreebaseIndexer freebaseIndexer = new FreebaseIndexer(nomeFileGz, cartellaDestinazione);
				
				try 
				{
					freebaseIndexer.index();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
	}
	
}
