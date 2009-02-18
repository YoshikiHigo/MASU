package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �ΏۃN���X�ɒ�`����Ă���t�B�[���h�̏��������N���X�D
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class TargetFieldInfo extends FieldInfo implements Visualizable, Member {

    /**
     * �t�B�[���h���I�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param name ���O
     * @param type �^
     * @param definitionClass ���̃t�B�[���h���`���Ă���N���X
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final ClassInfo definitionClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, name, type, definitionClass, fromLine, fromColumn, toLine, toColumn);

        this.initializer = null;

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.instance = instance;
    }

    /**
     * �t�B�[���h�̏����������Z�b�g����
     * 
     * @param initializer �t�B�[���h�̏�������
     */
    public final void setInitializer(final ExpressionInfo initializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == initializer) {
            throw new IllegalArgumentException();
        }

        this.initializer = initializer;
    }

    /**
     * �t�B�[���h�̏���������Ԃ�
     * 
     * @return�@�t�B�[���h�̏�������
     */
    public final ExpressionInfo getInitializer() {
        return this.initializer;
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    private ExpressionInfo initializer;

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean privateVisible;

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean namespaceVisible;

    /**
     * �q�N���X����Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean inheritanceVisible;

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean publicVisible;

    /**
     * �C���X�^���X�����o�[���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean instance;
}
