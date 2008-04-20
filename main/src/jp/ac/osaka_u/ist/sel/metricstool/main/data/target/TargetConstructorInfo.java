package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * ���͑ΏۃR���X�g���N�^��\���N���X
 * 
 * @author higo
 *
 */
public final class TargetConstructorInfo extends ConstructorInfo {

    /**
     * �K�v�ȏ���^���ăI�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�̃Z�b�g
     * @param ownerClass ���̃R���X�g���N�^���`���Ă���N���X
     * @param privateVisible private ���ǂ���
     * @param namespaceVisible �������O��Ԃ�������ǂ���
     * @param inheritanceVisible �q�N���X��������ǂ���
     * @param publicVisible public ���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetConstructorInfo(final Set<ModifierInfo> modifiers, final ClassInfo ownerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, fromLine, fromColumn, toLine, toColumn);
    }
}
