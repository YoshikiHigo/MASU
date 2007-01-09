package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * AST�m�[�h�̎�ނ�\������g�[�N��.
 * MASU��AST��͕��Ŕėp�I�ɗ��p�����.
 * 
 * @author kou-tngt
 *
 */
public interface AstToken {

    public boolean isAccessModifier();

    public boolean isAssignmentOperator();

    public boolean isArrayDeclarator();

    public boolean isBlock();

    public boolean isClassDefinition();

    public boolean isClassBlock();

    public boolean isConstructorDefinition();

    public boolean isInheritanceDescription();

    public boolean isExpression();

    public boolean isFieldDefinition();

    public boolean isIdentifier();

    public boolean isInstantiation();

    public boolean isLocalParameterDefinition();

    public boolean isLocalVariableDefinition();

    public boolean isMethodDefinition();

    public boolean isMethodCall();

    public boolean isMethodParameterDefinition();

    public boolean isModifier();

    public boolean isModifiersDefinition();

    public boolean isNameDescription();

    public boolean isNameSpaceDefinition();

    public boolean isNameUsingDefinition();

    public boolean isNameSeparator();

    public boolean isOperator();

    public boolean isPrimitiveType();

    public boolean isTypeDescription();

    public boolean isVoidType();
}
