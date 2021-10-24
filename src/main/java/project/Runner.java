package project;


import java.util.*;

public class Runner {
    private String get_results(String analyzer,String similarity) {
        Parser fileIndexer= new Parser();
        Indexer indexer= new Indexer(analyzer,similarity);
        List<Map<String,String>> cranQueryList= fileIndexer.parseQuery();
        List<Map<String,String>> cranList= fileIndexer.parseDoc();
        indexer.createIndex(cranList);
        ListQuery searcher = new ListQuery();
        return searcher.searchCranQueries(cranQueryList, indexer);
    }

    public static void main(String args[]) {
        Runner runner= new  Runner();
        System.out.println("Welcome to Information Retrival");
        System.out.println("You should be looking for analyzers and similarities that are used on the Cranfield data to search and index the file ");
        System.out.println("Starting with indexing now");

        List<String> similarities= new ArrayList();
        similarities.add("BM25");
        similarities.add("LMDirichletSimilarity");
        similarities.add("ClassicSimilarity");
        similarities.add("BooleanSimilarity");

        List<String> analyzer= new ArrayList();
        analyzer.add("English_Analyzer");
        analyzer.add("Standard_Analyzer");
        analyzer.add("Classic_Analyzer");

        System.out.println("*****************************************************************************************************************");
        System.out.format("|%20s  |%24s  |%60s |\n","Analyzers","SIMILARITY","RESULTS SAVED AT");
        System.out.println("*****************************************************************************************************************");
        for (String an:analyzer
        ) {
            for (String sim:similarities
            ) {
                String path=runner.get_results(an,sim);
                System.out.format("|%20s  |%24s  |%60s |\n",an,sim,path);
            }
            System.out.println("*****************************************************************************************************************");
        }





    }
}
