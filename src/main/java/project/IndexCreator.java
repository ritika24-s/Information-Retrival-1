package project;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.document.StringField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.util.Map;

public class IndexCreator {
    private final String analyzerArg;
    private final String similarityArg;
    private Analyzer analyzer;
    private Similarity similarity;





    public IndexCreator(String analyzer, String similarity) {
        this.analyzerArg=analyzer;
        this.similarityArg=similarity;

    }


    public void createIndex(List<Map<String, String>> cranlist)
    {
        try {

            List<String> stopWordList = Arrays.asList("a", "an", "and", "are", "as", "at", "be",
                    "but", "by",
                    "for", "if", "in", "into", "is", "it",
                    "no", "not", "of", "on", "or", "such",
                    "that", "the", "their", "then", "there", "these",
                    "they", "this", "to", "was", "will", "with");
            CharArraySet stopWordSet = new CharArraySet( stopWordList, true);


            Directory directory= FSDirectory.open(Paths.get("src/main/resources/index"));
            analyzer = new StandardAnalyzer();

            switch (analyzerArg) {
                case "English_Analyzer":
                    analyzer = new EnglishAnalyzer(stopWordSet);
                    break;
                case "Standard_Analyzer":
                    analyzer = new StandardAnalyzer(stopWordSet);
                    break;
                case "Classic_Analyzer":
                    analyzer = new ClassicAnalyzer(stopWordSet);
                    break;
            }

            IndexWriterConfig config= new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            switch (similarityArg) {
                case "BM25":
                    similarity = new BM25Similarity();
                    break;
                case "LMDirichletSimilarity":
                    similarity = new LMDirichletSimilarity();
                    break;
                case "ClassicSimilarity":
                    similarity = new ClassicSimilarity();
                    break;
                case "BooleanSimilarity":
                    similarity = new BooleanSimilarity();
                    break;
            }


            config.setSimilarity(similarity);
            IndexWriter iWriter = new IndexWriter(directory, config);
            for (Map<String, String> cranDict:cranlist
            ) {
                Document doc = new Document();
                doc.add(new StringField("ID", cranDict.get("ID"), Field.Store.YES));
                doc.add(new TextField("Title", cranDict.get("Title"), Field.Store.YES));
                doc.add(new TextField("Bibliography", cranDict.get("Bibliography"), Field.Store.YES));
                doc.add(new TextField("Author", cranDict.get("Author"), Field.Store.YES));
                doc.add(new TextField("Content", cranDict.get("Content"), Field.Store.NO));
                iWriter.addDocument(doc);
            }


            iWriter.close();
            directory.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Similarity getSimilarity() {
        return similarity;
    }
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public String getAnalyzerArg() {
        return analyzerArg;
    }

    public String getSimilarityArg() {
        return similarityArg;
    }

}