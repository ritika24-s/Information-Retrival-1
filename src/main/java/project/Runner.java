package project;


import java.util.*;

public class Runner {
    private String get_results(String analyzer,String similarity) {
        Parser parser= new Parser();
        IndexCreator indexCreator = new IndexCreator(analyzer,similarity);
        Querier searcher = new Querier();
        List<Map<String,String>> cranQueryList= parser.parseQuery();
        List<Map<String,String>> cranList= parser.parseDoc();
        indexCreator.createIndex(cranList);
        return searcher.searchQueries(cranQueryList, indexCreator);
    }

    public static void main(String[] args) {
        Runner runner= new  Runner();
        System.out.println("Starting indexing");

        ArrayList<String> similarities= new ArrayList();
        similarities.add("ClassicSimilarity");
        similarities.add("BM25");
        similarities.add("BooleanSimilarity");
        similarities.add("LMDirichletSimilarity");

        ArrayList<String> analyzer= new ArrayList();
        analyzer.add("English_Analyzer");
        analyzer.add("Classic_Analyzer");
        analyzer.add("Standard_Analyzer");

        for (String an:analyzer
        ) {
            for (String sim:similarities
            ) {
                String path=runner.get_results(an,sim);
                System.out.format(an + " with " + sim + " is stored in " + path + "\n");
            }
        }





    }
}
