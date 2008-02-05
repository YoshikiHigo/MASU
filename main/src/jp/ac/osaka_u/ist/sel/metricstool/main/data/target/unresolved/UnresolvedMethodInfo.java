package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ��x�ڂ�AST�p�[�X�Ŏ擾�������\�b�h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodInfo extends UnresolvedLocalSpaceInfo<TargetMethodInfo>
        implements VisualizableSetting, MemberSetting {

    /**
     * ���������\�b�h��`���I�u�W�F�N�g��������
     */
    public UnresolvedMethodInfo() {

        this.methodName = null;
        this.returnType = null;
        this.ownerClass = null;
        this.constructor = false;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameterInfos = new LinkedList<UnresolvedParameterInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;

        this.instance = true;

        this.resolvedInfo = null;
    }

    /**
     * ���������\�b�h��`���I�u�W�F�N�g��������
     * 
     * @param methodName ���\�b�h��
     * @param returnType �Ԃ�l�̌^
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     * @param constructor �R���X�g���N�^���ǂ���
     */
    public UnresolvedMethodInfo(final String methodName, final UnresolvedTypeInfo returnType,
            final UnresolvedClassInfo ownerClass, final boolean constructor) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodName) || (null == returnType) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.returnType = returnType;
        this.ownerClass = ownerClass;
        this.constructor = constructor;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameterInfos = new LinkedList<UnresolvedParameterInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;

        this.instance = true;

        this.resolvedInfo = null;
    }

    /**
     * ���̖��������\�b�h��񂪉�������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �����ς݂̏ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ς݃��\�b�h����Ԃ�
     * 
     * @return �����ς݃��\�b�h���
     * @throws �܂���������Ă��Ȃ��ꍇ�ɃX���[�����
     */
    @Override
    public TargetMethodInfo getResolvedUnit() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
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
    public TargetMethodInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // �C���q�C���O�C�Ԃ�l�C�s���C�R���X�g���N�^���ǂ����C�����C�C���X�^���X�����o�[���ǂ������擾
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final String methodName = this.getMethodName();

        final boolean constructor = this.isConstructor();
        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int methodFromLine = this.getFromLine();
        final int methodFromColumn = this.getFromColumn();
        final int methodToLine = this.getToLine();
        final int methodToColumn = this.getToColumn();

        // MethodInfo �I�u�W�F�N�g�𐶐�����D
        this.resolvedInfo = new TargetMethodInfo(methodModifiers, methodName, usingClass,
                constructor, privateVisible, namespaceVisible, inheritanceVisible, publicVisible,
                instance, methodFromLine, methodFromColumn, methodToLine, methodToColumn);

        // �^�p�����[�^���������C�����ς݃��\�b�h���ɒǉ�����
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = (TypeParameterInfo) unresolvedTypeParameter
                    .resolveType(usingClass, this.resolvedInfo, classInfoManager, null, null);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        // �Ԃ�l���Z�b�g����
        final UnresolvedTypeInfo unresolvedMethodReturnType = this.getReturnType();
        TypeInfo methodReturnType = unresolvedMethodReturnType.resolveType(usingClass, null,
                classInfoManager, null, null);
        assert methodReturnType != null : "resolveTypeInfo returned null!";
        if (methodReturnType instanceof UnknownTypeInfo) {
            if (unresolvedMethodReturnType instanceof UnresolvedClassReferenceInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final ExternalClassInfo classInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedMethodReturnType);
                methodReturnType = new ClassTypeInfo(classInfo);
                classInfoManager.add(classInfo);

            } else if (unresolvedMethodReturnType instanceof UnresolvedArrayTypeInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolveType(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                methodReturnType = ArrayTypeInfo.getType(elementType, dimension);

            } else {
                assert false : "Can't resolve method return type : "
                        + unresolvedMethodReturnType.toString();
            }
        }
        this.resolvedInfo.setReturnType(methodReturnType);

        // ������ǉ�����
        for (final UnresolvedParameterInfo unresolvedParameterInfo : this.getParameterInfos()) {

            final TargetParameterInfo parameterInfo = unresolvedParameterInfo.resolveUnit(
                    usingClass, this.resolvedInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            this.resolvedInfo.addParameter(parameterInfo);
        }

        // ���\�b�h���Œ�`����Ă���e���������[�J���ϐ��ɑ΂���
        for (final UnresolvedLocalVariableInfo unresolvedLocalVariable : this.getLocalVariables()) {

            final LocalVariableInfo localVariable = unresolvedLocalVariable.resolveUnit(usingClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(localVariable);
        }

        // ���\�b�h�������\�b�h���}�l�[�W���ɒǉ�
        usingClass.addDefinedMethod(this.resolvedInfo);
        methodInfoManager.add(this.resolvedInfo);
        return this.resolvedInfo;
    }

    /**
     * �R���X�g���N�^���ǂ�����Ԃ�
     * 
     * @return �R���X�g���N�^�̏ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isConstructor() {
        return this.constructor;
    }

    /**
     * �R���X�g���N�^���ǂ������Z�b�g����
     * 
     * @param constructor �R���X�g���N�^���ǂ���
     */
    public void setConstructor(final boolean constructor) {
        this.constructor = constructor;
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public void addModifier(final ModifierInfo modifier) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * �������^�p�����[�^�� List ��Ԃ�
     * 
     * @return �������^�p�����[�^�� List
     */
    public List<UnresolvedTypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * �������^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ����関�����^�p�����[�^
     */
    public void addTypeParameter(final UnresolvedTypeParameterInfo typeParameter) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
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
    public UnresolvedTypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * ���\�b�h�̕Ԃ�l���Z�b�g����
     * 
     * @param returnType ���\�b�h�̕Ԃ�l
     */
    public void setReturnType(final UnresolvedTypeInfo returnType) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * ���̃��\�b�h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃��\�b�h���`���Ă���N���X
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���\�b�h���`���Ă���N���X���Z�b�g����
     * 
     * @param ownerClass ���\�b�h���`���Ă���N���X
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * ���\�b�h�Ɉ�����ǉ�����
     * 
     * @param parameterInfo �ǉ��������
     */
    public void adParameter(final UnresolvedParameterInfo parameterInfo) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameterInfo) {
            throw new NullPointerException();
        }

        this.parameterInfos.add(parameterInfo);
    }

    /**
     * ���\�b�h�̈����̃��X�g��Ԃ�
     * 
     * @return ���\�b�h�̈����̃��X�g
     */
    public List<UnresolvedParameterInfo> getParameterInfos() {
        return Collections.unmodifiableList(this.parameterInfos);
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

    /**
     * �C���X�^���X�����o�[���ǂ������Z�b�g����
     * 
     * @param instance �C���X�^���X�����o�[�̏ꍇ�� true�C �X�^�e�B�b�N�����o�[�̏ꍇ�� false
     */
    public void setInstanceMember(final boolean instance) {
        this.instance = instance;
    }

    /**
     * �C���q��ۑ�����
     */
    private Set<ModifierInfo> modifiers;

    /**
     * �������^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;

    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    private String methodName;

    /**
     * ���\�b�h������ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedParameterInfo> parameterInfos;

    /**
     * ���\�b�h�̕Ԃ�l��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedTypeInfo returnType;

    /**
     * ���̃��\�b�h���`���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * �R���X�g���N�^���ǂ�����\���ϐ�
     */
    private boolean constructor;

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
     * ���O�������ꂽ�����i�[���邽�߂̕ϐ�
     */
    private TargetMethodInfo resolvedInfo;
}
