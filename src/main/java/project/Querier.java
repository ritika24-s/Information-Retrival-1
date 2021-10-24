package project;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Querier {
    private  String filePath="";
    private int hitspp;
    private int docId;
    private IndexSearcher isearcher;
    private DirectoryReader directoryReader;
    private ScoreDoc[] scoreDocs;

    public String searchQueries(List<Map<String, String>> cranQueryList, IndexCreator indexCreator)
    {
        int i=0;
        Map<String, List<String>> resultDict;
        resultDict = new HashMap<>();

        try {
            Directory directory= FSDirectory.open(Paths.get("src/main/resources/index"));
            directoryReader = DirectoryReader.open(directory);
            isearcher = new IndexSearcher(directoryReader);
            isearcher.setSimilarity(indexCreator.getSimilarity());
            Analyzer analyzer= indexCreator.getAnalyzer();
            IndexWriterConfig config= new IndexWriterConfig(analyzer);
            List<String> resFileContent = new ArrayList<>();
            for (Map<String, String> cranQuery:
                    cranQueryList ) {
                i++;
                MultiFieldQueryParser queryParser= new MultiFieldQueryParser( new String[]{"Title","Bibliography","Author","Content"},analyzer);
                Query query = queryParser.parse(QueryParserBase.escape(cranQuery.get("QUERY")));
                hitspp = 1400;
                TopDocs topDocs = isearcher.search(query, hitspp);
                scoreDocs = topDocs.scoreDocs;
                List<String> resultList = new ArrayList<>();
                for (ScoreDoc hit : scoreDocs) {

                    docId = hit.doc;
                    Document doc = isearcher.doc(docId);
                    resultList.add(doc.get("ID"));
                    resFileContent.add(cranQuery.get("ID") + " 0 " + doc.get("ID") + " 0 " + hit.score + " STANDARD");
                }
                resultDict.put(Integer.toString(i + 1), resultList);
                File outputDir = new File("output");
                if (!outputDir.exists()) outputDir.mkdir();
                filePath="output/"+"_"+ indexCreator.getAnalyzerArg().replace(" ","")+"_"+ indexCreator.getSimilarityArg().replace(" ","")+".txt";

                Files.write(Paths.get(filePath), resFileContent, StandardCharsets.UTF_8);
            }

        } catch ( IOException | org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
