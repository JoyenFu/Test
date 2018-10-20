package testsub1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apdplat.word.segmentation.Word;
import org.apdplat.word.util.AutoDetector;
import org.apdplat.word.util.ResourceLoader;
import org.apdplat.word.util.WordConfTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StopWord {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopWord.class);
    private static final Set<String> stopwords = new HashSet<>();
    static{
        reload();
    }
    static{
        reload();
    }
    public static void reload(){
        AutoDetector.loadAndWatch(new ResourceLoader(){

            @Override
            public void clear() {
                stopwords.clear();
            }

            @Override
            public void load(List<String> lines) {
                LOGGER.info("初始化停用词");
                for(String line : lines){
                    stopwords.add(line);
                }
                LOGGER.info("停用词初始化完毕，停用词个数："+stopwords.size());
            }

            @Override
            public void add(String line) {
                stopwords.add(line);
            }

            @Override
            public void remove(String line) {
               stopwords.remove(line);
            }
        
        }, WordConfTools.get("stopwords.path", "classpath:stopwords.txt"));
    }

    /**
     * 判断一个词是否是停用词
     * @param word
     * @return 
     */
    public static boolean is(String word){      
        if(word == null){
            return false;
        }
        word = word.trim();
        return stopwords.contains(word);
    }
    /**
     * 停用词过滤，删除输入列表中的停用词
     * @param words 词列表
     */
    public static void filterStopWords(List<Word> words){
        Iterator<Word> iter = words.iterator();
        while(iter.hasNext()){
            Word word = iter.next();
            if(is(word.getText())){
                //去除停用词
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("去除停用词：" + word.getText());
                }
                iter.remove();
            }
        }
    }

    public static void main(String[] args){
        LOGGER.info("停用词：");
        int i=1;
        for(String w : stopwords){
            LOGGER.info((i++)+" : "+w);
        }
    }
}