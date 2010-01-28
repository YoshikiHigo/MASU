package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���\�b�h��\���N���X
 * 
 * @author higo
 *
 */
public abstract class MethodInfo extends CallableUnitInfo implements MetricMeasurable, Member {

    /**
     * ���\�b�h�I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q��Set
     * @param methodName ���\�b�h��
     * @param ownerClass ��`���Ă���N���X
     * @param privateVisible private����
     * @param namespaceVisible ���O��ԉ���
     * @param inheritanceVisible �q�N���X�������
     * @param publicVisible public����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    MethodInfo(final Set<ModifierInfo> modifiers, final String methodName,
            final ClassInfo ownerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(modifiers, ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodName) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.returnType = null;

        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
    }

    /**
     * ���̃��\�b�h���C�����ŗ^����ꂽ�����g���ČĂяo�����Ƃ��ł��邩�ǂ����𔻒肷��D
     * 
     * @param methodName ���\�b�h��
     * @param actualParameters �������̃��X�g
     * @return �Ăяo����ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean canCalledWith(final String methodName,
            final List<ExpressionInfo> actualParameters) {

        if ((null == methodName) || (null == actualParameters)) {
            throw new IllegalArgumentException();
        }

        // ���\�b�h�����������Ȃ��ꍇ�͊Y�����Ȃ�
        if (!methodName.equals(this.getMethodName())) {
            return false;
        }

        return super.canCalledWith(actualParameters);
    }

    /**
     * ���̃��\�b�h�������ŗ^����ꂽ�I�u�W�F�N�g�i���\�b�h�j�Ɠ��������ǂ����𔻒肷��
     * 
     * @param o ��r�ΏۃI�u�W�F�N�g�i���\�b�h�j
     * @return �������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    @Override
    public final boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof MethodInfo)) {
            return false;
        }

        return 0 == this.compareTo((MethodInfo) o);
    }

    /**
     * ���g���N�X�v���ΏۂƂ��Ă̖��O��Ԃ�
     * 
     * @return ���g���N�X�v���ΏۂƂ��Ă̖��O
     */
    @Override
    public final String getMeasuredUnitName() {

        final StringBuilder sb = new StringBuilder();

        final String fullQualifiedName = this.getOwnerClass().getFullQualifiedName(
                Settings.getInstance().getLanguage().getNamespaceDelimiter());
        sb.append(fullQualifiedName);

        sb.append("#");

        final String methodName = this.getMethodName();
        sb.append(methodName);

        sb.append("(");

        if (this.getParameters().size() > 0) {
            for (final ParameterInfo parameter : this.getParameters()) {
                final TypeInfo parameterType = parameter.getType();
                sb.append(parameterType.getTypeName());
                sb.append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }

    @Override
    public final String getSignatureText() {

        final StringBuilder text = new StringBuilder();

        text.append(this.getReturnType().getTypeName());
        text.append(" ");
        text.append(this.getMethodName());

        text.append("(");
        for (final ParameterInfo parameter : this.getParameters()) {
            text.append(parameter.getType().getTypeName());
            text.append(",");
        }
        if (0 < this.getParameterNumber()) {
            text.deleteCharAt(text.length() - 1);
        }
        text.append(")");

        return text.toString();
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
     * �Ԃ�l�̌^��ۑ����邽�߂̕ϐ�
     */
    private TypeInfo returnType;

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<MethodInfo> overridees;

    /**
     * �I�[�o�[���C�h����Ă��郁�\�b�h��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<MethodInfo> overriders;
}
