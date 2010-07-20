package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.SimpleCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGControlEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * ����ˑ��O���t�̃m�[�h��\���N���X
 * 
 * @author t-miyake, higo
 * 
 * @param <T>
 *            �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class CFGNode<T extends ExecutableElementInfo> implements
        Comparable<CFGNode<? extends ExecutableElementInfo>> {

    /**
     * �^����ꂽ�������炽�ǂ邱�Ƃ��ł��钸�_�̂����C�ł��擪�i���\�b�h�̓����j�ɂ��钸�_��Ԃ�
     * 
     * @param node
     * @return
     */
    public static CFGNode<? extends ExecutableElementInfo> getHeadmostNode(
            final CFGNode<? extends ExecutableElementInfo> node) {

        final Set<CFGEdge> edges = node.getBackwardEdges();
        // �o�b�N���[�h�G�b�W���Ȃ��̂ł���΁C���̃m�[�h���ł���ɂ���
        if (0 == edges.size()) {
            return node;
        }

        else {
            final List<CFGNode<? extends ExecutableElementInfo>> headmostNodes = new ArrayList<CFGNode<? extends ExecutableElementInfo>>();
            for (final CFGEdge edge : edges) {
                final CFGNode<? extends ExecutableElementInfo> fromNode = edge.getFromNode();
                final CFGNode<? extends ExecutableElementInfo> headmostNode = getHeadmostNode(fromNode);
                headmostNodes.add(headmostNode);
            }

            // �S�Ă�headmostNode�������������`�F�b�N����
            for (int i = 0; i < headmostNodes.size(); i++) {
                for (int j = i + 1; j < headmostNodes.size(); j++) {
                    if (headmostNodes.get(i) != headmostNodes.get(j)) {
                        throw new IllegalStateException();
                    }
                }
            }

            return headmostNodes.get(0);
        }
    }

    private static final AtomicLong DUMMY_VARIABLE_NUMBER = new AtomicLong();

    /**
     * ���_�𕪉�����Ƃ��ɗp����ϐ��̖��O�𐶐����邽�߂̃��\�b�h
     * @return
     */
    protected static final String getDummyVariableName() {
        final StringBuilder text = new StringBuilder();
        text.append("$");
        text.append(Long.toString(DUMMY_VARIABLE_NUMBER.getAndIncrement()));
        return text.toString();

    }

    /**
     * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
     */
    protected final Set<CFGEdge> forwardEdges;

    /**
     * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
     */
    protected final Set<CFGEdge> backwardEdges;

    /**
     * ���̒��_�̃e�L�X�g�\��
     */
    private final String text;

    /**
     * ���̃m�[�h�ɑΉ����镶
     */
    private final T core;

    /**
     * �j�ƂȂ�v���O�����v�f��^����CFG��������
     * 
     * @param core
     */
    protected CFGNode(final T core) {

        if (null == core) {
            throw new IllegalArgumentException("core is null");
        }
        this.core = core;
        this.forwardEdges = new HashSet<CFGEdge>();
        this.backwardEdges = new HashSet<CFGEdge>();

        final StringBuilder text = new StringBuilder();
        text.append(core.getText());
        text.append(" <");
        text.append(core.getFromLine());
        text.append("> ");
        this.text = text.toString();
    }

    /**
     * �����ŗ^����ꂽ�ӂ����̒��_�̃t�H���[�h�G�b�W�Ƃ��Ēǉ�����
     * 
     * @param forwardEdge
     */
    public void addForwardEdge(final CFGEdge forwardEdge) {

        if (null == forwardEdge) {
            throw new IllegalArgumentException();
        }

        if (!this.equals(forwardEdge.getFromNode())) {
            throw new IllegalArgumentException();
        }

        if (this.forwardEdges.add(forwardEdge)) {
            forwardEdge.getToNode().backwardEdges.add(forwardEdge);
        }
    }

    /**
     * �����ŗ^����ꂽ�ӂ����̒��_�̃o�b�N���[�h�G�b�W�Ƃ��Ēǉ�����
     * 
     * @param backwardEdge
     */
    public void addBackwardEdge(final CFGEdge backwardEdge) {

        if (null == backwardEdge) {
            throw new IllegalArgumentException();
        }

        if (!this.equals(backwardEdge.getToNode())) {
            throw new IllegalArgumentException();
        }

        if (this.backwardEdges.add(backwardEdge)) {
            backwardEdge.getFromNode().forwardEdges.add(backwardEdge);
        }
    }

    /**
     * �����ŗ^����ꂽ�ӂ��t�H���[�h�G�b�W�����菜��
     * 
     * @param forwardEdge
     */
    void removeForwardEdge(final CFGEdge forwardEdge) {

        if (null == forwardEdge) {
            throw new IllegalArgumentException();
        }

        this.forwardEdges.remove(forwardEdge);
    }

    /**
     * �����ŗ^����ꂽ�ӌQ���t�H���[�h�G�b�W�����菜��
     * 
     * @param forwardEdge
     */
    void removeForwardEdges(final Collection<CFGEdge> forwardEdges) {

        if (null == forwardEdges) {
            throw new IllegalArgumentException();
        }

        this.forwardEdges.removeAll(forwardEdges);
    }

    /**
     * �����ŗ^����ꂽ�ӂ��o�b�N���[�h�G�b�W�����菜��
     * 
     * @param backwardEdge
     */
    void removeBackwardEdge(final CFGEdge backwardEdge) {

        if (null == backwardEdge) {
            throw new IllegalArgumentException();
        }

        this.backwardEdges.remove(backwardEdge);
    }

    /**
     * �����ŗ^����ꂽ�ӌQ���o�b�N���[�h�G�b�W�����菜��
     * 
     * @param backwardEdges
     */
    void removeBackwardEdges(final Collection<CFGEdge> backwardEdges) {

        if (null == backwardEdges) {
            throw new IllegalArgumentException();
        }

        this.backwardEdges.removeAll(backwardEdges);
    }

    /**
     * ���̃m�[�h��O�m�[�h�ƌ��m�[�h����H��Ȃ��悤�ɂ���
     */
    void remove() {
        for (final CFGEdge edge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = edge.getFromNode();
            backwardNode.removeForwardEdge(edge);
        }
        for (final CFGEdge edge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = edge.getToNode();
            forwardNode.removeBackwardEdge(edge);
        }
    }

    /**
     * ���̃m�[�h�����݂���ʒu�ɁC�����ŗ^����ꂽ�m�[�h��ǉ����C���̃m�[�h���폜
     * 
     * @param node
     */
    void replace(final CFGNode<?> node) {
        if (null == node) {
            throw new IllegalArgumentException();
        }
        this.remove();
        for (final CFGEdge edge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = edge.getFromNode();
            final CFGEdge newEdge = edge.replaceToNode(node);
            backwardNode.addForwardEdge(newEdge);
        }
        for (final CFGEdge edge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = edge.getToNode();
            final CFGEdge newEdge = edge.replaceFromNode(node);
            forwardNode.addBackwardEdge(newEdge);
        }
    }

    /**
     * ���̃m�[�h�ɑΉ����镶�̏����擾
     * 
     * @return ���̃m�[�h�ɑΉ����镶
     */
    public T getCore() {
        return this.core;
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g���擾
     * 
     * @return ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
        final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            forwardNodes.add(forwardEdge.getToNode());
        }
        return Collections.unmodifiableSet(forwardNodes);
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�G�b�W�̃Z�b�g���擾
     * 
     * @return ���̃m�[�h�̃t�H���[�h�G�b�W�̃Z�b�g
     */
    public Set<CFGEdge> getForwardEdges() {
        return Collections.unmodifiableSet(this.forwardEdges);
    }

    /**
     * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g���擾
     * 
     * @return ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getBackwardNodes() {
        final Set<CFGNode<? extends ExecutableElementInfo>> backwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            backwardNodes.add(backwardEdge.getFromNode());
        }
        return Collections.unmodifiableSet(backwardNodes);
    }

    /**
     * ���̃m�[�h�̃o�b�N���[�h�G�b�W�̃Z�b�g���擾
     * 
     * @return ���̃m�[�h�̃o�b�N���[�h�G�b�W�̃Z�b�g
     */
    public Set<CFGEdge> getBackwardEdges() {
        return Collections.unmodifiableSet(this.backwardEdges);
    }

    @Override
    public int compareTo(final CFGNode<? extends ExecutableElementInfo> node) {

        if (null == node) {
            throw new IllegalArgumentException();
        }

        final int methodOrder = this.getCore().getOwnerMethod().compareTo(
                node.getCore().getOwnerMethod());
        if (0 != methodOrder) {
            return methodOrder;
        }

        return this.getCore().compareTo(node.getCore());
    }

    /**
     * �K�v�̂Ȃ��m�[�h�̏ꍇ�́C���̃��\�b�h���I�[�o�[���C�h���邱�Ƃɂ���āC�폜�����
     * �œK���ɂ��m�[�h���폜�����Ƃ���true,�����łȂ��Ƃ���false��Ԃ�.
     */
    public boolean optimize() {
        return false;
    }

    /**
     * ���̒��_�̃e�L�X�g�\����Ԃ�
     * 
     * @return
     */
    public final String getText() {
        return this.text;
    }

    /**
     * ���̃m�[�h�Œ�`�E�ύX����Ă���ϐ���Set��Ԃ�
     * 
     * @param countObjectStateChange
     *            �Ăяo���ꂽ���\�b�h�Ȃ��ł̃I�u�W�F�N�g�̏�ԕύX
     *            �i�t�B�[���h�ւ̑���Ȃǁj���Q�Ƃ���Ă���ϐ��̕ύX�Ƃ���ꍇ��true�D
     * 
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables(
            final boolean countObjectStateChange) {

        final Set<VariableInfo<? extends UnitInfo>> assignments = new HashSet<VariableInfo<? extends UnitInfo>>();
        assignments.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this
                .getCore().getVariableUsages())));

        // �I�u�W�F�N�g�̏�ԕύX���C�ϐ��̕ύX�Ƃ����ꍇ
        if (countObjectStateChange) {
            for (final CallInfo<? extends CallableUnitInfo> call : this.getCore().getCalls()) {
                if (call instanceof MethodCallInfo) {
                    final MethodCallInfo methodCall = (MethodCallInfo) call;
                    final MethodInfo callee = methodCall.getCallee();

                    // methodCall��quantifier�𒲍�
                    if (CFGUtility.stateChange(callee)) {
                        final ExpressionInfo qualifier = methodCall.getQualifierExpression();
                        if (qualifier instanceof VariableUsageInfo<?>) {
                            assignments.add(((VariableUsageInfo<?>) qualifier).getUsedVariable());
                        }
                    }

                    for (final MethodInfo overrider : callee.getOverriders()) {
                        if (CFGUtility.stateChange(overrider)) {
                            final ExpressionInfo qualifier = methodCall.getQualifierExpression();
                            if (qualifier instanceof VariableUsageInfo<?>) {
                                assignments.add(((VariableUsageInfo<?>) qualifier)
                                        .getUsedVariable());
                            }
                        }
                    }
                }

                if (call instanceof MethodCallInfo || call instanceof ClassConstructorCallInfo) {
                    // methodCall��parameter�𒲍�
                    final List<ExpressionInfo> arguments = call.getArguments();
                    for (int index = 0; index < arguments.size(); index++) {

                        final CallableUnitInfo callee = call.getCallee();
                        if (CFGUtility.stateChange(callee, index)) {
                            final ExpressionInfo argument = call.getArguments().get(index);
                            if (argument instanceof VariableUsageInfo<?>) {
                                assignments
                                        .add(((VariableUsageInfo<?>) argument).getUsedVariable());
                            }
                        }

                        if (callee instanceof MethodInfo) {
                            for (final MethodInfo overrider : ((MethodInfo) callee).getOverriders()) {
                                if (CFGUtility.stateChange(overrider, index)) {
                                    final ExpressionInfo argument = call.getArguments().get(index);
                                    if (argument instanceof VariableUsageInfo<?>) {
                                        assignments.add(((VariableUsageInfo<?>) argument)
                                                .getUsedVariable());
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        return assignments;

    }

    /**
     * ���̃m�[�h�ŗ��p�i�Q�Ɓj����Ă���ϐ���Set��Ԃ�
     * 
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getVariableUsages()));
    }

    /**
     * ����ΏۂƂȂ肤��ExpressionInfo��Ԃ��D
     * 
     * @return
     */
    abstract ExpressionInfo getDissolvingTarget();

    /**
     * ���������m�[�h���g���čč\�z���s���D
     * 
     * @param requiredExpressions
     * @return
     */
    abstract T makeNewElement(LocalSpaceInfo ownerSpace, ExpressionInfo... requiredExpressions);

    /**
     * ���̃m�[�h���\���v�f��masu���\�z�����f�[�^�����菜���C���������m�[�h�̊j�ɂ������ǉ�����D
     */
    abstract void replace(List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList);

    /**
     * ���̃m�[�h�𕪉����郁�\�b�h�D �������ꂽ�ꍇ�́C������̃m�[�h�Q����Ȃ�CFG��Ԃ��D �������s���Ȃ������ꍇ��null��Ԃ��D
     * 
     * @param nodeFactory
     * @return
     */
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final ExpressionInfo dissolvingTarget = this.getDissolvingTarget();

        // ����Ώۂ��Ȃ��ꍇ�͉������Ȃ��Ŕ�����
        if (null == dissolvingTarget) {
            return null;
        }

        if (dissolvingTarget instanceof ArrayElementUsageInfo) {

            return this.dissolveArrayElementUsage((ArrayElementUsageInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof ArrayInitializerInfo) {

            return this.dissolveArrayInitializer((ArrayInitializerInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof ArrayTypeReferenceInfo) {

            return null;

        } else if (dissolvingTarget instanceof BinominalOperationInfo) {

            return this.dissolveBinominalOperationInfo((BinominalOperationInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof CallInfo<?>) {

            return this.dissolveCall((CallInfo<?>) dissolvingTarget, nodeFactory);

        } else if (dissolvingTarget instanceof CastUsageInfo) {

            return this.dissolveCastUsage((CastUsageInfo) dissolvingTarget, nodeFactory);

        } else if (dissolvingTarget instanceof ClassReferenceInfo) {

            return null;

        } else if (dissolvingTarget instanceof EmptyExpressionInfo) {

            return null;

        } else if (dissolvingTarget instanceof ForeachConditionInfo) {

            return null;

        } else if (dissolvingTarget instanceof LiteralUsageInfo) {

            return null;

        } else if (dissolvingTarget instanceof MonominalOperationInfo) {

            // �P�����Z�q�̃I�y�����h�͕ϐ��g�p�������Ȃ��͂��Ȃ̂ŁC��������K�v���Ȃ��͂�
            return null;

        } else if (dissolvingTarget instanceof NullUsageInfo) {

            return null;

        } else if (dissolvingTarget instanceof ParenthesesExpressionInfo) {

            return this.dissolveParenthesesExpression((ParenthesesExpressionInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof TernaryOperationInfo) {

            return this.dissolveTernaryOperation((TernaryOperationInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof UnknownEntityUsageInfo) {

            return null;

        } else if (dissolvingTarget instanceof VariableUsageInfo<?>) {

            return null;

        } else {
            throw new IllegalStateException("unknown expression type.");
        }
    }

    /**
     * �E�ӂ�ArrayElementUsage�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param arrayElementUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveArrayElementUsage(final ArrayElementUsageInfo arrayElementUsage,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final ExpressionInfo indexExpression = arrayElementUsage.getIndexExpression();
        final ExpressionInfo qualifiedExpression = arrayElementUsage.getQualifierExpression();

        final boolean indexExpressionIsDissolved = CFGUtility.isDissolved(indexExpression);
        final boolean qualifiedExpressionIsDissolved = CFGUtility.isDissolved(qualifiedExpression);

        // indexExpression��qualifiedExpression����������Ȃ��Ƃ��͉��������ɔ�����
        if (!indexExpressionIsDissolved && !qualifiedExpressionIsDissolved) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final LocalSpaceInfo ownerSpace = arrayElementUsage.getOwnerSpace();
        final int fromLine = arrayElementUsage.getFromLine();
        final int toLine = arrayElementUsage.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        if (indexExpressionIsDissolved) {
            this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (qualifiedExpressionIsDissolved) {
            this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(core);
        this.remove();

        // �_�~�[�ϐ��𗘗p����ArrayElementUsageInfo�C����т����p�����V�����v���O�����v�f���쐬
        int index = 0;
        final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                indexExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : indexExpression,
                qualifiedExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : qualifiedExpression, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace,
                newArrayElementUsage);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
    * �E�ӂ�ArrayInitializerInfo�ł��������𕪉����邽�߂̃��\�b�h
    * 
    * @param arrayInitialier
    * @param nodeFactory
    * @return
    */
    private CFG dissolveArrayInitializer(final ArrayInitializerInfo arrayInitializer,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final List<ExpressionInfo> initializers = arrayInitializer.getElementInitializers();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        //�e�C�j�V�����C�U�𕪉����ׂ����`�F�b�N���C�������C��������������V�K�m�[�h���쐬
        final List<ExpressionInfo> newInitializers = new LinkedList<ExpressionInfo>();
        for (final ExpressionInfo initializer : initializers) {

            if (CFGUtility.isDissolved(initializer)) {

                this.makeDissolvedNode(initializer, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);

                newInitializers.add(dissolvedVariableUsageList.getLast());
            }

            else {
                newInitializers.add(initializer);
            }
        }

        // �������ꂽ�C�j�V�����C�U���Ȃ���Ή��������ɔ�����
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(core);
        this.remove();

        // ����O�̕�����K�v�ȏ����擾
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(newInitializers,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace,
                newArrayInitializer);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ� BinominalOperation�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param binominalOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveBinominalOperationInfo(final BinominalOperationInfo binominalOperation,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final ExpressionInfo firstOperand = binominalOperation.getFirstOperand();
        final ExpressionInfo secondOperand = binominalOperation.getFirstOperand();

        final boolean firstOperandIsDissolved = CFGUtility.isDissolved(firstOperand);
        final boolean secondOperandIsDissolved = CFGUtility.isDissolved(secondOperand);

        // �����̕K�v�̂Ȃ��ꍇ�͔�����
        if (!firstOperandIsDissolved && !secondOperandIsDissolved) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        // firstOperand���K�v�ł���Ε���
        if (firstOperandIsDissolved) {
            this.makeDissolvedNode(firstOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // secondOperand���K�v�ł���Ε���
        if (secondOperandIsDissolved) {
            this.makeDissolvedNode(secondOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(core);
        this.remove();

        // �V�����񍀉��Z�I�u�W�F�N�g����ѐV�����v���O�����v�f�𐶐�
        int index = 0;
        final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
                binominalOperation.getOperator(),
                firstOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : firstOperand,
                secondOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : secondOperand,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace,
                newBinominalOperation);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ� CallInfo<?>�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param call
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCall(final CallInfo<? extends CallableUnitInfo> call,
            final ICFGNodeFactory nodeFactory) {

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        //�@�����𕪉�
        final List<ExpressionInfo> newArguments = new ArrayList<ExpressionInfo>();
        for (final ExpressionInfo argument : call.getArguments()) {
            if (CFGUtility.isDissolved(argument)) {
                this.makeDissolvedNode(argument, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newArguments.add(dissolvedVariableUsageList.getLast());
            } else {
                newArguments.add(argument);
            }
        }

        // ���\�b�h�Ăяo���ł���΁CqualifiedExpression�𕪉�
        final ExpressionInfo newQualifiedExpression;
        if (call instanceof MethodCallInfo) {

            final MethodCallInfo methodCall = (MethodCallInfo) call;
            final ExpressionInfo qualifiedExpression = methodCall.getQualifierExpression();
            if (CFGUtility.isDissolved(qualifiedExpression)) {
                this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newQualifiedExpression = dissolvedVariableUsageList.getLast();
            } else {
                newQualifiedExpression = qualifiedExpression;
            }
        } else {
            newQualifiedExpression = null;
        }

        // �z��R���X�g���N�^�ł���΁CindexExpression�𕪉�
        final SortedMap<Integer, ExpressionInfo> newIndexExpressions = new TreeMap<Integer, ExpressionInfo>();
        if (call instanceof ArrayConstructorCallInfo) {

            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            for (final Entry<Integer, ExpressionInfo> entry : arrayConstructorCall
                    .getIndexExpressions().entrySet()) {

                final Integer dimension = entry.getKey();
                final ExpressionInfo indexExpression = entry.getValue();

                if (CFGUtility.isDissolved(indexExpression)) {
                    this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                            dissolvedVariableUsageList);
                    newIndexExpressions.put(dimension, dissolvedVariableUsageList.getLast());
                } else {
                    newIndexExpressions.put(dimension, indexExpression);
                }
            }
        }

        // �������s���Ȃ������ꍇ�͉��������ɔ�����
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �Â��m�[�h���폜
        nodeFactory.removeNode(core);
        this.remove();

        final CallInfo<? extends CallableUnitInfo> newCall;
        if (call instanceof MethodCallInfo) {
            final MethodCallInfo methodCall = (MethodCallInfo) call;
            newCall = new MethodCallInfo(newQualifiedExpression.getType(), newQualifiedExpression,
                    methodCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ClassConstructorCallInfo) {
            final ClassConstructorCallInfo classConstructorCall = (ClassConstructorCallInfo) call;
            newCall = new ClassConstructorCallInfo(classConstructorCall.getType(),
                    classConstructorCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ArrayConstructorCallInfo) {
            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            newCall = new ArrayConstructorCallInfo(arrayConstructorCall.getType(),
                    outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                    CFGUtility.getRandomNaturalValue());

            for (final Entry<Integer, ExpressionInfo> entry : newIndexExpressions.entrySet()) {
                final Integer dimension = entry.getKey();
                final ExpressionInfo newIndexExpression = entry.getValue();
                ((ArrayConstructorCallInfo) newCall).addIndexExpression(dimension,
                        newIndexExpression);
            }

        } else {
            throw new IllegalStateException();
        }

        // ������ǉ�
        for (final ExpressionInfo newArgument : newArguments) {
            newCall.addArgument(newArgument);
        }

        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace, newCall);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ� CastUsageInfo�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param castUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCastUsage(final CastUsageInfo castUsage, final ICFGNodeFactory nodeFactory) {

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        final ExpressionInfo castedUsage = castUsage.getCastedUsage();
        if (CFGUtility.isDissolved(castedUsage)) {
            this.makeDissolvedNode(castedUsage, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �Â��m�[�h���폜
        nodeFactory.removeNode(core);
        this.remove();

        // �V�����񍀉��Z�I�u�W�F�N�g����т�����E�ӂƂ��Ď���������쐬
        final CastUsageInfo newCastUsage = new CastUsageInfo(castUsage.getType(),
                dissolvedVariableUsageList.getFirst(), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace, newCastUsage);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ�ParenthesesExpressionInfo�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param parenthesExpression
     * @param nodeFactory
     * @return
     */
    private CFG dissolveParenthesesExpression(
            final ParenthesesExpressionInfo parenthesesExpression, final ICFGNodeFactory nodeFactory) {

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        final ExpressionInfo innerExpression = parenthesesExpression.getParnentheticExpression();
        if (CFGUtility.isDissolved(innerExpression)) {
            this.makeDissolvedNode(innerExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �Â��m�[�h���폜
        nodeFactory.removeNode(core);
        this.remove();

        // �V�����񍀉��Z�I�u�W�F�N�g����т�����E�ӂƂ��Ď���������쐬
        final ParenthesesExpressionInfo newInnerExpression = new ParenthesesExpressionInfo(
                dissolvedVariableUsageList.getFirst(), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ExecutableElementInfo newElement = this
                .makeNewElement(ownerSpace, newInnerExpression);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ�TernaryOperationInfo�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param ternaryOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveTernaryOperation(final TernaryOperationInfo ternaryOperation,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();

        final ConditionInfo condition = ternaryOperation.getCondition();
        final ExpressionInfo trueExpression = ternaryOperation.getTrueExpression();
        final ExpressionInfo falseExpression = ternaryOperation.getFalseExpression();

        // condition���č\�z
        final IfBlockInfo newIfBlock = new IfBlockInfo(fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        newIfBlock.setOuterUnit(ownerSpace);
        final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(newIfBlock,
                condition, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        newIfBlock.setConditionalClause(newConditionalClause);
        final ElseBlockInfo newElseBlock = new ElseBlockInfo(fromLine, CFGUtility
                .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue(), newIfBlock);
        newElseBlock.setOuterUnit(ownerSpace);
        newIfBlock.setSequentElseBlock(newElseBlock);

        // trueExpression���č\�z
        final ExecutableElementInfo trueElement = this.makeNewElement(newIfBlock, trueExpression);

        // falseExpression���č\�z
        final ExecutableElementInfo falseElement = this.makeNewElement(newElseBlock,
                falseExpression);

        newIfBlock.addStatement((StatementInfo) trueElement); // StatementInfo�ȊO�͂��Ȃ��͂�
        newElseBlock.addStatement((StatementInfo) falseElement); // StatementInfo�ȊO�͂��Ȃ��͂�

        // �Â��m�[�h���폜
        nodeFactory.removeNode(core);
        this.remove();

        // �m�[�h���쐬���C�Ȃ�
        final CFGControlNode conditionNode = nodeFactory.makeControlNode(condition);
        final CFGNode<?> trueNode = nodeFactory.makeNormalNode(trueElement);
        final CFGNode<?> falseNode = nodeFactory.makeNormalNode(falseElement);
        final CFGControlEdge trueEdge = new CFGControlEdge(conditionNode, trueNode, true);
        final CFGControlEdge falseEdge = new CFGControlEdge(conditionNode, falseNode, false);
        conditionNode.addForwardEdge(trueEdge);
        conditionNode.addForwardEdge(falseEdge);
        trueNode.addBackwardEdge(trueEdge);
        falseNode.addBackwardEdge(falseEdge);

        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = backwardEdge.getFromNode();
            final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(conditionNode);
            backwardNode.addForwardEdge(newBackwardEdge);
            conditionNode.addBackwardEdge(newBackwardEdge);
        }
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = forwardEdge.getToNode();
            final CFGEdge newTrueForwardEdge = forwardEdge.replaceFromNode(trueNode);
            final CFGEdge newFalseForwardEdge = forwardEdge.replaceFromNode(falseNode);
            forwardNode.addBackwardEdge(newTrueForwardEdge);
            forwardNode.addBackwardEdge(newFalseForwardEdge);
            trueNode.addForwardEdge(newTrueForwardEdge);
            falseNode.addForwardEdge(newFalseForwardEdge);
        }

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement((StatementInfo) core); // StatementInfo�ȊO�͂��Ȃ��͂�
        ownerSpace.addStatement(newIfBlock);
        ownerSpace.addStatement(newElseBlock);

        // ���������m�[�h�Q����CFG���\�z
        final SimpleCFG newCFG = new SimpleCFG(nodeFactory);
        newCFG.addNode(conditionNode);
        newCFG.setEnterNode(conditionNode);
        newCFG.addNode(trueNode);
        newCFG.addExitNode(trueNode);
        newCFG.addNode(falseNode);
        newCFG.addExitNode(falseNode);

        // ���������m�[�h�ɂ��čċA�I�ɏ���
        final CFG conditionCFG = conditionNode.dissolve(nodeFactory);
        final CFG trueCFG = trueNode.dissolve(nodeFactory);
        final CFG falseCFG = falseNode.dissolve(nodeFactory);

        if (null != conditionCFG) {
            newCFG.removeNode(conditionNode);
            newCFG.addNodes(conditionCFG.getAllNodes());
            newCFG.setEnterNode(conditionCFG.getEnterNode());
        }

        if (null != trueCFG) {
            newCFG.removeNode(trueNode);
            newCFG.addNodes(trueCFG.getAllNodes());
            newCFG.addExitNodes(trueCFG.getExitNodes());
        }

        if (null != falseCFG) {
            newCFG.removeNode(falseNode);
            newCFG.addNodes(falseCFG.getAllNodes());
            newCFG.addExitNodes(falseCFG.getExitNodes());
        }

        return newCFG;
    }

    /**
     * �����ŗ^����ꂽoriginalExpression���E�ӂƂȂ��������쐬����D
     * �쐬�����������CFG�m�[�h��dissolvedNodeList�̍Ō�ɒǉ�����C
     * ������̍��ӂ̕ϐ��g�p�I�u�W�F�N�g��dissolvedVariableUsageList�̍Ō�ɒǉ������D
     * 
     * @param originalExpression
     * @param nodeFactory
     * @param dissolvedNodeList
     * @param dissolvedVariableUsageList
     */
    protected final void makeDissolvedNode(final ExpressionInfo originalExpression,
            final ICFGNodeFactory nodeFactory, final List<CFGNode<?>> dissolvedNodeList,
            final List<LocalVariableUsageInfo> dissolvedVariableUsageList) {

        final LocalSpaceInfo ownerSpace = originalExpression.getOwnerSpace();
        assert null != ownerSpace : "ownerSpace is null!";
        final CallableUnitInfo outerCallableUnit = originalExpression.getOwnerMethod();
        assert null != outerCallableUnit : "outerCallableUnit is null!";
        final int fromLine = originalExpression.getFromLine();
        final int toLine = originalExpression.getToLine();
        final TypeInfo type = originalExpression.getType();

        final LocalVariableInfo dummyVariable = new LocalVariableInfo(Collections
                .<ModifierInfo> emptySet(), getDummyVariableName(), type, ownerSpace, fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo dummyVariableDeclarationStatement = new VariableDeclarationStatementInfo(
                ownerSpace, LocalVariableUsageInfo.getInstance(dummyVariable, false, true,
                        outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                        CFGUtility.getRandomNaturalValue()), originalExpression, fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo.getInstance(
                dummyVariable, true, false, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());

        final CFGNode<?> newNode = nodeFactory.makeNormalNode(dummyVariableDeclarationStatement);
        dissolvedNodeList.add(newNode);
        dissolvedVariableUsageList.add(dummyVariableUsage);
    }

    /**
     * ���������m�[�h�Ȃ��C���̏ꏊ�ɓ����
     * 
     * @param dissolvedNodeList
     */
    protected final void makeEdges(final LinkedList<CFGNode<?>> dissolvedNodeList) {

        // ���������m�[�h���Ȃ�
        for (int i = 1; i < dissolvedNodeList.size(); i++) {
            final CFGNode<?> fromNode = dissolvedNodeList.get(i - 1);
            final CFGNode<?> toNode = dissolvedNodeList.get(i);
            final CFGEdge edge = new CFGNormalEdge(fromNode, toNode);
            fromNode.addForwardEdge(edge);
            toNode.addBackwardEdge(edge);
        }

        // ���̏ꏊ�ɓ����
        {
            final CFGNode<?> firstNode = dissolvedNodeList.getFirst();
            final CFGNode<?> lastNode = dissolvedNodeList.getLast();
            for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
                final CFGNode<?> backwardNode = backwardEdge.getFromNode();
                final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(firstNode);
                backwardNode.addForwardEdge(newBackwardEdge);
                firstNode.addBackwardEdge(newBackwardEdge);
            }
            for (final CFGEdge forwardEdge : this.getForwardEdges()) {
                final CFGNode<?> forwardNode = forwardEdge.getToNode();
                final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(lastNode);
                forwardNode.addBackwardEdge(newForwardEdge);
                lastNode.addForwardEdge(newForwardEdge);
            }
        }
    }

    /**
     * �����ŗ^����ꂽ�m�[�h�Q����CFG���\�z���ĕԂ��D
     * 
     * @param nodeFactory
     * @param dissolvedNodeList
     * @return
     */
    protected final CFG makeCFG(final ICFGNodeFactory nodeFactory,
            LinkedList<CFGNode<?>> dissolvedNodeList) {

        final SimpleCFG cfg = new SimpleCFG(nodeFactory);

        // enterNode��ݒ�
        {
            final CFGNode<?> firstNode = dissolvedNodeList.getFirst();
            final CFG dissolvedCFG = firstNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.setEnterNode(dissolvedCFG.getEnterNode());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.setEnterNode(firstNode);
                cfg.addNode(firstNode);
            }
        }

        // exitNode��ݒ�
        {
            final CFGNode<?> lastNode = dissolvedNodeList.getLast();
            final CFG dissolvedCFG = lastNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addExitNodes(dissolvedCFG.getExitNodes());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addExitNode(lastNode);
                cfg.addNode(lastNode);
            }
        }

        // nodes��ݒ�
        for (int index = 1; index < dissolvedNodeList.size() - 1; index++) {
            final CFGNode<?> node = dissolvedNodeList.get(index);
            final CFG dissolvedCFG = node.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addNode(node);
            }
        }

        return cfg;
    }
}
