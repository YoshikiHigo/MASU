package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * ������\�����߂̃N���X�D �^��񋟂���̂݁D
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedParameterInfo extends UnresolvedVariableInfo {

    /**
     * �����I�u�W�F�N�g������������D���O�ƌ^���K�v�D
     * 
     * @param name ������
     * @param type �����̌^
     */
    public UnresolvedParameterInfo(final String name, final UnresolvedTypeInfo type) {
        super(name, type);
    }
}
