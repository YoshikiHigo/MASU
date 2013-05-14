package sdl.ist.osaka_u.newmasu.cfg.node;

import java.util.List;

import sdl.ist.osaka_u.newmasu.cfg.CaughtExceptionDeclarationStatementInfo;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;

/**
 * catch�߂̗�O����\��CFG�m�[�h
 * 
 * @author higo
 * 
 */
public class CFGCaughtExceptionNode extends
		CFGNormalNode<CaughtExceptionDeclarationStatementInfo> {

	CFGCaughtExceptionNode(
			final CaughtExceptionDeclarationStatementInfo caughtException) {
		super(caughtException);
	}

	@Override
	final ExpressionInfo getDissolvingTarget() {
		return null;
	}

	@Override
	CaughtExceptionDeclarationStatementInfo makeNewElement(
			final LocalSpaceInfo ownerSpace, final int fromLine,
			final int fromColumn, final int toLine, final int toColumn,
			final ExpressionInfo... requiredExpressions) {
		return null;
	}

	@Override
	CaughtExceptionDeclarationStatementInfo makeNewElement(
			final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {
		return null;
	}
}
