package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h�C�����C���[�J���ϐ��̋��ʂ̐e�N���X�D �ȉ��̏������D
 * <ul>
 * <li>�ϐ���</li>
 * <li>�^</li>
 * <li>�C���q</li>
 * 
 * @author y-higo
 * 
 */
public abstract class VariableInfo implements Comparable<VariableInfo> {

    /**
     * �ϐ��̏������`���郁�\�b�h�D�ϐ����iString�j�ɏ]���D
     * 
     * @return �ϐ��̏����֌W
     */
    public int compareTo(final VariableInfo variable) {
        
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
    public final TypeInfo getType() {
        return this.type;
    }

    /**
     * �ϐ��I�u�W�F�N�g������������D�v���O�C������A�N�Z�X�ł��Ȃ��悤�� protected�D
     * 
     * @param name �ϐ���
     * @param type �ϐ��̌^
     */
    protected VariableInfo(final String name, final TypeInfo type) {
        
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
    private final TypeInfo type;

}
