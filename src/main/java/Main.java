import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.text.ParseException;

public class Main {
	private static final String datasetDir = "/ubuntu_dialogs_full/3";

	public static void main(String[] args) throws ParseException, SolrServerException, IOException {
		String url = "http://localhost:8983/solr";
		String collection = "chats";

		//Create collection example
//		SolrService solrService = new SolrService(url);
//		solrService.createCollection(collection);

//		SolrService solrService = new SolrService(url + "/" + collection);

		//Delete example
//		solrService.deleteDocument("0_1");

		//Insert example
//		Set usernames = new HashSet();
//		usernames.add("crimsun");
//		usernames.add("stuNNed");
//		solrService.insertDocument("0_1",
//				FileService.sdf.parse("2004-11-23T11:49:00.000Z"),
//				usernames,
//				"any ideas why java plugin takes so long to load?".toLowerCase(),
//				"any ideas why java plugin takes so long to load? java 1.4? yes java 1.5 loads _much_ faster".toLowerCase());
	}
}
