package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
        UnresolvedImportStatementInfo<MemberImportStatementInfo> {

    public static List<UnresolvedMemberImportStatementInfo> getMemberImportStatements(
            final Collection<UnresolvedImportStatementInfo<?>> importStatements) {

        final List<UnresolvedMemberImportStatementInfo> memberImportStatements = new LinkedList<UnresolvedMemberImportStatementInfo>();
        for (final UnresolvedImportStatementInfo<?> importStatement : importStatements) {
            if (importStatement instanceof UnresolvedMemberImportStatementInfo) {
                memberImportStatements.add((UnresolvedMemberImportStatementInfo) importStatement);
            }
        }
        return Collections.unmodifiableList(memberImportStatements);
    }
    
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
        super(namespace, allMembers);
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

        if (this.isAll()) {

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
}
