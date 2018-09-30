package testsub1;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

public class WordFenCi {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<Word> words = WordSegmenter.segWithStopWords("上海上海市浦东新区国际中心");
        System.out.println(words);
    }

}
