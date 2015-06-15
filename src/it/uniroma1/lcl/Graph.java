package it.uniroma1.lcl;

import it.uniroma1.lcl.pensieroprofondo.search.Searcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Graph - dato in input una lista di topic e una lista di type,
 * crea il grafo delle connessioni.  
 * 
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 */
public class Graph 
{
	/** lista dei nodi */
	public List<Object> nodes; 
	/** lista degli archi */
	public Map<Object,Object> arcs; 
	
 public Graph(List<Topic> topicList, List<Type>typeList) 
 {
	 System.out.println("Creo il grafo...");
	 
	 if (topicList == null)
		 topicList = new ArrayList<>();
	 if (typeList == null)
		 typeList = new ArrayList<>();	 
  
	 /** inserisco i nodi nel grafo */
	 nodes = new ArrayList<Object>();
	
	 arcs = new HashMap<Object,Object>();
	 for(Topic to : topicList)
		 nodes.add(to);
	 for(Type ty : typeList)
		 nodes.add(ty);
  
	 /** cerco gli archi del grafo */
	 Searcher searcher = Searcher.getInstance();
  
	 for(Object o1 : nodes)
		 for(Object o2 : nodes)
		 {
			 /** sono entrambi Topic, cerco le property associate */
			 if(o1 instanceof Topic && o2 instanceof Topic)
			 {
				 List<Object> arcList = (List<Object>)searcher.getQuery(null,(Topic)o1, null,(Topic)o2); 	 
				 if(arcList!=null)
					 for (Object arc : arcList)
						 arcs.put(new Object[]{o1,o2} , arc); // inserisco la coppia di nodi e l'arco relativo (chiamato con il nome della property). 
			 }
			 else if (o1 instanceof Topic && o2 instanceof Type)
			 {
				 List<Object> listRelations = (List<Object>)searcher.getQuery((Type)o2,(Topic)o1,null, null); 	 
				 if(listRelations!=null)
				 {
					 for(Object s:listRelations)
					 {
						 arcs.put(new Object[]{o1,o2} , (String)s); // inserisco la coppia di nodi e l'arco relativo	 				
					 }
				 }
			 }
			 else if (o1 instanceof Type && o2 instanceof Topic)
			 {
				 List<Object> listRelations=(List<Object>)searcher.getQuery((Type)o1,null, null,(Topic)o2); 
				 if(listRelations!=null) 
				 {
					 for(Object s:listRelations)
					 {
						 arcs.put(new Object[]{o1,o2} , (String)s); // inserisco la coppia di nodi e l'arco relativo	 				
					 }
				 }
			 }
	
			 else if (o1 instanceof Type && o2 instanceof Type)
			 {
				 //NON GESTITO!
			 }
		 }
	  
  }
 
 /** Semplice metodo per la stampa dei nodi del grafo */
 public void printNodes()
 {
	 System.out.println("Nodi del grafo:");
	
	 for(Object o : nodes)
	 {
		 if(o instanceof Topic)
			 //System.out.println(((Topic)o)+" "+((Topic)o).getLabel()); 
		 System.out.println(((Topic)o)); 
		 else
			 if(o instanceof Type)
				 System.out.println(((Type)o).toString());
	 }
 }
 
 /** Stampa archi appartenenti al grafo */
 public void printArcs()
 {
  
	 	System.out.println("Archi del grafo:");
	 	
	 	for (Map.Entry<Object, Object> entry : arcs.entrySet()) 
	 	{
	 		Object[] nodeCouple = (Object[])entry.getKey();
	 		String value = (String)entry.getValue();
	 		String[] nodes = new String[2];
	 		
	 		for(int i=0;i<2;i++)
	 		{
	 			if(nodeCouple[i] instanceof Topic)
	 				nodes[i] = ((Topic)nodeCouple[i]).getId()+" "+((Topic)nodeCouple[i]).getLabel();
	 			else
	 				if(nodeCouple[i] instanceof Type)
	 					nodes[i] = ((Type)nodeCouple[i]).getType();
	 		}
	 		
	 		System.out.println(nodes[0]+" ---"+value+"---> "+nodes[1]); 
	}
 }
 
 public static void main(String args[])
 {
	// char c=System.getProperty('\t').toCharArray()[0];
	 System.out.println((int)'\t');
 }
}
