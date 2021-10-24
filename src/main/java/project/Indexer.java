package project;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import java.util.Map;

public class Indexer {
    private  String analyzerArg;
    private  String similarityArg;
    private Analyzer analyzer;
    private Similarity similarity;

    public String getAnalyzerArg() {
        return analyzerArg;
    }

    public String getSimilarityArg() {
        return similarityArg;
    }

    public Similarity getSimilarity() {
        return similarity;
    }
    public Analyzer getAnalyzer() {
        return analyzer;
    }



    public Indexer(String analyzer, String similarity) {
        this.analyzerArg=analyzer;
        this.similarityArg=similarity;

    }


    public void createIndex(List<Map<String, String>> cranlist)
    {
        try {

            List<String> stopWordList = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
            CharArraySet stopWordSet = new CharArraySet( stopWordList, true);


            Directory directory= FSDirectory.open(Paths.get("src/main/resources/index"));
            analyzer = new StandardAnalyzer();

            if (analyzerArg.equals("English_Analyzer"))
                analyzer = new EnglishAnalyzer(stopWordSet);
            else if (analyzerArg.equals("Standard_Analyzer"))
                analyzer= new StandardAnalyzer(stopWordSet);
            else if (analyzerArg.equals("Classic_Analyzer"))
                analyzer= new ClassicAnalyzer(stopWordSet);

            IndexWriterConfig config= new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            if (similarityArg.equals("BM25"))
                similarity= new BM25Similarity();
            else if (similarityArg.equals("LMDirichletSimilarity"))
                similarity= new LMDirichletSimilarity();
            else if (similarityArg.equals("ClassicSimilarity"))
                similarity= new ClassicSimilarity();
            else if (similarityArg.equals("BooleanSimilarity"))
                similarity = new BooleanSimilarity();


            config.setSimilarity(similarity);
            IndexWriter iWriter = new IndexWriter(directory, config);
            for (Map<String, String> cranDict:cranlist
            ) {
                Document document = new Document();
                document.add(new StringField("ID", cranDict.get("ID"), Field.Store.YES));
                document.add(new TextField("Title", cranDict.get("Title"), Field.Store.YES));
                document.add(new TextField("Bibliography", cranDict.get("Bibliography"), Field.Store.YES));
                document.add(new TextField("Author", cranDict.get("Author"), Field.Store.YES));
                document.add(new TextField("Content", cranDict.get("Content"), Field.Store.YES));
                iWriter.addDocument(document);
            }


            iWriter.close();
            directory.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}