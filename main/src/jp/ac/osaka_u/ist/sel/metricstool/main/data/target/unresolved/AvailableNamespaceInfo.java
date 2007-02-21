package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�̍ہC�Q�ƌ^�ϐ��̗��p�\�Ȗ��O��Ԗ��C�܂��͊��S���薼��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class AvailableNamespaceInfo {

    /**
     * ���p�\���O��Ԗ��Ƃ���ȉ��̃N���X�S�ẴN���X�����p�\���ǂ�����\��boolean��^���ăI�u�W�F�N�g��������.
     * <p>
     * import aaa.bbb.ccc.DDD�G // new AvailableNamespace({"aaa","bbb","ccc","DDD"}, false); <br>
     * import aaa.bbb.ccc.*; // new AvailableNamespace({"aaa","bbb","ccc"},true); <br>
     * </p>
     * 
     * @param namespace ���p�\���O��Ԗ�
     * @param allClasses �S�ẴN���X�����p�\���ǂ���
     */
    public AvailableNamespaceInfo(final String[] namespace, final boolean allClasses) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.importName = namespace;
        this.allClasses = allClasses;
    }

    /**
     * �ΏۃI�u�W�F�N�g�Ɠ��������ǂ�����Ԃ�
     * 
     * @param o �ΏۃI�u�W�F�N�g
     * @return �������ꍇ true�C�����łȂ��ꍇ false
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof AvailableNamespaceInfo)) {
            return false;
        }

        String[] importName = this.getImportName();
        String[] correspondImportName = ((AvailableNamespaceInfo) o).getImportName();
        if (importName.length != correspondImportName.length) {
            return false;
        }

        for (int i = 0; i < importName.length; i++) {
            if (!importName[i].equals(correspondImportName[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String[] getImportName() {
        return this.importName;
    }

    /**
     * ���O��Ԗ���Ԃ��D
     * 
     * @return ���O��Ԗ�
     */
    public String[] getNamespace() {

        final String[] importName = this.getImportName();
        if (this.isAllClasses()) {
            return importName;
        } else {
            final String[] namespace = new String[importName.length - 1];
            System.arraycopy(importName, 0, namespace, 0, importName.length - 1);
            return namespace;
        }
    }

    /**
     * ���̃I�u�W�F�N�g�̃n�b�V���R�[�h��Ԃ�
     * 
     * @return ���̃I�u�W�F�N�g�̃n�b�V���R�[�h
     */
    @Override
    public int hashCode() {

        int hash = 0;
        String[] namespace = this.getNamespace();
        for (int i = 0; i < namespace.length; i++) {
            hash += namespace.hashCode();
        }

        return hash;
    }

    /**
     * �S�ẴN���X�����p�\���ǂ���
     * 
     * @return ���p�\�ł���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isAllClasses() {
        return this.allClasses;
    }

    /**
     * ���O��Ԗ���\���ϐ�
     */
    private final String[] importName;

    /**
     * �S�ẴN���X�����p�\���ǂ�����\���ϐ�
     */
    private final boolean allClasses;
}
