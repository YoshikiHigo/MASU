package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.CaughtExceptionDeclarationStatementInfo;


/**
 * catch�߂̗�O����\��CFG�m�[�h
 * @author higo
 *
 */
public class CFGCaughtExceptionNode extends CFGNormalNode<CaughtExceptionDeclarationStatementInfo> {

    CFGCaughtExceptionNode(final CaughtExceptionDeclarationStatementInfo caughtException) {
        super(caughtException);
    }
}
