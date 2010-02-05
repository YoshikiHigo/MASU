package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
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
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class TargetClassInfo extends
        ClassInfo<TargetFieldInfo, TargetMethodInfo, TargetConstructorInfo, TargetInnerClassInfo>
        implements Visualizable, StaticOrInstance {

    /**
     * �w�肳�ꂽ�N���X�Ɋ܂܂��S�ẴC���i�[�N���X��Ԃ�
     * 
     * @param classInfo �w�肷��N���X
     * @return�@�w�肳�ꂽ�N���X�Ɋ܂܂��S�ẴC���i�[�N���X
     */
    static public Collection<TargetInnerClassInfo> getAllInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final SortedSet<TargetInnerClassInfo> innerClassInfos = new TreeSet<TargetInnerClassInfo>();
        for (final TargetInnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {

            innerClassInfos.add(innerClassInfo);
            innerClassInfos.addAll(getAllInnerClasses(innerClassInfo));
        }

        return Collections.unmodifiableSortedSet(innerClassInfos);
    }

    /**
     * �w�肵���N���X�ɂ����ăA�N�Z�X�\�ȃC���i�[�N���X�ꗗ��Ԃ��D
     * �A�N�Z�X�\�ȃN���X�Ƃ́C�w�肳�ꂽ�N���X�C�������͂��̐e�N���X���ɒ�`���ꂽ�N���X����D
     * 
     * @param classInfo �w�肳�ꂽ�N���X
     * @return �w�肵���N���X�ɂ����ăA�N�Z�X�\�ȃC���i�[�N���X�ꗗ��Ԃ��D
     */
    static public Collection<TargetInnerClassInfo> getAccessibleInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final Set<TargetClassInfo> classCache = new HashSet<TargetClassInfo>();

        return Collections.unmodifiableCollection(getAccessibleInnerClasses(classInfo, classCache));
    }

    static private Collection<TargetInnerClassInfo> getAccessibleInnerClasses(
            final TargetClassInfo classInfo, final Set<TargetClassInfo> classCache) {

        if ((null == classInfo) || (null == classCache)) {
            throw new IllegalArgumentException();
        }

        if (classCache.contains(classInfo)) {
            return Collections.unmodifiableCollection(new TreeSet<TargetInnerClassInfo>());
        }

        classCache.add(classInfo);

        final SortedSet<TargetInnerClassInfo> innerClassInfos = new TreeSet<TargetInnerClassInfo>();

        for (final TargetInnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            innerClassInfos.add(innerClassInfo);
            innerClassInfos.addAll(getAccessibleInnerClasses(innerClassInfo, classCache));
        }

        for (final ClassInfo<?, ?, ?, ?> superClassInfo : ClassTypeInfo.convert(classInfo
                .getSuperClasses())) {
            if (superClassInfo instanceof TargetClassInfo) {
                if (superClassInfo instanceof TargetInnerClassInfo) {
                    innerClassInfos.add((TargetInnerClassInfo) superClassInfo);
                }
                innerClassInfos.addAll(getAccessibleInnerClasses((TargetClassInfo) superClassInfo,
                        classCache));
            }
        }

        return Collections.unmodifiableCollection(innerClassInfos);
    }

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
     * @param isInterface �C���^�[�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, namespace, className, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.implicitInstanceInitializer = new InstanceInitializerInfo(this, 0, 0, 0, 0);
        this.implicitStaticInitializer = new StaticInitializerInfo(this, 0, 0, 0, 0);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.instanceInitializers.add(this.implicitInstanceInitializer);
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.staticInitializers.add(this.implicitStaticInitializer);
        this.accessibleClasses = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;
        this.isInterface = isInterface;

        this.instance = instance;

        this.ownerFile = fileInfo;
    }

    /**
     * ���S���薼��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param fullQualifiedName ���S���薼
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param isInterface �C���^�t�F�[�X�ł��邩�ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fullQualifiedName, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers || null == fileInfo) {
            throw new NullPointerException();
        }

        this.implicitInstanceInitializer = new InstanceInitializerInfo(this, 0, 0, 0, 0);
        this.implicitStaticInitializer = new StaticInitializerInfo(this, 0, 0, 0, 0);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.instanceInitializers.add(this.implicitInstanceInitializer);
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.staticInitializers.add(this.implicitStaticInitializer);
        this.accessibleClasses = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;
        this.isInterface = isInterface;

        this.instance = instance;

        this.ownerFile = fileInfo;
    }

    /**
     * �C���X�^���X�C�j�V�����C�U�[��ǉ�����
     * 
     * @param instanceInitializer �ǉ������C���X�^���X�C�j�V�����C�U�[
     */
    public final void addInstanceInitializer(final InstanceInitializerInfo instanceInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == instanceInitializer) {
            throw new NullPointerException();
        }

        this.instanceInitializers.add(instanceInitializer);
    }

    /**
     * �X�^�e�B�b�N�C�j�V�����C�U�[��ǉ�����
     * 
     * @param staticInitializer �ǉ������X�^�e�B�b�N�C�j�V�����C�U�[
     */
    public final void addStaticInitializer(final StaticInitializerInfo staticInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == staticInitializer) {
            throw new NullPointerException();
        }

        this.staticInitializers.add(staticInitializer);
    }

    /**
     * ���̃N���X�ɂ����ăA�N�Z�X�\�ȃN���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[.
     * 
     * @param accessibleClass �A�N�Z�X�\�ȃN���X
     */
    public final void addAccessibleClass(final ClassInfo<?, ?, ?, ?> accessibleClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == accessibleClass) {
            throw new IllegalArgumentException();
        }

        this.accessibleClasses.add(accessibleClass);
    }

    /**
     * ���̃N���X�ɂ����ăA�N�Z�X�\�ȃN���X�Q��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[
     * 
     * @param accessibleClasses �A�N�Z�X�\�ȃN���X�Q
     */
    public final void addaccessibleClasses(final Set<ClassInfo<?, ?, ?, ?>> accessibleClasses) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == accessibleClasses) {
            throw new IllegalArgumentException();
        }

        this.accessibleClasses.addAll(accessibleClasses);
    }

    /**
     * ���̃N���X�̈Öق̃C���X�^���X�C�j�V�����C�U��Ԃ�
     * @return �Öق̃C���X�^���X�C�j�V�����C�U
     */
    public InstanceInitializerInfo getImplicitInstanceInitializer() {
        return this.implicitInstanceInitializer;
    }

    /**
     * ���̃N���X�̃C���X�^���X�C�j�V�����C�U�ꗗ��Ԃ�
     * @return ���̃N���X�̃C���X�^���X�C�j�V�����C�U�ꗗ
     */
    public SortedSet<InstanceInitializerInfo> getInstanceInitializers() {
        return this.instanceInitializers;
    }

    /**
     * ���̃N���X�̈Öق̃X�^�e�B�b�N�C�j�V�����C�U��Ԃ�
     * @return �Öق̃X�^�e�B�b�N�C�j�V�����C�U
     */
    public StaticInitializerInfo getImplicitStaticInitializer() {
        return this.implicitStaticInitializer;
    }

    /**
     * ���̃N���X�̃X�^�e�B�b�N�C�j�V�����C�U�ꗗ��Ԃ�
     * @return �X�^�e�B�b�N�C�j�V�����C�U�ꗗ
     */
    public SortedSet<StaticInitializerInfo> getStaticInitializers() {
        return this.staticInitializers;
    }

    /**
     * ���̃N���X�ɂ����ăA�N�Z�X�\�ȃN���X��SortedSet��Ԃ��D
     * 
     * @return ���̃N���X�ɂ����ăA�N�Z�X�\�ȃN���X��SortedSet
     */
    public final Set<ClassInfo<?, ?, ?, ?>> getAccessibleClasses() {
        return Collections.unmodifiableSet(this.accessibleClasses);
    }

    /**
     * ���̃N���X���ɂ�����ϐ��g�p��Set��Ԃ�
     * 
     * @return ���̃N���X���ɂ�����ϐ��g�p��Set
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {

        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        // ���\�b�h���Ŏg�p����Ă���ϐ���ǉ�
        for (final TargetMethodInfo definedMethod : this.getDefinedMethods()) {
            variableUsages.addAll(definedMethod.getVariableUsages());
        }

        // �R���X�g���N�^���Ŏg�p����Ă���ϐ���ǉ�
        for (final TargetConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            variableUsages.addAll(definedConstructor.getVariableUsages());
        }

        // �����N���X�Ŏg�p����Ă���ϐ���ǉ�
        for (final TargetInnerClassInfo innerClass : this.getInnerClasses()) {
            variableUsages.addAll(innerClass.getVariableUsages());
        }

        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * ���̃N���X���Œ�`����Ă���ϐ���Set��Ԃ�
     * 
     * @return ���̃N���X���Œ�`����Ă���ϐ���Set
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {

        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();

        // ��`����Ă���t�B�[���h��ǉ�
        definedVariables.addAll(this.getDefinedFields());

        // ���\�b�h���Œ�`����Ă���ϐ���ǉ�
        for (final TargetMethodInfo definedMethod : this.getDefinedMethods()) {
            definedVariables.addAll(definedMethod.getDefinedVariables());
        }

        // �R���X�g���N�^���Œ�`����Ă���ϐ���ǉ�
        for (final TargetConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            definedVariables.addAll(definedConstructor.getDefinedVariables());
        }

        // �����N���X�Œ�`����Ă���ϐ���ǉ�
        for (final TargetInnerClassInfo innerClass : this.getInnerClasses()) {
            definedVariables.addAll(innerClass.getDefinedVariables());
        }

        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * ���̃N���X���ɂ�����Ăяo����Set��Ԃ�
     * 
     * @return ���̃N���X���ɂ�����Ăяo����Set
     */
    @Override
    public final Set<CallInfo<? extends CallableUnitInfo>> getCalls() {

        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();

        // ���\�b�h���ł̌Ăяo����ǉ�
        for (final TargetMethodInfo definedMethod : this.getDefinedMethods()) {
            calls.addAll(definedMethod.getCalls());
        }

        // �R���X�g���N�^���ł̌Ăяo����ǉ�
        for (final TargetConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            calls.addAll(definedConstructor.getCalls());
        }

        // �����N���X�ł̌Ăяo����ǉ�
        for (final TargetInnerClassInfo innerClass : this.getInnerClasses()) {
            calls.addAll(innerClass.getCalls());
        }

        return Collections.unmodifiableSet(calls);
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
     * �C���^�[�t�F�[�X���ǂ����Ԃ��D
     * 
     * @return �C���^�[�t�F�[�X�̏ꍇ true�C�N���X�̏ꍇ false
     */
    public final boolean isInterface() {
        return this.isInterface;
    }

    /**
     * �N���X���ǂ����Ԃ��D
     * 
     *  @return �N���X�̏ꍇ true�C�C���^�[�t�F�[�X�̏ꍇ false
     */
    public final boolean isClass() {
        return !this.isInterface;
    }

    /**
     * ���̃N���X��錾���Ă���t�@�C������Ԃ�
     * 
     * @return ���̃N���X��錾���Ă���t�@�C�����
     */
    public final FileInfo getOwnerFile() {
        return this.ownerFile;
    }

    /**
     * ���̃N���X�̃X�^�e�B�b�N�C�j�V�����C�U�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<StaticInitializerInfo> staticInitializers;

    /**
     * ���̃N���X�̃C���X�^���X�C�j�V�����C�U�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<InstanceInitializerInfo> instanceInitializers;

    /**
     * ���̃N���X�̈Öق̃C���X�^���X�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final InstanceInitializerInfo implicitInstanceInitializer;

    /**
     * ���̃N���X�̈Öق̃X�^�e�B�b�N�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final StaticInitializerInfo implicitStaticInitializer;

    /**
     * ���̃N���X������A�N�Z�X�\�ȃN���X
     */
    private final Set<ClassInfo<?, ?, ?, ?>> accessibleClasses;

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

    /**
     * �C���^�[�t�F�[�X�ł��邩�ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean isInterface;

    /**
     * ���̃N���X��錾���Ă���t�@�C������ۑ����邽�߂̕ϐ�
     */
    private final FileInfo ownerFile;
}
