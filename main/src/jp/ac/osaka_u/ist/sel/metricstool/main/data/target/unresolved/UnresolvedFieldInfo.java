package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;




/**
 * AST�p�[�X�Ŏ擾�����t�B�[���h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldInfo extends UnresolvedVariableInfo implements
        VisualizableSetting, MemberSetting, PositionSetting {

    /**
     * Unresolved�t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     * @param ownerClass �t�B�[���h���`���Ă���N���X
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo type,
            final UnresolvedClassInfo ownerClass) {

        super(name, type);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;

        this.instance = true;
        
        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;
    }

    /**
     * ���̃t�B�[���h���`���Ă��関�����N���X����Ԃ�
     * 
     * @return ���̃t�B�[���h���`���Ă��関�����N���X���
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃t�B�[���h���`���Ă��関�����N���X�����Z�b�g����
     * 
     * @param ownerClass ���̃t�B�[���h���`���Ă��関�����N���X���
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param inheritanceVisible �q�N���X����Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public void setInheritanceVisible(final boolean inheritanceVisible) {
        this.inheritanceVisible = inheritanceVisible;
    }

    /**
     * �������O��ԓ�����Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public void setNamespaceVisible(final boolean namespaceVisible) {
        this.namespaceVisible = namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param privateVisible �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public void setPrivateVibible(final boolean privateVisible) {
        this.privateVisible = privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param publicVisible �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public void setPublicVisible(final boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isInheritanceVisible() {
        return this.privateVisible;
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
        return this.inheritanceVisible;
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

    /**
     * �C���X�^���X�����o�[���ǂ������Z�b�g����
     * 
     * @param instance �C���X�^���X�����o�[�̏ꍇ�� true�C �X�^�e�B�b�N�����o�[�̏ꍇ�� false
     */
    public void setInstanceMember(final boolean instance) {
        this.instance = instance;
    }

    /**
     * �J�n�s���Z�b�g����
     * 
     * @param fromLine �J�n�s
     */
    public void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * �J�n����Z�b�g����
     * 
     * @param fromColumn �J�n��
     */
    public void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * �I���s���Z�b�g����
     * 
     * @param toLine �I���s
     */
    public void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * �I������Z�b�g����
     * 
     * @param toColumn �I����
     */
    public void setToColumn(final int toColumn) {

        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public int getToColumn() {
        return this.toColumn;
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean privateVisible;

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean namespaceVisible;

    /**
     * �q�N���X����Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean inheritanceVisible;

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean publicVisible;

    /**
     * �C���X�^���X�����o�[���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private boolean instance;

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int toColumn;
}
