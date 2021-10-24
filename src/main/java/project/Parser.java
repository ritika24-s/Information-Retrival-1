package project;

import java.io.FileReader;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Parser {
    public List<Map<String,String>> parseDoc()
    {
        List<Map<String, String>> dataList= new ArrayList<>();
        File file=new File("src/main/resources/cranlist/cran.all.1400");
        try {
            FileReader reader= new FileReader(file);
            Map<String, String> cranDict = new HashMap<>();

            String sentence;
            String token=Constants.Id;
            int lineNum=0;
            String id = "";
            StringBuilder title = new StringBuilder();
            StringBuilder author = new StringBuilder();
            StringBuilder bibliography = new StringBuilder();
            StringBuilder content = new StringBuilder();
            Scanner scanner= new Scanner(file);
            while (scanner.hasNextLine())
            {
                lineNum++;
                sentence = scanner.nextLine();
                String[] words = sentence.split("\\s+");
                switch (words[0]) {
                    case Constants.Id:
                        if (lineNum > 1) {
                            cranDict.put("ID", id);
                            cranDict.put("Content", content.toString());
                            dataList.add(cranDict);
                            cranDict = new HashMap<>();
                        }
                        id = words[1];
                        content = new StringBuilder();
                        token = Constants.Title;

                        break;
                    case Constants.Title:
                        token = Constants.Author;
                        break;
                    case Constants.Author:
                        if (token != Constants.Author) {
                        }
                        cranDict.put("Title", title.toString());
                        title = new StringBuilder();
                        token = Constants.Bibliography;
                        break;
                    case Constants.Bibliography:
                        cranDict.put("Author", author.toString());
                        author = new StringBuilder();
                        token = Constants.Description;
                        break;
                    case Constants.Description:
                        cranDict.put("Bibliography", bibliography.toString());
                        bibliography = new StringBuilder();
                        token = Constants.Id;
                        break;
                    default:
                        switch (token) {
                            case ".A":
                                title.append(sentence).append(" ");
                                break;
                            case ".B":
                                author.append(sentence).append(" ");
                                break;
                            case ".W":
                                bibliography.append(sentence).append(" ");
                                break;
                            case ".I":
                                content.append(sentence).append(" ");
                                break;
                        }

                        break;
                }

            }

            cranDict.put("ID", id);
            cranDict.put("Content", content.toString());
            dataList.add(cranDict);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Create a dictionary to store the values


        return dataList;
    }


    public List<Map<String,String>> parseQuery()
    {

        List<Map<String,String>> queryList= new ArrayList<>();
        String nextline1="";
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/cranlist/cran.qry"));
            StringBuilder query = new StringBuilder();
            String line = "";
            int queryNum = 1;
            while (scanner.hasNextLine()) {

                Map<String, String> querymap = new HashMap<>();
                if (nextline1.equals("") )line = scanner.nextLine();
                else line=nextline1;


                String[] words = line.split("\\s+");
                if (Constants.Id.equals(words[0])) {
                    String nextline = scanner.nextLine();
                    if (nextline.equals(Constants.Description)) {
                        querymap.put("ID", Integer.toString(queryNum));
                        queryNum++;
                        query = new StringBuilder(scanner.nextLine());
                        nextline1 = scanner.nextLine();
                        if (nextline1.split("\\s+")[0].equals(Constants.Id)) {
//                                    query.replace("?","").replace("*","");
                            querymap.put("QUERY", query.toString());
                            query = new StringBuilder();
                            queryList.add(querymap);
                            continue;
                        } else {
                            while (!nextline1.split("\\s+")[0].equals(Constants.Id) && scanner.hasNextLine()) {
                                query.append(" ").append(nextline1);
                                nextline1 = scanner.nextLine();
                            }
                            if (!scanner.hasNextLine())
                                query.append(" ").append(nextline1);
                        }
//                                query.replace("?","").replace("*","");
                        querymap.put("QUERY", query.toString());
                        query = new StringBuilder();
                        queryList.add(querymap);
                    }
                }
            }

        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        return queryList;
    }



    public static void main(String[] args)
    {

        Parser fileParser= new Parser();
        List<Map<String, String>> list = fileParser.parseDoc();
        System.out.println(list.size());
        for (Map<String,String> mapobj:list
        ) {
            System.out.println(mapobj.get("ID"));
            System.out.println(mapobj.get("Content"));
        }

    }
}
