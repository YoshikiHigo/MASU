package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class MethodInfo implements Comparable<MethodInfo>, Resolved {

    /**
     * ���\�b�h�I�u�W�F�N�g������������
     * 
     * @param methodName ���\�b�h��
     * @param �Ԃ�l�̌^
     * @param ���\�b�h���`���Ă���N���X
     * @param �R���X�g���N�^���ǂ���
     */
    public MethodInfo(final String methodName, final TypeInfo returnType,
            final ClassInfo ownerClass, final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodName) || (null == returnType) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.returnType = returnType;
        this.ownerClass = ownerClass;
        this.constructor = constructor;

        this.parameters = new LinkedList<ParameterInfo>();
        this.callees = new TreeSet<MethodInfo>();
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
        ClassInfo ownerClass = this.getOwnerClass();
        ClassInfo correspondOwnerClass = method.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        } else {

            // ���\�b�h���Ŕ�r
            String name = this.getMethodName();
            String correspondName = method.getMethodName();
            final int methodNameOrder = name.compareTo(correspondName);
            if (methodNameOrder != 0) {
                return methodNameOrder;
            } else {

                // �����̌��Ŕ�r
                final int parameterNumber = this.getParameterNumber();
                final int correspondParameterNumber = method.getParameterNumber();
                if (parameterNumber < correspondParameterNumber) {
                    return 1;
                } else if (parameterNumber > correspondParameterNumber) {
                    return -1;
                } else {

                    // �����̌^�Ŕ�r�D���������珇�ԂɁD
                    Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
                    Iterator<ParameterInfo> correspondParameterIterator = method.getParameters()
                            .iterator();
                    while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                        ParameterInfo parameter = parameterIterator.next();
                        ParameterInfo correspondParameter = correspondParameterIterator.next();
                        String typeName = parameter.getName();
                        String correspondTypeName = correspondParameter.getName();
                        final int typeOrder = typeName.compareTo(correspondTypeName);
                        if (typeOrder != 0) {
                            return typeOrder;
                        }
                    }

                    return 0;
                }

            }
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
            final List<TypeInfo> actualParameterTypes) {

        if ((null == methodName) || (null == actualParameterTypes)) {
            throw new NullPointerException();
        }

        // ���\�b�h�����������Ȃ��ꍇ�͊Y�����Ȃ�
        if (!methodName.equals(this.getMethodName())) {
            return false;
        }

        // �����̐����������Ȃ��ꍇ�͊Y�����Ȃ�
        final List<ParameterInfo> dummyParameters = this.getParameters();
        if (dummyParameters.size() != actualParameterTypes.size()) {
            return false;
        }

        // �����̌^��擪����`�F�b�N�������Ȃ��ꍇ�͊Y�����Ȃ�
        final Iterator<ParameterInfo> dummyParameterIterator = dummyParameters.iterator();
        final Iterator<TypeInfo> actualParameterTypeIterator = actualParameterTypes.iterator();
        NEXT_PARAMETER: while (dummyParameterIterator.hasNext()
                && actualParameterTypeIterator.hasNext()) {
            final ParameterInfo dummyParameter = dummyParameterIterator.next();
            final TypeInfo actualParameterType = actualParameterTypeIterator.next();

            // ���������Q�ƌ^�̏ꍇ
            if (actualParameterType instanceof ClassInfo) {

                // ���������Q�ƌ^�łȂ��ꍇ�͊Y�����Ȃ�
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                // �������C���������ɑΏۃN���X�ł���ꍇ�́C���̌p���֌W���l������D�܂�C���������������̃T�u�N���X�łȂ��ꍇ�́C�Ăяo���\�ł͂Ȃ�
                if ((actualParameterType instanceof TargetClassInfo)
                        && (dummyParameter.getType() instanceof TargetClassInfo)) {

                    // ���������������Ɠ����Q�ƌ^�i�N���X�j�ł��Ȃ��C�������̃T�u�N���X�ł��Ȃ��ꍇ�͊Y�����Ȃ�
                    if (actualParameterType.equals(dummyParameter.getType())) {
                        continue NEXT_PARAMETER;

                    } else if (((ClassInfo) actualParameterType)
                            .isSubClass((ClassInfo) dummyParameter.getType())) {
                        continue NEXT_PARAMETER;

                    } else {
                        return false;
                    }

                    // �������C�������̂ǂ��炩�C���邢�͗������O���N���X�ł���ꍇ�́C�p���֌W����Ăяo���\���ǂ����𔻒f���邱�Ƃ��ł��Ȃ�
                    // ���̏ꍇ�́C�S�ČĂяo���\�ł���Ƃ���
                } else {
                    continue NEXT_PARAMETER;
                }

                // ���������v���~�e�B�u�^�̏ꍇ
            } else if (actualParameterType instanceof PrimitiveTypeInfo) {

                // PrimitiveTypeInfo#equals ���g���ē������̔���D
                // �������Ȃ��ꍇ�͊Y�����Ȃ�
                if (actualParameterType.equals(dummyParameter.getType())) {
                    continue NEXT_PARAMETER;
                }else{
                    return false;
                }

                // ���������z��^�̏ꍇ
            } else if (actualParameterType instanceof ArrayTypeInfo) {

                if (!(dummyParameter.getType() instanceof ArrayTypeInfo)) {
                    return false;
                }

                final int actualArrayDimension = ((ArrayTypeInfo) actualParameterType)
                        .getDimension();
                final int dummyArrayDimension = ((ArrayTypeInfo) dummyParameter.getType())
                        .getDimension();
                final TypeInfo actualArrayElementType = ((ArrayTypeInfo) actualParameterType)
                        .getElementType();
                final TypeInfo dummyArrayElementType = ((ArrayTypeInfo) dummyParameter.getType())
                        .getElementType();
                if (actualArrayDimension != dummyArrayDimension) {
                    return false;
                } else if (!actualArrayElementType.equals(dummyArrayElementType)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java����̏ꍇ�́C�������� java.lang.object �ł�OK�ȏ������K�v

                // �������� null �̏ꍇ
            } else if (actualParameterType instanceof NullTypeInfo) {

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
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param callee �ǉ�����Ăяo����郁�\�b�h
     */
    public void addCallee(final MethodInfo callee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == callee) {
            throw new NullPointerException();
        }

        this.callees.add(callee);
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
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        return Collections.unmodifiableSortedSet(this.callees);
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
    private final TypeInfo returnType;

    /**
     * ���̃��\�b�h���R���X�g���N�^���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean constructor;

    /**
     * �����̃��X�g�̕ۑ����邽�߂̕ϐ�
     */
    protected final List<ParameterInfo> parameters;

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<MethodInfo> callees;

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
