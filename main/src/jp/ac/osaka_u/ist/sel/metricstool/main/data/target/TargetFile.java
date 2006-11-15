package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


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
     * @param o ��r�Ώۃt�@�C��
     * @return ���̑Ώۃt�@�C���Ɣ�r�Ώۃt�@�C���̃t�@�C���p�X���������ꍇ�� true�C�����łȂ���� false
     * 
     * ���̑Ώۃt�@�C���̃t�@�C���p�X�ƁC�����ŗ^����ꂽ�Ώۃt�@�C���̃p�X�������������`�F�b�N����D�������ꍇ�� true ��Ԃ��C�����łȂ��ꍇ�� false ��Ԃ��D
     * 
     */
    public boolean equals(Object o) {
        String thisName = this.getName();
        String correspondName = ((TargetFile) o).getName();
        return thisName.equals(correspondName);
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
     * @return �Ώۃt�@�C���̃n�b�V���R�[�h
     * 
     * �Ώۃt�@�C���̃n�b�V���R�[�h��Ԃ�
     * 
     */
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * 
     * �Ώۃt�@�C���̃p�X��ۑ����邽�߂̕ϐ�
     */
    private final String name;
}
