package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * ���[�J���ϐ���\�����߂̃N���X�D 
 * �ȉ��̏������D
 * <ul>
 * <li>�ϐ���</li>
 * <li>�������^��</li>
 * </ul>
 * @author y-higo
 * 
 */
public final class UnresolvedLocalVariableInfo extends UnresolvedVariableInfo {

    /**
     * ���[�J���ϐ��u�W�F�N�g������������D
     * 
     * @param name �ϐ���
     * @param type �������^��
     */
    public UnresolvedLocalVariableInfo(final String name, final UnresolvedTypeInfo type) {
        super(name, type);
    }
}

