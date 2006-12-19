package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���������\�b�h�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedMethodCall implements UnresolvedTypeInfo {

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^�C���\�b�h����^���ăI�u�W�F�N�g��������
     * 
     * @param ownerClassType ���\�b�h�Ăяo�������s�����ϐ��̌^
     * @param methodName ���\�b�h��
     */
    public UnresolvedMethodCall(final UnresolvedTypeInfo ownerClassType, final String methodName,
            final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassType) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerClassType = ownerClassType;
        this.methodName = methodName;
        this.constructor = constructor;
        this.parameterTypes = new LinkedList<UnresolvedTypeInfo>();
    }

    /**
     * �����̌^��ǉ�
     * 
     * @param typeInfo
     */
    public void addParameterType(final UnresolvedTypeInfo typeInfo) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeInfo) {
            throw new NullPointerException();
        }

        this.parameterTypes.add(typeInfo);
    }

    /**
     * �����̃��X�g��Ԃ�
     * 
     * @return �����̃��X�g
     */
    public List<UnresolvedTypeInfo> getParameterTypes() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^��Ԃ�
     * 
     * @return ���\�b�h�Ăяo�������s�����ϐ��̌^
     */
    public UnresolvedTypeInfo getOwnerClassType() {
        return this.ownerClassType;
    }

    /**
     * �R���X�g���N�^���ǂ�����Ԃ�
     * 
     * @return �R���X�g���N�^�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isConstructor() {
        return this.constructor;
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
     * ���̃��\�b�h�Ăяo���̕Ԃ�l�̌^��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo���̕Ԃ�l�̌^
     */
    public String getTypeName() {
        return UnresolvedTypeInfo.UNRESOLVED;
    }

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedTypeInfo ownerClassType;

    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    private final String methodName;

    /**
     * ������ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeInfo> parameterTypes;

    /**
     * �Ăяo�����R���X�g���N�^���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean constructor;

}
