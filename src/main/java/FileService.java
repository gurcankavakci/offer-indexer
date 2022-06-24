import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class FileService {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	public static void readAndIndexFile(String datasetDir, SolrService solrService) throws IOException {
		Files.list(Paths.get(datasetDir))
				.map(Path::getFileName)
				.map(Path::toString)
				.forEach(fileName -> {
					String solrId = "30_" + fileName.split("\\.")[0];
					if(!solrService.isDocumentExist(solrId)) {
						boolean isFirstLine = true;
						boolean isQuestionOK = false;
						String previousUser = null;
						Set<String> usernames = new HashSet<>();
						StringBuilder question = new StringBuilder();
						StringBuilder text = new StringBuilder();
						Date date = null;

						System.out.println(fileName);

						File file = new File(Paths.get(datasetDir, fileName).toString());

						try {
							Scanner inputStream = new Scanner(file);
							while (inputStream.hasNextLine()) {
								String data = inputStream.nextLine();
								String[] values = data.split("\t");
								if (values.length < 4) {
									continue;
								}

								usernames.add(values[1]);
								usernames.add(values[2]);

								if (isFirstLine) {
									date = sdf.parse(values[0]);
									question.append(values[3]);
									isFirstLine = false;
								} else if(!previousUser.equals(values[1])) {
									text.append("\n");
									isQuestionOK = true;
								} else {
									text.append(" ");

									if(!isQuestionOK){
										question.append(" ");
										question.append(values[3]);
									}
								}
								text.append(values[3]);
								previousUser = values[1];

							}
							inputStream.close();
							if (usernames.size() > 0 && !StringUtils.isEmpty(question.toString())) {
								solrService.insertDocument(solrId,
										date,
										usernames.stream().map(u -> u.toLowerCase(Locale.ENGLISH)).collect(Collectors.toSet()),
										question.toString().toLowerCase(Locale.ENGLISH),
										text.toString().toLowerCase(Locale.ENGLISH));
							}
						} catch (FileNotFoundException | ParseException e) {
							e.printStackTrace();
						} catch (SolrServerException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println(solrId + " is found skipping...");
					}
				});
	}
}
