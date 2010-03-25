package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

public class CFGUtility {

	/**
	 * ���̃��\�b�h�Ăяo�����C�I�u�W�F�N�g�̏�Ԃ�ύX���Ă��邩��Ԃ��D ���݂̂Ƃ���C�ύX���Ă���͉̂��L�̂����ꂩ�̏����𖞂����Ƃ� 1.
	 * �t�B�[���h�ɑ΂��đ���������s���Ă���D 2. �t�B�[���h�ɒ���t�������\�b�h�Ăяo�����I�u�W�F�N�g�̏�Ԃ�ύX���Ă���D
	 * 
	 * @return�@�ύX���Ă���Ƃ���true, �ύX���Ă��Ȃ��ꍇ��false�D
	 */
	static public boolean stateChange(final MethodInfo method) {

		// �t�B�[���h�ɑ΂��đ�����������邩�ǂ����𒲂ׂ�
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variable instanceof FieldInfo) {
				return true;
			}
		}

		// ���\�b�h�Ăяo���ɂ��āC�I�u�W�F�N�g�̓��e���ω����Ă��邩�𒲂ׂ�
		final Set<MethodInfo> checkedMethods = new HashSet<MethodInfo>();
		checkedMethods.add(method);
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					if (stateChange(methodCall.getCallee(), checkedMethods)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	static private boolean stateChange(final MethodInfo method,
			final Set<MethodInfo> checkedMethods) {

		if (checkedMethods.contains(method)) {
			return false;
		} else {
			checkedMethods.add(method);
		}

		// �t�B�[���h�ɑ΂��đ�����������邩�ǂ����𒲂ׂ�
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variable instanceof FieldInfo) {
				return true;
			}
		}

		// ���\�b�h�Ăяo���ɂ��āC�I�u�W�F�N�g�̓��e���ω����Ă��邩�𒲂ׂ�
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					if (stateChange(methodCall.getCallee(), checkedMethods)) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
