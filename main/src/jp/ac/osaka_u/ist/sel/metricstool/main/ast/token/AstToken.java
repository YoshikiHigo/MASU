package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * AST�m�[�h�̎�ނ�\������N���X.
 * MASU��AST��͕��Ŕėp�I�ɗ��p�����.
 * 
 * @author kou-tngt
 *
 */
public interface AstToken {

    /**
     * �g�[�N�����A�N�Z�X�C���q��\�����ǂ�����Ԃ�.
     * @return �A�N�Z�X�C���q��\���g�[�N���Ȃ�true
     */
    public boolean isAccessModifier();

    /**
     * �g�[�N����������Z�q��\�����ǂ�����Ԃ�.
     * @return ������Z�q��\���g�[�N���Ȃ�true
     */
    public boolean isAssignmentOperator();

    /**
     * �z��L�q�q�i[]�j��\�����ǂ�����Ԃ�.
     * @return �z��L�q�q��\���g�[�N���Ȃ�true
     */
    public boolean isArrayDeclarator();

    /**
     * �g�[�N�����u���b�N��\�����ǂ�����Ԃ�.
     * @return �u���b�N��\���g�[�N���Ȃ�true
     */
    public boolean isBlock();
    
    /**
     * �g�[�N�����u���b�N�̐錾��\�����ǂ�����Ԃ��D
     * @return �u���b�N�̐錾��\���g�[�N���Ȃ�true
     */
    public boolean isBlockDefinition();
    
    /**
     * �g�[�N��������ȃu���b�N��\�����ǂ����Ԃ��D
     * @return ����ȃu���b�N��\���g�[�N���Ȃ�true
     */
    public boolean isBlockName();
    
    /**
     * �g�[�N�����g�ݍ��݌^�ł��邩�ǂ�����Ԃ��D
     * @return �g�ݍ��݌^�Ȃ�true
     */
    public boolean isBuiltinType();

    /**
     * �g�[�N�����N���X��`����\�����ǂ�����Ԃ�.
     * @return �N���X��`����\���g�[�N���Ȃ�true
     */
    public boolean isClassDefinition();

    /**
     * �g�[�N�����N���X�u���b�N��\�����ǂ�����Ԃ�.
     * �N���X�u���b�N�͒ʏ�̃u���b�N�Ƃ͋�ʂ���Ȃ���΂Ȃ�Ȃ�.
     * @return �N���X�u���b�N��\���g�[�N���Ȃ�true
     */
    public boolean isClassBlock();
    
    /**
     * �g�[�N�����萔��\�����ǂ�����Ԃ��D
     * @return �萔��\���g�[�N���Ȃ�true
     */
    public boolean isConstant();
    
    /**
     * �g�[�N�����R���X�g���N�^��`����\�����ǂ�����Ԃ�.
     * @return �R���X�g���N�^��`����\���g�[�N���Ȃ�true
     */
    public boolean isConstructorDefinition();

    /**
     * �g�[�N�����p���L�q����\�����ǂ�����Ԃ�.
     * @return �p���L�q����\���g�[�N���Ȃ�true
     */
    public boolean isInheritanceDescription();

    /**
     * �g�[�N��������\�����ǂ�����Ԃ�.
     * @return ����\���g�[�N���Ȃ�true
     */
    public boolean isExpression();
    
    /**
     * �g�[�N����������\�����ǂ�����Ԃ��D
     * @return ������\���g�[�N���Ȃ�true
     */
    public boolean isExpressionStatement();
    
    /**
     * �g�[�N�������̃��X�g��\�����ǂ����Ԃ�
     * @return ���̃��X�g��\���g�[�N���Ȃ�true
     */
    public boolean isSList();
    
    /**
     * �g�[�N�����t�B�[���h��`����\�����ǂ�����Ԃ�.
     * @return �t�B�[���h��`����\���g�[�N���Ȃ�true
     */
    public boolean isFieldDefinition();

    /**
     * �g�[�N�������ʎq��\�����ǂ�����Ԃ�.
     * @return �t�B�[���h���ʎq��\���g�[�N���Ȃ�true
     */
    public boolean isIdentifier();

    /**
     * �g�[�N����new����\�����ǂ�����Ԃ�.
     * @return new����\���g�[�N���Ȃ�true
     */
    public boolean isInstantiation();

    /**
     * �g�[�N����for����catch�߂Ȃǂ̖`���Œ�`�����ϐ��̒�`����\�����ǂ�����Ԃ�.
     * @return for����catch�߂Ȃǂ̖`���Œ�`�����ϐ��̒�`���ł����true
     */
    public boolean isLocalParameterDefinition();

    /**
     * �g�[�N�������[�J���ϐ���`����\�����ǂ�����Ԃ�.
     * @return ���[�J���ϐ���`���ł����true
     */
    public boolean isLocalVariableDefinition();

    /**
     * �g�[�N�������\�b�h��`����\�����ǂ�����Ԃ�.
     * @return ���\�b�h��`���ł����true
     */
    public boolean isMethodDefinition();

    /**
     * �g�[�N���� static initializer �̒�`����\�����ǂ�����Ԃ��D
     * @return static initializer �̒�`���ł����true
     */
    public boolean isStaticInitializerDefinition();
    
    /**
     * �g�[�N�������\�b�h�Ăяo����\�����ǂ�����Ԃ�.
     * @return ���\�b�h�Ăяo����\���g�[�N���Ȃ�true
     */
    public boolean isMethodCall();

    /**
     * �g�[�N�������\�b�h�p�����[�^��`��\�����ǂ�����Ԃ�.
     * @return ���\�b�h�p�����[�^�̒�`��\���g�[�N���Ȃ�true
     */
    public boolean isMethodParameterDefinition();

    /**
     * �g�[�N�����C���q��\�����ǂ�����Ԃ�.
     * @return �C���q��\���g�[�N���Ȃ�true
     */
    public boolean isModifier();

    /**
     * �g�[�N�����C���q��`����\�����ǂ�����Ԃ�.
     * @return �C���q��`����\���g�[�N���Ȃ�true
     */
    public boolean isModifiersDefinition();

    /**
     * �g�[�N�������炩�̒�`�����̖��O�L�q����\�����ǂ�����Ԃ�.
     * @return ���O�L�q����\���g�[�N���Ȃ�true
     */
    public boolean isNameDescription();

    /**
     * �g�[�N�������O��Ԓ�`����\�����ǂ�����Ԃ�.
     * @return ���O��Ԓ�`����\���g�[�N���Ȃ�true
     */
    public boolean isNameSpaceDefinition();

    /**
     * �g�[�N�������O��Ԃ̎g�p�錾����\�����ǂ�����Ԃ�.
     * @return ���O��Ԃ̎g�p�錾����\���g�[�N���Ȃ�true
     */
    public boolean isNameUsingDefinition();

    /**
     * �g�[�N�������ʎq�̋�؂�Ɏg������̂��ǂ�����Ԃ�.
     * @return ���ʎq�̋�؂�Ɏg����g�[�N���Ȃ�true
     */
    public boolean isNameSeparator();

    /**
     * �g�[�N�������Z�q��\�����ǂ�����Ԃ�.
     * @return ���Z�q��\���g�[�N���Ȃ�true
     */
    public boolean isOperator();

    /**
     * �g�[�N������{�^��\�����ǂ�����Ԃ�.
     * @return ��{�^��\���g�[�N���Ȃ�true
     */
    public boolean isPrimitiveType();

    /**
     * �g�[�N�����^�����L�q����\�����ǂ�����Ԃ��D
     * @return �^�����L�q����\���Ȃ�true
     */
    public boolean isTypeArgument();
    
    /**
     * �g�[�N�����^�����L�q���̗��\�����ǂ�����Ԃ��D
     * @return �^�����L�q���̗��\���Ȃ�true
     */
    public boolean isTypeArguments();
    
    /**
     * �g�[�N�����^�L�q����\�����ǂ�����Ԃ�.
     * @return �^�L�q����\���g�[�N���Ȃ�true
     */
    public boolean isTypeDescription();
    
    /**
     * �g�[�N�����^����̐���L�q���ł��邩�ǂ�����Ԃ��D
     * @return �^����̐���L�q���ł����true
     */
    public boolean isTypeUpperBoundsDescription();
    
    /**
     * �g�[�N�����^�p�����[�^��\�����ǂ�����Ԃ��D
     * @return �^�p�����[�^��\���g�[�N���Ȃ�true
     */
    public boolean isTypeParameterDefinition();

    /**
     * �g�[�N�����^�����̐���L�q���ł��邩�ǂ�����Ԃ��D
     * @return �^�����̐���L�q���ł����true
     */
    public boolean isTypeLowerBoundsDescription();
    
    /**
     * �g�[�N�������C���h�J�[�h�^������\�����ǂ�����Ԃ��D
     * @return ���C���h�J�[�h�^������\���Ȃ�true
     */
    public boolean isTypeWildcard();
    
    /**
     * �g�[�N����void�^��\�����ǂ�����Ԃ�.
     * @return void�^��\���g�[�N���Ȃ�true
     */
    public boolean isVoidType();
    
    /**
     * �g�[�N����if��\�����ǂ�����Ԃ�
     * @return if��\���g�[�N���Ȃ�true
     */
    public boolean isIf();
    
    /**
     * �g�[�N����else��\�����ǂ�����Ԃ�
     * @return else��\���g�[�N���Ȃ�true
     */
    public boolean isElse();
    
    /**
     * �g�[�N����while��\�����ǂ�����Ԃ�
     * @return while��\���g�[�N���Ȃ�true
     */
    public boolean isWhile();
    
    /**
     * �g�[�N����do��\�����ǂ�����Ԃ�
     * @return do��\���g�[�N���Ȃ�true
     */
    public boolean isDo();
    
    /**
     * �g�[�N����for��\�����ǂ�����Ԃ�
     * @return for��\���g�[�N���Ȃ�true
     */
    public boolean isFor();
    
    /**
     * �g�[�N����try��\�����ǂ�����Ԃ�
     * @return try��\���g�[�N���Ȃ�true
     */
    public boolean isTry();
    
    /**
     * �g�[�N����catch��\�����ǂ�����Ԃ�
     * @return catch��\���g�[�N���Ȃ�true
     */
    public boolean isCatch();
    
    /**
     * �g�[�N����finally��\�����ǂ�����Ԃ�
     * @return finally��\���g�[�N���Ȃ�true
     */
    public boolean isFinally();
    
    /**
     * �g�[�N����synchronized��\�����ǂ�����Ԃ�
     * @return synchronized��\���g�[�N���Ȃ�true
     */
    public boolean isSynchronized();
    
    /**
     * �g�[�N����switch��\�����ǂ�����Ԃ�
     * @return switch��\���g�[�N���Ȃ�true
     */
    public boolean isSwitch();
    
    /**
     * ���̃g�[�N����case�O���[�v(case��default�G���g��)�̒�`��\�����ǂ����Ԃ�
     * @return case�O���[�v�̒�`��\���Ȃ�true
     */
    public boolean isCaseGroupDefinition();
    
    /**
     * �g�[�N����Switch���̃G���g���̐錾��\�����ǂ����Ԃ��D
     * @return Switch���̃G���g���̐錾���Ȃ�true
     */
    public boolean isEntryDefinition();
    
    /**
     * �g�[�N����case��\�����ǂ�����Ԃ�
     * @return case��\���g�[�N���Ȃ�true
     */
    public boolean isCase();
    
    /**
     * �g�[�N����default��\�����ǂ�����Ԃ�
     * @return default��\���g�[�N���Ȃ�true
     */
    public boolean isDefault();
    
    /**
     * �g�[�N����break��������킷���ǂ����Ԃ�
     * @return break����\���g�[�N���Ȃ�true
     */
    public boolean isBreak();

    /**
     * �g�[�N����return����\�����ǂ����Ԃ�
     * @return return����\���g�[�N���Ȃ�true
     */
    public boolean isReturn();
    
    /**
     * �g�[�N����throw����\�����ǂ����Ԃ�
     * @return throw����\���g�[�N���Ȃ�true
     */
    public boolean isThrow();
}
