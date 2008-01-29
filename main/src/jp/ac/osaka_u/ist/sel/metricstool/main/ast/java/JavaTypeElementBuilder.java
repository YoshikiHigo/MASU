package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

/**
 * Java�̎����ɓo�ꂷ��^�v�f���\�z����N���X�D
 * 
 * �e�N���X�� {@link TypeElementBuilder#getTypeUpperBounds()} ���\�b�h���I�[�o�[���C�h���C
 * null���A���Ă����ꍇ��java.lang.Object��Ԃ��悤�Ɋg������D
 * 
 * @author kou-tngt
 *
 */
public class JavaTypeElementBuilder extends TypeElementBuilder{

    public JavaTypeElementBuilder(ExpressionElementManager expressionManager, BuildDataManager buildManager) {
        super(expressionManager, buildManager);
    }

    /**
     * �^�v�f�Ɍ^�̏�����w�肳��Ă���ꍇ�͂��̌^��Ԃ��D
     * ����Ă��Ȃ��ꍇ�́Cjava.lang.Object��\���^��Ԃ��D
     * @return �^�v�f�̌^�̏��
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElementBuilder#getTypeUpperBounds()
     */
    protected UnresolvedTypeInfo getTypeUpperBounds(){
        UnresolvedTypeInfo type = super.getTypeUpperBounds();
        if (null == type){
            return JavaTypeBuilder.JAVA_LANG_OBJECT;
        } else {
            return type;
        }
    }
}