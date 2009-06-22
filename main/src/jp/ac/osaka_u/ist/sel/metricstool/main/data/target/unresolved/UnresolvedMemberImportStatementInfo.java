package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstanceProcessing;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * static�C���|�[�g��\���N���X
 * 
 * @author higo
 *
 */
public class UnresolvedMemberImportStatementInfo extends
        UnresolvedUnitInfo<MemberImportStatementInfo> {

    /**
     * �N���X���Ƃ���ȉ�static�����o�[�S�Ă����p�\���ǂ�����\��boolean��^���ăI�u�W�F�N�g��������.
     * <p>
     * import aaa.bbb.CCC.DDD�G // new UnresolvedMemberImportStatementInfo({"aaa","bbb","CCC","DDD"}, false); <br>
     * import aaa.bbb.CCC.*; // new AvailableNamespace({"aaa","bbb","CCC"},true); <br>
     * </p>
     * 
     * @param namespace ���p�\���O��Ԗ�
     * @param allMembers �S�ẴN���X�����p�\���ǂ���
     */
    public UnresolvedMemberImportStatementInfo(final String[] namespace, final boolean allMembers) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.importName = Arrays.<String> copyOf(namespace, namespace.length);
        this.allMembers = allMembers;
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

        if (!(o instanceof UnresolvedMemberImportStatementInfo)) {
            return false;
        }

        String[] importName = this.getImportName();
        String[] correspondImportName = ((UnresolvedMemberImportStatementInfo) o).getImportName();
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
    public MemberImportStatementInfo resolve(final TargetClassInfo usingClass,
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

        // �ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final String[] fullQualifiedName = this.getFullQualifiedName();
        ClassInfo classInfo = classInfoManager.getClassInfo(fullQualifiedName);
        final Set<Member> accessibleMembers = new TreeSet<Member>();
        if (null == classInfo) {
            classInfo = new ExternalClassInfo(fullQualifiedName);
            classInfoManager.add(classInfo);
        }

        if (this.isAllMembers()) {

            if (classInfo instanceof TargetClassInfo) {
                final SortedSet<TargetFieldInfo> fields = ((TargetClassInfo) classInfo)
                        .getDefinedFields();
                final SortedSet<TargetFieldInfo> staticFields = StaticOrInstanceProcessing
                        .getStaticMembers(fields);
                accessibleMembers.addAll(staticFields);
                final SortedSet<TargetMethodInfo> methods = ((TargetClassInfo) classInfo)
                        .getDefinedMethods();
                final SortedSet<TargetMethodInfo> staticMethods = StaticOrInstanceProcessing
                        .getStaticMembers(methods);
                accessibleMembers.addAll(staticMethods);
            }
        }

        else {

            final String[] importName = this.getImportName();
            final String memberName = importName[importName.length - 1];

            if (classInfo instanceof TargetClassInfo) {
                final SortedSet<TargetFieldInfo> fields = ((TargetClassInfo) classInfo)
                        .getDefinedFields();
                for (TargetFieldInfo field : fields) {
                    if (memberName.equals(field.getName())) {
                        accessibleMembers.add(field);
                    }
                }
                final SortedSet<TargetMethodInfo> methods = ((TargetClassInfo) classInfo)
                        .getDefinedMethods();
                for (TargetMethodInfo method : methods) {
                    if (memberName.equals(method.getMethodName())) {
                        accessibleMembers.add(method);
                    }
                }
            }

            // �O�������o��ǉ����鏈�����K�v
            else {

            }
        }

        this.resolvedInfo = new MemberImportStatementInfo(accessibleMembers, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String[] getImportName() {
        return Arrays.<String> copyOf(this.importName, this.importName.length);
    }

    /**
     * ���O��Ԗ���Ԃ��D
     * 
     * @return ���O��Ԗ�
     */
    public String[] getFullQualifiedName() {

        final String[] importName = this.getImportName();
        if (this.isAllMembers()) {
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
        final String[] namespace = this.getFullQualifiedName();
        return Arrays.hashCode(namespace);
    }

    /**
     * �S�ẴN���X�����p�\���ǂ���
     * 
     * @return ���p�\�ł���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isAllMembers() {
        return this.allMembers;
    }

    /**
     * ���O��Ԗ���\���ϐ�
     */
    private final String[] importName;

    /**
     * �S�ẴN���X�����p�\���ǂ�����\���ϐ�
     */
    private final boolean allMembers;
}
