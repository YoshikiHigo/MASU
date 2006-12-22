package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �Ώۃ��\�b�h�̈�����\���N���X
 * 
 * @author y-higo
 *
 */
public final class TargetParameterInfo extends ParameterInfo {

    /**
     * �������C�����̌^��^���ăI�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param name ������
     * @param type �����̌^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetParameterInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(modifiers, name, type, fromLine, fromColumn, toLine, toColumn);
    }
}
