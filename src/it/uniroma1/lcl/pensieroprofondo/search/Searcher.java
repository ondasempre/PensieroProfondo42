package it.uniroma1.lcl.pensieroprofondo.search;

import it.uniroma1.lcl.Topic;
import it.uniroma1.lcl.Type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 * La classe Searcher permette di rispondere a singole interrogazioni,
 * leggendo gli indici creati in locale.
 * Anche questa classe è rappresentata come un singoletto.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class Searcher 
{

	private static Searcher instance = null;
	
	/** Costruttore di default dichiarato protected per creare il singoletto */
	protected Searcher() { }
	
	/** Metodo per recuperare l'istanza creata */
	public static Searcher getInstance() 
	{
		if(instance == null) 
		{
			instance = new Searcher();
		}
		return instance;
	}

	/** 
	 * Dato in input un ID restituisce un oggetto della classe Topic che permette di accedere
	 * a tutte le informazioni relative. 
	 * 
	 * */
	public Topic getTopic(String topicId)
	{
		String text = "";
		Topic topic = new Topic();
		
		// IndexReader è una classe che permette di rendere gli oggetti "searchable".
		IndexReader indexDir = null;
		
		try 
		{
			//System.out.println("CERCO "+topicId);
			
			/**
			 * TERM:
			 * Questa è l'unità di ricerca. E 'composto da due elementi , 
			 * il testo della parola , come una stringa , e il nome del campo che il testo si è trovato. 
			 * Nota che i termini possono rappresentare più di parole da campi di testo , ma anche cose
			 * come date, indirizzi e-mail , URL.
			 * 
			 */
			TermQuery q = new TermQuery(new Term("id", topicId));
			indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topic")));
			IndexSearcher searcherIndex = new IndexSearcher(indexDir);
			// limite imposto alla ricerca: 100unità
			TopDocs docs = searcherIndex.search(q, 100);
			
			for (ScoreDoc scoreDoc : docs.scoreDocs) 
			{
				Document doc = searcherIndex.doc(scoreDoc.doc);
				
				for (IndexableField field : doc.getFields())
				{		
					text+=field.name()+" / ";
					
					//System.out.println(text);
					
					if (field.name().equals("id"))
					{
						topic.setId(doc.get(field.name()));
					} 
					else if (field.name().equals("label")) 
					{
						
						topic.setLabel(doc.get(field.name()));
					}
				}
			}
			indexDir.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/** Controllo se l'ID è stato associato all'oggetto Topic */
		if(topic.getId() == null)
		{
			//System.out.println("ATTENZIONE "+topicId);
			return new Topic(""+topicId,"");
		}
			
		return topic;
	}

	/** Dato in input un Type ritorna la lista di Topic ad esso associati */
	public List<Topic> getTopics(Type type)
	{
		//System.out.println("CERCO TYPE "+type);
		List<Topic> retList = new ArrayList<Topic>();
		IndexReader indexDir = null;
		
		try 
		{
			TermQuery q = new TermQuery(new Term("type", type.getType()));
			indexDir = DirectoryReader.open( SimpleFSDirectory.open(new File("index"+File.separator+"typeTopic")));
			IndexSearcher searcherIndex = new IndexSearcher(indexDir);
			// limite imposto alla ricerca: 1000unità
			TopDocs docs = searcherIndex.search(q, 1000);
			
			for (ScoreDoc scoreDoc : docs.scoreDocs) 
			{
				Document doc = searcherIndex.doc(scoreDoc.doc);
				
				for (IndexableField field : doc.getFields())
				{
					//System.out.println(field.name());
					if (field.name().startsWith("topicId"))
					{
						retList.add(getTopic(doc.get(field.name())));
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retList;
	}

	/** Dato in input un Topic, ritorna la lista dei Types ad esso associati */
	public List<Type> getTypes(Topic topic)
	{
		List<Type> retList = new ArrayList<Type>();
		IndexReader indexDir = null;
		
		try 
		{
			TermQuery q = new TermQuery(new Term("id", topic.getId()));
			indexDir = DirectoryReader.open( SimpleFSDirectory.open(new File("index"+File.separator+"topicType")));
			IndexSearcher searcherIndex = new IndexSearcher(indexDir);
			// limite imposto alla ricerca: 1000unità
			TopDocs docs = searcherIndex.search(q, 1000);
			
			for (ScoreDoc scoreDoc : docs.scoreDocs) 
			{
				Document doc = searcherIndex.doc(scoreDoc.doc);
				
				for (IndexableField field : doc.getFields())
				{
					if (field.name().startsWith("type"))
					{
						retList.add(new Type(doc.get(field.name())));
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retList;
	}
	
	/** Data in input una label (etichetta) restituisce la lista dei Topic con quella label */
	public List<Topic> getTopicsByLabel(String label)
	{
		List<Topic> topicList = new ArrayList<Topic>();
		IndexReader indexDir = null;
		
		try 
		{
			TermQuery q = new TermQuery(new Term("label", label));
			indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topic")));
			IndexSearcher searcherIndex = new IndexSearcher(indexDir);
			// limite imposto alla ricerca: 1000unità
			TopDocs docs = searcherIndex.search(q, 1000);
			
			for (ScoreDoc scoreDoc : docs.scoreDocs) 
			{
				Topic topic = new Topic();
				Document doc = searcherIndex.doc(scoreDoc.doc);
				
				for (IndexableField field : doc.getFields())
				{
					if (field.name().equals("id"))
					{
						topic.setId(doc.get(field.name()));
					}
					else if (field.name().equals("label")) 
					{
						topic.setLabel(doc.get(field.name()));
					}
				}
				topicList.add(topic);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return topicList;
	}

	/** Data in input una label (etichetta) restituisce la lista dei Topic con quella label + [SPLIT SPAZI] */
	public List<Topic> getTopicsByLabelSplit(String label)
	{
		List<Topic> topicList = new ArrayList<Topic>();
		IndexReader indexDir = null;
		
		String[] sA = label.split(" ");
		
		BooleanQuery booleanQuery = new BooleanQuery();
		for (String s:sA)
		{
			booleanQuery.add(new TermQuery(new Term("label",s)),BooleanClause.Occur.MUST);
		}
		
		try 
		{
			//TermQuery q = new TermQuery(new Term("label", label));
			indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topic")));
			IndexSearcher searcherIndex = new IndexSearcher(indexDir);
			
			TopDocs docs = searcherIndex.search(booleanQuery, 1000);
			
			for (ScoreDoc scoreDoc : docs.scoreDocs) 
			{
				Topic topic = new Topic();
				Document doc = searcherIndex.doc(scoreDoc.doc);
				
				for (IndexableField field : doc.getFields())
				{
					if (field.name().equals("id"))
					{
						topic.setId(doc.get(field.name()));
					} else if (field.name().equals("label")) {
						topic.setLabel(doc.get(field.name()));
					}
				}
				topicList.add(topic);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return topicList;
	}

	/*********************************************************************************************************/
	
	
	/** 
	 * Questo metodo permette di effettuare una ricerca nell'indice
	 * fornendo alcuni oggetti tra: type, topic, property.
	 * 
	 * Utilizzo: getQuery(null, topic1, property, null)  ---> elenco dei topic che sono associati come oggetto alla relazione.
	 *           getQuery(null, null, property, topic2)  ---> elenco dei topic che sono associati come soggetti alla relazione.
	 *           getQuery(null, null, property, null)    ---> elenco di coppie di topic associate da una property comune.
	 *           getQuery(null, topic1, null, topic2)    ---> cerco la property associata ai due topic passati.
	 *           
	*/
    public Object getQuery(Type type, Topic topic1,String property,Topic topic2)
    {    		
        //se è specificato un topic soggetto e una property, cerco il topic oggetto associato
    	if(type==null && topic1!=null && property!= null && topic2==null)
    	{
    		List<Topic> topicList = new ArrayList();
    		
    		BooleanQuery q = new BooleanQuery();
    		
    		q.add(new BooleanClause(new TermQuery(new Term("topic1Id",topic1.getId())),Occur.MUST));
    		q.add(new BooleanClause(new TermQuery(new Term("typeProperty",property)),Occur.MUST));
        
    		IndexReader indexDir = null;
     
    		try
    		{
    			indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topicRelation")));
    			IndexSearcher searcherIndex = new IndexSearcher(indexDir);
    			TopDocs docs = searcherIndex.search(q, 1000); 
	 
    			for (ScoreDoc scoreDoc : docs.scoreDocs) 
    			{
    				Document doc = searcherIndex.doc(scoreDoc.doc);
    				for (IndexableField field : doc.getFields())
    				{
    					if (field.name().equals("topic1Id"))		// ?
    						topic1 = getTopic(doc.get(field.name()));		
    					else
    						if (field.name().equals("topic2Id"))
    						{
    							topic2 = getTopic(doc.get(field.name()));	
    						}
    				}
    				topicList.add(topic2);
    			}
    			indexDir.close();
    		} catch (IOException e) {e.printStackTrace();} 
	
    		if(topicList.size()>0)
    			return (Object)topicList;
    		else
    			return null;
    		}
    	
    	else //se è specificata una property e un topico oggetto, cerco il soggetto
    	 if(type==null && topic1==null && property!= null && topic2!=null)
    	 {
    	     	List<Topic> topicList=new ArrayList();
    	     	
    	     	BooleanQuery q = new BooleanQuery();
    	     	q.add(new BooleanClause(new TermQuery(new Term("topic2Id",topic2.getId())),Occur.MUST));
    	     	q.add(new BooleanClause(new TermQuery(new Term("typeProperty",property)),Occur.MUST));
    	     
    	     	IndexReader indexDir = null;
    	    
    	     	try
    	     	{
    	     		indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topicRelation")));
    	     		IndexSearcher searcherIndex = new IndexSearcher(indexDir);
    	     		
    	     		TopDocs docs = searcherIndex.search(q, 1000); 
    	    	 
    	     		for (ScoreDoc scoreDoc : docs.scoreDocs) 
    	     		{
    	     			Document doc = searcherIndex.doc(scoreDoc.doc);
    	     			for (IndexableField field : doc.getFields())
    	     			{
    	     				if (field.name().equals("topic1Id"))
    	     					topic1=getTopic(doc.get(field.name()));		
    	     				else
    	     					if (field.name().equals("topic2Id"))
    	     						topic2=getTopic(doc.get(field.name()));		
    	     			}
    			
    	     			topicList.add(topic1);
    	     		}
    	     		
    		  indexDir.close();
    		  
    		} catch (IOException e) { e.printStackTrace(); } 
    		
    	     if(topicList.size()>0)
    	    	 return (Object)topicList;
    	     else
    	    	 return null;
    	     }
    	 else
    		 //se è specificata solo una property, cerco le coppie di topic legate dalla property
    	  	 if(type==null && topic1==null && property!= null && topic2==null)
    	  	 {
    	  		 List<Object> topicList=new ArrayList();
    	  		 BooleanQuery q = new BooleanQuery();
    	  		 q.add(new BooleanClause(new TermQuery(new Term("typeProperty",property)),Occur.MUST));  	     
     	         IndexReader indexDir = null;
     	   
     	     try
     	     {
     	      indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topicRelation")));
     		  IndexSearcher searcherIndex = new IndexSearcher(indexDir);
     		  TopDocs docs = searcherIndex.search(q, 1000); 
     		  
     		  for (ScoreDoc scoreDoc : docs.scoreDocs) 
     			{
     				Document doc = searcherIndex.doc(scoreDoc.doc);
     				for (IndexableField field : doc.getFields())
     				{				
     					if (field.name().equals("topic1Id"))
     						topic1=getTopic(doc.get(field.name()));		
     					else
     					if (field.name().equals("topic2Id"))
     								topic2=getTopic(doc.get(field.name()));		
     				}
     				
     			topicList.add(new Object[] {topic1,topic2}); 	/** arrayList di coppie */
     			
     			}
     		  indexDir.close();
     		 
     		} catch (IOException e) {e.printStackTrace();} 
     		
     	     if(topicList.size()>0)
     	    	 return (Object)topicList;
     	     else
     	    	 return null;
     	     }
    	  	 else
    	  		 //se sono specificati due topic, cerco la property che li lega
        	 if(type==null && topic1!=null && property== null && topic2!=null)
         	 {
        		 List<String> propertyList=new ArrayList();
         	     BooleanQuery q = new BooleanQuery();
         	     q.add(new BooleanClause(new TermQuery(new Term("topic1Id",topic1.getId())),Occur.MUST));
         	     q.add(new BooleanClause(new TermQuery(new Term("topic2Id",topic2.getId())),Occur.MUST));
         	     IndexReader indexDir = null;
         	     try
         	     {
         	      indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topicRelation")));
         		  IndexSearcher searcherIndex = new IndexSearcher(indexDir);
         		  TopDocs docs = searcherIndex.search(q, 1000); 
         		  for (ScoreDoc scoreDoc : docs.scoreDocs) 
         			{
         				Document doc = searcherIndex.doc(scoreDoc.doc);
         				for (IndexableField field : doc.getFields())
         				{				
         				 if (field.name().equals("typeProperty"))
         						property=doc.get(field.name());		
         						
         				}
         			propertyList.add(property);
         			}
         		 indexDir.close();
         		} catch (IOException e) {e.printStackTrace();} 
         		
         	     if(propertyList.size()>0)
         	    	 return (Object)propertyList;
         	     else
         	    	 return null;		   		 
         	 }	
        	 else  //cerco relazione tra type e topic
            	 if(type!=null && topic1!=null && property== null && topic2==null)
             	 {
            		 List<String> propertyList=new ArrayList();
             	     BooleanQuery q = new BooleanQuery();
             	     q.add(new BooleanClause(new TermQuery(new Term("topicId",topic1.getId())),Occur.MUST));
             	     q.add(new BooleanClause(new TermQuery(new Term("type",type.getType())),Occur.MUST));
             	     IndexReader indexDir = null;
             	     
             	     try
             	     {
             	    	 indexDir = DirectoryReader.open( SimpleFSDirectory.open(new File("index"+File.separator+"typeTopic")));
             	    	 IndexSearcher searcherIndex = new IndexSearcher(indexDir);
             	    	 TopDocs docs = searcherIndex.search(q, 1000); 
             	    	 
             	    	 for (ScoreDoc scoreDoc : docs.scoreDocs) 
             	    	 {
             	    		 Document doc = searcherIndex.doc(scoreDoc.doc);
             	    		 if(doc.getFields().size()>0) //vuol dire che esiste una entry type-topic
             	    			propertyList.add("type.type.instance"); 
             	    			         				
             	    	 }
             	    	 
             		 indexDir.close();
             		} catch (IOException e) {e.printStackTrace();} 
             		
             	     if(propertyList.size()>0)
             	    	 return (Object)propertyList;
             	     else
             	    	 return null;		   		 
             	 }
            	 else  //cerco relazione tra topic e type
                	 if(type!=null && topic1==null && property== null && topic2!=null)
                 	 {
                		 List<String> propertyList=new ArrayList();
                 	     BooleanQuery q = new BooleanQuery();
                 	     q.add(new BooleanClause(new TermQuery(new Term("id",topic2.getId())),Occur.MUST));
                 	     q.add(new BooleanClause(new TermQuery(new Term("type",type.getType())),Occur.MUST));
                 	     IndexReader indexDir = null;
                 	     try
                 	     {
                 	      indexDir = DirectoryReader.open( SimpleFSDirectory.open(new File("index"+File.separator+"topicType")));
                 	      IndexSearcher searcherIndex = new IndexSearcher(indexDir);
                 		  TopDocs docs = searcherIndex.search(q, 1000); 
                 		  for (ScoreDoc scoreDoc : docs.scoreDocs) 
                 			{
                 			 Document doc = searcherIndex.doc(scoreDoc.doc);
                 			 if(doc.getFields().size()>0) //vuol dire che esiste una entry type-topic
                 				propertyList.add("rdf-syntax-ns#type"); 
                 			          				
                 			}
                 		 indexDir.close();
                 		} catch (IOException e) {e.printStackTrace();} 
                 		
                 	     if(propertyList.size()>0)
                 	    	 return (Object)propertyList;
                 	     else
                 	    	 return null;		   		 
                 	 }
     return null;
    }
    
    /*********************************************************************************************************/
    
    
	public void getProperties(Topic topic)
	{
		
		IndexReader indexDir = null;
		
		try 
		{
			 System.out.println(topic.getId());
			 // query che corrisponde a documenti che contengono quel termine in particolare.
			 TermQuery q = new TermQuery(new Term("topic1Id",topic.getId()));
			 indexDir = DirectoryReader.open(SimpleFSDirectory.open(new File("index"+File.separator+"topicRelation")));
			 IndexSearcher searcherIndex = new IndexSearcher(indexDir);
			 
			 TopDocs docs = searcherIndex.search(q, 1000);
			 
			 for (ScoreDoc scoreDoc : docs.scoreDocs) 
			 {
				 Document doc = searcherIndex.doc(scoreDoc.doc);
				 String proprieta="",topic2="";
				 
				 for (IndexableField field : doc.getFields())
				 {
					 if (field.name().equals("typeProperty"))
						 proprieta = doc.get(field.name());
							else
								if (field.name().equals("topic2Id"))
									topic2 = getTopic(doc.get(field.name())).getLabel();		
				 }
				 //if(proprieta.equals("fictional_universe.fictional_character.married_with") && topic2!=null)
				 System.out.println(proprieta+"= "+topic2);
			}
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[])
	{

	}

}
