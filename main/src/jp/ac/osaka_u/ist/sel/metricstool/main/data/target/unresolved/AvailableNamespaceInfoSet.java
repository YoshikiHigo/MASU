package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���p�\�Ȗ��O��Ԗ��̏W���������N���X�D
 * AvailableNamespaceInfo��v�f�Ƃ��Ď���
 * @author y-higo
 *
 */
public final class AvailableNamespaceInfoSet implements Iterable<AvailableNamespaceInfo> {

    public AvailableNamespaceInfoSet() {
        
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.availableNamespaces = new HashSet<AvailableNamespaceInfo>();
    }

    public void add(final AvailableNamespaceInfo availableNamespace) {
        
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == availableNamespace){
            throw new NullPointerException();
        }
        
        this.availableNamespaces.add(availableNamespace);
    }

    public Iterator<AvailableNamespaceInfo> iterator() {
        Set<AvailableNamespaceInfo> unmodifiableAvailableNamespace = Collections
                .unmodifiableSet(this.availableNamespaces);
        return unmodifiableAvailableNamespace.iterator();
    }

    private final Set<AvailableNamespaceInfo> availableNamespaces;
}
