package sdl.ist.osaka_u.newmasu.util;

import org.apache.commons.collections15.multimap.MultiHashMap;

/**
 * 1:nの関係を双方向から高速に引き出すためのマップ
 * 
 * @author s-kimura
 */
public class DualMultiMap<T_CALLER, T_CALLEE> {

	private final MultiHashMap<T_CALLER, T_CALLEE> callerMap = new MultiHashMap<T_CALLER, T_CALLEE>();

	public MultiHashMap<T_CALLER, T_CALLEE> getCallerMap() {
		return callerMap;
	}

	private final MultiHashMap<T_CALLEE, T_CALLER> calleeMap = new MultiHashMap<T_CALLEE, T_CALLER>();

	public MultiHashMap<T_CALLEE, T_CALLER> getCalleeMap() {
		return calleeMap;
	}

	public void AddRelation(final T_CALLER caller, final T_CALLEE callee) {
		callerMap.put(caller, callee);
		calleeMap.put(callee, caller);
	}

}
