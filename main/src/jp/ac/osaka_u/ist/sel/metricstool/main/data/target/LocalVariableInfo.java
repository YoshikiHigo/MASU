package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * ���[�J���ϐ���\���N���X�D�^��񋟂���̂݁D
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class LocalVariableInfo extends VariableInfo<LocalSpaceInfo> {

    /**
     * ���[�J���ϐ��I�u�W�F�N�g������������D�ϐ����ƕϐ��̌^���K�v�D
     * 
     * @param modifiers �C���q�� Set
     * @param name ���[�J���ϐ���
     * @param type ���[�J���ϐ��̌^
     * @param definitionSpace ���̕ϐ����`���Ă���u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public LocalVariableInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final LocalSpaceInfo definitionSpace, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, name, type, definitionSpace, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * �^����ꂽ�ϐ���Set�Ɋ܂܂�Ă��郍�[�J���ϐ���Set�Ƃ��ĕԂ�
     * @param variables �ϐ���Set
     * @return �^����ꂽ�ϐ���Set�Ɋ܂܂�郍�[�J���ϐ���Set
     */
    public static Set<LocalVariableInfo> getLocalVariables(Collection<VariableInfo<?>> variables) {
        final Set<LocalVariableInfo> localVariables = new HashSet<LocalVariableInfo>();
        for (final VariableInfo<?> variable : variables) {
            if (variable instanceof LocalVariableInfo) {
                localVariables.add((LocalVariableInfo) variable);
            }
        }
        return Collections.unmodifiableSet(localVariables);
    }
}
