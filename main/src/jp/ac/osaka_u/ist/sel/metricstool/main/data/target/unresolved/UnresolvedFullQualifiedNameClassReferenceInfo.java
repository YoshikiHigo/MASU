package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

public final class UnresolvedFullQualifiedNameClassReferenceInfo extends UnresolvedClassReferenceInfo {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedFullQualifiedNameClassReferenceInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName) {

        super(availableNamespaces, referenceName);
    }
}
