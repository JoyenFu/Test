package testsub1;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.recognition.PersonName;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.segmentation.WordRefiner;

public class WordFenCi {

    public static void main(String[] args) {
        // 严格18233333333上海上海市浦东新区管委会
        // 严格18233333333上海上海市浦东新区国际中心
        //付文军18233333333陕西省西安市雁塔区大寨路99号宏府麒麟山9号楼2单元
        List<Word> words = WordSegmenter.segWithStopWords("段茵15249047756西安软件新城软件研发基地2期（A3-2）",SegmentationAlgorithm.MinimalWordCount);
        //words = WordRefiner.refine(words);
        //List<Word> nameWords = PersonName.recognize(words);
        System.out.println(words);
        //System.out.println(nameWords);
        
        System.out.println("陕西省西安市".indexOf("西安市"));
        
    }

}
