package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class OperatorExpressionBuilder extends ExpressionBuilder {

    public OperatorExpressionBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildManager) {
        super(expressionManager, buildManager);
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (isTriggerToken(token)) {
            this.buildOperatorElement(((OperatorToken) token), event);
        }
    }

    protected void buildOperatorElement(final OperatorToken token, final AstVisitEvent event) {
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

        if (term > 0 && term == elements.length) {
            //�e���̌^���L�^����z��
            final UnresolvedEntityUsageInfo<? extends EntityUsageInfo>[] termTypes = new UnresolvedEntityUsageInfo<?>[elements.length];

            //�ō��Ӓl�ɂ���
            final ExpressionElement primary = elements[0];
            if (primary instanceof IdentifierElement) {
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
            } else if (primary instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) primary;
                if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                    // �L���X�g������Ƃ����炭�����ɓ��B
                    // TODO UnresolvedReferenceTypeInfo�ɂ��ׂ�
                    termTypes[0] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage();
                } else if (typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                    UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement
                            .getType();
                    termTypes[0] = new UnresolvedArrayTypeReferenceInfo(arrayType);
                } else {

                    termTypes[0] = elements[0].getUsage();
                }
            } else {
                //����ȊO�̏ꍇ�͒��ڌ^���擾����
                termTypes[0] = primary.getUsage();
            }

            //2���ڈȍ~�ɂ���
            for (int i = 1; i < term; i++) {
                if (elements[i] instanceof IdentifierElement) {
                    //���ʎq�Ȃ珟��ɎQ�ƂƂ��ĉ������ĕ����擾����
                    termTypes[i] = ((IdentifierElement) elements[i])
                            .resolveAsReferencedVariable(this.buildDataManager);
                } else if (elements[i] instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) elements[i];
                    if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                        termTypes[i] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage();
                    } else if (typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                        // �����ɓ��B����̂�instanceof type[]�Ƃ�
                        // TODO instanceof�̎g�p������EntityUsage�𐶐������ق�����������
                        UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement
                                .getType();
                        termTypes[i] = new UnresolvedArrayTypeReferenceInfo(arrayType);
                    } else {
                        termTypes[i] = elements[i].getUsage();
                    }
                } else {
                    //����ȊO�Ȃ璼�ڌ^���擾����
                    termTypes[i] = elements[i].getUsage();
                }
            }

            final OPERATOR_TYPE operatorType = token.getOperator();

            if (2 == term && null != operatorType) {
                //�I�y���[�^�[�C���X�^���X���Z�b�g����Ă���2�����Z�q�����O�������Ɍ^���菈�����Ϗ�����
                assert (null != termTypes[0]) : "Illega state: first term type was not decided.";
                assert (null != termTypes[1]) : "Illega state: second term type was not decided.";

                final OPERATOR operator = OPERATOR.getOperator(event.getText());

                assert null != operator : "Illegal state: operator is null";

                final UnresolvedBinominalOperationInfo operation = new UnresolvedBinominalOperationInfo(
                        operator, termTypes[0], termTypes[1]);
                operation.setFromLine(termTypes[0].getFromLine());
                operation.setFromColumn(termTypes[0].getFromColumn());
                operation.setToLine(termTypes[1].getToLine());
                operation.setToColumn(termTypes[1].getToColumn());

                pushElement(UsageElement.getInstance(operation));

            } else {
                //�����Ō^���肷��
                UnresolvedEntityUsageInfo<? extends EntityUsageInfo> resultType = null;

                //�I�y���[�^�ɂ���Ă��łɌ��肵�Ă���߂�l�̌^�C�m�肵�Ă��Ȃ����null
                final PrimitiveTypeInfo type = token.getSpecifiedResultType();

                if (null != type) {
                    //�I�y���[�^�ɂ���Ă��łɌ��ʂ̌^�����肵�Ă���

                    resultType = new UnresolvedMonominalOperationInfo(termTypes[0], type);

                    if ((termTypes[0].getFromLine() < event.getStartLine())
                            || (termTypes[0].getFromLine() == event.getStartLine() && termTypes[0]
                                    .getFromColumn() < event.getStartColumn())) {
                        resultType.setFromLine(termTypes[0].getFromLine());
                        resultType.setFromColumn(termTypes[0].getFromColumn());
                        resultType.setToLine(event.getEndLine());
                        resultType.setToColumn(event.getEndColumn());
                    } else {
                        resultType.setFromLine(event.getStartLine());
                        resultType.setFromColumn(event.getStartColumn());
                        resultType.setToLine(termTypes[0].getToLine());
                        resultType.setToColumn(termTypes[0].getToColumn());
                    }
                } else if (token.equals(OperatorToken.ARRAY)) {
                    //�z��L�q�q�̏ꍇ�͓��ʏ���
                    final UnresolvedEntityUsageInfo<? extends EntityUsageInfo> ownerType;
                    if (elements[0] instanceof IdentifierElement) {
                        ownerType = ((IdentifierElement) elements[0])
                                .resolveAsReferencedVariable(this.buildDataManager);
                    } else {
                        ownerType = elements[0].getUsage();
                    }
                    assert null != elements[1] : "Illegal state: expression that show index of array is not found.";
                    resultType = new UnresolvedArrayElementUsageInfo(ownerType, elements[1]
                            .getUsage());
                } else if (token.equals(OperatorToken.CAST) && elements[0] instanceof TypeElement) {
                    final UnresolvedTypeInfo castType = ((TypeElement) elements[0]).getType();
                    final UnresolvedEntityUsageInfo<? extends EntityUsageInfo> castedUsage = elements[1]
                            .getUsage();
                    resultType = new UnresolvedCastUsageInfo(castType, castedUsage);
                } else {
                    //�^����Ɋ֘A���鍀�������珇�Ԃɋ����Ă����čŏ��Ɍ���ł����z�ɏ���Ɍ��߂�
                    for (int i = 0; i < typeSpecifiedTermIndexes.length; i++) {
                        resultType = termTypes[typeSpecifiedTermIndexes[i]];
                        if (null != resultType) {
                            break;
                        }
                    }
                }

                assert (null != resultType) : "Illegal state: operation resultType was not decided.";

                this.pushElement(UsageElement.getInstance(resultType));

                boolean bool;
                int i;
                bool = ((i = 0) == 0);

            }
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isOperator();
    }

}
