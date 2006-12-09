package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved�ȕϐ��̋��ʂȐe�N���X.
 * <ul>
 * <li>�ϐ���</li>
 * <li>�^</li>
 * <li>�C���q</li>
 * 
 * @author y-higo
 * 
 */
public class UnresolvedVariableInfo implements Comparable<UnresolvedVariableInfo> {

    /**
     * �ϐ��̏������`���郁�\�b�h�D�ϐ����iString�j�ɏ]���D
     * 
     * @return �ϐ��̏����֌W
     */
    public int compareTo(final UnresolvedVariableInfo variable) {

        if (null == variable) {
            throw new NullPointerException();
        }

        String variableName = this.getName();
        String correspondVariableName = variable.getName();
        return variableName.compareTo(correspondVariableName);
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
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * �ϐ��I�u�W�F�N�g������������D�v���O�C������A�N�Z�X�ł��Ȃ��悤�� protected�D
     * 
     * @param name �ϐ���
     * @param type �ϐ��̌^
     */
    protected UnresolvedVariableInfo(final String name, final UnresolvedTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == type)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
    }

    /**
     * �ϐ�����\���ϐ�
     */
    private final String name;

    /**
     * �ϐ��̌^��\���ϐ�
     */
    private final UnresolvedTypeInfo type;

}
