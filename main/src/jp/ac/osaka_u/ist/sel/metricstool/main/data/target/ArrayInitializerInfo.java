package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * �z��̏�������\���N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public final class ArrayInitializerInfo extends ExpressionInfo {

    public ArrayInitializerInfo(List<ExpressionInfo> elements, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == elements) {
            throw new IllegalArgumentException("elements is null");
        }
        this.elements = Collections.unmodifiableList(elements);

        for (final ExpressionInfo element : this.elements) {
            element.setOwnerExecutableElement(this);
        }
    }

    public List<ExpressionInfo> getElements() {
        return elements;
    }

    public final int getArrayLength() {
        return this.elements.size();
    }

    @Override
    public String getText() {
        final StringBuilder text = new StringBuilder();
        text.append("{");

        final Iterator<ExpressionInfo> elements = this.elements.iterator();
        if (elements.hasNext()) {
            text.append(elements.next().getText());
        }
        while (elements.hasNext()) {
            text.append(", ");
            text.append(elements.next().getText());
        }

        text.append("}");
        return text.toString();
    }

    @Override
    public TypeInfo getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usages = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        for (final ExpressionInfo element : this.getElements()) {
            usages.addAll(element.getVariableUsages());
        }
        return Collections.unmodifiableSet(usages);
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        for (final ExpressionInfo element : this.getElements()) {
            calls.addAll(element.getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    private final List<ExpressionInfo> elements;

}
