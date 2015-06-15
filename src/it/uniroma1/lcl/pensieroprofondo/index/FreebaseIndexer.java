package it.uniroma1.lcl.pensieroprofondo.index;

import it.uniroma1.lcl.Type;
import it.uniroma1.lcl.pensieroprofondo.record.TopicLabelRecord;
import it.uniroma1.lcl.pensieroprofondo.record.TopicRelationRecord;
import it.uniroma1.lcl.pensieroprofondo.record.TopicTypeRecord;
import it.uniroma1.lcl.pensieroprofondo.record.TypeInstanceRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/** 
 * Classe FREEBASEINDEXER - si occupa della creazione dell'indice utilizzando le API di Lucene.
 * Si creano più indici, a seconda dell'oggetto da classificare.
 * Mediamente sono richiesti 7-10 minuti per creare l'indice su Mac OS X i7 2.3Ghz.
 * 
 * @author ondasempre@gmail.com
 * @author elisa.magalotti@gmail.com
 *
 */
public class FreebaseIndexer 
{
 
	/** nome del file delle triple .gz */
	private String nomeFileGz;
	/** campo relativo al nome della cartella index */
	private String cartellaDestinazione;
	/** Oggetto parser */
	private Parser parser;
    /** campo relativo alla gestione dell'orario inizio-fine indicizzazione */
	private SimpleDateFormat dateFormat;
	
	/** Costruttore al quale passiamo il nome del file .gz e la cartella di destinazione dell'indice. */
	public FreebaseIndexer(String nomeFileGz, String cartellaDestinazione)
	{
		this.nomeFileGz = nomeFileGz;
		this.cartellaDestinazione = cartellaDestinazione;
		/** Richiamo l'istanza del parser */
		parser = Parser.getInstance();
		
		dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss.SSS");
		
	}
	
	/** Funzione per creare l'indice/i con API Apache Lucene */
	public void index() throws IOException
	{
		/** Stampo l'ora in cui inizia l'indicizzazione */
		System.out.println("INIZIO INDICIZZAZIONE "+dateFormat.format(Calendar.getInstance().getTime()));
		
		/**̀ l’oggetto Analyzer si occupa di suddivide lo stream dei dati in token per analizzare il testo dei documenti */
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		
		/** crea la cartella degli indici */
		IndexWriterConfig topicIndexConfig = new IndexWriterConfig(Version.LUCENE_48, analyzer);
		File topicIndex = new File(cartellaDestinazione+File.separator+"topic");
		Directory topicIndexDir = new SimpleFSDirectory(topicIndex);
		/** Punto di accesso all'indice */
		IndexWriter topicIndexWriter = new IndexWriter(topicIndexDir, topicIndexConfig);

		IndexWriterConfig topicTypeIndexConfig = new IndexWriterConfig(Version.LUCENE_48, analyzer);
		File topicTypeIndex = new File(cartellaDestinazione+File.separator+"topicType");
		Directory topicTypeIndexDir = new SimpleFSDirectory(topicTypeIndex);
		IndexWriter topicTypeIndexWriter = new IndexWriter(topicTypeIndexDir, topicTypeIndexConfig);

		IndexWriterConfig typeTopicIndexConfig = new IndexWriterConfig(Version.LUCENE_48, analyzer);
		File typeTopicIndex = new File(cartellaDestinazione+File.separator+"typeTopic");
		Directory typeTopicIndexDir = new SimpleFSDirectory(typeTopicIndex);
		IndexWriter typeTopicIndexWriter = new IndexWriter(typeTopicIndexDir, typeTopicIndexConfig);

		IndexWriterConfig topicRelationIndexConfig = new IndexWriterConfig(Version.LUCENE_48, analyzer);
		File topicRelationIndex = new File(cartellaDestinazione+File.separator+"topicRelation");
		Directory topicRelationIndexDir = new SimpleFSDirectory(topicRelationIndex);
		IndexWriter topicRelationIndexWriter = new IndexWriter(topicRelationIndexDir, topicRelationIndexConfig);

		FileInputStream fin = new FileInputStream(nomeFileGz);
		GZIPInputStream gzis = new GZIPInputStream(fin);
		
		/** Meglio specificare la codifica del flusso di lettura per non sollevare eccezioni. */
		InputStreamReader isr = new InputStreamReader(gzis, "UTF8");
		
		BufferedReader br = new BufferedReader(isr);

		String line = "";
		
		/** Inizio lettura del file delle triple riga per riga. */
		while(br.ready())
		{
			line = br.readLine();
			
			/** Utilizzo l'oggetto parser per estrarre l'informazione dalla tripla */
			Object o = parser.extractInformationFrom(line);
			
			if (o != null)
			{
				/** Riconosco che l'oggetto è di tipo topicLabelRecord*/
				if (o instanceof TopicLabelRecord) 
				{
					TopicLabelRecord topicLabelRecord = (TopicLabelRecord) o;
					
					addTopicDoc(topicIndexWriter, topicLabelRecord.getTopicId(), topicLabelRecord.getLabel());
					
				/** Riconosco che l'oggetto è di tipo topicLabelRecord*/
				} else if (o instanceof TopicTypeRecord) {
					
					TopicTypeRecord topicTypeRecord = (TopicTypeRecord) o;
				
					addTopicTypeDoc(topicTypeIndexWriter, topicTypeRecord.getTopicId(), topicTypeRecord.getType());
					
				/** Riconosco che l'oggetto è di tipotypeIstanceRecordRecord*/	
				} else if (o instanceof TypeInstanceRecord) {
					
					TypeInstanceRecord typeInstanceRecord = (TypeInstanceRecord) o;


			
					addTypeTopicDoc(typeTopicIndexWriter, typeInstanceRecord.getType(), typeInstanceRecord.getTopicId());
				}
				/** Riconosco che l'oggetto è di tipo topicRelationRecord*/
					else if (o instanceof TopicRelationRecord) {
									
						TopicRelationRecord topicRelationRecord = (TopicRelationRecord) o;
						addTopicRelationDoc(topicRelationIndexWriter, topicRelationRecord.getTopic1Id(), topicRelationRecord.getTypeProperty(), topicRelationRecord.getTopic2Id());
				}
			}	
		}
		
		fin.close();
		gzis.close();
		isr.close();
		br.close();

		topicIndexWriter.close();

		topicTypeIndexWriter.close();

		typeTopicIndexWriter.close();

		topicRelationIndexWriter.close();
		/** Stampo l'ora di fine indicizzazione */
		System.out.println("FINE INDICIZZAZIONE "+dateFormat.format(Calendar.getInstance().getTime()));
		
	}

