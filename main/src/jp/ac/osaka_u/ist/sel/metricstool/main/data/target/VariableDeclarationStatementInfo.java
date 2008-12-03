package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;


/**
 * �ϐ��錾���̏���ۗL����N���X
 * 
 * @author t-miyake
 *
 */
public class VariableDeclarationStatementInfo extends SingleStatementInfo implements ConditionInfo {

    /**
     * �錾����Ă���ϐ��C���������C�ʒu����^���ď�����
     * �錾����Ă���ϐ�������������Ă���ꍇ�C���̃R���X�g���N�^���g�p����
     * 
     * @param variableDeclaration �錾����Ă��郍�[�J���ϐ�
     * @param initializationExpression ��������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public VariableDeclarationStatementInfo(final LocalVariableUsageInfo variableDeclaration,
            final ExpressionInfo initializationExpression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == variableDeclaration) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.variableDeclaration = variableDeclaration;
        this.initializationExpression = initializationExpression;

    }

    @Override
    public int compareTo(ExecutableElement o) {
        int result = super.compareTo(o);

        if (0 == result && o instanceof VariableDeclarationStatementInfo) {
            LocalVariableInfo argVariable = ((VariableDeclarationStatementInfo) o)
                    .getDeclaredLocalVariable();

            return this.getDeclaredLocalVariable().compareTo(argVariable);
        } else {
            return result;
        }

    }

    /**
     * ���̐錾���Ő錾����Ă���ϐ���Ԃ�
     * 
     * @return ���̐錾���Ő錾����Ă���ϐ�
     */
    public final LocalVariableInfo getDeclaredLocalVariable() {
        return this.variableDeclaration.getUsedVariable();
    }

    /**
     * �錾����Ă���ϐ��̏���������Ԃ�
     * 
     * @return �錾����Ă���ϐ��̏��������D����������Ă��ꍇ��null
     */
    public final ExpressionInfo getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * �錾����Ă���ϐ�������������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �錾����Ă���ϐ�������������Ă����true
     */
    public boolean isInitialized() {
        return null != this.initializationExpression;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usages = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        usages.add(this.variableDeclaration);
        if (this.isInitialized()) {
            usages.addAll(this.getInitializationExpression().getVariableUsages());
        }

        return Collections.unmodifiableSet(usages);
    }

    /**
     * ���̕ϐ��錾���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̕ϐ��錾���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final LocalVariableInfo variable = this.getDeclaredLocalVariable();
        final TypeInfo type = variable.getType();
        sb.append(type.getTypeName());

        sb.append(" ");

        sb.append(variable.getName());

        if (this.isInitialized()) {

            sb.append(" = ");
            final ExpressionInfo expression = this.getInitializationExpression();
            sb.append(expression.getText());
        }

        sb.append(";");

        return sb.toString();
    }

    /**
     * �錾����Ă���ϐ���\���t�B�[���h
     */
    private final LocalVariableUsageInfo variableDeclaration;

    /**
     * �錾����Ă���ϐ��̏���������\���t�B�[���h
     */
    private final ExpressionInfo initializationExpression;

}
