package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�Ŏ擾�����t�B�[���h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldInfo extends UnresolvedVariableInfo<TargetFieldInfo, UnresolvedClassInfo> implements
        VisualizableSetting, MemberSetting {

    /**
     * Unresolved�t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     * @param definitionClass �t�B�[���h���`���Ă���N���X
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo type,
            final UnresolvedClassInfo definitionClass) {

        super(name, type, definitionClass);

        if (null == definitionClass) {
            throw new NullPointerException();
        }

        this.ownerClass = definitionClass;

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;

        this.instance = true;
    }

    @Override
    public TargetFieldInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == fieldInfoManager)) {
            throw new NullPointerException();
        }

        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // �C���q�C���O�C�^�C�����C�C���X�^���X�����o�[���ǂ������擾
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String fieldName = this.getName();
        final UnresolvedTypeInfo unresolvedFieldType = this.getType();
        TypeInfo fieldType = unresolvedFieldType.resolveType(usingClass, null, classInfoManager,
                fieldInfoManager, null);
        assert fieldType != null : "resolveTypeInfo returned null!";
        if (fieldType instanceof UnknownTypeInfo) {
            if (unresolvedFieldType instanceof UnresolvedClassReferenceInfo) {

                final ExternalClassInfo classInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedFieldType);
                fieldType = new ClassTypeInfo(classInfo);
                for (final UnresolvedTypeInfo unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedFieldType)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolveType(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    ((ClassTypeInfo) fieldType).addTypeArgument(typeArgument);
                }
                classInfoManager.add(classInfo);

            } else if (unresolvedFieldType instanceof UnresolvedArrayTypeInfo) {

                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedFieldType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedFieldType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolveType(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                fieldType = ArrayTypeInfo.getType(elementType, dimension);

            } else {
                assert false : "Can't resolve field type : " + unresolvedFieldType.toString();
            }
        }
        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �t�B�[���h�I�u�W�F�N�g�𐶐�
        this.resolvedInfo = new TargetFieldInfo(modifiers, fieldName, fieldType, usingClass,
                privateVisible, namespaceVisible, inheritanceVisible, publicVisible, instance,
                fromLine, fromColumn, toLine, toColumn);
        fieldInfoManager.add(this.resolvedInfo);
        usingClass.addDefinedField(this.resolvedInfo);
        return this.resolvedInfo;
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
}
