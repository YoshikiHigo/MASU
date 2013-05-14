package sdl.ist.osaka_u.newmasu.cfg;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

public class CFGUtility {

	/**
	 * ���̃��\�b�h���C����t���Ă���I�u�W�F�N�g�̏�Ԃ�ύX���Ă��邩��Ԃ��D ���݂̂Ƃ���C�ύX���Ă���͉̂��L�̂����ꂩ�̏����𖞂����Ƃ�
	 * 1.�t�B�[���h�ɑ΂��đ����s���Ă���D 2. �t�B�[���h�ɒ���t�������\�b�h�Ăяo�����I�u�W�F�N�g�̏�Ԃ�ύX���Ă���D
	 * 
	 * @return�@�ύX���Ă���Ƃ���true, �ύX���Ă��Ȃ��ꍇ��false�D
	 */
	static public boolean stateChange(final MethodInfo method) {

		final ClassInfo ownerClass = method.getOwnerClass();
		final SortedSet<FieldInfo> fields = ownerClass.getDefinedFields();

		// �t�B�[���h�ɑ΂��đ������邩�ǂ����𒲂ׂ�
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variableUsage.isAssignment() && fields.contains(variable)) {
				return true;
			}
		}

		// ���\�b�h�Ăяo���ɂ��āC�I�u�W�F�N�g�̓��e���ω����Ă��邩�𒲂ׂ�
		final Set<CallableUnitInfo> checkedMethods = new HashSet<CallableUnitInfo>();
		checkedMethods.add(method);
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (fields.contains(usedVariable)) {
						final MethodInfo callee = methodCall.getCallee();
						if (stateChange(callee, checkedMethods)) {
							return true;
						}

						for (final MethodInfo overrider : callee
								.getOverriders()) {
							if (stateChange(overrider, checkedMethods)) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * ����̃��\�b�h���C����Ŏw�肳�ꂽ���\�b�h�̈�̏�Ԃ�ύX���Ă��邩��Ԃ��D
	 * 
	 * @param method
	 * @param index
	 * @return
	 */
	static public boolean stateChange(final CallableUnitInfo method,
			final int index) {

		// method ��External�ň��Ȃ��ꍇ�́C��͂�������߂�
		if ((method instanceof ExternalMethodInfo)
				|| (method instanceof ExternalConstructorInfo)) {
			if (0 == method.getParameterNumber()) {
				return false;
			}
		}

		// �w�肳�ꂽ����擾�i�ϒ���ւ̑Ή����܂ށj
		final ParameterInfo parameter;
		if (index < method.getParameters().size()) {
			parameter = method.getParameters().get(index);
		} else {
			parameter = method.getParameters().get(
					method.getParameters().size() - 1);
		}

		// �Q�ƌ^�łȂ��ꍇ�́u��Ԃ�ύX�v�Ƃ����̂͂��肦�Ȃ�
		if (!(parameter.getType() instanceof ClassTypeInfo)) {
			return false;
		}

		// ���\�b�h�Ăяo���ɂ��āC�I�u�W�F�N�g�̓��e���ω����Ă��邩�𒲂ׂ�
		final Set<CallableUnitInfo> checkedMethods = new HashSet<CallableUnitInfo>();
		checkedMethods.add(method);
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {

			// quantifiler �𒲂ׂ�
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (parameter.equals(usedVariable)) {
						final MethodInfo callee = methodCall.getCallee();
						if (stateChange(callee, checkedMethods)) {
							return true;
						}

						for (final MethodInfo overrider : callee
								.getOverriders()) {
							if (stateChange(overrider, checkedMethods)) {
								return true;
							}
						}
					}
				}
			}

			// parameter �𒲂ׂ�
			if (call instanceof MethodCallInfo
					|| call instanceof ClassConstructorCallInfo) {
				final List<ExpressionInfo> arguments = call.getArguments();
				for (int i = 0; i < arguments.size(); i++) {
					if (arguments.get(i) instanceof VariableUsageInfo<?>) {
						final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) arguments
								.get(i)).getUsedVariable();
						if (parameter.equals(usedVariable)) {
							final CallableUnitInfo callee = call.getCallee();
							if (stateChange(callee, i, checkedMethods)) {
								return true;
							}

							if (callee instanceof MethodInfo) {
								for (final MethodInfo overrider : ((MethodInfo) callee)
										.getOverriders()) {
									if (stateChange(overrider, i,
											checkedMethods)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	static private boolean stateChange(final MethodInfo method,
			final Set<CallableUnitInfo> checkedMethods) {

		if (checkedMethods.contains(method)) {
			return false;
		} else {
			checkedMethods.add(method);
		}

		final ClassInfo ownerClass = method.getOwnerClass();
		final SortedSet<FieldInfo> fields = ownerClass.getDefinedFields();

		// �t�B�[���h�ɑ΂��đ������邩�ǂ����𒲂ׂ�
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variableUsage.isAssignment() && fields.contains(variable)) {
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
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (fields.contains(usedVariable)) {
						final MethodInfo callee = methodCall.getCallee();
						if (stateChange(callee, checkedMethods)) {
							return true;
						}

						for (final MethodInfo overrider : callee
								.getOverriders()) {
							if (stateChange(overrider, checkedMethods)) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	static private boolean stateChange(final CallableUnitInfo method,
			final int index, final Set<CallableUnitInfo> checkedMethods) {

		if (checkedMethods.contains(method)) {
			return false;
		} else {
			checkedMethods.add(method);
		}

		// method ��External�ň��Ȃ��ꍇ�́C��͂�������߂�
		if ((method instanceof ExternalMethodInfo)
				|| (method instanceof ExternalConstructorInfo)) {
			if (0 == method.getParameterNumber()) {
				return false;
			}
		}

		// �w�肳�ꂽ����擾�i�ϒ���ւ̑Ή����܂ށj
		final ParameterInfo parameter;
		if (index < method.getParameters().size()) {
			parameter = method.getParameters().get(index);
		} else {
			parameter = method.getParameters().get(
					method.getParameters().size() - 1);
		}

		// �Q�ƌ^�łȂ��ꍇ�́u��Ԃ�ύX�v�Ƃ����̂͂��肦�Ȃ�
		if (!(parameter.getType() instanceof ClassTypeInfo)) {
			return false;
		}

		// ���\�b�h�Ăяo���ɂ��āC�I�u�W�F�N�g�̓��e���ω����Ă��邩�𒲂ׂ�
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {

			// quantifiler �𒲂ׂ�
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (parameter.equals(usedVariable)) {
						if (stateChange(methodCall.getCallee(), checkedMethods)) {
							return true;
						}
					}
				}
			}

			// parameter �𒲂ׂ�
			if (call instanceof MethodCallInfo
					|| call instanceof ClassConstructorCallInfo) {
				final List<ExpressionInfo> arguments = call.getArguments();
				for (int i = 0; i < arguments.size(); i++) {
					if (arguments.get(i) instanceof VariableUsageInfo<?>) {
						final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) arguments
								.get(i)).getUsedVariable();
						if (parameter.equals(usedVariable)) {
							final CallableUnitInfo callee = call.getCallee();
							if (stateChange(callee, i, checkedMethods)) {
								return true;
							}

							if (callee instanceof MethodInfo) {
								for (final MethodInfo overrider : ((MethodInfo) callee)
										.getOverriders()) {
									if (stateChange(overrider, i,
											checkedMethods)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * ��ŗ^����ꂽexpression�����������ׂ����̂ł����true, �����łȂ��ꍇ��false��Ԃ�
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isDissolved(final ExpressionInfo expression) {
		if (expression instanceof VariableUsageInfo<?>) {
			return false;
		} else if (expression instanceof ClassReferenceInfo) {
			return false;
		} else if (expression instanceof ArrayTypeReferenceInfo) {
			return false;
		} else if (expression instanceof EmptyExpressionInfo) {
			return false;
		} else if (expression instanceof LiteralUsageInfo) {
			return false;
		} else if (expression instanceof NullUsageInfo) {
			return false;
		} else if (expression instanceof UnknownEntityUsageInfo) {
			return false;
		} else {
			return true;
		}
	}

	private static final Random NATULAL_VALUE_GENERATOR = new Random();

	// public static int getRandomNaturalValue() {
	// return NATULAL_VALUE_GENERATOR.nextInt(Integer.MAX_VALUE);
	// }
}
