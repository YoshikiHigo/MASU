package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �Ăяo���\�ȒP��(���\�b�h��R���X�g���N�^)��\���N���X
 * 
 * @author higo
 */

public abstract class CallableUnitInfo extends LocalSpaceInfo implements Visualizable, Modifier,
        TypeParameterizable, Comparable<CallableUnitInfo> {

    /**
     * �I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q��Set
     * @param ownerClass ���L�N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    CallableUnitInfo(final Set<ModifierInfo> modifiers, final ClassInfo ownerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.parameters = new LinkedList<ParameterInfo>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.typeParameterUsages = new HashMap<TypeParameterInfo, TypeInfo>();

        this.unresolvedUsage = new HashSet<UnresolvedExpressionInfo<?>>();

        this.callers = new TreeSet<CallableUnitInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * ��`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ��`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(super.getDefinedVariables());
        definedVariables.addAll(this.getParameters());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * ���\�b�h�Ԃ̏����֌W���`���郁�\�b�h�D�ȉ��̏����ŏ��������߂�D
     * <ol>
     * <li>���\�b�h���`���Ă���N���X�̖��O��Ԗ�</li>
     * <li>���\�b�h���`���Ă���N���X�̃N���X��</li>
     * <li>���\�b�h��</li>
     * <li>���\�b�h�̈����̌�</li>
     * <li>���\�b�h�̈����̌^�i���������珇�ԂɁj</li>
     */
    @Override
    public int compareTo(final CallableUnitInfo target) {

        if (null == target) {
            throw new IllegalArgumentException();
        }

        // �N���X�I�u�W�F�N�g�� compareTo ��p����D
        // �N���X�̖��O��Ԗ��C�N���X������r�ɗp�����Ă���D
        final ClassInfo ownerClass = this.getOwnerClass();
        final ClassInfo correspondOwnerClass = target.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        }

        return this.compareArgumentsTo(target);
    }

    /**
     * �����̌^�ɂ���r���s��
     * 
     * @param target ��r�Ώۃ��j�b�g
     * @return
     */
    public int compareArgumentsTo(final CallableUnitInfo target) {
        // �����̌��Ŕ�r
        final int parameterNumber = this.getParameterNumber();
        final int correspondParameterNumber = target.getParameterNumber();
        if (parameterNumber < correspondParameterNumber) {
            return 1;
        } else if (parameterNumber > correspondParameterNumber) {
            return -1;
        } else {

            // �����̌^�Ŕ�r�D���������珇�ԂɁD
            final Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
            final Iterator<ParameterInfo> correspondParameterIterator = target.getParameters()
                    .iterator();
            while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                final ParameterInfo parameter = parameterIterator.next();
                final ParameterInfo correspondParameter = correspondParameterIterator.next();
                final String typeName = parameter.getType().getTypeName();
                final String correspondTypeName = correspondParameter.getType().getTypeName();
                final int typeOrder = typeName.compareTo(correspondTypeName);
                if (typeOrder != 0) {
                    return typeOrder;
                }
            }

            return 0;
        }
    }

    /**
     * ���̃I�u�W�F�N�g���C�����ŗ^����ꂽ�����g���ČĂяo�����Ƃ��ł��邩�ǂ����𔻒肷��D
     * 
     * @param actualParameters �������̃��X�g
     * @return �Ăяo����ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    boolean canCalledWith(final List<ExpressionInfo> actualParameters) {

        if (null == actualParameters) {
            throw new IllegalArgumentException();
        }

        // �����̐����������Ȃ��ꍇ�͊Y�����Ȃ�
        final List<ParameterInfo> dummyParameters = this.getParameters();
        if (dummyParameters.size() != actualParameters.size()) {
            return false;
        }

        // �����̌^��擪����`�F�b�N�������Ȃ��ꍇ�͊Y�����Ȃ�
        final Iterator<ParameterInfo> dummyParameterIterator = dummyParameters.iterator();
        final Iterator<ExpressionInfo> actualParameterIterator = actualParameters.iterator();
        NEXT_PARAMETER: while (dummyParameterIterator.hasNext()
                && actualParameterIterator.hasNext()) {
            final ParameterInfo dummyParameter = dummyParameterIterator.next();
            final ExpressionInfo actualParameter = actualParameterIterator.next();

            TypeInfo actualParameterType = actualParameter.getType();

            // �^�p�����[�^�̏ꍇ�͂��̌p���^�����߂�
            if (actualParameterType instanceof TypeParameterInfo) {
                final TypeInfo extendsType = ((TypeParameterInfo) actualParameterType)
                        .getExtendsType();
                if (null != extendsType) {
                    actualParameterType = extendsType;
                } else {
                    assert false : "Here should not be reached";
                }
            }

            // ���������Q�ƌ^�̏ꍇ
            if (actualParameterType instanceof ClassTypeInfo) {

                // �������̌^�̃N���X���擾
                final ClassInfo actualParameterClass = ((ClassTypeInfo) actualParameterType)
                        .getReferencedClass();

                // ���������Q�ƌ^�łȂ��ꍇ�͊Y�����Ȃ�
                if (!(dummyParameter.getType() instanceof ClassTypeInfo)) {
                    return false;
                }

                // �������̌^�̃N���X���擾
                final ClassInfo dummyParameterClass = ((ClassTypeInfo) dummyParameter.getType())
                        .getReferencedClass();

                // �������C���������ɑΏۃN���X�ł���ꍇ�́C���̌p���֌W���l������D�܂�C���������������̃T�u�N���X�łȂ��ꍇ�́C�Ăяo���\�ł͂Ȃ�
                if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof TargetClassInfo)) {

                    // ���������������Ɠ����Q�ƌ^�i�N���X�j�ł��Ȃ��C�������̃T�u�N���X�ł��Ȃ��ꍇ�͊Y�����Ȃ�
                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else {
                        return false;
                    }

                    // �������C���������ɊO���N���X�ł���ꍇ�́C�������ꍇ�̂݌Ăяo���\�Ƃ���
                } else if ((actualParameterClass instanceof ExternalClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // ���������O���N���X�C���������ΏۃN���X�̏ꍇ�́C���������������̃T�u�N���X�ł���ꍇ�C�Ăяo���\�Ƃ���
                } else if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // ���������ΏۃN���X�C���������O���N���X�̏ꍇ�́C�Ăяo���s�\�Ƃ���
                } else {
                    return false;
                }

                // ���������v���~�e�B�u�^�̏ꍇ
            } else if (actualParameterType instanceof PrimitiveTypeInfo) {

                // PrimitiveTypeInfo#equals ���g���ē������̔���D
                // �������Ȃ��ꍇ�͊Y�����Ȃ�
                // �v���~�e�B�u�^�C�vString��dummmyType�̌^����String�Ȃ瓙��
                // TODO �N���X����String�ł��邪java.lang.String�ł͂Ȃ��ꍇ�C����~�X��������D
                if (actualParameterType.equals(dummyParameter.getType())) {
                    continue NEXT_PARAMETER;
                }

                return false;

                // ���������z��^�̏ꍇ
            } else if (actualParameterType instanceof ArrayTypeInfo) {

                if (!(dummyParameter.getType() instanceof ArrayTypeInfo)) {
                    return false;
                }

                if (!actualParameter.getType().equals(dummyParameter.getType())) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java����̏ꍇ�́C�������� java.lang.object �ł�OK�ȏ������K�v

                // �������� null �̏ꍇ
            } else if (actualParameter instanceof NullUsageInfo) {

                // ���������Q�ƌ^�łȂ��ꍇ�͊Y�����Ȃ�
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java����̏ꍇ�́C���������z��^�̏ꍇ�ł�OK�ȏ������K�v

                // �������̌^�������ł��Ȃ������ꍇ
            } else if (actualParameterType instanceof UnknownTypeInfo) {

                // �������̌^���s���ȏꍇ�́C�������̌^�����ł��낤�Ƃ�OK�ɂ��Ă���
                continue NEXT_PARAMETER;

            } else {
                assert false : "Here shouldn't be reached!";
            }
        }

        return true;
    }

    /**
     * ���̃��\�b�h�̈����� List ��Ԃ��D
     * 
     * @return ���̃��\�b�h�̈����� List
     */
    public final List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * ���̃��\�b�h�̈����̐���Ԃ�
     * 
     * @return ���̃��\�b�h�̈����̐�
     */
    public final int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * �^�p�����[�^�̎g�p��ǉ�����
     * 
     * @param typeParameterInfo �^�p�����[�^ 
     * @param usedType �^�p�����[�^�ɑ������Ă���^
     */
    @Override
    public void addTypeParameterUsage(final TypeParameterInfo typeParameterInfo,
            final TypeInfo usedType) {

        if ((null == typeParameterInfo) || (null == usedType)) {
            throw new IllegalArgumentException();
        }

        this.typeParameterUsages.put(typeParameterInfo, usedType);
    }

    /**
     * �^�p�����[�^�g�p�̃}�b�v��Ԃ�
     * 
     * @return �^�p�����[�^�g�p�̃}�b�v
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        return Collections.unmodifiableMap(this.typeParameterUsages);
    }

    /**
     * �����Ŏw�肳�ꂽ�^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^
     */
    @Override
    public void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * �w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^��Ԃ�
     * 
     * @param index �^�p�����[�^�̃C���f�b�N�X
     * @return�@�w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^
     */
    @Override
    public TypeParameterInfo getTypeParameter(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * ���̃N���X�̌^�p�����[�^�� List ��Ԃ��D
     * 
     * @return ���̃N���X�̌^�p�����[�^�� List
     */
    @Override
    public List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo����ǉ�����D �v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param entityUsage ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo��
     */
    public void addUnresolvedUsage(final UnresolvedExpressionInfo<?> entityUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(entityUsage);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set ��Ԃ��D
     * 
     * @return ���̃��\�b�h���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set
     */
    public Set<UnresolvedExpressionInfo<?>> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�܂��̓R���X�g���N�^��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param caller �ǉ�����Ăяo�����\�b�h
     */
    public final void addCaller(final CallableUnitInfo caller) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caller) {
            throw new NullPointerException();
        }

        this.callers.add(caller);
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�܂��̓R���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<CallableUnitInfo> getCallers() {
        return Collections.unmodifiableSortedSet(this.callers);
    }

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
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * �^�p�����[�^��ۑ�����ϐ�
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * ���̃N���X�Ŏg�p����Ă���^�p�����[�^�Ǝ��ۂɌ^�p�����[�^�ɑ������Ă���^�̃y�A.
     * ���̃N���X�Œ�`����Ă���^�p�����[�^�ł͂Ȃ��D
     */
    private final Map<TypeParameterInfo, TypeInfo> typeParameterUsages;

    /**
     * �����̃��X�g�̕ۑ����邽�߂̕ϐ�
     */
    protected final List<ParameterInfo> parameters;

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<CallableUnitInfo> callers;

    /**
     * ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���Ȃǂ�ۑ����邽�߂̕ϐ�
     */
    private final transient Set<UnresolvedExpressionInfo<?>> unresolvedUsage;
}
