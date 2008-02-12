package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X�̏���ۗL����N���X�D�ȉ��̏������D
 * <ul>
 * <li>�N���X��</li>
 * <li>�C���q</li>
 * <li>���O��ԁi�p�b�P�[�W���j</li>
 * <li>�s��</li>
 * <li>�p�����Ă���N���X</li>
 * <li>�p������Ă���N���X</li>
 * <li>�Q�Ƃ��Ă���N���X</li>
 * <li>�Q�Ƃ���Ă���N���X</li>
 * <li>�����N���X</li>
 * <li>���̃N���X���Œ�`����Ă��郁�\�b�h</li>
 * <li>���̃N���X���Œ�`����Ă���t�B�[���h</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public class TargetClassInfo extends ClassInfo implements Visualizable, Member {

    /**
     * ���O��Ԗ��C�N���X����^���ăN���X���I�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, namespace, className, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedConstructors = new TreeSet<TargetConstructorInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.instance = instance;
    }

    /**
     * ���S���薼��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param fullQualifiedName ���S���薼
     * @param loc �s��
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fullQualifiedName, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedConstructors = new TreeSet<TargetConstructorInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.instance = instance;
    }

    /**
     * ���̃N���X�ɃC���i�[�N���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param innerClass �ǉ�����C���i�[�N���X
     */
    public final void addInnerClass(final TargetInnerClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * ���̃N���X�̃C���i�[�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �C���i�[�N���X�� SortedSet
     */
    public final SortedSet<TargetInnerClassInfo> getInnerClasses() {
        return Collections.unmodifiableSortedSet(this.innerClasses);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ���\�b�h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedMethod �ǉ������`���ꂽ���\�b�h
     */
    public final void addDefinedMethod(final TargetMethodInfo definedMethod) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ�R���X�g���N�^����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedConstructor �ǉ������`���ꂽ�R���X�g���N�^
     */
    public final void addDefinedConstructor(final TargetConstructorInfo definedConstructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedConstructor) {
            throw new NullPointerException();
        }

        this.definedConstructors.add(definedConstructor);
    }

    /**
     * �����Ŏw�肳�ꂽ�^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^
     */
    public final void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * ���̃N���X�̌^�p�����[�^�� List ��Ԃ��D
     * 
     * @return ���̃N���X�̌^�p�����[�^�� List
     */
    public final List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * �w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^��Ԃ�
     * 
     * @param index �^�p�����[�^�̃C���f�b�N�X
     * @return�@�w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^
     */
    public final TypeParameterInfo getIndex(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ�t�B�[���h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedField �ǉ������`���ꂽ�t�B�[���h
     */
    public final void addDefinedField(final TargetFieldInfo definedField) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * ���̃N���X�ɒ�`����Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<TargetMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSortedSet(this.definedMethods);
    }

    /**
     * ���̃N���X�ɒ�`����Ă���R���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<TargetConstructorInfo> getDefinedConstructors() {
        return Collections.unmodifiableSortedSet(this.definedConstructors);
    }

    /**
     * ���̃N���X�ɒ�`����Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă���t�B�[���h�� SortedSet
     */
    public final SortedSet<TargetFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSortedSet(this.definedFields);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public final boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public final boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * �^�p�����[�^��ۑ�����ϐ�
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * ���̃N���X�̓����N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̓����N���X�݂̂�ۗL����D
     */
    private final SortedSet<TargetInnerClassInfo> innerClasses;

    /**
     * ���̃N���X�Œ�`����Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final SortedSet<TargetMethodInfo> definedMethods;

    /**
     * ���̃N���X�Œ�`����Ă���R���X�g���N�^�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final SortedSet<TargetConstructorInfo> definedConstructors;

    /**
     * ���̃N���X�Œ�`����Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final SortedSet<TargetFieldInfo> definedFields;

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean privateVisible;

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean namespaceVisible;

    /**
     * �q�N���X����Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean inheritanceVisible;

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean publicVisible;

    /**
     * �C���X�^���X�����o�[���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean instance;
}
