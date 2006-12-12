package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �ΏۃN���X�ɒ�`����Ă���t�B�[���h�̏��������N���X�D
 * 
 * @author y-higo
 */
public final class TargetFieldInfo extends FieldInfo {

    /**
     * �t�B�[���h���I�u�W�F�N�g��������
     * 
     * @param modidiers �C���q���� Set
     * @param name ���O
     * @param type �^
     * @param ownerClass ���̃t�B�[���h���`���Ă���N���X
     */
    public TargetFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final ClassInfo ownerClass) {

        super(name, type, ownerClass);

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;
}
