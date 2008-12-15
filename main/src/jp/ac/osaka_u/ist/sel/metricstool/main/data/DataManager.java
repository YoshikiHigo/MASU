package jp.ac.osaka_u.ist.sel.metricstool.main.data;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;


/**
 * �S�Ẵ}�l�[�W���[���Ǘ�����}�l�[�W���[
 * 
 * @author higo
 *
 */
public class DataManager {

    /**
     * �f�[�^�}�l�[�W���[�̃C���X�^���X���擾����
     * 
     * @return�@�f�[�^�}�l�[�W���[�̃C���X�^���X
     */
    public static DataManager getInstance() {

        if (null == singleton) {
            singleton = new DataManager();
        }

        return singleton;

    }

    /**
     * �f�[�^�}�l�[�W���[�ɓo�^����Ă�������N���A����
     */
    public static void clear() {
        singleton = null;
    }

    /**
     * ClassMetricsInfoManager�@��Ԃ�
     * 
     * @return ClassMetricsInfoManager
     */
    public ClassMetricsInfoManager getClassMetricsInfoManager() {
        return this.classMetricsInfoManager;
    }

    /**
     * FieldMetricsInfoManager�@��Ԃ�
     * 
     * @return FieldMetricsInfoManager
     */
    public FieldMetricsInfoManager getFieldMetricsInfoManager() {
        return this.fieldMetricsInfoManager;
    }

    /**
     * FileMetricsInfoManager�@��Ԃ�
     * 
     * @return FileMetricsInfoManager
     */
    public FileMetricsInfoManager getFileMetricsInfoManager() {
        return this.fileMetricsInfoManager;
    }

    /**
     * MethodMetricsInfoManager�@��Ԃ�
     * 
     * @return MethodMetricsInfoManager
     */
    public MethodMetricsInfoManager getMethodMetricsInfoManager() {
        return this.methodMetricsInfoManager;
    }

    private DataManager() {
        this.classMetricsInfoManager = new ClassMetricsInfoManager();
        this.fieldMetricsInfoManager = new FieldMetricsInfoManager();
        this.fileMetricsInfoManager = new FileMetricsInfoManager();
        this.methodMetricsInfoManager = new MethodMetricsInfoManager();
    }

    private static DataManager singleton;

    final private ClassMetricsInfoManager classMetricsInfoManager;

    final private FieldMetricsInfoManager fieldMetricsInfoManager;

    final private FileMetricsInfoManager fileMetricsInfoManager;

    final private MethodMetricsInfoManager methodMetricsInfoManager;

}
