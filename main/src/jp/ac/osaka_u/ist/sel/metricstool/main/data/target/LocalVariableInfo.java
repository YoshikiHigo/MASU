package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���[�J���ϐ���\���N���X�D�^��񋟂���̂݁D
 * 
 * @author y-higo
 * 
 */
public final class LocalVariableInfo extends VariableInfo {

    /**
     * ���[�J���ϐ��I�u�W�F�N�g������������D�ϐ����ƕϐ��̌^���K�v�D
     * 
     * @param name ���[�J���ϐ���
     * @param type ���[�J���ϐ��̌^
     */
    public LocalVariableInfo(String name, TypeInfo type) {
        super(name, type);
    }
}
