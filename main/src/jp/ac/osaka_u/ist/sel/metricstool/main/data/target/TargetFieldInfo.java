package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �ΏۃN���X�ɒ�`����Ă���t�B�[���h�̏��������N���X�D
 * 
 * @author y-higo
 */
public final class TargetFieldInfo extends FieldInfo {

    /**
     * �t�B�[���h���I�u�W�F�N�g��������
     * 
     * @param modifier �C���q
     * @param name ���O
     * @param type �^
     * @param ownerClass ���̃t�B�[���h���`���Ă���N���X
     */
    public TargetFieldInfo(final ModifierInfo modifier, final String name, final TypeInfo type,
            final ClassInfo ownerClass) {

        super(name, type, ownerClass);

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifier = modifier;
    }

    /**
     * �C���q��Ԃ�
     * 
     * @return �C���q
     */
    public ModifierInfo getModifier() {
        return this.modifier;
    }

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final ModifierInfo modifier;
}
