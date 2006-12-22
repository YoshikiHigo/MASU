package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * void �^��\���N���X�D
 * 
 * @author y-higo
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
