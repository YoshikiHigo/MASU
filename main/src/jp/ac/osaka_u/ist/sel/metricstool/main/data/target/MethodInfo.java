package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class MethodInfo implements UnitInfo, Comparable<MethodInfo>, MetricMeasurable {

    /**
     * ���\�b�h�I�u�W�F�N�g������������
     * 
     * @param methodName ���\�b�h��
     * @param ownerClass ���\�b�h���`���Ă���N���X
     * @param constructor �R���X�g���N�^���ǂ���
     */
    public MethodInfo(final String methodName, final ClassInfo ownerClass, final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodName) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.ownerClass = ownerClass;
        this.constructor = constructor;
        this.returnType = null;

        this.parameters = new LinkedList<ParameterInfo>();
        this.callees = new HashSet<MemberCallInfo>();
        this.callers = new TreeSet<MethodInfo>();
        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
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
    public int compareTo(final MethodInfo method) {

        if (null == method) {
            throw new NullPointerException();
        }

        // �N���X�I�u�W�F�N�g�� compareTo ��p����D
        // �N���X�̖��O��Ԗ��C�N���X������r�ɗp�����Ă���D
        final ClassInfo ownerClass = this.getOwnerClass();
        final ClassInfo correspondOwnerClass = method.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        }

        // ���\�b�h���Ŕ�r
        final String name = this.getMethodName();
        final String correspondName = method.getMethodName();
        final int methodNameOrder = name.compareTo(correspondName);
        if (methodNameOrder != 0) {
            return methodNameOrder;
        }

        // �����̌��Ŕ�r
        final int parameterNumber = this.getParameterNumber();
        final int correspondParameterNumber = method.getParameterNumber();
        if (parameterNumber < correspondParameterNumber) {
            return 1;
        } else if (parameterNumber > correspondParameterNumber) {
            return -1;
        } else {

            // �����̌^�Ŕ�r�D���������珇�ԂɁD
            final Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
            final Iterator<ParameterInfo> correspondParameterIterator = method.getParameters()
                    .iterator();
            while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                final ParameterInfo parameter = parameterIterator.next();
                final ParameterInfo correspondParameter = correspondParameterIterator.next();
                final String typeName = parameter.getName();
                final String correspondTypeName = correspondParameter.getName();
                final int typeOrder = typeName.compareTo(correspondTypeName);
                if (typeOrder != 0) {
                    return typeOrder;
                }
            }

            return 0;
        }
    }

    /**
     * ���̃��\�b�h���C�����ŗ^����ꂽ�����g���ČĂяo�����Ƃ��ł��邩�ǂ����𔻒肷��D
     * 
     * @param methodName ���\�b�h��
     * @param actualParameterTypes �����̌^�̃��X�g
     * @return �Ăяo����ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean canCalledWith(final String methodName,
            final List<EntityUsageInfo> actualParameters) {

        if ((null == methodName) || (null == actualParameters)) {
            throw new NullPointerException();
        }

        // ���\�b�h�����������Ȃ��ꍇ�͊Y�����Ȃ�
        if (!methodName.equals(this.getMethodName())) {
            return false;
        }

        // �����̐����������Ȃ��ꍇ�͊Y�����Ȃ�
        final List<ParameterInfo> dummyParameters = this.getParameters();
        if (dummyParameters.size() != actualParameters.size()) {
            return false;
        }

        // �����̌^��擪����`�F�b�N�������Ȃ��ꍇ�͊Y�����Ȃ�
        final Iterator<ParameterInfo> dummyParameterIterator = dummyParameters.iterator();
        final Iterator<EntityUsageInfo> actualParameterIterator = actualParameters.iterator();
        NEXT_PARAMETER: while (dummyParameterIterator.hasNext()
                && actualParameterIterator.hasNext()) {
            final ParameterInfo dummyParameter = dummyParameterIterator.next();
            final EntityUsageInfo actualParameter = actualParameterIterator.next();

            // ���������Q�ƌ^�̏ꍇ
            if (actualParameter.getType() instanceof ClassTypeInfo) {

                // �������̌^�̃N���X���擾
                final ClassInfo actualParameterClass = ((ClassTypeInfo) actualParameter
                        .getType()).getReferencedClass();

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
            } else if (actualParameter.getType() instanceof PrimitiveTypeInfo) {

                // PrimitiveTypeInfo#equals ���g���ē������̔���D
                // �������Ȃ��ꍇ�͊Y�����Ȃ�
                if (actualParameter.getType().equals(dummyParameter.getType())) {
                    continue NEXT_PARAMETER;
                }

                return false;

                // ���������z��^�̏ꍇ
            } else if (actualParameter.getType() instanceof ArrayTypeInfo) {

                if (!(dummyParameter.getType() instanceof ArrayTypeInfo)) {
                    return false;
                }

                if (!actualParameter.getType().equals(dummyParameter.getType())) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java����̏ꍇ�́C�������� java.lang.object �ł�OK�ȏ������K�v

                // �������� null �̏ꍇ
            } else if (actualParameter.getType() instanceof NullTypeInfo) {

                // ���������Q�ƌ^�łȂ��ꍇ�͊Y�����Ȃ�
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java����̏ꍇ�́C���������z��^�̏ꍇ�ł�OK�ȏ������K�v

                // �������̌^�������ł��Ȃ������ꍇ
            } else if (actualParameter.getType() instanceof UnknownTypeInfo) {

                // �������̌^���s���ȏꍇ�́C�������̌^�����ł��낤�Ƃ�OK�ɂ��Ă���
                continue NEXT_PARAMETER;

            } else {
                assert false : "Here shouldn't be reached!";
            }
        }

        return true;
    }

    /**
     * ���̃��\�b�h�������ŗ^����ꂽ�I�u�W�F�N�g�i���\�b�h�j�Ɠ��������ǂ����𔻒肷��
     * 
     * @param o ��r�ΏۃI�u�W�F�N�g�i���\�b�h�j
     * @return �������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof MethodInfo)) {
            return false;
        }

        return 0 == this.compareTo((MethodInfo) o) ? true : false;
    }

    /**
     * ���̃��\�b�h�̃n�b�V���R�[�h��Ԃ�
     * 
     * @return ���̃��\�b�h�̃n�b�V���R�[�h
     */
    @Override
    public final int hashCode() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.ownerClass.getFullQualifiedName(Settings.getLanguage()
                .getNamespaceDelimiter()));
        sb.append(this.methodName);

        return sb.toString().hashCode();
    }

    /**
     * ���g���N�X�v���ΏۂƂ��Ă̖��O��Ԃ�
     * 
     * @return ���g���N�X�v���ΏۂƂ��Ă̖��O
     */
    public final String getMeasuredUnitName() {

        final StringBuilder sb = new StringBuilder(this.getMethodName());
        sb.append("#");
        sb.append(this.getOwnerClass().getFullQualifiedName(
                Settings.getLanguage().getNamespaceDelimiter()));
        return sb.toString();
    }

    /**
     * ���̃��\�b�h�̖��O��Ԃ�
     * 
     * @return ���\�b�h��
     */
    public final String getMethodName() {
        return this.methodName;
    }

    /**
     * ���̃��\�b�h���`���Ă���N���X��Ԃ��D
     * 
     * @return ���̃��\�b�h���`���Ă���N���X
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃��\�b�h�̕Ԃ�l�̌^��Ԃ�
     * 
     * @return �Ԃ�l�̌^
     */
    public final TypeInfo getReturnType() {

        if (null == this.returnType) {
            throw new NullPointerException();
        }

        return this.returnType;
    }

    /**
     * ���̃��\�b�h���R���X�g���N�^���ǂ�����Ԃ��D
     * 
     * @return �R���X�g���N�^�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isConstuructor() {
        return this.constructor;
    }

    /**
     * ���̃��\�b�h�̈�����ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param parameter �ǉ��������
     */
    public void addParameter(final ParameterInfo parameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new NullPointerException();
        }

        this.parameters.add(parameter);
    }

    /**
     * ���̃��\�b�h�̈�����ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param parameters �ǉ���������Q
     */
    public void addParameters(final List<ParameterInfo> parameters) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new NullPointerException();
        }

        this.parameters.addAll(parameters);
    }

    /**
     * ���̃��\�b�h�̕Ԃ�l���Z�b�g����D
     * 
     * @param returnType ���̃��\�b�h�̕Ԃ�l
     */
    public void setReturnType(final TypeInfo returnType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * ���̃��\�b�h�̈����̐���Ԃ�
     * 
     * @return ���̃��\�b�h�̈����̐�
     */
    public int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * ���̃��\�b�h�̈����� List ��Ԃ��D
     * 
     * @return ���̃��\�b�h�̈����� List
     */
    public List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * ���\�b�h����уR���X�g���N�^�Ăяo����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param callee �ǉ�����Ăяo����郁�\�b�h
     */
    public void addCallee(final MemberCallInfo memberCall) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.callees.add(memberCall);
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param caller �ǉ�����Ăяo�����\�b�h
     */
    public void addCaller(final MethodInfo caller) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caller) {
            throw new NullPointerException();
        }

        this.callers.add(caller);
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param overridee �ǉ�����I�[�o�[���C�h����Ă��郁�\�b�h
     */
    public void addOverridee(final MethodInfo overridee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overridee) {
            throw new NullPointerException();
        }

        this.overridees.add(overridee);
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param overrider �ǉ�����I�[�o�[���C�h���Ă��郁�\�b�h
     * 
     */
    public void addOverrider(final MethodInfo overrider) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overrider) {
            throw new NullPointerException();
        }

        this.overriders.add(overrider);
    }

    /**
     * ���\�b�h����уR���X�g���N�^�Ăяo���ꗗ��Ԃ�
     */
    public Set<MemberCallInfo> getMemberCalls() {
        return Collections.unmodifiableSet(this.callees);

    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h����уR���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        final SortedSet<MethodInfo> callees = new TreeSet<MethodInfo>();
        for (final MemberCallInfo memberCall : this.getMemberCalls()) {
            callees.add(memberCall.getCallee());
        }
        return Collections.unmodifiableSortedSet(callees);
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getCallers() {
        return Collections.unmodifiableSortedSet(this.callers);
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getOverridees() {
        return Collections.unmodifiableSortedSet(this.overridees);
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getOverriders() {
        return Collections.unmodifiableSortedSet(this.overriders);
    }

    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    private final String methodName;

    /**
     * �������Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private final ClassInfo ownerClass;

    /**
     * �Ԃ�l�̌^��ۑ����邽�߂̕ϐ�
     */
    private TypeInfo returnType;

    /**
     * ���̃��\�b�h���R���X�g���N�^���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean constructor;

    /**
     * �����̃��X�g�̕ۑ����邽�߂̕ϐ�
     */
    protected final List<ParameterInfo> parameters;

    /**
     * ���\�b�h�Ăяo���ꗗ��ۑ����邽�߂̕ϐ�
     */
    protected final Set<MemberCallInfo> callees;

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<MethodInfo> callers;

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<MethodInfo> overridees;

    /**
     * �I�[�o�[���C�h����Ă��郁�\�b�h��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<MethodInfo> overriders;
}
