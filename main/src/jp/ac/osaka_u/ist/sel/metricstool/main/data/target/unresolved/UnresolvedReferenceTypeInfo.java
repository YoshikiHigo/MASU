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
        this.typeParameterUsages = new LinkedList<UnresolvedTypeParameterUsage>();
    }

    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeParameterUsage �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeParameterUsage(final UnresolvedTypeParameterUsage typeParameterUsage) {

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
    public final List<UnresolvedTypeParameterUsage> getTypeParameterUsages() {
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

    /**
     * ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     * 
     * @return ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     */
    public final String[] getReferenceName() {
        return this.referenceName;
    }

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

    /**
     * ���p�\�Ȗ��O��Ԗ���ۑ����邽�߂̕ϐ��C���O���������̍ۂɗp����
     */
    private final AvailableNamespaceInfoSet availableNamespaceSet;

    /**
     * �Q�Ɩ���ۑ�����ϐ�
     */
    private final String[] referenceName;

    /**
     * �������^�p�����[�^�g�p��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeParameterUsage> typeParameterUsages;
}
