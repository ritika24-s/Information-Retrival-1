CSI73 : Information Retreival and Web Search Assignment 1: Lucene and Cranfield

Project Directory : 
src/main/java -- 
                |-- indexer.java 
                |-- ListQuery.java 
                |-- Parser.java 
                |-- Runner.java cran : 
Contains all the Cranfield files output : Contains all the outputs

To run and build the code 
Step 1 - mvn clean install 
Step 2 - mvn compile 
Step 3 - mvn exec:java -Dexec.mainClass=com.sharmari.irlucene.Runner 
Step 4 - trec_eval ./cran/QRelsCorrectedforTRECeval output/
