package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
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
 * @author y-higo
 * 
 */
public class TargetClassInfo extends ClassInfo implements Visualizable, Member {

    /**
     * ���O��Ԗ��C�N���X����^���ĕ�炷���I�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * @param loc �s��
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final int loc, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance) {

        super(namespace, className);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        if (loc < 0) {
            throw new IllegalAccessError("LOC is must be 0 or more!");
        }

        this.loc = loc;
        this.modifiers = new HashSet<ModifierInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.modifiers.addAll(modifiers);

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
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final int loc, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {

        super(fullQualifiedName);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        if (loc < 0) {
            throw new IllegalAccessError("LOC is must be 0 or more!");
        }

        this.loc = loc;
        this.modifiers = new HashSet<ModifierInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.modifiers.addAll(modifiers);

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
    public void addInnerClass(final TargetInnerClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * ���̃N���X�̍s����Ԃ�
     * 
     * @return ���̃N���X�̍s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * ���̃N���X�̏C���q�� Set ��Ԃ�
     * 
     * @return ���̃N���X�̏C���q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * ���̃N���X�̃C���i�[�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �C���i�[�N���X�� SortedSet
     */
    public SortedSet<TargetInnerClassInfo> getInnerClasses() {
        return Collections.unmodifiableSortedSet(this.innerClasses);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ���\�b�h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedMethod �ǉ������`���ꂽ���\�b�h
     */
    public void addDefinedMethod(final TargetMethodInfo definedMethod) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ�t�B�[���h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedField �ǉ������`���ꂽ�t�B�[���h
     */
    public void addDefinedField(final TargetFieldInfo definedField) {

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
    public SortedSet<TargetMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSortedSet(this.definedMethods);
    }

    /**
     * ���̃N���X�ɒ�`����Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă���t�B�[���h�� SortedSet
     */
    public SortedSet<TargetFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSortedSet(this.definedFields);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isInheritanceVisible() {
        return this.privateVisible;
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
        return this.inheritanceVisible;
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
     * �s����ۑ����邽�߂̕ϐ�
     */
    private final int loc;

    /**
     * �C���q��ۑ�����ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * ���̃N���X�̓����N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̓����N���X�݂̂�ۗL����D
     */
    private final SortedSet<TargetInnerClassInfo> innerClasses;

    /**
     * ���̃N���X�Œ�`����Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final SortedSet<TargetMethodInfo> definedMethods;

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
