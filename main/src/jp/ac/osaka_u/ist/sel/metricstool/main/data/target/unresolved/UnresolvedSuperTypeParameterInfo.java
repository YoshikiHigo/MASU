package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������^�p�����[�^�i�\�L���uA super B�v�j��\���N���X
 * 
 * @author higo
 *
 */
public final class UnresolvedSuperTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * �^�p�����[�^���C�������h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param ownerUnit ���̌^�p�����[�^�̏��L���j�b�g(�N���X or�@���\�b�h)
     * @param name �^�p�����[�^��
     * @param index ���Ԗڂ̌^�p�����[�^�ł��邩��\��
     * @param extendsType ���������N���X�^
     * @param superType �������h���N���X�^
     */
    public UnresolvedSuperTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit,
            final String name, final int index,
            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> extendsType,
            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> superType) {

        super(ownerUnit, name, index, extendsType);

        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
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
     * @return �����ς݂̌^�p�����[�^
     */
    @Override
    public TypeParameterInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        final int index = this.getIndex();
        final String name = this.getName();
        /*
        final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedSuperType = this
                .getSuperType();
        final TypeInfo superType = unresolvedSuperType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        //�@�^�p�����[�^�̏��L���j�b�g������
        final UnresolvedUnitInfo<?> unresolvedOwnerUnit = this.getOwnerUnit();
        final TypeParameterizable ownerUnit = (TypeParameterizable) unresolvedOwnerUnit.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // extends �� ������ꍇ
        if (this.hasExtendsType()) {

            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType = this
                    .getExtendsType();
            final TypeInfo extendsType = unresolvedExtendsType.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, extendsType,
                    superType);

        } else {

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, null, superType);
        }*/

        return super.resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);
    }

    /**
     * �������h���N���X�^��Ԃ�
     * 
     * @return �������h���N���X�^
     */
    public UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getSuperType() {
        return this.superType;
    }

    /**
     * �������h���N���X�^��ۑ�����
     */
    private final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> superType;
}
