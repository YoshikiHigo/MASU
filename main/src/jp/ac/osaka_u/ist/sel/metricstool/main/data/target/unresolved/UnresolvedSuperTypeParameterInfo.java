package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class UnresolvedSuperTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * �^�p�����[�^���C�������h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param ownerUnit ���̌^�p�����[�^�̏��L���j�b�g(�N���X or�@���\�b�h)
     * @param name �^�p�����[�^��
     * @param extendsType ���������N���X�^
     * @param superType �������h���N���X�^
     */
    public UnresolvedSuperTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit,
            final String name, final int index, final UnresolvedTypeInfo extendsType,
            final UnresolvedTypeInfo superType) {

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
        final UnresolvedTypeInfo unresolvedSuperType = this.getSuperType();
        final TypeInfo superType = unresolvedSuperType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        //�@�^�p�����[�^�̏��L���j�b�g������
        final UnresolvedUnitInfo<?> unresolvedOwnerUnit = this.getOwnerUnit();
        final UnitInfo ownerUnit = unresolvedOwnerUnit.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // extends �� ������ꍇ
        if (this.hasExtendsType()) {

            final UnresolvedTypeInfo unresolvedExtendsType = this.getExtendsType();
            final TypeInfo extendsType = unresolvedExtendsType.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, extendsType,
                    superType);

        } else {

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, null, superType);
        }

        return this.resolvedInfo;
    }

    /**
     * �������h���N���X�^��Ԃ�
     * 
     * @return �������h���N���X�^
     */
    public UnresolvedTypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * �������h���N���X�^��ۑ�����
     */
    private final UnresolvedTypeInfo superType;
}
