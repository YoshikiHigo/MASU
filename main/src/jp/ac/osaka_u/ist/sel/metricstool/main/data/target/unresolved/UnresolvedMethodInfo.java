package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ��x�ڂ�AST�p�[�X�Ŏ擾�������\�b�h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author y-higo
 * 
 */
public class UnresolvedMethodInfo {

    /**
     * ���������\�b�h��`���I�u�W�F�N�g��������
     */
    public UnresolvedMethodInfo() {

        this.modifier = null;
        this.methodName = null;
        this.returnType = null;
        this.ownerClass = null;
        this.constructor = false;

        this.parameterInfos = new LinkedList<UnresolvedParameterInfo>();
        this.methodCalls = new HashSet<UnresolvedMethodCall>();
        this.fieldReferences = new HashSet<UnresolvedFieldUsage>();
        this.fieldAssignments = new HashSet<UnresolvedFieldUsage>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
    }

    /**
     * ���������\�b�h��`���I�u�W�F�N�g��������
     * 
     * @param modifier �C���q
     * @param methodName ���\�b�h��
     * @param returnType �Ԃ�l�̌^
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     * @param constructor �R���X�g���N�^���ǂ���
     */
    public UnresolvedMethodInfo(final ModifierInfo modifier, final String methodName,
            final UnresolvedTypeInfo returnType, final UnresolvedClassInfo ownerClass,
            final boolean constructor) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == modifier) || (null == methodName) || (null == returnType)
                || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.modifier = modifier;
        this.methodName = methodName;
        this.returnType = returnType;
        this.ownerClass = ownerClass;
        this.constructor = constructor;
        
        this.parameterInfos = new LinkedList<UnresolvedParameterInfo>();
        this.methodCalls = new HashSet<UnresolvedMethodCall>();
        this.fieldReferences = new HashSet<UnresolvedFieldUsage>();
        this.fieldAssignments = new HashSet<UnresolvedFieldUsage>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
    }

    /**
     * �R���X�g���N�^���ǂ�����Ԃ�
     * 
     * @return �R���X�g���N�^�̏ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isConstructor() {
        return this.constructor;
    }

    /**
     * �R���X�g���N�^���ǂ������Z�b�g����
     * 
     * @param constructor �R���X�g���N�^���ǂ���
     */
    public void setConstructor(final boolean constructor) {
        this.constructor = constructor;
    }

    /**
     * �C���q��Ԃ�
     * 
     * @return �C���q
     */
    public ModifierInfo getModifier() {
        return this.modifier;
    }

    /**
     * �C���q���Z�b�g����
     * 
     * @param modifier �C���q
     */
    public void setModifier(final ModifierInfo modifier) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifier = modifier;
    }

    /**
     * ���\�b�h����Ԃ�
     * 
     * @return ���\�b�h��
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * ���\�b�h�����Z�b�g����
     * 
     * @param methodName ���\�b�h��
     */
    public void setMethodName(final String methodName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodName) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
    }

    /**
     * ���\�b�h�̕Ԃ�l�̌^��Ԃ�
     * 
     * @return ���\�b�h�̕Ԃ�l�̌^
     */
    public UnresolvedTypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * ���\�b�h�̕Ԃ�l���Z�b�g����
     * 
     * @param returnType ���\�b�h�̕Ԃ�l
     */
    public void setReturnType(final UnresolvedTypeInfo returnType) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * ���̃��\�b�h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃��\�b�h���`���Ă���N���X
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���\�b�h���`���Ă���N���X���Z�b�g����
     * 
     * @param ownerClass ���\�b�h���`���Ă���N���X
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * ���\�b�h�Ɉ�����ǉ�����
     * 
     * @param parameterInfo �ǉ��������
     */
    public void adParameter(final UnresolvedParameterInfo parameterInfo) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameterInfo) {
            throw new NullPointerException();
        }

        this.parameterInfos.add(parameterInfo);
    }

    /**
     * ���\�b�h�Ăяo����ǉ�����
     * 
     * @param methodCall ���\�b�h�Ăяo��
     */
    public void addMethodCall(final UnresolvedMethodCall methodCall) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodCall) {
            throw new NullPointerException();
        }

        this.methodCalls.add(methodCall);
    }

    /**
     * �t�B�[���h�Q�Ƃ�ǉ�����
     * 
     * @param fieldUsage �t�B�[���h�Q��
     */
    public void addFieldReference(final UnresolvedFieldUsage fieldUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldReferences.add(fieldUsage);
    }

    /**
     * �t�B�[���h�����ǉ�����
     * 
     * @param fieldUsage �t�B�[���h���
     */
    public void addFieldAssignment(final UnresolvedFieldUsage fieldUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldAssignments.add(fieldUsage);
    }

    /**
     * ���[�J���ϐ���ǉ�����
     * 
     * @param localVariable ���[�J���ϐ�
     */
    public void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * ���\�b�h�̈����̃��X�g��Ԃ�
     * 
     * @return ���\�b�h�̈����̃��X�g
     */
    public List<UnresolvedParameterInfo> getParameterInfos() {
        return Collections.unmodifiableList(this.parameterInfos);
    }

    /**
     * ���\�b�h�Ăяo���� Set ��Ԃ�
     * 
     * @return ���\�b�h�Ăяo���� Set
     */
    public Set<UnresolvedMethodCall> getMethodCalls() {
        return Collections.unmodifiableSet(this.methodCalls);
    }

    /**
     * �t�B�[���h�Q�Ƃ� Set ��Ԃ�
     * 
     * @return �t�B�[���h�Q�Ƃ� Set
     */
    public Set<UnresolvedFieldUsage> getFieldReferences() {
        return Collections.unmodifiableSet(this.fieldReferences);
    }

    /**
     * �t�B�[���h����� Set ��Ԃ�
     * 
     * @return �t�B�[���h����� Set
     */
    public Set<UnresolvedFieldUsage> getFieldAssignments() {
        return Collections.unmodifiableSet(this.fieldAssignments);
    }

    /**
     * ��`����Ă��郍�[�J���ϐ��� Set ��Ԃ�
     * 
     * @return ��`����Ă��郍�[�J���ϐ��� Set
     */
    public Set<UnresolvedLocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSet(this.localVariables);
    }

    /**
     * ���̃��\�b�h�̍s����Ԃ�
     * 
     * @return ���\�b�h�̍s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * ���̃��\�b�h�̍s����ۑ�����
     * 
     * @param loc ���̃��\�b�h�̍s��
     */
    public void setLOC(final int loc) {
        this.loc = loc;
    }

    /**
     * �C���q��ۑ�����
     */
    private ModifierInfo modifier;

    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    private String methodName;

    /**
     * ���\�b�h������ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedParameterInfo> parameterInfos;

    /**
     * ���\�b�h�̕Ԃ�l��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedTypeInfo returnType;

    /**
     * ���̃��\�b�h���`���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * �R���X�g���N�^���ǂ�����\���ϐ�
     */
    private boolean constructor;

    /**
     * ���\�b�h�Ăяo����ۑ�����ϐ�
     */
    private final Set<UnresolvedMethodCall> methodCalls;

    /**
     * �t�B�[���h�Q�Ƃ�ۑ�����ϐ�
     */
    private final Set<UnresolvedFieldUsage> fieldReferences;

    /**
     * �t�B�[���h�����ۑ�����ϐ�
     */
    private final Set<UnresolvedFieldUsage> fieldAssignments;

    /**
     * ���̃��\�b�h���Œ�`����Ă��郍�[�J���ϐ���ۑ�����ϐ�
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * ���\�b�h�̍s����ۑ����邽�߂̕ϐ�
     */
    private int loc;
}
