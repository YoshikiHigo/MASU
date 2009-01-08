package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ConstructorCallBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeArgumentElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.UsageElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayInitilizerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class JavaConstructorCallBuilder extends ConstructorCallBuilder {

    public JavaConstructorCallBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
        this.buildDataManager = buildDataManager;
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        super.afterExited(event);

        final AstToken token = event.getToken();
        final int fromLine = event.getStartLine();
        final int fromColumn = event.getStartColumn();
        final int toLine = event.getEndLine();
        final int toColumn = event.getEndColumn();

        if (token.equals(JavaAstToken.CONSTRUCTOR_CALL)) {
            buildInnerConstructorCall(buildDataManager.getCurrentClass(), fromLine, fromColumn,
                    toLine, toColumn);
        } else if (token.equals(JavaAstToken.SUPER_CONSTRUCTOR_CALL)) {
            buildSuperConstructorCall(buildDataManager.getCurrentClass().getSuperClasses()
                    .iterator().next(), fromLine, fromColumn, toLine, toColumn);
        }
    }

    @Override
    protected void buildNewConstructorCall(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        final ConstructorCallElements elements = new ConstructorCallElements(this
                .getAvailableElements());
        assert (elements.getAvailableElements().size() > 0) : "Illegal state: constructor element not found.";

        if (elements.isJavaArrayInstantiation()) {
            //配列のnew文はこっちで処理する

            final UnresolvedArrayTypeInfo arrayType = elements.resolveArrayElement();
            final UnresolvedArrayConstructorCallInfo constructorCall = new UnresolvedArrayConstructorCallInfo(
                    arrayType, fromLine, fromColumn, toLine, toColumn);

            for (final TypeArgumentElement typeArgument : elements.typeArguments) {
                if (typeArgument.getType() instanceof UnresolvedReferenceTypeInfo) {
                    constructorCall.addTypeArgument((UnresolvedReferenceTypeInfo<?>) typeArgument
                            .getType());
                } else {
                    assert typeArgument.getType() instanceof UnresolvedReferenceTypeInfo : "Illegal state: type argument was not reference type.";
                }
            }

            int dimention = 0;
            for (final JavaArrayInstantiationElement instantiationElement : elements
                    .getArrayInstantiationElements()) {
                constructorCall.addIndexExpression(++dimention, instantiationElement
                        .getIndexExpression());
            }

            if (null != elements.getArrayInitilizer()) {
                for (final UnresolvedExpressionInfo<? extends ExpressionInfo> initilizerElement : elements
                        .getArrayInitilizer().getElements()) {
                    constructorCall.addArgument(initilizerElement);
                }
            }

            pushElement(new UsageElement(constructorCall));
        } else {
            //それ以外は普通に処理する
            super.buildNewConstructorCall(fromLine, fromColumn, toLine, toColumn);
        }
    }

    class ConstructorCallElements {

        private final List<ExpressionElement> availableElements;

        private TypeElement typeElement;

        private final List<TypeArgumentElement> typeArguments;

        private final List<ExpressionElement> arguments;

        private UnresolvedArrayInitilizerInfo arrayInitilizer;

        private final List<JavaArrayInstantiationElement> arrayInstaiationElements;

        ConstructorCallElements(final ExpressionElement[] availableElements) {
            if (null == availableElements) {
                throw new IllegalArgumentException();
            }

            this.availableElements = Arrays.asList(availableElements);
            this.typeElement = null;
            this.arrayInitilizer = null;
            this.typeArguments = new LinkedList<TypeArgumentElement>();
            this.arguments = new ArrayList<ExpressionElement>();
            this.arrayInstaiationElements = new ArrayList<JavaArrayInstantiationElement>();

            for (final ExpressionElement element : this.availableElements) {
                if (element instanceof TypeArgumentElement) {
                    this.typeArguments.add((TypeArgumentElement) element);
                } else if (element instanceof TypeElement) {
                    this.typeElement = (TypeElement) element;
                } else if (element instanceof JavaArrayInstantiationElement) {
                    this.arrayInstaiationElements.add((JavaArrayInstantiationElement) element);
                } else if (element.getUsage() instanceof UnresolvedArrayInitilizerInfo) {
                    this.arrayInitilizer = (UnresolvedArrayInitilizerInfo) element.getUsage();
                } else {
                    this.arguments.add(element);
                }
            }

        }

        UnresolvedArrayInitilizerInfo getArrayInitilizer() {
            return arrayInitilizer;
        }

        List<ExpressionElement> getArguments() {
            return this.arguments;
        }

        List<ExpressionElement> getAvailableElements() {
            return this.availableElements;
        }

        int getArrayDimention() {
            return this.arrayInstaiationElements.size();
        }

        List<JavaArrayInstantiationElement> getArrayInstantiationElements() {
            return this.arrayInstaiationElements;
        }

        List<TypeArgumentElement> getTypeArguments() {
            return this.typeArguments;
        }

        TypeElement getTypeElement() {
            return this.typeElement;
        }

        boolean isJavaArrayInstantiation() {
            return this.getArrayDimention() > 0;
        }

        UnresolvedArrayTypeInfo resolveArrayElement() {
            return this.getArrayDimention() > 0 ? UnresolvedArrayTypeInfo.getType(this.typeElement
                    .getType(), this.getArrayDimention()) : null;
        }

    }

    protected void buildInnerConstructorCall(final UnresolvedClassInfo currentClass,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedClassTypeInfo classType = currentClass.getClassType();

        final UnresolvedClassConstructorCallInfo constructorCall = new UnresolvedClassConstructorCallInfo(
                classType, fromLine, fromColumn, toLine, toColumn);

        resolveParameters(constructorCall, Arrays.asList(this.getAvailableElements()));
        pushElement(new UsageElement(constructorCall));
        buildDataManager.addMethodCall(constructorCall);
    }

    protected void buildSuperConstructorCall(final UnresolvedClassTypeInfo superClass,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final ExpressionElement[] elements = getAvailableElements();

        int argStartIndex = 0;

        //String[] superClassReferenceName = superClass.getFullReferenceName();
        final String[] superClassReferenceName = superClass.getReferenceName();
        final String className = superClassReferenceName[superClassReferenceName.length - 1];

        if (elements.length > 0 && elements[0] instanceof TypeElement) {
            //苦し紛れの特別処理
            //elementsの1個目がUnresolvedReferenceTypeInfoでありかつsuperClassのアウタークラスであるなら
            //それはOuterClass.this.super()という呼び出し形式であるとみなす

            final UnresolvedTypeInfo<? extends TypeInfo> type = ((TypeElement) elements[0])
                    .getType();
            if (type instanceof UnresolvedClassTypeInfo) {
                // TODO UnresolvedReferenceTypeにすべきかも 要テスト
                final String[] firstElementReference = ((UnresolvedClassTypeInfo) type)
                        .getReferenceName();
                //.getFullReferenceName();
                if (firstElementReference.length < superClassReferenceName.length) {
                    boolean match = true;
                    for (int i = 0; i < firstElementReference.length; i++) {
                        if (!firstElementReference[i].equals(superClassReferenceName[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        argStartIndex = 1;
                    }
                }
            }
        }

        assert (null != className) : "Illegal state: unexpected ownerClass type.";

        final UnresolvedClassConstructorCallInfo constructorCall = new UnresolvedClassConstructorCallInfo(
                superClass, fromLine, fromColumn, toLine, toColumn);

        List<ExpressionElement> paramters = Arrays.asList(elements);
        resolveParameters(constructorCall, paramters.subList(argStartIndex, paramters.size()));
        pushElement(new UsageElement(constructorCall));
        buildDataManager.addMethodCall(constructorCall);
    }

    /*protected boolean isJavaArrayInstantiation(final ExpressionElement[] elements) {
        for (ExpressionElement element : elements) {
            if (element.equals(JavaArrayInstantiationElement.getInstance())) {
                return true;
            }
        }
        return false;
    }

    protected UnresolvedArrayTypeInfo resolveArrayElement(
            final UnresolvedTypeInfo<? extends TypeInfo> type, final ExpressionElement[] elements) {
        int i = 1;
        int dimension = 0;
        while (i < elements.length
                && elements[i].equals(JavaArrayInstantiationElement.getInstance())) {
            dimension++;
            i++;
        }

        if (dimension > 0) {
            return UnresolvedArrayTypeInfo.getType(type, dimension);
        } else {
            return null;
        }
    }*/

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return super.isTriggerToken(token) || token.equals(JavaAstToken.CONSTRUCTOR_CALL)
                || token.equals(JavaAstToken.SUPER_CONSTRUCTOR_CALL);
    }

    private final BuildDataManager buildDataManager;
}
