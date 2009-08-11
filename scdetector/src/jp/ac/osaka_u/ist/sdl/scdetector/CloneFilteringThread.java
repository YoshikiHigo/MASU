package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.Set;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;


public class CloneFilteringThread implements Runnable {

    public CloneFilteringThread(final Set<ClonePairInfo> clonepairs,
            final Set<ClonePairInfo> refinedClonepairs) {
        this.clonepairs = clonepairs;
        this.refinedClonepairs = refinedClonepairs;
    }

    @Override
    public void run() {
        CLONEPAIR: for (final ClonePairInfo clonePair : this.clonepairs) {

            //���̃N���[���y�A�ɓ�����N���[���y�A����������
            COUNTERCLONEPAIR: for (final ClonePairInfo counterClonePair : this.clonepairs) {

                if (clonePair.subsumedBy(counterClonePair)) {
                    continue CLONEPAIR;
                }

                if (clonePair == counterClonePair) {
                    continue COUNTERCLONEPAIR;
                }
            }

            this.refinedClonepairs.add(clonePair);
        }
    }

    private final Set<ClonePairInfo> clonepairs;

    private final Set<ClonePairInfo> refinedClonepairs;
}
