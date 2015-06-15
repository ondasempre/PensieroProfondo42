package pensieroprofondo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class Index
{

	public Index(File file) throws IOException
	{
		FileInputStream fin = new FileInputStream(file);
		GZIPInputStream gzis = new GZIPInputStream(fin);
		InputStreamReader isr = new InputStreamReader(gzis);
		BufferedReader br = new BufferedReader(isr);
		
		
		while(br.ready())
		{
			System.out.println(br.readLine());
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		File f1 = new File("fb_triples_film.gz");
		new Index(f1);
	}
	
}
