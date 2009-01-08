package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitilizerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedArrayInitilizerInfo extends UnresolvedExpressionInfo<ArrayInitilizerInfo> {

    public UnresolvedArrayInitilizerInfo() {
        super();

        this.elements = new ArrayList<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
    }

    public UnresolvedArrayInitilizerInfo(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        this();
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    @Override
    public ArrayInitilizerInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final List<ExpressionInfo> elements = new ArrayList<ExpressionInfo>();
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> unresolvedElement : this.elements) {
            elements.add(unresolvedElement.resolve(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager));
        }

        this.resolvedInfo = new ArrayInitilizerInfo(elements, usingMethod, this.getFromLine(), this
                .getFromColumn(), this.getToLine(), this.getToColumn());
        return this.resolvedInfo;
    }

    public void addElement(final UnresolvedExpressionInfo<? extends ExpressionInfo> element) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == element) {
            throw new IllegalArgumentException();
        }

        this.elements.add(element);
    }
    
    public List<UnresolvedExpressionInfo<? extends ExpressionInfo>> getElements() {
        return elements;
    }

    private final List<UnresolvedExpressionInfo<? extends ExpressionInfo>> elements;
}
