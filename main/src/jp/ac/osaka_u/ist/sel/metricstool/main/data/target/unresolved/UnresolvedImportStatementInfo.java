package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�̍ہC�Q�ƌ^�ϐ��̗��p�\�Ȗ��O��Ԗ��C�܂��͊��S���薼��\���N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedImportStatementInfo extends UnresolvedUnitInfo<ImportStatementInfo> {

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
    public UnresolvedImportStatementInfo(final String[] namespace, final boolean allClasses) {

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

        if (!(o instanceof UnresolvedImportStatementInfo)) {
            return false;
        }

        String[] importName = this.getImportName();
        String[] correspondImportName = ((UnresolvedImportStatementInfo) o).getImportName();
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

    @Override
    public ImportStatementInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final SortedSet<ClassInfo> accessibleClasses = new TreeSet<ClassInfo>();
        if (this.isAllClasses()) {
            final String[] namespace = this.getNamespace();
            final Collection<ClassInfo> specifiedClasses = classInfoManager
                    .getClassInfos(namespace);
            accessibleClasses.addAll(specifiedClasses);
        } else {
            final String[] importName = this.getImportName();
            ClassInfo specifiedClass = classInfoManager.getClassInfo(importName);
            if (null == specifiedClass) {
                specifiedClass = new ExternalClassInfo(importName);
                accessibleClasses.add(specifiedClass);
            }
        }

        this.resolvedInfo = new ImportStatementInfo(fromLine, fromColumn, toLine, toColumn,
                accessibleClasses);
        return this.resolvedInfo;
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
        }

        final String[] namespace = new String[importName.length - 1];
        System.arraycopy(importName, 0, namespace, 0, importName.length - 1);
        return namespace;
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
