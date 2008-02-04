package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AccessModifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ConstantToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DefinitionToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DescriptionToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.IdentifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.InstanceToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.MemberTypeModifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ModifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.SpecificBlockToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.SyntaxToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.VisitControlToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15TokenTypes;
import antlr.collections.AST;


/**
 * {@link Java15Parser}���琶�������AST�m�[�h�� {@link AstToken}�ɕϊ�����N���X.
 * 
 * @author kou-tngt
 *
 */
public class Java15AntlrAstTranslator implements AstTokenTranslator<AST> {
    
    /**
     *  {@link Java15Parser}���琶�������AST�m�[�h�� {@link AstToken}�ɕϊ�����.
     *  
     *  @param node �ϊ��Ώۂ̃m�[�h
     *  @return �ϊ����ʂ�AstToken
     */
    public AstToken translate(AST node) {
        int type = node.getType();
        AstToken result = null;
        
        //���ʎq�����͖��O���g������p�̃L���b�V�����g��.
        if (type == Java15TokenTypes.IDENT){
            String name = node.getText();
            if (identifierTokenMap.containsKey(name)){
                return identifierTokenMap.get(name);
            } else {
                return new IdentifierToken(node.getText());
            }
        }
        
        //���̌^�̓L���b�V����T���ĕԂ�
        if (tokenMap.containsKey(type)){
            return tokenMap.get(type);
        }
        
        //�L���b�V�����Ȃ��̂ő�����
        switch (type) {
        case Java15TokenTypes.PACKAGE_DEF:
            result = DefinitionToken.NAMESPACE_DEFINITION;
            break;
        case Java15TokenTypes.ANNOTATIONS:
        case Java15TokenTypes.ANNOTATION_DEF:
            result = VisitControlToken.SKIP;
            //�A�m�e�[�V�����֘A�͑S������
            break;
        case Java15TokenTypes.IMPORT:
//        case Java15TokenTypes.STATIC_IMPORT:
            result = JavaAstToken.IMPORT;
            break;
        case Java15TokenTypes.DOT:
            result = SyntaxToken.NAME_SEPARATOR;
            break;
        case Java15TokenTypes.BLOCK:
            result = SyntaxToken.BLOCK_START;
            break;
        case Java15TokenTypes.ARRAY_INIT:
            result = JavaAstToken.ARRAY_INIT;
            break;
        case Java15TokenTypes.OBJBLOCK:
            result = SyntaxToken.CLASSBLOCK_START;
            break;
        case Java15TokenTypes.CLASS_DEF:
            result = DefinitionToken.CLASS_DEFINITION;
            break;
        case Java15TokenTypes.INTERFACE_DEF:
            result = JavaAstToken.INTERFACE_DEFINITION;
            break;
        case Java15TokenTypes.ENUM_DEF://enum�̓N���X�Ƃ��Ĉ���
            result = DefinitionToken.CLASS_DEFINITION;
            break;
        case Java15TokenTypes.ENUM_CONSTANT_DEF:
            result = JavaAstToken.ENUM_CONSTANT;
            break;
        case Java15TokenTypes.FIELD_DEF:
            result = DefinitionToken.FIELD_DEFINITION;
            break;
        case Java15TokenTypes.METHOD_PARAMETER_DEF:
            result = DefinitionToken.METHOD_PARAMETER_DEFINITION;
            break;
        case Java15TokenTypes.METHOD_DEF:
            result = DefinitionToken.METHOD_DEFINITION;
            break;
        case Java15TokenTypes.CTOR_DEF:
            result = DefinitionToken.CONSTRUCTOR_DEFINITION;
            break;
        case Java15TokenTypes.LOCAL_VARIABLE_DEF:
            result = DefinitionToken.LOCALVARIABLE_DEFINITION;
            break;
        case Java15TokenTypes.LOCAL_PARAMETER_DEF:
            result = DefinitionToken.LOCAL_PARAMETER_DEFINITION;
            break;
        case Java15TokenTypes.MODIFIERS:
            result = DescriptionToken.MODIFIER;
            break;
        case Java15TokenTypes.TYPE:
            result = DescriptionToken.TYPE;
            break;
        case Java15TokenTypes.NAME:
            result = DescriptionToken.NAME;
            break;
        case Java15TokenTypes.ARRAY_DECLARATOR:
            result = SyntaxToken.ARRAY;
            break;
        case Java15TokenTypes.ARRAY_INSTANTIATION:
            result = JavaAstToken.ARRAY_INSTANTIATION;
            break;
        case Java15TokenTypes.EXTENDS_CLAUSE:
            result = DescriptionToken.INHERITANCE;
            break;
        case Java15TokenTypes.IMPLEMENTS_CLAUSE:
            result = DescriptionToken.INHERITANCE;
            break;
            
//        case Java15TokenTypes.TYPE_PARAMETERS:
//            result = DefinitionToken.TYPE_PARAMETERS_DEFINITION;
//            break;
            
        case Java15TokenTypes.TYPE_PARAMETER:
            result = DefinitionToken.TYPE_PARAMETER_DEFINITION;
            break;
        
        case Java15TokenTypes.TYPE_UPPER_BOUNDS:
            result = DescriptionToken.TYPE_UPPER_BOUNDS;
            break;
            
        case Java15TokenTypes.TYPE_LOWER_BOUNDS:
            result = DescriptionToken.TYPE_LOWER_BOUNDS;
            break;
            
        case Java15TokenTypes.TYPE_ARGUMENTS:
            result = DescriptionToken.TYPE_ARGUMENTS;
            break;
            
        case Java15TokenTypes.TYPE_ARGUMENT:
            result = DescriptionToken.TYPE_ARGUMENT;
            break;
            
        case Java15TokenTypes.WILDCARD_TYPE:
            result = DescriptionToken.TYPE_WILDCARD;
            break;
            
        case Java15TokenTypes.LITERAL_public:
            result = new AccessModifierToken("public",true,true,true);
            break;
        case Java15TokenTypes.LITERAL_private:
            result = new AccessModifierToken("private",false,false,false);
            break;
        case Java15TokenTypes.LITERAL_protected:
            result = new AccessModifierToken("protected",false,true,true);
            break;
        case Java15TokenTypes.LITERAL_static:
            result = MemberTypeModifierToken.STATIC;
            break;
        case Java15TokenTypes.LITERAL_synchronized:
            result = new ModifierToken("synchronized");
            break;
        case Java15TokenTypes.FINAL:
            result = new ModifierToken("final");
            break;
        case Java15TokenTypes.LITERAL_boolean:
            result = BuiltinTypeToken.BOOLEAN;
            break;
        case Java15TokenTypes.LITERAL_byte:
            result = BuiltinTypeToken.BYTE;
            break;
        case Java15TokenTypes.LITERAL_char:
            result = BuiltinTypeToken.CHAR;
            break;
        case Java15TokenTypes.LITERAL_double:
            result = BuiltinTypeToken.DOUBLE;
            break;
        case Java15TokenTypes.LITERAL_float:
            result = BuiltinTypeToken.FLOAT;
            break;
        case Java15TokenTypes.LITERAL_int:
            result = BuiltinTypeToken.INT;
            break;
        case Java15TokenTypes.LITERAL_long:
            result = BuiltinTypeToken.LONG;
            break;
        case Java15TokenTypes.LITERAL_short:
            result = BuiltinTypeToken.SHORT;
            break;
        case Java15TokenTypes.LITERAL_void:
            result = BuiltinTypeToken.VOID;
            break;
        case Java15TokenTypes.EXPR:
            result = DescriptionToken.EXPRESSION;
            break;
        case Java15TokenTypes.METHOD_CALL:
            result = SyntaxToken.METHOD_CALL;
            break;
        case Java15TokenTypes.CTOR_CALL:
            result = JavaAstToken.CONSTRUCTOR_CALL;
            break;
        case Java15TokenTypes.SUPER_CTOR_CALL:
            result = JavaAstToken.SUPER_CONSTRUCTOR_CALL;
            break;
        case Java15TokenTypes.TYPECAST:
            result = OperatorToken.CAST;
            break;
        case Java15TokenTypes.LITERAL_new:
            result = SyntaxToken.NEW;
            break;
            
        case Java15TokenTypes.INDEX_OP:
            result = OperatorToken.ARRAY;
            break;
            
        case Java15TokenTypes.ASSIGN:
            result = OperatorToken.ASSIGNMENT;
            return result;
            
        case Java15TokenTypes.PLUS_ASSIGN:
        case Java15TokenTypes.MINUS_ASSIGN:
        case Java15TokenTypes.STAR_ASSIGN:
        case Java15TokenTypes.DIV_ASSIGN:
        case Java15TokenTypes.MOD_ASSIGN:
        case Java15TokenTypes.SR_ASSIGN:
        case Java15TokenTypes.BSR_ASSIGN:
        case Java15TokenTypes.SL_ASSIGN:
        case Java15TokenTypes.BAND_ASSIGN:
        case Java15TokenTypes.BXOR_ASSIGN:
        case Java15TokenTypes.BOR_ASSIGN:
            result = OperatorToken.COMPOUND_ASSIGNMENT;
            break;
            
        case Java15TokenTypes.LOR:
        case Java15TokenTypes.LAND:
            result = OperatorToken.LOGICAL_BINOMIAL;
            break;
            
        case Java15TokenTypes.NOT_EQUAL:
        case Java15TokenTypes.EQUAL:
        case Java15TokenTypes.LE:
        case Java15TokenTypes.GE:
        case Java15TokenTypes.LITERAL_instanceof:
        case Java15TokenTypes.LT:
        case Java15TokenTypes.GT:
            result = OperatorToken.COMPARATIVE;
            break;
            
        case Java15TokenTypes.SL:
        case Java15TokenTypes.SR:
        case Java15TokenTypes.BSR:
            result = OperatorToken.SHIFT;
            break;
        
        case Java15TokenTypes.BAND:
        case Java15TokenTypes.BOR:
        case Java15TokenTypes.BXOR:
            result = OperatorToken.BIT_BINOMIAL;
            break;
            
        case Java15TokenTypes.PLUS:
        case Java15TokenTypes.MINUS:
        case Java15TokenTypes.DIV:
        case Java15TokenTypes.MOD:
        case Java15TokenTypes.STAR:
            result = OperatorToken.ARITHMETICH_BINOMIAL;
            break;
            
        case Java15TokenTypes.INC:
        case Java15TokenTypes.DEC:
        case Java15TokenTypes.POST_INC:
        case Java15TokenTypes.POST_DEC:
            result = OperatorToken.INCL_AND_DECL;
            break;
            
        case Java15TokenTypes.LNOT:
            result = OperatorToken.LOGICAL_UNARY;
            break;
            
        case Java15TokenTypes.BNOT:
            result = OperatorToken.BIT_UNARY;
            break;
            
        case Java15TokenTypes.UNARY_MINUS:
        case Java15TokenTypes.UNARY_PLUS:
            result = OperatorToken.ARITHMETHIC_UNARY;
            break;
            
        case Java15TokenTypes.QUESTION:
            result = OperatorToken.TERNARY;
            break;
            
        case Java15TokenTypes.LITERAL_true:
            result = new ConstantToken("true",PrimitiveTypeInfo.BOOLEAN);
            break;
        case Java15TokenTypes.LITERAL_false:
            result = new ConstantToken("false",PrimitiveTypeInfo.BOOLEAN);
            break;
            
        case Java15TokenTypes.LITERAL_null:
            result = InstanceToken.NULL;
            break;
        case Java15TokenTypes.LITERAL_this:
            result = InstanceToken.THIS;
            break;
        case Java15TokenTypes.LITERAL_super:
            result = JavaAstToken.SUPER;
            break;
        case Java15TokenTypes.LITERAL_class:
            result = JavaAstToken.CLASS;
            break;
            
        case Java15TokenTypes.NUM_INT:
            result = new ConstantToken("int",PrimitiveTypeInfo.INT);
            break;
        case Java15TokenTypes.CHAR_LITERAL:
            result = new ConstantToken("char",PrimitiveTypeInfo.CHAR);
            break;
        case Java15TokenTypes.STRING_LITERAL:
            result = new ConstantToken("String", PrimitiveTypeInfo.STRING);
            break;
        case Java15TokenTypes.NUM_FLOAT:
            result = new ConstantToken("float",PrimitiveTypeInfo.FLOAT);
            break;
        case Java15TokenTypes.NUM_LONG:
            result = new ConstantToken("logn",PrimitiveTypeInfo.LONG);
            break;
        case Java15TokenTypes. NUM_DOUBLE:
            result = new ConstantToken("double",PrimitiveTypeInfo.DOUBLE);
            break;
            
        case Java15TokenTypes.STATIC_INIT:
        case Java15TokenTypes.INSTANCE_INIT:
        case Java15TokenTypes.ANNOTATION:
        case Java15TokenTypes.LITERAL_throws:
            result = VisitControlToken.SKIP;
            break;
            
//        case Java15TokenTypes.PARAMETERS:
//        case Java15TokenTypes.ELIST:
//        case Java15TokenTypes.SLIST:
        case Java15TokenTypes.LITERAL_if:
       		result = SpecificBlockToken.IF_BLOCK;
        	break;
        case Java15TokenTypes.LITERAL_else:
        	result = SpecificBlockToken.ELSE_BLOCK;
        	break;
        case Java15TokenTypes.LITERAL_for:
        	result = SpecificBlockToken.FOR_BLOCK;
        	break;
        case Java15TokenTypes.LITERAL_try:
        	result = SpecificBlockToken.TRY_BLOCK;
        	break;
        case Java15TokenTypes.LITERAL_catch:
        	result = SpecificBlockToken.CATCH_BLOCK;
        	break;
        case Java15TokenTypes.LITERAL_finally:
        	result = SpecificBlockToken.FINALLY_BLOCK;
        	break;
//        case Java15TokenTypes.LITERAL_return:
        case Java15TokenTypes.LITERAL_do:
        	result = SpecificBlockToken.DO_BLOCK;
        	break;
        case Java15TokenTypes.LITERAL_while:
        	result = SpecificBlockToken.WHILE_BLOCK;
            break;
        case Java15TokenTypes.LITERAL_switch:
        	result = SpecificBlockToken.SWITCH_BLOCK;
        	break;
        case Java15TokenTypes.LITERAL_case:
        	result = SpecificBlockToken.CASE_ENTRY;
        	break;
        case Java15TokenTypes.LITERAL_default:
        	result = SpecificBlockToken.DEFAULT_ENTRY;
        	break;
        default :
            //�ϊ��ł��Ȃ������m�[�h�͎�肠�������̎q���ɐi��
            result = VisitControlToken.ENTER;
            break;
        }
        
        tokenMap.put(type, result);
        
        return result;
    }
    
    /**
     * �g�[�N���̃L���b�V��
     */
    private final Map<Integer,AstToken> tokenMap = new HashMap<Integer, AstToken>();
    
    /**
     * ���ʎq�g�[�N���̃L���b�V��
     */
    private final Map<String,AstToken> identifierTokenMap = new HashMap<String, AstToken>();
}
