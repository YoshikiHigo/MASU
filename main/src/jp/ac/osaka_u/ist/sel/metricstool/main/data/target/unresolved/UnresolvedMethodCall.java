package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���������\�b�h�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedMethodCall implements Comparable<UnresolvedMethodCall> {

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^���C���\�b�h����^���ăI�u�W�F�N�g��������
     * 
     * @param ownerClassName ���\�b�h�Ăяo�������s�����ϐ��̌^��
     * @param methodName ���\�b�h��
     */
    public UnresolvedMethodCall(final String[] ownerClassName, final String methodName,
            final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassName) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerClassName = ownerClassName;
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
     * ���\�b�h�Ăяo�������s�����ϐ��̌^����Ԃ�
     * 
     * @return ���\�b�h�Ăяo�������s�����ϐ��̌^
     */
    public String[] getOwnerClassName() {
        return this.ownerClassName;
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
     * ���\�b�h�Ăяo���̏����֌W���`����
     * 
     * @methodCall ��r�Ώۃ��\�b�h�Ăяo��
     * @return ���\�b�h�Ăяo���̏���
     */
    public int compareTo(final UnresolvedMethodCall methodCall) {

        if (null == methodCall) {
            throw new NullPointerException();
        }

        // ���\�b�h���ł̔�r
        final String methodName = this.getMethodName();
        final String correspondMethodName = methodCall.getMethodName();
        final int methodNameOrder = methodName.compareTo(correspondMethodName);
        if (0 != methodNameOrder) {
            return methodNameOrder;

        } else {

            // �����̐��ł̔�r
            final List<UnresolvedTypeInfo> parameterTypes = this.getParameterTypes();
            final List<UnresolvedTypeInfo> correspondParameterTypes = methodCall
                    .getParameterTypes();
            if (parameterTypes.size() > correspondParameterTypes.size()) {
                return 1;
            } else if (parameterTypes.size() < correspondParameterTypes.size()) {
                return -1;
            } else {

                // �����̌^��O���珇�ԂɌ��āC��r���s��
                final Iterator<UnresolvedTypeInfo> typeIterator = parameterTypes.iterator();
                final Iterator<UnresolvedTypeInfo> correspondTypeIterator = correspondParameterTypes
                        .iterator();
                while (typeIterator.hasNext() && correspondTypeIterator.hasNext()) {
                    final UnresolvedTypeInfo typeInfo = typeIterator.next();
                    final UnresolvedTypeInfo correspondTypeInfo = correspondTypeIterator.next();
                    final String typeInfoName = typeInfo.getName();
                    final String correspondTypeInfoName = correspondTypeInfo.getName();
                    final int stringOrder = typeInfoName.compareTo(correspondTypeInfoName);
                    if (0 != stringOrder) {
                        return stringOrder;
                    }
                }

                // ���\�b�h�Ăяo�����s���Ă���ϐ��̌^�Ŕ�r
                final String[] ownerClassName = this.getOwnerClassName();
                final String[] correspondOwnerClassName = methodCall.getOwnerClassName();
                if (ownerClassName.length > correspondOwnerClassName.length) {
                    return 1;
                } else if (ownerClassName.length < correspondOwnerClassName.length) {
                    return -1;
                } else {
                    for (int i = 0; i < ownerClassName.length; i++) {
                        final int stringOrder = ownerClassName[i]
                                .compareTo(correspondOwnerClassName[i]);
                        if (0 != stringOrder) {
                            return stringOrder;
                        }
                    }

                    return 0;
                }
            }
        }
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
     * ���\�b�h�Ăяo�������s�����ϐ��̌^����ۑ����邽�߂̕ϐ�
     */
    private final String[] ownerClassName;

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
