package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h�C�����C���[�J���ϐ��̋��ʂ̐e�N���X�D �ȉ��̏������D
 * <ul>
 * <li>�ϐ���</li>
 * <li>�^</li>
 * <li>�C���q</li>
 * <li>�ʒu���</li>
 * 
 * @author higo
 * 
 */
public abstract class VariableInfo extends UnitInfo implements Comparable<VariableInfo> {

    /**
     * �ϐ��̏������`���郁�\�b�h�D�ϐ����iString�j�ɏ]���D
     * 
     * @return �ϐ��̏����֌W
     */
    public final int compareTo(final VariableInfo variable) {

        if (null == variable) {
            throw new NullPointerException();
        }

        String variableName = this.getName();
        String correspondVariableName = variable.getName();
        return variableName.compareTo(correspondVariableName);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �ϐ�����Ԃ�
     * 
     * @return �ϐ���
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �ϐ��̌^��Ԃ�
     * 
     * @return �ϐ��̌^
     */
    public final TypeInfo getType() {
        return this.type;
    }

    /**
     * �ϐ��I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q�� Set
     * @param name �ϐ���
     * @param type �ϐ��̌^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    VariableInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == modifiers) || (null == name) || (null == type)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * �ϐ�����\���ϐ�
     */
    private final String name;

    /**
     * �ϐ��̌^��\���ϐ�
     */
    private final TypeInfo type;
}
