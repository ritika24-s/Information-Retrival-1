package project;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;    
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ListQuery {
    private int hitsPerPage=100;
    private  String filePath="";

    public String searchCranQueries(List<Map<String, String>> cranQueryList, Indexer indexer)
    {
        int i=0;
        Map<String, List<String>> resultDict;
        resultDict = new HashMap<String, List<String>>();
        try {


            Directory directory= FSDirectory.open(Paths.get("src/main/resources/index"));
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
            isearcher.setSimilarity(indexer.getSimilarity());
            Analyzer analyzer= indexer.getAnalyzer();
            IndexWriterConfig config= new IndexWriterConfig(analyzer);
            List<String> resFileContent = new ArrayList<String>();
            for (Map<String, String> cranQuery:
                    cranQueryList ) {
                i++;
                MultiFieldQueryParser queryParser= new MultiFieldQueryParser( new String[]{"Title","Bibliography","Author","Content"},analyzer);
                Query query = queryParser.parse(queryParser.escape(cranQuery.get("QUERY")));
                TopDocs topDocs = isearcher.search(query,hitsPerPage);
                ScoreDoc[] hits= topDocs.scoreDocs;
                List<String> resultList = new ArrayList<String>();
                for(int j = 0; j < hits.length; j++) {

                    int docId = hits[j].doc;
                    Document doc = isearcher.doc(docId);
                    resultList.add(doc.get("ID"));
                    resFileContent.add(cranQuery.get("ID") + " 0 " + doc.get("ID") + " 0 " + hits[j].score + " STANDARD");
                }
                resultDict.put(Integer.toString(i + 1), resultList);
                File outputDir = new File("output");
                if (!outputDir.exists()) outputDir.mkdir();
                filePath="output/results"+"_"+indexer.getAnalyzerArg().replace(" ","")+"_"+indexer.getSimilarityArg().replace(" ","")+".txt";

                Files.write(Paths.get(filePath), resFileContent, Charset.forName("UTF-8"));
//                System.out.println("Results written to output/results.txt to be used in TREC Eval.");
            }

        } catch ( IOException | org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
