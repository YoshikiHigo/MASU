package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

public final class UnresolvedFullQualifiedNameReferenceTypeInfo extends UnresolvedReferenceTypeInfo {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedFullQualifiedNameReferenceTypeInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName) {

        super(availableNamespaces, referenceName);
    }
}
