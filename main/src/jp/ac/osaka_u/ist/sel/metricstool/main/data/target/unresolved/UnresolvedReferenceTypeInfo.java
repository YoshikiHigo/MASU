package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������Q�ƌ^��\���N���X
 * 
 * @author y-higo
 * 
 */
public class UnresolvedReferenceTypeInfo implements UnresolvedTypeInfo {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedReferenceTypeInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaceSet = availableNamespaces;
        this.referenceName = referenceName;
        //this.fullReferenceName = referenceName;
        //this.ownerType = null;
        this.typeParameterUsages = new LinkedList<UnresolvedReferenceTypeInfo>();
    }
    
    /**
     * ���p�\�Ȗ��O��ԁC�^�̊��S�C������^���ď�����
     * @param referenceName �^�̊��S�C����
     */
    public UnresolvedReferenceTypeInfo(final String[] referenceName) {
    	this(new AvailableNamespaceInfoSet(), referenceName);
    }

    ///**
    // * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
    // * 
    // * @param availableNamespaces ���O��Ԗ�
    // * @param referenceName �Q�Ɩ�
    // */
    /*
    public UnresolvedReferenceTypeInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName, final UnresolvedReferenceTypeInfo ownerType) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName) || (null == ownerType)) {
            throw new NullPointerException();
        }

        this.availableNamespaceSet = availableNamespaces;
        String[] ownerReferenceName = ownerType.getFullReferenceName();
        String[] fullReferenceName = new String[referenceName.length+ownerReferenceName.length];
        System.arraycopy(ownerReferenceName, 0, fullReferenceName, 0, ownerReferenceName.length);
        System.arraycopy(referenceName, 0, fullReferenceName, ownerReferenceName.length, referenceName.length);
        this.fullReferenceName = fullReferenceName;
        this.referenceName = referenceName;
        this.ownerType = ownerType;
        this.typeParameterUsages = new LinkedList<UnresolvedReferenceTypeInfo>();
    }
*/
    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeParameterUsage �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeArgument(final UnresolvedReferenceTypeInfo typeParameterUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeParameterUsages.add(typeParameterUsage);
    }

    /**
     * ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List ��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List
     */
    public final List<UnresolvedReferenceTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeParameterUsages);
    }

    /**
     * ���̎Q�ƌ^�̖��O��Ԃ�
     * 
     * @return ���̎Q�ƌ^�̖��O��Ԃ�
     */
    public final String getTypeName() {
        final String[] referenceName = this.getReferenceName();
        return referenceName[referenceName.length - 1];
    }

    ///**
    // * ���̎Q�ƌ^��owner���܂߂��Q�Ɩ���Ԃ�
    // * 
    // * @return ���̎Q�ƌ^��owner���܂߂��Q�Ɩ���Ԃ�
    // */
    /*public final String[] getFullReferenceName() {
        return this.fullReferenceName;
    }*/
    
    /**
     * ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     * 
     * @return ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     */
    public final String[] getReferenceName() {
        return this.referenceName;
    }

    ///**
    // * ���̎Q�ƌ^���������Ă��関�����Q�ƌ^��Ԃ�
    // * 
    // * @return ���̎Q�ƌ^���������Ă��関�����Q�ƌ^
    // */
    /*public final UnresolvedReferenceTypeInfo getOwnerType() {
        return this.ownerType;
    }*/

    ///**
    // * ���̎Q�ƌ^���C���̎Q�ƌ^�ɂ������Ă��邩�ǂ�����Ԃ�
    // * 
    // * @return �������Ă���ꍇ�� true�C�������Ă��Ȃ��ꍇ�� false
    // */
    /*public final boolean hasOwnerReference() {
        return null != this.ownerType;
    }*/

    /**
     * ���̎Q�ƌ^�̎Q�Ɩ��������ŗ^����ꂽ�����Ō������ĕԂ�
     * 
     * @param delimiter �����ɗp���镶��
     * @return ���̎Q�ƌ^�̎Q�Ɩ��������ŗ^����ꂽ�����Ō�������������
     */
    public final String getReferenceName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        final StringBuilder sb = new StringBuilder(this.referenceName[0]);
        for (int i = 1; i < this.referenceName.length; i++) {
            sb.append(delimiter);
            sb.append(this.referenceName[i]);
        }

        return sb.toString();
    }

    /**
     * ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ��Ԃ�
     * 
     * @return ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ
     */
    public final AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaceSet;
    }
    
    public final static UnresolvedReferenceTypeInfo getInstance(UnresolvedClassInfo referencedClass) {
        return new UnresolvedReferenceTypeInfo(referencedClass.getFullQualifiedName());
    }

    /**
     * ���p�\�Ȗ��O��Ԗ���ۑ����邽�߂̕ϐ��C���O���������̍ۂɗp����
     */
    private final AvailableNamespaceInfoSet availableNamespaceSet;

    /**
     * �Q�Ɩ���ۑ�����ϐ�
     */
    private final String[] referenceName;
    
    ///**
    // * owner���܂߂��Q�Ɩ���ۑ�����ϐ�
    // */
    //private final String[] fullReferenceName;

    ///**
    // * ���̎Q�Ƃ��������Ă��関�����Q�ƌ^��ۑ�����ϐ�
    // */
    //private final UnresolvedReferenceTypeInfo ownerType;

    /**
     * �^�����Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedReferenceTypeInfo> typeParameterUsages;
    
}
