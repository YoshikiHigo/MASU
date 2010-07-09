package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ModifiersInterpriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JavaPredefinedModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


public class JavaModifiersInterpriter implements ModifiersInterpriter {
    public void interprit(ModifierInfo[] modifiers, Visualizable visualizable,
            StaticOrInstance member) {
        analyze(modifiers);

        if (null != visualizable) {
            //visualizable.setPublicVisible(isPublicAccessible());
            //visualizable.setInheritanceVisible(isInheritanceAccessible());
            //visualizable.setNamespaceVisible(isNameSpaceAccessible());
            //visualizable.setPrivateVibible(isOnlyClassInsideAccessible());
        }

        if (null != member) {
            //member.setInstanceMember(!isStaticMember());
        }
    }

    private void analyze(ModifierInfo[] modifiers) {
        reset();

        for (ModifierInfo info : modifiers) {
            if (info.equals(publicModifier)) {
                this.publicAccess = true;
            } else if (info.equals(protectedModifier)) {
                this.inheritanceAccess = true;
            } else if (info.equals(privateModifier)) {
                this.nameSpaceAccess = false;
            } else if (info.equals(staticModifier)) {
                this.staticMember = true;
            } else if (info.equals(finalModifier)) {
                this.reassignable = false;
            } else if (info.equals(abstractModifier)) {
                this.pureVirtual = true;
            } else if (info.equals(synchronizedModifier)) {
                this.synchronizedAccess = true;
            }
        }

        if (!this.publicAccess && !this.inheritanceAccess && this.nameSpaceAccess) {
            this.inheritanceAccess = true;
        }
    }

    private boolean isInheritanceAccessible() {
        return inheritanceAccess || publicAccess;
    }

    private boolean isNameSpaceAccessible() {
        return nameSpaceAccess || publicAccess;
    }

    private boolean isPublicAccessible() {
        return publicAccess;
    }

    private boolean isModifiable() {
        //Java�͏�Ƀf�[�^���̂̕ҏW����͉\
        return true;
    }

    private boolean isStaticMember() {
        return staticMember;
    }

    private boolean isReassignable() {
        return reassignable;
    }

    private boolean isInheritable() {
        return isReassignable();
    }

    private boolean isOnlyClassInsideAccessible() {
        return !publicAccess && !nameSpaceAccess && !inheritanceAccess;
    }

    private boolean isPureVirtual() {
        return pureVirtual;
    }

    private boolean isSynchronized() {
        return synchronizedAccess;
    }

    private void reset() {
        publicAccess = false;
        nameSpaceAccess = true;
        inheritanceAccess = false;
        staticMember = false;
        reassignable = true;
        pureVirtual = false;
        synchronizedAccess = false;
    }

    private boolean publicAccess = false;

    private boolean nameSpaceAccess = true;

    private boolean inheritanceAccess = false;

    private boolean staticMember = false;

    private boolean reassignable = true;

    private boolean pureVirtual = false;

    private boolean synchronizedAccess = false;

    private static final ModifierInfo finalModifier = JavaPredefinedModifierInfo
            .getModifierInfo("final");

    private static final ModifierInfo publicModifier = JavaPredefinedModifierInfo
            .getModifierInfo("public");

    private static final ModifierInfo protectedModifier = JavaPredefinedModifierInfo
            .getModifierInfo("protected");

    private static final ModifierInfo privateModifier = JavaPredefinedModifierInfo
            .getModifierInfo("private");

    private static final ModifierInfo staticModifier = JavaPredefinedModifierInfo
            .getModifierInfo("static");

    private static final ModifierInfo abstractModifier = JavaPredefinedModifierInfo
            .getModifierInfo("abstract");

    private static final ModifierInfo synchronizedModifier = JavaPredefinedModifierInfo
            .getModifierInfo("synchronized");

}
