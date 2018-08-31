package topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import bolts.WordCounter2;
import bolts.WordNormalizer2;
import spouts.WordReader2;  
public class WordCountTopologyMain {  
    public static void main(String[] args) throws InterruptedException {  
        //定义一个Topology  
        TopologyBuilder builder = new TopologyBuilder();  
        builder.setSpout("word-reader",new WordReader2());  
        builder.setBolt("word-normalizer", new WordNormalizer2())  
        .shuffleGrouping("word-reader");  
        builder.setBolt("word-counter", new WordCounter2(),2)  
        .fieldsGrouping("word-normalizer", new Fields("word"));  
        //配置  
        Config conf = new Config();  
//        conf.put("wordsFile", "src/main/resources/words.txt"); 
        String filePath = WordCountTopologyMain.class.getClassLoader().getResource("words.txt").getPath();
        System.out.println(filePath);
        conf.put("wordsFile",filePath);
        conf.setDebug(false);  
        //提交Topology  
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);  
        //创建一个本地模式cluster  
        LocalCluster cluster = new LocalCluster();  
        cluster.submitTopology("Getting-Started-Toplogie", conf,  
        builder.createTopology());  
        Thread.sleep(1000);  
        cluster.shutdown();  
    }  
}  