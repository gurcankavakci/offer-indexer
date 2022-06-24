import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

public class Main {
	private static final String datasetDir = "/ubuntu_dialogs_full/30";
	private static String url = "http://localhost:8983/solr";
	private static String collection = "chats";

	public static void main(String[] args) throws ParseException, SolrServerException, IOException {
		init();
		SolrService solrService = insertExampleDoc();
		FileService.readAndIndexFile(datasetDir,solrService);
//		solrService.deleteDocuments();
	}

	public static void init() throws SolrServerException, IOException {
//		Create collection example
		SolrService solrService = new SolrService(url);
		try {
			solrService.createCollection(collection);
		} catch (Exception e) {
//			System.out.println(e);
		}
	}

	public static SolrService insertExampleDoc() throws SolrServerException, IOException, ParseException {
		SolrService solrService = new SolrService(url + "/" + collection);

//		Delete example
		solrService.deleteDocument("0_1");

//		Insert example
		Set usernames = new HashSet();
		usernames.add("crimsun");
		usernames.add("stuNNed");
		solrService.insertDocument("0_1",
				FileService.sdf.parse("2004-11-23T11:49:00.000Z"),
				usernames,
				"hugging such that fishing any ideas why java plugin takes so long to load?".toLowerCase(),
				"hugging such that fishing any ideas why java plugin takes so long to load?\njava 1.4?\nyes java 1.5\nloads _much_ faster".toLowerCase());

		return solrService;
	}
}
