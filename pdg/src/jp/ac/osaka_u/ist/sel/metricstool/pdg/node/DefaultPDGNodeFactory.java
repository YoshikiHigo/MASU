package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import sdl.ist.osaka_u.newmasu.cfg.node.CFGCaseEntryNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGCaughtExceptionNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGControlNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGEmptyNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGExpressionNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGExpressionStatementNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGForeachControlNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGNormalNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGReturnStatementNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGStatementNode;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

public class DefaultPDGNodeFactory implements IPDGNodeFactory {

	private final ConcurrentMap<ExecutableElementInfo, PDGNode<?>> elementToNodeMap;

	public DefaultPDGNodeFactory() {
		this.elementToNodeMap = new ConcurrentHashMap<ExecutableElementInfo, PDGNode<?>>();
	}

	@Override
	public synchronized PDGControlNode makeControlNode(
			final CFGControlNode cfgNode) {

		if (null == cfgNode) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo core = cfgNode.getCore();
		PDGControlNode node = (PDGControlNode) this.getNode(core);
		if (null != node) {
			return node;
		}

		if (cfgNode instanceof CFGForeachControlNode) {
			node = new PDGForeachControlNode((CFGForeachControlNode) cfgNode);
		}

		else {
			node = new PDGControlNode((CFGControlNode) cfgNode);
		}

		this.elementToNodeMap.put(core, node);

		return node;
	}

	@Override
	public synchronized PDGNormalNode<?> makeNormalNode(
			final CFGNormalNode<?> cfgNode) {

		if (null == cfgNode) {
			throw new IllegalArgumentException();
		}

		final ExecutableElementInfo core = cfgNode.getCore();
		PDGNormalNode<?> node = (PDGNormalNode<?>) this.getNode(core);
		if (null != node) {
			return node;
		}

		if (cfgNode instanceof CFGCaseEntryNode) {
			node = new PDGCaseEntryNode((CFGCaseEntryNode) cfgNode);
		}

		else if (cfgNode instanceof CFGExpressionNode) {
			node = new PDGExpressionNode((CFGExpressionNode) cfgNode);
		}

		else if (cfgNode instanceof CFGCaughtExceptionNode) {
			node = new PDGCaughtExceptionNode((CFGCaughtExceptionNode) cfgNode);
		}

		else if (cfgNode instanceof CFGEmptyNode) {
			node = new PDGEmptyNode((CFGEmptyNode) cfgNode);
		}

		else if (cfgNode instanceof CFGExpressionStatementNode) {
			node = new PDGExpressionStatementNode(
					(CFGExpressionStatementNode) cfgNode);
		}

		else if (cfgNode instanceof CFGStatementNode<?>) {

			if (cfgNode instanceof CFGReturnStatementNode) {
				node = new PDGReturnStatementNode(
						(CFGReturnStatementNode) cfgNode);
			}

			else {
				node = new PDGStatementNode<CFGStatementNode<?>>(
						(CFGStatementNode<?>) cfgNode);
			}
		}

		else {
			throw new IllegalStateException();
		}

		this.elementToNodeMap.put(core, node);

		return node;
	}

	@Override
	public PDGNode<?> getNode(final ExecutableElementInfo element) {

		if (null == element) {
			throw new IllegalArgumentException();
		}

		return this.elementToNodeMap.get(element);
	}

	@Override
	public SortedSet<PDGNode<?>> getAllNodes() {
		final SortedSet<PDGNode<?>> nodes = new TreeSet<PDGNode<?>>();
		nodes.addAll(this.elementToNodeMap.values());
		return nodes;
	}

	@Override
	public synchronized void addNode(final PDGNode<?> node) {

		if (null == node) {
			throw new IllegalArgumentException();
		}

		final ExecutableElementInfo element = node.getCore();
		this.elementToNodeMap.put(element, node);
	}

	@Override
	public synchronized void addNodes(final Collection<PDGNode<?>> nodes) {

		if (null == nodes) {
			throw new IllegalArgumentException();
		}

		for (final PDGNode<?> node : nodes) {
			this.addNode(node);
		}
	}

	@Override
	public synchronized void removeNode(ExecutableElementInfo element) {

		if (null == element) {
			throw new IllegalArgumentException();
		}

		this.elementToNodeMap.remove(element);
	}

	public int getNodeCount() {
		return this.elementToNodeMap.size();
	}
}
