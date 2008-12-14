package jp.ac.osaka_u.ist.sdl.scdetector;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class Conversion {

    public static String getNormalizedString(final SingleStatementInfo statement) {

        final StringBuilder sb = new StringBuilder();

        if (statement instanceof AssertStatementInfo) {

            sb.append("assert");

            final ExpressionInfo expression = ((AssertStatementInfo) statement)
                    .getAssertedExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof ExpressionStatementInfo) {

            final ExpressionInfo expression = ((ExpressionStatementInfo) statement).getExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof ReturnStatementInfo) {

            sb.append("return");

            final ExpressionInfo expression = ((ReturnStatementInfo) statement)
                    .getReturnedExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof ThrowStatementInfo) {

            sb.append("throw");

            final ExpressionInfo expression = ((ThrowStatementInfo) statement)
                    .getThrownExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof VariableDeclarationStatementInfo) {

            sb.append("TYPE");

            {
                final LocalVariableInfo variable = ((VariableDeclarationStatementInfo) statement)
                        .getDeclaredLocalVariable();
                final TypeInfo variableType = variable.getType();

                switch (Configuration.INSTANCE.getPV()) {
                case 0: // ���K�����x��0�C�ϐ��������̂܂܎g��
                    sb.append(variable.getName());
                    break;

                case 1: // ���K�����x��1�C�ϐ��͌^���ɐ��K������D�ϐ������قȂ��Ă��Ă��C�^�������ł���΁C�N���[���Ƃ��Č��o����
                    sb.append(variableType.getTypeName());
                    break;

                case 2: // ���K�����x��2�C�S�Ă̕ϐ��𓯈ꎚ��ɐ��K������D
                    sb.append("VARIABLE");
                    break;

                default:
                    assert false : "Here shouldn't be reached!";
                }
            }

            if (((VariableDeclarationStatementInfo) statement).isInitialized()) {

                sb.append("=");
                final ExpressionInfo expression = ((VariableDeclarationStatementInfo) statement)
                        .getInitializationExpression();
                final String expressionString = Conversion.getNormalizedString(expression);
                sb.append(expressionString);
            }
        }

        return sb.toString();
    }

    public static String getNormalizedString(final BlockInfo block) {

        final StringBuilder sb = new StringBuilder();

        if (block instanceof CatchBlockInfo) {

            sb.append("CATCH");

        } else if (block instanceof ConditionalBlockInfo) {

            sb.append("CONDITION");

        } else if (block instanceof ElseBlockInfo) {

            sb.append("ELSE");

        } else if (block instanceof FinallyBlockInfo) {

            sb.append("FINALLY");

        } else if (block instanceof SimpleBlockInfo) {

            sb.append("SIMPLE");

        } else if (block instanceof SynchronizedBlockInfo) {

            sb.append("CYNCHRONIZED");

        } else if (block instanceof TryBlockInfo) {

            sb.append("TRY");
        }

        return sb.toString();
    }

    public static String getNormalizedString(final ExpressionInfo expression) {

        final StringBuilder sb = new StringBuilder();

        if (expression instanceof ArrayElementUsageInfo) {

            final ExpressionInfo ownerExpression = ((ArrayElementUsageInfo) expression)
                    .getOwnerExpression();
            final String ownerExpressionString = Conversion.getNormalizedString(ownerExpression);
            sb.append(ownerExpressionString);

            sb.append("[");
            final ExpressionInfo indexExpression = ((ArrayElementUsageInfo) expression)
                    .getIndexExpression();
            final String indexExpressionString = Conversion.getNormalizedString(indexExpression);
            sb.append(indexExpressionString);
            sb.append("]");

        } else if (expression instanceof ArrayTypeReferenceInfo) {

            sb.append("TYPE[]");

        } else if (expression instanceof BinominalOperationInfo) {

            final ExpressionInfo firstOperand = ((BinominalOperationInfo) expression)
                    .getFirstOperand();
            final String firstOperandString = Conversion.getNormalizedString(firstOperand);
            sb.append(firstOperandString);

            final OPERATOR operator = ((BinominalOperationInfo) expression).getOperator();
            final String operatorString = operator.getToken();
            sb.append(operatorString);

            final ExpressionInfo secondOperand = ((BinominalOperationInfo) expression)
                    .getSecondOperand();
            final String secondOperandString = Conversion.getNormalizedString(secondOperand);
            sb.append(secondOperandString);

        } else if (expression instanceof MethodCallInfo) {

            final MethodInfo method = ((MethodCallInfo) expression).getCallee();

            switch (Configuration.INSTANCE.getPM()) {

            case 0: // ���K�����x��0�C���\�b�h���͂��̂܂܁C���������p����
                sb.append(method.getMethodName());
                sb.append("(");
                for (final ExpressionInfo argument : ((CallInfo) expression).getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 1: // ���K�����x��1�C���\�b�h����Ԃ�l�̌^���ɐ��K������C���������p����
                sb.append(method.getReturnType().getTypeName());
                sb.append("(");
                for (final ExpressionInfo argument : ((CallInfo) expression).getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 2: // ���K�����x��1�C���\�b�h����Ԃ�l�̌^���ɐ��K������C�������͗p���Ȃ�
                sb.append(method.getReturnType().getTypeName());
                break;
            case 3: // ���K�����x��1�C���\�b�h����Ԃ�l�̌^���ɐ��K������C�������͗p���Ȃ�
                sb.append("MEHTOD");
                break;
            default:
                assert false : "Here shouldn't be reached!";
            }

            sb.append("");

        } else if (expression instanceof ConstructorCallInfo) {

            final ConstructorCallInfo constructorCall = ((ConstructorCallInfo) expression);

            switch (Configuration.INSTANCE.getPM()) {

            case 0: // ���K�����x��0�C�R���X�g���N�^���͂��̂܂܁C���������p����
                sb.append("new ");
                sb.append(constructorCall.getType().getTypeName());
                sb.append("(");
                for (final ExpressionInfo argument : ((CallInfo) expression).getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 1: // ���K�����x��1�C�R���X�g���N�^�Ăяo�����^�ɐ��K������(new ���Z�q�����)�C�������͗p����
                sb.append(constructorCall.getType().getTypeName());
                sb.append("(");
                for (final ExpressionInfo argument : ((CallInfo) expression).getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 2: // ���K�����x��1�C�R���X�g���N�^�Ăяo�����^�ɐ��K������inew ���Z�q�����j�C�������͗p���Ȃ�
                sb.append(constructorCall.getType().getTypeName());
                break;
            case 3: // ���K�����x��2�C���\�b�h����Ԃ�l�̌^���ɐ��K������C�������͗p���Ȃ�
                sb.append("CONSTRUCTOR");
                break;
            default:
                assert false : "Here shouldn't be reached!";
            }

        } else if (expression instanceof CastUsageInfo) {

            sb.append("(TYPE)");

            final ExpressionInfo castedExpression = ((CastUsageInfo) expression).getCastedUsage();
            final String castedExpressionString = Conversion.getNormalizedString(castedExpression);
            sb.append(castedExpressionString);

        } else if (expression instanceof ClassReferenceInfo) {

            sb.append("TYPE");

        } else if (expression instanceof LiteralUsageInfo) {

            sb.append("LITERAL");

        } else if (expression instanceof MonominalOperationInfo) {

            final OPERATOR operator = ((MonominalOperationInfo) expression).getOperator();
            sb.append(operator.getToken());

            final ExpressionInfo operand = ((MonominalOperationInfo) expression).getOperand();
            final String operandString = Conversion.getNormalizedString(operand);
            sb.append(operandString);

        } else if (expression instanceof NullUsageInfo) {

            sb.append("NULL");

        } else if (expression instanceof TernaryOperationInfo) {

            final ExpressionInfo conditionExpression = ((TernaryOperationInfo) expression)
                    .getConditionalExpression();
            final String conditionExpressionString = Conversion
                    .getNormalizedString(conditionExpression);
            sb.append(conditionExpressionString);

            sb.append("?");

            final ExpressionInfo trueExpression = ((TernaryOperationInfo) expression)
                    .getTrueExpression();
            String trueExpressionString = Conversion.getNormalizedString(trueExpression);
            sb.append(trueExpressionString);

            sb.append(":");

            final ExpressionInfo falseExpression = ((TernaryOperationInfo) expression)
                    .getTrueExpression();
            String falseExpressionString = Conversion.getNormalizedString(falseExpression);
            sb.append(falseExpressionString);

        } else if (expression instanceof TypeParameterUsageInfo) {

            sb.append("<");

            final ExpressionInfo typeParameterExpression = ((TypeParameterUsageInfo) expression)
                    .getExpression();
            final String typeParameterExpressionString = Conversion
                    .getNormalizedString(typeParameterExpression);
            sb.append(typeParameterExpressionString);

            sb.append(">");

        } else if (expression instanceof UnknownEntityUsageInfo) {

            sb.append("UNKNOWN");

        } else if (expression instanceof VariableUsageInfo) {

            final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) expression)
                    .getUsedVariable();
            final TypeInfo variableType = usedVariable.getType();

            switch (Configuration.INSTANCE.getPV()) {
            case 0: // ���K�����x��0�C�ϐ��������̂܂܎g��
                sb.append(usedVariable.getName());
                break;

            case 1: // ���K�����x��1�C�ϐ��͌^���ɐ��K������D�ϐ������قȂ��Ă��Ă��C�^�������ł���΁C�N���[���Ƃ��Č��o����
                sb.append(variableType.getTypeName());
                break;

            case 2: // ���K�����x��2�C�S�Ă̕ϐ��𓯈ꎚ��ɐ��K������D
                sb.append("VARIABLE");
                break;

            default:
                assert false : "Here shouldn't be reached!";
            }
        }

        return sb.toString();
    }
}
