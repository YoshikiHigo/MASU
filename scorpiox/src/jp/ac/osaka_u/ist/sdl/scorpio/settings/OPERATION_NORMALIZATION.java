package jp.ac.osaka_u.ist.sdl.scorpio.settings;

public enum OPERATION_NORMALIZATION {

    /**
     * 演算をそのまま用いる 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * 演算をその型の正規化する
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     *　演算のキャストを同一の字句に正規化する
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
