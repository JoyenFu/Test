package testsub1;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TestOcr {

	public static void main(String[] args) {
		
		File imageFile = new File("C:\\Users\\yto_fwj\\Pictures\\testorc.png");
        ITesseract instance = new Tesseract();     
        instance.setDatapath("C:\\Program Files (x86)\\Tesseract-OCR\\tessdata");
        // 默认是英文（识别字母和数字），如果要识别中文(数字 + 中文），需要制定语言包
        instance.setLanguage("chi_sim");
        //instance.set
        try{
        	long btime = System.currentTimeMillis();
            String result = instance.doOCR(imageFile);
            long etime = System.currentTimeMillis();
            System.out.println("文字识别耗时：" + (etime-btime)/1000.0 + "s");
            System.out.println();
            System.out.println(result);
        }catch(TesseractException e){
            System.out.println(e.getMessage());
        }

	}
}
