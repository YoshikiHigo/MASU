package jp.ac.osaka_u.ist.sel.metricstool.sample;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;
import library.ClassPathTest;


/**
 * �v���O�C���T���v��
 *  
 * @author kou-tngt
 */
public class SamplePlugin extends AbstractPlugin {

    @Override
    protected LANGUAGE[] getMeasurableLanguages() {
        //Enum��S���擾
        LANGUAGE[] languages = LANGUAGE.values();
        List<LANGUAGE> resultList = new ArrayList<LANGUAGE>();
        
        for(LANGUAGE language : languages){
            if (language.isObjectOrientedLanguage()){
                //Enum�̌���ŃI�u�W�F�N�g�w������Ȃ��̂��������X�g�ɋl�߂�
                resultList.add(language);
            }
        }
        
        //�߂�l�p�̔z�������ĕԂ��D
        LANGUAGE[] resultArray = new LANGUAGE[resultList.size()];
        return resultList.toArray(resultArray);
    }


    @Override
    protected String getMetricsName() {
        return "sample";
    }

    @Override
    protected METRICS_TYPE getMetricsType() {
        return METRICS_TYPE.FILE_METRICS;
    }

    @Override
    protected void execute() {
        System.out.println("This is plugin sample.");
        System.out.println("Measure Sample Metrics!");
        System.out.println();
        System.out.println("======================");
        System.out.println("Start classpath test");
        System.out.println("======================");
        System.out.println("Instanciate ClassPathTest ...");
        
        ClassPathTest test = new ClassPathTest();
        System.out.println("success!");
        System.out.println();
        System.out.println(test);
    }
}
