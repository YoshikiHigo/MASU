package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * ������\�����߂̃N���X�D �^��񋟂���̂݁D
 * 
 * @author higo
 * 
 */
public abstract class ParameterInfo extends VariableInfo {

    /**
     * �����I�u�W�F�N�g������������D���O�ƌ^���K�v�D
     * 
     * @param modifiers �C���q�� Set
     * @param name ������
     * @param type �����̌^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ParameterInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, name, type, fromLine, fromColumn, toLine, toColumn);
    }
}
