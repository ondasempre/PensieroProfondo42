package pensieroprofondo;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class LuceneTest 
{
	public LuceneTest()
	{
		
	}
	
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException
	{
		Document doc = new Document();
		doc.add(new StringField("nome campo", "valore1", Field.Store.YES));
		doc.add(new StringField("nome campo2", "valore2", Field.Store.YES));
		doc.add(new StringField("nome campo3", "valore3", Field.Store.YES));
		
		File index = new File("nome_indice");
		
		StandardAnalyzer standard = new StandardAnalyzer(Version.LUCENE_48);
						
		Directory dir = new SimpleFSDirectory(index);
		IndexWriter out = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_48, standard)); 
		
	
		
	}
}