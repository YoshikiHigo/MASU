package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
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
 * <li>�t�@�C�����ʒu</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedClassInfo implements UnresolvedTypeInfo, VisualizableSetting,
        MemberSetting, PositionSetting, NameResolvable<TargetClassInfo> {

    /**
     * �����Ȃ��R���X�g���N�^
     */
    public UnresolvedClassInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.namespace = null;
        this.className = null;
        this.loc = 0;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.superClasses = new LinkedHashSet<UnresolvedTypeInfo>();
        this.innerClasses = new HashSet<UnresolvedClassInfo>();
        this.definedMethods = new HashSet<UnresolvedMethodInfo>();
        this.definedFields = new HashSet<UnresolvedFieldInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;

        this.instance = true;

        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;

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
    public void addTypeParameter(final UnresolvedTypeParameterInfo typeParameter){
        
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter){
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
     * @return �������^�p�����[�^�� List
     */
    public List<UnresolvedTypeParameterInfo> getTypeParameters(){
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
     * �s�����擾����
     * 
     * @return �s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * �s����ۑ�����
     * 
     * @param loc �s��
     */
    public void setLOC(final int loc) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be o or more!");
        }

        this.loc = loc;
    }

    /**
     * �e�N���X��ǉ�����
     * 
     * @param superClass �e�N���X��
     */
    public void addSuperClass(final UnresolvedReferenceTypeInfo superClass) {

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
     * �e�N���X���̃Z�b�g��Ԃ�
     * 
     * @return �e�N���X���̃Z�b�g
     */
    public Set<UnresolvedTypeInfo> getSuperClasses() {
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
     * �O���N���X��Ԃ�
     * 
     * @return �O���N���X. �O���N���X���Ȃ��ꍇ��null
     */
    public UnresolvedClassInfo getOuterClass() {
        return this.outerClass;
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
     * ��`���Ă���t�B�[���h�̃Z�b�g
     * 
     * @return ��`���Ă���t�B�[���h�̃Z�b�g
     */
    public Set<UnresolvedFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSet(this.definedFields);
    }

    /**
     * �^����Ԃ�
     * 
     * @return �^��
     */
    public String getTypeName() {

        final String delimiter = Settings.getLanguage().getNamespaceDelimiter();
        final StringBuffer buffer = new StringBuffer();

        for (final String name : this.getFullQualifiedName()) {
            buffer.append(name);
            buffer.append(delimiter);
        }

        int delimiterLength = 0;
        if (null != delimiter) {
            delimiterLength = delimiter.length();
        }

        return buffer.substring(0, buffer.length() - delimiterLength);
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
     * �O���N���X���Z�b�g����
     * 
     * @param outerClass �O���N���X
     */
    public void setOuterClass(final UnresolvedClassInfo outerClass) {
        this.outerClass = outerClass;
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
     * �J�n�s���Z�b�g����
     * 
     * @param fromLine �J�n�s
     */
    public void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * �J�n����Z�b�g����
     * 
     * @param fromColumn �J�n��
     */
    public void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * �I���s���Z�b�g����
     * 
     * @param toLine �I���s
     */
    public void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * �I������Z�b�g����
     * 
     * @param toColumn �I����
     */
    public void setToColumn(final int toColumn) {

        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public int getToColumn() {
        return this.toColumn;
    }

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     */
    public TargetClassInfo getResolvedInfo() {
        return this.resolvedInfo;
    }

    /**
     * ���O�������ꂽ�����Z�b�g����
     * 
     * @param resolvedInfo ���O�������ꂽ���
     */
    public void setResolvedInfo(final TargetClassInfo resolvedInfo) {

        if (null == resolvedInfo) {
            throw new NullPointerException();
        }

        this.resolvedInfo = resolvedInfo;
    }
    
    /**
     * ���ɖ��O�������ꂽ���ǂ�����Ԃ�
     * 
     * @return ���O��������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private String[] namespace;

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private String className;

    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private int loc;

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
    private final Set<UnresolvedTypeInfo> superClasses;

    /**
     * �C���i�[�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedClassInfo> innerClasses;

    /**
     * �O���̃N���X��ێ�����ϐ�
     */
    private UnresolvedClassInfo outerClass;

    /**
     * ��`���Ă��郁�\�b�h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedMethodInfo> definedMethods;

    /**
     * ��`���Ă���t�B�[���h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedFieldInfo> definedFields;

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
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int toColumn;

    /**
     * ���O�������ꂽ�����i�[���邽�߂̕ϐ�
     */
    private TargetClassInfo resolvedInfo;
}