	/** Aggiungo il topic all'index writer (punto di accesso all'indice) secondo id e label associati */
	private static void addTopicDoc(IndexWriter w, String id, String label) throws IOException 
	{
		Document doc = new Document();

		doc.add(new StringField("id", id.trim(), Field.Store.YES));
		doc.add(new StringField("label", label.trim(), Field.Store.YES));

		w.addDocument(doc);
	}

	/** Aggiungo al topic i type associati */
	private static void addTopicTypeDoc(IndexWriter w, String topic, Set<Type> typeSet) throws IOException 
	{
		Document doc = new Document();

		doc.add(new StringField("id", topic.trim(), Field.Store.YES));
		// use a string field for isbn because we don't want it tokenized
		Iterator<Type> it = typeSet.iterator();
		Type type = null;
		int i = 0;
		
		while (it.hasNext())
		{
			type = it.next();
			doc.add(new TextField("type"+i, type.getType().trim(), Field.Store.YES));
			i++;
		}

		w.addDocument(doc);
	}
	
	/** Aggiungo il type relativo a un certo topic */
	private static void addTypeTopicDoc(IndexWriter w, String type, Set<String> topicSet) throws IOException 
	{
		Document doc = new Document();

		doc.add(new TextField("type", type, Field.Store.YES));
		// use a string field for isbn because we don't want it tokenized
		Iterator<String> it = topicSet.iterator();
		String topicId = "";
		
		int i=0;
		
		while (it.hasNext())
		{
			topicId = it.next();
			doc.add(new StringField("topicId"+i, topicId.trim(), Field.Store.YES));
			i++;
		}

		w.addDocument(doc);
	}
	/** Aggiungo al topic un certo type secondo l'ID del type */
	private static void addTopicTypeDoc(IndexWriter w, String topic, String type) throws IOException 
	{
		Document doc = new Document();

		doc.add(new StringField("id", topic.trim(), Field.Store.YES));
		doc.add(new TextField("type", type.trim(), Field.Store.YES));

		w.addDocument(doc);
	}

	/** Incremento l'indice con i type relativi al topic */
	private static void addTypeTopicDoc(IndexWriter w, String type, String topicId) throws IOException 
	{
		Document doc = new Document();

		doc.add(new TextField("type", type.trim(), Field.Store.YES));
		doc.add(new StringField("topicId", topicId.trim(), Field.Store.YES));

		w.addDocument(doc);
	}
	
	/** Aggiungo come doc la relazione tra i topic */ 
	private static void addTopicRelationDoc(IndexWriter w, String topic1Id, String typeProperty, String topic2Id) throws IOException 
	{
		Document doc = new Document();
	
		String t1=topic1Id.trim(),t2=topic2Id.trim();
		
		doc.add(new StringField("topic1Id", t1, Field.Store.YES));
		doc.add(new TextField("typeProperty", typeProperty.trim(), Field.Store.YES));
		doc.add(new StringField("topic2Id", t2, Field.Store.YES));

		w.addDocument(doc);
	}


}
