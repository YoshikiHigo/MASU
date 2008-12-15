package jp.ac.osaka_u.ist.sel.metricstool.main.data;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;


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

        if (null == SINGLETON) {
            SINGLETON = new DataManager();
        }

        return SINGLETON;

    }

    /**
     * �f�[�^�}�l�[�W���[�ɓo�^����Ă�������N���A����
     */
    public static void clear() {
        SINGLETON = null;
    }

    /**
     * TargetFileManager�@��Ԃ�
     * 
     * @return TargetFileManager
     */
    public TargetFileManager getTargetFileManager() {
        return this.targetFileManager;
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

    /**
     * ClassInfoManager�@��Ԃ�
     * 
     * @return ClassInfoManager
     */
    public ClassInfoManager getClassInfoManager() {
        return this.classInfoManager;
    }

    /**
     * FieldInfoManager�@��Ԃ�
     * 
     * @return FieldInfoManager
     */
    public FieldInfoManager getFieldInfoManager() {
        return this.fieldInfoManager;
    }

    /**
     * FileInfoManager�@��Ԃ�
     * 
     * @return FileInfoManager
     */
    public FileInfoManager getFileInfoManager() {
        return this.fileInfoManager;
    }

    /**
     * MethodInfoManager�@��Ԃ�
     * 
     * @return MethodInfoManager
     */
    public MethodInfoManager getMethodInfoManager() {
        return this.methodInfoManager;
    }

    private DataManager() {
        this.targetFileManager = new TargetFileManager();

        this.classMetricsInfoManager = new ClassMetricsInfoManager();
        this.fieldMetricsInfoManager = new FieldMetricsInfoManager();
        this.fileMetricsInfoManager = new FileMetricsInfoManager();
        this.methodMetricsInfoManager = new MethodMetricsInfoManager();

        this.classInfoManager = new ClassInfoManager();
        this.fieldInfoManager = new FieldInfoManager();
        this.fileInfoManager = new FileInfoManager();
        this.methodInfoManager = new MethodInfoManager();
    }

    private static DataManager SINGLETON;

    final private TargetFileManager targetFileManager;

    final private ClassMetricsInfoManager classMetricsInfoManager;

    final private FieldMetricsInfoManager fieldMetricsInfoManager;

    final private FileMetricsInfoManager fileMetricsInfoManager;

    final private MethodMetricsInfoManager methodMetricsInfoManager;

    final private ClassInfoManager classInfoManager;

    final private FieldInfoManager fieldInfoManager;

    final private FileInfoManager fileInfoManager;

    final private MethodInfoManager methodInfoManager;
}
