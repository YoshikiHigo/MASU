package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


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
     * @param modifiers �C���q�� Set
     * @param name ���[�J���ϐ���
     * @param type ���[�J���ϐ��̌^
     */
    public LocalVariableInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type) {
        super(modifiers, name, type);
    }
}
