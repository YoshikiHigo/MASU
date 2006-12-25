package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.VisitControlToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import antlr.collections.AST;

/**
 * �r�W�^�[�����ɂǂ̃m�[�h�ɐi�ނׂ�����U������.
 * �\���؏�̉����̗v�f�̓����ɓ��鎞�͂��̂��Ƃ��r���_�[�ɂ��ʒm����.
 * @author kou-tngt
 *
 */
public class AntlrAstVisitStrategy implements AstVisitStrategy<AST>{
    public AntlrAstVisitStrategy(boolean intoClass, boolean intoMethod){
        this.intoClass = intoClass;
        this.intoMethod = intoMethod;
    }
    
    public void guide(AstVisitor<AST> visitor, AST prev, AstToken token) {
        if (needToVisitChild(token)){
            //���̎q�m�[�h�ɓ���K�v������炵��
            
            AST child = prev.getFirstChild();
                
            //�g�[�N���̒��ɓ��邱�Ƃ�ʒm
            visitor.enter();
            
            //�r�W�^�[��U������.
            visitor.visit(child);
            
            //���̃g�[�N���̒�����o�����Ƃ�ʒm
            visitor.exit();
            
//        } else {
//            System.out.println("skip: " + prev.getText());
        }
        
        //����ɗׂ̃m�[�h������ΐ�ɗU������.
        AST sibiling = prev.getNextSibling();
        if (null != sibiling){
            visitor.visit(sibiling);
        }
    }
    
    protected boolean needToVisitChild(AstToken token){
        if (token.isClassDefinition()){
            return intoClass;
        } else if (token.isMethodDefinition()){
            return intoMethod;
        } else if (token.equals(VisitControlToken.SKIP)){
            return false;
        } else if (token.equals(VisitControlToken.UNKNOWN)){
            return false;
        }
        return true;
    }
    
    private final boolean intoClass;
    private final boolean intoMethod;
}
