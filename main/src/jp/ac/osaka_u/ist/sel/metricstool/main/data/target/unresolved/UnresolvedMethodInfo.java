package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ��x�ڂ�AST�p�[�X�Ŏ擾�������\�b�h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodInfo extends UnresolvedCallableUnitInfo<TargetMethodInfo>
        implements MemberSetting {

    /**
     * ���������\�b�h��`���I�u�W�F�N�g��������
     * @param ownerClass ���̃��\�b�h��錾���Ă���N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedMethodInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        this.methodName = null;
        this.returnType = null;
        this.instance = true;
        this.resolvedInfo = null;
    }

    /**
     * ���������\�b�h�����������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass ���������\�b�h���̒�`������N���X
     * @param usingMethod ���������\�b�h���̒�`�����郁�\�b�h�i���̃��\�b�h���Ă΂��ꍇ�͒ʏ� null ���Z�b�g����Ă���͂��j
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݃��\�b�h���
     */
    @Override
    public TargetMethodInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �C���q�C���O�C�Ԃ�l�C�s���C�R���X�g���N�^���ǂ����C�����C�C���X�^���X�����o�[���ǂ������擾
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final String methodName = this.getMethodName();

        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // MethodInfo �I�u�W�F�N�g�𐶐�����D
        this.resolvedInfo = new TargetMethodInfo(methodModifiers, methodName, usingClass,
                privateVisible, namespaceVisible, inheritanceVisible, publicVisible, instance,
                fromLine, fromColumn, toLine, toColumn);

        // �^�p�����[�^���������C�����ς݃��\�b�h���ɒǉ�����
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(usingClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        // �Ԃ�l���Z�b�g����
        final UnresolvedTypeInfo<?> unresolvedMethodReturnType = this.getReturnType();
        TypeInfo methodReturnType = unresolvedMethodReturnType.resolve(usingClass, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert methodReturnType != null : "resolveTypeInfo returned null!";
        if (methodReturnType instanceof UnknownTypeInfo) {
            if (unresolvedMethodReturnType instanceof UnresolvedClassReferenceInfo) {

                final ExternalClassInfo classInfo = UnresolvedClassReferenceInfo
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedMethodReturnType);
                methodReturnType = new ClassTypeInfo(classInfo);
                for (final UnresolvedTypeInfo<?> unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedMethodReturnType)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    ((ClassTypeInfo) methodReturnType).addTypeArgument(typeArgument);
                }
                classInfoManager.add(classInfo);

            } else if (unresolvedMethodReturnType instanceof UnresolvedArrayTypeInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final UnresolvedTypeInfo<?> unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolve(usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager);
                methodReturnType = ArrayTypeInfo.getType(elementType, dimension);

            } else {
                assert false : "Can't resolve method return type : "
                        + unresolvedMethodReturnType.toString();
            }
        }
        this.resolvedInfo.setReturnType(methodReturnType);

        // ������ǉ�����
        for (final UnresolvedParameterInfo unresolvedParameterInfo : this.getParameters()) {

            final TargetParameterInfo parameterInfo = unresolvedParameterInfo.resolve(usingClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addParameter(parameterInfo);
        }

        // �������u���b�N�������������C�����ς݃I�u�W�F�N�g�ɒǉ�
        this.resolveInnerBlock(usingClass, this.resolvedInfo, classInfoManager, fieldInfoManager,
                methodInfoManager);

        return this.resolvedInfo;
    }

    /**
     * ���\�b�h����Ԃ�
     * 
     * @return ���\�b�h��
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * ���\�b�h�����Z�b�g����
     * 
     * @param methodName ���\�b�h��
     */
    public void setMethodName(final String methodName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodName) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
    }

    /**
     * ���\�b�h�̕Ԃ�l�̌^��Ԃ�
     * 
     * @return ���\�b�h�̕Ԃ�l�̌^
     */
    public UnresolvedTypeInfo<?> getReturnType() {
        return this.returnType;
    }

    /**
     * ���\�b�h�̕Ԃ�l���Z�b�g����
     * 
     * @param returnType ���\�b�h�̕Ԃ�l
     */
    public void setReturnType(final UnresolvedTypeInfo<?> returnType) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
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
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    private String methodName;

    /**
     * ���\�b�h�̕Ԃ�l��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedTypeInfo<?> returnType;

    /**
     * �C���X�^���X�����o�[���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private boolean instance;

}
