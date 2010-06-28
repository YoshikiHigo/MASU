package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

/**
 * PDGParameterNode�ŗp���邽�߂݂̂ɍ쐬�����N���X
 * 
 * @author higo
 * 
 */
public class ParameterDeclarationStatementInfo extends SingleStatementInfo {

	/**
	 * �錾����Ă���p�����[�^�C�ʒu����^���ď�����
	 * 
	 * @param parameter
	 *            �錾����Ă���p�����[�^
	 * @param fromLine
	 *            �J�n�s
	 * @param fromColumn
	 *            �J�n��
	 * @param toLine
	 *            �I���s
	 * @param toColumn
	 *            �I����
	 */
	public ParameterDeclarationStatementInfo(final ParameterInfo parameter,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn) {

		super(parameter.getDefinitionUnit(), fromLine, fromColumn, toLine,
				toColumn);
		this.parameter = parameter;
	}

	@Override
	public String getText() {

		final StringBuilder text = new StringBuilder();
		final TypeInfo type = this.getParameter().getType();
		text.append(type.getTypeName());

		text.append(" ");

		text.append(this.getParameter().getName());

		return text.toString();
	}

	@Override
	public Set<CallInfo<?>> getCalls() {
		return CallInfo.EmptySet;
	}

	@Override
	public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
		final Set<VariableInfo<? extends UnitInfo>> variables = new HashSet<VariableInfo<? extends UnitInfo>>();
		variables.add(this.getParameter());
		return Collections.unmodifiableSet(variables);
	}

	@Override
	public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
		return VariableUsageInfo.EmptySet;
	}

	@Override
	public Set<ReferenceTypeInfo> getThrownExceptions() {
		return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
	}

	public ParameterInfo getParameter() {
		return this.parameter;
	}

	private final ParameterInfo parameter;
}
