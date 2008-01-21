package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * void �^��\���N���X�D
 * 
 * @author higo
 * 
 */
public final class NullTypeInfo implements TypeInfo, UnresolvedTypeInfo {

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��Ԃ�
     * 
     * @return ���̃N���X�̒P��I�u�W�F�N�g
     */
    public static NullTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * void �^�̖��O��Ԃ��D
     */
    public String getTypeName() {
        return this.name;
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        return typeInfo instanceof NullTypeInfo;
    }

    /**
     * ���O��������Ă��邩�ǂ�����Ԃ��D
     * 
     * @return ��� true ��Ԃ�
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return �������g��Ԃ�
     */
    public TypeInfo getResolvedType() {
        return this;
    }

    /**
     * ���O�������s��
     * 
     * @param usingClass ���O�������s���G���e�B�e�B������N���X
     * @param usingMethod ���O�������s���G���e�B�e�B�����郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * 
     * @return �����ς݂̌^�i�������g�j
     */
    public TypeInfo resolveType(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * null �^�̌^����\���萔
     */
    public static final String NULL_STRING = new String("null");

    /**
     * �����Ȃ��R���X�g���N�^
     */
    private NullTypeInfo() {
        this.name = NULL_STRING;
    }

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��ۑ����邽�߂̒萔
     */
    private static final NullTypeInfo SINGLETON = new NullTypeInfo();

    /**
     * ���̌^�̖��O��ۑ����邽�߂̕ϐ�
     */
    private final String name;
}
