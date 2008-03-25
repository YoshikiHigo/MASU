package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;

/**
 * ������ static initializer ��\���N���X
 * 
 * @author t-miyake
 */
public class UnresolvedStaticInitializerInfo extends UnresolvedLocalSpaceInfo {

    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super();
        
        if(null == ownerClass) {
            throw new IllegalArgumentException("ownerClass is null");
        }
        
        this.ownerClass = ownerClass;
    }

    @Override
    public UnitInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
        // TODO �����������ꂽ���\�b�h�E�X�^�u
        return null;
    }
    
    public final UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }
    
    private final UnresolvedClassInfo ownerClass;

}
