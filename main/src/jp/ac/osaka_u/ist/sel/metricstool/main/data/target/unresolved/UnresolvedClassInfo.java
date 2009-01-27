package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetAnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�Ŏ擾�����N���X�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D �ȉ��̏�������
 * 
 * <ul>
 * <li>�C���q</li>
 * <li>���������O���</li>
 * <li>�^�p�����[�^���ꗗ</li>
 * <li>�N���X��</li>
 * <li>�s��</li>
 * <li>�������e�N���X���ꗗ</li>
 * <li>�������q�N���X���ꗗ</li>
 * <li>�������C���i�[�N���X�ꗗ</li>
 * <li>��������`���\�b�h�ꗗ</li>
 * <li>��������`�t�B�[���h�ꗗ</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public final class UnresolvedClassInfo extends UnresolvedUnitInfo<TargetClassInfo> implements
        VisualizableSetting, MemberSetting, ModifierSetting {

    /**
     * ���̃N���X���L�q����Ă���t�@�C������^���ď�����
     * 
     * @param fileInfo ���̃N���X���L�q���ꂢ�Ă�t�@�C�����
     * @param outerUnit ���̃N���X�̊O���̃��j�b�g
     */
    public UnresolvedClassInfo(final FileInfo fileInfo,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == fileInfo) {
            throw new IllegalArgumentException("fileInfo is null");
        }

        this.outerUnit = outerUnit;

        this.fileInfo = fileInfo;
        this.namespace = null;
        this.className = null;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.superClasses = new LinkedHashSet<UnresolvedClassTypeInfo>();
        this.innerClasses = new HashSet<UnresolvedClassInfo>();
        this.definedMethods = new HashSet<UnresolvedMethodInfo>();
        this.definedConstructors = new HashSet<UnresolvedConstructorInfo>();
        this.definedFields = new HashSet<UnresolvedFieldInfo>();
        this.staticInitializer = new UnresolvedStaticInitializerInfo(this);
        this.instanceInitializer = new UnresolvedInstanceInitializerInfo(this);
        this.importStatements = new LinkedList<UnresolvedImportStatementInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
        this.isInterface = false;

        this.instance = true;

        this.anonymous = false;

        this.resolvedInfo = null;
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
     * �������^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ����関�����^�p�����[�^��
     */
    public void addTypeParameter(final UnresolvedTypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * ���̃N���X�ƑΏۃN���X�����������ǂ����𔻒肷��
     * 
     * @param o ��r�ΏۃN���X
     */
    @Override
    public boolean equals(final Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof UnresolvedClassInfo)) {
            return false;
        }

        final String[] fullQualifiedName = this.getFullQualifiedName();
        final String[] correspondFullQualifiedName = ((UnresolvedClassInfo) o)
                .getFullQualifiedName();

        if (fullQualifiedName.length != correspondFullQualifiedName.length) {
            return false;
        }

        for (int i = 0; i < fullQualifiedName.length; i++) {
            if (!fullQualifiedName[i].equals(correspondFullQualifiedName[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * ���̃N���X�̃n�b�V���R�[�h��Ԃ�
     * 
     * @return ���̃N���X�̃n�b�V���R�[�h
     */
    @Override
    public int hashCode() {

        final StringBuffer buffer = new StringBuffer();
        final String[] fullQualifiedName = this.getFullQualifiedName();
        for (int i = 0; i < fullQualifiedName.length; i++) {
            buffer.append(fullQualifiedName[i]);
        }

        return buffer.toString().hashCode();
    }

    /**
     * ���̃N���X���L�q����Ă���t�@�C������Ԃ�
     * 
     * @return ���̃N���X���L�q����Ă���t�@�C�����
     */
    public FileInfo getFileInfo() {
        return this.fileInfo;
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String[] getNamespace() {
        return this.namespace;
    }

    /**
     * �N���X�����擾����
     * 
     * @return �N���X��
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * ���̃N���X�̊��S�C������Ԃ�
     * 
     * @return ���̃N���X�̊��S�C����
     */
    public String[] getFullQualifiedName() {

        final String[] namespace = this.getNamespace();
        final String[] fullQualifiedName = new String[namespace.length + 1];

        for (int i = 0; i < namespace.length; i++) {
            fullQualifiedName[i] = namespace[i];
        }
        fullQualifiedName[fullQualifiedName.length - 1] = this.getClassName();

        return fullQualifiedName;
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
     * �������^�p�����[�^�� List ��Ԃ�
     * 
     * @return �������^�p�����[�^�� List
     */
    public List<UnresolvedTypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * ���O��Ԗ���ۑ�����.���O��Ԗ����Ȃ��ꍇ�͒���0�̔z���^���邱�ƁD
     * 
     * @param namespace ���O��Ԗ�
     */
    public void setNamespace(final String[] namespace) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
    }

    /**
     * �N���X����ۑ�����
     * 
     * @param className
     */
    public void setClassName(final String className) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == className) {
            throw new NullPointerException();
        }

        this.className = className;
    }

    /**
     * �e�N���X��ǉ�����
     * 
     * @param superClass �e�N���X��
     */
    public void addSuperClass(final UnresolvedClassTypeInfo superClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superClass) {
            throw new NullPointerException();
        }

        this.superClasses.add(superClass);
    }

    /**
     * �C���i�[�N���X��ǉ�����
     * 
     * @param innerClass �C���i�[�N���X
     */
    public void addInnerClass(final UnresolvedClassInfo innerClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * ��`���Ă��郁�\�b�h��ǉ�����
     * 
     * @param definedMethod ��`���Ă��郁�\�b�h
     */
    public void addDefinedMethod(final UnresolvedMethodInfo definedMethod) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * ��`���Ă���R���X�g���N�^��ǉ�����
     * 
     * @param definedConstructor ��`���Ă���R���X�g���N�^���\�b�h
     */
    public void addDefinedConstructor(final UnresolvedConstructorInfo definedConstructor) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedConstructor) {
            throw new NullPointerException();
        }

        this.definedConstructors.add(definedConstructor);
    }

    /**
     * ��`���Ă���t�B�[���h��ǉ�����
     * 
     * @param definedField ��`���Ă���t�B�[���h
     */
    public void addDefinedField(final UnresolvedFieldInfo definedField) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X��ǉ�����
     * 
     * @param importStatement ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X
     */
    public void addImportStatement(final UnresolvedImportStatementInfo importStatement) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatement) {
            throw new IllegalArgumentException();
        }

        this.importStatements.add(importStatement);
    }

    /**
     * ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X�Q��ǉ�����
     * 
     * @param importStatements ���̃N���X�ɂ����ė��p�\�ȁi�C���|�[�g����Ă���j�N���X�Q
     */
    public void addImportStatements(final List<UnresolvedImportStatementInfo> importStatements) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatements) {
            throw new IllegalArgumentException();
        }

        this.importStatements.addAll(importStatements);
    }

    /**
     * �e�N���X���̃Z�b�g��Ԃ�
     * 
     * @return �e�N���X���̃Z�b�g
     */
    public Set<UnresolvedClassTypeInfo> getSuperClasses() {
        return Collections.unmodifiableSet(this.superClasses);
    }

    /**
     * �C���i�[�N���X�̃Z�b�g��Ԃ�
     * 
     * @return �C���i�[�N���X�̃Z�b�g
     */
    public Set<UnresolvedClassInfo> getInnerClasses() {
        return Collections.unmodifiableSet(this.innerClasses);
    }

    /**
     * �O���̃��j�b�g��Ԃ�
     * 
     * @return �O���̃��j�b�g. �O���̃��j�b�g���Ȃ��ꍇ��null
     */
    public UnresolvedUnitInfo<?> getOuterUnit() {
        return this.outerUnit;
    }

    /**
     * ��`���Ă��郁�\�b�h�̃Z�b�g��Ԃ�
     * 
     * @return ��`���Ă��郁�\�b�h�̃Z�b�g
     */
    public Set<UnresolvedMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSet(this.definedMethods);
    }

    /**
     * ��`���Ă���R���X�g���N�^�̃Z�b�g��Ԃ�
     * 
     * @return ��`���Ă���R���X�g���N�^�̃Z�b�g
     */
    public Set<UnresolvedConstructorInfo> getDefinedConstructors() {
        return Collections.unmodifiableSet(this.definedConstructors);
    }

    /**
     * ��`���Ă���t�B�[���h�̃Z�b�g
     * 
     * @return ��`���Ă���t�B�[���h�̃Z�b�g
     */
    public Set<UnresolvedFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSet(this.definedFields);
    }

    /**
     * ���p�\�ȃN���X�i�C���|�[�g����Ă���N���X�j��List��Ԃ�
     * 
     * @return�@���p�\�ȃN���X�i�C���|�[�g����Ă���N���X�j��List��Ԃ�
     */
    public List<UnresolvedImportStatementInfo> getImportStatements() {
        return Collections.unmodifiableList(this.importStatements);
    }
    
    /**
     * �C���X�^���X�C�j�V�����C�U��Ԃ�
     * @return �C���X�����X�C�j�V�����C�U
     */
    public UnresolvedInstanceInitializerInfo getInstanceInitializer() {
        return instanceInitializer;
    }
    
    /**
     * �X�^�e�B�b�N�C�j�V�����C�U��Ԃ�
     * @return �X�^�e�B�b�N�C�j�V�����C�U
     */
    public UnresolvedStaticInitializerInfo getStaticInitializer() {
        return staticInitializer;
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
     * �O���̃��j�b�g���Z�b�g����
     * 
     * @param outerUnit �O���̃��j�b�g
     */
    public void setOuterUnit(final UnresolvedUnitInfo<?> outerUnit) {
        this.outerUnit = outerUnit;
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
     * �C���^�[�t�F�[�X���ǂ�����Ԃ�
     * 
     * @return �C���^�[�t�F�[�X�̏ꍇ��true, �����łȂ��ꍇ��false
     */
    public final boolean isInterface() {
        return this.isInterface;
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
     * �C���^�[�t�F�[�X���ǂ������Z�b�g����D
     * @param isInterface �C���^�[�t�F�[�X�̏ꍇ�� true�C�N���X�̏ꍇ�� false
     */
    public void setIsInterface(final boolean isInterface) {
        this.isInterface = isInterface;
    }

    /**
     * �����N���X���ǂ������Z�b�g����
     * 
     * @param anonymous �����N���X�̏ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public void setAnonymous(final boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * �����N���X���ǂ�����Ԃ�
     * 
     * @return �����N���X�ł���ꍇ��true, �����łȂ��ꍇ��false
     */
    public boolean isAnonymous() {
        return this.anonymous;
    }

    /**
     * ���̖������N���X������������
     * 
     * @param usingClass �����N���X�C���̃��\�b�h�Ăяo���̍ۂ� null ���Z�b�g����Ă���Ǝv����D
     * @param usingMethod �������\�b�h�C���̃��\�b�h�Ăяo���̍ۂ� null ���Z�b�g����Ă���Ǝv����D
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public TargetClassInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �C���q�C���S���薼�C�s���C�����C�C���X�^���X�����o�[���ǂ������擾
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String[] fullQualifiedName = this.getFullQualifiedName();
        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ClassInfo �I�u�W�F�N�g���쐬���CClassInfoManager�ɓo�^
        // �����N���X�̏ꍇ
        if (this.isAnonymous()) {
            final UnitInfo resolvedOuterUnit = this.outerUnit.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo = new TargetAnonymousClassInfo(fullQualifiedName, resolvedOuterUnit,
                    this.fileInfo, fromLine, fromColumn, toLine, toColumn);

            // ��ԊO���̃N���X�̏ꍇ
        } else if (null == this.outerUnit) {
            this.resolvedInfo = new TargetClassInfo(modifiers, fullQualifiedName, privateVisible,
                    namespaceVisible, inheritanceVisible, publicVisible, instance,
                    this.isInterface, this.fileInfo, fromLine, fromColumn, toLine, toColumn);

            // �C���i�[�N���X�̏ꍇ
        } else {
            final UnitInfo resolvedOuterUnit = this.outerUnit.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo = new TargetInnerClassInfo(modifiers, fullQualifiedName,
                    resolvedOuterUnit, privateVisible, namespaceVisible, inheritanceVisible,
                    publicVisible, instance, this.isInterface, this.fileInfo, fromLine, fromColumn,
                    toLine, toColumn);
        }

        // ���p�\�ȃN���X�𖼑O�������C�����ς݃N���X�ɓo�^
        final List<UnresolvedImportStatementInfo> unresolvedImportStatements = this
                .getImportStatements();
        for (final UnresolvedImportStatementInfo unresolvedImportStatement : unresolvedImportStatements) {
            final ImportStatementInfo importStatement = unresolvedImportStatement.resolve(
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            final SortedSet<ClassInfo> importedClasses = importStatement.getImportClasses();
            this.resolvedInfo.addaccessibleClasses(importedClasses);
        }

        return this.resolvedInfo;
    }

    /**
     * ���̖������N���X��`���̖������Q�ƌ^��Ԃ�
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     * @return ���̖������N���X��`���̖������Q�ƌ^
     */
    public UnresolvedClassReferenceInfo getClassReference(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        final UnresolvedClassReferenceInfo classReference = new UnresolvedFullQualifiedNameClassReferenceInfo(
                this);
        classReference.setFromLine(fromLine);
        classReference.setFromColumn(fromColumn);
        classReference.setToLine(toLine);
        classReference.setToColumn(toColumn);

        for (UnresolvedTypeParameterInfo typeParameter : this.typeParameters) {
            classReference.addTypeArgument(typeParameter);
        }
        return classReference;
    }

    public UnresolvedClassTypeInfo getClassType() {
        if (null != this.classType) {
            return this.classType;
        }
        final List<UnresolvedImportStatementInfo> namespaces = new LinkedList<UnresolvedImportStatementInfo>();
        final UnresolvedImportStatementInfo namespace = new UnresolvedImportStatementInfo(this
                .getFullQualifiedName(), false);
        namespaces.add(namespace);
        this.classType = new UnresolvedClassTypeInfo(namespaces, this.getFullQualifiedName());
        return this.classType;
    }

    /**
     * �N���X���L�q����Ă���t�@�C������ۑ����邽�߂̕ϐ�
     */
    private final FileInfo fileInfo;

    /**
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private String[] namespace;

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private String className;

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * �^�p�����[�^��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;

    /**
     * �e�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedClassTypeInfo> superClasses;

    /**
     * �C���i�[�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedClassInfo> innerClasses;

    /**
     * �O���̃��j�b�g��ێ�����ϐ�
     */
    private UnresolvedUnitInfo<?> outerUnit;

    /**
     * ��`���Ă��郁�\�b�h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedMethodInfo> definedMethods;

    /**
     * ��`���Ă���R���X�g���N�^��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedConstructorInfo> definedConstructors;

    /**
     * ��`���Ă���t�B�[���h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedFieldInfo> definedFields;

    /**
     * �X�^�e�B�b�N�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedStaticInitializerInfo staticInitializer;
    
    /**
     * �C���X�^���X�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedInstanceInitializerInfo instanceInitializer;
    
    /**
     * ���p�\�Ȗ��O��Ԃ�ۑ����邽�߂̃Z�b�g
     */
    private final List<UnresolvedImportStatementInfo> importStatements;

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
     * �C���^�[�t�F�[�X�ł��邩�ǂ�����ۑ����邽�߂̕ϐ�
     */
    private boolean isInterface;

    /**
     * �����N���X���ǂ�����\���ϐ�
     */
    private boolean anonymous;

    private UnresolvedClassTypeInfo classType = null;

}
