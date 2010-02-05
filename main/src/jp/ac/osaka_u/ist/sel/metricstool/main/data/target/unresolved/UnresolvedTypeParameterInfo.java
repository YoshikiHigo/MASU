package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������^�p�����[�^��\�����ۃN���X
 * 
 * @author higo
 * 
 */
public class UnresolvedTypeParameterInfo implements UnresolvedReferenceTypeInfo<TypeParameterInfo> {
    // TODO ������Ή�����UnresolvedTypeInfo�ɕύX���ׂ�

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param ownerUnit ���̌^�p�����[�^���`���Ă��郆�j�b�g(�N���X or ���\�b�h)
     * @param name �^�p�����[�^��
     * @param index ���Ԗڂ̌^�p�����[�^�ł��邩��\��
     * @param extendsType ���������N���X�^
     */
    public UnresolvedTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit, final String name,
            final int index,
            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerUnit) || (null == name)) {
            throw new IllegalArgumentException();
        }

        // ownerUnit�����\�b�h���N���X�łȂ��ꍇ�̓G���[
        if ((!(ownerUnit instanceof UnresolvedClassInfo))
                && (!(ownerUnit instanceof UnresolvedCallableUnitInfo))) {
            throw new IllegalArgumentException();
        }

        this.ownerUnit = ownerUnit;
        this.name = name;
        this.index = index;
        this.extendsType = extendsType;
    }

    /**
     * ���ɖ��O��������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ���ɖ��O��������Ă���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     * @throws NotResolvedException
     */
    public final TypeParameterInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
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
     * @return �����ς݂̃G���e�B�e�B
     */
    public TypeParameterInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        //�@�^�p�����[�^�̏��L���j�b�g������
        final UnresolvedUnitInfo<?> unresolvedOwnerUnit = this.getOwnerUnit();
        final TypeParameterizable ownerUnit = (TypeParameterizable) unresolvedOwnerUnit.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final String name = this.getName();
        final int index = this.getIndex();

        if (this.hasExtendsType()) {

            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType = this
                    .getExtendsType();
            final TypeInfo extendsType = unresolvedExtendsType.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new TypeParameterInfo(ownerUnit, name, index, extendsType);

        } else {

            this.resolvedInfo = new TypeParameterInfo(ownerUnit, name, index, null);
        }

        return this.resolvedInfo;
    }

    /**
     * ���̌^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)��Ԃ�
     * 
     * @return ���̌^�p�����[�^��錾���Ă��郆�j�b�g(�N���X or ���\�b�h)
     */
    public final UnresolvedUnitInfo<?> getOwnerUnit() {
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
     * �^�p�����[�^�̃C���f�b�N�X��Ԃ�
     * 
     * @return�@�^�p�����[�^�̃C���f�b�N�X
     */
    public final int getIndex() {
        return this.index;
    }

    /**
     * ���N���X�̖������^����Ԃ�
     * 
     * @return ���N���X�̖������^���
     */
    public final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getExtendsType() {
        return this.extendsType;
    }

    /**
     * ���N���X�������ǂ�����Ԃ�
     * 
     * @return ���N���X�����ꍇ�� true, �����Ȃ��ꍇ�� false
     */
    public final boolean hasExtendsType() {
        return null != this.extendsType;
    }

    /**
     * �^�p�����[�^��錾���Ă��郆�j�b�g��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedUnitInfo<?> ownerUnit;

    /**
     * �^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final String name;

    /**
     * ���N���X��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> extendsType;

    /**
     * �^�p�����[�^�̃C���f�b�N�X��ۑ����邽�߂̕ϐ�
     */
    private final int index;

    /**
     * ���O�������ꂽ����ۑ����邽�߂̕ϐ�
     */
    protected TypeParameterInfo resolvedInfo;
}
