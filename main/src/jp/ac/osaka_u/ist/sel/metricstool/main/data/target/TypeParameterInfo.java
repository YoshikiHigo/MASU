package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �^�p�����[�^��\�����ۃN���X
 * 
 * @author higo
 * 
 */
public class TypeParameterInfo implements ReferenceTypeInfo {

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param ownerUnit �^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)
     * @param name �^�p�����[�^��
     * @param index ���Ԗڂ̌^�p�����[�^����\��
     * @param extendsType �p�����Ă���^
     */
    public TypeParameterInfo(final UnitInfo ownerUnit, final String name, final int index,
            final TypeInfo extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerUnit) || (null == name)) {
            throw new NullPointerException();
        }

        if ((!(ownerUnit instanceof TargetClassInfo)) && (!(ownerUnit instanceof CallableUnitInfo))) {
            throw new NullPointerException();
        }

        this.ownerUnit = ownerUnit;
        this.name = name;
        this.index = index;
        this.extendsType = extendsType;
    }

    /**
     * ���̌^�p�����[�^�������ŗ^����ꂽ�^�Ɠ��������ǂ����𔻒肷��
     * 
     * @param o ��r�Ώی^���
     * @return �������ꍇ�� true�C�������Ȃ��ꍇ�� false;
     */
    public boolean equals(final TypeInfo o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof TypeParameterInfo)) {
            return false;
        }

        return this.getName().equals(((TypeParameterInfo) o).getName());
    }

    /**
     * �^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)��Ԃ�
     * 
     * @return  �^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)
     */
    public final UnitInfo getOwnerUnit() {
        return this.ownerUnit;
    }

    /**
     * �^�p�����[�^����Ԃ�
     * 
     * @return �^�p�����[�^��
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �^���i���ۂɂ͌^�p�����[�^���j��Ԃ�
     * 
     * @return �^��
     */
    public final String getTypeName() {
        return this.name;
    }

    /**
     * �^�p�����[�^�̃C���f�b�N�X��Ԃ�
     * 
     * @return�@�^�p�����[�^�̃C���f�b�N�X
     */
    public final int getIndex() {
        return this.index;
    }

    /**
     * ���N���X�^��Ԃ�
     * 
     * @return ���N���X�^
     */
    public final TypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * * ���N���X�������ǂ�����Ԃ�
     * 
     * @return ���N���X�����ꍇ�� true,�����Ȃ��ꍇ�� false
     */
    public final boolean hasExtendsType() {
        return null != this.extendsType;
    }

    /**
     * �^�p�����[�^�����L���Ă��郆�j�b�g��ۑ����邽�߂̕ϐ�
     */
    private final UnitInfo ownerUnit;

    /**
     * �^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final String name;

    /**
     * ���̌^�p�����[�^�����Ԗڂ̂��̂���\���ϐ�
     */
    private final int index;

    /**
     * ���������N���X�^��ۑ����邽�߂̕ϐ�
     */
    private final TypeInfo extendsType;
}
