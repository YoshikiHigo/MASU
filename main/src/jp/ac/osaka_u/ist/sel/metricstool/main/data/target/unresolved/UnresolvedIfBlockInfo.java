package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������if�u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedIfBlockInfo extends UnresolvedConditionalBlockInfo<IfBlockInfo> {

    /**
     * if �u���b�N����������
     */
    public UnresolvedIfBlockInfo(final UnresolvedLocalSpaceInfo<?> ownerSpace) {
        super(ownerSpace);

        this.sequentElseBlock = null;
    }

    /**
     * ���̖����� if �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManger �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public IfBlockInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // ���� if�� �̏����߂���������
        final UnresolvedConditionalClauseInfo unresolvedConditionalClause = this
                .getConditionalClause();
        final ConditionalClauseInfo conditionalClause = unresolvedConditionalClause.resolveUnit(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // ���� if���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new IfBlockInfo(usingClass, usingMethod, conditionalClause, fromLine,
                fromColumn, toLine, toColumn);

        // ����else�u���b�N������ꍇ�͉�������
        if (this.hasElseBlock()) {
            final UnresolvedElseBlockInfo unresolvedElseBlockInfo = this.getSequentElseBlock();
            final ElseBlockInfo sequentBlockInfo = unresolvedElseBlockInfo.resolveUnit(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.setSequentElseBlock(sequentBlockInfo);
        }

        //�@�����u���b�N�����������C�����ς�if�u���b�N�I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedBlockInfo<?> unresolvedInnerBlock : this.getInnerBlocks()) {
            final BlockInfo innerBlock = unresolvedInnerBlock.resolveUnit(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInnerBlock(innerBlock);
        }

        // ���[�J���ϐ������������C�����ς�if�u���b�N�I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolveUnit(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        return this.resolvedInfo;
    }

    /**
     * �Ή�����else�u���b�N��Ԃ�
     * @return �Ή�����else�u���b�N�D�Ή�����else�u���b�N�����݂��Ȃ��ꍇ��null
     */
    public UnresolvedElseBlockInfo getSequentElseBlock() {
        return this.sequentElseBlock;
    }

    /**
     * �Ή�����else�u���b�N���Z�b�g����
     * @param elseBlock �Ή�����else�u���b�N
     */
    public void setSequentElseBlock(UnresolvedElseBlockInfo elseBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == elseBlock) {
            throw new IllegalArgumentException("elseBlock is null");
        }

        this.sequentElseBlock = elseBlock;
    }

    /**
     * �Ή�����else�u���b�N�����݂��邩�ǂ����\��
     * @return �Ή�����else�u���b�N�����݂���Ȃ�true
     */
    public boolean hasElseBlock() {
        return null != this.sequentElseBlock;
    }

    /**
     * �Ή�����else�u���b�N��ۑ�����ϐ�
     */
    private UnresolvedElseBlockInfo sequentElseBlock;

}
