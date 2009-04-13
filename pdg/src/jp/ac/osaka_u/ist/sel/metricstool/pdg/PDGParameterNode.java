package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * ������\��PDG�m�[�h
 * 
 * @author higo
 *
 */
public class PDGParameterNode extends PDGNormalNode<ParameterDeclarationStatementInfo> {

    /**
     * �����̃I�u�W�F�N�g��^���ď�����
     * 
     * @param parameter
     */
    public PDGParameterNode(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        this.core = new ParameterDeclarationStatementInfo(parameter, parameter.getFromLine(),
                parameter.getFromColumn(), parameter.getToLine(), parameter.getToColumn());

        this.text = parameter.getType().getTypeName() + " " + parameter.getName();
    }

    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this.getCore().getParameter());
        return definedVariables;
    }

    @Override
    public Set<VariableInfo<?>> getReferencedVariables() {
        return new HashSet<VariableInfo<? extends UnitInfo>>();
    }
}
