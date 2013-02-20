package sdl.ist.osaka_u.newmasu.util;

import org.eclipse.jdt.core.JavaCore;


public enum VERSION {

	JAVA17{
		@Override
		public String getVersion(){
			return JavaCore.VERSION_1_7;
		}
	},

	JAVA16{
		@Override
		public String getVersion(){
			return JavaCore.VERSION_1_6;
		}
	},

	JAVA15{
		@Override
		public String getVersion(){
			return JavaCore.VERSION_1_5;
		}
	},

	JAVA14{
		@Override
		public String getVersion(){
			return JavaCore.VERSION_1_4;
		}
	},

	JAVA13{
		@Override
		public String getVersion(){
			return JavaCore.VERSION_1_3;
		}
	}
	;

	public abstract String getVersion();

}
