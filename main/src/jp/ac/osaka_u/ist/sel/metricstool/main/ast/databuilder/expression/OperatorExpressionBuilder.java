package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedNullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class OperatorExpressionBuilder extends ExpressionBuilder {

    public OperatorExpressionBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildManager) {
        super(expressionManager);
        this.buildDataManager = buildManager;
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (isTriggerToken(token)) {
            this.buildOperatorElement((OperatorToken) token);
        }
    }

    protected void buildOperatorElement(final OperatorToken token) {
        //���Z�q���K�v�Ƃ��鍀�̐�
        final int term = token.getTermCount();
        //���Ӓl�ւ̑�������邩�ǂ���
        final boolean assignmentLeft = token.isAssignmentOperator();
        //���Ӓl�ւ̎Q�Ƃ����邩�ǂ���
        final boolean referenceLeft = token.isLeftTermIsReferencee();
        //�^����Ɋւ�鍀�̃C���f�b�N�X�̔z��
        final int[] typeSpecifiedTermIndexes = token.getTypeSpecifiedTermIndexes();

        final ExpressionElement[] elements = this.getAvailableElements();

        assert (term > 0 && term == elements.length) : "Illegal state: unexpected element count.";
        
        if (term > 0 && term == elements.length){
            //�e���̌^���L�^����z��
            final UnresolvedEntityUsageInfo[] termTypes = new UnresolvedEntityUsageInfo[elements.length];
    
            //�ō��Ӓl�ɂ���
            if (elements[0] instanceof IdentifierElement) {
                //���ʎq�̏ꍇ
                final IdentifierElement leftElement = (IdentifierElement) elements[0];
                if (referenceLeft) {
                    //�Q�ƂȂ��Q�ƕϐ��Ƃ��ĉ������Č��ʂ̌^���擾����
                    termTypes[0] = leftElement.resolveAsReferencedVariable(this.buildDataManager);
                }
    
                if (assignmentLeft) {
                    //����Ȃ�����ϐ��Ƃ��ĉ������Č��ʂ̌^���擾����
                    termTypes[0] = leftElement.resolveAsAssignmetedVariable(this.buildDataManager);
                }
            } else if (elements[0].equals(InstanceSpecificElement.THIS)){
                termTypes[0] = InstanceSpecificElement.getThisInstanceType(buildDataManager);
            } else if (elements[0].equals(InstanceSpecificElement.NULL)){
                termTypes[0] = new UnresolvedNullUsageInfo();
            } else if (elements[0] instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) elements[0];
                if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                    // �L���X�g������Ƃ����炭�����ɓ��B
                    // TODO UnresolvedReferenceTypeInfo�ɂ��ׂ�
                    termTypes[0] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage();
                } else if(typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                    UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement.getType();
                    termTypes[0] = new UnresolvedArrayTypeReferenceInfo(arrayType);
                } else {
                
                    termTypes[0] = elements[0].getUsage();
                }
            }  else {
                //����ȊO�̏ꍇ�͒��ڌ^���擾����
                termTypes[0] = elements[0].getUsage();
            }
    
            //2���ڈȍ~�ɂ���
            for (int i = 1; i < term; i++) {
                if (elements[i] instanceof IdentifierElement) {
                    //���ʎq�Ȃ珟��ɎQ�ƂƂ��ĉ������ĕ����擾����
                    termTypes[i] = ((IdentifierElement) elements[i])
                            .resolveAsReferencedVariable(this.buildDataManager);
                } else if (elements[i].equals(InstanceSpecificElement.THIS)){
                    termTypes[i] = InstanceSpecificElement.getThisInstanceType(buildDataManager);
                } else if (elements[i].equals(InstanceSpecificElement.NULL)){
                    termTypes[i] = new UnresolvedNullUsageInfo();
                } else if (elements[i] instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) elements[i];
                    if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                        termTypes[i] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage();
                    } else if(typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                        // �����ɓ��B����̂�instanceof type[]�Ƃ�
                        // TODO instanceof�̎g�p������EntityUsage�𐶐������ق�����������
                        UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement.getType();
                        termTypes[i] = new UnresolvedArrayTypeReferenceInfo(arrayType);
                    } else {
                        termTypes[i] = elements[i].getUsage();
                    }
                } else {
                    //����ȊO�Ȃ璼�ڌ^���擾����
                    termTypes[i] = elements[i].getUsage();
                }
            }
            
            final OPERATOR operator = token.getOperator();
            
            if (2 == term && null != operator){
                //�I�y���[�^�[�C���X�^���X���Z�b�g����Ă���2�����Z�q�����O�������Ɍ^���菈�����Ϗ�����
                assert(null != termTypes[0]) : "Illega state: first term type was not decided.";
                assert(null != termTypes[1]) : "Illega state: second term type was not decided.";
                
                UnresolvedBinominalOperationInfo operation = new UnresolvedBinominalOperationInfo(operator,termTypes[0],termTypes[1]);
                pushElement(UsageElement.getInstance(operation));
                
            } else{
                //�����Ō^���肷��
                UnresolvedEntityUsageInfo resultType = null;
                
                //�I�y���[�^�ɂ���Ă��łɌ��肵�Ă���߂�l�̌^�C�m�肵�Ă��Ȃ����null
                final PrimitiveTypeInfo type = token.getSpecifiedResultType();
                
                if (null != type) {
                    //�I�y���[�^�ɂ���Ă��łɌ��ʂ̌^�����肵�Ă���
                    resultType = new UnresolvedMonominalOperationInfo(termTypes[0], type);
                } else if (token.equals(OperatorToken.ARRAY)) {
                    //�z��L�q�q�̏ꍇ�͓��ʏ���
                    UnresolvedEntityUsageInfo ownerType;
                    if (elements[0] instanceof IdentifierElement) {
                        ownerType = ((IdentifierElement) elements[0])
                                .resolveAsReferencedVariable(this.buildDataManager);
                    } else {
                        ownerType = elements[0].getUsage();
                    }
                    resultType = new UnresolvedArrayElementUsageInfo(ownerType);
                } else if(token.equals(OperatorToken.CAST) && elements[0] instanceof TypeElement) {
                    UnresolvedTypeInfo castedType = ((TypeElement) elements[0]).getType();
                    resultType = new UnresolvedCastUsageInfo(castedType);
                } else {
                    //�^����Ɋ֘A���鍀�������珇�Ԃɋ����Ă����čŏ��Ɍ���ł����z�ɏ���Ɍ��߂�
                    for (int i = 0; i < typeSpecifiedTermIndexes.length; i++) {
                        resultType = termTypes[typeSpecifiedTermIndexes[i]];
                        if (null != resultType){
                            break;
                        }
                    }
                }

                assert (null != resultType) : "Illegal state: operation resultType was not decided.";
                
                this.pushElement(UsageElement.getInstance(resultType));
            }
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isOperator();
    }

    private final BuildDataManager buildDataManager;
 
}
