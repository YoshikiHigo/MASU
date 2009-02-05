package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ������̗v�f���L�q����Ă���ӏ���\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class DescriptionToken extends AstTokenAdapter {

    /**
     * �������̏����߂�\���萔�C���X�^���X
     */
    public static final DescriptionToken CONDITIONAL_CLAUSE = new DescriptionToken("CONDITIONAL_CLAUSE") {
        @Override
        public boolean isConditionalClause() {
            return true;
        }
    };
    
    /**
     * for���̏������߂�\���萔�C���X�^���X
     */
    public static final DescriptionToken FOR_INIT = new DescriptionToken("FOR_INIT") {
        @Override
        public boolean isForInit() {
            return true;
        }
    };
    
    /**
     * for���̌J��Ԃ��߂�\���C���X�^���X
     */
    public static final DescriptionToken FOR_ITERATOR = new DescriptionToken("FOR_ITERATOR") {
        @Override
        public boolean isForIterator() {
            return true;
        };
    };
    
    /**
     * foreach���̐߂�\���C���X�^���X
     */
    public static final DescriptionToken FOR_EACH_CLAUSE = new DescriptionToken("FOR_EACH_CLAUSE") {
        @Override
        public boolean isForEachClause() {
            return true;
        }
    };
    
    /**
     * ���L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken EXPRESSION = new DescriptionToken("EXPRESSION") {
        @Override
        public boolean isExpression() {
            return true;
        }
    };
    
    /**
     * �����L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken EXPRESSION_STATEMENT = new DescriptionToken("EXPRESSION_STATEMENT") {
        @Override
        public boolean isExpressionStatement() {
            return true;
        }
        
        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * ���x���t�����L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken LABELED_STATEMENT = new DescriptionToken("LABELED_STATEMENT") {
        @Override
        public boolean isLabeledStatement() {
            return true;
        }
        
        @Override
        public boolean isStatement() {
            return true;
        }
    };
    
    /**
     * �e�N���X�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken INHERITANCE = new DescriptionToken("INHERITANCE") {
        @Override
        public boolean isInheritanceDescription() {
            return true;
        }
    };

    /**
     * �C���q�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken MODIFIER = new DescriptionToken("MODIFIER") {
        @Override
        public boolean isModifiersDefinition() {
            return true;
        }
    };

    /**
     * ���O�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken NAME = new DescriptionToken("NAME") {
        @Override
        public boolean isNameDescription() {
            return true;
        }
    };

    /**
     * �^�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken TYPE = new DescriptionToken("TYPE") {
        @Override
        public boolean isTypeDescription() {
            return true;
        }
    };

    /**
     * �^�����L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken TYPE_LOWER_BOUNDS = new DescriptionToken(
            "TYPE_LOWER_BOUNDS") {
        @Override
        public boolean isTypeLowerBoundsDescription() {
            return true;
        }
    };

    /**
     * �^����L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken TYPE_UPPER_BOUNDS = new DescriptionToken(
            "TYPE_UPPER_BOUNDS") {
        @Override
        public boolean isTypeUpperBoundsDescription() {
            return true;
        }
    };

    /**
     * �^�����L�q����\���萔�C���X�^���X
     */
    public static final DescriptionToken TYPE_ARGUMENT = new DescriptionToken("TYPE_ARGUMENT") {
        @Override
        public boolean isTypeArgument() {
            return true;
        }
    };

    /**
     * �^�����L�q���̗��\���萔�C���X�^���X
     */
    public static final DescriptionToken TYPE_ARGUMENTS = new DescriptionToken("TYPE_ARGUMENTS") {
        @Override
        public boolean isTypeArguments() {
            return true;
        }
    };

    /**
     * ���C���h�J�[�h�^������\���萔�C���X�^���X
     */
    public static final DescriptionToken TYPE_WILDCARD = new DescriptionToken("TYPE_WILDCARD") {
        @Override
        public boolean isTypeWildcard() {
            return true;
        }
    };

    /**
     * ���O��ԗ��p�錾�L�q����\���萔�C���X�^���X.
     */
    public static final DescriptionToken USING = new DescriptionToken("USING") {
        @Override
        public boolean isNameUsingDefinition() {
            return true;
        }
    };

    public static final DescriptionToken SLIST = new DescriptionToken("SLIST") {
        @Override
        public boolean isSList() {
            return true;
        }
    };
    
    /**
     * �w�肳�ꂽ������ŏ���������R���X�g���N�^.
     * @param text ���̃g�[�N����\��������.
     */
    public DescriptionToken(final String text) {
        super(text);
    }
}
