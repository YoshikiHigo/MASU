package jp.ac.osaka_u.ist.sel.metricstool.main.data.file;


/**
 * 
 * @author y-higo
 * 
 * �Ώۃt�@�C���̃f�[�^���i�[����N���X
 * 
 * since 2006.11.12
 */
public class TargetFile {

    /**
     * 
     * @param name �Ώۃt�@�C���̃p�X
     * 
     * �Ώۃt�@�C���̃p�X��p���ď�����
     */
    public TargetFile(final String name) {
        this.name = name;
    }

    /**
     * 
     * @return �Ώۃt�@�C���̃p�X
     * 
     * �Ώۃt�@�C���̃p�X��Ԃ�
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 
     * �Ώۃt�@�C���̃p�X��ۑ����邽�߂̕ϐ�
     */
    private final String name;
}
