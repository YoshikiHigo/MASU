package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


/**
 * ������\�����߂̃N���X�D �^��񋟂���̂݁D
 * 
 * @author y-higo
 * 
 */
public abstract class ParameterInfo extends VariableInfo {

    /**
     * �����I�u�W�F�N�g������������D���O�ƌ^���K�v�D
     * 
     * @param modifiers �C���q�� Set
     * @param name ������
     * @param type �����̌^
     */
    public ParameterInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type) {
        super(modifiers, name, type);
    }
}
