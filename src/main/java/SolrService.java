import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

public class SolrService {
	private HttpSolrClient solr;


	public SolrService(String url) {
		solr = new HttpSolrClient.Builder(url).build();
		solr.setParser(new XMLResponseParser());
	}

	public void insertDocument(String id, Date date, Set<String> users, String question, String text) throws ParseException, SolrServerException, IOException {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField("date", date);
		document.addField("users", users);
		document.addField("question", question);
		document.addField("text", text);
		solr.add(document);
		solr.commit();
	}

	public boolean isDocumentExist(String docId) {
		SolrDocument byId = null;
		try {
			byId = solr.getById(docId);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byId != null;
	}

	public void deleteDocument(String docId) throws SolrServerException, IOException {
		solr.deleteById(docId);
		solr.commit();
	}

	public void deleteDocuments() throws SolrServerException, IOException {
		solr.deleteByQuery("*");
		solr.commit();
	}

	public void createCollection(String collection) throws SolrServerException, IOException {
		CollectionAdminRequest.Create creator = CollectionAdminRequest.createCollection(collection, 1, 1);
		creator.process(solr);
	}
}
